package com.thirdparty.uuid;

public class kUuid
{

	private static kUuid ourInstance = new kUuid();

	public static kUuid getInstance() {
		return ourInstance;
	}

	private kUuid() {
	}

	public void init(int centerId, int workerId) {
		idWorker = new SnowflakeIdWorker(workerId, centerId);
	}

	private SnowflakeIdWorker idWorker;

	public long getUUID() {
		return idWorker.nextId();
	}
}
