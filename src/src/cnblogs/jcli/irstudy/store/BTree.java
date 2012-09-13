package cnblogs.jcli.irstudy.store;

import cnblogs.jcli.irstudy.utils.NumberUtils;

public class BTree {
	
	private Storage storage;

	private Node root;
	
	public BTree(Storage storage){
		this.storage = storage;
		byte[] b = new byte[4];
		storage.seek(0);
		int len = storage.read(b);
		if(len == 4){
			int rootPointer = NumberUtils.byteArrayToInt(b);
			root = StorageUtils.loadNode(storage,rootPointer);
		}
	}
	
	public long insert(DataItem item){
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
			newRoot.setLeaf(false);
			newRoot.addDataItem(node.getDataItems()[1],node,right);// move the middle node to new root
			StorageUtils.flushToStorage(newRoot, storage);
			//process the new right
			right.clear();
			right.setLeaf(node.isLeaf());
			right.setParent(newRoot);
			right.addDataItem(node.getDataItems()[2],node.getChild()[2],node.getChild()[3]);
			StorageUtils.flushToStorage(right, storage);
			//process the original node
			node.setParent(newRoot);
			node.setCount(1);
			StorageUtils.flushToStorage(right, storage);
		}else{//split up the other nodes except the root
			//create the new rightsibling node
			Node right = StorageUtils.allocateNewNode(storage);
			//process the new right
			right.clear();
			right.setLeaf(node.isLeaf());
			right.setParent(node.getParent());
			right.addDataItem(node.getDataItems()[2],node.getChild()[2],node.getChild()[3]);
			StorageUtils.flushToStorage(right, storage);
			//process the parent node
			Node parent = StorageUtils.loadNode(storage,node.getParent().getStoragePointer());
			parent.addDataItem(node.getDataItems()[1],right);
			StorageUtils.flushToStorage(right, storage);
		}
		
		
	}

	private Node searchInsertedLeaf(Node node,DataItem item){
		if(node == null)
			return null;
		//if the root is full,split up
		if(node.isFull()){
			splitUp(node);
			//splitup the node may change the the inner info with the node,so must reload the node
			node = StorageUtils.loadNode(storage, node.getStoragePointer());
			Node searchNode = StorageUtils.loadNode(storage,node.getParent().getStoragePointer());
			return searchInsertedLeaf(searchNode,item);
		}
		
		if(node.isLeaf())
			return node;
		
		for(int i = 0 ; i < node.getCount() ; i++){
			DataItem currentItem = node.getDataItems()[i];
			if(item.compareTo(currentItem) == 0){
				throw new RuntimeException("it is denied to insert duplicated data item");
			}else if(item.compareTo(currentItem) < 0 ){
				Node searchChild = StorageUtils.loadNode(storage,node.getChild()[i].getStoragePointer());
				return searchInsertedLeaf(searchChild,item);
			}
		}
		//search on the last child node
		Node searchChild = StorageUtils.loadNode(storage,node.getChild()[node.getCount()].getStoragePointer());
		return searchInsertedLeaf(searchChild,item);
		
	}
	
	public void releaseSource(){
		storage.close();
	}
	
}
