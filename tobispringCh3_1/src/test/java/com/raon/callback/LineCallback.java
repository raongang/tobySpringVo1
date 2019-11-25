package com.raon.callback;

/**
 *  template/callback 의 고려사항 
 *   1. 템플릿에 반복되는 작업내용 체크.
 *   2. 템플릿이 콜백에게 전달할 내부 정보 체크 / 콜백이 템플릿에게 돌려줄 내용 체크.
 *   3. 템플릿이 클라이언트에게 줄 내용 체크.
 *   4. 콜백의 인터페이스를 정의
 * @author admin
 *
 */
public interface LineCallback<T> {
	T doSomethingWithLine(String line, T value);
	
	
	
}
