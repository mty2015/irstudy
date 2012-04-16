package cnblogs.jcli.irstudy.suport;

import java.io.IOException;
import java.io.OutputStream;

import cnblogs.jcli.irstudy.Document;
import cnblogs.jcli.irstudy.IndexWriter;

public class DefaultIndexWriter implements IndexWriter{

	private OutputStream destIndexStore;
	
	public DefaultIndexWriter(OutputStream destIndexStore){
		this.destIndexStore = destIndexStore;
	}
	
	@Override
	public void write(Document entry) {
		
		
	}
	
	public void close() throws IOException {
		destIndexStore.close();
	}

}
