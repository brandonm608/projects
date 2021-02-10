package com.example.hash;

public interface Set<T> extends UnmodifiableSet<T> {
	public void add(T obj);

	public void clear();

	public boolean remove(T obj);
}
