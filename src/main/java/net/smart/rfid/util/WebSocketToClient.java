package net.smart.rfid.util;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.smart.rfid.MyWebSocketHandler;

public class WebSocketToClient {
	private static Logger logger = LogManager.getLogger(WebSocketToClient.class);

	
	public static void sendMessageOnPackageReadEvent(String jsonMessage) {
		try {
			Iterator<WebSocketSession> iterator = MyWebSocketHandler.tunnelUsers.iterator();
			while (iterator.hasNext()) {
				WebSocketSession wss = iterator.next();
				logger.info(wss.getUri().getPath());
				if (wss.getUri().getPath().contains("shipping")) {
					synchronized (wss) {
						logger.info(jsonMessage);
						wss.sendMessage(new TextMessage(jsonMessage));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.toString() + " - " + e.getMessage());
		}
	}
	
	public static void sendMessageOnFileEvent(String jsonMessage) {
		try {
			Iterator<WebSocketSession> iterator = MyWebSocketHandler.tunnelUsers.iterator();
			while (iterator.hasNext()) {
				WebSocketSession wss = iterator.next();
				logger.info(wss.getUri().getPath());
				if (wss.getUri().getPath().contains("shipFile")) {
					synchronized (wss) {
						logger.info(jsonMessage);
						wss.sendMessage(new TextMessage(jsonMessage));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.toString() + " - " + e.getMessage());
		}
	}

	

}
