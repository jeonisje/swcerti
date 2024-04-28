package 알고리즘별.union_find.순환회로_검사;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int[][] visited;
	static int[] parent;
	
	static int find(int a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	static void union(int a, int b) {
		int p1 = find(a);
		int p2 = find(b);
		
		if(p1 == p2) return;
		parent[p2] = p1;
	}
	
	
	public static void main(String[] args) throws IOException {		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		
		visited = new int[N][N];
		parent = new int[N];
		for(int i=0; i<N; i++) {
			parent[i] = i;
		}
		
		String ret = "STABLE";
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			int p1 = find(i);
			for(int j=0; j<N; j++) {
				int num = Integer.parseInt(st.nextToken());
				
				if(num == 1 && visited[i][j] == 0) {					
					int p2 = find(j);
					
					if(p1==p2) {
						ret = "WARNING";						
					}					
				
					union(i, j);
					visited[i][j] = 1;
					visited[j][i] = 1;
				}
			}
			if(ret.equals("WARNING")) break;
		}
		
		System.out.println(ret);
	}	
	
	
}
