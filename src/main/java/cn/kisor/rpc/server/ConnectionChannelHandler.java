package cn.kisor.rpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kisor.rpc.component.request.impl.KisorMessageRequest;
import cn.kisor.rpc.config.ServerConfig;
import cn.kisor.rpc.utils.NetUtils;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description
 */
public class ConnectionChannelHandler extends SimpleChannelInboundHandler<KisorMessageRequest> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionChannelHandler.class);

	/**
	 * 连接数
	 */
	private AtomicInteger connectCount = new AtomicInteger(0);
	private final ServerConfig serverConfig;

	public ConnectionChannelHandler(ServerConfig serverConfig) {
		super();
		this.serverConfig = serverConfig;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, KisorMessageRequest msg) throws Exception {
		/*
		 * 最好其实是实现channelActive方法， 但是AdapterDecoder会重新fireChannelActive，导致重复执行，所以用此事件
		 */
		int now = connectCount.incrementAndGet();
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Connected from {}, now connection is {}", NetUtils.channelToString(ctx.channel().remoteAddress(), ctx.channel().localAddress()), now);
		}
		// 刚建立连接直接计数器加一，不管是长连接
		if (now > serverConfig.getMaxConnection()) {
			LOGGER.error("Maximum connection {} have been reached, cannot create channel any more", serverConfig.getMaxConnection());
			ctx.channel().close();
		}
	}

}
