package 알고리즘별.dijkstra.HeartCatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class Main {
	static int N;
	static int[][] map;
	static int[][] visited;
	
	static int[] directY = {-1, 1, 0, 0};
	static int[] directX = {0, 0, -1, 1};
		
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		int T = Integer.parseInt(br.readLine());
		
		
		for(int q=1; q<=T; q++) {
			
			st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			
			map = new int[N][N];
			visited = new int[N][N];
			ArrayList<Node> starts = new ArrayList<>(); 
			for(int i=0; i<N; i++) {
				st = new StringTokenizer(br.readLine());
				for(int j=0; j<N; j++) {
					map[i][j] = Integer.parseInt(st.nextToken());
					if(map[i][j] == 0)  starts.add(new Node(i, j, 0));
				}
			}
			
			int answer = Integer.MAX_VALUE;
			for(Node start : starts) {
				if(visited[start.y][start.x] != 0) continue;				
				int ret = dijkstart(start.y,  start.x);
				if(ret == -1) continue;
				answer = Math.min(ret, answer);
				
			}
			
			System.out.println("#" + q + " " + answer);
		}
	}
	
	static int dijkstart(int sy, int sx) {
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Node(sy, sx, 0));
		
		int[][] dist = new int[N][N];
		for(int i=0; i<N; i++) {
			Arrays.fill(dist[i], Integer.MAX_VALUE);
		}
		dist[sy][sx] = 0;
		while(!q.isEmpty()) {
			Node cur = q.remove();
			
			
			if(dist[cur.y][cur.x] < cur.cost) continue;
			for(int i=0; i<4; i++) {
				int ny = cur.y + directY[i];
				int nx = cur.x + directX[i];
				
				if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
				if(map[ny][nx] == -1) continue;
				
				int nextCost = Math.max(cur.cost, map[ny][nx]);
				if(dist[ny][nx] <= nextCost) continue;
				dist[ny][nx] = nextCost;
				q.add(new Node(ny, nx, dist[ny][nx]));
				
				if(map[ny][nx] == 0) {
					visited[sy][sx] = 1;
					visited[ny][nx] = 1;
					return dist[ny][nx];
				}
				
			}
		}
		
		
		return -1;
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


/*
1
9
82 69 57 42 16 46 91 -1 72 
54 59 100 84 96 7 6 46 0 
59 12 0 14 73 99 21 50 55 
21 34 87 25 86 84 39 90 78 
26 71 99 4 82 17 66 74 32 
39 21 22 1 4 46 86 8 97 
72 6 69 39 62 22 9 54 61 
16 73 -1 55 29 14 60 15 41 
30 14 81 1 47 96 25 43 86 


#4 11


*/

 