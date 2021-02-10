package com.example.filter;

import java.util.function.Function;

public class CuckooFilter<T, F> {
	private final int maxCount;

	private F[] objs;
	private int pow;
	private int numElements;
	private F orphanFp;
	private int orphanHash;

	private final Function<T, Integer> hash;
	private final Function<F, Integer> hash2;
	private final Function<T, F> fingerPrint;

	private boolean insert() {
		if (!isSet(orphanHash)) {
			set(orphanHash, orphanFp);
			return true;
		}

		int hash = hash2(orphanHash, orphanFp);
		if (!isSet(hash)) {
			set(hash, orphanFp);
			return true;
		}

		return false;
	}

	private boolean evict() {
		for (int i = 0; i < maxCount; i++) {
			F tmp;

			if (!isSet(orphanHash)) {
				set(orphanHash, orphanFp);
				return true;
			}

			tmp = objs[orphanHash];
			objs[orphanHash] = orphanFp;

			orphanFp = tmp;
			orphanHash = hash2(orphanHash, orphanFp);
			tmp = null;
		}

		return false;
	}

	protected F fingerPrint(T obj) {
		return fingerPrint.apply(obj);
	}

	protected int hash(T obj) {
		return hash.apply(obj) & ((1 << pow) - 1);
	}

	protected int hash2(int inverseHash, F fingerPrint) {
		return inverseHash ^ (hash2.apply(fingerPrint) & ((1 << pow) - 1));
	}

	protected void rehash() {
		throw new IllegalStateException("Hash is in an inconsistent state");
	}

	protected boolean isSet(int pos) {
		return objs[pos] != null;
	}

	protected F unset(int pos) {
		F fp = objs[pos];
		objs[pos] = null;
		numElements--;
		return fp;
	}

	protected void set(int pos, F fingerPrint) {
		objs[pos] = fingerPrint;
		numElements++;
	}

	protected int lookup(F fingerPrint, int hash) {
		if (isSet(hash) && objs[hash].equals(fingerPrint)) {
			return hash;
		}

		hash = hash2(hash, fingerPrint);
		if (isSet(hash) && objs[hash].equals(fingerPrint)) {
			return hash;
		}

		return -1;
	}

	public CuckooFilter(int size, Function<T, Integer> hash, Function<F, Integer> hash2, Function<T, F> fingerPrint) {
		int pow = (int) (Math.ceil(Math.log(size) / Math.log(2)));
		this.pow = pow;
		objs = (F[]) new Object[1 << pow];

		numElements = 0;
		maxCount = 16 * ((int) (Math.log((1 << pow) / Math.log(2))));

		this.hash = hash;
		this.hash2 = hash2;
		this.fingerPrint = fingerPrint;
	}

	public boolean add(T obj) {
		if (obj == null) {
			return false;
		}

		orphanFp = fingerPrint(obj);
		orphanHash = hash(obj);

		if (lookup(orphanFp, orphanHash) > -1) {
			return true;
		}

		if (numElements > ((1 << (pow - 1)) - 1)) {
			return false;
		}

		if (insert()) {
			return true;
		}

		if (evict()) {
			return true;
		}

		rehash();
		return false;
	}

	public boolean exists(T obj) {
		if (obj == null) {
			return false;
		}

		F fp = fingerPrint(obj);
		return lookup(fp, hash(obj)) > -1;
	}

	public F at(int i) {
		return objs[i];
	}

	public int size() {
		return numElements;
	}
}
