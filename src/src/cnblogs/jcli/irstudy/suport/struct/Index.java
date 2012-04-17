package cnblogs.jcli.irstudy.suport.struct;

import java.io.Serializable;
import java.util.BitSet;

public class Index implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * term key
	 */
	private String key;
	
	/*
	 * the point ref the position of data area
	 */
	private int dataPoint;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getDataPoint() {
		return dataPoint;
	}

	public void setDataPoint(int dataPoint) {
		this.dataPoint = dataPoint;
	}
	
}
