package fastrpc.transport.netty;

import fastrpc.context.RpcBeanContent;
import fastrpc.context.annotation.Weave;
import fastrpc.context.support.ApplicationUtils;
import fastrpc.rpc.ProviderService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Weave
    private ProviderService providerService;

    public ServerHandler() {
        RpcBeanContent beanContent = ApplicationUtils.getBeanContent();
        providerService = beanContent.getBean(ProviderService.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.info("Server accept msg:" + ((ByteBuf) msg).toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

    }
}
