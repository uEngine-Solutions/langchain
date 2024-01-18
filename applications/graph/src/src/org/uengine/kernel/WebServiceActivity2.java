package org.uengine.kernel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.metaworks.Type;

import com.predic8.wsdl.WSDLParser;

/**
 * http://www.membrane-soa.org/soa-model/  라이브러리와 (SOAP Request 템플릿 생성이 부실)
 * apache httpclinet 를 이용하고 있는데 다른 대처 방안이 있어야 한다.
 * 
 * Soap UI 는 템플릿 생성 기능이 현존 오픈 소스중에는 최고인듯 하나 라이브러리가 많이 필요하고 너무 무겁다. 대신 퀄러티는 확실하다.
 * 일단은 Membrane SOA Model 을 좀 더 지켜보도록 한다.
 *
 */
public class WebServiceActivity2 extends DefaultActivity {

	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;
	
	public static void metaworksCallback_changeMetadata(Type type) {
		type.setFieldOrder(new String[] { "EndpointAddress", "PortType", "Operation", "Binding", "RequestXML", "TestResponseXML", "xPathContexts", "OutputXMLString" });
	}
	
	private String endpointAddress;
		public String getEndpointAddress() {
			return endpointAddress;
		}
		public void setEndpointAddress(String endpointAddress) {
			this.endpointAddress = endpointAddress;
		}
	
	private String portType;
		public String getPortType() {
			return portType;
		}
		public void setPortType(String portType) {
			this.portType = portType;
		}
	
	private String operation;
		public String getOperation() {
			return operation;
		}
		public void setOperation(String operation) {
			this.operation = operation;
		}
	
	private String binding;
		public String getBinding() {
			return binding;
		}
		public void setBinding(String binding) {
			this.binding = binding;
		}
		
	private String requestXML;
		public String getRequestXML() {
			return requestXML;
		}
		public void setRequestXML(String requestXML) {
			this.requestXML = requestXML;
		}
		
	private String testResponseXML;
		public String getTestResponseXML() {
			return testResponseXML;
		}
		public void setTestResponseXML(String testResponseXML) {
			this.testResponseXML = testResponseXML;
		}
		
	private XPathContext[] xPathContexts;
		public XPathContext[] getxPathContexts() {
			return xPathContexts;
		}
		public void setxPathContexts(XPathContext[] xPathContexts) {
			this.xPathContexts = xPathContexts;
		}
		
	private ProcessVariable outputXMLString;
		public ProcessVariable getOutputXMLString() {
			return outputXMLString;
		}
		public void setOutputXMLString(ProcessVariable outputXMLString) {
			this.outputXMLString = outputXMLString;
		}
		
	public WebServiceActivity2() {
		setName("WebServiceActivity2");
		setEndpointAddress("http://localhost:8080/uengine-web/services/workflow.wsdl");
	}
	
	public static String removeXmlStringNamespaceAndPreamble(String xmlString) {
		return xmlString.replaceAll("(<\\?[^<]*\\?>)?", ""). /* remove preamble */
		replaceAll("xmlns.*?(\"|\').*?(\"|\')", "") /* remove xmlns declaration */
		.replaceAll("(<)(\\w+:)(.*?>)", "$1$3") /* remove opening tag prefix */
		.replaceAll("(</)(\\w+:)(.*?>)", "$1$3"); /* remove closing tags prefix */
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void executeActivity(ProcessInstance instance) throws Exception {
		StringBuffer responseXML = new StringBuffer();
		
		BufferedReader br = null;
		PostMethod methodPost = null;
		try {
			WSDLParser parser = new WSDLParser();
			String soapAddress = (String) parser.parse(endpointAddress).getBaseDir();
			
			HttpClient httpClient = new HttpClient();
			httpClient.getParams().setParameter("http.useragent", "Web Service Test Client");
			
			methodPost = new PostMethod(endpointAddress);
			methodPost.setRequestBody(this.getRequestXML());
			methodPost.addRequestHeader("SOAPAction", soapAddress);
			methodPost.setRequestHeader("Content-Type", "text/xml");
			
			int returnCode = httpClient.executeMethod(methodPost);

			if (returnCode == HttpStatus.SC_OK) {
				br = new BufferedReader(new InputStreamReader(methodPost.getResponseBodyAsStream()));
				String readLine;
				while (((readLine = br.readLine()) != null)) {
					responseXML.append(readLine);
				}
			}
			
			Document doc = new SAXBuilder().build(new StringReader(removeXmlStringNamespaceAndPreamble(responseXML.toString())));
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			System.out.println(outputter.outputString(doc));
			
			if (outputXMLString != null) {
				instance.set(outputXMLString.getName(), outputter.outputString(doc));
			}
			
			if (xPathContexts != null) {
				for (int i = 0; i < xPathContexts.length; i++) {
					XPathContext xPathContext = xPathContexts[i];
					
					StringBuffer newXpression = new StringBuffer();
					String[] xlist = xPathContext.getXpression().split("/");
					for (int n = 0; n < xlist.length; n++) {
						if (n > 0) {
							newXpression.append("/");
						}
						String[] pathName = xlist[n].split(":");
						if (pathName.length == 1) {
							newXpression.append(pathName[0]);
						} else {
							newXpression.append(pathName[1]);
						}
					}
					
					XPath xPath = XPath.newInstance(newXpression.toString());
					xPath.addNamespace(Namespace.NO_NAMESPACE);
//					xPath.addNamespace(xPathContext.getNamespacePrefix(), xPathContext.getNamespaceUri());
					
					Element obj = (Element) xPath.selectSingleNode(doc);
					String value = obj.getValue();
					
					instance.set(xPathContext.getVariable().getName(), value);
				}
			}
			
		} catch (Exception e) {
			throw e;
		} finally {
			if (methodPost != null) try { methodPost.releaseConnection(); } catch (Exception e) {}
			if (br != null) try { br.close(); } catch (Exception e) {}
		}
		
		fireComplete(instance);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public ValidationContext validate(Map options) {
		ValidationContext vc = super.validate(options);

		if (getOutputXMLString() != null && getOutputXMLString().getType() != String.class) {
			vc.add("OutputXMLString property of the variable must be a text(string) type.");
		}
		
		return vc;
	}

}
