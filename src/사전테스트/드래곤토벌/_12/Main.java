package 사전테스트.드래곤토벌._12;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M;
	static int[][] map;
	
	static int[][] horizonIce;
	static int[][] viticalIce;
	
	static int[] directRow = {-1, 1, 0, 0};
	static int[] directCol = {0, 0, -1, 1};
	
	public static void main(String[] args) throws IOException {
		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\사전테스트\\드래곤토벌\\sample_input6.txt"));
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
		
		System.out.println(dijkstra());
		
		System.out.println("estimated : " + (System.currentTimeMillis() - start));
	}
	
	static int dijkstra() {
		int[][][] slided = new int[N][M][2];		
		int[][] dist = new int[N][M];
		for(int i=0;i<N; i++) {
			Arrays.fill(dist[i], Integer.MAX_VALUE);
		}
		dist[0][0] = 0;
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.count, o2.count));
		//q.add(new Node(0,0, 0, false, "(0,0)(0)"));
		q.add(new Node(0,0, 0, false, "(0,0)"));
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.row == N-1 && cur.col == M -1) {			
				System.out.println(cur.path);
				
				for(int i=0; i<N; i++) {
					for(int j=0; j<M; j++) {
						int num = dist[i][j] == 2147483647 ? -1 : dist[i][j];
						System.out.print(num + " ");
					}
						
					System.out.println();
				}
				
				return dist[N-1][M -1];
			}
			
			if(dist[cur.row][cur.col] < cur.count) continue;
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				boolean hasShield = cur.hasShield;
				
				
				if(impossibleDir(nextRow, nextCol)) continue;
				if(map[nextRow][nextCol] == 0) continue;
				
				int addCount = 0;
				
//				if(map[nextRow][nextCol] == 3 && !cur.hasShield) {
//					continue;
//				}
				
				if(map[nextRow][nextCol] == 3 && !cur.hasShield) {
					
					
					addCount = bfs2(cur.row, cur.col, cur.path) ;
					if(addCount == -1) continue;
					
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
					
					int nextCount = dist[cur.row][cur.col] + sCount;
					
					if(dist[newRow][newCol] < nextCount) continue;
					dist[newRow][newCol] = nextCount;
					
					//q.add(new Node(newRow, newCol, dist[newRow][newCol], false, cur.path +  "->S("+newRow+","+newCol+")("+dist[newRow][newCol]+")"));
					q.add(new Node(newRow, newCol, dist[newRow][newCol], false, cur.path +  "("+newRow+","+newCol+")"));
					
					
				} else {
					
					int nextCount =  cur.count + 1 + addCount;
					if(dist[nextRow][nextCol] <= nextCount) continue;
					
					
					if(map[nextRow][nextCol] == 2) {						
						dist[nextRow][nextCol] = nextCount;
						hasShield = true;
					} else {
						dist[nextRow][nextCol] = nextCount;
					}
					
					//q.add(new Node(nextRow, nextCol, dist[nextRow][nextCol], hasShield, cur.path +  "->("+nextRow+","+nextCol+")("+dist[nextRow][nextCol]+")"));
					q.add(new Node(nextRow, nextCol, dist[nextRow][nextCol], hasShield, cur.path +  "("+nextRow+","+nextCol+")"));
					
				}
			}			
		}
		
		return -1;
	}
	
	
	static int bfs2(int row, int col, String path) {
		
		int[][] visited = new int[N][M];
		
		ArrayDeque<Node2> q = new ArrayDeque<>();
		
		q.add(new Node2(row,col,0));
		visited[row][col] = 1;
		
		while(!q.isEmpty()) {
			Node2 cur = q.poll();
			
			if(map[cur.row][cur.col] == 2) {
				return cur.count * 2;
			}
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				int count = cur.count; 
				
				if(impossibleDir(nextRow, nextCol)) continue;
				if(map[nextRow][nextCol] == 0)  continue;
				if(map[nextRow][nextCol] == 3) continue;
				if(map[nextRow][nextCol] == 4) continue;
				if(visited[nextRow][nextCol] == 1) continue;
				if(nextRow > row) continue;
				
				String point = "("+nextRow + "," + nextCol +")";
				if(path.indexOf(point) == -1) {
					count += 1;
				}
				
				q.add(new Node2(nextRow, nextCol, count));
				visited[nextRow][nextCol] = 1;
			}				
		}
		
		return -1;
	}

	static int[][] newDistArray() {
		int[][] dist = new int[N+1][M+1];
		for(int i=0; i<N; i++) {
			Arrays.fill(dist[i], Integer.MAX_VALUE);
		}		
		return dist;
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
		
		public Node(int row, int col, int count, boolean hasShield,  String path) {			
			this.row = row;
			this.col = col;
			this.count = count;
			this.hasShield = hasShield;
			this.path = path;
		}
		public Node(int row, int col, int count, boolean hasShield) {			
			this.row = row;
			this.col = col;
			this.count = count;
			this.hasShield = hasShield;			
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
}