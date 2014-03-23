package cnblogs.jcli.irstudy.utils;

/**
 * reference the spring assert framework
 * 
 * @author tony.li.fly@gmail.com
 */
public abstract class Assert {

	public static void notNull(Object obj){
		if(obj == null){
			throw new IllegalArgumentException("the argument is required,it must no be null!");
		}
	}
	
}
