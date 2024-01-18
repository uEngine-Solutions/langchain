package org.uengine.kernel;

import javax.swing.tree.DefaultMutableTreeNode;


public class XMLTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	public XMLTreeNode() {
		super();
	}

	public XMLTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public XMLTreeNode(Object userObject) {
		super(userObject);
	}

	private NodeData nodeData;

	public NodeData getNodeData() {
		return nodeData;
	}

	public void setNodeData(NodeData nodeData) {
		this.nodeData = nodeData;
	}

}
