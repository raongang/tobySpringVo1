package com.raon.pattern.strategy2;


/***
 * 
 * @author admin
 *   Strategy Pattern
 *       - 전략객체 (interface)
 *       - 전략객체를 이용하는 컨텍스트
 *       - 전략객체를 생성 컨텍스트에 주입하는 클라이언트
 *       - 
 */

//전략생성 및 context에게 주입
public class Client {
	public static void main(String[] args) {
		Strategy strategy = null;
		
		Solider rambo = new Solider();
		strategy = new StrategtGun();
		
		rambo.runContext(strategy);
		System.out.println("\n");
		
		strategy = new StrategyGrenade();
		
		rambo.runContext(strategy);
		System.out.println("\n");
	}
}
