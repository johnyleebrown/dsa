package bits;

public class Operations {

	/**
	 * Unset n-th bit
	 */
	public int unsetNthBit(int bitmask, int n) {
		return bitmask &= ~(1 << n);
	}

	public boolean isSet(int mask, int i, int length) {
		int position = length - i;
		return ((mask >> position) & 1) == 1;
	}

	public boolean isSet(int mask, int position) {
		return ((mask >> position) & 1) == 1;
	}

	public boolean isSet2(int mask, int i, int length) {
		int position = length - i;
		return (mask & (~mask | (1 << position))) != 0;
	}

	public static int setBit(int mask, int i, int length) {
		int position = length - i;
		return (1 << position) | mask;
	}

	// from the end
	public int findFirstSet(int mask, StringBuilder s) {
		if (mask == 0) {
			return 0;
		}
		int end = s.length() - 1;
		while (((mask & 1) != 1) && end != -1) {
			mask >>= 1;
			end--;
		}
		if (end == -1) {
			return 0;
		}
		return end + 1;
	}
}
