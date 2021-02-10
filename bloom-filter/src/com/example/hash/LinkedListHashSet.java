package com.example.hash;

import java.util.Iterator;

import com.example.list.LinkedList;

public class LinkedListHashSet<T> extends AbstractHashSet<T> {
	private LinkedList<Object>[] table;
	private int pow;
	private int size;

	private void resize() {
		if ((1 << pow) <= 0) {
			return;
		}

		LinkedList<Object>[] oldTable = table;

		pow++;
		table = new LinkedList[1 << pow];
		size = 0;

		for (int i = 0; i < oldTable.length; i++) {
			LinkedList<Object> list = oldTable[i];
			if (list == null) {
				continue;
			}

			for (;;) {
				T obj = (T) list.remove();
				if (obj == null) {
					break;
				}

				this.add(obj);
			}

			list.clear();
		}

		oldTable = null;
	}

	@Override
	protected T get(T obj) {
		if (obj == null) {
			return null;
		}

		int hash = hash(obj);
		Iterator<Object> it = itForHash(hash);

		return (T) lookup(obj, it);
	}

	@Override
	protected Object[] getTable() {
		return table;
	}

	@Override
	protected T unset(int pos) {
		table[pos].clear();
		table[pos] = null;
		return null;
	}

	protected void decrementSize() {
		size--;
		incrementModCount();
	}

	protected int hash(T obj) {
		return (obj.hashCode() & ((1 << pow) - 1));
	}

	protected Iterator<Object> itForHash(int hash) {
		if (table[hash] != null) {
			return table[hash].iterator();
		}

		return null;
	}

	protected Object lookup(T obj, Iterator<Object> it) {
		if (it != null) {
			while (it.hasNext()) {
				T tmp = (T) it.next();
				if (tmp.equals(obj)) {
					return tmp;
				}
			}
		}

		return null;
	}

	public LinkedListHashSet() {
		this(1024);
	}

	public LinkedListHashSet(int size) {
		pow = (int) Math.ceil(Math.log(size) / Math.log(2));
		if ((1 << pow) < 0) {
			throw new IllegalArgumentException("Size must be less than or equal to 1073741824");
		}
		table = new LinkedList[1 << pow];
		size = 0;
	}

	@Override
	public boolean contains(T obj) {
		if (obj == null) {
			return false;
		}

		T tmp;
		int hash = hash(obj);
		Iterator<Object> it;

		if (table[hash] == null)
			return false;

		it = table[hash].iterator();
		while (it.hasNext()) {
			tmp = (T) it.next();
			if (tmp.equals(obj)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void add(T obj) {
		if (obj == null) {
			return;
		}

		int hash = hash(obj);
		Iterator<Object> it;
		incrementModCount();

		if (table[hash] == null) {
			table[hash] = new LinkedList<>();
			table[hash].add(obj);
			size++;
			return;
		}

		it = itForHash(hash);
		if (lookup(obj, it) != null) {
			it.remove();
			table[hash].add(obj);
			return;
		}

		table[hash].add(obj);
		size++;

		if (table[hash].size() > 6) {
			resize();
		}
	}

	@Override
	public void clear() {
		incrementModCount();
		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				table[i].clear();
				table[i] = null;
			}
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new LinkedListHashSetIterator<>(this);
	}

	@Override
	public boolean remove(T obj) {
		if (obj == null) {
			return false;
		}

		int hash = hash(obj);
		Iterator<Object> it;

		if (table[hash] == null)
			return false;

		it = itForHash(hash);
		if (lookup(obj, it) != null) {
			it.remove();
			if (table[hash].size() <= 0) {
				table[hash] = null;
			}
			size--;
			return true;
		}

		return false;
	}

	@Override
	public int size() {
		return size;
	}
}
