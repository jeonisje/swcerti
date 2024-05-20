package 알고리즘별.머리맞대기;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N, T;
	static int[] arr;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		T = Integer.parseInt(st.nextToken());
		
		arr = new int[N * 2];
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<N; i++) {
			int ii = i + N;
			int n = Integer.parseInt(st.nextToken());
			arr[i] = n;
			arr[ii] = n;
		}
		
		
		System.out.println(ps());
		
		
		return;
		
	}
	
	static int ps() {		
		int min = Integer.MAX_VALUE;
		
		for(int i=0; i<=N/2; i++) {
			int start = 0;
			int end = 10_000_000;
			
			while(start<=end) {
				int mid = (start + end) / 2;
				int ret = test(mid, i);
				//System.out.println("mid => " + mid + " , result => " + ret);
				if(ret <= T) {		
					end = mid - 1;					
				} else {
					start = mid + 1;
				}
			}
			
			min = Math.min(min, start);
		}
		
		return min;
	}
	
	static int test(int target, int start) {
		int cnt = 0;
		int sum = 0;
		for(int i=start; i<N+start; i++) {
			if(arr[i] > target) return T + 1;
			sum += arr[i];
			if(sum > target) {
				cnt++;
				sum = 0;
				i--;
			}
			
			if(i == N+start-1) {
				cnt++;
			}
			
			if(cnt > T) return cnt;
		}
		
		return cnt;
	}
}
