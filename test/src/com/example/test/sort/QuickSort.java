package com.example.test.sort;

import java.util.Comparator;

public class QuickSort<T> extends AbstractSort<T> {
	protected void swap(T[] a, int i, int j) {
		T tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
		tmp = null;
	}

	protected void quickSort(T[] a, int start, int end) {
		int pivot;
		int j;
		int test = end - start + 1;

		if (test < 2) {
			return;
		}

		if (test == 2) {
			if (compare(a[end], a[start]) < 0) {
				swap(a, start, end);
			}

			return;
		}

		pivot = end;
		j = start;

		for (int i = end - 1; j < pivot;) {
			if (compare(a[pivot], a[i]) < 0) {
				swap(a, i, pivot);
				pivot = i;
				i--;
			} else {
				swap(a, i, j);
				j++;
			}
		}

		quickSort(a, start, pivot - 1);
		quickSort(a, pivot, end);
	}

	public QuickSort(Comparator<T> cmp) {
		super(cmp);
	}

	@Override
	public void sort(T[] a) {
		quickSort(a, 0, a.length - 1);
	}
}
