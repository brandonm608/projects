package com.example;

import java.io.IOException;
import java.util.UUID;

import com.example.hash.Cuckoo3HashSet;
import com.example.hash.Murmur3;

public class C3HTApplication {
	public static void main(String[] args) throws NumberFormatException, IOException {
		Murmur3<UUID> obj2Hash = Murmur3
				.obj2Hash((seed, id) -> Murmur3.hash(seed, id.getMostSignificantBits(), id.getLeastSignificantBits()));
		Cuckoo3HashSet<UUID> ht = new Cuckoo3HashSet<>(1843, obj2Hash);
		int count = 0;
		int dup = 0;
		UUID[] ids = new UUID[1843];
		int errorCount = 0;
		int items = 0;

		for (int i = 0; i < 1843; i++) {
			UUID id = UUID.randomUUID();

			if (ht.contains(id)) {
				System.out.println("Id=" + id.toString() + " duplicate i=" + i);
				dup++;
			}

			ht.add(id);
			boolean added = ht.contains(id);

			if (!added) {
				System.out.println("Id=" + id.toString() + " not added i=" + i);
				count++;
			} else {
				ids[items] = id;
				items++;
			}
		}

		for (int i = 0; i < items; i++) {
			if (!ht.contains(ids[i])) {
				System.out.println("ERROR ID NOT FOUND " + ids[i]);
				errorCount++;
			}
		}

		System.out.println("Hash table size: " + ht.size());
		System.out.println(count + " items not added");
		System.out.println(dup + " duplicate items");
		System.out.println(errorCount + " errors");
		System.out.println(errorCount + ht.size());
		ht.clear();
	}
}
