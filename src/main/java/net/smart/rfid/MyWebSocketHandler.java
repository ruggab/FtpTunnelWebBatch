package net.smart.rfid;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

	public static Set<WebSocketSession> tunnelUsers = Collections.synchronizedSet(new HashSet<WebSocketSession>());

	private static Logger logger = LogManager.getLogger(MyWebSocketHandler.class);

	@Override
	public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
		logger.debug("error occured at sender " + session);

	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		logger.debug("Session closed.");
		tunnelUsers.remove(session);
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.debug("Connected ... " + session.getId());
		tunnelUsers.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
		logger.debug("message received: " + jsonTextMessage.getPayload());

	}
}