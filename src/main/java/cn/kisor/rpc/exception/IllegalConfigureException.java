package cn.kisor.rpc.exception;

public class IllegalConfigureException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4635464289228184332L;

	public IllegalConfigureException() {
		super();
	}

	public IllegalConfigureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalConfigureException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalConfigureException(String message) {
		super(message);
	}

	public IllegalConfigureException(Throwable cause) {
		super(cause);
	}

}
