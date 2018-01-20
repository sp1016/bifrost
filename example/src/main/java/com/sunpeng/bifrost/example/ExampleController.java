package com.sunpeng.bifrost.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunpeng.bifrost.limiter.Limiter;

/**
 * 
 * @author sunpeng
 *
 */
@RestController
public class ExampleController {

	@RequestMapping("/getperson")
	@Limiter(code = "XXXXXX", message = "limiter test")
	public Person home() throws InterruptedException {
		Thread.sleep(100);
		Person person = new Person();
		person.setCode("000000");
		person.setMessage("OK");
		person.setName("test");
		return person;
	}
}
