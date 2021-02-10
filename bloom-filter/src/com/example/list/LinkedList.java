package com.example.list;

import java.util.Iterator;

public class LinkedList<T> implements Iterable<T> {
	private static class Node<T> {
		private T value;
		private Node<T> next;

		public Node(T value) {
			this.value = value;
			this.next = null;
		}

		public T get() {
			return value;
		}

		public void clear() {
			value = null;
			next = null;
		}

		public void setNextNode(Node<T> next) {
			this.next = next;
		}

		public Node<T> getNextNode() {
			return next;
		}
	}

	private int size = 0;
	private Node<T> head = null;
	private transient int modCount = Integer.MIN_VALUE;

	protected void decrementSize() {
		size--;
		incrementModCount();
	}

	protected void incrementModCount() {
		if (modCount == Integer.MAX_VALUE) {
			modCount = Integer.MIN_VALUE;
		} else {
			modCount++;
		}
	}

	public void add(T obj) {
		Node<T> node = head;

		if (node == null) {
			head = new Node<>(obj);
			size++;

			return;
		}

		while (node.getNextNode() != null) {
			if (node.get().equals(obj)) {
				return;
			}

			node = node.getNextNode();
		}

		if (node.get().equals(obj)) {
			return;
		}

		node.setNextNode(new Node<>(obj));
		size++;
	}

	public void clear() {
		Node<T> node = head;

		if (head == null) {
			return;
		}

		while (node.getNextNode() != null) {
			Node<T> next = node.getNextNode();
			node.clear();
			node = next;
		}

		node.clear();
	}

	public boolean contains(T obj) {
		Node<T> node = head;

		if (node == null) {
			return false;
		}

		while (node.getNextNode() != null) {
			if (node.get().equals(obj)) {
				return true;
			}

			node = node.getNextNode();
		}

		if (node.get().equals(obj)) {
			return true;
		}

		return false;
	}

	public T get(T obj) {
		Node<T> node = head;

		if (node == null) {
			return null;
		}

		while (node.getNextNode() != null) {
			if (node.get().equals(obj)) {
				return node.get();
			}

			node = node.getNextNode();
		}

		if (node.get().equals(obj)) {
			return node.get();
		}

		return null;
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Node<T> parentNode;
			private Node<T> node;
			private Node<T> nextNode = head;

			@Override
			public boolean hasNext() {
				return nextNode != null;
			}

			@Override
			public T next() {
				if (hasNext()) {
					if (parentNode != node) {
						if (node != null) {
							parentNode = node;
						}
						node = nextNode;
						nextNode = node.getNextNode();
						return node.get();
					} else if (node == head) {
						node = nextNode;
						nextNode = node.getNextNode();
						return node.get();
					}

					parentNode = node = nextNode;
					nextNode = node.getNextNode();
					return node.get();
				}

				throw new IllegalStateException();
			}

			@Override
			public void remove() {
				if (node != null) {
					if (node != head) {
						parentNode.setNextNode(nextNode);
						node.clear();
						node = null;
						decrementSize();
						return;
					}

					head = parentNode = nextNode;
					node.clear();
					node = null;
					decrementSize();
					return;
				}

				throw new IllegalStateException("There is not a current element");
			}
		};
	}

	public T remove() {
		Node<T> node = head;
		T obj;

		if (node == null) {
			return null;
		}

		head = node.getNextNode();
		obj = node.get();
		node.clear();
		size--;
		return obj;
	}

	public T remove(T obj) {
		Node<T> node = head;

		if (node == null) {
			return null;
		}

		if (node.get().equals(obj)) {
			T ret = node.get();
			head = node.getNextNode();
			node.clear();
			size--;
			return ret;
		}

		while (node.getNextNode() != null) {
			if (node.getNextNode().get().equals(obj)) {
				T ret = node.get();
				Node<T> tmp = node.getNextNode().getNextNode();
				node.getNextNode().clear();
				node.setNextNode(tmp);
				size--;
				return ret;
			}

			node = node.getNextNode();
		}

		return null;

	}

	public int size() {
		return size;
	}
}
