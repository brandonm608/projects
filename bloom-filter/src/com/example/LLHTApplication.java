package com.example;

import java.util.UUID;

import com.example.hash.LinkedListHashSet;

public class LLHTApplication {
	public static void main(String[] args) {
		int count = 2048;
		LinkedListHashSet<UUID> ht = new LinkedListHashSet<>();
		UUID[] ids = new UUID[count];

		for (int i = 0; i < count; i++) {
			ids[i] = UUID.randomUUID();
			ht.add(ids[i]);
		}

		for (int i = 0; i < count; i++) {
			if (!ht.contains(ids[i])) {
				System.out.println("ERROR");
			}
		}

		System.out.println("Final size: " + ht.size());
		ht.clear();
	}
}
