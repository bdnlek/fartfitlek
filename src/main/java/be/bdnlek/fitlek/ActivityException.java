package be.bdnlek.fitlek;

public class ActivityException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -93562730935218073L;

	public ActivityException(Exception e) {
		super(e.getMessage(), e);
	}

	public ActivityException(String msg) {
		super(msg);
	}

}
