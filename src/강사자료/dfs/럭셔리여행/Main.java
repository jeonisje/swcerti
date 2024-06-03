package 강사자료.dfs.럭셔리여행;

import java.util.*;
import java.io.*; 

public class Main {
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st; 
	static int n; 
	static int[][] MAP;
	static int[] visited; 
	static int start, end; 
	static int sum = 0; 
	static int maxval = Integer.MIN_VALUE;
	static int minval = Integer.MAX_VALUE;
	
	static void dfs(int node) {
		
		visited[node] = 1; 
		// 기저조건 -> 도착지에 도착했다면 최소, 최대값 갱신
		if(node == end) {
			if(sum > maxval)
				maxval = sum;
			if(sum < minval)
				minval = sum; 
		}
		for(int i = 0; i < n; i++) {
			// 길이 없으면 pass
			if(MAP[node][i] == 0)
				continue;
			// 방문했다면 pass 
			if(visited[i] == 1)
				continue;
			//pre -> i로 들어가는 비용을 넣기
			sum += MAP[node][i]; 
			dfs(i);
			// post -> i에서 빠져나오면서 비용, 방문 기록을 해제
			sum -= MAP[node][i];
			visited[i] = 0;
		}
	}

	public static void main(String[]args) throws IOException {
		n = Integer.parseInt(br.readLine());
		
		// 초기화
		visited = new int[n]; 
		MAP = new int[n][n];
		
		for(int i = 0; i < n; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j = 0; j < n; j++) {
				MAP[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		st = new StringTokenizer(br.readLine());
		start = Integer.parseInt(st.nextToken());
		end = Integer.parseInt(st.nextToken());
		dfs(start); 
		System.out.println(minval);
		System.out.println(maxval);
	}
}