package 사전테스트.해저터널;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Objects;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M;
	
	static char[][] map;
	static int[][] map2;
	static int[][] visited;
	static HashSet<Node> boundary;
	
	static int[] directY = {-1, 1, 0, 0};
	static int[] directX = {0, 0, -1, 1};
	
	static int num;
	static int cc;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new char[N][M];
		map2 = new int[N][M];
		visited = new int[N][M];
		
		cc = 0;
		
		for(int i=0; i<N; i++) {			
			String s = br.readLine();
			for(int j=0; j<M; j++) {
				map[i][j] = s.charAt(j);
			}
		}
	
		boundary = new HashSet<>();
		
		boolean found = false;
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				if(map[i][j] == 'X') {
					bfs1(i, j);					
					found = true;
					break;
				}
			}
			if(found) break;
		}
		int answer = Integer.MAX_VALUE;
		for(Node n : boundary) {
			answer = Math.min(answer, bfs2(n.y, n.x));
		}
		
		System.out.println(answer);
		
		return;
	}
	
	static void bfs1(int sy, int sx) {
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(sy, sx));
		
		map2[sy][sx] = 1;
		while(!q.isEmpty()) {
			Node cur = q.remove();
			
			for(int i=0; i<4; i++) {
				int ny = cur.y + directY[i];
				int nx = cur.x + directX[i];
				
				if(ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
				if(map2[ny][nx] == 1) continue;
				if(map[ny][nx] == '.') {
					boundary.add(new Node(cur.y, cur.x));
					continue;
				}
				
				q.add(new Node(ny, nx));
				map2[ny][nx] = 1;
				
			}
		}
		return;
	}	
	
	static int bfs2(int sy, int sx) {
		cc++;
		
		ArrayDeque<Node2> q = new ArrayDeque<>();
		q.add(new Node2(sy, sx, 0));
		
		visited[sy][sx] = cc;
		while(!q.isEmpty()) {
			Node2 cur = q.remove();
			
			for(int i=0; i<4; i++) {
				int ny = cur.y + directY[i];
				int nx = cur.x + directX[i];
				
				if(ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
				if(map2[ny][nx] == 1) 
					continue;
				if(visited[ny][nx] == cc) continue;
				if(map[ny][nx] == 'X') {
					//System.out.println("start: "+ sy + ", " + sx +  " / end : " +ny + "," + nx + " / cost : " + cur.cost);
					return cur.cost;
				}
				
				q.add(new Node2(ny, nx, cur.cost + 1));
				visited[ny][nx] = cc;
			}
		}
		return Integer.MAX_VALUE;
	}
	
	
	static class Node {
		int y;
		int x;
		public Node(int y, int x) {
			this.y = y;
			this.x = x;
		}
		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			return x == other.x && y == other.y;
		}		
	}
	
	static class Node2 {
		int y;
		int x;
		int cost;
		public Node2(int y, int x, int cost) {		
			this.y = y;
			this.x = x;
			this.cost = cost;
		}
	}
}

