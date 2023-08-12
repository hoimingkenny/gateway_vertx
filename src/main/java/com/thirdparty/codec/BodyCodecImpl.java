package com.thirdparty.codec;

import com.alipay.remoting.serialization.SerializerManager;
import com.thirdparty.checksum.CheckSumImpl;

import java.io.Serializable;


public class BodyCodecImpl implements IBodyCodec
{
	// 1. object -> byte[]
	@Override
	public <T> byte[] serialize(T obj) throws Exception {
		//1. jdk 序列化 //2. json //3.自定义算法（Hessian2）
		return SerializerManager.getSerializer(SerializerManager.Hessian2).serialize(obj);
	}

	// 2. byte[] -> object
	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
		return SerializerManager.getSerializer(SerializerManager.Hessian2).deserialize(bytes,clazz.getName());
	}

	static class A implements Serializable {
		public String a;
	}

	public static void main(String[] args) throws Exception {
//		A a = new A();
//		a.a = "test";
//
//		byte[] serialize = new BodyCodecImpl().serialize(a);
//		A deserialize = new BodyCodecImpl().deserialize(serialize, A.class);
//
//		System.out.println(deserialize.a);

		String a = "test";
		String b = "test1";
		String c = "test";
		byte ca = new CheckSumImpl().getChecksum(a.getBytes());
		byte cb = new CheckSumImpl().getChecksum(b.getBytes());
		byte cc = new CheckSumImpl().getChecksum(c.getBytes());
		System.out.println(ca);
		System.out.println(cb);
		System.out.println(cc);
	}
}