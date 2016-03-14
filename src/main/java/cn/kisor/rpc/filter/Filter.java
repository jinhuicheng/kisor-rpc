package cn.kisor.rpc.filter;

import cn.kisor.rpc.component.request.MessageRequest;
import cn.kisor.rpc.component.response.MessageResponse;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description 过滤器
 * @param <T>
 */
public interface Filter {
	public abstract void doFilter(MessageRequest messageRequest, MessageResponse messageResponse, FilterChain taskChain) throws Exception;
}
