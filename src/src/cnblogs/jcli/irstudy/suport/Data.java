package cnblogs.jcli.irstudy.suport;

import java.io.Serializable;

public class Data implements Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	static int WINDOW_BLOCK_SIZE = 512;//byte
	
	

	/*
	 * if no next block data,it is the default value,0
	 */
	private int nextPosition;
	
	private int[] documentIds = new int[WINDOW_BLOCK_SIZE/4];
	
	
}
