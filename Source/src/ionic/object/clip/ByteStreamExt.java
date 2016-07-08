package ionic.object.clip;

public final class ByteStreamExt {

	public void skip(int length) {
		currentOffset += length;
	}

	public ByteStreamExt(byte abyte0[]) {
		buffer = abyte0;
		currentOffset = 0;
	}

	public int readUnsignedByte() {
		return buffer[currentOffset++] & 0xff;
	}

	public byte readSignedByte() {
		return buffer[currentOffset++];
	}

	public int readUnsignedWord() {
		currentOffset += 2;
		return ((buffer[currentOffset - 2] & 0xff) << 8)
				+ (buffer[currentOffset - 1] & 0xff);
	}

	public int readSignedWord() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 2] & 0xff) << 8)
				+ (buffer[currentOffset - 1] & 0xff);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}


	public String readNewString() {
		int i = currentOffset;
		while (buffer[currentOffset++] != 0)
			;
		return new String(buffer, i, currentOffset - i - 1);
	}

	public byte buffer[];
	public int currentOffset;
}
