package springbook.callbackpatternTest;

public class Client {
	public static void main(String[] args) {
		SoliderWithRefactoring rambo = new SoliderWithRefactoring();
		rambo.runContext("test1");
		System.out.println("\n");
		rambo.runContext("test2");
	}

}
