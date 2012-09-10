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
	
	private void splitUp(Node node) {
		if(node == null || node.getCount() < Node.DATAITEMS_COUNT){//if null or not be full
			return;
		}
		if(node.getParent() == null){//split up the root node 
			//create the new root node
			Node newRoot = StorageUtils.allocateNewNode(storage);
			//crate the new right sibling node
			Node right = StorageUtils.allocateNewNode(storage);
			//process the new root
			newRoot.clear();
			newRoot.setCount(1);
			newRoot.addDataItem(node.getDataItems()[1]);// move the middle node to new root
			newRoot.getChild()[0] = node;
			newRoot.getChild()[1] = right;
			StorageUtils.flushToStorage(newRoot, storage);
			//process the new right
			right.clear();
			right.setParent(newRoot);
			right.addDataItem(node.getDataItems()[2]);
			right.getChild()[0] = node.getChild()[2];
			right.getChild()[1] = node.getChild()[4];
			
			
		}else{
			
		}
		
		
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
