package cn.kisor.rpc.component.response.impl;

import cn.kisor.rpc.component.response.MessageResponseBody;

public class KisorMessageResponseBody implements MessageResponseBody {
	private Object result;
	private Throwable error;

	@Override
	public Throwable getError() {
		return error;
	}

	@Override
	public Boolean isError() {
		return error != null;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	@Override
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}