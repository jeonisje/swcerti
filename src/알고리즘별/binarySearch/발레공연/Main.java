package 알고리즘별.binarySearch.발레공연;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
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
		
		arr = new int[N];
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		System.out.println(ps());
		
	}
	
	static int ps() {
		int start = 0;
		int end = N;
		while(start <= end) {
			int mid = (start + end) / 2;
			if(test(mid)) {						
				end = mid -1;
			} else {
				start = mid + 1;
			}
		}
		
		return start;
	}
	
	static boolean test(int mid) {
		PriorityQueue<Integer> q = new PriorityQueue<>();
		
		for(int i=0; i<mid; i++) {
			q.add(arr[i]);
		}
		int idx = mid;
	
		while(!q.isEmpty()) {
			int n = q.poll();						
			
			if(n > T) return false;
			
			if(idx >= N) continue; 
			q.add(arr[idx] + n);
			idx++;			
		}		
		
		return true;
	}	
}


/*
20 222
36
87
93
50
22
63
28
91
60
64
27
41
27
73
37
12
69
68
30
83


=> 6

*/
