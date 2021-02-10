package com.example.test.sort;

import java.util.Arrays;
import java.util.Comparator;

public class HybridSort2<T> extends AbstractSort<T> {
	protected void specialSort3(final T[] sequence, final int start, final int end) {
		final int tmpIdx2 = start + 1;

		if (isLessThan(sequence[start], sequence[tmpIdx2])) {
			if (isLessThan(sequence[tmpIdx2], sequence[end])) {
				return;
			} else {
				if (isLessThan(sequence[start], sequence[end])) {
					swap(sequence, tmpIdx2, end);
				} else {
					final T tmp = sequence[end];
					sequence[end] = sequence[tmpIdx2];
					sequence[tmpIdx2] = sequence[start];
					sequence[start] = tmp;
				}
			}
		} else {
			if (isLessThan(sequence[start], sequence[end])) {
				swap(sequence, start, tmpIdx2);
			} else if (isLessThan(sequence[tmpIdx2], sequence[end])) {
				final T tmp = sequence[end];
				sequence[end] = sequence[start];
				sequence[start] = sequence[tmpIdx2];
				sequence[tmpIdx2] = tmp;
			} else {
				swap(sequence, start, end);
			}
		}
	}

	protected void specialSort(final T[] a, final int start, final int end) {
		int i = start + 2;
		specialSort3(a, start, i);

		for (; i < end; i++) {
			for (int k = i; k >= start; k--) {
				if (!(compare(a[k], a[k + 1]) < 0)) {
					swap(a, k, k + 1);
				} else {
					break;
				}
			}
		}
	}

	protected void swap(final T[] a, final int i, final int j) {
		final T tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	protected void merge(final T[] a, final T[] b, int j, int end) {
		int i;
		int k = b.length - 1;

		j--;
		for (i = end - 1; j >= 0 && k >= 0; i--) {
			if (!(compare(a[j], b[k]) < 0)) {
				a[i] = a[j];
				j--;
			} else {
				a[i] = b[k];
				b[k] = null;
				k--;
			}
		}

		while (k >= 0) {
			a[i] = b[k];
			i--;
			k--;
		}
	}

	protected void partition(final T[] a, final int end) {
		int partition;

		if (end < 1000) {
			hybridSort(a, 0, end - 1);
			return;
		}

		partition = end / 2;

		final T[] b = Arrays.copyOfRange(a, partition, end);

		partition(a, partition);
		partition(b, b.length);

		merge(a, b, partition, end);
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

	protected int quickSortHelp(final T[] a, final int start, int pivot, int j) {
		for (int i = start + 1; j > pivot;) {
			if (compare(a[i], a[pivot]) < 0) {
				swap(a, i, pivot);
				pivot = i;
				i++;
			} else {
				swap(a, i, j);
				j--;
			}
		}

		return pivot;
	}

	protected void quickSort(final T[] a, final int start, final int end) {
		final int pivot;
		final int mid = (end - start + 1) / 2;

		medianOf3Swap(a, start, mid, end);

		pivot = quickSortHelp(a, start, start, end);

		hybridSort(a, start, pivot);
		hybridSort(a, pivot + 1, end);
	}

	protected void hybridSort(final T[] a, final int start, final int end) {
		if (end - start > 8) {
			quickSort(a, start, end);
			return;
		}

		// 7 to 3 items
		if (end - start > 1) {
			specialSort(a, start, end);
			return;
		}

		// 2 items
		if (end - start > 0) {
			if (isLessThan(a[end], a[start])) {
				swap(a, start, end);
			}
		}
	}

	public HybridSort2(final Comparator<T> cmp) {
		super(cmp);
	}

	@Override
	public void sort(final T[] a) {
		partition(a, a.length);
	}
}
