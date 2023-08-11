package org;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import io.vertx.core.parsetools.RecordParser;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class TcpServer
{
	public static void main(String[] args) {
		new TcpServer().startServ();
	}

	public void startServ() {
		Vertx vertx = Vertx.vertx();

		// create server and Handler
		NetServer netServer = vertx.createNetServer();
		netServer.connectHandler(new ConnectionHandler());

		// start the server and specify the port and host to listen on
		netServer.listen(8091, "127.0.0.1", res -> {
			if (res.succeeded()) {
				System.out.println("Server started on port 8091");
			} else {
				System.out.println("Failed to start server");
			}
		});
	}

	// NetSocket class: to represent a TCP socket connection in Vertx
	// an event handler
	public static class ConnectionHandler implements Handler<NetSocket> {
		// message
		// header [int header length]
		// body [byte[] data]

		private static final int PACKET_HEADER_LENGTH = 4;

		// is called whenever a new socket connection is established
		@Override
		public void handle(NetSocket socket) {
			// specifies the fixed size of records that the parser should produce
			final RecordParser parser = RecordParser.newFixed(PACKET_HEADER_LENGTH);

			parser.setOutput(new Handler<Buffer>()
			{
				// if = -1, means that the header of the packet has not been read
				int bodyLength = -1;

				@Override
				public void handle(Buffer buffer)
				{
					if (bodyLength == -1) {
						// means that the header of the packet has not yet been read
						// so the handler reads the header from the buffer and sets bodyLength to the value of the header

						// read header
						bodyLength = buffer.getInt(0);
						parser.fixedSizeMode(bodyLength);
					}
					else {
						// if bodyLength != -1
						// means that the body of the packet has been received
						// so the handler reads the body from the buffer and processes it

						// read data
						byte[] bodyBytes = buffer.getBytes();

						log.info("get msg from client:{}", new String(bodyBytes));

						// recover the message
						parser.fixedSizeMode(PACKET_HEADER_LENGTH);
						bodyLength = -1;
					}
				}
			});

			// make the handle() method of ConnectionHandler to be called whenever new data is received on the socket
			socket.handler(parser);
		}
	}
}
