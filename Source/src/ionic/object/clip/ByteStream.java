package ionic.object.clip;

public class ByteStream {

	private byte[] buffer;
	private int offset;

	public ByteStream(byte[] buffer) {
		this.buffer = buffer;
		this.offset = 0;
	}

	public void skip(int length) {
		offset += length;
	}

	public void setOffset(int position) {
		offset = position;
	}

	public void setOffset(long position) {
		offset = (int) position;
	}

	public int length() {
		return buffer.length;
	}

	public byte getByte() {
		return buffer[offset++];
	}

	public int getUByte() {
		return buffer[offset++] & 0xff;
	}

	public int getUShort() {
		return (getUByte() << 8) + getUByte();
	}

	public int getInt() {
		return (getUByte() << 24) + (getUByte() << 16) + (getUByte() << 8)
				+ getUByte();
	}

	public long getLong() {
		return (getUByte() << 56) + (getUByte() << 48) + (getUByte() << 40) + (getUByte() << 32) + (getUByte() << 24) + (getUByte() << 16) + (getUByte() << 8) + getUByte();
	}

	public int getUSmart() {
		int i = buffer[offset] & 0xff;
		if (i < 128) {
			return getUByte();
		} else {
			return getUShort() - 32768;
		}
	}

	public byte[] read(int length) {
		byte[] b = new byte[length];
		for (int i = 0; i < length; i++)
			b[i] = buffer[offset++];
		return b;
	}

}
