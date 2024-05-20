package 프로원정대.day5.츄러스;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int[] arr;
	static int N, K;
	static int Answer;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		Answer = 0;
		
		arr = new int[N];
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		Answer = bs();
		System.out.println(Answer);
	}
	
	static int bs() {
		int start = 0;
		int end = 100_000_000;		
		
		while(start <= end) {
			int mid = (start + end) / 2;
			
			int result = test(mid);
			
			if(result == 1) {	
				start = mid + 1;
			} 
			else {
				end = mid - 1;
			}
		}
		
		
		return end;
	}
	
	static int test(int length) {
		int count = 0;
		for(int i=0; i<N; i++) {
			int num = arr[i] / length;
			count += num;
			
			if(count > K) return 1;
		}
		if(count == K) return 1;
		return -1;
	}
	
}
