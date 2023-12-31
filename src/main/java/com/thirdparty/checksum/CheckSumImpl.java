package com.thirdparty.checksum;

public class CheckSumImpl implements ICheckSum
{
	@Override
	public byte getChecksum(byte[] data)
	{
		byte sum = 0;

		for (byte b: data)
		{
			sum ^= b;
		}

		return sum;
	}
}
