package com.example.hash;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import com.example.list.LinkedList;

class LinkedListHashSetIterator<E> implements Iterator<E> {
	private LinkedListHashSet<E> set;
	private int expectedModCount;
	private int pos;
	private int size;
	private int found;
	private Iterator<Object> listIt;

	protected void checkModCount() {
		if (set.getModCount() == expectedModCount) {
			return;
		}

		throw new ConcurrentModificationException();
	}

	protected void findNext() {
		LinkedList<Object>[] table = (LinkedList<Object>[]) set.getTable();

		if (listIt != null && listIt.hasNext()) {
			found++;
			return;
		}

		pos++;
		while (found < size && pos < table.length) {
			if (table[pos] != null) {
				listIt = table[pos].iterator();
				found++;
				return;
			} else {
				pos++;
			}
		}

		throw new IllegalStateException();
	}

	public LinkedListHashSetIterator(LinkedListHashSet<E> set) {
		this.set = set;
		expectedModCount = set.getModCount();
		pos = -1;
		size = set.size();
		found = 0;
		listIt = null;
	}

	@Override
	public boolean hasNext() {
		checkModCount();
		return found < size;
	}

	@Override
	public E next() {
		checkModCount();
		findNext();
		return (E) listIt.next();
	}

	@Override
	public void remove() {
		checkModCount();
		if (listIt != null) {
			listIt.remove();

			if (((LinkedList<Object>[]) set.getTable())[pos].size() <= 0) {
				set.unset(pos);
				listIt = null;
			}

			set.decrementSize();
			expectedModCount = set.getModCount();
			return;
		}

		throw new IllegalStateException("There is not a current element");
	}
}
