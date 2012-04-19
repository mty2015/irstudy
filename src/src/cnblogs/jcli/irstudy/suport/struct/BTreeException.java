package cnblogs.jcli.irstudy.suport.struct;

public class BTreeException extends RuntimeException{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8580573626368817800L;
	
	public BTreeException(String message){
		super(message);
	}
	
	public BTreeException(Throwable ex){
		super(ex);
	}
	
	public BTreeException(String message,Throwable ex){
		super(message,ex);
	}

}
