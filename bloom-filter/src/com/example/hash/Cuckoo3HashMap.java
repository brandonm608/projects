package com.example.hash;

public class Cuckoo3HashMap<K, V> extends AbstractHashMap<K, V> {
	public Cuckoo3HashMap(Murmur3<K> hash) {
		this(0, hash);
	}

	public Cuckoo3HashMap(int seed, Murmur3<K> hash) {
		this(1024, seed, hash);

	}

	public Cuckoo3HashMap(int size, int seed, Murmur3<K> hash) {
		super(new Cuckoo3HashSet<>(size, seed, keyHash(hash)));
	}
}
