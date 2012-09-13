package cnblogs.jcli.irstudy.store;

import java.util.Arrays;

import cnblogs.jcli.irstudy.utils.NumberUtils;

public class Node {

	protected static final int DATAITEMS_COUNT = 3;

	/**
	 * the binary protocol is below:
	 * 
	 * <pre>
	 * -----------------------------------------------------------------------------------------------------------
	 * currentPointer(4) | parentPointer(4) | count(1) | isLeaf(1) | dataItems | childPoints |                                      
	 * -----------------------------------------------------------------------------------------------------------
	 * </pre>
	 */
	public static final int NODE_BYTES_LEN = 4 + 4 + 1 + 1
			+ DataItem.BYTE_LENGTH * DATAITEMS_COUNT
			+ 4 * (DATAITEMS_COUNT + 1);

	private long storagePointer;

	private DataItem[] dataItems = new DataItem[DATAITEMS_COUNT];

	private Node[] child = new Node[DATAITEMS_COUNT + 1];

	private Node parent;

	private int count;

	private boolean isLeaf = true;

	public Node() {
		for(int i = 0 ; i < DATAITEMS_COUNT ; i++){
			dataItems[i] = new DataItem();
		}
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
		for (int j = count + 1; j > i + 1; j--) {
			child[j] = child[j - 1];
		}
		if (insertedChild != null) {
			if (insertedChild.length == 1)
				child[i + 1] = insertedChild[0];
			else if (insertedChild.length == 2) {
				child[0] = insertedChild[0];
				child[1] = insertedChild[1];
			}
		}

		// count increment
		count++;

	}

	/**
	 * the binary protocol is below:
	 * 
	 * <pre>
	 * -----------------------------------------------------------------------------------------------------------
	 * currentPointer(4) | parentPointer(4) | count(1) | isLeaf(1) | dataItems | childPoints |                                      
	 * -----------------------------------------------------------------------------------------------------------
	 * </pre>
	 * 
	 * @return
	 */
	public byte[] toBytes() {
		byte[] b = new byte[NODE_BYTES_LEN];
		int coordinate = 0;
		System.arraycopy(NumberUtils.intToByteArray((int)storagePointer), 0, b,
				coordinate, 4);
		coordinate += 4;
		if(parent != null)
			System.arraycopy(
				NumberUtils.intToByteArray((int)parent.getStoragePointer()), 0, b,
				coordinate, 4);
		else
			System.arraycopy(new byte[4], 0, b,coordinate, 4);
		coordinate += 4;
		b[coordinate] = (byte) count;
		coordinate += 1;
		b[coordinate] = isLeaf ? (byte) 1 : (byte) 0;
		coordinate += 1;
		for (int i = 0; i < DATAITEMS_COUNT; i++) {
			if (i < count) {
				System.arraycopy(dataItems[i].toBytes(), 0, b, coordinate,
						DataItem.BYTE_LENGTH);
			} else {
				System.arraycopy(new byte[DataItem.BYTE_LENGTH], 0, b,
						coordinate, DataItem.BYTE_LENGTH);
			}
			coordinate += DataItem.BYTE_LENGTH;
		}
		for (int i = 0; i <= DATAITEMS_COUNT; i++) {
			if (!isLeaf && i <= count) {
				System.arraycopy(NumberUtils
						.intToByteArray((int)child[i].storagePointer), 0, b,
						coordinate, 4);
			} else {
				System.arraycopy(new byte[4], 0, b, coordinate, 4);
			}
			coordinate += 4;
		}
		return b;
	}

	/**
	 * the binary protocol see {@linkplain #toBytes()}
	 * 
	 * @param b
	 */
	public void fill(byte[] b) {
		int coordinate = 0;
		storagePointer = NumberUtils.byteArrayToInt(Arrays.copyOfRange(b,
				coordinate, coordinate + 4));
		coordinate += 4;
		int parent_storagePointer = NumberUtils.byteArrayToInt(Arrays.copyOfRange(
				b, coordinate, coordinate + 4));
		coordinate += 4;
		if(parent_storagePointer > 0){
			parent = new Node();
			parent.storagePointer = parent_storagePointer;
		}
		count = b[coordinate];
		coordinate += 1;
		isLeaf = b[coordinate] == 1 ? true : false;
		coordinate += 1;
		for (int i = 0; i < DATAITEMS_COUNT; i++) {
			if (i < count) {
				if(dataItems[i] == null)
					dataItems[i] = new DataItem();
				dataItems[i].fill(Arrays.copyOfRange(b, coordinate, coordinate
						+ DataItem.BYTE_LENGTH));
			}
			coordinate += DataItem.BYTE_LENGTH;
		}
		for (int i = 0; i <= DATAITEMS_COUNT; i++) {
			if (!isLeaf && i <= count) {
				if(child[i] == null)
					child[i] = new Node();
				child[i].storagePointer = NumberUtils.byteArrayToInt(Arrays.copyOfRange(b, coordinate, coordinate + 4));
			}
			coordinate += 4;
		}
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

	public long getStoragePointer() {
		return storagePointer;
	}

	public void setStoragePointer(long storagePointer) {
		this.storagePointer = storagePointer;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public void setCount(int count) {
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
