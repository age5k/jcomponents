package com.age5k.jcps.dataversion;

import java.util.HashMap;
import java.util.Map;

public class DataVersion {

	private static Map<Integer, Map<Integer, Map<Integer, DataVersion>>> VERSION_MAP = new HashMap<>();

	private int[] verionNumbers;

	private DataVersion(int major, int minor, int variant) {
		this.verionNumbers = new int[] { major, minor, variant };
	}

	public static DataVersion valueOf(int i, int j, int k) {
		Map<Integer, Map<Integer, DataVersion>> map1 = VERSION_MAP.get(i);
		if (map1 == null) {
			map1 = new HashMap<>();
			VERSION_MAP.put(i, map1);
		}
		Map<Integer, DataVersion> map2 = map1.get(j);
		if (map2 == null) {
			map2 = new HashMap<>();
			map1.put(j, map2);
		}

		DataVersion dv = map2.get(k);

		if (dv == null) {
			dv = new DataVersion(i, j, k);
			map2.put(k, dv);
		}

		return dv;
	}

	public int getMajor() {
		return this.verionNumbers[0];
	}

	public int getMinor() {
		return this.verionNumbers[1];
	}

	public int getVariant() {
		return this.verionNumbers[2];
	}

	public static DataVersion valueOf(String value) {
		//
		String[] vs = value.split("\\.");
		int ma = Integer.parseInt(vs[0]);
		int mi = Integer.parseInt(vs[1]);
		int va = Integer.parseInt(vs[2]);

		return valueOf(ma, mi, va);
	}

	public boolean isGreatOrEquals(DataVersion dv) {
		for (int i = 0; i < 3; i++) {
			if (this.verionNumbers[i] > dv.verionNumbers[i]) {
				return true;
			} else if (this.verionNumbers[i] < dv.verionNumbers[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return this.verionNumbers[0] + "." + this.verionNumbers[1] + "." + this.verionNumbers[2];
	}

}
