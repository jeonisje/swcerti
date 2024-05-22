package 프로원정대.day7.미생물관찰;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class Main {
	static int N, M;
	static int[][] map;
	
	static int[] directY = {-1, 1, 0, 0};
	static int[] directX = {0, 0, -1, 1};
	static int[][] visited;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N][M];
		visited = new int[N][M];		
		
		for(int i=0; i<N; i++) {
			String s = br.readLine();
			for(int j=0; j<M; j++) {
				int num = 0;
				if(s.charAt(j) == 'A') num = 1;
				if(s.charAt(j) == 'B') num = 2;
				map[i][j] = num;
			}
		}
		
		int[] result = new int[3];
		int max = 0;
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				int type = map[i][j];
				if(type == 0) {
					visited[i][j] = 1; 
					continue;
				}
				int ret = ff(type, i, j);
				if(ret == 0) continue;
				result[type]++;
				max = Math.max(ret, max);
			}
		}
		
		System.out.println(result[1] + " " + result[2]);
		System.out.println(max);
		
	}
	
	static int ff(int type, int y, int x) {		
		if(visited[y][x] == 1) return 0;
		
		int count = 0;
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(y, x));
		visited[y][x] = 1;
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			count++;
			
			for(int i=0; i<4; i++) {
				int ny = cur.y + directY[i];
				int nx = cur.x + directX[i];
				
				if(ny < 0 || nx < 0 || ny >= N || nx >= M) 	continue;
				if(visited[ny][nx] == 1) continue;
				if(map[ny][nx] != type) continue;
				
				visited[ny][nx] = 1;
				q.add(new Node(ny, nx));
			}
		}
		
		return count;
	}
	
	
	static class Node {
		int y;
		int x;
		public Node(int y, int x) {
			this.y = y;
			this.x = x;
		}
	}
}
