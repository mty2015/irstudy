package cnblogs.jcli.irstudy.suport.struct;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.Serializable;

import cnblogs.jcli.irstudy.utils.Assert;

public class BTreeIndexManager {

	private RandomAccessFile file;
	
	public BTreeIndexManager(File indexFile){
		Assert.notNull(indexFile);
		if(indexFile.isDirectory()){
			throw new IllegalArgumentException("the indexFile argument must be a binary stream file");
		}
		
		try {
			file = new RandomAccessFile(indexFile,"rw");
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("the indexFile argument does not exist!");
		}
		
	}
	
	public void put(String term,int documentId){
		
	}
	
	public int search(String term){
		
		return 0;
	}
	
	public void compress(){
		
	}
	
	static class BTreeNode implements Serializable{
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		
		private String[] terms = new String[3];
		
		private BTreeNode[] childs = new BTreeNode[4];
		
		
	}
	
	
}
