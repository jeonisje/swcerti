package 프로원정대.day8.무서운시어머니;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	
	static int N;
	
	static int[][] map;
	
	static int[] directY = {-1, 1, 0, 0};
	static int[] directX = {0, 0, -1, 1};
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		int startY = Integer.parseInt(st.nextToken());
		int startX = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		map = new int[N][N];
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<N; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		System.out.println(dijkstra(startY, startX));
		
	}
	
	static int dijkstra(int sy, int sx) {
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Node(sy, sx, map[sy][sx]));
		
		int[][] dist = new int[N+1][N+1];
		for(int i=0; i<N+1; i++) {
			Arrays.fill(dist[i], Integer.MAX_VALUE);
		}
		
		int max = 0;
		while(!q.isEmpty()) {
			Node cur = q.remove();			
			
			if(dist[cur.y][cur.x] < cur.cost) continue;
			
			max = Math.max(max, cur.cost);
			
			for(int i=0; i<4; i++) {
				int ny = cur.y + directY[i];
				int nx = cur.x + directX[i];
				
				if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
				if(map[ny][nx] == -1) continue;
				
				int nextCost = cur.cost + map[ny][nx];
				if(dist[ny][nx] <= nextCost) continue;
				dist[ny][nx] = nextCost;
				q.add(new Node(ny, nx, dist[ny][nx]));
			}
			
		}
		
		return max;
	}
	
	static class Node {
		int y;
		int x;
		int cost;
		public Node(int y, int x, int cost) {		
			this.y = y;
			this.x = x;
			this.cost = cost;
		}
	}
}
