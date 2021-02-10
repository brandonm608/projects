package com.example.hash;

import java.util.Iterator;

class KeySet<K, V> extends AbstractUnmodifiableHashSet<K> {
	private AbstractHashSet<HashKeyEntry<K, V>> hashMap;

	public KeySet(AbstractHashSet<HashKeyEntry<K, V>> hashMap) {
		this.hashMap = hashMap;
	}

	@Override
	public boolean contains(K obj) {
		return get(obj) != null;
	}

	@Override
	public int size() {
		return hashMap.size();
	}

	@Override
	public Iterator<K> iterator() {
		return new KeySetIterator<>(hashMap);
	}

	@Override
	protected K get(K obj) {
		HashKeyEntry<K, V> entry = hashMap.get(new HashKeyEntry<>(Map.entry(obj, null)));
		if (entry != null) {
			return entry.getKey();
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
