package 알고리즘별.segmentTree.콩쿠르순위예측기;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int NUM = 1_000_001;
	static int[] arr = new int[NUM];
	static int[] tree = new int[NUM*4];
	static int N;
	static int count;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		
		count = 0;
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			int command = Integer.parseInt(st.nextToken());
			int num = Integer.parseInt(st.nextToken());
			
			if(command == 1) {
				count++;
				update(1, NUM, 1, num, 1);
				System.out.println(query(1, NUM, 1, 1, num) + "위");
			} else {
				count--;
				update(1, NUM, 1, num, 0);
				System.out.println(count + "명");
			}
		}
		
	}
	
	private static int query(int start, int end, int node, int left, int right) {
		if(end < left || right < start) return 0;
		if(left <= start && end <=right) return tree[node];
		
		int mid = (start + end) / 2;
		int lv = query(start, mid, node*2, left, right);
		int rv = query(mid+1, end, node*2+1, left, right);
		return lv + rv;		
	}

	static void update(int start, int end, int node, int index, int value) {
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

	
	
}
