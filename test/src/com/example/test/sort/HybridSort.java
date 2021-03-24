package com.example.test.sort;

import java.util.Arrays;
import java.util.Comparator;

public class HybridSort<T> extends AbstractSort<T> {
	protected boolean almostSort(T[] sequence, int start, int end) {
		int skipped;
		int tmpIdx1, tmpIdx2;

		end++;
		if (end - start < 2) {
			return true;
		}
		if (end - start == 2) {
			if (compare(sequence[start], sequence[end - 1]) < 0) {
				return true;
			} else {
				swap(sequence, start, end - 1);
				return true;
			}
		}

		tmpIdx1 = start;
		tmpIdx2 = start + 1;
		skipped = -1;

		for (int i = start + 2; i < end; i++) {
			if (compare(sequence[tmpIdx1], sequence[tmpIdx2]) < 0) {
				if (compare(sequence[tmpIdx2], sequence[i]) < 0) {
					tmpIdx1 = tmpIdx2;
					tmpIdx2 = i;
				} else {
					if (skipped > -1) {
						return false;
					}

					if (compare(sequence[tmpIdx1], sequence[i]) < 0) {
						skipped = tmpIdx2;
						tmpIdx2 = i;
					} else {
						skipped = i;
					}
				}
			} else {
				if (skipped > -1) {
					return false;
				}

				if (compare(sequence[tmpIdx1], sequence[i]) < 0) {
					skipped = tmpIdx2;
					tmpIdx2 = i;
				} else if (compare(sequence[tmpIdx2], sequence[i]) < 0) {
					skipped = tmpIdx1;
					tmpIdx1 = tmpIdx2;
					tmpIdx2 = i;
				} else {
					return false;
				}
			}
		}

		if (skipped < 0) {
			return true;
		}

		end--;
		while (skipped < end && compare(sequence[skipped + 1], sequence[skipped]) < 0) {
			swap(sequence, skipped, skipped + 1);
			skipped++;
		}

		while (skipped > start && compare(sequence[skipped], sequence[skipped - 1]) < 0) {
			swap(sequence, skipped - 1, skipped);
			skipped--;
		}

		return true;
	}

	protected void insertionSort(T[] a, int start, int end) {
		for (int i = start; i < end; i++) {
			for (int k = i; k >= start; k--) {
				if (compare(a[k + 1], a[k]) < 0) {
					swap(a, k, k + 1);
				} else {
					break;
				}
			}
		}
	}

	protected void swap(T[] a, int i, int j) {
		T tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
		tmp = null;
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

		if (a.length < 1000) {
			hybridSort(a, 0, a.length - 1);
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

	protected void hybridSort(T[] a, int start, int end) {
		if (end - start > 7) {
			int i;
			int j;
			final T pivot;
			
			swap(a, medianOf3(a, start, (end - start + 1) / 2, end), start);
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

			hybridSort(a, start, i - 1);
			hybridSort(a, i, end);

			return;
		}

		if (end - start > 0) {
			if (!almostSort(a, start, end)) {
				insertionSort(a, start, end);
			}

			return;
		}
	}

	protected int medianOf3(T[] a, int idx1, int idx2, int idx3) {
		if (compare(a[idx1], a[idx2]) < 0) {
			if (compare(a[idx1], a[idx3]) < 0) {
				if (compare(a[idx2], a[idx3]) < 0) {
					return idx2;
				} else {
					return idx3;
				}
			} else {
				if (compare(a[idx1], a[idx2]) < 0) {
					return idx1;
				} else {
					return idx2;
				}
			}
		} else {
			if (compare(a[idx2], a[idx3]) < 0) {
				if (compare(a[idx1], a[idx3]) < 0) {
					return idx1;
				} else {
					return idx3;
				}
			} else {
				return idx2;
			}
		}
	}

	public HybridSort(Comparator<T> cmp) {
		super(cmp);
	}

	@Override
	public void sort(T[] a) {
		partition(a);
	}
}
