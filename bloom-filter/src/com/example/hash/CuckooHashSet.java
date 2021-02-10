package com.example.hash;

import java.util.Iterator;

public class CuckooHashSet<T> extends AbstractHashSet<T> {
	private final int maxCount;
	private Object[] objs;
	private T orphan;
	private int numElements;
	private int pow;
	private int seedCount;

	private final Murmur3<T> hash;

	private boolean insert(int hash) {
		if (!isSet(hash)) {
			set(orphan, hash);
			return true;
		}

		hash = hash2(orphan);
		if (!isSet(hash)) {
			set(orphan, hash);
			return true;
		}

		return false;
	}

	private boolean evict(int pos) {
		for (int i = 0; i < maxCount;) {
			int hash = hash(orphan);
			int hash2 = hash2(orphan);
			T tmp;

			if (pos != hash) {
				if (pos != hash2) {
					i = 0;
				} else {
					i++;
				}

				pos = hash;
				if (!isSet(pos)) {
					set(orphan, pos);
					return true;
				}
			} else {
				if (pos != hash) {
					i = 0;
				} else {
					i++;
				}

				pos = hash2(orphan);
				if (!isSet(pos)) {
					set(orphan, pos);
					return true;
				}
			}

			tmp = (T) objs[pos];
			objs[pos] = orphan;
			orphan = tmp;
			tmp = null;
		}

		return false;

	}

	private boolean rehashHelp() {
		boolean rehashed = true;

		for (int j = 0; j < objs.length; j++) {
			if (isSet(j)) {
				orphan = unset(j);

				if (!insert(hash(orphan))) {
					if (!evict(hash(orphan))) {
						rehashed = false;
						break;
					}
				}
			}
		}

		return rehashed;
	}

	@Override
	protected T get(T obj) {
		int pos = lookupHelp(obj, hash(obj));
		if (pos > -1) {
			return (T) objs[pos];
		}

		return null;
	}

	@Override
	protected Object[] getTable() {
		return objs;
	}

	@Override
	protected T unset(int pos) {
		T tmp = (T) objs[pos];
		objs[pos] = null;
		numElements--;
		return tmp;
	}

	protected int hash2Pos(Murmur3.Murmur3Hash hash) {
		return ((int) hash.getLeastSignificantBits()) & ((1 << pow) - 1);
	}

	protected int hash(T obj) {
		return hash2Pos(hash.hashObject(seedCount, obj));
	}

	protected int hash2(T obj) {
		return hash2Pos(hash.hashObject(Integer.MAX_VALUE - seedCount, obj));
	}

	protected boolean rehash() {
		final int maxRehash = 100;

		for (int i = 0; i < maxRehash; i++) {
			if (seedCount < Integer.MAX_VALUE) {
				seedCount++;
			} else {
				seedCount = 0;
			}
			if (!insert(hash(orphan)) && !evict(hash(orphan))) {
				continue;
			}

			if (rehashHelp()) {
				return true;
			}
		}

		return false;
	}

	protected void set(T obj, int pos) {
		objs[pos] = (T) obj;
		numElements++;
	}

	protected boolean isSet(int pos) {
		return objs[pos] != null;
	}

	protected int lookupHelp(T obj, int hash) {
		if (isSet(hash) && ((T) objs[hash]).equals(obj)) {
			return hash;
		}

		hash = hash2(obj);
		if (isSet(hash) && ((T) objs[hash]).equals(obj)) {
			return hash;
		}

		return -1;
	}

	protected void resize() {
		Object[] oldObjs = objs;
		pow++;
		objs = new Object[1 << pow];

		int found = 0;
		int sz = numElements;
		numElements = 0;

		for (int pos = 0; found < sz; pos++) {
			if (oldObjs[pos] != null) {
				int hash;
				orphan = (T) oldObjs[pos];
				hash = hash(orphan);
				if (!insert(hash) && !evict(hash)) {
					throw new IllegalStateException("Failed to resize and the hash is in an inconsistent state.");
				}
				oldObjs[pos] = null;
				found++;
			}
		}
	}

	public CuckooHashSet(Murmur3<T> hash) {
		this(614, 0, hash);
	}

	public CuckooHashSet(int size, Murmur3<T> hash) {
		this(size, 0, hash);
	}

	public CuckooHashSet(int size, int seed, Murmur3<T> hash) {
		super();
		int pow = (int) (Math.ceil(Math.log(size * 10.0 / 6.0) / Math.log(2)));
		this.pow = pow;
		objs = new Object[1 << pow];
		numElements = 0;
		maxCount = 32 * ((int) (Math.log((1 << pow) / Math.log(2))));
		seedCount = seed;
		this.hash = hash;
	}

	@Override
	public void add(T obj) {
		if (obj == null) {
			return;
		}

		orphan = obj;
		int hash = hash(orphan);
		int pos = lookupHelp(orphan, hash);
		incrementModCount();

		if (pos > -1) {
			objs[pos] = orphan;
			return;
		}

		if (numElements > (((int) (0.6 * (1 << (pow))) - 1))) {
			resize();
			orphan = obj;
			hash = hash(orphan);
		}

		if (insert(hash)) {
			return;
		}

		if (evict(hash)) {
			return;
		}

		if (!rehash()) {
			throw new IllegalStateException("Failed to rehash and the hash is in an inconsistent state.");
		}
	}

	@Override
	public void clear() {
		int found = 0;
		int sz = numElements;
		incrementModCount();

		for (int pos = 0; found < sz; pos++) {
			if (objs[pos] != null) {
				objs[pos] = null;
				found++;
			}
		}

		numElements = 0;
	}

	@Override
	public boolean contains(T obj) {
		int pos = lookupHelp(obj, hash(obj));

		if (pos >= 0) {
			return true;
		}

		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return new HashIterator<T>(this);
	}

	@Override
	public boolean remove(T obj) {
		int pos = lookupHelp(obj, hash(obj));

		if (pos > -1) {
			incrementModCount();
			unset(pos);
			return true;
		}

		return false;
	}

	@Override
	public int size() {
		return numElements;
	}
}
