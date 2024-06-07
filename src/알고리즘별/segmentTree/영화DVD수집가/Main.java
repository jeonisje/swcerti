package 알고리즘별.segmentTree.영화DVD수집가;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N, M;
	static int[] tree;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		int T = Integer.parseInt(st.nextToken());
		
		for(int t=0; t<T; t++) {
			st = new StringTokenizer(br.readLine());		
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			int idx = M;
			int depth = N+M+1;			
			tree = new int[depth * 4];
		
			initTree(1, depth, 1);
			st = new StringTokenizer(br.readLine());		
			for(int i=0; i<M; i++) {
				int num = Integer.parseInt(st.nextToken());
				
				System.out.print(query(1, depth, 1, 1, num+M) + " ");
				update(1, depth, 1, M+num, 0);
				update(1, depth, 1, idx--, 1);
			}
			System.out.println();
		}
	}

	private static void initTree(int start, int end, int node) {
		if(start == end) {
			if(start <= M) {
				tree[node] = 0;
			} else {
				tree[node]  = 1;
			}			
			return;
		}
		
		int mid = (start + end) / 2;
		initTree(start, mid, node * 2);
		initTree(mid+1, end, node*2+1);
		tree[node] = tree[node*2] + tree[node*2+1];
	}

	private static int query(int start, int end, int node, int left, int right) {
		
		if(end < left || right < start) return 0;
		if(left <= start && end <=right) return tree[node];
		
		int mid = (start + end) / 2;
		int lv = query(start, mid, node*2, left, right);
		int rv = query(mid+1, end, node*2+1, left, right);
		return lv + rv;		
	}

	private static void update(int start, int end, int node, int index, int value) {
		if(index < start || index > end) return;
		if(start == end) {
			tree[node] = value;
			return;
		}
		
		int mid = (start + end) / 2;
		update(start, mid, node*2, index, value);
		update(mid+1, end, node*2+1, index, value);
		
		tree[node] = tree[node*2] + tree[node*2+1];
	}
}
