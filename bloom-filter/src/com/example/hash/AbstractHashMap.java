package com.example.hash;

class AbstractHashMap<K, V> implements Map<K, V> {
	private AbstractHashSet<HashKeyEntry<K, V>> hashTable;

	protected HashKeyEntry<K, V> getEntry(HashKeyEntry<K, V> keyEntry) {
		return hashTable.get(keyEntry);
	}

	protected static <K, V> Murmur3<HashKeyEntry<K, V>> keyHash(Murmur3<K> hash) {
		return Murmur3.obj2Hash((s, obj) -> hash.hashObject(s, obj.getKey()));
	}

	public AbstractHashMap(AbstractHashSet<HashKeyEntry<K, V>> hashTable) {
		this.hashTable = hashTable;
	}

	@Override
	public void clear() {
		hashTable.clear();
	}

	@Override
	public AbstractUnmodifiableHashSet<Entry<K, V>> entrySet() {
		return new EntrySet<>(hashTable);
	}

	@Override
	public void put(K key, V value) {
		HashKeyEntry<K, V> entry = new HashKeyEntry<>(Map.entry(key, value));
		hashTable.add(entry);
	}

	@Override
	public V get(K key) {
		HashKeyEntry<K, V> entry = new HashKeyEntry<>(Map.entry(key, null));
		entry = getEntry(entry);

		if (entry != null) {
			return entry.getEntry().getValue();
		}

		return null;
	}

	@Override
	public AbstractUnmodifiableHashSet<K> keySet() {
		return new KeySet<K, V>(hashTable);
	}

	@Override
	public V remove(K key) {
		HashKeyEntry<K, V> entry = new HashKeyEntry<>(Map.entry(key, null));
		entry = getEntry(entry);
		V tmp = entry == null ? null : entry.getEntry().getValue();
		hashTable.remove(entry);
		return tmp;
	}

	@Override
	public int size() {
		return hashTable.size();
	}
}
