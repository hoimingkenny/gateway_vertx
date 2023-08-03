package org.codec;

import com.alipay.remoting.serialization.SerializerManager;
import org.checksum.ICheckSum;

import java.io.Serializable;

public class IBodyCodecImpl implements IBodyCodec
{
	@Override
	public <T> byte[] serialize(T obj) throws Exception {
		//1. jdk 序列化 //2. json //3.自定义算法（Hessian2）
		return SerializerManager.getSerializer(SerializerManager.Hessian2).serialize(obj);
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
		return SerializerManager.getSerializer(SerializerManager.Hessian2).deserialize(bytes,clazz.getName());
	}


	public static void main(String[] args) throws Exception {
		String a = "test";
		String b = "test1";
		String c = "test";
		byte ca = new ICheckSum().getChecksum(a.getBytes());
		byte cb = new ICheckSum().getChecksum(b.getBytes());
		byte cc = new ICheckSum().getChecksum(c.getBytes());
		System.out.println(ca);
		System.out.println(cb);
		System.out.println(cc);


	}
}
