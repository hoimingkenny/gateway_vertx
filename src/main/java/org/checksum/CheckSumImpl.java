package org.checksum;

public class ICheckSumImpl implements ICheckSum
{
	@Override
	public byte getChecksum(byte[] data)
	{
		byte sum = 0;

		for (byte b: data)
		{
			sum ^= b;
		}

		return 0;
	}
}
