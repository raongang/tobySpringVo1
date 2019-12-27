package com.raon.pattern.strategy2;

//context 
public class Solider {
	void runContext(Strategy strategy) {
		System.out.println("Start!!");
		strategy.runStrategy();
		System.out.println("Finish");
	}
}
