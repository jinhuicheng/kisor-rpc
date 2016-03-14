package cn.kisor.rpc.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import cn.kisor.rpc.coder.RpcDecoder;
import cn.kisor.rpc.coder.RpcEncoder;
import cn.kisor.rpc.config.ServerConfig;

@ChannelHandler.Sharable
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final ServerConfig serverConfig;

	public ServerChannelInitializer(ServerConfig serverConfig) {
		super();
		this.serverConfig = serverConfig;
	}

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		channel.pipeline().addLast(new ConnectionChannelHandler(serverConfig))//
				.addLast(new RpcDecoder(8 * 1024 * 1024)) // 将 RPC 请求进行解码（为了处理请求）
				.addLast(new RpcEncoder()) // 将 RPC 响应进行编码（为了返回响应）
				.addLast(new ServerChannelHandler()); // 处理 RPC 请求
	}

}
