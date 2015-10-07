package com.adtheorent.UnitTesting.Utils;

import com.eaio.uuid.UUID;

public class UID {

	public static String generateUID() {
		UUID id = new UUID(); // eaio-uuid, faster than java.util.uuid
		return id.toString();
	}
}
