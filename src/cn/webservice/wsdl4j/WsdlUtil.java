package cn.webservice.wsdl4j;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;

/**
 * ����wsdl�ļ�������
 * 
 * @author dingsj
 * @since 2015-09-02
 */
public class WsdlUtil {
	private static final String wsdlUrl = "http://10.71.32.98:9001/guidService?wsdl";

	@SuppressWarnings("unchecked")
	public static void test1() {
		try {
			// ����factory
			WSDLFactory factory = WSDLFactory.newInstance();
			// ����WSDLReader�Ķ���
			WSDLReader reader = factory.newWSDLReader();
			// ����reader�Ķ�������
			ExtensionRegistry registry = factory.newPopulatedExtensionRegistry();
			reader.setExtensionRegistry(registry);
			reader.setFeature("javax.wsdl.verbose", true);
			reader.setFeature("javax.wsdl.importDocuments", true);
			// ����wsdl
			Definition def = reader.readWSDL(wsdlUrl);
			Service service = (Service) def.getServices().values().iterator().next();
			Port port = (Port) service.getPorts().values().iterator().next();
			Binding binding = port.getBinding();
			PortType pt = binding.getPortType();
			String TargetNamespace = def.getTargetNamespace();
			System.out.println("TargetNamespace��" + TargetNamespace);
			List<Operation> operations = pt.getOperations();
			Iterator<Operation> itr = operations.iterator();
			String method = "";
			while (itr.hasNext()) {
				Operation operation = itr.next();
				method = operation.getName();
				System.out.println("method:" + method);
				/** ������� */
				Input input = operation.getInput();
				if (input != null) {
					Message message = input.getMessage();
					List<?> paramList = operation.getParameterOrdering();
					List<Part> partList = message.getOrderedParts(paramList);
					for (Part part : partList) {
						System.out.println("�������:" + part.toString());
					}
				}
				/** ������� */
				Output output = operation.getOutput();
				if (input != null) {
					Message message = output.getMessage();
					List<Part> partList = message.getOrderedParts(null);
					for (Part part : partList) {
						System.out.println("�������:" + part.toString());
					}
				}
			}
			// System.out.println(new QName(TargetNamespace,
			// method).getLocalPart());
			/** ���� */
		} catch (WSDLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void test2() {
		try {
			// ����factory
			WSDLFactory factory = WSDLFactory.newInstance();
			// ����WSDLReader�Ķ���
			WSDLReader reader = factory.newWSDLReader();
			// ����reader�Ķ�������
			ExtensionRegistry registry = factory.newPopulatedExtensionRegistry();
			reader.setExtensionRegistry(registry);
			reader.setFeature("javax.wsdl.verbose", true);
			reader.setFeature("javax.wsdl.importDocuments", true);
			// ����wsdl
			Definition def = reader.readWSDL(wsdlUrl);
			Map<?,Service> map = def.getServices();
			System.out.println("��ȡService�Ĵ�С��"+map.size());
			for(Map.Entry<?, Service> entry : map.entrySet()){
				entry.getKey();
				Service service = entry.getValue();
				Map<?,Port> portMap = service.getPorts();
				System.out.println("��ȡportMap�Ĵ�С��"+portMap.size());
				for(Map.Entry<?, Port> port_entry : portMap.entrySet()){
					port_entry.getKey();
					Port port = port_entry.getValue();
					Binding binding = port.getBinding();
					PortType portType = binding.getPortType();
					List<Operation> operationList = portType.getOperations();
					System.out.println("��ȡoperationList�Ĵ�С��"+operationList.size());
					for(Operation operation : operationList){
					}
				}
			}
		} catch (WSDLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		test2();
	}
}
