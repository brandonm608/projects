package com.example.tree;

import java.util.Comparator;

public class TreeSet4<T> {
	private static class CompareResult<T> {
		public static final int DATA2 = 2;
		public static final int DATA = 0;
		public static final int DATA1 = 1;

		private final BNode4<T> node;
		private final int comp;
		private final int dataFieldComp;

		public CompareResult(final BNode4<T> node, final int comp, final int dataFieldComp) {
			this.node = node;
			this.comp = comp;
			this.dataFieldComp = dataFieldComp;
		}

		public BNode4<T> getNode() {
			return node;
		}

		public int getComp() {
			return comp;
		}

		public int getDataFieldComp() {
			return dataFieldComp;
		}
	}

	private BNode4<T> root;
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

	protected void correction(BNode4<T> node) {
		BNode4<T> parent = node.getParent();

		if (parent != null) {
			if (parent.getType() != BNode4.DOUBLE) {
				node.combineWithParent();
				return;
			}

			for (;;) {
				if (node.isPtr2Child()) {
					parent.shuffleDouble();
				} else if (node.isPtr1Child()) {
					parent.rotateDoubleRight();
				} else {
					parent.rotateDoubleLeft();
				}

				node = parent;
				parent = node.getParent();

				if (parent == null) {
					return;
				}

				if (parent.getType() != BNode4.DOUBLE) {
					node.combineWithParent();
					return;
				}
			}
		}
	}

	protected CompareResult<T> getNode(T item) {
		BNode4<T> node = null;
		BNode4<T> nextNode = root;
		int comp;
		int dataFieldComp;

		do {
			comp = cmp.compare(item, nextNode.getData());

			if (comp == 0) {
				return new CompareResult<>(nextNode, 0, CompareResult.DATA);
			}

			node = nextNode;
			switch (node.getType()) {
			case BNode4.SINGLE:
				dataFieldComp = CompareResult.DATA;
				if (comp < 0) {
					nextNode = node.getPtr1();
				} else {
					nextNode = node.getPtr4();
				}
				break;
			case BNode4.DOUBLE:
				if (comp < 0) {
					dataFieldComp = CompareResult.DATA;
					nextNode = node.getPtr1();
				} else {
					comp = cmp.compare(item, node.getData1());

					if (comp == 0) {
						return new CompareResult<>(node, 0, CompareResult.DATA1);
					}

					dataFieldComp = CompareResult.DATA1;
					if (comp < 0) {
						nextNode = node.getPtr2();
					} else {
						nextNode = node.getPtr4();
					}
				}
				break;
			default:
				if (comp < 0) {
					comp = cmp.compare(item, node.getData2());

					if (comp == 0) {
						return new CompareResult<>(node, 0, CompareResult.DATA2);
					}

					dataFieldComp = CompareResult.DATA2;
					if (comp < 0) {
						nextNode = node.getPtr1();
					} else {
						nextNode = node.getPtr2();
					}
				} else {
					comp = cmp.compare(item, node.getData1());

					if (comp == 0) {
						return new CompareResult<>(node, 0, CompareResult.DATA1);
					}

					dataFieldComp = CompareResult.DATA1;
					if (comp < 0) {
						nextNode = node.getPtr3();
					} else {
						nextNode = node.getPtr4();
					}
				}
				break;
			}
		} while (nextNode != null);

		return new CompareResult<>(node, comp, dataFieldComp);
	}

	public TreeSet4(Comparator<T> cmp) {
		root = null;
		modCount = Integer.MIN_VALUE;
		size = 0;
		this.cmp = cmp;
	}

	public void add(T item) {
		if (item != null) {
			if (root != null) {
				final CompareResult<T> result = getNode(item);

				if (result.getComp() == 0) {
					switch (result.getDataFieldComp()) {
					case CompareResult.DATA:
						result.getNode().setData(item);
						return;
					case CompareResult.DATA1:
						result.getNode().setData1(item);
						return;
					default:
						result.getNode().setData2(item);
						return;
					}
				}

				BNode4<T> node = result.getNode();
				switch (node.getType()) {
				case BNode4.SINGLE:
					if (result.getComp() < 0) {
						node.setDoubleLeft(item);
					} else {
						node.setDoubleRight(item);
					}

					incSize();
					return;
				case BNode4.DOUBLE:
					if (result.getDataFieldComp() == CompareResult.DATA) {
						node.setTripleLeft(item);
					} else {
						if (result.getComp() < 0) {
							node.setTripleCenter(item);
						} else {
							node.setTripleRight(item);
						}
					}

					correction(node);
					incSize();
					return;
				default:
					if (result.getDataFieldComp() == CompareResult.DATA2) {
						node = node.splitTripleLeafAndGetPtr1();
						if (result.getComp() < 0) {
							node.setDoubleLeft(item);
						} else {
							node.setDoubleRight(item);
						}
					} else {
						node = node.splitTripleLeafAndGetPtr4();
						if (result.getComp() < 0) {
							node.setDoubleLeft(item);
						} else {
							node.setDoubleRight(item);
						}
					}

					incSize();
					return;
				}
			}

			root = new BNode4<>(item);
			incSize();
		}
	}

	public boolean contains(T item) {
		if (root != null) {
			return getNode(item).getComp() == 0;
		}

		return false;
	}

	// TODO
//	public Iterator<T> iterator() {
//		return new TreeSetIterator<T>(this);
//	}

	public int size() {
		return size;
	}
}
