package 사전테스트.드래곤토벌;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M;
	static int[][] map;
	
	static int[] directRow = {-1, 1, 0, 0};
	static int[] directCol = {0, 0, -1, 1};
	
	public static void main(String[] args) throws IOException {
		//Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\사전테스트\\드래곤토벌\\sample_input3.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N][M];
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<M; j++) {
				map[i][j] =  Integer.parseInt(st.nextToken());
			}
		}
		System.out.println(bfs());
		
		//System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
	
	static int bfs() {
		int[][] visited = new int[N][M];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(1,0,0,0,0,0, false));
		//q.add(new Node(1,0,0,0,0,0, false, "(0,0)"));
		visited[0][0] = 1;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.row == N-1 && cur.col == M -1) {
				//Answer = Math.min(Answer, cur.count);
				//continue;
				//System.out.println(cur.path);
				return cur.count;
			}
			
			if(cur.type == 4) {
				int nextRow = cur.row + cur.dirRow;
				int nextCol = cur.col + cur.dirCol;
				
				if(impossibleDir(nextRow, nextCol)) continue; 
				
				int dirRow = cur.dirRow;
				int dirCol = cur.dirCol;
				
				if(map[nextRow][nextCol] == 0) continue;
				if(visited[nextRow][nextCol] == 1) continue;
				
				boolean hasShield = false;
				if(map[nextRow][nextCol] == 3) {
					// 불
					getNext(q, cur, visited);
				
				} else { 
					if(map[nextRow][nextCol] == 1 || map[nextRow][nextCol] == 2) {
						dirRow = 0;
						dirCol = 0;
						if(map[nextRow][nextCol] == 2) {
							hasShield = true;
						}
						visited[nextRow][nextCol] = 1;
					}
					
					q.add(new Node(map[nextRow][nextCol], nextRow, nextCol, cur.count + 1, dirRow, dirCol, hasShield));
					//q.add(new Node(map[nextRow][nextCol], nextRow, nextCol, cur.count + 1, dirRow, dirCol, hasShield, cur.path + "-> (" + nextRow + "," + nextCol + ")(" +(cur.count + 1) + ")"));
					
				}
			} else {
				getNext(q, cur, visited);
			}
		}
		
		
		return -1;
	}
	
	static int bfs2(int row, int col) {
		
		int[][] visited = new int[N][M];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		
		q.add(new Node(row,col,0));
		visited[row][col] = 1;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(map[cur.row][cur.col] == 2) {
				return cur.count * 2;
			}
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				
				if(impossibleDir(nextRow, nextCol)) 
					continue;
				if(map[nextRow][nextCol] == 0) 
					continue;
				if(map[nextRow][nextCol] == 3) 
					continue;
				if(map[nextRow][nextCol] == 4) 
					continue;
				if(visited[nextRow][nextCol] == 1) 
					continue;
				
				q.add(new Node(nextRow, nextCol, cur.count + 1));
				visited[nextRow][nextCol] = 1;
			}
				
		}
		
		return -1;
	}
	
	static boolean impossibleDir(int row, int col) {
		if(row < 0 || col < 0 || row >= N || col >= M) return true;
		return false;
	}
	
	static void getNext(ArrayDeque<Node> q, Node cur, int[][] visited) {
		for(int i=0; i<4; i++) {
			int nextRow = cur.row + directRow[i];
			int nextCol = cur.col + directCol[i];
			
			int nextCount = cur.count + 1;
			
			if(impossibleDir(nextRow, nextCol)) continue;
			if(visited[nextRow][nextCol] == 1) continue;
			if(map[nextRow][nextCol] == 0) continue;
			if(map[nextRow][nextCol] == 3 && !cur.hasShield) {
				int findStatue = bfs2(cur.row, cur.col);
				if(findStatue == -1) continue;
				nextCount = nextCount + findStatue;
			}
			
			int dirRow = 0;
			int dirCol = 0;
			boolean hasShield = cur.hasShield;
			if(map[nextRow][nextCol] == 2) {
				hasShield = true;
			} else if(map[nextRow][nextCol] == 4) {
				//if(cur.type == 4) continue;
				hasShield = false;
				dirRow = directRow[i];
				dirCol = directCol[i];
			}
					
			q.add(new Node(map[nextRow][nextCol], nextRow, nextCol, nextCount, dirRow, dirCol, hasShield));
			//q.add(new Node(map[nextRow][nextCol], nextRow, nextCol, nextCount, dirRow, dirCol, hasShield, cur.path + "-> (" + nextRow + "," + nextCol + ")"));
			if(map[nextRow][nextCol] != 4) {
				visited[nextRow][nextCol] = 1;
			}
		}
	}
	
	
	static class Node {
		int type;
		int row;
		int col;
		int count;
		int dirRow;
		int dirCol;
		boolean hasShield;
		String path;
		public Node(int type, int row, int col, int count, int dirRow, int dirCol, boolean hasShield) {
			super();
			this.type = type;
			this.row = row;
			this.col = col;
			this.count = count;
			this.dirRow = dirRow;
			this.dirCol = dirCol;
			this.hasShield = hasShield;
		}
		
		public Node(int type, int row, int col, int count, int dirRow, int dirCol, boolean hasShield, String path) {
			super();
			this.type = type;
			this.row = row;
			this.col = col;
			this.count = count;
			this.dirRow = dirRow;
			this.dirCol = dirCol;
			this.hasShield = hasShield;
			this.path = path;
		}

		public Node(int row, int col, int count) {
			this.row = row;
			this.col = col;
			this.count = count;
		}
		
		
	}
}