package cnblogs.jcli.irstudy.store;

public class DataItem implements Comparable<DataItem>{

	private String key;
	
	private int storageOffset;
	
	@Override
	public int compareTo(DataItem o) {
		return 0;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getStorageOffset() {
		return storageOffset;
	}

	public void setStorageOffset(int storageOffset) {
		this.storageOffset = storageOffset;
	}
	
	
}
