package cn.entersoft.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * �������л��������л������л�����תbyte[],ByteBuffer, byte[]תobject
 * 
 * @author Vicky
 * @email eclipser@163.com
 */
public class ByteUtil {

	public static byte[] getBytes(Object obj) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bout);
		out.writeObject(obj);
		out.flush();
		byte[] bytes = bout.toByteArray();
		bout.close();
		out.close();
		return bytes;
	}

	public static Object getObject(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = new ObjectInputStream(bi);
		Object obj = oi.readObject();
		bi.close();
		oi.close();
		return obj;
	}

//	public static Object getObject(ByteBuffer byteBuffer) throws ClassNotFoundException, IOException {
//		// ��Ҫmina��ܵ�IoBuffer
//		IoBuffer buffer = IoBuffer.allocate(byteBuffer.capacity()).setAutoExpand(true); // �Զ�չ��
//		for (int i = 0; i < byteBuffer.capacity(); i++) {
//			byteBuffer.position(i);
//			buffer.put(byteBuffer.get());
//		}
//		buffer.position(0);
//		InputStream input = buffer.asInputStream();
//		ObjectInputStream oi = new ObjectInputStream(input);
//		Object obj = oi.readObject();
//		input.close();
//		oi.close();
//		return obj;
//	}

	public static ByteBuffer getByteBuffer(Object obj) throws IOException {
		byte[] bytes = ByteUtil.getBytes(obj);
		ByteBuffer buff = ByteBuffer.wrap(bytes);
		return buff;
	}

}
