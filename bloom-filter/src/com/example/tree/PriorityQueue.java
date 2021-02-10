package com.example.tree;

import java.util.Comparator;

public class PriorityQueue<T> {
	private static class Node<T> {
		private T data;

		private Node<T> parent;

		private Node<T> left;
		private Node<T> right;

		public Node(T data) {
			this.data = data;

			parent = null;

			left = null;
			right = null;
		}

		public void clear() {
			parent = null;
			left = null;
			right = null;
			data = null;
		}

		public Node<T> swap(Node<T> node) {
			T tmp = data;
			data = node.getData();
			node.setData(tmp);
			return node;
		}

		public Node<T> getParent() {
			return parent;
		}

		public void setParent(Node<T> parent) {
			this.parent = parent;
		}

		public Node<T> getLeft() {
			return left;
		}

		public void setLeft(Node<T> left) {
			if (left != null) {
				left.setParent(this);
			}
			this.left = left;
		}

		public Node<T> getRight() {
			return right;
		}

		public void setRight(Node<T> right) {
			if (right != null) {
				right.setParent(this);
			}
			this.right = right;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}
	}

	private Node<T> head;
	private Node<T> tail;
	private int size;
	private Comparator<T> cmp;

	protected void incSize() {
		size++;
	}

	protected void decSize() {
		size--;
	}

	protected void bubbleUp() {
		Node<T> current = tail;
		Node<T> parent = current.getParent();

		do {
			if (cmp.compare(current.getData(), parent.getData()) < 0) {
				current.swap(parent);

				current = parent;
				parent = current.getParent();
			} else {
				return;
			}
		} while (parent != null);
	}

	protected void siftRootDown() {
		Node<T> current = head;

		for (;;) {
			if (current.getLeft() != null) {
				T left = current.getLeft().getData();

				if (current.getRight() != null) {
					T right = current.getRight().getData();

					if (cmp.compare(left, right) < 0) {
						if (cmp.compare(left, current.getData()) < 0) {
							current = current.swap(current.getLeft());
						} else {
							return;
						}
					} else {
						if (cmp.compare(right, current.getData()) < 0) {
							current = current.swap(current.getRight());
						} else {
							return;
						}
					}
				} else {
					if (cmp.compare(left, current.getData()) < 0) {
						current = current.swap(current.getLeft());
					} else {
						return;
					}
				}
			} else if (current.getRight() != null) {
				if (cmp.compare(current.getRight().getData(), current.getData()) < 0) {
					current = current.swap(current.getRight());
				} else {
					return;
				}
			} else {
				return;
			}
		}
	}

	public PriorityQueue(Comparator<T> cmp) {
		head = null;
		tail = null;
		size = 0;
		this.cmp = cmp;
	}

	public void add(T element) {
		if (element != null) {
			if (head != null) {
				Node<T> tmp = tail.getParent();
				Node<T> toAdd = new Node<>(element);

				if (cmp.compare(toAdd.getData(), head.getData()) < 0) {
					head.swap(toAdd);
				}

				if (tmp == null) {
					tail.setLeft(toAdd);
					tail = toAdd;
					bubbleUp();
					incSize();
					return;
				}

				if (tmp.getLeft() == tail) {
					tmp.setRight(toAdd);
					tail = toAdd;
					bubbleUp();
					incSize();
					return;
				} else {
					do {
						tail = tmp;
						tmp = tmp.getParent();
					} while (tmp != null && tmp.getRight() == tail);

					if (tmp != null) {
						tail = tmp.getRight();
					}

					tmp = tail.getLeft();
					while (tmp != null) {
						tail = tmp;
						tmp = tmp.getLeft();
					}

					tail.setLeft(toAdd);
					tail = toAdd;
					bubbleUp();
					incSize();
					return;
				}
			} else {
				head = new Node<>(element);
				tail = head;
				incSize();
				return;
			}
		}
	}

	public void clear() {
		while (size > 0) {
			remove();
		}
	}

	public T peek() {
		return head.getData();
	}

	public T remove() {
		if (size > 0) {
			T value = head.getData();

			if (head == tail) {
				tail.clear();
				head = null;
				tail = null;
			} else {
				Node<T> tmp = tail.getParent();
				Node<T> oldTail = tail;
				head.setData(tail.getData());

				if (tmp.getLeft() == tail) {
					tmp.setLeft(null);

					do {
						tail = tmp;
						tmp = tmp.getParent();
					} while (tmp != null && tmp.getLeft() == tail);

					if (tmp != null) {
						tail = tmp.getLeft();
					}

					tmp = tail.getRight();
					while (tmp != null) {
						tail = tmp;
						tmp = tmp.getRight();
					}
				} else {
					tmp.setRight(null);
					tail = tmp.getLeft();
				}

				oldTail.clear();
				oldTail = null;

				siftRootDown();
			}

			decSize();
			return value;
		}

		return null;

	}

	public int size() {
		return size;
	}
}
