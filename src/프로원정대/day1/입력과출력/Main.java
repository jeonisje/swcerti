package 프로원정대.day1.입력과출력;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		int type = Integer.valueOf(st.nextToken());
		
		
		int n;
		switch(type) {
			case 1:
				st = new StringTokenizer(br.readLine());		
				n = Integer.valueOf(st.nextToken());
				
				int sum = 0;
				long mul = 1L;
				st =  new StringTokenizer(br.readLine());
				for(int i=0; i<n; i++) {
					int num = Integer.valueOf(st.nextToken());
					sum += num;
					mul *= num;
				}
				System.out.println(sum + " " + mul);
				break;
			case 2:
				st = new StringTokenizer(br.readLine());		
				n = Integer.valueOf(st.nextToken());
				
				int min = Integer.MAX_VALUE;
				int max = Integer.MIN_VALUE;
				String minWord = "";
				String maxWord = "";
				for(int i=0; i<n; i++) {
					st =  new StringTokenizer(br.readLine());
					String s = st.nextToken() ;
					if(s.length() < min) {
						min = s.length();
						minWord = s;
					}
					
					if(s.length() > max) {
						max = s.length();
						maxWord = s;
					}
				}
				System.out.println(maxWord);
				System.out.println(minWord);
				break;
			case 3:
				st = new StringTokenizer(br.readLine());		
				n = Integer.valueOf(st.nextToken());
				int m = Integer.valueOf(st.nextToken());
				int[] dat = new int[101];
				
				int minNum = Integer.MAX_VALUE;
				for(int i=0; i<n; i++) {
					st = new StringTokenizer(br.readLine());
					for(int j=0; j<m; j++) {
						int num = Integer.valueOf(st.nextToken());
						dat[num]++;
						if(num < minNum) {
							minNum = num;							
						}
					}
				}
				System.out.println(minNum);
				System.out.println(dat[minNum] + "개");
				
				
				break;
			default :
				
				
		}
	}
}
