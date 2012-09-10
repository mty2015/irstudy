package cnblogs.jcli.irstudy.store;

public interface Storage {

	public void seek(int offset);
	
	public void write(byte[] b);

	public int getPointer();

	public int read(byte[] b);

	public int readInt();
	
}
