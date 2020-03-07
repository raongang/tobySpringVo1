package springbook.learningtest.jdk;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class ReflectionTest {

	@Test
	public void invokeMethod() throws Exception{
		
		String name = "String";
		
		//length()
		assertThat(name.length(),is(6));
		
		//class정보에서 특정 이름을 가진 메소드 정보를 가져온다(여기서는 length)
		Method lengthMethod = String.class.getMethod("length");
		
		//특정오브젝트의 메소드를 실행
		assertThat((Integer)lengthMethod.invoke(name),is(6));
		
	
		//charAt()
		assertThat(name.charAt(0),is('S'));
		
		
		/*
		 *    public Object invoke(Object obj, Object...args)
		 *     - obj : 대상오브젝트
		 *     - args : 파라미터 목록
		*/
		Method charAtMethod = name.getClass().getMethod("charAt", int.class);
		assertThat((Character)charAtMethod.invoke(name,0), is('S'));
		 
	}
	
}
