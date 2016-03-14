package cn.kisor.rpc.exception;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @date 2016年1月17日 下午10:49:34
 * @description
 */
public class NoAliveProviderException extends RuntimeException{

	/** */
	private static final long serialVersionUID = -3652567462201542904L;

	public NoAliveProviderException() {
		super();
	}

	public NoAliveProviderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NoAliveProviderException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoAliveProviderException(String message) {
		super(message);
	}

	public NoAliveProviderException(Throwable cause) {
		super(cause);
	}

}
