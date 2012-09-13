package cnblogs.jcli.irstudy.store;

import java.io.File;
import java.io.UnsupportedEncodingException;

import cnblogs.jcli.irstudy.store.support.FileStorage;

public class NodeTest {

	public static void main(String[] args){
		BTree tree = null;
		try{
			Storage store = new FileStorage(new File("e:/abc.index"));
			tree = new BTree(store);
			DataItem item = new DataItem();
			item.setKey("a");
			tree.insert(item);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			tree.releaseSource();
		}
		
		
		
	}
}
