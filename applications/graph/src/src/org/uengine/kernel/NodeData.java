package org.uengine.kernel;

import java.util.Map;

public class NodeData {

	private String namespaceURI;
	private String QName;
	private String LName;
	private Map<String, String> attributes;
	private String value;

	public NodeData() {

	}

	public NodeData(String namespaceURI, String qName, String lName, Map<String, String> attributes) {
		super();
		this.namespaceURI = namespaceURI;
		QName = qName;
		LName = lName;
		this.attributes = attributes;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNamespaceURI() {
		return namespaceURI;
	}

	public void setNamespaceURI(String namespaceURI) {
		this.namespaceURI = namespaceURI;
	}

	public String getQName() {
		return QName;
	}

	public void setQName(String qName) {
		QName = qName;
	}

	public String getLName() {
		return LName;
	}

	public void setLName(String lName) {
		LName = lName;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "NodeData [namespaceURI=" + namespaceURI + ", QName=" + QName + ", LName=" + LName + ", attributes=" + attributes + ", value=" + value + "]";
	}

}
