package cn.kisor.rpc.filter;

import cn.kisor.rpc.component.request.MessageRequest;
import cn.kisor.rpc.component.response.MessageResponse;

public class AuthFilter implements Filter {

	@Override
	public void doFilter(MessageRequest messageRequest, MessageResponse messageResponse, FilterChain taskChain) throws Exception {
		taskChain.doFilter(messageRequest, messageResponse, taskChain);
	}

}
