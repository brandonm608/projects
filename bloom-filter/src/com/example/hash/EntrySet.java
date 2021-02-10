package com.example.hash;

import java.util.Iterator;

class EntrySet<K, V> extends AbstractUnmodifiableHashSet<Map.Entry<K, V>> {
	private AbstractHashSet<HashKeyEntry<K, V>> hashMap;

	public EntrySet(AbstractHashSet<HashKeyEntry<K, V>> hashMap) {
		this.hashMap = hashMap;
	}

	@Override
	public boolean contains(Map.Entry<K, V> obj) {
		return get(obj) != null;
	}

	@Override
	public int size() {
		return hashMap.size();
	}

	@Override
	public Iterator<Map.Entry<K, V>> iterator() {
		return new EntrySetIterator<>(hashMap);
	}

	@Override
	protected Map.Entry<K, V> get(Map.Entry<K, V> obj) {
		if (obj != null) {
			HashKeyEntry<K, V> entry = hashMap.get(new HashKeyEntry<K, V>(Map.entry(obj.getKey(), obj.getValue())));
			if (entry != null) {
				return entry.getEntry();
			}
		}

		return null;
	}

	@Override
	protected int getModCount() {
		return hashMap.getModCount();
	}

	@Override
	protected Object[] getTable() {
		return hashMap.getTable();
	}
}
