package ynn.tech.algorithms.network.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import ynn.network.adapter.NetworkAdapter;
import ynn.network.model.NetworkModel;
import ynn.network.model.Node;
import ynn.network.ui.AbstractShape;
import ynn.network.ui.ConnectorsEvent;
import ynn.network.ui.INetworkViewListener;
import ynn.network.ui.NetworkView;
import ynn.network.ui.NetworkView.Mode;
import ynn.network.ui.NodeShape;
import ynn.network.ui.NodesEvent;
import ynn.network.ui.SelectionChangedEvent;
import ynn.network.util.NetworkSerializer;
import ynn.tech.algorithms.network.AlgorithmDescriptor;
import ynn.tech.algorithms.network.AlgorithmExecuter;
import ynn.tech.algorithms.network.CommandStack;
import ynn.tech.algorithms.network.aky90.Aky90Descriptor;
import ynn.tech.algorithms.network.ewd426.EWD426Descriptor;
import ynn.tech.algorithms.network.ui.attributes.AttributesView;
import ynn.tech.algorithms.network.ui.icons.Icons;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JMenuBar _menuBar;
	private JMenu _fileMenu;
	private JMenuItem _fileMenuExit;
	private JMenuItem _fileMenuOpen;
	private JMenuItem _fileMenuSave;
	private JMenuItem _fileMenuSaveAs;
	private JMenuItem _fileMenuNew;
	private JMenu _editMenu;
	private JCheckBoxMenuItem _editMenuMode;
	private JRadioButtonMenuItem _editMenuMove;
	private JRadioButtonMenuItem _editMenuConnect;
	private JMenuItem _editMenuInsertNode;
	private JMenuItem _editMenuDelete;
	private JMenu _playMenu;
	private JMenuItem _playMenuNext;
	private JMenuItem _playMenuPrev;
	private JMenu _helpMenu;
	private JMenuItem _helpMenuAbout;
	
	private JPanel _toolBarsPanel;
	private JToolBar _toolBarFile;
	private JButton _toolBarFileNew;
	private JButton _toolBarFileOpen;
	private JButton _toolBarFileSave;
	private JToolBar _toolBarEdit;
	private JToggleButton _toolBarEditMode;
	private JButton _toolBarEditInsert;
	private JButton _toolBarEditDelete;
	private JComboBox _toolBarEditModeType;
	private ComboBoxLabelItemModel _toolBarEditModeTypeMove;
	private ComboBoxLabelItemModel _toolBarEditModeTypeConnect;
	private JToolBar _toolBarPlay;
	private JButton _toolBarPlayNext;
	private JButton _toolBarPlayPrev;
	private JLabel _toolBarPlayTime;

	private JPanel _contentPanel;
	private JSplitPane _splitter;
	private JTextPane _console;
	private AttributesView _attributesView;
	private JTabbedPane _tabbedPane;
	
    private NetworkView _networkView;
    private NetworkModel _networkModel;
    private NetworkAdapter _networkAdapter;
    private AlgorithmExecuter _algExececuter = null;
    private CommandStack _commands;
    
    private int _nodeId = 1;
	private File _currentFile = null;
    private AlgorithmDescriptor[] _algDescriptors = new AlgorithmDescriptor[] {
    	new Aky90Descriptor(),
    	new EWD426Descriptor()
    };
    private AlgorithmDescriptor _algDescriptor = null;

	public MainWindow()
	{
		super("Network Algorithms Simulator");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(800, 600);
        setLocationRelativeTo(null);
        setIconImage(Icons.getNetwork());
		initializeControls();
		_commands = new CommandStack();
		writeToConsole(
			"Welcome!\n" +
			"In order to start, please create a new network " +
			"or open an existing one from the file system.");
	}
	
	// #######################################################################
	// ##########               initialization methods              ##########
	// #######################################################################
	
	private void initializeControls()
	{
		_contentPanel = new JPanel();
		_contentPanel.setLayout(new BorderLayout());
		_contentPanel.setPreferredSize(new Dimension(10,10));
		initializeTabs();
		_splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT,true,_contentPanel,_tabbedPane);
		setContentPane(_splitter);
		addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentShown(ComponentEvent e)
			{
				_splitter.setDividerLocation(0.7);
			}
		});
		initializeMenu();
		initializeToolBar();
		enableEdit(false);
		enablePlay(false);
		enableSave(false);
	}

	private void initializeTabs()
	{
		_tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		// Console Tab
		_console = new JTextPane();
		_console.setText("");
		_console.setBorder(BorderFactory.createLoweredBevelBorder());
		_console.setEditable(false);
		_console.setAutoscrolls(true);
		_console.setFont((new JList()).getFont());
		_tabbedPane.addTab("Console", Icons.getConsole(), new JScrollPane(_console));
		// Attributes Tab
		_attributesView = new AttributesView();
		_attributesView.addErrorListener(new ErrorOccurredListener()
		{
			@Override
			public void errorOccurred(Object source, Throwable ex, String message)
			{
				handleErrorOccured(source,ex,message);
			}
		});
		_tabbedPane.addTab("Attributes", Icons.getClipboard(), _attributesView);
	}

	private void initializeNetworkView()
	{
		if (_networkView != null) _contentPanel.remove(_networkView);
		_networkModel = null;
        _networkView = null;
        _networkAdapter = null;
        _networkModel = new NetworkModel();
        _networkView = new NetworkView();
        _networkView.setBackground(Color.WHITE);
        _networkAdapter = new NetworkAdapter();
        _networkAdapter.attach(_networkModel);
        _networkAdapter.attach(_networkView);
        _contentPanel.add(_networkView,BorderLayout.CENTER);
        _networkView.addListener(new INetworkViewListener()
		{
			@Override
			public void selectionChanged(SelectionChangedEvent e)
			{
				AbstractShape shape = e.getFirstNewSelection();
				Node node = null;
				if (shape instanceof NodeShape)
				{
					Object data = ((NodeShape)shape).getData();
					if (data instanceof Node) node = (Node)data;
				}
				_attributesView.setNodeAttributes(node);
			}
			@Override
			public void nodesChanged(NodesEvent e) {}
			@Override
			public void connectorsChanged(ConnectorsEvent e) {}
		});
	}

	private void initializeToolBar()
	{
		_toolBarsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
		_toolBarsPanel.setBorder(BorderFactory.createEtchedBorder());
		initializeFileToolBar();
		initializeEditToolBar();
		initializePlayToolBar();
        _contentPanel.add(_toolBarsPanel,BorderLayout.NORTH);
	}
	
	private void initializeFileToolBar()
	{
		_toolBarFile = new JToolBar();
		
		_toolBarFileNew = new JButton(Icons.getNew());
		_toolBarFileNew.setToolTipText("New File");
		_toolBarFileNew.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onNew();
			}
		});
		_toolBarFile.add(_toolBarFileNew);

		_toolBarFileOpen = new JButton(Icons.getOpen());
		_toolBarFileOpen.setToolTipText("Open File");
		_toolBarFileOpen.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onOpen();
			}
		});
		_toolBarFile.add(_toolBarFileOpen);

		_toolBarFileSave = new JButton(Icons.getSave());
		_toolBarFileSave.setToolTipText("Save File");
		_toolBarFileSave.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onSave();
			}
		});
		_toolBarFile.add(_toolBarFileSave);
		
		_toolBarsPanel.add(_toolBarFile);
	}
	
	private void initializeEditToolBar()
	{
		_toolBarEdit = new JToolBar();
		
		_toolBarEditMode = new JToggleButton(Icons.getEditMode());
		_toolBarEditMode.setSelected(true);
		_toolBarEditMode.setToolTipText("Switch To View Mode");
		_toolBarEditMode.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onAllowEditChanged(_toolBarEditMode.isSelected());
			}
		});
		_toolBarEdit.add(_toolBarEditMode);
		
		_toolBarEdit.addSeparator();

		_toolBarEditInsert = new JButton(Icons.getInsert());
		_toolBarEditInsert.setToolTipText("Insert New Node");
		_toolBarEditInsert.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onInsertNewNode();
			}
		});
		_toolBarEdit.add(_toolBarEditInsert);

		_toolBarEditDelete = new JButton(Icons.getDelete());
		_toolBarEditDelete.setToolTipText("Delete Selected Shape(s)");
		_toolBarEditDelete.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onDeleteSelectedShapes();
			}
		});
		_toolBarEdit.add(_toolBarEditDelete);
		
		_toolBarEditModeType = new JComboBox();
		_toolBarEditModeType.setRenderer(new ComboBoxLabelRenderer(false));
		_toolBarEditModeTypeMove = new ComboBoxLabelItemModel()
		{	
			@Override
			public Object getValue() { return Mode.Move; }
			
			@Override
			public String getToolTip() { return "Move Elements"; }
			
			@Override
			public String getText() { return "Move"; }
			
			@Override
			public Icon getIcon() { return Icons.getEditModeMove(); }
		};
		_toolBarEditModeType.addItem(_toolBarEditModeTypeMove);
		_toolBarEditModeTypeConnect = new ComboBoxLabelItemModel()
		{	
			@Override
			public Object getValue() { return Mode.Connect; }
			
			@Override
			public String getToolTip() { return "Connect Elements"; }
			
			@Override
			public String getText() { return "Connect"; }
			
			@Override
			public Icon getIcon() { return Icons.getEditModeConnect(); }
		};
		_toolBarEditModeType.addItem(_toolBarEditModeTypeConnect);
		_toolBarEditModeType.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Object value = _toolBarEditModeType.getSelectedItem();
				if (value instanceof ComboBoxLabelItemModel)
				{
					onEditModeSelected((Mode)((ComboBoxLabelItemModel)value).getValue());
				}
			}
		});
		_toolBarEdit.add(_toolBarEditModeType);

		_toolBarsPanel.add(_toolBarEdit);
	}
	
	private void initializePlayToolBar()
	{
		_toolBarPlay = new JToolBar();
		
		_toolBarPlayPrev = new JButton(Icons.getUndo());
		_toolBarPlayPrev.setToolTipText("Undo Step");
		_toolBarPlayPrev.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onSimulationStepBack();
			}
		});
		_toolBarPlay.add(_toolBarPlayPrev);
		
//		_toolBarPlayStop = new JButton(Icons.getStop());
//		_toolBarPlayStop.setToolTipText("Stop Simulation");
//		_toolBarPlayStop.addActionListener(new ActionListener()
//		{	
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				onStopSimulation();
//			}
//		});
//		_toolBarPlay.add(_toolBarPlayStop);
		
//		_toolBarPlayStart = new JButton(Icons.getPlay());
//		_toolBarPlayStart.setToolTipText("Play Simulation");
//		_toolBarPlayStart.addActionListener(new ActionListener()
//		{	
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				onStartSimulation();
//			}
//		});
//		_toolBarPlay.add(_toolBarPlayStart);
		
		_toolBarPlayNext = new JButton(Icons.getStep());
		_toolBarPlayNext.setToolTipText("Step Forward");
		_toolBarPlayNext.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onSimulationNextStep();
			}
		});
		_toolBarPlay.add(_toolBarPlayNext);
		
		_toolBarPlayTime = new JLabel();
		_toolBarPlayTime.setBorder(BorderFactory.createEtchedBorder());
		updateSimulationTime();
		_toolBarPlay.add(_toolBarPlayTime);
		
		_toolBarsPanel.add(_toolBarPlay);
	}

	private void initializeMenu()
	{
		_menuBar = new JMenuBar();
		initializeFileMenu();
		initializeEditMenu();
		initializePlayMenu();
		initializeHelpMenu();
		setJMenuBar(_menuBar);
	}

	private void initializeFileMenu()
	{
		// File
		_fileMenu = new JMenu("File");
		_fileMenu.setMnemonic(KeyEvent.VK_F);
		_menuBar.add(_fileMenu);
		// File -> New
		_fileMenuNew = new JMenuItem("New", KeyEvent.VK_N);
		_fileMenuNew.setIcon(Icons.getNew());
		_fileMenuNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
		_fileMenuNew.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onNew();
			}
		});
		_fileMenu.add(_fileMenuNew);
		// File -> Open
		_fileMenuOpen = new JMenuItem("Open", KeyEvent.VK_O);
		_fileMenuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
		_fileMenuOpen.setIcon(Icons.getOpen());
		_fileMenuOpen.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onOpen();
			}
		});
		_fileMenu.add(_fileMenuOpen);
		// File -> Save
		_fileMenuSave = new JMenuItem("Save", KeyEvent.VK_S);
		_fileMenuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));
		_fileMenuSave.setIcon(Icons.getSave());
		_fileMenuSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onSave();
			}
		});
		_fileMenu.add(_fileMenuSave);
		// File -> Save As
		_fileMenuSaveAs = new JMenuItem("Save As", KeyEvent.VK_A);
		_fileMenuSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
		_fileMenuSaveAs.setIcon(Icons.getSave());
		_fileMenuSaveAs.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onSaveAs(null);
			}
		});
		_fileMenu.add(_fileMenuSaveAs);
		// File -> -----
		_fileMenu.add(new JSeparator());
		// File -> Exit
		_fileMenuExit = new JMenuItem("Exit", KeyEvent.VK_X);
		_fileMenuExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.ALT_MASK));
		_fileMenuExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onExit();
			}
		});
		_fileMenu.add(_fileMenuExit);
	}
	
	private void initializeEditMenu()
	{
		// Edit
		_editMenu = new JMenu("Edit");
		_editMenu.setMnemonic(KeyEvent.VK_E);
		_menuBar.add(_editMenu);
		// Edit -> Allow Edit
		_editMenuMode = new JCheckBoxMenuItem("Allow Edit");
		_editMenuMode.setSelected(true);
		_editMenuMode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.ALT_MASK));
		_editMenuMode.setIcon(Icons.getEditMode());
		_editMenuMode.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onAllowEditChanged(_editMenuMode.isSelected());
			}
		});
		_editMenu.add(_editMenuMode);
		// Edit -> -----
		_editMenu.add(new JSeparator());
		// Edit -> Move
		ButtonGroup group = new ButtonGroup();
		_editMenuMove = new JRadioButtonMenuItem("Move");
		_editMenuMove.setSelected(true);
		_editMenuMove.setMnemonic(KeyEvent.VK_M);
		_editMenuMove.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.ALT_MASK));
		_editMenuMove.setIcon(Icons.getEditModeMove());
		_editMenuMove.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onEditModeSelected(Mode.Move);
			}
		});
		group.add(_editMenuMove);
		_editMenu.add(_editMenuMove);
		// Edit -> Connect
		_editMenuConnect = new JRadioButtonMenuItem("Connect");
		_editMenuConnect.setMnemonic(KeyEvent.VK_C);
		_editMenuConnect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.ALT_MASK));
		_editMenuConnect.setIcon(Icons.getEditModeConnect());
		_editMenuConnect.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onEditModeSelected(Mode.Connect);
			}
		});
		group.add(_editMenuConnect);
		_editMenu.add(_editMenuConnect);
		// Edit -> -----
		_editMenu.add(new JSeparator());
		// Edit -> Insert Node
		_editMenuInsertNode = new JMenuItem("Insert", KeyEvent.VK_I);
		_editMenuInsertNode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0));
		_editMenuInsertNode.setIcon(Icons.getInsert());
		_editMenuInsertNode.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onInsertNewNode();
			}
		});
		_editMenu.add(_editMenuInsertNode);
		// Edit -> Delete
		_editMenuDelete = new JMenuItem("Delete", KeyEvent.VK_D);
		_editMenuDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		_editMenuDelete.setIcon(Icons.getDelete());
		_editMenuDelete.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onDeleteSelectedShapes();
			}
		});
		_editMenu.add(_editMenuDelete);
	}

	private void initializePlayMenu()
	{
		// Play
		_playMenu = new JMenu("Simulation");
		_playMenu.setMnemonic(KeyEvent.VK_P);
		_menuBar.add(_playMenu);
		// Play -> Start
//		_playMenuStart = new JMenuItem("Start", KeyEvent.VK_S);
//		_playMenuStart.setIcon(Icons.getPlay());
//		_playMenuStart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
//		_playMenuStart.addActionListener(new ActionListener()
//		{
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				onStartSimulation();
//			}
//		});
//		_playMenu.add(_playMenuStart);
		// Play -> Stop
//		_playMenuStop = new JMenuItem("Stop", KeyEvent.VK_T);
//		_playMenuStop.setIcon(Icons.getStop());
//		_playMenuStop.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, KeyEvent.CTRL_MASK));
//		_playMenuStop.addActionListener(new ActionListener()
//		{
//			@Override
//			public void actionPerformed(ActionEvent e)
//			{
//				onStopSimulation();
//			}
//		});
//		_playMenu.add(_playMenuStop);
		// Play -> Next
		_playMenuNext = new JMenuItem("Next", KeyEvent.VK_N);
		_playMenuNext.setIcon(Icons.getStep());
		_playMenuNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
		_playMenuNext.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onSimulationNextStep();
			}
		});
		_playMenu.add(_playMenuNext);
		// Play -> Back
		_playMenuPrev = new JMenuItem("Back", KeyEvent.VK_B);
		_playMenuPrev.setIcon(Icons.getUndo());
		_playMenuPrev.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		_playMenuPrev.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onSimulationStepBack();
			}
		});
		_playMenu.add(_playMenuPrev);
	}

	private void initializeHelpMenu()
	{
		// Help
		_helpMenu = new JMenu("Help");
		_helpMenu.setMnemonic(KeyEvent.VK_H);
		_menuBar.add(_helpMenu);
		// Help -> About
		_helpMenuAbout = new JMenuItem("About", KeyEvent.VK_A);
		_helpMenuAbout.setIcon(Icons.getAbout());
		_helpMenuAbout.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				onAbout();
			}
		});
		_helpMenu.add(_helpMenuAbout);
	}

	private void enableEdit(boolean enabled)
	{
		_editMenu.setEnabled(enabled);
		_toolBarEditMode.setEnabled(enabled);
		_toolBarEditInsert.setEnabled(enabled);
		_toolBarEditDelete.setEnabled(enabled);
		_toolBarEditModeType.setEnabled(enabled);
	}

	private void enablePlay(boolean enabled)
	{
		_playMenu.setEnabled(enabled);
		_toolBarPlayNext.setEnabled(enabled);
		_toolBarPlayPrev.setEnabled(enabled);
	}
	
	private void enableSave(boolean enabled)
	{
		_fileMenuSave.setEnabled(enabled);
		_fileMenuSaveAs.setEnabled(enabled);
		_toolBarFileSave.setEnabled(enabled);
	}

	private void updateSimulationTime()
	{
		int time = getSimulationTime();
		_toolBarPlayTime.setText(String.format("t = %d", time));
	}
	
	private int getSimulationTime()
	{
		return _algExececuter == null ? 0 : _algExececuter.getTime();
	}
	
	private void writeToConsole(String[] strings, boolean isError)
	{
		Color c = isError ? Color.RED : Color.BLACK;
		StyleContext sc = StyleContext.getDefaultStyleContext();
	    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
	        StyleConstants.Foreground, c);
	    _console.setCharacterAttributes(aset, false);
		if (strings != null)
			for (String s : strings) 
			{
				_console.setCaretPosition(_console.getDocument().getLength());
			    _console.setEditable(true);
				_console.replaceSelection(s + "\n");
			    _console.setEditable(false);
			}
		else _console.setText("");
	}
	
	private void writeToConsole(String[] strings)
	{
		writeToConsole(strings,false);
	}
	
	private void writeToConsole(String message)
	{
		writeToConsole(new String[] { message });
	}
	
	private void writeToConsole(String message, boolean isError)
	{
		writeToConsole(new String[] { message }, isError);
	}
	
	private void writeToConsole(Throwable e)
	{
		String prefix = "Error occurred: ";
		while (e != null)
		{
			writeToConsole(prefix + e.toString(),true);
			prefix = "Caused by: ";
			e = e.getCause();
		}
	}
	
	private void clearConsole()
	{
		writeToConsole((String[])null);
	}

	protected void handleErrorOccured(Object source, Throwable ex, String message)
	{
		writeToConsole(message,true);
		writeToConsole(ex);
		StringBuilder sb = new StringBuilder();
		sb.append(message);
		if (ex != null)
		{
			while (ex != null)
			{
				sb.append("\nCaused by: ");
				sb.append(ex);
				ex = ex.getCause();
			}
		}
		JOptionPane.showMessageDialog(
			this, sb.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	}

	// #######################################################################
	// ##########                 on event methods                  ##########
	// #######################################################################
	
	private void setEditMode()
	{
		enableEdit(true);
		enableSave(true);
		enablePlay(false);
	}

	private void onNew()
	{
		NewNetworkDialog dialog = new NewNetworkDialog(this, _algDescriptors);
		AlgorithmDescriptor descriptor = dialog.showDialog();
		if (descriptor != null)
		{
			_nodeId = 1;
			_algDescriptor = descriptor;
			initializeNetworkView();
			_algExececuter = new AlgorithmExecuter(_algDescriptor, _networkModel, _commands);
			updateSimulationTime();
			validate();
			setEditMode();
			onAllowEditChanged(true);
			onEditModeSelected(Mode.Move);
			clearConsole();
		}
	}

	private void onOpen()
	{
		File file = null;
		try
		{
			JFileChooser dialog = new JFileChooser(_currentFile);
			dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
			dialog.setMultiSelectionEnabled(false);
			dialog.setAcceptAllFileFilterUsed(false);
			dialog.setFileFilter(createFileFilter());
			if (dialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			{
				file = dialog.getSelectedFile();
				initializeNetworkView();
				validate();
				NetworkSerializer serializer = new NetworkSerializer(_networkView);
				serializer.deserialize(file);
				_currentFile = file;
				setEditMode();
				onAllowEditChanged(true);
				onEditModeSelected(Mode.Move);
				validate();
				clearConsole();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			writeToConsole(ex);
			JOptionPane.showMessageDialog(this, 
				String.format(
					"Error occured while trying to load from file \"%s\":\n%s",
					file, ex.getMessage()), 
				"Error", JOptionPane.ERROR_MESSAGE);
			enableEdit(false);
			enableSave(false);
			enablePlay(false);
			_algDescriptor = null;
		}
	}

	private void onSave()
	{		
		onSaveAs(_currentFile);
	}

	private void onSaveAs(File file)
	{
		try
		{
			if (file == null)
			{
				JFileChooser dialog = new JFileChooser(_currentFile);
				dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
				dialog.setMultiSelectionEnabled(false);
				dialog.setAcceptAllFileFilterUsed(false);
				dialog.setDialogTitle("Save As");
				dialog.setFileFilter(createFileFilter());
				if (dialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				{
					file = dialog.getSelectedFile();
				}
			}
			if (file != null)
			{
				NetworkSerializer serializer = new NetworkSerializer(_networkView);
				serializer.serialize(file);
				_currentFile = file;
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			writeToConsole(ex);
			JOptionPane.showMessageDialog(this, 
				String.format(
					"Error occured while trying to save to file \"%s\":\n%s",
					file, ex.getMessage()), 
				"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onExit()
	{
		processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	protected void onAllowEditChanged(boolean allow)
	{
		if (allow)
		{
			if (_editMenuConnect.isSelected()) _networkView.setMode(Mode.Connect);
			else _networkView.setMode(Mode.Move);
			_toolBarEditMode.setToolTipText("Switch To View Mode");
		}
		else
		{
			_networkView.setMode(Mode.View);
			_toolBarEditMode.setToolTipText("Switch To Edit Mode");
		}
		if (_editMenuMode.isSelected() != allow) _editMenuMode.setSelected(allow);
		if (_toolBarEditMode.isSelected() != allow) _toolBarEditMode.setSelected(allow);
		
		_editMenuMove.setEnabled(allow);
		_editMenuConnect.setEnabled(allow);
		_editMenuInsertNode.setEnabled(allow);
		_editMenuDelete.setEnabled(allow);
		_toolBarEditDelete.setEnabled(allow);
		_toolBarEditInsert.setEnabled(allow);
		_toolBarEditModeType.setEnabled(allow);
		enablePlay(!allow);
		_attributesView.setEnabled(allow);
	}

	protected void onEditModeSelected(Mode mode)
	{
		_networkView.setMode(mode);
		switch (mode)
		{
		case Move:
			_editMenuMove.setSelected(true);
			_toolBarEditModeType.setSelectedItem(_toolBarEditModeTypeMove);
			break;
		case Connect:
			_editMenuConnect.setSelected(true);
			_toolBarEditModeType.setSelectedItem(_toolBarEditModeTypeConnect);
			break;
		}
	}
	
	protected void onInsertNewNode()
	{
		NodeShape shape = new NodeShape();
		shape.move(5*(_nodeId-1), 5*(_nodeId-1));
		shape.setText(String.valueOf(_nodeId++));
		Node node = _algDescriptor.getUtilities().createNode();
		node.setName(shape.getText());
		_algDescriptor.getUtilities().initNodeAttributes(node);
		shape.setData(node);
		_networkView.addShape(shape);
	}
	
	protected void onDeleteSelectedShapes()
	{
		_networkView.removeSelectedShape();
	}
	
	private void onSimulationNextStep()
	{
		if (_algExececuter != null)
		{
			String[] strings = _algExececuter.stepForward();
			updateSimulationTime();
			writeToConsole("Step at t = " + getSimulationTime());
			writeToConsole(strings);
			_networkView.repaint();
		}
	}

	private void onSimulationStepBack()
	{
		if (_algExececuter != null)
		{
			_algExececuter.stepBack();
			writeToConsole("Undo step, back to t = " + getSimulationTime());
			updateSimulationTime();
			_networkView.repaint();
		}
	}
	
	private void onAbout()
	{
		// TODO
	}
	
	// #######################################################################
	// ##########                  Utility methods                  ##########
	// #######################################################################
	
	private FileFilter createFileFilter()
	{
		return new FileFilter()
		{
			@Override
			public String getDescription()
			{
				return "Network Model";
			}
			@Override
			public boolean accept(File file)
			{
				return file.isDirectory() || file.getName().toLowerCase().endsWith(".nas");
			}
		};
	}
}
