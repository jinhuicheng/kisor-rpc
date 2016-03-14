package cn.kisor.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import cn.kisor.rpc.config.ServerConfig;
import cn.kisor.rpc.exception.InitErrorException;

/**
 *
 * @author 孙浩
 * @email sunhao5@jd.com
 * @description
 */
public class ProviderServer implements Server, InitializingBean {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProviderServer.class);

	private final ServerConfig serverConfig = new ServerConfig();
	private volatile static boolean isStarted = false;
	private final ServiceRegistry serviceRegistry;
	public ProviderServer(ServiceRegistry serviceRegistry) {
		super();
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public boolean start() {
		if (isStarted) {
			return true;
		}
		this.initConfig();// 初始化配置
		Boolean isConnected = connect();// 建立连接 监听端口
		// 注册服务 TODO
		if (StringUtils.isNotBlank(serverConfig.getHost())) {
			serviceRegistry.register(serverConfig.getHost() + ":" + serverConfig.getPort());
			isStarted = isConnected;
		}
		return isConnected;
	}

	private void initConfig() {
		serverConfig.init();
		HandlerMapping.initHandlerMapping();
	}

	private Boolean connect() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(serverConfig.getBossGroup(), serverConfig.getWorkerGroup())//
				.channel(NioServerSocketChannel.class)//
				.childHandler(new ServerChannelInitializer(serverConfig))//
				.option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
		// 绑定ip 端口
		ChannelFuture future = bootstrap.bind(new InetSocketAddress(serverConfig.getHost(), serverConfig.getPort()));
		ChannelFuture channelFuture = future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					LOGGER.info("Server have success bind to {}:{}", serverConfig.getHost(), serverConfig.getPort());
				} else {
					LOGGER.error("Server fail bind to {}:{}", serverConfig.getHost(), serverConfig.getPort());
					stop();
					throw new InitErrorException("Server start fail !", future.cause());
				}
			}
		});
		try {
			channelFuture.await(5000, TimeUnit.MILLISECONDS);
			if (channelFuture.isSuccess()) {
				return Boolean.TRUE;
			}
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public void stop() {
		LOGGER.info("Shutdown the kisor-rpc server transport now...");
		serverConfig.getWorkerGroup().shutdownGracefully();
		serverConfig.getBossGroup().shutdownGracefully();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 启动服务
		this.start();
	}

}
