package cn.kisor.rpc.component.response;

public interface MessageResponseBody {

	Object getResult();

	Throwable getError();

	Boolean isError();

}
