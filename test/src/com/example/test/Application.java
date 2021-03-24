package com.example.test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

import com.example.test.sort.HeapSort;
import com.example.test.sort.HybridSort;
import com.example.test.sort.HybridSort2;
import com.example.test.sort.MergeSort;
import com.example.test.sort.QuickSort;
import com.example.test.sort.Sort;

public class Application {
	private static final String ARRAYS_SORT = "1";
	private static final String MERGE_SORT = "2";
	private static final String HEAP_SORT = "3";
	private static final String HYBRID_SORT = "4";
	private static final String QUICK_SORT = "5";
	private static final String HYBRID_SORT2 = "6";

	private static Integer[] createFilledArray(int length) {
		Integer[] a = new Integer[length];

		for (int i = 0; i < length; i++) {
			a[i] = Integer.valueOf((int) (Math.random() * Integer.MAX_VALUE));
		}

		return a;
	}

	private static boolean isArraysEqual(Integer[] a, Integer[] b) {
		return Arrays.compare(a, b) == 0;
	}

	private static boolean isEqual(String str1, String str2) {
		return str1.equals(str2);
	}

	private static Sort<Integer> getAlgorithm(String choice) {
		if (isEqual(MERGE_SORT, choice)) {
			return new MergeSort<>(Integer::compareTo);
		} else if (isEqual(HEAP_SORT, choice)) {
			return new HeapSort<>(Integer::compareTo);
		} else if (isEqual(HYBRID_SORT, choice)) {
			return new HybridSort<>(Integer::compareTo);
		} else if (isEqual(QUICK_SORT, choice)) {
			return new QuickSort<>(Integer::compareTo);
		} else if (isEqual(HYBRID_SORT2, choice)) {
			return new HybridSort2<>(Integer::compareTo);
		}

		return null;
	}

	private static void loop() {
		try (InputStreamReader in = new InputStreamReader(System.in);
				BufferedReader reader = new BufferedReader(in);
				BufferedOutputStream out = new BufferedOutputStream(System.out)) {
			String choice = readChoice(out, reader);
			while (!choice.equals("q")) {
				Sort<Integer> algorithm = getAlgorithm(choice);
				Integer[] filledArray = createFilledArray(10000);
				Integer[] copy1 = Arrays.copyOf(filledArray, filledArray.length);
				Integer[] copy2 = Arrays.copyOf(filledArray, filledArray.length);
				Integer[] copy3 = Arrays.copyOf(filledArray, filledArray.length);
				long deltaTime1, deltaTime2, deltaTime3;
				boolean isArrayEqual;

				sort(out, algorithm, filledArray, ARRAYS_SORT);
				deltaTime1 = sort(out, algorithm, copy1, choice);
				deltaTime2 = sort(out, algorithm, copy2, choice);
				deltaTime3 = sort(out, algorithm, copy3, choice);

				println(out, "The median time of 3 runs is " + medianOf3(deltaTime1, deltaTime2, deltaTime3) + "ms");
				println(out, "Time to sort sorted array: ");
				sort(out, algorithm, copy3, ARRAYS_SORT);
				sort(out, algorithm, copy3, choice);

				isArrayEqual = isArraysEqual(filledArray, copy1);
				println(out, "Matches Arrays Sort: " + isArrayEqual);
				println(out);

				if (!isArrayEqual && deltaTime1 > 0) {
					System.exit(1);
				}

				choice = readChoice(out, reader);

				Arrays.setAll(filledArray, i -> null);
				Arrays.setAll(copy1, i -> null);
				Arrays.setAll(copy2, i -> null);
				Arrays.setAll(copy3, i -> null);

				filledArray = null;
				copy1 = null;
				copy2 = null;
				copy3 = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static long medianOf3(long a1, long a2, long a3) {
		if (a1 < a2) {
			if (a1 < a3) {
				if (a2 < a3) {
					return a2;
				} else {
					return a3;
				}
			} else {
				if (a1 < a2) {
					return a1;
				} else {
					return a2;
				}
			}
		} else {
			if (a2 < a3) {
				if (a1 < a3) {
					return a1;
				} else {
					return a3;
				}
			} else {
				return a2;
			}
		}
	}

	private static long sort(OutputStream out, Sort<Integer> algorithm, Integer[] a, String choice) throws IOException {
		long deltaTime;

		if (algorithm == null) {
			if (isEqual(ARRAYS_SORT, choice)) {
				deltaTime = System.currentTimeMillis();
				Arrays.sort(a);
				deltaTime = System.currentTimeMillis() - deltaTime;
			} else {
				println(out, "Invalid choice\n");
				return -1;
			}
		} else {
			deltaTime = System.currentTimeMillis();
			algorithm.sort(a);
			deltaTime = System.currentTimeMillis() - deltaTime;
		}

		if (isEqual(ARRAYS_SORT, choice)) {
			print(out, "Array Sort");
		} else if (isEqual(MERGE_SORT, choice)) {
			print(out, "Merge Sort");
		} else if (isEqual(HEAP_SORT, choice)) {
			print(out, "Heap Sort");
		} else if (isEqual(HYBRID_SORT, choice)) {
			print(out, "Hybrid Sort");
		} else if (isEqual(QUICK_SORT, choice)) {
			print(out, "Quick Sort");
		} else if (isEqual(HYBRID_SORT2, choice)) {
			print(out, "Hybrid Sort 2");
		}

		println(out, " took " + deltaTime + "ms to complete");

		return deltaTime;
	}

	private static void print(OutputStream os, String output) throws IOException {
		os.write(output.getBytes());
		os.flush();
	}

	private static void println(OutputStream os) throws IOException {
		os.write("\n".getBytes());
		os.flush();
	}

	private static void println(OutputStream os, String output) throws IOException {
		StringBuffer buffer = new StringBuffer(output);

		buffer.append('\n');
		os.write(buffer.toString().getBytes());
		os.flush();
	}

	private static void printMenu(OutputStream out) throws IOException {
		println(out, ARRAYS_SORT + ": Arrays Sort");
		println(out, MERGE_SORT + ": Merge Sort");
		println(out, HEAP_SORT + ": Heap Sort");
		println(out, HYBRID_SORT + ": Hybrid Sort");
		println(out, QUICK_SORT + ": Quick Sort");
		println(out, HYBRID_SORT2 + ": Hybrid Sort 2");
		print(out, "Enter Choice: ");
	}

	private static String readChoice(OutputStream out, BufferedReader reader) throws IOException {
		String line;

		printMenu(out);
		line = reader.readLine();

		return line;
	}

	public static void main(String[] args) {
		loop();
	}
}
