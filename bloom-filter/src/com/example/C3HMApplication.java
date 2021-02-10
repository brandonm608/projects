package com.example;

import com.example.hash.Cuckoo3HashMap;
import com.example.hash.Map;
import com.example.hash.Murmur3;

public class C3HMApplication {
	public static void main(String args[]) {
		Map<Integer, Integer> m = new Cuckoo3HashMap<Integer, Integer>(Murmur3.murmur3Int());

		m.put(0, 0);
		m.put(1, 1);
		m.put(2, 2);
		m.put(3, 3);

		m.put(0, 1);
		m.put(1, 2);
		m.put(2, 4);
		m.put(3, 6);

		System.out.println("Keys:");
		for(var key:m.keySet()) {
			System.out.println(key);
		}
		System.out.println();
		m.remove(2);

		for (int i = 0; i < 4; i++) {
			System.out.println(m.get(i));
		}

		System.out.println();

		for (Integer key : m.keySet()) {
			System.out.println(key);
		}

		System.out.println();

		for (Map.Entry<Integer, Integer> entry : m.entrySet()) {
			System.out.println(entry.getKey() + ", " + entry.getValue());
		}

		m.clear();
	}
}
