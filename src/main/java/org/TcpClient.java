package org;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;
import lombok.extern.log4j.Log4j2;


// Reconnect actively
// based on specific format to send TCP data
@Log4j2
public class TcpClient
{

	public static void main(String[] args)
	{
		new TcpClient().startConn();
	}

	private Vertx vertx;

	public void startConn() {
		vertx = Vertx.vertx();

		vertx.createNetClient().connect(8091, "127.0.0.1", new ClientConnHandler());
	}

	public class ClientConnHandler implements Handler<AsyncResult<NetSocket>>
	{
		@Override
		public void handle(AsyncResult<NetSocket> result)
		{
			if (result.succeeded()) {
				// send request
				// set up eveny handler for the socket
				NetSocket socket = result.result();

				socket.closeHandler(c -> {
					log.info("Client disconnected from {}", socket.remoteAddress());
					reconnect();
				});

				// excception handler
				socket.exceptionHandler(e -> {
					log.info("Socket exception", e);
				});

				// send request
				byte[] req = "HELLO IM CLIENT".getBytes();

				int bodyLength = req.length;

				Buffer buffer = Buffer.buffer().appendInt(bodyLength)
						.appendBytes(req);

				socket.write(buffer);
			}
			else
			{
				// reconnect (should be done by client side)
				log.error("Connection to server: 127.0.0.1:8091 fail");
				reconnect();
			}
		}
	}

	private void reconnect() {
		vertx.setTimer(1000*5, res -> {
			log.info("try reconnecting to server");
			vertx.createNetClient().connect(8091, "127.0.0.1", new ClientConnHandler());
		});
	}
}
