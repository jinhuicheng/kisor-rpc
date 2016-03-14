package cn.kisor.rpc.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kisor.rpc.component.MsgType;
import cn.kisor.rpc.component.header.impl.KisorMessageHeader;
import cn.kisor.rpc.component.request.MessageRequest;
import cn.kisor.rpc.component.response.MessageResponse;
import cn.kisor.rpc.component.response.impl.KisorMessageResponse;
import cn.kisor.rpc.component.response.impl.KisorMessageResponseBody;
import cn.kisor.rpc.constant.Constant;
import cn.kisor.rpc.filter.AuthFilter;
import cn.kisor.rpc.filter.Filter;
import cn.kisor.rpc.filter.FilterChain;
import cn.kisor.rpc.filter.RequestInvokeFilter;
import cn.kisor.rpc.server.excutor.TaskExcutor;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @date 2016年2月17日
 * @description 服务端请求处理
 */
public class ServerChannelHandler extends SimpleChannelInboundHandler<MessageRequest> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerChannelHandler.class);
	private final Filter requestInvokeFilter = new RequestInvokeFilter();
	private final Filter authFilter = new AuthFilter();
	@Override
	public void channelRead0(final ChannelHandlerContext ctx, MessageRequest messageRequest) throws Exception {
		KisorMessageResponseBody responseBody = new KisorMessageResponseBody();
		KisorMessageHeader header = new KisorMessageHeader();
		header.setMsgType(MsgType.RESPONSE);
		header.setRequestId(111111111l);
		final MessageResponse messageResponse = new KisorMessageResponse(header, responseBody);
		try {
			final FilterChain taskChain = new FilterChain();// 创建新的任务链
			taskChain.addTask(authFilter)// 权限校验过滤器
					.addTask(requestInvokeFilter);// 请求处理过滤器
			TaskExcutor.excute(messageRequest, messageResponse, taskChain, Constant.RPC_TIMEOUT, TimeUnit.MILLISECONDS);
		} catch (Throwable t) {
			responseBody.setError(t);
		}
		ctx.writeAndFlush(messageResponse).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		LOGGER.error("server caught exception", cause);
		ctx.close();
	}
}