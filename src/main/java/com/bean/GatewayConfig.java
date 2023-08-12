package com.bean;


import com.thirdparty.checksum.ICheckSum;
import com.thirdparty.codec.IBodyCodec;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

@Getter
@Log4j2
public class GatewayConfig {

    // gateway ID
    private short id;

    // port
    private int recvPort;

    // TODO database Connection

    @Setter
    private IBodyCodec bodyCodec;

    @Setter
    private ICheckSum checkSum;

    private Vertx vertx = Vertx.vertx();

    public void initConfig(String fileName) throws Exception {
        // parser to read xml
        SAXReader reader = new SAXReader();

        Document document = reader.read(new File(fileName));
        Element root = document.getRootElement();

        id = Short.parseShort(root.element("id").getText());
        recvPort = Integer.parseInt(root.element("recvport").getText());
        log.info("Gateway ID: {}, Port: {}", id, recvPort);
    }

    public void startup() throws Exception{
        // 1. Start TCP listening
        initRecv();

        // 2. Connect to RAFT queue machine
    }

    public void initRecv() {
        NetServer netServer = vertx.createNetServer();
        netServer.connectHandler(new TcpServer.ConnectionHandler());

        // start the server and specify the port and host to listen on
        netServer.listen(8091, "127.0.0.1", res -> {
            if (res.succeeded()) {
                System.out.println("Server started on port 8091");
            } else {
                System.out.println("Failed to start server");
            }
        });
    }
}
