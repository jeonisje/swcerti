package 프로원정대추가문제.럭셔리여행;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N, S, E;
	static int[][] map;
	static int[] visited;
	
	static int min = Integer.MAX_VALUE;
	static int max = 0;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		
		map = new int[N][N];
		visited = new int[7];
		for(int i=0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		st = new StringTokenizer(br.readLine());
		S = Integer.parseInt(st.nextToken());
		E = Integer.parseInt(st.nextToken());
		
		dfs(S, 0);
		
		System.out.println(min);
		System.out.println(max);
	}
	
	static void dfs(int n, int cost) {
		if(n == E) {
			min = Math.min(min, cost);
			max = Math.max(max, cost);
			return;
		}
		
		visited[n] = 1;
		for(int i=0; i<N; i++) {
			if(map[n][i] == 0) continue;
			if(visited[i] == 1) continue;
			dfs(i, cost + map[n][i]);
		}
		
		visited[n] = 0;
	}

}
