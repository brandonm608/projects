package com.example.hash;

import java.util.Iterator;

class EntrySetIterator<K, V> implements Iterator<Map.Entry<K, V>> {
	private Iterator<HashKeyEntry<K, V>> it;

	public EntrySetIterator(AbstractHashSet<HashKeyEntry<K, V>> set) {
		it = set.iterator();
	}

	@Override
	public boolean hasNext() {
		return it.hasNext();
	}

	@Override
	public Map.Entry<K, V> next() {
		return it.next().getEntry();
	}

	@Override
	public void remove() {
		it.remove();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EntrySetIterator) {
			EntrySetIterator it = (EntrySetIterator) obj;
			return this.it.equals(it.it);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return it.hashCode();
	}
}
