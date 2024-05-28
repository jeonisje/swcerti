package 프로원정대.day9.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M;
	static int[] arr;
	static int[] tree;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		
		arr = new int[N+1];
		tree = new int[N*4];
		st = new StringTokenizer(br.readLine());
		for(int i=1; i<N+1; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}
		
		initTree(1, N, 1);
		
		st = new StringTokenizer(br.readLine());		
		M = Integer.parseInt(st.nextToken());
		
		for(int i=0; i<M; i++) {
			st = new StringTokenizer(br.readLine());		
			int q = Integer.parseInt(st.nextToken());
			int num1 = Integer.parseInt(st.nextToken());
			int num2 = Integer.parseInt(st.nextToken());
			
			if(q == 1) {
				updateTree(1, N, 1, num1, num2);
			} else {
				System.out.println(query(1, N, 1, num1, num2));
			}
		}
	}

	private static void initTree(int start, int end, int node) {
		if(start == end) {
			tree[node] = arr[start];
			return;
		}
		
		int mid = (start + end) / 2;
		initTree(start, mid, node * 2);
		initTree(mid + 1, end, node * 2 + 1);
		tree[node] = Math.min(tree[node*2], tree[node * 2 + 1]);		
	}
	
	static int query(int start, int end, int node, int left, int right) {
		if(end < left || right < start) return Integer.MAX_VALUE;
		if(left <= start && end <= right) return tree[node];
		int mid = (start + end) / 2;
		int lv = query(start, mid, node*2, left, right);
		int rv = query(mid+1, end, node*2+1, left, right);
		return Math.min(lv, rv);
	}
	
	static void updateTree(int start, int end, int node, int index, int value) {
		if(index < start || index > end) return;
		if(start == end) {
			tree[node] = value;
			arr[index] = value;
			return;
		}
		
		int mid = (start + end) / 2;
		updateTree(start, mid, node*2, index, value);
		updateTree(mid+1, end, node*2+1, index, value);
		tree[node] = Math.min(tree[node*2], tree[node*2+1]);
	}
}
