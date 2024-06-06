package 알고리즘별.segmentTree.RangeSum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N, Q;
	
	static int[] arr;
	static long[] tree;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());	
		
		arr = new int[N+1];
		tree = new long[N*4];
		
		st = new StringTokenizer(br.readLine());
		for(int i=1; i<N+1; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		initTree(1, N, 1);	
		
		for(int i=0; i<Q; i++) {
			st = new StringTokenizer(br.readLine());
			int command = Integer.parseInt(st.nextToken());
			int left = Integer.parseInt(st.nextToken());
			int right = Integer.parseInt(st.nextToken());
						
			if(command == 1) update(1, N, 1, left, right);
			else {
				System.out.println(query(1, N, 1, left, right));
			}			
		}
		
	}

	private static long query(int start, int end, int node, int left, int right) {
		if(end < left || right < start) return 0;
		if(left <= start && end <= right) return tree[node];
		
		int mid = (start+ end) / 2;
		long lv = query(start, mid, node*2, left, right);
		long rv = query(mid+1, end, node*2+1, left, right);		
		
		return lv + rv;
	}

	private static void update(int start, int end, int node, int index, int value) {
		if(index < start || index > end) return;
		if(start == end) {
			tree[node] = value;
			arr[index] = value;
			return;
		}
		
		int mid = (start + end) / 2;
		update(start, mid, node*2, index, value);
		update(mid+1, end, node*2+1, index, value);
		tree[node] = tree[node*2] + tree[node*2+1];
	}

	private static void initTree(int start, int end, int node) {
		if(start == end) {
			tree[node] = arr[start];
			return;
		}
		int mid = (start + end) / 2;
		initTree(start, mid, node*2);
		initTree(mid+1, end, node*2+1);
		tree[node] = tree[node*2] + tree[node*2+1];
		
	}

}


/*
 
 
10 9
2 3 -3 8 10 10 2 7 6 2
2 2 6
2 1 10
2 5 8
2 1 2
1 8 0
2 1 5
2 1 3
2 2 7
1 6 0


28
47
29
5
20
2
30

*/


