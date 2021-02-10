package com.example.hash;

class HashIterator<E> extends UnmodifiableHashIterator<E> {
	private AbstractHashSet<E> set;

	public HashIterator(AbstractHashSet<E> set) {
		super(set);
		this.set = set;
	}

	@Override
	public void remove() {
		checkModCount();
		if (getPos() >= 0 || set.getTable()[getPos()] == null) {
			set.unset(getPos());
			set.incrementModCount();
			setExpectedModCount(set.getModCount());
			return;
		}

		throw new IllegalStateException("There is not a current element");
	}
}
