package cnblogs.jcli.irstudy.store;

public class StorageUtils {

	public static Node allocateNewNode(Storage storage){
		Node node = new Node();
		node.clear();
		node.setStoragePointer(storage.length());
		storage.seek(storage.length());
		storage.write(node.toBytes());
		return node;
	}
	
	public static Node loadNode(Storage storage,long offset){
		storage.seek(offset);
		Node node = new Node();
		byte[] b = new byte[Node.NODE_BYTES_LEN];
		int len = storage.read(b);
		if(len <= Node.NODE_BYTES_LEN){
			storage.seek(storage.getPointer() - len);//if load node fail,reset the offset
			return null;
		}
		node.fill(b);
		return node;
	}

	public static void flushToStorage(Node item,Storage storage) {
		if(item == null)
			return;
		storage.seek(item.getStoragePointer());
		
		storage.write(item.toBytes());
	}
	
	
}
