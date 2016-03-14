package cn.kisor.rpc.filter;

import java.util.LinkedList;
import java.util.List;

import cn.kisor.rpc.component.request.MessageRequest;
import cn.kisor.rpc.component.response.MessageResponse;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description 过滤器链
 */
public class FilterChain implements Filter {
	private int index = 0;
	private final List<Filter> filters = new LinkedList<Filter>();

	public FilterChain addTask(Filter filter) {
		filters.add(filter);
		return this;
	}

	@Override
	public void doFilter(MessageRequest messageRequest, MessageResponse messageResponse, FilterChain taskChain) throws Exception {
		if (index == filters.size()) {// 表示执行最后一个filter之后又调用doFilter
			return;
		}
		Filter filter = filters.get(index++);
		filter.doFilter(messageRequest, messageResponse, taskChain);
	}

	public FilterChain addTasks(List<Filter> filters) {
		this.filters.addAll(filters);
		return this;
	}
}
