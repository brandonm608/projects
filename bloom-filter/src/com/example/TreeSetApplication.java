package com.example;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.example.tree.RBTreeSet;

public class TreeSetApplication {
	public static void println(OutputStream os, String output) {
		try {
			StringBuffer buffer = new StringBuffer(output);
			buffer.append("\n");
			os.write(buffer.toString().getBytes());
			os.flush();
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) {
		OutputStream out = new BufferedOutputStream(System.out);

		RBTreeSet<Integer> set = new RBTreeSet<>(Integer::compare);
		java.util.TreeSet<Integer> test = new java.util.TreeSet<>();
		int to = 500000;
		int count = 0;
		int[] in = new int[to];
		long time;

		for (int i = 0; i < to; i++) {
			in[i] = (int) (Math.random() * 100 * to);
		}

		time = System.currentTimeMillis();
		for (int i = 0; i < to; i++) {
			set.add(in[i]);
		}
		time = System.currentTimeMillis() - time;
		println(out, "tree built in " + time + " ms\n");

		set.clear();

		set = new RBTreeSet<>(Integer::compare);
		time = System.currentTimeMillis();
		for (int i = 0; i < to; i++) {
			set.add(in[i]);
		}
		time = System.currentTimeMillis() - time;
		println(out, "tree built in " + time + " ms\n");

		time = System.currentTimeMillis();
		for (int i = 0; i < to; i++) {
			test.add(in[i]);
		}
		time = System.currentTimeMillis() - time;
		println(out, "Built-in tree built in " + time + " ms\n");

		test.clear();

		for (int i = 0; i < to; i++) {
			if (!set.contains(in[i])) {
				println(out, in[i] + " not found");
				count++;
			}
		}

		println(out, count + " items not found");
		count = 0;

		for (Integer i : set) {
			if (!test.add(i)) {
				println(out, "Duplicate detected: " + i);
			}
			count++;
		}

		println(out, set.size() + " " + count);

		try {
			out.close();
		} catch (IOException e) {
		}
	}
}
