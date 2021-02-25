package com.learn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.learn.controller.SimpleController;

@SpringBootTest
public class SimpleSpringBootTest {
	
	@Autowired
	SimpleController controller;
	
	@Test
	void simpleTest() {
		String expected = "Simple Spring Boot Demo...";
		String result = controller.getMessage();
		assertEquals(expected, result, "The output message is not equals to " + expected);
	}
}
