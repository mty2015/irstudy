package cnblogs.jcli.irstudy.store;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import cnblogs.jcli.irstudy.store.support.FileStorage;

public class NodeTest {

	public static void main(String[] args){
		BTree tree = null;
		try{
			Storage store = new FileStorage(new File("/home/tony/index.data"));
			tree = new BTree(store);
			for(int i = 0 ; i< 100 ; i++){
				DataItem item = new DataItem();
				item.setKey((int)(new Random().nextDouble() * 100000) + "");
				tree.insert(item);
				System.out.println(i);
			}
			
//			DataItem item = new DataItem();
//			item.setKey("25626");
//			tree.insert(item);
//			
//			item = new DataItem();
//			item.setKey("87358");
//			tree.insert(item);
//			
//			item = new DataItem();
//			item.setKey("31961");
//			tree.insert(item);
//			
//			item = new DataItem();
//			item.setKey("22627");
//			tree.insert(item);
//			
//			item = new DataItem();
//			item.setKey("77456");
//			tree.insert(item);
//			
//			item = new DataItem();
//			item.setKey("78034");
//			tree.insert(item);
//			
//			item = new DataItem();
//			item.setKey("9410");
//			tree.insert(item);
//			
//			item = new DataItem();
//			item.setKey("1366");
//			tree.insert(item);
			
//			DataItem item = new DataItem();
//			item.setKey("23963");
//			tree.insert(item);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			tree.releaseSource();
		}
		
		
		
	}
}
