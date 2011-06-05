package ynn.tech.algorithms.network.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ynn.tech.algorithms.network.AlgorithmDescriptor;

public class NewNetworkDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private AlgorithmDescriptor[] _descriptors;
	private AlgorithmDescriptor _selectedDescriptor = null;
	
	private JLabel _lblTitle;
	private JList _list;
	private JLabel _lblDescription;
	private JTextArea _fldDescription;
	private JButton _btnAccept;
	private JButton _btnCancel;
	
	public NewNetworkDialog(Frame owner, AlgorithmDescriptor[] descriptors)
	{
		super(owner);
		setTitle("New Network Model");
		setModal(true);
		_descriptors = descriptors;
		initializeControls();
		pack();
		setLocationRelativeTo(owner);
		setResizable(false);
	}

	public AlgorithmDescriptor getSelectedDescriptor()
	{
		return _selectedDescriptor;
	}
	
	private void initializeControls()
	{
		setLayout(new BorderLayout());
		_lblTitle = new JLabel("Select the algorithm for which the network is for");
		add(_lblTitle,BorderLayout.NORTH);
		add(new JLabel(" "), BorderLayout.WEST);
		add(new JLabel(" "), BorderLayout.EAST);
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		add(contentPanel,BorderLayout.CENTER);
		_list = new JList(_descriptors);
		_list.setBorder(BorderFactory.createLoweredBevelBorder());
		_list.setCellRenderer(new ListCellRenderer()
		{
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus)
			{
				JLabel label = new JLabel("<Not an Algorithm>");
				label.setOpaque(true);
				label.setFont(list.getFont());
				if (isSelected)
				{
					label.setBackground(list.getSelectionBackground());
					label.setForeground(list.getSelectionForeground());
				}
				else
				{
					label.setBackground(list.getBackground());
					label.setForeground(list.getForeground());
				}
				if (value instanceof AlgorithmDescriptor)
				{
					AlgorithmDescriptor descriptor = (AlgorithmDescriptor)value;
					label.setText(String.format("%s - %s", descriptor.getCode(), descriptor.getName()));
					StringBuilder sb = new StringBuilder();
					int j, i = 0;
					do 
					{
						j = descriptor.getDescription().indexOf(" ", i+50);
						j = j < 0 ? descriptor.getDescription().length() : j;
						sb.append(descriptor.getDescription().substring(i,j));
						sb.append("<BR>");
						i = j;
					} while (i < descriptor.getDescription().length());
					label.setToolTipText("<html>"+ sb + "</html>");
				}
				return label;
			}
		});
		_list.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				_btnAccept.setEnabled(false);
				if (_list.getSelectedValue() instanceof AlgorithmDescriptor)
				{
					_btnAccept.setEnabled(true);
					_fldDescription.setText(((AlgorithmDescriptor)_list.getSelectedValue()).getDescription());
					_fldDescription.setCaretPosition(0);
				}
			}
		});
		JScrollPane pane = new JScrollPane(_list);
		pane.setPreferredSize(new Dimension(_list.getPreferredSize().width, Math.min(_list.getPreferredSize().height, 70)));
		
		contentPanel.add(pane,BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		_lblDescription = new JLabel("Description");
		panel.add(_lblDescription,BorderLayout.NORTH);
		_fldDescription = new JTextArea();
		_fldDescription.setRows(5);
		_fldDescription.setBorder(BorderFactory.createLoweredBevelBorder());
		_fldDescription.setEditable(false);
		_fldDescription.setWrapStyleWord(true);
		_fldDescription.setLineWrap(true);
		_fldDescription.setAutoscrolls(true);
		panel.add(new JScrollPane(_fldDescription),BorderLayout.CENTER);
		contentPanel.add(panel,BorderLayout.SOUTH);
		
		JPanel buttonsPanel = new JPanel(new BorderLayout());
		add(buttonsPanel,BorderLayout.SOUTH);
		_btnAccept = new JButton("OK");
		_btnAccept.addActionListener(new ActionListener()
		{	
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (_list.getSelectedValue() instanceof AlgorithmDescriptor)
				{
					_selectedDescriptor = (AlgorithmDescriptor)_list.getSelectedValue();
				}
				close();
			}
		});
		_btnAccept.setEnabled(false);
		buttonsPanel.add(_btnAccept,BorderLayout.WEST);
		_btnCancel = new JButton("Cancel");
		_btnCancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				_selectedDescriptor = null;
				close();
			}
		});
		buttonsPanel.add(_btnCancel,BorderLayout.EAST);
		_btnAccept.setPreferredSize(_btnCancel.getPreferredSize());
	}
	
	public AlgorithmDescriptor showDialog()
	{
		setVisible(true);
		return getSelectedDescriptor();
	}
	
	private void close()
	{
		processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
