package 프로원정대.day5.binarySearch강의;

import java.io.*;
import java.util.*;

public class Main {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	
	static int[] arr = {1, 3, 5, 7, 9, 2, 4, 6, 8, 70}; 
	
	static void bs(int target) {
		// #1. 범위 설정
		int left = 0; // 찾고자 하는 요소의 범위 첫번쨰 (또는 가장 작은값) 
		int right = arr.length -1; // 찾고자 하는 요소의 범위 마짐가 (또는 가장 큰값

		// 얼마나 반복해야 할까?
		// left는 항상 right보다 작은 값이여야 합니다.
		// --> 이 둘의 위치가 이름값을 하는 동안만 돌려줍니다.
		while(left <= right) {
			// #2. mid를 때려볼겁니다.
			// -> ** mid = 이게 정답이다 라고 가정한다!
			int mid = (left + right) / 2; 
			
			System.out.println(">> " + arr[mid]);
			
			// binary search에서는 나올수 있는 경우의 수 3개
			if(target == arr[mid]) {
				// 찾았으면 -> 더 이상 뭐 해볼게 없다
				// 그러면 여기서 그냥 종료
				System.out.println("찾았따!");
				return; 
			}
			else if(target > arr[mid]) {
				// mid가 target보다 작다면
				// mid보다 더 큰 영역을 봐야 한다 -> 그래야 찾을 수 있다.
				left = mid + 1; 
			}
			else if(target < arr[mid]) {
				// mid가 target보다 크다면
				// mid보다 더 작은 영역을 봐야 합니다. 
				right = mid - 1; 
			}
		}
		System.out.println("없다!");
	}

	public static void main(String[] args) throws IOException {
		// #0. 일단 정렬
		Arrays.sort(arr);
//		for(int x : arr)
//			System.out.print(x + " " );
		
		System.out.println(Arrays.binarySearch(arr, 70));
		
		int target = 11; 
		bs(target); 
	}
}