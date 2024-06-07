package 알고리즘별.segmentTree.라이벌의식;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int MAX = 1_000_001;
	static int N;
	static int[] arr;
	static int[] cow;
	static int[] tree;
	static int maxHeight;
	static int minHeight;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());		
		
		N = Integer.parseInt(st.nextToken());
		//arr = new int[MAX];
		tree = new int[MAX*4];
		
		maxHeight = 0;
		minHeight = Integer.MAX_VALUE;
		int count = 0;
		 st = new StringTokenizer(br.readLine());		
		for(int i=0; i<N; i++) {
			int num = Integer.parseInt(st.nextToken());
			
			maxHeight = Math.max(maxHeight, num);
			int ret = query(1, MAX, 1, 1, num-1);
			count += ret;
			update(1, MAX, 1, num, 1);			
		}
		
		System.out.println(count);
	
	}

	private static void update(int start, int end, int node, int index, int value) {
		if(index < start || index > end) return;
		if(start == end) {
			tree[node] += value;
			return;
		}
		int mid = (start + end) / 2;
		update(start, mid, node*2, index, value);
		update(mid+1, end, node*2+1, index, value);
		tree[node] = tree[node*2] + tree[node*2+1];
		
	}

	private static int query(int start, int end, int node, int left, int right) {
		if(end < left || right < start) return 0;
		if(left <= start && end <= right) return tree[node];
	
		int mid = (start + end) / 2;
		int lv = query(start, mid, node*2, left, right);
		int rv = query(mid+1, end, node*2+1, left, right);
		return lv + rv;
	}	
	
}
