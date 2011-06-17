package ynn.tech.algorithms.network.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ynn.tech.algorithms.network.AlgorithmDescriptor;

public class AlgorithmDetailsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private AlgorithmDescriptor _algDescriptor = null;
	private JLabel _lblCode;
	private JLabel _lblName;
	private JLabel _lblDescription;
	private JTextField _fldCode;
	private JTextField _fldName;
	private JTextArea _fldDescription;

	public AlgorithmDetailsPanel()
	{
		super();
		initializeControls();
		initializeLayout();
	}

	private void initializeControls()
	{
		// Labels
		_lblCode = new JLabel("Code");
		_lblName = new JLabel("Name");
		_lblDescription = new JLabel("Description");
		// Fields
		_fldCode = new JTextField();
		_fldCode.setEditable(false);
		_fldCode.setBackground(Color.WHITE);
		_fldName = new JTextField();
		_fldName.setEditable(false);
		_fldName.setBackground(Color.WHITE);
		_fldDescription = new JTextArea();
		_fldDescription.setEditable(false);
		_fldDescription.setRows(5);
		_fldDescription.setWrapStyleWord(true);
		_fldDescription.setLineWrap(true);
		_fldDescription.setFont(_fldCode.getFont());
	}

	private void initializeLayout()
	{
		GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[] { 0.0, 1.0 };
		setLayout(layout);
		GridBagConstraints c;
		// Code
		c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		add(_lblCode,c);
		c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(_fldCode,c);
		// Name
		c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		add(_lblName,c);
		c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(_fldName,c);
		// Description
		c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		add(_lblDescription,c);
		c = new GridBagConstraints();
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 1;
		c.gridy = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(new JScrollPane(_fldDescription),c);
		c.gridx = 0;
		c.gridy = 3;
		c.weighty = 1;
		c.fill = GridBagConstraints.VERTICAL;
		add(new JLabel(" "), c);
	}
	
	public void setAlgorithmDescriptor(AlgorithmDescriptor descriptor)
	{
		_algDescriptor = descriptor;
		updateFields();
	}

	private void updateFields()
	{
		_fldCode.setText(_algDescriptor == null ? "" : _algDescriptor.getCode());
		_fldName.setText(_algDescriptor == null ? "" : _algDescriptor.getName());
		_fldDescription.setText(_algDescriptor == null ? "" : _algDescriptor.getDescription());
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		int height = super.getPreferredSize().height;
		return new Dimension(250, height);
	}
}
