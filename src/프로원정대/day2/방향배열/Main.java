package 프로원정대.day2.방향배열;

import java.io.*;
import java.util.*; 

public class Main {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st; 
	
	public static void main(String[] args) throws IOException {
		int[][] arr = {
				{3, 5, 7},
				{9, 1, 3},
				{4, 8, 6} 
		};
		
		int y = 1;
		int x = 1; 
		
		int[] directY = {-1, 1, 0, 0};
		int[] directX = {0, 0, -1, 1};
		
		int sum = 0;
		for(int i=0; i<4; i++) {
			int nextY = y + directY[i];
			int nextX = x + directX[i];
			if(nextY < 0 || nextX < 0 || nextY >= 3 || nextX >=3) continue;
					
			sum += arr[nextY][nextX];
		}
		
		System.out.println(sum);
		// (y,x)를 기반으로 상하좌우의 합을 출력하는 프로그램
		
	}
}