package 멘토링권장문제.도로건설;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M;
	static int[][] map;
	
	static int[] directRow = {-1, 1, 0, 0};
	static int[] directCol = {0, 0, -1, 1};
	
		
	public static void main(String[] args) throws Exception {
		//System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//멘토링권장문제//도로건설//sample_input1.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N][M];
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<M; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		
		int ret = dijkstra();
		System.out.println(ret);
	}


	private static int dijkstra() {
		int[][] dist = new int[N+1][M+1];
		for(int i=0; i<N+1; i++) {
			Arrays.fill(dist[i], Integer.MAX_VALUE);
		}
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Node(0, 0, map[0][0]));
		
		dist[0][0] = map[0][0];
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.row == N-1 && cur.col == M-1) return dist[cur.row][cur.col];
			
			if(dist[cur.row][cur.col] < cur.cost) continue;
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				
				if(nextRow < 0 || nextCol < 0 || nextRow >= N || nextCol >= M) continue;
				if(map[nextRow][nextCol] == -1) continue;
				if(dist[nextRow][nextCol] <= cur.cost + map[nextRow][nextCol]) continue;
				
				dist[nextRow][nextCol] = cur.cost + map[nextRow][nextCol];
				q.add(new Node(nextRow, nextCol, dist[nextRow][nextCol]));
			}
		}
		
		
		return -1;
	}
	
	static class Node {
		int row;
		int col;
		int cost;
		public Node(int row, int col, int cost) {
			this.row = row;
			this.col = col;
			this.cost = cost;
		}
		
	}

}
