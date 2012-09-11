package cnblogs.jcli.irstudy.store;

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

	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
