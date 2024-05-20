package 프로원정대.day5.parametricSearch강의._02;

import java.io.*;
import java.util.*;

// parametric search 예시 #2
// 키워드 : 가능성, 경우의수, 최대중 최소, 최소중 최대
// -> 이런걸 찾는 최적해 알고리즘

// 시간복잡도 : 
// test의 시간복잡도 x O(log범주의크기)

public class Main {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	
	static int money = 100;
	static int M = 5; 
	static int[] arr = {10, 30, 20, 40, 50}; 
	
	static boolean test(int mid) {
		// 이 mid 상한선이면 나라가 파산하지 않고 돈을 줄수 있는가?
		int sum = 0;
		for(int i = 0; i < M; i++) {
			// 모든 지차제들 한번씩 보면서
			// 얘가 요구하는 금액 확인
			// 내가 책정한 상한선 이하라면
			if(arr[i] <= mid)
				sum += arr[i]; // 요구한 만큼만 주고
			// 내가 책정한 상한선보다 높은 금액을 요구한다면
			else if(arr[i] > mid)
				// 상한선 만큼만 준다.
				sum += mid; 
			
			// 만약에 i번 지자체까지 돈을 지급했는데
			// 이미 파산남? 
			if(sum > money)
				return false; // 이건 안돼
		}
		return true; // 이 금액이면 가능!
	}
	
	static void ps() {
		// #1. left right 설정
		// 범주 = mid를 어떤의미로 정했는가? 
		// mid = 이 상한선 금액이 정답이다 라고 가정
		int left = 1; // 내가 상한선으로 정할수 있는 가장 작은 금액
		int right = money; // 내가 상한선으로 정할수 있는 가장 큰 금액
		
		while(left <= right) {
			// mid = 내가 이 상한선 금액이면 나라가 파산하지 않고
			// 모든 지자체한테 돈을 줄 수 있따!
			int mid = (left + right) / 2;
			
			// 이게 실제로 가능한가? => 검증
			if(test(mid)) {
				// 검증 성공 -> 오케이, 단 돈을 줄 수 있어.
				// 하지만 우리가 원하는것은 [최대] 상한선을 찾는것.
				// 그러니까 일단 이 mid 금액이 된다는건 알았지만
				// 더 많은 금액을 책정했을때도 될지를 확인해보자 (즉, 돈을 더줘보자!)
				left = mid + 1;
			}
			else {
				// 검증 실패 - > 돈 못죠
				// 나라 파산함.
				// 그러니까 욕심부리지 말고 금액을 좀 줄여보자
				right = mid - 1; 
			}
		}
		System.out.println(right);
	}

	public static void main(String[] args) throws IOException {
		ps(); 
	}
}