package cnblogs.jcli.irstudy.suport.struct;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	/**
	 * <pre>
	 * ---------------------------------------------
	 * |count(byte)|term(String) * 3|childs(int) * 4
	 * ---------------------------------------------
	 * </pre>
	 *
	 * @author tony.li.fly@gmail.com
	 */
	static class BTreeNode implements Serializable{
		public static int NODE_LEN = 30;
		
		/**
		 * serialVersionUID
		 */
		private static final long serialVersionUID = 1L;
		
		private boolean isFresh; 

		private byte[] byteContent;
		
		private int count;
		
		private String[] terms = new String[3];
		
		private int[] childs = new int[4];
		
		public BTreeNode(){
			
		}
		
		public BTreeNode(byte[] bytes){
			super();
			if(bytes == null || bytes.length > NODE_LEN){
				throw new BTreeException("bytes must not be null and it's len must be less than " + NODE_LEN);
			}
			
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			DataInput di = new DataInputStream(bis);
			
			try {
				this.count = di.readInt();
				if(count < 0 || count > 3)
					throw new IOException("contains error 'count' byte");
				for(int i = 0;i < count;i++){
					terms[i] = di.readUTF();
				}
				for(int i = 0;i <= count;i++){
					childs[i] = di.readInt();
				}
				childs[count] = di.readInt();
				
				this.byteContent = bytes;
				isFresh = true;
			} catch (IOException e) {
				throw new BTreeException("bytes maybe not follow the principle of the BTreeNode",e);
			}
		}
		
		public boolean insertTerm(String term){
			isFresh = false;
			if(count >= 3)
				return false;
			terms[count++] = term;
			return true;
		}
		
		public byte[] getBytes(){
			if(isFresh)
				return byteContent;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutput dataOutput = new DataOutputStream(baos);
			try {
				dataOutput.writeByte(count);
				for(int i = 0;i < count;i++){
					dataOutput.writeUTF(terms[i]);
				}
				for(int i = 0;i <= count;i++){
					dataOutput.writeInt(childs[i]);
				}
				byteContent = baos.toByteArray();
				isFresh = true;
				return byteContent;
			} catch (IOException e) {
				throw new BTreeException(e);
			} 
		}
	}
	
	
}
