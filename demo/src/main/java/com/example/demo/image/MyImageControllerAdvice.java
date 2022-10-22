package com.example.demo.image;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.image.storageService.StorageServiceException;

@RestControllerAdvice
public class MyImageControllerAdvice {

	@ExceptionHandler(StorageServiceException.class)
	public ResponseEntity<?> handleException(StorageServiceException e) {
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
}
