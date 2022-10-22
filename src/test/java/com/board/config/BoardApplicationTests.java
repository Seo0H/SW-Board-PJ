package com.board.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BoardApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void printInfo() {
		System.out.println("스프링 버전 보기 = " + org.springframework.core.SpringVersion.getVersion());
	}
	
}
