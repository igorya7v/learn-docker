package com.learn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

	@GetMapping("/message")
	public String getMessage() {
		return "Simple Spring Boot Demo...";
	}
}
