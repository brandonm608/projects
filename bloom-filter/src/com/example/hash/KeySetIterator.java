package com.example.hash;

import java.util.Iterator;

class KeySetIterator<K, V> implements Iterator<K> {
	private Iterator<HashKeyEntry<K, V>> it;

	public KeySetIterator(AbstractHashSet<HashKeyEntry<K, V>> set) {
		it = set.iterator();
	}

	@Override
	public boolean hasNext() {
		return it.hasNext();
	}

	@Override
	public K next() {
		return it.next().getKey();
	}

	@Override
	public void remove() {
		it.remove();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KeySetIterator) {
			KeySetIterator it = (KeySetIterator) obj;
			return this.it.equals(it.it);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return it.hashCode();
	}
}
