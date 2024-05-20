package 프로원정대.day5.parametricSearch강의._01;

import java.io.*;
import java.util.*;

// parametric search 예시 #1

public class Main {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	
	static String map = "###########~~~~~~~~~";
	
	static boolean test(int mid) {
		// 얘가 정답 후보가 될수 있는가?
		// -> 얘가 땅이기만 하면 후보 가능
		if(map.charAt(mid) == '#') 
			return true;
		return false; 
	}
	
	static void ps() {
		// #1. 범위 설정
		int left = 0 ; // 맵의 시작 
		int right = map.length() - 1; // 맵의 끝 
		
		// #2. 반복
		while(left <= right) {
			// #3. mid 
			int mid = (left + right) / 2;
			// 이 mid가 우리의 정답이다! 라고 저희는 가정합니다.
			// --> 그렇다면 이 mid가 실제로 정답이 될수 있는 후보인가? 에 대한 검증
			// --> 그리고 이 검증 결과에 따라 우리가 어떤 방향으로 탐색을 할지를 정하는겁니다.
			if(test(mid)) {
				// 여기에 들어왔다면 -> 새로운 정답 후보를 발견했다!
				// ps에서 원하는것은 -> 최적의 정답을 찾기를 원함
				// -> 더 좋은 정답이 있을까?에 대해서 탐색을 해야 합니다.
				// -> 더 멀리 에 땅이 있을까?
				left = mid + 1; 
			}
			else {
				// 검증 실패
				// 여기는 물입니다 -> 더 앞에 땅이 있으니, 앞으로 와야 합니다.
				right = mid - 1;
			}
		}
		// 정답
		// ** 내가 test를 통과 했을때 움직였던 벽이 아닌 벽에 답이 있습니다.
		System.out.println(right);
	}
	
	public static void main(String[] args) throws IOException {
		ps(); 
	}
}