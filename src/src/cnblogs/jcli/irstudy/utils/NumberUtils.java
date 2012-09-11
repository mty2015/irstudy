package cnblogs.jcli.irstudy.utils;

public class NumberUtils {

	public static byte[] intToByteArray(int value) {
		return new byte[] {
				(byte) (value >>> 24), (byte) (value >>> 16),
				(byte) (value >>> 8), (byte) value };
	}

}
