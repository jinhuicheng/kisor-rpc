package cn.kisor.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.kisor.rpc.coder.RpcDecoder;
import cn.kisor.rpc.coder.RpcEncoder;
import cn.kisor.rpc.component.request.impl.KisorMessageRequest;
import cn.kisor.rpc.component.response.impl.KisorMessageResponse;
import cn.kisor.rpc.utils.thread.KisorFuture;

public class RpcClient extends SimpleChannelInboundHandler<KisorMessageResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RpcClient.class);

	private String host;
	private int port;
	private final KisorFuture<KisorMessageResponse> kisorFuture = new KisorFuture<KisorMessageResponse>();

	public RpcClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, KisorMessageResponse response) throws Exception {
		kisorFuture.success(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("client caught exception", cause);
		ctx.close();
	}

	public KisorFuture<KisorMessageResponse> send(KisorMessageRequest kisorRequest) throws Exception {
		Channel channel = this.buildChannel();
		channel.writeAndFlush(kisorRequest).sync();
		return kisorFuture;
	}

	protected Channel buildChannel() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel channel) throws Exception {
				channel.pipeline().addLast(new RpcEncoder()) // 将 RPC 请求进行编码（为了发送请求）
						.addLast(new RpcDecoder(8 * 1024 * 1024)) // 将 RPC 响应进行解码（为了处理响应）
						.addLast(RpcClient.this); // 使用 RpcClient 发送 RPC 请求
			}
		}).option(ChannelOption.SO_KEEPALIVE, true);

		ChannelFuture future = bootstrap.connect(host, port).sync();
		Channel channel = future.channel();
		return channel;
	}
}