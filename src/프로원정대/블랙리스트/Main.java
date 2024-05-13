package 프로원정대.블랙리스트;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		
		StringTokenizer st;		
		st = new StringTokenizer(br.readLine());		
		
		int h = Integer.parseInt(st.nextToken());
		int w = Integer.parseInt(st.nextToken());
		
		int[] dat = new int[100_001];
		
		for(int i=0; i<h; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<w; j++) {
				int num = Integer.parseInt(st.nextToken());
				dat[num]++; 
			}
		}
		
		st = new StringTokenizer(br.readLine());
		int bh = Integer.parseInt(st.nextToken());
		int bw = Integer.parseInt(st.nextToken());
		
		int blackCount = 0;
		int[] used = new int[100_001];
		for(int i=0; i<bh; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<bw; j++) {
				int num = Integer.parseInt(st.nextToken());
				if(used[num] == 1) continue;
				blackCount += dat[num];
				used[num] = 1;
			}
		}
		
		int nonBlack = h * w - blackCount;
		
		System.out.println(blackCount);
		System.out.println(nonBlack);
				
	}
}

/*

10 10
1 2 3 4 5 6 7 8 9 10
1 2 3 4 5 6 7 8 9 10
1001 1001 1001 1001 1001 1001 1001 1001 1001 1001
1001 10022 1003 1004 1005 1006 1007 1008 1009 10010
11 12 12 13 14 15 16 17 18 19
11 12 12 13 14 15 16 17 18 19
20 21 22 23 24 25 26 27 28 29
31 32 33 34 35 36 37 38 39 40
31 32 33 34 35 36 37 38 39 40
101 102 103 104 105 106 107 108 109 109
3 4
1001 1001 11 109
1 2 3 4
5 6 7 8
 





*/
