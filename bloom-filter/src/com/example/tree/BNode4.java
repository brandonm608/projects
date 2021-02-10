package com.example.tree;

class BNode4<T> {
	public static final byte SINGLE = 0;
	public static final byte DOUBLE = 1;
	public static final byte TRIPLE = 2;

	private BNode4<T> ptr1;
	private BNode4<T> ptr2;
	private BNode4<T> ptr3;
	private BNode4<T> ptr4;

	private BNode4<T> parent;

	private T data;
	private T data1;
	private T data2;

	private byte type;

	protected void combine(BNode4<T> ptr, BNode4<T> left, BNode4<T> right) {
		if (left != null) {
			ptr.setPtr1(left);
		}
		if (right != null) {
			ptr.setPtr4(right);
		}
	}

	protected BNode4<T> combineWithPtr1() {
		BNode4<T> ptr = ptr1;

		if (getType() == SINGLE) {
			setDoubleLeft(ptr.getData());

			if (ptr.getType() == SINGLE) {
				setPtr1(ptr.getPtr1());
				setPtr2(ptr.getPtr4());
			} else {
				BNode4<T> left = new BNode4<>(ptr.getData2());
				BNode4<T> right = new BNode4<>(ptr.getData1());

				combine(left, ptr.getPtr1(), ptr.getPtr2());
				combine(right, ptr.getPtr3(), ptr.getPtr4());

				setPtr1(left);
				setPtr2(right);
			}

			ptr.clear();
			return this;
		}

		BNode4<T> right = new BNode4<>(data1);
		combine(right, ptr3, ptr4);
		setPtr4(right);
		ptr3 = null;

		if (ptr.getType() == SINGLE) {
			ptr.setPtr2(ptr.getPtr4());
		} else {
			BNode4<T> leftOfPtr = new BNode4<>(ptr.getData2());
			BNode4<T> rightOfPtr = new BNode4<>(ptr.getData1());

			combine(leftOfPtr, ptr.getPtr1(), ptr.getPtr2());
			combine(rightOfPtr, ptr.getPtr2(), ptr.getPtr4());

			ptr.setPtr1(leftOfPtr);
			ptr.setPtr2(rightOfPtr);

			ptr.setPtr3(null);
			ptr.setData2(null);
		}

		ptr.setDoubleRight(data2);
		ptr.setPtr4(ptr2);
		ptr2 = null;

		type = SINGLE;
		return ptr;
	}

	protected BNode4<T> combineWithPtr2() {
		BNode4<T> ptr = ptr2;
		BNode4<T> right = new BNode4<>(data1);
		combine(right, ptr3, ptr4);
		setPtr4(right);
		ptr3 = null;

		if (ptr.getType() == SINGLE) {
			ptr.setPtr2(ptr.getPtr1());
		} else {
			BNode4<T> leftOfPtr = new BNode4<>(ptr.getData2());
			BNode4<T> rightOfPtr = new BNode4<>(ptr.getData1());

			combine(leftOfPtr, ptr.getPtr1(), ptr.getPtr2());
			combine(rightOfPtr, ptr.getPtr2(), ptr.getPtr4());

			ptr.setPtr2(leftOfPtr);
			ptr.setPtr4(rightOfPtr);

			ptr.setPtr3(null);
			ptr.setData2(null);
		}

		ptr.setDoubleLeft(data2);
		ptr.setPtr1(ptr1);
		setPtr1(ptr);
		ptr2 = null;

		type = SINGLE;
		return ptr;
	}

	protected BNode4<T> combineWithPtr3() {
		BNode4<T> ptr = ptr3;
		BNode4<T> left = new BNode4<>(data2);
		combine(left, ptr1, ptr2);
		setPtr1(left);
		ptr2 = null;

		if (ptr.getType() == SINGLE) {
			ptr.setPtr2(ptr.getPtr4());
		} else {
			BNode4<T> leftOfPtr = new BNode4<>(ptr.getData2());
			BNode4<T> rightOfPtr = new BNode4<>(ptr.getData1());

			combine(leftOfPtr, ptr.getPtr1(), ptr.getPtr2());
			combine(rightOfPtr, ptr.getPtr2(), ptr.getPtr4());

			ptr.setPtr1(leftOfPtr);
			ptr.setPtr2(rightOfPtr);

			ptr.setPtr3(null);
			ptr.setData2(null);
		}

		ptr.setDoubleRight(data1);
		ptr.setPtr4(ptr4);
		setPtr4(ptr);
		ptr3 = null;

		type = SINGLE;
		return ptr;
	}

	protected BNode4<T> combineWithPtr4() {
		BNode4<T> ptr = ptr4;

		if (getType() == SINGLE) {
			setDoubleRight(ptr.getData());

			if (ptr.getType() == SINGLE) {
				setPtr2(ptr.getPtr1());
				setPtr4(ptr.getPtr4());
			} else {
				BNode4<T> left = new BNode4<>(ptr.getData2());
				BNode4<T> right = new BNode4<>(ptr.getData1());

				combine(left, ptr.getPtr1(), ptr.getPtr2());
				combine(right, ptr.getPtr3(), ptr.getPtr4());

				setPtr2(left);
				setPtr4(right);
			}

			ptr.clear();
			return this;
		}

		BNode4<T> left = new BNode4<>(data2);
		combine(left, ptr1, ptr2);
		setPtr1(left);
		ptr2 = null;

		if (ptr.getType() == SINGLE) {
			ptr.setPtr2(ptr.getPtr1());
		} else {
			BNode4<T> leftOfPtr = new BNode4<>(ptr.getData2());
			BNode4<T> rightOfPtr = new BNode4<>(ptr.getData1());

			combine(leftOfPtr, ptr.getPtr1(), ptr.getPtr2());
			combine(rightOfPtr, ptr.getPtr2(), ptr.getPtr4());

			ptr.setPtr2(leftOfPtr);
			ptr.setPtr4(rightOfPtr);

			ptr.setPtr3(null);
			ptr.setData2(null);
		}

		ptr.setDoubleLeft(data1);
		ptr.setPtr1(ptr3);
		ptr3 = null;

		type = SINGLE;
		return ptr;
	}

	protected void splitTriple() {
		BNode4<T> left = new BNode4<>(data2);
		BNode4<T> right = new BNode4<>(data1);

		combine(left, ptr1, ptr2);
		combine(right, ptr3, ptr4);

		setPtr1(left);
		setPtr4(right);

		data2 = null;
		data1 = null;

		type = SINGLE;
	}

	protected void splitTripleLeaf() {
		setPtr1(new BNode4<>(data2));
		setPtr4(new BNode4<>(data1));

		type = SINGLE;
	}

	public BNode4(T item) {
		ptr1 = null;
		ptr2 = null;
		ptr3 = null;
		ptr4 = null;

		parent = null;

		data = item;

		data1 = null;
		data2 = null;

		type = 0;
	}

	public void clear() {
		ptr1 = null;
		ptr2 = null;
		ptr3 = null;
		ptr4 = null;

		parent = null;

		data = null;
		data1 = null;
		data2 = null;
	}

	public BNode4<T> combineWithParent() {
		if (isPtr1Child()) {
			return parent.combineWithPtr1();
		}

		if (isPtr4Child()) {
			return parent.combineWithPtr4();
		}

		if (isPtr2Child()) {
			return parent.combineWithPtr2();
		}

		return parent.combineWithPtr3();
	}

	public void rotateDoubleLeft() {
		BNode4<T> ptr = new BNode4<>(data);
		combine(ptr, ptr1, ptr2);

		setPtr1(ptr);

		if (ptr4.getType() == TRIPLE) {
			ptr4.splitTriple();
		}

		data = data1;
		data1 = null;
		ptr2 = null;
		type = SINGLE;
	}

	public void rotateDoubleRight() {
		BNode4<T> ptr = new BNode4<>(data1);

		combine(ptr, ptr2, ptr4);
		setPtr4(ptr);

		if (ptr1.getType() == TRIPLE) {
			ptr1.splitTriple();
		}

		data1 = null;
		ptr2 = null;
		type = SINGLE;
	}

	public void shuffleDouble() {
		BNode4<T> left = new BNode4<>(data);
		BNode4<T> right = new BNode4<>(data1);

		BNode4<T> ptr2Left;
		BNode4<T> ptr2Right;

		if (ptr2.getType() == SINGLE) {
			ptr2Left = ptr2.getPtr1();
			ptr2Right = ptr2.getPtr4();
		} else {
			ptr2Left = new BNode4<>(ptr2.getData2());
			ptr2Right = new BNode4<>(ptr2.getData1());

			combine(ptr2Left, ptr2.getPtr1(), ptr2.getPtr2());
			combine(ptr2Right, ptr2.getPtr3(), ptr2.getPtr4());
		}

		combine(left, ptr1, ptr2Left);
		combine(right, ptr2Right, ptr4);
		setPtr1(left);
		setPtr4(right);

		data = ptr2.getData();
		type = SINGLE;
		ptr2.clear();
		ptr2 = null;
	}

	public void rotateDoubleLeafLeft(T item) {
		setPtr1(new BNode4<>(data));
		setPtr4(new BNode4<>(item));

		data = data1;
		data1 = null;
		type = SINGLE;
	}

	public void rotateDoubleLeafRight(T item) {
		setPtr1(new BNode4<>(item));
		setPtr4(new BNode4<>(data1));

		data1 = null;
		type = SINGLE;
	}

	public BNode4<T> splitTripleLeafAndGetPtr1() {
		splitTripleLeaf();
		return ptr1;
	}

	public BNode4<T> splitTripleLeafAndGetPtr4() {
		splitTripleLeaf();
		return ptr4;
	}

	public void setDoubleLeft(T item) {
		data1 = data;
		data = item;

		type = DOUBLE;
	}

	public void setDoubleRight(T item) {
		data1 = item;

		type = DOUBLE;
	}

	public void setTripleLeftAndRight(T left, T right) {
		data2 = left;
		data1 = right;

		type = TRIPLE;
	}

	public void setTripleLeft(T item) {
		data2 = item;

		type = TRIPLE;
	}

	public void setTripleRight(T item) {
		data2 = data;
		data = data1;
		data1 = item;

		type = TRIPLE;
	}

	public void setTripleCenter(T item) {
		data2 = data;
		data = item;

		type = TRIPLE;

	}

	public T getData() {
		return data;
	}

	public void setData(T item) {
		this.data = item;
	}

	public T getData1() {
		return data1;
	}

	public void setData1(T item) {
		this.data1 = item;
	}

	public T getData2() {
		return data2;
	}

	public void setData2(T item) {
		this.data2 = item;
	}

	public BNode4<T> getPtr1() {
		return ptr1;
	}

	public boolean isPtr1Child() {
		return parent.getPtr1() == this;
	}

	public void setPtr1(BNode4<T> ptr) {
		ptr.setParent(this);
		ptr1 = ptr;
	}

	public BNode4<T> getPtr2() {
		return ptr2;
	}

	public boolean isPtr2Child() {
		return parent.getPtr2() == this;
	}

	public void setPtr2(BNode4<T> ptr) {
		ptr.setParent(this);
		ptr2 = ptr;
	}

	public BNode4<T> getPtr3() {
		return ptr3;
	}

	public boolean isPtr3Child() {
		return parent.getPtr3() == this;
	}

	public void setPtr3(BNode4<T> ptr) {
		ptr.setParent(this);
		ptr3 = ptr;
	}

	public BNode4<T> getPtr4() {
		return ptr4;
	}

	public boolean isPtr4Child() {
		return parent.getPtr4() == this;
	}

	public void setPtr4(BNode4<T> ptr) {
		ptr.setParent(this);
		ptr4 = ptr;
	}

	public BNode4<T> getParent() {
		return parent;
	}

	public void setParent(BNode4<T> parent) {
		this.parent = parent;
	}

	public byte getType() {
		return type;
	}
}
