package com.example.tree;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class RBTreeSet<T> implements Iterable<T> {
	private static class RBNode<T> {
		private T data;

		private RBNode<T> parent;

		private RBNode<T> left;
		private RBNode<T> right;

		private boolean black;

		public RBNode(T data) {
			this.data = data;
			black = true;

			parent = null;
			left = null;
			right = null;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

		public RBNode<T> getParent() {
			return parent;
		}

		public void setParent(RBNode<T> parent) {
			this.parent = parent;
		}

		public RBNode<T> getLeft() {
			return left;
		}

		public void setLeft(RBNode<T> left) {
			left.setParent(this);
			this.left = left;
		}

		public RBNode<T> getRight() {
			return right;
		}

		public void setRight(RBNode<T> right) {
			right.setParent(this);
			this.right = right;
		}

		public boolean isBlack() {
			return black;
		}

		public void setBlack(boolean black) {
			this.black = black;
		}
	}

	private static class RBTreeSetIterator<T> implements Iterator<T> {
		private final RBTreeSet<T> tree;
		private int expectedModCount;
		private RBNode<T> nextNode;

		public RBTreeSetIterator(final RBTreeSet<T> tree) {
			this.tree = tree;
			this.expectedModCount = tree.getModCount();
			if (tree.root != null) {
				this.nextNode = tree.getDeepestNodeBiasedRight(tree.root);
			} else {
				this.nextNode = null;
			}
		}

		@Override
		public boolean hasNext() {
			return nextNode != null;
		}

		@Override
		public T next() {
			if (expectedModCount == tree.getModCount()) {
				if (nextNode != null) {
					final RBNode<T> node = nextNode;

					if (nextNode != tree.root) {
						nextNode = tree.getNextNode(node);
					} else {
						nextNode = null;
					}

					return node.getData();
				}

				throw new IllegalStateException("No more items to iterate through");
			} else {
				throw new ConcurrentModificationException("An unexpected modification has occurred");
			}
		}

		@Override
		public void remove() {
			// TODO
		}
	}

	private RBNode<T> root;
	private Comparator<T> cmp;
	private int size;
	private int modCount;

	protected void incModCount() {
		if (modCount != Integer.MAX_VALUE) {
			modCount++;
		} else {
			modCount = Integer.MIN_VALUE;
		}
	}

	protected int getModCount() {
		return modCount;
	}

	protected void rebalanceLeafLeft(final RBNode<T> node, final RBNode<T> parent, final T item) {
		node.setBlack(true);

		if (parent.getLeft() == node) {
			rotateLeafRight(node, parent, item);
		} else {
			shuffleLeafLeft(parent, item);
		}
		color(parent);
	}

	protected void addLeft(final RBNode<T> node, final RBNode<T> parent, final T item) {
		if (node.isBlack()) {
			final RBNode<T> sibling = node.getRight();
			final RBNode<T> left = new RBNode<>(item);
			node.setLeft(left);

			colorIfSibling(node, left, sibling);
		} else {
			rebalanceLeafLeft(node, parent, item);
		}
	}

	protected void rebalanceLeafRight(final RBNode<T> node, final RBNode<T> parent, final T item) {
		node.setBlack(true);

		if (parent.getLeft() == node) {
			shuffleLeafRight(parent, item);
		} else {
			rotateLeafLeft(node, parent, item);
		}
		color(parent);
	}

	protected void addRight(final RBNode<T> node, final RBNode<T> parent, final T item) {
		if (node.isBlack()) {
			final RBNode<T> sibling = node.getLeft();
			final RBNode<T> right = new RBNode<>(item);
			node.setRight(right);

			colorIfSibling(node, right, sibling);
		} else {
			rebalanceLeafRight(node, parent, item);
		}
	}

	protected void colorIfSibling(final RBNode<T> node, final RBNode<T> siblingToColor, final RBNode<T> sibling) {
		if (sibling == null) {
			siblingToColor.setBlack(false);
		} else {
			sibling.setBlack(true);
			color(node);
		}
	}

	protected void color(RBNode<T> node) {
		while (node != root) {
			final RBNode<T> parent = node.getParent();

			if (parent.isBlack()) {
				final RBNode<T> sibling = getSibling(node, parent);

				if (sibling.isBlack()) {
					node.setBlack(false);
					return;
				} else {
					sibling.setBlack(true);
					node = parent;
				}
			} else {
				node = rebalance(node, parent);
			}
		}
	}

	protected RBNode<T> getSibling(final RBNode<T> node, final RBNode<T> parent) {
		if (parent.getLeft() == node) {
			return parent.getRight();
		} else {
			return parent.getLeft();
		}
	}

	protected void setNewParent(final RBNode<T> parent, final RBNode<T> newParent) {
		if (parent != root) {
			final RBNode<T> grandparent = parent.getParent();
			if (grandparent.getLeft() == parent) {
				grandparent.setLeft(newParent);
			} else {
				grandparent.setRight(newParent);
			}
		} else {
			newParent.setParent(null);
			root = newParent;
		}
	}

	protected RBNode<T> rebalanceLeftChild(final RBNode<T> node, final RBNode<T> parent, final RBNode<T> grandparent) {
		if (grandparent.getLeft() == parent) {
			rotateRight(parent, grandparent);
			return parent;
		} else {
			shuffleLeft(node, parent, grandparent);
			return node;
		}
	}

	protected RBNode<T> rebalanceRightChild(final RBNode<T> node, final RBNode<T> parent, final RBNode<T> grandparent) {
		if (grandparent.getLeft() == parent) {
			shuffleRight(node, parent, grandparent);
			return node;
		} else {
			rotateLeft(parent, grandparent);
			return parent;
		}
	}

	protected RBNode<T> rebalance(final RBNode<T> node, final RBNode<T> parent) {
		final RBNode<T> grandparent = parent.getParent();
		parent.setBlack(true);

		if (grandparent.getLeft() == parent) {
			return rebalanceLeftChild(node, parent, grandparent);
		} else {
			return rebalanceRightChild(node, parent, grandparent);
		}
	}

	protected void rotateLeft(final RBNode<T> node, final RBNode<T> parent) {
		setNewParent(parent, node);

		parent.setRight(node.getLeft());
		node.setLeft(parent);
	}

	protected void rotateLeafLeft(final RBNode<T> right, final RBNode<T> parent, final T item) {
		parent.setLeft(new RBNode<>(parent.getData()));
		parent.setData(right.getData());
		right.setData(item);
	}

	protected void shuffleLeft(final RBNode<T> left, final RBNode<T> node, final RBNode<T> parent) {
		setNewParent(parent, left);

		node.setLeft(left.getRight());
		parent.setRight(left.getLeft());
		left.setLeft(parent);
		left.setRight(node);
	}

	protected void shuffleLeafLeft(final RBNode<T> parent, final T item) {
		parent.setLeft(new RBNode<>(parent.getData()));
		parent.setData(item);
	}

	protected void rotateRight(final RBNode<T> node, final RBNode<T> parent) {
		setNewParent(parent, node);

		parent.setLeft(node.getRight());
		node.setRight(parent);
	}

	protected void rotateLeafRight(final RBNode<T> left, final RBNode<T> parent, final T item) {
		parent.setRight(new RBNode<>(parent.getData()));
		parent.setData(left.getData());
		left.setData(item);
	}

	protected void shuffleRight(final RBNode<T> right, final RBNode<T> node, final RBNode<T> parent) {
		setNewParent(parent, right);

		node.setRight(right.getLeft());
		parent.setLeft(right.getRight());
		right.setRight(parent);
		right.setLeft(node);
	}

	protected void shuffleLeafRight(final RBNode<T> parent, final T item) {
		parent.setRight(new RBNode<>(parent.getData()));
		parent.setData(item);
	}

	protected RBNode<T> getNode(final T item) {
		RBNode<T> node = root;

		do {
			final int comp = cmp.compare(item, node.getData());

			if (comp < 0) {
				node = node.getLeft();
			} else if (comp > 0) {
				node = node.getRight();
			} else {
				return node;
			}
		} while (node != null);

		return null;
	}

	protected RBNode<T> getDeepestNodeBiasedRight(RBNode<T> localRoot) {
		for (;;) {
			RBNode<T> nextNode = localRoot.getRight();

			if (nextNode != null) {
				localRoot = nextNode;
			} else {
				nextNode = localRoot.getLeft();
				if (nextNode != null) {
					localRoot = nextNode;
				} else {
					return localRoot;
				}
			}
		}
	}

	protected RBNode<T> getNextNode(RBNode<T> node) {
		final RBNode<T> parent = node.getParent();

		if (parent.getRight() == node) {
			node = parent.getLeft();

			if (node == null) {
				return parent;
			}

			return getDeepestNodeBiasedRight(node);
		}

		// else parent.getLeft() == node
		return parent;
	}

	public RBTreeSet(Comparator<T> cmp) {
		this.cmp = cmp;
		root = null;
		size = 0;
		modCount = Integer.MIN_VALUE;
	}

	public void add(final T item) {
		if (item != null) {
			if (root != null) {
				RBNode<T> node = root;
				RBNode<T> parent = null;

				for (;;) {
					final int comp = cmp.compare(item, node.getData());

					if (comp < 0) {
						final RBNode<T> left = node.getLeft();

						if (left == null) {
							addLeft(node, parent, item);
							break;
						}

						parent = node;
						node = left;
					} else if (comp > 0) {
						final RBNode<T> right = node.getRight();

						if (right == null) {
							addRight(node, parent, item);
							break;
						}

						parent = node;
						node = right;
					} else {
						node.setData(item);
						incModCount();
						return;
					}
				}
			} else {
				root = new RBNode<>(item);
			}
			size++;
			incModCount();
		}
	}

	public void clear() {
		if (root != null) {
			root = null;
			incModCount();
			size = 0;
		}
	}

	public boolean contains(final T item) {
		if (item != null && root != null) {
			return getNode(item) != null;
		}

		return false;
	}

	public Iterator<T> iterator() {
		return new RBTreeSetIterator<>(this);
	}

	public int size() {
		return size;
	}
}
