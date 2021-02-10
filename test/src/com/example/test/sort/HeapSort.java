package com.example.test.sort;

import java.util.Comparator;

public class HeapSort<T> extends AbstractSort<T> {
	protected int getParentIndex(int currentIndex) {
		if (currentIndex % 2 == 0) {
			return currentIndex / 2 - 1;
		}

		return currentIndex / 2;
	}

	protected int getLeftChildIndex(int currentIndex) {
		return 2 * currentIndex + 1;
	}

	protected int getRighChildIndex(int currentIndex) {
		return 2 * currentIndex + 2;
	}

	protected void heapify(T[] a) {
		if (a.length < 2)
			return;

		for (int i = 1; i < a.length; i++) {
			int currentIndex = i;
			int p = getParentIndex(currentIndex);
			while (p >= 0 && !(compare(a[currentIndex], a[p]) < 0)) {
				swap(a, p, currentIndex);
				currentIndex = p;
				p = getParentIndex(p);
			}
		}
	}

	protected void swapRoot(T[] a, int leafIndex) {
		swap(a, 0, leafIndex);
		siftRootDown(a, leafIndex);
	}

	protected void siftRootDown(T[] a, int size) {
		int currentIndex = 0;
		for (;;) {
			int l = getLeftChildIndex(currentIndex);
			int r = getRighChildIndex(currentIndex);

			if (l < size) {
				if (r < size) {
					if (!(compare(a[l], a[r]) < 0)) {
						if (!(compare(a[l], a[currentIndex]) < 0)) {
							swap(a, currentIndex, l);
							currentIndex = l;
						} else {
							break;
						}
					} else {
						if (!(compare(a[r], a[currentIndex]) < 0)) {
							swap(a, currentIndex, r);
							currentIndex = r;
						} else {
							break;
						}
					}
				} else {
					if (!(compare(a[l], a[currentIndex]) < 0)) {
						swap(a, currentIndex, l);
						currentIndex = l;
					} else {
						break;
					}
				}
			} else {
				break;
			}
		}
	}

	protected void swap(T[] a, int i, int j) {
		T tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
		tmp = null;
	}

	public HeapSort(Comparator<T> cmp) {
		super(cmp);
	}

	@Override
	public void sort(T[] a) {
		heapify(a);
		for (int i = a.length - 1; i >= 0; i--) {
			swapRoot(a, i);
		}
	}
}
