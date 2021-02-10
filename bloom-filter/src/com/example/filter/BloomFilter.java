package com.example.filter;

import java.nio.ByteBuffer;
import java.util.UUID;

public class BloomFilter {
	public static class Hash {
		private static final short seed1 = 3;
		private static final short seed2 = 7;
		private static final short seed3 = 11;

		private int hash;

		private short pow1;
		private short pow2;
		private short pow3;

		protected long fmix64(long k) {
			k ^= k >> 33;
			k *= 0xff51afd7 << 4 | 0xed558ccd;
			k ^= k >> 33;
			k *= 0xc4ceb9fe << 4 | 0x1a85ec53;
			k ^= k >> 33;

			return k;
		}

		protected long rotl64(long x, int r) {
			return (x << r) | (x >> (64 - r));
		}

		protected long[] mumurHash3(ByteBuffer key, int len, long seed) {
			long[] out = new long[2];
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

			out[0] = h1;
			out[1] = h2;

			return out;
		}

		public Hash(UUID id) {
			// optimal; murmurhash times 3
			ByteBuffer buf = ByteBuffer.allocate(2 * Long.BYTES);

			buf.mark();
			buf.asLongBuffer().put(id.getLeastSignificantBits()).put(id.getMostSignificantBits());
			buf.reset();

			pow1 = (short) (mumurHash3(buf, 16, seed1)[0] & ((1 << 4) - 1));

			buf.reset();
			pow2 = (short) (mumurHash3(buf, 16, seed2)[0] & ((1 << 4) - 1));

			buf.reset();
			pow3 = (short) (mumurHash3(buf, 16, seed3)[0] & ((1 << 4) - 1));

			hash = (int) 1 << pow1;
			hash |= (int) 1 << pow2;
			hash |= (int) 1 << pow3;
		}

		public int getHash() {
			return hash;
		}
	}

	private int filter;

	public void add(Hash hash) {
		filter |= hash.getHash();
	}

	public boolean query(Hash hash) {
		return ((filter & hash.getHash()) ^ hash.getHash()) == 0;
	}
}
