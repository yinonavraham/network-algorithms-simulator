package ynn.tech.algorithms.network.ui.attributes;

import javax.swing.event.TableModelListener;

public interface NodeAttributesTableModelListener extends TableModelListener
{
	void valueParseError(NodeAttributesTableModelEvent e);
	
	void valueSet(NodeAttributesTableModelEvent e);
}
