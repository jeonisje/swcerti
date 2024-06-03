package 강사자료.분할정복.사진전송;

import java.util.*;
import java.io.*;

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	static int[][] MAP; 
	static int n;
	
	static void dc(int x, int y, int n)
	{
	    int compressed = 0;
	    int cur = MAP[x][y]; 
	    for(int i = x; i < x + n; i++)
	        for(int j = y; j < y + n; j++)
	        	// 다른 픽셀이 있다! 
	            if(MAP[i][j] != cur) 
	            	compressed = 1; 
	            
	    // 다시 압축을 해야 한다면
	    if(compressed == 1) {
	        // pre - 들어가면서 출력
	    	int part = n / 2;
	        System.out.print("(");
	        dc(x, y, part); 
	        dc(x, y + part, part);
	        dc(x + part, y, part);
	        dc(x + part, y + part, part);
	        // post - 나오면서 출력
	        System.out.print(")");
	    }
	    else 
	    	System.out.print(cur);
	}

	public static void main(String[] args) throws IOException {
		n = Integer.parseInt(br.readLine());
		
		// 초기화
		MAP = new int[n][n]; 
		
		for(int i = 0; i < n; i++) {
			String inp = br.readLine();
			for(int j = 0; j < n; j++) {
				MAP[i][j] = inp.charAt(j) -'0'; 
			}
		}
		dc(0, 0, n); 
	}
}