package springbook.user.etc;

import java.util.Arrays;
import java.util.List;

public class ArrayListTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/** 
		 * Arrays.asList - 일반 배열을 ArrayList로 변환한다. 
		 * java.util.Arrays.ArrayList 클래스는 set(), get(), contains() 메서드를 가지고 있지만
			원소를 추가하는 메서드는 가지고 있지 않기 때문에 사이즈를 바꿀 수 없다.
		 */
		
		String[] strs = { "alpha","beta","charlie" };
		System.out.println(Arrays.toString(strs));
		
		List<String> lst = Arrays.asList(strs);
		System.out.println(lst);
		
		// lst.add("ttt"); 추가불가 에러
		
	     // Changes in array or list write thru
	      strs[0] += "88";
	      lst.set(2, lst.get(2) + "99"); // 2번째 인덱스 원소에 charlie99 넣음
	      System.out.println(Arrays.toString(strs)); // [alpha88, beta, charlie99]
	      System.out.println(lst);  // [alpha88, beta, charlie99]
	      
	      // Initialize a list using an array
	      List<Integer> lstInt = Arrays.asList(22, 44, 11, 33);
	      System.out.println(lstInt);  // [22, 44, 11, 33]
	}

}
