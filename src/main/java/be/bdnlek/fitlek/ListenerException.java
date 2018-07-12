package be.bdnlek.fitlek;

public class ListenerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3815952352181636357L;

	public ListenerException(String msg) {
		super(msg);
	}

	public ListenerException(String msg, Exception e) {
		super(msg, e);
	}

}
