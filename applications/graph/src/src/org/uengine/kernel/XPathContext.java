package org.uengine.kernel;

import java.io.Serializable;

import org.metaworks.Type;

public class XPathContext implements Serializable {

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

	public static void metaworksCallback_changeMetadata(Type type) {
		type.removeFieldDescriptor("NamespacePrefix");
		type.removeFieldDescriptor("NamespaceUri");
//		type.setFieldOrder(new String[] { "Xpression", "NamespacePrefix", "NamespaceUri", "Variable" });
		type.setFieldOrder(new String[] { "Xpression", "Variable" });
	}

	private String xpression;
	private String namespacePrefix;
	private String namespaceUri;
	private ProcessVariable variable;

	public String getXpression() {
		return xpression;
	}

	public void setXpression(String xpression) {
		this.xpression = xpression;
	}

	public String getNamespacePrefix() {
		return namespacePrefix;
	}

	public void setNamespacePrefix(String namespacePrefix) {
		this.namespacePrefix = namespacePrefix;
	}

	public String getNamespaceUri() {
		return namespaceUri;
	}

	public void setNamespaceUri(String namespaceUri) {
		this.namespaceUri = namespaceUri;
	}

	public ProcessVariable getVariable() {
		return variable;
	}

	public void setVariable(ProcessVariable variable) {
		this.variable = variable;
	}

}
