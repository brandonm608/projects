package com.example.hash;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.function.BiFunction;

public class Murmur3<T> {
	public static class Murmur3Hash {
		private long mostSignificantBits;
		private long leastSignificantBits;

		private String bitsToHexString(long l) {
			String str1 = Long.toHexString(l);
			StringBuilder str2 = new StringBuilder();

			for (int i = str1.length(); i < 16; i++) {
				str2.append(0);
			}

			return str2.toString() + str1;
		}

		public Murmur3Hash(long mostSignificantBits, long leastSignificantBits) {
			this.mostSignificantBits = mostSignificantBits;
			this.leastSignificantBits = leastSignificantBits;
		}

		public long getMostSignificantBits() {
			return mostSignificantBits;
		}

		public long getLeastSignificantBits() {
			return leastSignificantBits;
		}

		@Override
		public String toString() {
			return "0x" + bitsToHexString(mostSignificantBits) + bitsToHexString(leastSignificantBits);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Murmur3Hash) {
				Murmur3Hash hash = (Murmur3Hash) obj;
				return mostSignificantBits == hash.getMostSignificantBits()
						&& leastSignificantBits == hash.getLeastSignificantBits();
			}

			return false;
		}

		@Override
		public int hashCode() {
			return (int) leastSignificantBits & ((1 << 32) - 1);
		}
	}

	private final BiFunction<Integer, T, Murmur3Hash> hash;

	protected static long fmix64(long k) {
		k ^= k >> 33;
		k *= 0xff51afd7 << 4 | 0xed558ccd;
		k ^= k >> 33;
		k *= 0xc4ceb9fe << 4 | 0x1a85ec53;
		k ^= k >> 33;

		return k;
	}

	protected static long rotl64(long x, int r) {
		return (x << r) | (x >> (64 - r));
	}

	protected static Murmur3Hash murmurHash3(ByteBuffer key, int len, long seed) {
		ByteBuffer data = key;
		int nblocks = len / 16;

		long h1 = seed;
		long h2 = seed;

		long c1 = 0x87c37b91 << 4 | 0x114253d5;
		long c2 = 0x4cf5ad43 << 4 | 0x2745937f;

		// ----------
		// body

		ByteBuffer blocks = data;

		for (int i = 0; i < nblocks; i++) {
			long k1 = blocks.getLong();
			long k2 = blocks.getLong();

			k1 *= c1;
			k1 = rotl64(k1, 31);
			k1 *= c2;
			h1 ^= k1;

			h1 = rotl64(h1, 27);
			h1 += h2;
			h1 = h1 * 5 + 0x52dce729;

			k2 *= c2;
			k2 = rotl64(k2, 33);
			k2 *= c1;
			h2 ^= k2;

			h2 = rotl64(h2, 31);
			h2 += h1;
			h2 = h2 * 5 + 0x38495ab5;
		}

		// ----------
		// tail

		int offset = nblocks * 16;
		byte[] tail = data.array();

		long k1 = 0;
		long k2 = 0;

		switch (len & 15) {
		case 15:
			k2 ^= ((long) tail[offset + 14]) << 48;
		case 14:
			k2 ^= ((long) tail[offset + 13]) << 40;
		case 13:
			k2 ^= ((long) tail[offset + 12]) << 32;
		case 12:
			k2 ^= ((long) tail[offset + 11]) << 24;
		case 11:
			k2 ^= ((long) tail[offset + 10]) << 16;
		case 10:
			k2 ^= ((long) tail[offset + 9]) << 8;
		case 9:
			k2 ^= ((long) tail[offset + 8]) << 0;
			k2 *= c2;
			k2 = rotl64(k2, 33);
			k2 *= c1;
			h2 ^= k2;

		case 8:
			k1 ^= ((long) tail[offset + 7]) << 56;
		case 7:
			k1 ^= ((long) tail[offset + 6]) << 48;
		case 6:
			k1 ^= ((long) tail[offset + 5]) << 40;
		case 5:
			k1 ^= ((long) tail[offset + 4]) << 32;
		case 4:
			k1 ^= ((long) tail[offset + 3]) << 24;
		case 3:
			k1 ^= ((long) tail[offset + 2]) << 16;
		case 2:
			k1 ^= ((long) tail[offset + 1]) << 8;
		case 1:
			k1 ^= ((long) tail[offset + 0]) << 0;
			k1 *= c1;
			k1 = rotl64(k1, 31);
			k1 *= c2;
			h1 ^= k1;
		}

		// ----------
		// finalization

		h1 ^= len;
		h2 ^= len;

		h1 += h2;
		h2 += h1;

		h1 = fmix64(h1);
		h2 = fmix64(h2);

		h1 += h2;
		h2 += h1;

		return new Murmur3Hash(h1, h2);
	}

	private Murmur3(BiFunction<Integer, T, Murmur3Hash> hash) {
		this.hash = hash;
	}

	public Murmur3Hash hashObject(int seed, T obj) {
		return hash.apply(seed, obj);
	}

	public static <T> Murmur3<T> obj2Hash(BiFunction<Integer, T, Murmur3Hash> hash) {
		return new Murmur3<T>(hash);
	}

	public static Murmur3<Long> murmur3Long() {
		return new Murmur3<Long>(Murmur3::hash);
	}

	public static Murmur3<Integer> murmur3Int() {
		return new Murmur3<Integer>(Murmur3::hash);
	}

	public static Murmur3<Short> murmur3Short() {
		return new Murmur3<Short>(Murmur3::hash);
	}

	public static Murmur3<Byte> murmur3Byte() {
		return new Murmur3<Byte>(Murmur3::hash);
	}

	public static Murmur3<Character> murmur3Char() {
		return new Murmur3<Character>(Murmur3::hash);
	}

	public static Murmur3<String> murmur3String() {
		return new Murmur3<String>(Murmur3::hash);
	}

	public static Murmur3Hash hash(int seed, Long... args) {
		ByteBuffer buf = ByteBuffer.allocate(2 * Long.BYTES);

		buf.mark();
		for (long l : args) {
			buf.asLongBuffer().put(l);
		}
		buf.reset();

		return murmurHash3(buf, args.length * Long.BYTES, seed);
	}

	public static Murmur3Hash hash(int seed, Integer... args) {
		ByteBuffer buf = ByteBuffer.allocate(Integer.BYTES);

		buf.mark();
		for (int i : args) {
			buf.asIntBuffer().put(i);
		}
		buf.reset();

		return murmurHash3(buf, args.length * Integer.BYTES, seed);
	}

	public static Murmur3Hash hash(int seed, Short... args) {
		ByteBuffer buf = ByteBuffer.allocate(Short.BYTES);

		buf.mark();
		for (short s : args) {
			buf.asShortBuffer().put(s);
		}
		buf.reset();

		return murmurHash3(buf, args.length * Short.BYTES, seed);
	}

	protected static Murmur3Hash hash(int seed, byte... args) {
		ByteBuffer buf = ByteBuffer.wrap(args);
		return murmurHash3(buf, args.length * Byte.BYTES, seed);
	}

	public static Murmur3Hash hash(int seed, Byte... args) {
		ByteBuffer buf = ByteBuffer.allocate(args.length);

		buf.mark();
		for (byte b : args) {
			buf.put(b);
		}
		buf.reset();

		return hash(seed, buf.array());
	}

	public static Murmur3Hash hash(int seed, Byte arg) {
		ByteBuffer buf = ByteBuffer.allocate(1);

		buf.mark();
		buf.put(arg);
		buf.reset();

		return hash(seed, buf.array());
	}

	public static Murmur3Hash hash(int seed, Character... args) {
		CharBuffer buf = CharBuffer.allocate(args.length);

		buf.mark();
		for (char c : args) {
			buf.put(c);
		}
		buf.reset();
		return hash(seed, String.valueOf(buf.array()).getBytes());
	}

	public static Murmur3Hash hash(int seed, String s) {
		return hash(seed, s.getBytes());
	}
}
