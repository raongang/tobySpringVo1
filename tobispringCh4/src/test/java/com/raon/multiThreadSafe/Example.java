package com.raon.multiThreadSafe;

class SingleTon{
	
	
	private static SingleTon singleton = new SingleTon();
	
	private SingleTon() {}
	
	public static SingleTon getInstance() {
		
		return singleton;
		
	}
	
	
	
	public int add(int num) {
		return ++num;
	}
	
}

public class Example {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SingleTon singleton = SingleTon.getInstance();
		int[] array= {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19};
		
		for(int i: array) {
			new Thread(()->{
				System.out.print(" " +singleton.add(i));
			}).start();
		}
	}

}
