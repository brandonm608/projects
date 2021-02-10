package com.example;

import java.util.UUID;

import com.example.filter.CuckooFilter;
import com.example.hash.Murmur3;

public class CFApplication {
	private static Integer fingerPrint(UUID id) {
		return Integer.valueOf((int) Murmur3.hash(16, id.getMostSignificantBits(), id.getMostSignificantBits())
				.getLeastSignificantBits() & ((1 << 8) - 1));
	}

	private static Integer hash(UUID id) {
		return Integer.valueOf((int) Murmur3.hash(11, id.getLeastSignificantBits(), id.getMostSignificantBits())
				.getLeastSignificantBits());
	}

	private static Integer hash2(Integer fp) {
		return Integer.valueOf((int) Murmur3.hash(23, fp).getLeastSignificantBits());
	}

	public static void main(String[] args) {
		int pow = 20;
		int count = 500000;
		int added = 0;
		int notFound = 0;
		CuckooFilter<UUID, Integer> cf = new CuckooFilter<>((1 << (pow)), CFApplication::hash, CFApplication::hash2,
				CFApplication::fingerPrint);
		UUID[] ids = new UUID[count];

		for (int i = 0; i < count; i++) {
			ids[i] = UUID.randomUUID();
			if (cf.add(ids[i])) {
				if (!cf.exists(ids[i])) {
					System.out.println("ERROR");
				}
				added++;
			}
		}

		for (int i = 0; i < ids.length; i++) {
			if (!cf.exists(ids[i])) {
				notFound++;
			}
		}

		System.out.println("number of IDs=" + ids.length);
		System.out.println("filter size=" + cf.size());
		System.out.println("Added: " + added);
		System.out.println("Not found: " + notFound);
		if (notFound > (ids.length - added)) {
			System.out.println("ERROR");
		}
	}
}
