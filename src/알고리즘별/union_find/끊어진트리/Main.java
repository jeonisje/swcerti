package 알고리즘별.union_find.끊어진트리;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	static int N, Q;
	
	static int[] parent;
	static int[] myParent;
	static ArrayList<Integer>[] children;
	static int[] deleted;
			
	
	static int find(int a) {
		if(parent[a] == a) return a;
		//return parent[a] = find(parent[a]);
		return  find(parent[a]);
	}
	
	static void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		
		parent[pb] = pa;
	}
	
	static void bfs(int start) {
		ArrayDeque<Integer> q = new ArrayDeque<>();
		q.add(start);
		
		int[] visited = new int[N+1];
		visited[start] = 1;
		
		while(!q.isEmpty()) {
			int cur = q.poll();
			
			for(int next : children[cur]) {
				if(deleted[next] == 1) continue;
				if(visited[next] == 1) continue;
				
				q.add(next);
				parent[next] = next;
				union(cur, next);
				visited[next] = 1;
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());		
		
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		
		parent = new int[N+1];
		myParent = new int[N+1];
		children = new ArrayList[N+1];
		deleted = new int[N+1];
		
		for(int i=1; i<=N; i++) {
			parent[i] = i;
			myParent[i] = i;
			children[i] = new ArrayList<>();
		}
		
		
		for(int i=1; i<N; i++) {
			st = new StringTokenizer(br.readLine());		
			int num = Integer.parseInt(st.nextToken());
			union(num, i+1);
			children[num].add(i+1);
			myParent[i+1] = num ;
		}
		
		for(int i=0; i<(N-1)+Q; i++) {
			st = new StringTokenizer(br.readLine());		
			int q = Integer.parseInt(st.nextToken());
			
			switch(q) {
				case 0	:
					int num = Integer.parseInt(st.nextToken());
					deleted[num] = 1;
					parent[num] = num;
					
					bfs(num);
					
					break;
				case 1:
					int a = Integer.parseInt(st.nextToken());
					int b = Integer.parseInt(st.nextToken());
					
					int pa = find(a);
					int pb = find(b);
					if(pa == pb) System.out.println("YES");
					else System.out.println("NO");
					break;
			}
			
		}		
	}
}



/*


YES
YES
YES
YES
YES
YES
YES
YES
NO
NO







*/
