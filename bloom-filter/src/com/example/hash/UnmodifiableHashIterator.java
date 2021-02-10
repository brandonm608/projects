package com.example.hash;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class UnmodifiableHashIterator<E> implements Iterator<E> {
	private AbstractUnmodifiableHashSet<E> set;
	private int pos;
	private int found;
	private int size;
	private int expectedModCount;

	protected void checkModCount() {
		if (expectedModCount != set.getModCount()) {
			throw new ConcurrentModificationException();
		}
	}

	protected int getPos() {
		return pos;
	}

	protected void setExpectedModCount(int expectedModCount) {
		this.expectedModCount = expectedModCount;
	}

	protected void findNext() {
		Object[] table = set.getTable();

		while (found < size && pos < table.length) {
			if (table[pos] == null) {
				pos++;
			} else {
				found++;
				table = null;
				return;
			}
		}

		table = null;
		throw new IllegalStateException("There is not a next element");
	}

	public UnmodifiableHashIterator(AbstractUnmodifiableHashSet<E> set) {
		if (set == null) {
			throw new IllegalArgumentException("The set argument must not be null");
		}

		this.set = set;
		this.pos = -1;
		this.found = 0;
		this.size = set.size();
		this.expectedModCount = set.getModCount();
	}

	@Override
	public boolean hasNext() {
		checkModCount();
		return found < size;
	}

	@Override
	public E next() {
		pos++;
		checkModCount();
		findNext();
		return (E) set.getTable()[pos];
	}

	@Override
	public void remove() {
		throw new IllegalAccessError("Unable to remove an item from an unmodifiable set");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof HashIterator) {
			UnmodifiableHashIterator it = (UnmodifiableHashIterator) obj;
			return this.set.equals(it.set) && this.pos == it.pos && this.found == it.found
					&& this.expectedModCount == it.expectedModCount;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return set.getTable().hashCode();
	}
}
