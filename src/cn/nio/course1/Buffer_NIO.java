package cn.nio.course1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO中buffer使用教程 
 * @author Administrator
 */
public class Buffer_NIO {
	
	public static void main(String[] args) {
		try {
			RandomAccessFile aFile = new RandomAccessFile("src/resource/test.xml","rw");
			FileChannel fileChannel = aFile.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(1024);
			int bytesRead = fileChannel.read(buf);
			while(bytesRead != -1){
				buf.flip();
				while(buf.hasRemaining()){
					System.out.println((char)buf.get());
				}
				buf.clear();
				bytesRead = fileChannel.read(buf);
			}
			aFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
