package 프로원정대.day7.플러드필;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class Main {
	
	static int[][] map;
	
	static int[] directY = {-1, 1, 0, 0};
	static int[] directX = {0, 0, -1, 1};
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		
		int y = Integer.parseInt(st.nextToken());
		int x = Integer.parseInt(st.nextToken());
		
		map = new int[5][5];
		
		ff(y, x);
	}
	
	static void ff(int y, int x) {
		int[][] visited = new int[5][5];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(y, x));
		visited[y][x] = 1;
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			
			for(int i=0; i<4; i++) {
				int ny = cur.y + directY[i];
				int nx = cur.x + directX[i];
				
				if(ny < 0 || nx < 0 || ny >=5 || nx >= 5) continue;
				if(visited[ny][nx] != 0) continue;
				
				visited[ny][nx] = visited[cur.y][cur.x] + 1;
				q.add(new Node(ny, nx));
			}
		}
		
		for(int i=0; i<5; i++) {			
			for(int j=0; j<5; j++) {
				System.out.print(visited[i][j] + " ");				
			}
			System.out.println();
		}
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
