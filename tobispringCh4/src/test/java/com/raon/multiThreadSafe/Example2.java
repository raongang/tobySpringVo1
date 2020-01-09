package com.raon.multiThreadSafe;



class TSSPdfTSGenerator{
	
	String temp;
	
	
	private static TSSPdfTSGenerator tSSPdfTSGenerator = new TSSPdfTSGenerator();
	
	private TSSPdfTSGenerator() {}
	
	public static TSSPdfTSGenerator getInstance() {
		
		return tSSPdfTSGenerator;
		
	}
	
	public void generateFile(int temp1) {
		System.out.print(" " + (++temp1));
	}
	
}


public class Example2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TSSPdfTSGenerator tssPdfTSGenerator = TSSPdfTSGenerator.getInstance();
		
		int[] array= {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19};
		
		for(int temp: array) { //람다식표현.
			new Thread(()->{
				tssPdfTSGenerator.generateFile(temp);
			}).start();
		}
	}

}
