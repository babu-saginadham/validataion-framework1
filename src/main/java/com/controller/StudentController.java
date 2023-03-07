package com.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class StudentController {

	// student+test
	@RequestMapping(value="/test", method = RequestMethod.GET)	
	public String test() {
		return "Babu";
	}
}
