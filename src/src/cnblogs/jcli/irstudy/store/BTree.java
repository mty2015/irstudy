package cnblogs.jcli.irstudy.store;

public class BTree {
	
	private Storage storage;

	private Node root;
	
	public BTree(Storage storage){
		this.storage = storage;
		int rootPointer = storage.readInt();
		storage.seek(rootPointer);
		root = StorageUtils.loadNode(storage);
	}
	
	public int insert(DataItem item){
		Node insertedLeaf = searchInsertedLeaf(root,item);
		if(insertedLeaf == null){//the tree is empty,create root node
			root = StorageUtils.allocateNewNode(storage);
			root.addDataItem(item);
			StorageUtils.flushToStorage(root,storage);
			return root.getStoragePointer();
		}else{
			if(insertedLeaf.isFull()){//leaf node is full,so must split up the leaf 
				splitUp(insertedLeaf);
				//after split up ,then recall insert operation
				return insert(item);
			}else{
				insertedLeaf.addDataItem(item);
				StorageUtils.flushToStorage(insertedLeaf,storage);
				return insertedLeaf.getStoragePointer();
			}
		}
	}
	
	private void splitUp(Node insertedLeaf) {
		
		
	}

	private Node searchInsertedLeaf(Node node,DataItem item){
		if(node == null)
			return null;
		//if the root is full,split up
		if(node.isFull()){
			splitUp(node);
			storage.seek(node.getParent().getStoragePointer());
			Node searchNode = StorageUtils.loadNode(storage);
			return searchInsertedLeaf(searchNode,item);
		}
		
		if(node.getCount() == 0)
			return node;
		
		for(int i = 0 ; i < node.getCount() ; i++){
			DataItem currentItem = node.getDataItems()[i];
			if(item.compareTo(currentItem) == 0){
				throw new RuntimeException("it is denied to insert duplicated data item");
			}else if(item.compareTo(currentItem) < 0 ){
				storage.seek(node.getChild()[i].getStoragePointer());
				Node searchChild = StorageUtils.loadNode(storage);
				return searchInsertedLeaf(searchChild,item);
			}
		}
		//search on the last child node
		storage.seek(node.getChild()[node.getCount()].getStoragePointer());
		Node searchChild = StorageUtils.loadNode(storage);
		return searchInsertedLeaf(searchChild,item);
		
	}
	
}
