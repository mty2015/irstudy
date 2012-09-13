package cnblogs.jcli.irstudy.store;

public interface Storage {

	public void seek(long offset);
	
	public void write(byte[] b);

	public long getPointer();

	public int read(byte[] b);

	public long length();

	public void close();
	
}
