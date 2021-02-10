package com.example.hash;

import java.util.Iterator;

public class Cuckoo3HashSet<T> extends AbstractHashSet<T> {
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

		hash = hash3(orphan);
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
					if (!isSet(hash)) {
						set(orphan, hash);
						return true;
					}

					if (!isSet(hash2)) {
						set(orphan, hash2);
						return true;
					}

					hash2 = hash3(orphan);
					if (pos != hash2) {
						i = 0;
						if (!isSet(hash2)) {
							set(orphan, hash2);
							return true;
						}
					} else {
						i++;
					}

					pos = hash;
				} else {
					i++;
					if (!isSet(hash)) {
						set(orphan, hash);
						return true;
					}

					hash2 = hash3(orphan);
					if (!isSet(hash2)) {
						set(orphan, hash2);
						return true;
					}

					pos = hash2;
				}
			} else {
				i++;
				hash = hash3(orphan);
				if (!isSet(hash)) {
					set(orphan, hash);
					return true;
				}

				if (!isSet(hash2)) {
					set(orphan, hash2);
					return true;
				}

				pos = hash2;
			}

			tmp = (T) objs[pos];
			objs[pos] = orphan;
			orphan = tmp;
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
		int pos = lookup(obj, hash(obj));

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

	protected int hash3(T obj) {
		return hash2Pos(hash.hashObject((Integer.MAX_VALUE >> 1) + seedCount, obj));
	}

	protected boolean rehash() {
		final int maxRehash = 20;

		for (int i = 0; i < maxRehash; i++) {
			if (seedCount == (Integer.MAX_VALUE >> 1) - 1) {
				seedCount += 2;
			} else if (seedCount < Integer.MAX_VALUE) {
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
		objs[pos] = obj;
		numElements++;
	}

	protected boolean isSet(int pos) {
		return objs[pos] != null;
	}

	protected int lookup(T obj, int hash) {
		if (isSet(hash) && objs[hash].equals(obj)) {
			return hash;
		}

		hash = hash2(obj);
		if (isSet(hash) && objs[hash].equals(obj)) {
			return hash;
		}

		hash = hash3(obj);
		if (isSet(hash) && objs[hash].equals(obj)) {
			return hash;
		}

		return -1;
	}

	protected void resize() {
		T tmp = orphan;
		int found = 0;
		int sz = numElements;
		int hash;
		Object[] oldObjs = objs;
		pow++;
		objs = new Object[1 << pow];

		numElements = 0;

		for (int pos = 0; found < sz; pos++) {
			if (oldObjs[pos] != null) {
				orphan = (T) oldObjs[pos];
				hash = hash(orphan);
				if (!insert(hash) && !evict(hash)) {
					throw new IllegalStateException("Failed to resize and the hash is in an inconsistent state.");
				}
				oldObjs[pos] = null;
				found++;
			}
		}

		orphan = tmp;
		hash = hash(orphan);
		if (!insert(hash) && !evict(hash) && !rehash()) {
			throw new IllegalStateException("Hash is in an inconsitent state after resizeing");
		}

		tmp = null;
	}

	public Cuckoo3HashSet(Murmur3<T> hash) {
		this(921, hash);
	}

	public Cuckoo3HashSet(int size, Murmur3<T> hash) {
		this(size, 0, hash);
	}

	public Cuckoo3HashSet(int size, int seed, Murmur3<T> hash) {
		super();
		if (size < 921) {
			size = 921;
		}

		int pow = (int) (Math.ceil(Math.log(size * 10.0 / 9.0) / Math.log(2.0)));
		this.pow = pow;
		objs = new Object[1 << pow];
		numElements = 0;
		maxCount = 32 * ((int) (Math.log((1 << pow) / Math.log(2))));

		if (seed == Integer.MAX_VALUE / 2) {
			seedCount = seed + 1;
		} else if (seed < 0) {
			seedCount = 0;
		} else {
			seedCount = seed;
		}

		this.hash = hash;
	}

	@Override
	public void add(T obj) {
		if (obj == null) {
			return;
		}

		orphan = obj;
		int hash = hash(orphan);
		int pos = lookup(orphan, hash);
		incrementModCount();

		if (pos > -1) {
			objs[pos] = orphan;
			return;
		}

		if (numElements > (((int) (0.9 * (1 << (pow))) - 1))) {
			resize();
			return;
		}

		if (insert(hash)) {
			return;
		}

		if (evict(hash)) {
			return;
		}

		if (!rehash()) {
			resize();
		}
	}

	@Override
	public void clear() {
		int found = 0;
		incrementModCount();

		for (int pos = 0; found < numElements; pos++) {
			if (objs[pos] != null) {
				objs[pos] = null;
				found++;
			}
		}

		numElements = 0;
	}

	@Override
	public boolean contains(T obj) {
		if (obj == null) {
			return false;
		}

		return lookup(obj, hash(obj)) > -1;
	}

	@Override
	public boolean remove(T obj) {
		int pos = lookup(obj, hash(obj));

		if (pos > -1) {
			incrementModCount();
			unset(pos);
			return true;
		}

		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return new HashIterator<T>(this);
	}

	@Override
	public int size() {
		return numElements;
	}
}
