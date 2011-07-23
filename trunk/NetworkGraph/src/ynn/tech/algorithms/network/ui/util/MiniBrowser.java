package ynn.tech.algorithms.network.ui.util;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

public class MiniBrowser extends JDialog implements HyperlinkListener
{
	private static final long serialVersionUID = -3534362484917960668L;
	
	private String _title;
	private ResourceLoader _resourceLoader;
	private JButton _btnBack;
	private JButton _btnNext;
	private JEditorPane _editor;
	private List<URL> _history = new ArrayList<URL>();
	private int _currentIndex = -1;
	
	public MiniBrowser(Window owner, String title, ResourceLoader resourceLoader)
	{
		super(owner);
		_title = title;
		_resourceLoader = resourceLoader;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(_title);
		initComponents();
		setSize(500,400);
		setLocationRelativeTo(owner);
	}

	private void initComponents()
	{
		 // Set up file menu.
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem fileExitMenuItem = new JMenuItem("Exit",
                KeyEvent.VK_X);
        fileExitMenuItem.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                actionExit();
            }
        });
        fileMenu.add(fileExitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        // Set up buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(getButtonBack());
        buttonPanel.add(getButtonNext());
        getButtonBack().setEnabled(false);
        getButtonNext().setEnabled(false);
        // Set up the editor
        getEditor().setContentType("text/html");
        getEditor().setEditable(false);
        getEditor().addHyperlinkListener(this);
        // Set up the content pane
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(getEditor()),
                BorderLayout.CENTER);
	}

	private JEditorPane getEditor()
	{
		if (_editor == null)
		{
			_editor = new JEditorPane();
			
		}
		return _editor;
	}
	
	private JButton getButtonBack()
	{
		if (_btnBack == null)
		{
			_btnBack = new JButton("<< Back");
			_btnBack.setPreferredSize(new Dimension(80, 25));
			_btnBack.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					actionBack();
				}
			});
		}
		return _btnBack;
	}
	
	private JButton getButtonNext()
	{
		if (_btnNext == null)
		{
			_btnNext = new JButton("Next >>");
			_btnNext.setPreferredSize(new Dimension(80, 25));
			_btnNext.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					actionNext();
				}
			});
		}
		return _btnNext;
	}
	
	private boolean hasBack()
	{
		return _currentIndex > 0;
	}
	
	private boolean hasNext()
	{
		return _currentIndex + 1 < _history.size();
	}
	
	private void updateButtons()
	{
		getButtonNext().setEnabled(hasNext());
		getButtonBack().setEnabled(hasBack());
	}
	
	private void actionBack()
	{
		if (hasBack())
		{
			_currentIndex--;
			displayPage(_history.get(_currentIndex), false);
		}
	}
	
	private void actionNext()
	{
		if (hasNext())
		{
			_currentIndex++;
			displayPage(_history.get(_currentIndex), false);
		}
	}

	public void displayPage(URL pageURL, boolean addToHistory)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		try
		{
			getEditor().setPage(pageURL);
			if (addToHistory)
			{
				for (int i = _history.size()-1; i > _currentIndex; i--)
				{
					_history.remove(i);
				}
				_history.add(pageURL);
				_currentIndex++;
			}
			updateButtons();
		}
		catch (IOException e)
		{
			showError("Failed to load the page '" + pageURL + "'");
		}
		finally 
		{
            setCursor(Cursor.getDefaultCursor());
        }
	}

	private void actionExit()
	{
		this.processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event)
	{
		HyperlinkEvent.EventType eventType = event.getEventType();
        if (eventType == HyperlinkEvent.EventType.ACTIVATED) 
        {
            if (event instanceof HTMLFrameHyperlinkEvent) 
            {
                HTMLFrameHyperlinkEvent linkEvent = (HTMLFrameHyperlinkEvent) event;
                HTMLDocument document = (HTMLDocument) getEditor().getDocument();
                document.processHTMLFrameHyperlinkEvent(linkEvent);
            } 
            else 
            {
                displayPage(event.getURL(), true);
            }
        }
	}
	
	private void showError(String errorMessage) 
	{
        JOptionPane.showMessageDialog(this, errorMessage,
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}
