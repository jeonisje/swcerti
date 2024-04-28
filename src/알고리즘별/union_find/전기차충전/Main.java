package 알고리즘별.union_find.전기차충전;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	
	static int T, N;
	static int[] parent;
	static int[] visited;
	static int[] minMap;
	
	static int count;
	
	static int find(int a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	static void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		
		parent[pb] = pa;		
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());		
		T = Integer.parseInt(st.nextToken());
		
		parent = new int[T+1];
		visited = new int[T+1];
		minMap = new int[T+1];
		
				
		for(int i=1; i<=T; i++) {
			parent[i] = i;
		}		
		
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		
		count = 0;
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			int num = Integer.parseInt(st.nextToken());
			
			boolean found = false;
			if(visited[num] == 0) {
				count++;
				visited[num] = 1;
				minMap[num] = num;
				found = true;;
				continue;
			}
			
			
			for(int preVer = minMap[num]  - 1; preVer > 0; preVer--) {
				 
				int parent = find(preVer);
				if(parent == preVer) {
					if(visited[preVer] == 1) continue;
					union(num, preVer);
					count++;
					visited[preVer] = 1;
					minMap[num] = preVer;
					found = true;
					break;
				}
			}
			
			if(!found) break;
		}
		System.out.println(count);
		
	}
}


/*

4
6
1
3
4
4
4
3


=> 4



7
5
5
3
5
5
5

*/
