package com.example.hash;

import com.example.hash.Map.Entry;

class HashKeyEntry<K, V> {
	private Entry<K, V> entry;

	public HashKeyEntry(Entry<K, V> entry) {
		this.entry = entry;
	}

	public K getKey() {
		return entry.getKey();
	}

	public Entry<K, V> getEntry() {
		return entry;
	}

	@Override
	public int hashCode() {
		return entry.getKey().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HashKeyEntry) {
			HashKeyEntry hke = (HashKeyEntry) obj;
			return entry.getKey().equals(hke.getKey());
		}

		return false;
	}
}
