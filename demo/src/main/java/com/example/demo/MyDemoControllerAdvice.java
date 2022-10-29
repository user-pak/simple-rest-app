package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyDemoControllerAdvice {
	
	@ExceptionHandler(DemoControllerException.class)
	public ResponseEntity<?> handleDemoControllerException(DemoControllerException e) {
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDeniedException(Exception e) {
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다");
	}
}
