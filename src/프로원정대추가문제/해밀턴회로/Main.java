package 프로원정대추가문제.해밀턴회로;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
	static int N;
	static int[][] map;
	static int[] visited;
	
	static int min = Integer.MAX_VALUE;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		
		map = new int[N][N];
		visited = new int[N];
		
		for(int i=0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		
		dfs(0,0, 1, "0");
		
		System.out.println(min);
	}

	static void dfs(int n, int sum, int count, String path) {
		if(count == N) {
			if(map[n][0] == 0) return;
			sum +=  map[n][0];
			min = Math.min(min, sum);
			//System.out.println(path + " : " + sum);
			return ;
		}
		
		visited[n] = 1;
		for(int i=0; i<N; i++) {
			if(map[n][i]== 0) continue;
			if(visited[i] == 1) continue;
			int nextSum = sum + map[n][i];
			if(min < nextSum) continue;
			dfs(i, nextSum, count+1, path + " > " + i);
		}
		visited[n] = 0;
	}
}



/*

6
0 93 23 32 39 46 
0 0 7 58 59 13 
40 98 0 14 33 98 
3 39 0 0 13 16 
51 25 19 88 0 47 
65 81 63 0 6 0

101

*/


