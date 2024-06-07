package 알고리즘별.segmentTree.Invision;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int[] tree;
	static int count;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());		
		tree = new int[N*4];
		count = 0;
		st = new StringTokenizer(br.readLine());
		for(int i=0; i<N; i++) {
			int num = Integer.parseInt(st.nextToken());
			
			int ret = query(1, N, 1, num+1, N);
			count += ret;			
			update(1, N, 1, num);
		}
		System.out.println(count);
	}

	private static void update(int start, int end, int node, int index) {
		if(index < start || index > end) return;
		if(start == end) {
			tree[node] = 1;
			return;
		}
		int mid = (start + end) / 2;
		update(start, mid, node*2, index);
		update(mid+1, end, node*2+1, index);
		tree[node] = tree[node*2]+ tree[node*2+1];
		
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
