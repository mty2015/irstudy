package cnblogs.jcli.irstudy.store;

import cnblogs.jcli.irstudy.utils.NumberUtils;

public class Node {

	protected static final int DATAITEMS_COUNT = 3;
	
	/**
	 * the binary protocol is below:
	 * <pre>
	 * -----------------------------------------------------------------------------------------------------------
	 * currentPointer(4) | parentPointer(4) | count(1) | isLeaf(1) | dataItems | childPoints |                                      
	 * -----------------------------------------------------------------------------------------------------------
	 * </pre>
	 */
	public static final int NODE_BYTES_LEN = 4 + 4 + 1 + 1 + DataItem.BYTE_LENGTH * DATAITEMS_COUNT + (4 * DATAITEMS_COUNT + 1);

	private int storagePointer;

	private DataItem[] dataItems = new DataItem[DATAITEMS_COUNT];

	private Node[] child = new Node[DATAITEMS_COUNT + 1];

	private Node parent;

	private int count;
	
	private boolean isLeaf = true;

	public Node() {

	}

	protected void addDataItem(DataItem item, Node... insertedChild) {
		if (count >= DATAITEMS_COUNT)
			throw new RuntimeException(
					"the node couldn't contains dataitems over the pre-set counts");

		int i = 0;
		/*
		 * insert data item
		 */
		for (; i < count; i++) {
			if (item.compareTo(dataItems[i]) < 0) {
				for (int j = count; j > i; j--) {
					dataItems[j] = dataItems[j - 1];
				}
				break;
			}
		}
		dataItems[i] = item;
		/*
		 * insert the child reference
		 */
		for (int j = count+1; j > i+1; j--) {
			child[j] = child[j - 1];
		}
		if(insertedChild != null){
			if(insertedChild.length == 1)
				child[i+1] = insertedChild[0];
			else if(insertedChild.length == 2){
				child[0] = insertedChild[0];
				child[1] = insertedChild[0];
			}
		}
		
		//count increment
		count++;
		
		
		
	}

	/**
	 * the binary protocol is below:
	 * <pre>
	 * -----------------------------------------------------------------------------------------------------------
	 * currentPointer(4) | parentPointer(4) | count(1) | isLeaf(1) | dataItems | childPoints |                                      
	 * -----------------------------------------------------------------------------------------------------------
	 * </pre>
	 * @return
	 */
	public byte[] toBytes() {
		byte[] b = new byte[NODE_BYTES_LEN];
		int coordinate = 0;
		System.arraycopy(NumberUtils.intToByteArray(storagePointer), 0, b, coordinate, 4);
		coordinate += 4;
		System.arraycopy(NumberUtils.intToByteArray(parent.getStoragePointer()), 0, b, coordinate, 4);
		coordinate += 4;
		b[8] = (byte)count;
		coordinate += 1;
		b[9] = isLeaf ? (byte)1 : (byte)0;
		coordinate += 1;
		for(int i = 0 ; i < count ; i++){
			System.arraycopy(dataItems[i].toBytes(), 0, b, coordinate, DataItem.BYTE_LENGTH);
			coordinate += DataItem.BYTE_LENGTH;
		}
		if(!isLeaf){// if one node is a leaf node ,then the node has not any child
			for(int i = 0 ; i<= count ; i++){
				System.arraycopy(NumberUtils.intToByteArray(child[i].storagePointer), 0, b, coordinate, 4);
				coordinate += 4;
			}
		}
		return b;
	}

	public void fill(byte[] b) {
		
	}

	public boolean isFull() {
		return count >= DATAITEMS_COUNT ? true : false;
	}

	/**
	 * clear data in the node ,but don't clear up the info about the storage
	 * pointer
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
	
	public void setCount(int count){
		this.count = count;
	}

	public int getCount() {
		return count;
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

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

}
