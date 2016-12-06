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
 * 解析wsdl文件辅助类
 * 
 * @author dingsj
 * @since 2015-09-02
 */
public class WsdlUtil {
	private static final String wsdlUrl = "http://10.71.32.98:9001/guidService?wsdl";

	@SuppressWarnings("unchecked")
	public static void test1() {
		try {
			// 创建factory
			WSDLFactory factory = WSDLFactory.newInstance();
			// 生成WSDLReader阅读器
			WSDLReader reader = factory.newWSDLReader();
			// 设置reader阅读器属性
			ExtensionRegistry registry = factory.newPopulatedExtensionRegistry();
			reader.setExtensionRegistry(registry);
			reader.setFeature("javax.wsdl.verbose", true);
			reader.setFeature("javax.wsdl.importDocuments", true);
			// 读入wsdl
			Definition def = reader.readWSDL(wsdlUrl);
			Service service = (Service) def.getServices().values().iterator().next();
			Port port = (Port) service.getPorts().values().iterator().next();
			Binding binding = port.getBinding();
			PortType pt = binding.getPortType();
			String TargetNamespace = def.getTargetNamespace();
			System.out.println("TargetNamespace：" + TargetNamespace);
			List<Operation> operations = pt.getOperations();
			Iterator<Operation> itr = operations.iterator();
			String method = "";
			while (itr.hasNext()) {
				Operation operation = itr.next();
				method = operation.getName();
				System.out.println("method:" + method);
				/** 输入参数 */
				Input input = operation.getInput();
				if (input != null) {
					Message message = input.getMessage();
					List<?> paramList = operation.getParameterOrdering();
					List<Part> partList = message.getOrderedParts(paramList);
					for (Part part : partList) {
						System.out.println("输入参数:" + part.toString());
					}
				}
				/** 输出参数 */
				Output output = operation.getOutput();
				if (input != null) {
					Message message = output.getMessage();
					List<Part> partList = message.getOrderedParts(null);
					for (Part part : partList) {
						System.out.println("输出参数:" + part.toString());
					}
				}
			}
			// System.out.println(new QName(TargetNamespace,
			// method).getLocalPart());
			/** 参数 */
		} catch (WSDLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void test2() {
		try {
			// 创建factory
			WSDLFactory factory = WSDLFactory.newInstance();
			// 生成WSDLReader阅读器
			WSDLReader reader = factory.newWSDLReader();
			// 设置reader阅读器属性
			ExtensionRegistry registry = factory.newPopulatedExtensionRegistry();
			reader.setExtensionRegistry(registry);
			reader.setFeature("javax.wsdl.verbose", true);
			reader.setFeature("javax.wsdl.importDocuments", true);
			// 读入wsdl
			Definition def = reader.readWSDL(wsdlUrl);
			Map<?,Service> map = def.getServices();
			System.out.println("获取Service的大小："+map.size());
			for(Map.Entry<?, Service> entry : map.entrySet()){
				entry.getKey();
				Service service = entry.getValue();
				Map<?,Port> portMap = service.getPorts();
				System.out.println("获取portMap的大小："+portMap.size());
				for(Map.Entry<?, Port> port_entry : portMap.entrySet()){
					port_entry.getKey();
					Port port = port_entry.getValue();
					Binding binding = port.getBinding();
					PortType portType = binding.getPortType();
					List<Operation> operationList = portType.getOperations();
					System.out.println("获取operationList的大小："+operationList.size());
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
