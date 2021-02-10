package com.example.hash;

public class LinkedListHashMap<K, V> extends AbstractHashMap<K, V> {
	public LinkedListHashMap() {
		this(1024);
	}

	public LinkedListHashMap(int size) {
		super(new LinkedListHashSet<>(size));
	}
}
