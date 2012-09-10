package cnblogs.jcli.irstudy.store;


public class Node {
	
	
	protected static final int DATAITEMS_COUNT = 3;
	
	protected static final int NODE_BYTE_LENGTH = 10;
	
	private int storagePointer;
	
	private DataItem[] dataItems = new DataItem[DATAITEMS_COUNT];
	
	private Node[] child = new Node[DATAITEMS_COUNT + 1];
	
	private Node parent;
	
	private int count;

	public Node(){
		
	}

	
	public void addDataItem(DataItem item){
		if(count >= DATAITEMS_COUNT)
			throw new RuntimeException("the node couldn't contains dataitems over the pre-set counts");
		dataItems[count++] = item;;
	}
	
	public byte[] toBytes(){
		return null;
	}
	
	public void fill(byte[] b) {
		
		
	}
	
	public void flush(Storage storage) {
		// TODO Auto-generated method stub
		
	}

	public boolean isFull() {
		return count >= DATAITEMS_COUNT ? true : false;
	}

	/**
	 * clear data in the node ,but don't clear up the info about the storage pointer
	 */
	public void clear() {
		dataItems = new DataItem[DATAITEMS_COUNT];
		child = new Node[DATAITEMS_COUNT + 1];
		parent = null;
		count = 0;
		
	}
	
	public int getStoragePointer() {
		return storagePointer;
	}


	public void setStoragePointer(int storagePointer) {
		this.storagePointer = storagePointer;
	}


	public Node getParent() {
		return parent;
	}


	public void setParent(Node parent) {
		this.parent = parent;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public DataItem[] getDataItems() {
		return dataItems;
	}


	public void setDataItems(DataItem[] dataItems) {
		this.dataItems = dataItems;
	}


	public Node[] getChild() {
		return child;
	}


	public void setChild(Node[] child) {
		this.child = child;
	}

}
