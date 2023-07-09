package com.dillip.api;

import javax.annotation.Resource;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StaticServiceApplicationTests {
	
//	@Resource(name = "jasyptStringEncryptor")
//	StringEncryptor encryptor;
	
//	@Value("${hello.dillip}")
//	private String hello;

	@Test
	void contextLoads() {
	}
	
//	@Test
//	void generateEncryptedKey() {
//		String normalKey = "thedillip1@gmail.com";
//		String encrypt = encryptor.encrypt(normalKey);
//		String decrypt = encryptor.decrypt(encrypt);
//		System.out.println("Encrypted ########################");
//		System.out.println(encrypt);
//		System.out.println("decrypt ########################");
//		System.out.println(decrypt);
//		
//	}
	
//	@Test
//	void getMsg() {
//		System.out.println("########## "+hello);
//	}
}
