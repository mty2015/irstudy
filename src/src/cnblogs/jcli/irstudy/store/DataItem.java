package cnblogs.jcli.irstudy.store;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class DataItem implements Comparable<DataItem>{
	
	protected static final int BYTE_LENGTH = 10;
	
	private String key;
	
	@Override
	public int compareTo(DataItem o) {
		if(o == null)
			return 1;
		
		
		return key.compareTo(o.getKey());
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * <pre>
	 * 	-----------------------------------
	 * 	 len(1) | content
	 * 	-----------------------------------
	 * </pre>
	 * @return
	 */
	public byte[] toBytes() {
		byte[] b = new byte[BYTE_LENGTH];
		try {
			byte[] content = key.getBytes("UTF-8");
			b[0] = (byte)content.length;
			System.arraycopy(content, 0, b, 1, content.length);
			return b;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public void fill(byte[] b) {
		int len = b[0];
		try {
			key = new String(Arrays.copyOfRange(b, 1, len+1),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		
	}

	
}
