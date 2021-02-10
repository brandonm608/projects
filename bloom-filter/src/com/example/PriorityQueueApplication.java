package com.example;

import com.example.tree.PriorityQueue;

public class PriorityQueueApplication {
	public static void main(String[] args) {
		PriorityQueue<Integer> queue = new PriorityQueue<>(Integer::compare);
		Integer last;

		for (int i = 0; i < 1000; i++) {
			queue.add((int) (Math.random() * 10000));
		}

		last = queue.remove();
		while (queue.size() > 0) {
			Integer tmp = queue.remove();

			if (!(Integer.compare(last, tmp) <= 0)) {
				System.out.println("Error: " + last + " " + tmp);
			}
			last = tmp;
		}

		System.out.println("Done");
	}
}
