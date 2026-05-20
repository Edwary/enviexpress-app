package com.enviexpres.logistica.admmodule.utils;

import java.util.Comparator;
import java.util.Map;

public class IntComparator implements Comparator<Map<String, Object>> {
	
	private String key;
	
	public IntComparator(String key) {
		this.key = key;
	}

	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		Integer id1 = Integer.valueOf(String.valueOf(o1.get(key)));
		Integer id2 = Integer.valueOf(String.valueOf(o2.get(key)));
		return id1.compareTo(id2);
	}
}
