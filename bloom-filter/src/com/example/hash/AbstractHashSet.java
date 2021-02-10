package com.example.hash;

abstract class AbstractHashSet<T> extends AbstractUnmodifiableHashSet<T> implements Set<T> {
	private transient int modCount;

	protected abstract T unset(int pos);

	@Override
	protected int getModCount() {
		return modCount;
	}

	protected void incrementModCount() {
		if (modCount < Integer.MAX_VALUE) {
			modCount++;
		} else {
			modCount = Integer.MIN_VALUE;
		}
	}

	public AbstractHashSet() {
		modCount = Integer.MIN_VALUE;
	}
}
