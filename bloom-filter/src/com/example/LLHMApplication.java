package com.example;

import com.example.hash.LinkedListHashMap;
import com.example.hash.Map;

public class LLHMApplication {
	public static void main(String[] args) {
		Map<Integer, Integer> map = new LinkedListHashMap<>(8);

		for (int i = 0; i < 5; i++) {
			map.put(i, i);
		}

		System.out.println("Keys");
		for (int key : map.keySet()) {
			System.out.println(key);
		}

		map.put(3, 0);
		map.remove(4);
		System.out.println("Entries");
		for (var entry : map.entrySet()) {
			System.out.println(entry.getKey() + ", " + entry.getValue());
		}
	}
}
