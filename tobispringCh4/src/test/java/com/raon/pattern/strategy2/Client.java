package com.raon.pattern.strategy2;


/***
 * 
 * @author admin
 * 
 *
 */


//전략생성 및 context에게 주입
public class Client {
	public static void main(String[] args) {
		Strategy strategy = null;
		
		Solider rambo = new Solider();
		strategy = new StrategtGun();
		
		rambo.runContext( strategy);
		System.out.println("\n");
		
		strategy = new StrategyGrenade();
		
		rambo.runContext(strategy);
		System.out.println("\n");
	}
}
