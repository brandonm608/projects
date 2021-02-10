package com.example.tree;

class BNode<T> {
	public static final byte SINGLE = 0;
	public static final byte DOUBLE = 1;

	private BNode<T> ptr1;
	private BNode<T> ptr2;
	private BNode<T> ptr3;

	private BNode<T> parent;

	private T singleLeftData;
	private T rightData;

	private byte type;

	protected void combineWithPtr1() {
		BNode<T> ptr = ptr1;

		rightData = singleLeftData;
		singleLeftData = ptr.getSingleLeftData();

		setPtr1NoCheck(ptr.getPtr1());
		setPtr2NoCheck(ptr.getPtr3());

		type = DOUBLE;
		ptr.clear();
	}

	protected void combineWithPtr3() {
		BNode<T> ptr = ptr3;

		rightData = ptr.getSingleLeftData();

		setPtr2NoCheck(ptr.getPtr1());
		setPtr3NoCheck(ptr.getPtr3());

		type = DOUBLE;
		ptr.clear();
	}

	protected void clear() {
		ptr1 = null;
		ptr2 = null;
		ptr3 = null;

		parent = null;

		singleLeftData = null;
		rightData = null;
	}

	public BNode(T item) {
		singleLeftData = item;

		ptr1 = null;
		ptr2 = null;
		ptr3 = null;

		parent = null;

		rightData = null;

		type = 0;
	}

	public BNode<T> combineWithParent() {
		BNode<T> parent = this.parent;

		if (parent.getPtr1() == this) {
			parent.combineWithPtr1();
		} else {
			parent.combineWithPtr3();
		}

		return parent;
	}

	public void rotateLeft() {
		BNode<T> ptr = new BNode<>(singleLeftData);

		ptr.setPtr1NoCheck(ptr1);
		ptr.setPtr3NoCheck(ptr2);

		setPtr1NoCheck(ptr);

		type = SINGLE;
		singleLeftData = rightData;
		rightData = null;
		ptr2 = null;
	}

	public void rotateRight() {
		BNode<T> ptr = new BNode<>(rightData);

		ptr.setPtr1NoCheck(ptr2);
		ptr.setPtr3NoCheck(ptr3);

		setPtr3NoCheck(ptr);

		type = SINGLE;
		rightData = null;
		ptr2 = null;
	}

	public void shuffle() {
		BNode<T> left = new BNode<>(singleLeftData);
		BNode<T> right = new BNode<>(rightData);

		BNode<T> ptr2Left = ptr2.getPtr1();
		BNode<T> ptr2Right = ptr2.getPtr3();

		left.setPtr1NoCheck(ptr1);
		left.setPtr3NoCheck(ptr2Left);
		right.setPtr1NoCheck(ptr2Right);
		right.setPtr3NoCheck(ptr3);

		singleLeftData = ptr2.getSingleLeftData();
		setPtr1NoCheck(left);
		setPtr3NoCheck(right);

		type = SINGLE;
		rightData = null;

		ptr2.clear();
		ptr2 = null;
	}

	public void rotateLeafLeft(T item) {
		setPtr1NoCheck(new BNode<>(singleLeftData));
		setPtr3NoCheck(new BNode<>(item));

		type = SINGLE;
		singleLeftData = rightData;
		rightData = null;
	}

	public void rotateLeafRight(T item) {
		setPtr1NoCheck(new BNode<>(item));
		setPtr3NoCheck(new BNode<>(rightData));

		type = SINGLE;
		rightData = null;
	}

	public void splitLeaf(T item) {
		BNode<T> left = new BNode<>(singleLeftData);
		BNode<T> right = new BNode<>(rightData);

		type = SINGLE;
		singleLeftData = item;
		setPtr1NoCheck(left);
		setPtr3NoCheck(right);

		rightData = null;
	}

	public T getSingleLeftData() {
		return singleLeftData;
	}

	public void setLeftData(T leftData) {
		this.singleLeftData = leftData;
	}

	public void setSingleLeftData(T singleLeftData) {
		this.singleLeftData = singleLeftData;
		type = SINGLE;
	}

	public void setDoubleLeftData(T leftData) {
		rightData = singleLeftData;
		this.singleLeftData = leftData;
		type = DOUBLE;
	}

	public T getRightData() {
		return rightData;
	}

	public void setRightData(T rightData) {
		this.rightData = rightData;
		type = DOUBLE;
	}

	public void replaceRightData(T rightData) {
		this.rightData = rightData;
	}

	public void setPtr1(BNode<T> ptr) {
		if (ptr != null) {
			setPtr1NoCheck(ptr);
			return;
		}

		ptr1 = null;
	}

	public void setPtr1NoCheck(BNode<T> ptr) {
		ptr.setParent(this);
		ptr1 = ptr;
	}

	public BNode<T> getPtr1() {
		return ptr1;
	}

	public boolean isPtr1Child() {
		return parent.getPtr1() == this;
	}

	public void setPtr2(BNode<T> ptr) {
		if (ptr != null) {
			setPtr2NoCheck(ptr);
			return;
		}

		ptr2 = null;
	}

	public void setPtr2NoCheck(BNode<T> ptr) {
		ptr.setParent(this);
		ptr2 = ptr;
	}

	public BNode<T> getPtr2() {
		return ptr2;
	}

	public boolean isPtr2Child() {
		return parent.getPtr2() == this;
	}

	public void setPtr3(BNode<T> ptr) {
		if (ptr != null) {
			setPtr3NoCheck(ptr);
			return;
		}
		ptr3 = null;
	}

	public void setPtr3NoCheck(BNode<T> ptr) {
		ptr.setParent(this);
		ptr3 = ptr;
	}

	public BNode<T> getPtr3() {
		return ptr3;
	}

	public boolean isPtr3Child() {
		return parent.getPtr3() == this;
	}

	public BNode<T> getParent() {
		return parent;
	}

	public void setParent(BNode<T> parent) {
		this.parent = parent;
	}

	public byte getType() {
		return type;
	}
}
