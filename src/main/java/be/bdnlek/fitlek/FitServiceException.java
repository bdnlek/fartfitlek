package be.bdnlek.fitlek;

public class FitServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -93562730935218073L;

	public FitServiceException(Exception e) {
		super(e.getMessage(), e);
	}

	public FitServiceException(String msg) {
		super(msg);
	}

}
