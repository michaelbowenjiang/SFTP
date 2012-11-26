import java.nio.ByteBuffer;


public class makePacket {

	int seqno;
	int offset;
	int size;
	byte[] data = null;
	makePacket(byte[] data, int offset, int size, int seqno)
	{
		
		this.seqno = seqno;
		this.data = data;
		this.offset = offset;
		this.size = size;

	}
	byte[] getPacket()
	{
		int dataPacket = 1;
		int bufferSize = size+8;
		ByteBuffer buf = ByteBuffer.allocate(bufferSize);
		buf.putInt(0, seqno);
		buf.putInt(4, dataPacket);
		buf.position(8);
		buf.put(data, offset, size);
		byte[] dataBuffer = buf.array();
		return dataBuffer;
	}
}