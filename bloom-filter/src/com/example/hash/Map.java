package com.example.hash;

public interface Map<K, V> {
	public static interface Entry<K, V> {
		public K getKey();

		public V getValue();
	}

	public static <K, V> Entry<K, V> entry(final K key, final V value) {
		return new Entry<K, V>() {
			private K k = key;
			private V v = value;

			public K getKey() {
				return k;
			}

			public V getValue() {
				return v;
			}

			@Override
			public boolean equals(Object obj) {
				if (obj instanceof Entry) {
					Entry<K, V> entry = (Entry<K, V>) obj;
					return this.k.equals(entry.getKey()) && this.v.equals(entry.getValue());
				}

				return false;
			}

			@Override
			public int hashCode() {
				return key.hashCode();
			}

			@Override
			public String toString() {
				return "{" + k.toString() + ", " + v.toString() + "}";
			}
		};
	}

	public void clear();

	public AbstractUnmodifiableHashSet<Map.Entry<K, V>> entrySet();

	public void put(K key, V value);

	public V get(K key);

	public AbstractUnmodifiableHashSet<K> keySet();

	public V remove(K key);

	public int size();
}
