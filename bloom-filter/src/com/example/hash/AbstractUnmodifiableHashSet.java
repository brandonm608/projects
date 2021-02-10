package com.example.hash;

public abstract class AbstractUnmodifiableHashSet<T> implements UnmodifiableSet<T>, Iterable<T> {
	protected abstract T get(T obj);

	protected abstract int getModCount();

	protected abstract Object[] getTable();
}
