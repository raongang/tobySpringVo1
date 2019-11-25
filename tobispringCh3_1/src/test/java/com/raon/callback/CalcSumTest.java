package com.raon.callback;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CalcSumTest {

	Calculator calculator;
	String numFilePath;
	

	@Before 
	public void setUp() {
		this.calculator = new Calculator();
		this.numFilePath = getClass().getResource("/numbers.txt").getPath();
	}
	
	
	
	@Test
	public void sumOfNumbers() throws IOException{
		//ClassPathResource resource = new ClassPathResource("numbers.txt");
		/*
		System.out.println(resource.getFile()); // 파일 객체
		System.out.println(resource.getFilename()); // 파일 이름
		System.out.println(resource.getInputStream()); // InputStream 객체
		System.out.println(resource.getPath()); // 파일 경로
		System.out.println(resource.getURL()); // URL 객체
		System.out.println(resource.getURI()); // URI 객체
		*/		
		assertThat(calculator.calcSum(this.numFilePath),is(10));
	}
	
	
	@Test
	public void multiplyOfNumbers() throws IOException{
		assertThat(calculator.calcMultiply(this.numFilePath),is(24));
	}
	
	@Test
	public void concatenateStrings() throws IOException{
		assertThat(calculator.concatenate(this.numFilePath),is("1234"));
	}
}
