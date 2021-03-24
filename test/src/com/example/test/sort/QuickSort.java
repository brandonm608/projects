package com.example.test.sort;

import java.util.Comparator;

public class QuickSort<T> extends AbstractSort<T> {
	protected void swap(T[] a, int i, int j) {
		T tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
		tmp = null;
	}

	protected boolean isLessThan(final T first, final T second) {
		return compare(first, second) < 0;
	}

	protected void swapStartLessThanMid(final T[] a, final int start, final int mid, final int end) {
		if (isLessThan(a[start], a[end])) {
			if (isLessThan(a[mid], a[end])) {
				swap(a, start, mid);
			} else {
				swap(a, start, end);
			}
		} else {
			if (!isLessThan(a[start], a[mid])) {
				swap(a, start, mid);
			}
		}
	}

	protected void swapStartNotLessThanMid(final T[] a, final int start, final int mid, final int end) {
		if (isLessThan(a[mid], a[end])) {
			if (!isLessThan(a[start], a[end])) {
				swap(a, start, end);
			}
		} else {
			swap(a, start, mid);
		}
	}

	protected void medianOf3Swap(final T[] a, final int start, final int mid, final int end) {
		if (isLessThan(a[start], a[mid])) {
			swapStartLessThanMid(a, start, mid, end);
		} else {
			swapStartNotLessThanMid(a, start, mid, end);
		}
	}

	protected void quickSort(T[] a, final int start, final int end) {
		int i;
		int j;
		final T pivot;

		if (end <= start) {
			return;
		}

		medianOf3Swap(a, start, (end - start + 1) / 2, end);

		pivot = a[start];

		for (i = start, j = end; i <= j;) {
			while (compare(a[i], pivot) < 0) {
				i++;
			}

			while (compare(a[j], pivot) > 0) {
				j--;
			}

			if (i <= j) {
				swap(a, i, j);
				i++;
				j--;
			}
		}

		quickSort(a, start, i - 1);
		quickSort(a, i, end);
	}

	public QuickSort(Comparator<T> cmp) {
		super(cmp);
	}

	@Override
	public void sort(T[] a) {
		quickSort(a, 0, a.length - 1);
	}
}
