package springbook.callbackpatternTest;

public class SoliderWithRefactoring {

	void runContext(String strategySound) {
		System.out.println("Start");
		execute(strategySound).runStrategy();
		System.out.println("finish");
		
	}
	
	private Strategy execute(final String strategySound) {
		return new Strategy() {
			@Override
			public void runStrategy() {
				// TODO Auto-generated method stub
				System.out.println(strategySound);
			}
			
		};
	}
}
