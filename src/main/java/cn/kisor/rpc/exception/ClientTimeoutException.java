package cn.kisor.rpc.exception;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @date 2016年1月17日 下午10:37:55
 * @description
 */
public class ClientTimeoutException extends RuntimeException {

	private static final long serialVersionUID = 2850162468099429503L;

	public ClientTimeoutException() {
		super();
	}

	public ClientTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ClientTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientTimeoutException(String message) {
		super(message);
	}

	public ClientTimeoutException(Throwable cause) {
		super(cause);
	}

}
