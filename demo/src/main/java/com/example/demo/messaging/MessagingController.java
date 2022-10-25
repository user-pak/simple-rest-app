package com.example.demo.messaging;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class MessagingController {
	
	@MessageMapping("/hello")
	@SendTo("/topic/messaging")
	public MessageContent helloGreeting(MessageContent content, MessageHeaders messageHeaders) throws InterruptedException {
		Thread.sleep(1000);
		StompHeaderAccessor stompHeaderAccessor = MessageHeaderAccessor.getAccessor(messageHeaders, StompHeaderAccessor.class);
		String echo = stompHeaderAccessor.getUser().getName();
		return new MessageContent(echo + " : " + content.getContent());
	}

}
