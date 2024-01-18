package org.uengine.kernel;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//class SAXTreeBuilder extends DefaultHandler {
//
//	private DefaultMutableTreeNode rootNode, currentNode;
//	
//	public SAXTreeBuilder(DefaultMutableTreeNode rootNode) {
//		this.rootNode = rootNode;
//	}
//
//	public DefaultMutableTreeNode getRoot() {
//		return rootNode;
//	}
//
//	// SAX Parser Handler methods...
//	public void startElement(String namespaceURI, String lName, String qName, Attributes attributes) throws SAXException {
//		String eName = lName; // Element name
//		if ("".equals(eName))
//			eName = qName;
//		
//		NodeData nodeData =  new NodeData(namespaceURI, qName, lName, attributes);
//		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeData);
//		
//		if (currentNode == null) {
//			rootNode = newNode;
//		} else {
//			// Must not be the root node...
//			currentNode.add(newNode);
//		}
//		currentNode = newNode;
//	}
//
//	public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
//		currentNode = (DefaultMutableTreeNode) currentNode.getParent();
//	}
//
//	public void characters(char buf[], int offset, int len) throws SAXException {
//		String s = new String(buf, offset, len).trim();
//		System.out.println("이 데이터는 무엇? : " + s);
////		((NodeData) currentNode.getUserObject()).addData(s);
//	}
//	
//	public DefaultMutableTreeNode getTree() {
//		return rootNode;
//	}
//
//}

//package com.a.webservice;
//
//import javax.swing.tree.DefaultMutableTreeNode;
//
//import org.apache.xerces.util.AttributesProxy;
//import org.apache.xerces.util.XMLAttributesImpl;
//import org.xml.sax.Attributes;
//import org.xml.sax.helpers.DefaultHandler;
//
class SAXTreeBuilder extends DefaultHandler {

	private XMLTreeNode currentNode = null;
	private XMLTreeNode previousNode = null;
	private XMLTreeNode rootNode = null;

	public SAXTreeBuilder(XMLTreeNode root) {
		rootNode = root;
	}

	public void startDocument() {
		currentNode = rootNode;
	}

	public void endDocument() {
	}

	public void characters(char[] data, int start, int end) {
		String elementValue = new String(data, start, end).trim();
		if (!elementValue.equals("")) {
			currentNode.getNodeData().setValue(elementValue);
		}
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		super.startPrefixMapping(prefix, uri);
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		super.endPrefixMapping(prefix);
	}

	public void startElement(String namespaceURI, String qName, String lName, Attributes attributes) {
		previousNode = currentNode;
		currentNode = new XMLTreeNode(lName);
		
		Map<String, String> attrs = new LinkedHashMap<String, String>();
		for (int i = 0; i < attributes.getLength(); i++) {
			attrs.put(attributes.getQName(i), attributes.getValue(i));
		}
		
		currentNode.setNodeData(new NodeData(namespaceURI, qName, lName, attrs));
		// Add attributes as child nodes //
//		attachAttributeList(currentNode, attributes);
		previousNode.add(currentNode);
	}

	public void endElement(String uri, String qName, String lName) {
		if (currentNode.getUserObject().equals(lName))
			currentNode = (XMLTreeNode) currentNode.getParent();
	}

	public DefaultMutableTreeNode getTree() {
		return rootNode;
	}

//	private void attachAttributeList(DefaultMutableTreeNode node, Attributes atts) {
//		for (int i = 0; i < atts.getLength(); i++) {
//			String name = atts.getLocalName(i);
//			String value = atts.getValue(name);
//			node.add(new DefaultMutableTreeNode(name + " = " + value));
//		}
//	}

}