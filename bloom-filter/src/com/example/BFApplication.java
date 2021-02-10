package com.example;

import java.util.UUID;

import com.example.filter.BloomFilter;

public class BFApplication {
	public static void main(String[] args) {
		UUID id1 = UUID.randomUUID();
		UUID id2 = UUID.randomUUID();
		BloomFilter filter = new BloomFilter();

		filter.add(new BloomFilter.Hash(id1));
		System.out.println("Query for id1(true): " + filter.query(new BloomFilter.Hash(id1)));
		System.out.println("Query for id2(false): " + filter.query(new BloomFilter.Hash(id2)));
	}
}
