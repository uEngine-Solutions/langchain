package org.uengine.kernel.descriptor;

import groovy.xml.MarkupBuilder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.metaworks.FieldDescriptor;
import org.metaworks.inputter.SelectInput;
import org.metaworks.inputter.TextAreaInput;
import org.metaworks.inputter.TextInput;
import org.uengine.kernel.Activity;
import org.uengine.kernel.GlobalContext;
import org.uengine.kernel.WebServiceActivity2;
import org.uengine.kernel.XMLFrame;
import org.uengine.processdesigner.ProcessDesigner;
import org.uengine.util.UEngineUtil;

import com.predic8.wsdl.Binding;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wstool.creator.RequestCreator;
import com.predic8.wstool.creator.RequestTemplateCreator;
import com.predic8.wstool.creator.SOARequestCreator;

public class WebServiceActivity2Descriptor extends ActivityDescriptor {

	private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
	private Definitions defs;

	public WebServiceActivity2Descriptor() throws Exception{
		super();
	}
	
	public void initialize(ProcessDesigner pd, Activity activity) {
		super.initialize(pd, activity);
			
		// EndpointAddress
		FieldDescriptor endpointAddressFD = getFieldDescriptor("EndpointAddress");
		endpointAddressFD.setInputter(new TextInput() {

			private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;

			private JTextField textField = null;
			private JButton button = null;

			@Override
			public Component getNewComponent() {
				textField = (JTextField) super.getNewComponent();
				button = new JButton("bind");

				JPanel panel = new JPanel(new BorderLayout());
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						try {
							initializeDefinitions();
						} catch (Exception e) {
							e.printStackTrace();
						}
						initializePortTypeInputter();
						clearOperationInputter();
						initializeBindingInputter();
						
						FieldDescriptor fd = getFieldDescriptor("RequestXML");
						TextAreaInput textAreaInput = (TextAreaInput) fd.getInputter();
						textAreaInput.setValue("");
					}
				});

				panel.add("Center", textField);
				panel.add("East", button);

				textField.addKeyListener(new KeyListener() {
					@Override
					public void keyTyped(KeyEvent e) {
					}

					@Override
					public void keyReleased(KeyEvent e) {
					}

					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							try {
								initializeDefinitions();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							initializePortTypeInputter();
							clearOperationInputter();
							initializeBindingInputter();
							
							FieldDescriptor fd = getFieldDescriptor("RequestXML");
							TextAreaInput textAreaInput = (TextAreaInput) fd.getInputter();
							textAreaInput.setValue("");
						}
					}
				});

				return panel;
			}

			@Override
			public Component getValueComponent() {
				return textField;
			}

		});
		
		
		// PortType
		FieldDescriptor portTypeFD = getFieldDescriptor("PortType");
		String portType = ((WebServiceActivity2) activity).getPortType();
		if (portType == null) {
			portTypeFD.setInputter(new SelectInput(new String[] { "" }, new String[] { null }));
		} else {
			portTypeFD.setInputter(new SelectInput(new String[] { "", portType }, new String[] { null, portType }));
		}
		JComboBox portTypeComboBox = (javax.swing.JComboBox) portTypeFD.getInputComponent();
		portTypeComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int state = e.getStateChange();
				if (state == ItemEvent.SELECTED) {
					if (!"".equals(e.getItem())) {
						initializeOperationInputter(e.getItem().toString());
						createRequestXMLTemplate();
					}
				}
			}
		});
		
		
		// Operation
		FieldDescriptor operationFD = getFieldDescriptor("Operation");
		String operation = ((WebServiceActivity2) activity).getOperation();
		if (operation == null) {
			operationFD.setInputter(new SelectInput(new String[] { "" }, new String[] { null }));
		} else {
			operationFD.setInputter(new SelectInput(new String[] { "", operation }, new String[] { null, operation }));
		}
		JComboBox operationComboBox = (javax.swing.JComboBox) operationFD.getInputComponent();
		operationComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int state = e.getStateChange();
				if (state == ItemEvent.SELECTED) {
					if (!"".equals(e.getItem())) {
						createRequestXMLTemplate();
					}
				}
			}
		});
		
		
		// Binding
		FieldDescriptor bindingFD = getFieldDescriptor("Binding");
		String binding = ((WebServiceActivity2) activity).getBinding();
		if (binding == null) {
			bindingFD.setInputter(new SelectInput(new String[] { "" }, new String[] { null }));
		} else {
			bindingFD.setInputter(new SelectInput(new String[] { "", binding }, new String[] { null, binding }));
		}
		JComboBox bindingComboBox = (javax.swing.JComboBox) bindingFD.getInputComponent();
		bindingComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				int state = e.getStateChange();
				if (state == ItemEvent.SELECTED) {
					if (!"".equals(e.getItem())) {
						createRequestXMLTemplate();
					}
				}
			}
		});
		
		
		// RequestXML
		{
			FieldDescriptor requestXMLFD = getFieldDescriptor("RequestXML");
			requestXMLFD.setInputter(new TextAreaInput(50, 10) {
	
				private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
				private JScrollPane scrollPane;
	
				@Override
				public Component getNewComponent() {
					scrollPane = (JScrollPane) super.getNewComponent();
					return scrollPane;
				}
	
			});
			JScrollPane scrollPane = (JScrollPane) requestXMLFD.getInputComponent();
			JViewport viewport = (javax.swing.JViewport) scrollPane.getComponents()[0];
			JTextArea textArea = (JTextArea) viewport.getComponents()[0];
			textArea.addMouseListener(new MouseListener() {
	
				@Override
				public void mouseReleased(MouseEvent e) {
				}
	
				@Override
				public void mousePressed(MouseEvent e) {
				}
	
				@Override
				public void mouseExited(MouseEvent e) {
				}
	
				@Override
				public void mouseEntered(MouseEvent e) {
				}
	
				@Override
				public void mouseClicked(MouseEvent e) {
					FieldDescriptor requestXMLFD = getFieldDescriptor("RequestXML");
					TextAreaInput textAreaInput = (TextAreaInput) requestXMLFD.getInputter();
					String xml = (String) textAreaInput.getValue();
					if (UEngineUtil.isNotEmpty(xml)) {
						XMLFrame xmlFrame = new XMLFrame(requestXMLFD, xml);
						xmlFrame.setVisible(true);
					}
				}
			});
			textArea.setEditable(false);
		}
		
		
		// TestResponseXML
		{
			((WebServiceActivity2) activity).setTestResponseXML(null);
			FieldDescriptor testResponseXMLFD = getFieldDescriptor("TestResponseXML");
			testResponseXMLFD.setInputter(new TextAreaInput(50, 10) {
	
				private static final long serialVersionUID = GlobalContext.SERIALIZATION_UID;
	
				private JScrollPane scrollPane;
				private JButton button = null;
	
				@Override
				public Component getNewComponent() {
					scrollPane = (JScrollPane) super.getNewComponent();
					button = new JButton("Test");
					
					JPanel panel = new JPanel(new BorderLayout());
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							testWebservice();
						}
					});

					panel.add("North", button);
					panel.add("South", scrollPane);
					
					return panel;
				}
	
			});
			JPanel panel = (JPanel) testResponseXMLFD.getInputComponent();;
			JScrollPane scrollPane = (JScrollPane) panel.getComponents()[1];
			JViewport viewport = (javax.swing.JViewport) scrollPane.getComponents()[0];
			JTextArea textArea = (JTextArea) viewport.getComponents()[0];
			textArea.setEditable(false);
		}

		setFieldDisplayNames(WebServiceActivity2.class);
	}
	
	protected void testWebservice() {
		if (defs == null) {
			try {
				initializeDefinitions();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		
		FieldDescriptor fd = getFieldDescriptor("EndpointAddress");
		JPanel panel = (JPanel) fd.getInputComponent();
		final String wsdlAddress = ((JTextField) panel.getComponents()[0]).getText();
		final String soapAddress = (String) defs.getBaseDir();
//		final String soapAddress = "https://digidocservice.sk.ee/DigiDocService";
		// http://www.sk.ee/DigiDocService/DigiDocService_2_3.wsdl
		
		fd = getFieldDescriptor("RequestXML");
		TextAreaInput textAreaInput = (TextAreaInput) fd.getInputter();
		final String xmlBody = (String) textAreaInput.getValue();
		
//		try {
//			WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
//			webServiceTemplate.setDefaultUri(endpointAddress);
////			webServiceTemplate.setMessageFactory(new SaajSoapMessageFactory());
//			
//			StreamSource source = new StreamSource(new StringReader(xmlBody));
//			StreamResult result = new StreamResult(System.out);
//			webServiceTemplate.sendSourceAndReceiveToResult(source, result);
//			System.out.println();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				StringBuffer responseXML = new StringBuffer();
				String data = xmlBody;
				String prettyFormatXML = null;
				
				HttpClient httpClient = new HttpClient();
				httpClient.getParams().setParameter("http.useragent", "Web Service Test Client");
				
				PostMethod methodPost = new PostMethod(wsdlAddress);
				methodPost.setRequestBody(data);
				methodPost.addRequestHeader("SOAPAction", soapAddress);
				methodPost.setRequestHeader("Content-Type", "text/xml");
				
				BufferedReader br = null;
				try {
					int returnCode = httpClient.executeMethod(methodPost);

					if (returnCode == HttpStatus.SC_OK) {
						br = new BufferedReader(new InputStreamReader(methodPost.getResponseBodyAsStream()));
						String readLine;
						while (((readLine = br.readLine()) != null)) {
							responseXML.append(readLine);
						}
						
						if (responseXML.toString().trim().length() > 0) {
							try {
								Document doc = new SAXBuilder().build(new StringReader(responseXML.toString()));
								XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
								prettyFormatXML = outputter.outputString(doc);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						prettyFormatXML = "The Post method is not implemented by this URI\n";
						prettyFormatXML += methodPost.getResponseBodyAsString();
					}
					
				} catch (Exception e) {
					prettyFormatXML = e.getMessage();
					e.printStackTrace();
				} finally {
					methodPost.releaseConnection();
					if (br != null) try { br.close(); } catch (Exception e) {}
					
					FieldDescriptor testResponseXMLFD = getFieldDescriptor("TestResponseXML");
					testResponseXMLFD.getInputter().setValue(prettyFormatXML == null ? "" : prettyFormatXML);
				}
			}
		}).start();
		
	}
	
	protected void createRequestXMLTemplate() {
		if (defs == null) {
			try {
				initializeDefinitions();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}

		FieldDescriptor fd = getFieldDescriptor("EndpointAddress");
		JPanel panel = (JPanel) fd.getInputComponent();
		String endpointAddress = ((JTextField) panel.getComponents()[0]).getText();

		fd = getFieldDescriptor("PortType");
		SelectInput selectInput = (SelectInput) fd.getInputter();
		String portType = (String) selectInput.getValue();

		fd = getFieldDescriptor("Operation");
		selectInput = (SelectInput) fd.getInputter();
		String operation = (String) selectInput.getValue();

		fd = getFieldDescriptor("Binding");
		selectInput = (SelectInput) fd.getInputter();
		String binding = (String) selectInput.getValue();

		StringWriter writer = new StringWriter();
		if (UEngineUtil.isNotEmpty(portType) && UEngineUtil.isNotEmpty(operation) && UEngineUtil.isNotEmpty(binding)) {
			try {
				SOARequestCreator creator = new SOARequestCreator(defs, new RequestTemplateCreator(), new MarkupBuilder(writer));
				creator.createRequest(portType, operation, binding);
			} catch (Exception e) {
				writer.getBuffer().setLength(0);
				SOARequestCreator creator = new SOARequestCreator(defs, new RequestCreator(), new MarkupBuilder(writer));
				creator.createRequest(portType, operation, binding);
				e.printStackTrace();
			}
		}

		fd = getFieldDescriptor("RequestXML");
		TextAreaInput textAreaInput = (TextAreaInput) fd.getInputter();
		textAreaInput.setValue(writer.toString());
	}

	protected void initializeBindingInputter() {
		FieldDescriptor fd = getFieldDescriptor("Binding");
		SelectInput inputter = (SelectInput) fd.getInputter();

		List<Binding> bindings = (defs != null) ? defs.getBindings() : new ArrayList<Binding>();
		String[] selections = new String[bindings.size() + 1];
		String[] values = new String[bindings.size() + 1];
		selections[0] = "";
		values[0] = null;
		for (int i = 0; i < bindings.size(); i++) {
			Binding b = bindings.get(i);
			selections[i + 1] = b.getName();
			values[i + 1] = b.getName();
		}
		inputter.setSelections(selections);
		inputter.setValues(values);
	}

	protected void initializeOperationInputter(String portTypeName) {
		if (defs == null) {
			return;
		}

		FieldDescriptor fd = getFieldDescriptor("Operation");
		SelectInput inputter = (SelectInput) fd.getInputter();

		List<Operation> ops = defs.getPortType(portTypeName).getOperations();
		String[] selections = new String[ops.size() + 1];
		String[] values = new String[ops.size() + 1];
		selections[0] = "";
		values[0] = null;
		for (int i = 0; i < ops.size(); i++) {
			Operation op = ops.get(i);
			selections[i + 1] = op.getName();
			values[i + 1] = op.getName();
		}
		inputter.setSelections(selections);
		inputter.setValues(values);
	}
	
	protected void initializeDefinitions() throws Exception {
		FieldDescriptor fd = getFieldDescriptor("EndpointAddress");
		JPanel panel = (JPanel) fd.getInputComponent();
		String endpointAddress = ((JTextField) panel.getComponents()[0]).getText();
//		String endpointAddress = ((JTextField) fd.getInputComponent()).getText();
		
		if (UEngineUtil.isNotEmpty(endpointAddress)) {
			try {
				WSDLParser parser = new WSDLParser();
				defs = parser.parse(endpointAddress);
			} catch (Exception e) {
				defs = null;
				throw e;
			}
		}
	}

	protected void initializePortTypeInputter() {
		FieldDescriptor fd = getFieldDescriptor("PortType");
		SelectInput inputter = (SelectInput) fd.getInputter();

		List<PortType> pts = (defs != null) ? defs.getPortTypes() : new ArrayList<PortType>();
		String[] selections = new String[pts.size() + 1];
		String[] values = new String[pts.size() + 1];
		selections[0] = "";
		values[0] = null;
		for (int i = 0; i < pts.size(); i++) {
			PortType pt = pts.get(i);
			selections[i + 1] = pt.getName();
			values[i + 1] = pt.getName();
		}
		inputter.setSelections(selections);
		inputter.setValues(values);
	}

	protected void clearOperationInputter() {
		FieldDescriptor operationFD = getFieldDescriptor("Operation");
		SelectInput selectInput = (SelectInput) operationFD.getInputter();
		selectInput.setSelections(new String[] { "" });
		selectInput.setValue(new String[] { null });
	}
	
}