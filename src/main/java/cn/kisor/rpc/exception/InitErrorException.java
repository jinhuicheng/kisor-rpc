package cn.kisor.rpc.exception;

public class InitErrorException extends Exception {

	private static final long serialVersionUID = -7496561581589936598L;

	public InitErrorException() {
		super();
	}

	public InitErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InitErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public InitErrorException(String message) {
		super(message);
	}

	public InitErrorException(Throwable cause) {
		super(cause);
	}

}
