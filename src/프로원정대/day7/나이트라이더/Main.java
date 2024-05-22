package 프로원정대.day7.나이트라이더;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;


public class Main {
	static int H, W;
	static int[][] map;
	
	static int[] directY = {-1, -2, -2, -1, 1, 2, 2, 1};
	static int[] directX = {-2, -1, 1, 2, 2, 1, -1, -2};
	
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		
		H = Integer.parseInt(st.nextToken());
		W = Integer.parseInt(st.nextToken());
		
		map = new int[H+1][W+1];
		int[] loc = new int[4];
		st = new StringTokenizer(br.readLine());
		
		for(int i=0; i<4; i++) {
			loc[i] = Integer.parseInt(st.nextToken());
		}	
		
		System.out.println(ff(loc));
	}
	
	static int ff(int[] loc) {		
		int[][] visited = new int[H+1][W+1];
		
		int startY = loc[0];
		int startX = loc[1];
		int endY = loc[2];
		int endX = loc[3];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(startY, startX));
		visited[startY][startX] = 1;
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			
			if(cur.y == endY && cur.x == endX) {
				return visited[cur.y][cur.x] - 1;
			}
			
			for(int i=0; i<8; i++) {
				int ny = cur.y + directY[i];
				int nx = cur.x + directX[i];
				
				if(ny < 1 || nx < 1 || ny > H || nx > W) 	continue;
				if(visited[ny][nx] != 0) continue;				
				
				visited[ny][nx] = visited[cur.y][cur.x] + 1;
				q.add(new Node(ny, nx));
			}
		}
		
		return -1;
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