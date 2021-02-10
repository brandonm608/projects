package com.example.test.sort;

import java.util.Comparator;

public abstract class AbstractSort<T> implements Sort<T> {
	private Comparator<T> cmp;

	public AbstractSort(Comparator<T> cmp) {
		this.cmp = cmp;
	}

	protected int compare(T frist, T second) {
		return cmp.compare(frist, second);
	}
}
