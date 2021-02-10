package com.example.hash;

public class CuckooHashMap<K, V> extends AbstractHashMap<K, V> {
	public CuckooHashMap(Murmur3<K> hash) {
		this(0, hash);
	}

	public CuckooHashMap(int seed, Murmur3<K> hash) {
		this(1024, seed, hash);

	}

	public CuckooHashMap(int size, int seed, Murmur3<K> hash) {
		super(new CuckooHashSet<>(size, seed, keyHash(hash)));
	}
}
