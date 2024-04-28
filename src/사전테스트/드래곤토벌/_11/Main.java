package 사전테스트.드래곤토벌._11;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M;
	static int[][] map;
	
	static int[] directRow = {-1, 1, 0, 0};
	static int[] directCol = {0, 0, -1, 1};
	
	static int[][] shield;
	
	public static void main(String[] args) throws IOException {
		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\사전테스트\\드래곤토벌\\sample_input6.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N][M];
		shield = new int[N][M];
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			for(int j=0; j<M; j++) {
				map[i][j] =  Integer.parseInt(st.nextToken());
			}
		}
		System.out.println(bfs());
		
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
	
	static int bfs() {
		int[][] visited = new int[N][M];
		int[][][] slided = new int[N][M][2];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		//q.add(new Node(0,0, false));		
		q.add(new Node(0,0, false, "(0,0)(0)"));
		visited[0][0] = 0;
		int idx = 0;
		
		int lastRow = 0;
		int lastCol = 0;
		String lastPath = "";
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.row == N-1 && cur.col == M -1) {
				System.out.println(cur.path);
				return visited[cur.row][cur.col];
			}
			idx++;
			ArrayList<Hold> hold = new ArrayList<>();
			
			lastRow = cur.row;
			lastCol = cur.col;
			lastPath = cur.path;
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				boolean hasShield = cur.hasShield;
				
				//System.out.println("nextRow => " + nextRow + ", nextCol" + nextCol);
				
				if(impossibleDir(nextRow, nextCol)) continue;
				if(visited[nextRow][nextCol] != 0) continue;
				if(map[nextRow][nextCol] == 0) continue;
				
				int addCount = 0;
				Shield findShield = null;
				if(map[nextRow][nextCol] == 3 && !cur.hasShield) {
					hold.add(new Hold(nextRow, nextCol));
					
					//findShield = bfs2(cur.row, cur.col, nextRow, nextCol, cur.path) ;
					findShield = bfs2(nextRow, nextCol, nextRow, nextCol, cur.path) ;
					if(findShield == null) continue;
					hasShield = true;
				}
			
				
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
							int a=1;
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
						
						//System.out.println("after : newRow : " + newRow+ ", newCol : " + newCol + ", dir : " + dir);
					}
					visited[newRow][newCol] = visited[cur.row][cur.col] + sCount;
					//q.add(new Node(newRow, newCol, false));
					q.add(new Node(newRow, newCol, false, cur.path + "->S("+newRow + ","+newCol+")("+  (visited[newRow][newCol])+")"));
					
				} else if(map[nextRow][nextCol] == 3 && !hasShield) {
					System.out.println("need Shield " + nextRow + nextCol);
				} else {
					if(map[nextRow][nextCol] == 2) {
						hasShield = true;
					} 
					
					if(map[nextRow][nextCol] == 3) {						
						//visited[nextRow][nextCol] = visited[cur.row][cur.col]
						if(findShield.meetShield) {
							shield[nextRow][nextCol] = visited[cur.row][cur.col] + findShield.count + 1;
							
							visited[nextRow][nextCol] = shield[nextRow][nextCol];
						} else {
							shield[nextRow][nextCol] = findShield.count + 1;
							visited[nextRow][nextCol] = shield[nextRow][nextCol] ;
						}
						
						
					} else {
						visited[nextRow][nextCol] = visited[cur.row][cur.col] + 1;
					}
					
					//q.add(new Node(nextRow, nextCol,  hasShield));
					q.add(new Node(nextRow, nextCol,  hasShield, cur.path + "->("+nextRow + ","+nextCol+")("+  (visited[nextRow][nextCol])+")(" + addCount + ")"));
					
					
				}
				
				
			}
			
		
		}
		
		
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<M; j++) {
				System.out.print(visited[i][j] + " ");
			}
				
			System.out.println();
		}
		return -1;
	}
	
	static Shield bfs2(int row, int col, int nRow, int nCol, String path) {
		
		int[][] visited = new int[N][M];
		
		ArrayDeque<Node2> q = new ArrayDeque<>();
		
		//q.add(new Node2(row,col,0));
		q.add(new Node2(row,col,0, "("+row+"," +col+")"));
		visited[row][col] = 0;
		
		while(!q.isEmpty()) {
			Node2 cur = q.poll();
			
			if(map[cur.row][cur.col] == 2 ) {
				//System.out.println(cur.path);
				
				return new Shield(true, cur.count * 2 );
			} else if (map[cur.row][cur.col] == 3 && cur.row != row && cur.col != col) {
				if(shield[cur.row][cur.col] != 0) {
					return new Shield(false, visited[cur.row][cur.col] + shield[cur.row][cur.col]) ;
				} else {
					continue;
				}
			} 
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				int count = cur.count; 
				
				if(impossibleDir(nextRow, nextCol)) continue;
				if(map[nextRow][nextCol] == 0)  continue;
				//if(map[nextRow][nextCol] == 3) continue;
				if(map[nextRow][nextCol] == 4) continue;
				if(visited[nextRow][nextCol] != 0) continue;
				if(nRow == nextRow && nCol == nextCol) continue;
				
				String point = "("+nextRow + "," + nextCol +")";
				if(path.indexOf(point) == -1) {
					count += 1;
				} 
				
				//q.add(new Node2(nextRow, nextCol, count));
				q.add(new Node2(nextRow,nextCol,count, cur.path + "("+nextRow+"," +nextCol+")("+count+")"));
				visited[nextRow][nextCol] = visited[cur.row][cur.col] + 1;
			}				
		}
		
		return null;
	}
	
	
	static Shield dijkstra(int row, int col, int nRow, int nCol, String path) {
		
		int[][] dist = new int[N][M];
		for(int i=0; i<N; i++) {
			Arrays.fill(dist[i], Integer.MAX_VALUE);
		}
		
		PriorityQueue<Node2> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.count, o2.count));
		
		//q.add(new Node2(row,col,0));
		q.add(new Node2(row,col,0, "("+row+"," +col+")"));
		dist[row][col] = 0;
		
		while(!q.isEmpty()) {
			Node2 cur = q.poll();
			
			if(map[cur.row][cur.col] == 2 ) {
				//System.out.println(cur.path);
				return new Shield(true, dist[cur.row][cur.count]);
				//return new Shield(true, cur.count * 2 );
			} else if (map[cur.row][cur.col] == 3 && cur.row != row && cur.col != col) {
				if(shield[cur.row][cur.col] != 0) {
					return new Shield(false, dist[cur.row][cur.col] + shield[cur.row][cur.col]) ;
				} else {
					continue;
				}
			} 
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				int count = cur.count; 
				
				if(impossibleDir(nextRow, nextCol)) continue;
				if(map[nextRow][nextCol] == 0)  continue;
				//if(map[nextRow][nextCol] == 3) continue;
				if(map[nextRow][nextCol] == 4) continue;
				
				if(nRow == nextRow && nCol == nextCol) continue;
				
				int newCount = cur.count;
				
				
				String point = "("+nextRow + "," + nextCol +")";
				if(path.indexOf(point) == -1) {
					count += 1;
				} 
				
				//q.add(new Node2(nextRow, nextCol, count));
				q.add(new Node2(nextRow,nextCol,count, cur.path + "("+nextRow+"," +nextCol+")("+count+")"));
				dist[nextRow][nextCol] = dist[cur.row][cur.col] + 1;
			}				
		}
		
		return null;
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
		
		public Node(int row, int col, boolean hasShield) {
			this.row = row;
			this.col = col;
			this.hasShield = hasShield;
		}
		
		public Node(int row, int col, boolean hasShield, String path) {
			super();
			this.row = row;
			this.col = col;
			this.hasShield = hasShield;
			this.path = path;
		}
	}
	
	static class Node2 {
		int row;
		int col;
		int count;
		String path;
		
		public Node2(int row, int col, int count) {			
			this.row = row;
			this.col = col;
			this.count = count;
		}
		public Node2(int row, int col, int count, String path) {			
			this.row = row;
			this.col = col;
			this.count = count;
			this.path = path;
		}
	}
	
	static class Hold {
		int row;
		int col;
		public Hold(int row, int col) {
			super();
			this.row = row;
			this.col = col;
		}		
	}
	
	static class Shield {
		boolean meetShield;
		int count;
		public Shield(boolean meetShield, int count) {
			this.meetShield = meetShield;
			this.count = count;
		}
		
	}
}
