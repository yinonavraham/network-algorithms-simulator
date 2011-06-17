package ynn.tech.algorithms.network.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class About extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private JLabel _lblName;
	private JLabel _lblVersion;
	private JLabel _lblCreators;
	private JLabel _fldVersion;
	private JTextArea _fldDescription;
	private JLabel _fldCreators;

	public About(Frame owner)
	{
		super(owner);
		setTitle("About");
		setModal(true);
		initializeControls();
		initializeLayout();
		setPreferredSize(new Dimension(310, 210));
		pack();
		setLocationRelativeTo(owner);
	}

	private void initializeControls()
	{
		_lblName = new JLabel("Network Algorithms Simulator");
		Font font = _lblName.getFont().deriveFont(Font.BOLD);
		_lblName.setFont(font);
		_lblCreators = new JLabel("Created By:");
		_lblCreators.setFont(font);
		_lblVersion = new JLabel("Version:");
		_lblVersion.setFont(font);
		_fldCreators = new JLabel("<html>Yinon Avraham<br>Tal Kellner</html>");
		_fldVersion = new JLabel("1.0.0.0");
		_fldDescription = new JTextArea();
		_fldDescription.setFont(_fldVersion.getFont());
		_fldDescription.setEditable(false);
		_fldDescription.setOpaque(false);
		_fldDescription.setLineWrap(true);
		_fldDescription.setWrapStyleWord(true);
		_fldDescription.setText(
			"Network Algorithms Simulator (NAS) was created as a project in the course " +
			"\"Fault Tolerant Networks Protocols\" (097211), lead by Professor Shay Kutten " +
			"in the Faculty of Industrial Engineering and Management in the Technion, " +
			"Israel Institute of Technology, Spring 2011." +
			"");
	}

	private void initializeLayout()
	{
		JPanel content = new JPanel();
		setContentPane(content);
		content.setLayout(new GridBagLayout());
		GridBagConstraints c;
		// Name
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridwidth = GridBagConstraints.REMAINDER;
		content.add(_lblName,c);
		// Version
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.insets = new Insets(5, 5, 5, 5);
		content.add(_lblVersion,c);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.insets = new Insets(5, 5, 5, 5);
		content.add(_fldVersion,c);
		// Creators
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.insets = new Insets(5, 5, 5, 5);
		content.add(_lblCreators,c);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.insets = new Insets(5, 5, 5, 5);
		content.add(_fldCreators,c);
		// Description
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.BASELINE_LEADING;
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		content.add(_fldDescription,c);
		// Anchor to the upper-left corner
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 100;
		c.weighty = 1;
		c.fill = GridBagConstraints.VERTICAL;
		content.add(new JLabel(), c);
		c = new GridBagConstraints();
		c.gridx = 100;
		c.gridy = 0;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		content.add(new JLabel(), c);
	}
}
