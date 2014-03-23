package cnblogs.jcli.irstudy.utils;

import cnblogs.jcli.irstudy.store.DataItem;
import junit.framework.TestCase;
import org.junit.Test;


public class NumberUtilsTest extends TestCase{

    @Test
	public void testByteArrayToInt(){
		byte[] b = new byte[]{0,0,0,-84};
		assertEquals(NumberUtils.byteArrayToInt(b),172);
 	}
	
	public void testStringCompare(){
		DataItem d1 = new DataItem();
		d1.setKey("a");
		DataItem d2 = new DataItem();
		d2.setKey("b");
		assertTrue(d1.compareTo(d2) < 0);
	}
	
	public void testIntToByteArray(){
		byte[] b = NumberUtils.intToByteArray(172);
		for(byte bb : b){
			System.out.println(bb);
		}
	}
	
	public static void main(String[] args){
		System.out.println(Integer.toBinaryString(172));
	}

}
