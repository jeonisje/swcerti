package 사전테스트.드래곤토벌._01;


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
		
		//System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\사전테스트\\드래곤토벌\\sample_input4.txt"));
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
		int[][][] slided = new int[N][M][2];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(0,0,0, false));
		//q.add(new Node(0,0,0, false, "(0,0)"));
		visited[0][0] = 1;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.row == N-1 && cur.col == M -1) {
				//Answer = Math.min(Answer, cur.count);
				//continue;
				//System.out.println(cur.path);
				return cur.count;
			}
			
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				
				int addCount = 0;
				
				if(impossibleDir(nextRow, nextCol)) continue;
				if(visited[nextRow][nextCol] == 1) continue;
				if(map[nextRow][nextCol] == 0) continue;
				
				if(map[nextRow][nextCol] == 3 && !cur.hasShield) {
					addCount = bfs2(cur.row, cur.col) ;
					if(addCount == -1) continue;
					
				}
				
			
				boolean hasShield = cur.hasShield;
				if(map[nextRow][nextCol] == 4) {
					
					int dir = 0; // 1 상하 , 2 좌우
					if(directRow[i] == 0)  {
						dir = 1;
					}
					
					if(slided[nextRow][nextCol][dir] == 1) continue;
					slided[nextRow][nextCol][dir] = 1;	
						
					boolean continualble = true;
					int newRow = nextRow;
					int newCol = nextCol;
					int newCount = cur.count;					
					int sCount = 1;
					while(continualble) {
						
						newRow += directRow[i];
						newCol += directCol[i];
						sCount += 1;
						if(impossibleDir(newRow, newCol)) {
							continualble = false;
							newRow -= directRow[i];
							newCol -= directCol[i];
							sCount -= 1;
							int a=1;
						} else {
							if(map[newRow][newCol] != 4) {
								continualble = false;
								if(map[newRow][newCol] == 3) {
									newRow -= directRow[i];
									newCol -= directCol[i];
									sCount -= 1;
								}
							}
						}					
						slided[newRow][newCol][dir] = 1;
						
						//System.out.println("after : newRow : " + newRow+ ", newCol : " + newCol + ", dir : " + dir);
					}
					//q.add(new Node(newRow, newCol, newCount, false));
					//if(visited[newRow][newCol] == 1) continue;
					q.add(new Node(newRow, newCol, cur.count + sCount, false));
					//q.add(new Node(newRow, newCol, cur.count + sCount, false, cur.path + "->S("+newRow + ","+newCol+")("+(cur.count + sCount)+")("+ sCount+")"));
					
				} else {
					if(map[nextRow][nextCol] == 2) {
						hasShield = true;
					} 
					q.add(new Node(nextRow, nextCol, cur.count + 1 + addCount, hasShield));
					//q.add(new Node(nextRow, nextCol, cur.count + 1 + addCount, hasShield, cur.path + "->("+nextRow + ","+nextCol+")("+  (cur.count + 1)+")"));
					visited[nextRow][nextCol] = 1;
				}
			}
			
		}
		
		
		return -1;
	}
	
	static int bfs2(int row, int col) {
		
		int[][] visited = new int[N][M];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		
		q.add(new Node(row,col,0));
		visited[row][col] = 1;
		int already = 0; 
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
				//if(alreadyVistied[nextRow])
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
	
	static class Node {
		int row;
		int col;
		int count;
		boolean hasShield;
		String path;
		
		
		public Node(int row, int col, int count, boolean hasShield) {			
			this.row = row;
			this.col = col;
			this.count = count;
			this.hasShield = hasShield;
		}


		public Node(int row, int col, int count) {
			this.row = row;
			this.col = col;
			this.count = count;
		}


		public Node(int row, int col, int count, boolean hasShield, String path) {
			this.row = row;
			this.col = col;
			this.count = count;
			this.hasShield = hasShield;
			this.path = path;
		}
		
		
	}
}