package com.example.demo.messaging;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessagingController {
	
	@MessageMapping("/hello")
	@SendTo("/topic/messaging")
	public MessageContent helloGreeting(MessageContent content) throws InterruptedException {
		Thread.sleep(1000);
		return new MessageContent("echo:" + content.getContent());
	}

}
