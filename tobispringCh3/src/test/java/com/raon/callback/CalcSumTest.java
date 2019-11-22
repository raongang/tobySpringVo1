package com.raon.callback;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CalcSumTest {

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
		
		System.out.println();
		
		Calculator cal = new Calculator();
		
		int sum = cal.calcSum(getClass().getResource("/numbers.txt").getPath());
		assertThat(sum,is(10));


	}
}
