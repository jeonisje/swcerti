package 알고리즘별.union_find.Union_Find;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	static int N, Q;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		
		UnionFind uf = new UnionFind(N);
		
		for(int i=0; i<Q; i++) {
			st = new StringTokenizer(br.readLine());
			int q = Integer.parseInt(st.nextToken());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			
			switch(q) {
				case 0 : 
					int p1 = uf.find(a);
					int p2 = uf.find(b);
					if(p1 == p2) System.out.println("YES");
					else System.out.println("NO");
					break;
				case 1 :
					uf.union(a, b);
					break;					
			}
		}
		
	}
	
	static class UnionFind {
		int arr[];
		
		public UnionFind(int n) {
			arr = new int[n+1];
			for(int i=1; i<=n; i++) {
				arr[i] = i;
			}
		}
		
		int find(int a) {
			if(arr[a] == a) return a;
			return arr[a] = find(arr[a]);
		}
		
		void union(int a, int b) {
			int n1 = find(a);
			int n2 = find(b);
			
			if(n1 == n2) return;
			
			arr[n2] = find(a);
		}
	}
}
