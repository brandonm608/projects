package com.example.tree;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class TreeSet<T> implements Iterable<T> {
	private static class CompareResult<T> {
		private final BNode<T> node;
		private final int comp;
		private final boolean rightCompare;

		public CompareResult(final BNode<T> node, final int comp, final boolean rightCompare) {
			this.node = node;
			this.comp = comp;
			this.rightCompare = rightCompare;
		}

		public BNode<T> getNode() {
			return node;
		}

		public int getComp() {
			return comp;
		}

		public boolean isRightCompare() {
			return rightCompare;
		}
	}

	private static class TreeSetIterator<T> implements Iterator<T> {
		TreeSet<T> tree;
		private BNode<T> parent;
		private BNode<T> next;
		private boolean right;
		private int expectedModCount;
		private int found;

		public TreeSetIterator(TreeSet<T> tree) {
			boolean moved;
			this.tree = tree;
			parent = null;
			next = tree.root;
			expectedModCount = tree.getModCount();
			right = false;
			// add 1 to the found count so we don't have to do tree.size() - 1
			found = 1;

			do {
				moved = moveLeft() || moveRight();
			} while (moved);
		}

		protected boolean moveLeft() {
			if (next.getType() == BNode.DOUBLE && right) {
				if (next.getPtr2() != null) {
					right = false;
					parent = next;
					next = next.getPtr2();
					return true;
				}

				return false;
			}

			if (next.getPtr1() != null) {
				parent = next;
				next = next.getPtr1();
				return true;
			}

			return false;
		}

		protected boolean moveRight() {
			if (next.getType() == BNode.DOUBLE && !right) {
				right = true;
				parent = next;
				return true;
			}

			if (next.getPtr3() != null) {
				/// next.getType() could be BNode.DOUBLE
				right = false;
				parent = next;
				next = next.getPtr3();
				return true;
			}

			return false;
		}

		protected void moveUp() {
			if (right) {
				// next remains the same
				parent = next.getParent();
				right = false;
				return;
			}

			if (parent != null && parent.getType() == BNode.DOUBLE) {
				right = true;
				next = parent;
				// parent remains the same
			} else {
				next = parent;
				parent = parent.getParent();
			}

			return;
		}

		protected boolean isLeftChild(final BNode<T> parent) {
			if (!right && (next == parent.getPtr1() || next == parent.getPtr2())) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public T next() {
			BNode<T> current = next;

			if (found < tree.size()) {
				if (expectedModCount == tree.getModCount()) {
					boolean currentRight = right;

					if (found < tree.size) {
						if (parent != null) {
							if (isLeftChild(parent)) {
								moveUp();
								if (moveRight()) {
									boolean moved;

									do {
										moved = moveLeft() || moveRight();
									} while (moved);
								}
							} else {
								moveUp();
							}
						}

						found++;
					} else {
						next = null;
					}

					if (currentRight) {
						return current.getRightData();
					}

					return current.getSingleLeftData();
				}

				throw new ConcurrentModificationException("Concurrent modification detected");
			} else {
				if (current == null) {
					throw new IllegalStateException("No more items to iterate through");
				}

				next = null;
				if (right) {
					return current.getRightData();
				}

				return current.getSingleLeftData();
			}
		}

		@Override
		public void remove() {
			if (expectedModCount == tree.getModCount()) {
				// TODO
				return;
			}

			throw new ConcurrentModificationException("Concurrent modification detected");
		}
	}

	private BNode<T> root;
	private final Comparator<T> cmp;
	private int modCount;
	private int size;

	protected void incModCount() {
		if (modCount == Integer.MAX_VALUE) {
			modCount = Integer.MIN_VALUE;
		} else {
			modCount++;
		}
	}

	protected int getModCount() {
		return modCount;
	}

	protected void incSize() {
		size++;
		incModCount();
	}

	protected void decSize() {
		size--;
		incModCount();
	}

	protected void correction(BNode<T> node) {
		while (node != root) {
			final BNode<T> parent = node.getParent();

			if (parent.getType() == BNode.SINGLE) {
				node.combineWithParent();
				return;
			}

			if (node == parent.getPtr1()) {
				parent.rotateRight();
			} else if (node == parent.getPtr3()) {
				parent.rotateLeft();
			} else {
				parent.shuffle();
			}

			node = parent;
		}
	}

	protected CompareResult<T> getNode(final T item) {
		BNode<T> node = null;
		BNode<T> nextNode = root;
		int comp;
		boolean rightComp;

		do {
			// use nextNode to avoid an unnecessary assignment
			comp = cmp.compare(item, nextNode.getSingleLeftData());

			if (comp == 0) {
				return new CompareResult<>(nextNode, 0, false);
			}
			node = nextNode;
			rightComp = false;

			if (comp < 0) {
				nextNode = node.getPtr1();
			} else {
				if (node.getType() == BNode.DOUBLE) {
					comp = cmp.compare(item, node.getRightData());

					if (comp == 0) {
						return new CompareResult<>(node, 0, true);
					}
					rightComp = true;

					if (comp < 0) {
						nextNode = node.getPtr2();
					} else {
						nextNode = node.getPtr3();
					}
				} else {
					nextNode = node.getPtr3();
				}
			}
		} while (nextNode != null);

		return new CompareResult<>(node, comp, rightComp);
	}

	public TreeSet(final Comparator<T> cmp) {
		root = null;
		modCount = Integer.MIN_VALUE;
		size = 0;
		this.cmp = cmp;
	}

	public void add(final T item) {
		if (item != null) {
			if (root != null) {
				final CompareResult<T> result = getNode(item);

				if (result.getComp() == 0) {
					if (result.isRightCompare()) {
						result.getNode().setRightData(item);
					} else {
						result.getNode().setLeftData(item);
					}

					return;
				} else {
					final BNode<T> node = result.getNode();

					if (result.isRightCompare()) {
						// node.getType() must be BNode2.DOUBLE
						if (result.getComp() < 0) {
							node.splitLeaf(item);
							correction(node);
							incSize();
							return;
						}

						node.rotateLeafLeft(item);
						correction(node);
						incSize();
						return;
					}

					// otherwise we have only compared node.getSingleLeftData()
					if (result.getComp() < 0) {
						if (node.getType() == BNode.DOUBLE) {
							node.rotateLeafRight(item);
							correction(node);
							incSize();
							return;
						}

						node.setDoubleLeftData(item);
						incSize();
						return;
					}

					node.setRightData(item);
					incSize();
					return;
				}
			}

			root = new BNode<>(item);
			incSize();
		}
	}

	public boolean contains(final T item) {
		if (root != null) {
			return getNode(item).getComp() == 0;
		}

		return false;
	}

	public Iterator<T> iterator() {
		return new TreeSetIterator<T>(this);
	}

	public int size() {
		return size;
	}
}
