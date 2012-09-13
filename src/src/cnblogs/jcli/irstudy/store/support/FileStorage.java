package cnblogs.jcli.irstudy.store.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import cnblogs.jcli.irstudy.store.Storage;

public class FileStorage implements Storage{

	private RandomAccessFile file ;
	
	public FileStorage(File file){
		try {
			this.file = new RandomAccessFile(file,"rw");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public long getPointer() {
		try {
			return file.getFilePointer();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long length() {
		try {
			return file.length();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int read(byte[] b) {
		try {
			return file.read(b);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void seek(long offset) {
		try {
			file.seek(offset);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void write(byte[] b) {
		try {
			file.write(b);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void close() {
		try {
			file.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

}
