package com.raon.callback;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
	
	//calcSum >> client
	//template과callback을 적용한 calSum() 메소드
	public Integer calcSum(String filepath) throws IOException{
		
		LineCallback<Integer> sumCallback= new LineCallback<Integer>() {
			@Override
			public Integer doSomethingWithLine(String line,Integer value) {
				// TODO Auto-generated method stub
				return value+Integer.valueOf(line);
			}};
		return lineReadTemplate(filepath,sumCallback,0);
		
	}//end method
	
	//calcMultiply >> client 
	//template과callback을 적용한 calcMultiply() 메소드
	public Integer calcMultiply(String filepath) throws IOException{
		
		LineCallback<Integer> multiplyCallback= new LineCallback<Integer>() {
			@Override
			public Integer doSomethingWithLine(String line,Integer value){
				// TODO Auto-generated method stub
				return value*Integer.valueOf(line);
			}};
		return lineReadTemplate(filepath,multiplyCallback,1);
		
	}//end method	
	
	//문자열 연결기능
	public String concatenate(String filepath) throws IOException{
		LineCallback<String> concatenateCallback = new LineCallback<String>() {
			
			@Override
			public String doSomethingWithLine(String line, String value) {
				// TODO Auto-generated method stub
				return value+line;
			}};
			
			return lineReadTemplate(filepath, concatenateCallback, "");
	}
	
	
	//template 
	public <T> T lineReadTemplate(String filepath, LineCallback<T> callback , T initVal) throws IOException{
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(filepath));
			T res = initVal;
			String line=null;
			
			while((line=br.readLine())!=null) {
				//callback 호출
				res = callback.doSomethingWithLine(line, res);
			}
			return res;
			
		}catch(IOException e){
			System.out.println(e.getMessage());
			throw e;
		}finally {
			if(br!=null) { try { br.close(); } catch(IOException e) { System.out.println(e.getMessage()); }}
		}
		
	}
	
	
	
}//end class 
