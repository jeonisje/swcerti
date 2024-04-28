package 사전테스트.드래곤토벌._03;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;


public class Main {
	
	static int N, M;
	static int[][] map;
	
	static int[][] horizonIceRight;
	static int[][] horizonIceLeft;
	static int[][] verticalIce;
	
	static int[][] horizonCheck;
	static int[][] verticalCheck;
	
	static int[][] marked;
	
	
	static int[] directRow = {-1, 1, 0, 0};
	static int[] directCol = {0, 0, -1, 1};
	
	public static void main(String[] args) throws IOException {
		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\사전테스트\\드래곤토벌\\sample_input2.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N][M];
		horizonIceRight = new int[N][M];
		horizonIceLeft = new int[N][M];
		verticalIce = new int[N][M];
		
		horizonCheck = new int[N][M];
		verticalCheck = new int[N][M];
		marked = new int[N][M];
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			int horizonCnt = 0;
			int h_startRow = 0;
			int h_startCol = 0;
			
			
			for(int j=0; j<M; j++) {
				map[i][j] =  Integer.parseInt(st.nextToken());
				
				if(map[i][j] == 4) {
					if(horizonCnt == 0) {
						h_startRow = i;
						h_startCol = j;
					}
					horizonCnt++;
					horizonIceRight[i][j] = h_startCol;
					
					
					
					if(i == 0 ) {
						verticalIce[i][j] = 1;
						verticalCheck[i][j] = i;
					} else {
						verticalIce[i][j] = verticalIce[i-1][j]  + 1;
						verticalCheck[i][j] = i;
					}
				} else {
					if(horizonCnt != 0) {
						//horizonIce[h_startRow][h_startCol] = horizonCnt;
						//horizonIce[i][j] = horizonCnt;						
						
						if(map[i][j] == 1 || map[i][j] == 2) {
							horizonIceRight[h_startRow][h_startCol] = j;
							
						} else {
							horizonIceRight[h_startRow][h_startCol] = j-1;
							
						}
						horizonCnt = 0;
					}
					
					if(i != 0 ) {
						if(verticalIce[i-1][j] != 0) {
							int count = verticalIce[i-1][j];
							verticalIce[i-count][j] = count;
						}
						
						if(map[i][j] == 1 || map[i][j] == 2) {
							horizonIceRight[h_startRow][h_startCol] = j;
							
						} else {
							horizonIceRight[h_startRow][h_startCol] = j-1;
							
						}
					}
					
				}
				
				if(i==N-1) {
					int count = verticalIce[i][j];
					if(count != 0) {
						verticalIce[i-count + 1][j] = count;
					}
				}
				
				
			}
		}
		
		
		System.out.println(bfs());
		
		System.out.println("estimated : " + (System.currentTimeMillis() - start));
		
		
	}
	
	static int bfs() {
		int[][] fVisited = new int[N][M];
		int[][][] slided = new int[N][M][2];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(0,0,false, fVisited));
		fVisited[0][0] = 0;
		
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.row == N-1 && cur.col == M -1) {				
				return cur.visited[N-1][M -1];
			}
			
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				
								
				if(impossibleDir(nextRow, nextCol)) continue;
				if(cur.visited[nextRow][nextCol] != 0) continue;
				
				
				if(map[nextRow][nextCol] == 3 && !cur.hasShield) continue;
				
				int[][] visited =cur.visited;
 			
				boolean hasShield = cur.hasShield;
				if(map[nextRow][nextCol] == 4) {
					hasShield = false;
					int dir = 0; // 1 상하 , 2 좌우
					if(directRow[i] == 0)  {
						dir = 1;
					}
					
					if(slided[nextRow][nextCol][dir] == 1) continue;
					
					int newRow = nextRow;
					int newCol = nextCol;				
					int count = 0;
					
					int moveVer = 0;
					int moveCol = 0;
					int move = 0;
					if(dir == 0) {
						moveVer = verticalIce[newRow][nextCol];
						move = moveVer;
					} else {
						moveCol = horizonIceRight[newRow][nextCol];
						move = moveCol;
					}
					
					newRow += moveVer * directRow[i];
					newCol += moveCol * directCol[i];
					
					
					if(impossibleDir(newRow, newCol)) {					
						newRow -= directRow[i];
						newCol -= directCol[i];								
					} else {
						
							
						if(map[newRow][newCol] == 3 || map[newRow][newCol] == 0) {
							newRow -= directRow[i];
							newCol -= directCol[i];
							move -= 1;
						} else if (map[newRow][newCol] == 1 || map[newRow][newCol] == 2){
							
							count = 1;
							if (map[newRow][newCol] == 2) {
								hasShield = true;
							}
						}
					
					}					
					slided[newRow][newCol][dir] = 1;
						
					visited[newRow][newCol] += visited[cur.row][cur.col] + move + count;
					q.add(new Node(newRow, newCol, hasShield, visited));
					
					
				} else {
					
					if(map[nextRow][nextCol] == 0) continue;
					
					if(map[nextRow][nextCol] == 2) {
						visited = new int[N][M];
						visited[nextRow][nextCol] = cur.visited[cur.row][cur.col] + 1;
						hasShield = true;
					} else {
						visited[nextRow][nextCol] = visited[cur.row][cur.col] + 1;
					}
					q.add(new Node(nextRow, nextCol, hasShield, visited));
					//q.add(new Node(nextRow, nextCol, cur.count + 1 + addCount, hasShield, cur.path + "->("+nextRow + ","+nextCol+")("+  (cur.count + 1)+")"));
					//visited[nextRow][nextCol] = 1;
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
		boolean hasShield;
		String path;
		int[][] visited;
		
		public Node(int row, int col, boolean hasShield, String path, int[][] visited) {			
			this.row = row;
			this.col = col;
			this.hasShield = hasShield;
			this.path = path;
			this.visited = visited;
		}
		public Node(int row, int col, boolean hasShield, int[][] visited) {			
			this.row = row;
			this.col = col;
			this.hasShield = hasShield;
			this.path = path;
			this.visited = visited;
		}
	}
}