package ynn.tech.algorithms.network.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import ynn.network.adapter.NetworkAdapter;
import ynn.network.model.NetworkModel;
import ynn.network.model.Node;
import ynn.network.ui.NetworkView;
import ynn.network.ui.NetworkView.Mode;
import ynn.network.ui.NodeShape;
import ynn.network.util.NetworkSerializer;
import ynn.tech.algorithms.network.AlgorithmDescriptor;
import ynn.tech.algorithms.network.aky90.Aky90Descriptor;
import ynn.tech.algorithms.network.ui.icons.Icons;

class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private JMenuBar _menuBar;
	private JMenu _fileMenu;
	private JMenuItem _fileMenuExit;
	private JMenuItem _fileMenuOpen;
	private JMenuItem _fileMenuSave;
	private JMenuItem _fileMenuNew;
	private JMenu _editMenu;
	private JCheckBoxMenuItem _editMenuMode;
	private JRadioButtonMenuItem _editMenuMove;
	private JRadioButtonMenuItem _editMenuConnect;
	private JMenuItem _editMenuInsertNode;
	private JMenuItem _editMenuDelete;
	
	private JToolBar _toolBar;
	private JButton _toolBarFileNew;
	private JButton _toolBarFileOpen;
	private JButton _toolBarFileSave;
	private JToggleButton _toolBarEditMode;
	private JButton _toolBarEditInsert;
	private JButton _toolBarEditDelete;

	private JPanel _contentPanel;
	
    private NetworkView _networkView;
    private NetworkModel _networkModel;
    private NetworkAdapter _networkAdapter;
    
    private int _nodeId = 0;
    private AlgorithmDescriptor _algDescriptor = null;

	public MainWindow()
	{
		super("Network Algorithms Simulator");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500, 300);
        setLocationRelativeTo(null);
        setIconImage(Icons.getNetwork());
		initializeControls();
		_algDescriptor = new Aky90Descriptor();
	}
	
	// #######################################################################
	// ##########               initialization methods              ##########
	// #######################################################################
	
	private void initializeControls()
	{
		_contentPanel = new JPanel();
		_contentPanel.setLayout(new BorderLayout());
		setContentPane(_contentPanel);
		initializeMenu();
		initializeToolBar();
		initializeNetworkView();
        _contentPanel.add(_toolBar,BorderLayout.NORTH);
        _contentPanel.add(_networkView,BorderLayout.CENTER);
		addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				switch (e.getKeyCode())
				{
				case KeyEvent.VK_P:
					_networkAdapter.getClass();
					break;
				}
			}
		});
	}

	private void initializeNetworkView()
	{
        _networkModel = new NetworkModel();
        _networkView = new NetworkView();
        _networkAdapter = new NetworkAdapter();
        _networkAdapter.attach(_networkModel);
        _networkAdapter.attach(_networkView);
	}

	private void initializeToolBar()
	{
		_toolBar = new JToolBar();
		
		_toolBarFileNew = new JButton(Icons.getNew());
		_toolBarFileNew.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onNew();
			}
		});
		_toolBar.add(_toolBarFileNew);

		_toolBarFileOpen = new JButton(Icons.getOpen());
		_toolBarFileOpen.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onOpen();
			}
		});
		_toolBar.add(_toolBarFileOpen);

		_toolBarFileSave = new JButton(Icons.getSave());
		_toolBarFileSave.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onSave();
			}
		});
		_toolBar.add(_toolBarFileSave);
		
		_toolBar.addSeparator();

		_toolBarEditInsert = new JButton(Icons.getInsert());
		_toolBarEditInsert.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onInsertNewNode();
			}
		});
		_toolBar.add(_toolBarEditInsert);

		_toolBarEditDelete = new JButton(Icons.getDelete());
		_toolBarEditDelete.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onDeleteSelectedShapes();
			}
		});
		_toolBar.add(_toolBarEditDelete);
		
		_toolBarEditMode = new JToggleButton(Icons.getEditMode());
		_toolBarEditMode.setSelected(true);
		_toolBarEditMode.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				onAllowEditChanged(_toolBarEditMode.isSelected());
			}
		});
		_toolBar.add(_toolBarEditMode);
	}

	private void initializeMenu()
	{
		_menuBar = new JMenuBar();
		initializeFileMenu();
		initializeEditMenu();
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
//		_editMenuMode.addItemListener(new ItemListener()
//		{
//			@Override
//			public void itemStateChanged(ItemEvent e)
//			{
//				onAllowEditChanged(e.getStateChange() == ItemEvent.SELECTED);
//			}
//		});
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
				onEditModeSelected(_editMenuMove);
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
				onEditModeSelected(_editMenuConnect);
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

	// #######################################################################
	// ##########                 on event methods                  ##########
	// #######################################################################

	private void onNew()
	{
		// TODO
		System.err.println("New");
	}

	private void onOpen()
	{
		try
		{
			File file = new File("c:\\Temp\\model.xml");
			NetworkSerializer serializer = new NetworkSerializer(_networkView);
			serializer.deserialize(file);
			_networkView.repaint();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	private void onSave()
	{
		try
		{
			File file = new File("c:\\Temp\\model.xml");
			NetworkSerializer serializer = new NetworkSerializer(_networkView);
			serializer.serialize(file);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
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
		}
		else
		{
			_networkView.setMode(Mode.View);
		}
		if (_editMenuMode.isSelected() != allow) _editMenuMode.setSelected(allow);
		if (_toolBarEditMode.isSelected() != allow) _toolBarEditMode.setSelected(allow);
		
		_editMenuMove.setEnabled(allow);
		_editMenuConnect.setEnabled(allow);
		_editMenuInsertNode.setEnabled(allow);
		_editMenuDelete.setEnabled(allow);
		_toolBarEditDelete.setEnabled(allow);
		_toolBarEditInsert.setEnabled(allow);
	}

	protected void onEditModeSelected(JRadioButtonMenuItem editMenuModeItem)
	{
		if (editMenuModeItem.equals(_editMenuMove))
		{
			_networkView.setMode(Mode.Move);
		}
		else if (editMenuModeItem.equals(_editMenuConnect))
		{
			_networkView.setMode(Mode.Connect);
		} 
	}
	
	protected void onInsertNewNode()
	{
		NodeShape shape = new NodeShape();
		shape.setText(String.valueOf(_nodeId++));
		Node node = new Node();
		node.setName(shape.getText());
		_algDescriptor.getUtilities().initNodeAttributes(node);
		shape.setData(node);
		_networkView.addShape(shape);
	}
	
	protected void onDeleteSelectedShapes()
	{
		_networkView.removeSelectedShape();
	}
	
	// #######################################################################
	// ##########                       main                        ##########
	// #######################################################################

	public static void main(String[] args)
	{
		try
		{
			SwingUtilities.invokeAndWait(new Runnable()
			{
				@Override
				public void run()
				{
					MainWindow window = new MainWindow();
					window.setVisible(true);
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
