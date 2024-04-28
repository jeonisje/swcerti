package 사전테스트.드래곤토벌._14;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;



public class Main {
	
	static int N, M;
	static int[][] map;
	
	static int[][] horizonIce;
	static int[][] viticalIce;
	
	static int[] directRow = {-1, 1, 0, 0};
	static int[] directCol = {0, 0, -1, 1};
	
	public static void main(String[] args) throws IOException {
		//Long start = System.currentTimeMillis();
		
		//System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\사전테스트\\드래곤토벌\\sample_input6.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N][M];
		horizonIce = new int[N][M];
		viticalIce = new int[N][M];
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());			
			for(int j=0; j<M; j++) {
				map[i][j] =  Integer.parseInt(st.nextToken());
			}
		}
		
		System.out.println(bfs());
		//System.out.println("estimated : " + (System.currentTimeMillis() - start));
		
		
	}
	
	static int bfs() {
		int[][][] visited = new int[N][M][2];
		int[][][] slided = new int[N][M][2];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		//q.add(new Node(0,0,false, fVisited));
		q.add(new Node(0,0,0));
		visited[0][0][0] = 0;
		
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.row == N-1 && cur.col == M -1) {			
				//System.out.println(cur.path);
				return visited[N-1][M -1][cur.hasShield];
			}
			
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				
				if(impossibleDir(nextRow, nextCol)) continue;
				
				if(map[nextRow][nextCol] == 0) continue; 
				if(map[nextRow][nextCol] == 3 && cur.hasShield == 0) continue;
					
				if(map[nextRow][nextCol] == 2) {
					if(visited[nextRow][nextCol][1] != 0) continue;
				} else if(map[nextRow][nextCol] == 1) {
					if(cur.hasShield == 1) {
						if(visited[nextRow][nextCol][1] != 0) 
							continue;
					} else {
						if(visited[nextRow][nextCol][0] != 0) 
							continue;
					}
				} else if(map[nextRow][nextCol] == 3) {
					if(visited[nextRow][nextCol][1] != 0) 
						continue;
				}
 			
				int hasShield = cur.hasShield;
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
						} else {
							if(map[newRow][newCol] != 4) {
								continualble = false;
								if(map[newRow][newCol] == 3 || map[newRow][newCol] == 0) {
									newRow -= directRow[i];
									newCol -= directCol[i];
									sCount -= 1;
								}
							}
						}					
						slided[newRow][newCol][dir] = 1;
						
						
					}
					visited[newRow][newCol][0] = visited[cur.row][cur.col][0] + visited[cur.row][cur.col][1] + sCount;
					q.add(new Node(newRow, newCol, 0));
					//q.add(new Node(newRow, newCol, false, visited, cur.path +  "->S("+newRow+","+newCol+")("+visited[newRow][newCol]+")"));
					
				} else {
					if(map[nextRow][nextCol] == 2) {
						visited[nextRow][nextCol][1] = visited[cur.row][cur.col][cur.hasShield] + 1;						
						hasShield = 1;
					} else if(map[nextRow][nextCol] == 3) {
						if(hasShield != 1) continue;
						visited[nextRow][nextCol][hasShield] = visited[cur.row][cur.col][cur.hasShield] +  1;	
					} else {
						visited[nextRow][nextCol][hasShield] = visited[cur.row][cur.col][cur.hasShield] + 1;	
					}
					q.add(new Node(nextRow, nextCol, hasShield));
					//q.add(new Node(nextRow, nextCol, hasShield, visited, cur.path +  "->("+nextRow+","+nextCol+")("+visited[nextRow][nextCol]+")"));
				}
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
		int hasShield;
		String path;
		
		public Node(int row, int col, int hasShield, String path) {			
			this.row = row;
			this.col = col;
			this.hasShield = hasShield;
			this.path = path;
		}
		public Node(int row, int col, int hasShield) {			
			this.row = row;
			this.col = col;
			this.hasShield = hasShield;
			this.path = path;
		}
	}
}