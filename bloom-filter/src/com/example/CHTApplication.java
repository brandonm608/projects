package com.example;

import java.io.IOException;
import java.util.UUID;

import com.example.hash.CuckooHashSet;
import com.example.hash.Murmur3;

public class CHTApplication {
	private static Murmur3<UUID> hash() {
		return Murmur3
				.obj2Hash((seed, id) -> Murmur3.hash(seed, id.getMostSignificantBits(), id.getLeastSignificantBits()));
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		CuckooHashSet<UUID> ht = new CuckooHashSet<>(1024, CHTApplication.hash());
		int count = 0;
		UUID[] ids = new UUID[1024];
		int errorCount = 0;
		int items = 0;

		for (int i = 0; i < 1024; i++) {
			UUID id = UUID.randomUUID();

			ht.add(id);

			if (!ht.contains(id)) {
				System.out.println("Id=" + id.toString() + " not added i=" + i);
				count++;
			} else {
				ids[items] = id;
				items++;
			}
		}

		for (int i = 0; i < items; i++) {
			if (!ht.contains(ids[i])) {
				System.out.println("ERROR ID NOT FOUND");
				errorCount++;
			}
		}

		System.out.println("Hash table size: " + ht.size());
		System.out.println(count + " items not added");
		System.out.println(errorCount + " errors");
		System.out.println(errorCount + ht.size());
	}
}
