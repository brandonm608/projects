package com.example.test.sort;

import java.util.Arrays;
import java.util.Comparator;

public class MergeSort<T> extends AbstractSort<T> {
	public MergeSort(Comparator<T> cmp) {
		super(cmp);
	}

	protected void merge(T[] a, T[] b, T[] ab) {
		int i;
		int j = 0;
		int k = 0;

		for (i = 0; j < a.length && k < b.length; i++) {
			if (compare(a[j], b[k]) < 0) {
				ab[i] = a[j];
				a[j] = null;
				j++;
			} else {
				ab[i] = b[k];
				b[k] = null;
				k++;
			}
		}

		while (k < b.length) {
			ab[i] = b[k];
			b[k] = null;
			i++;
			k++;
		}

		while (j < a.length) {
			ab[i] = a[j];
			a[j] = null;
			i++;
			j++;
		}
	}

	protected void partition(T[] a) {
		int partition;

		if (a.length < 2) {
			return;
		}

		partition = a.length / 2;

		if (partition > 0) {
			T[] b = Arrays.copyOfRange(a, 0, partition);
			T[] c = Arrays.copyOfRange(a, partition, a.length);

			partition(b);
			partition(c);
			merge(b, c, a);

			b = null;
			c = null;
		}
	}

	@Override
	public void sort(T[] a) {
		partition(a);
	}
}
