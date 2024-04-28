package 사전테스트.드래곤토벌._09;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		//Long start = System.currentTimeMillis();
		
		//System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\사전테스트\\드래곤토벌\\sample_input5.txt"));
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
		
		//System.out.println("estimated : " + (System.currentTimeMillis() - start));
	}
	
	static int dijkstra() {
		int[][][] slided = new int[N][M][2];		
		int[][] dist = newDistArray();
		dist[0][0] = 0;
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.count, o2.count));
		//q.add(new Node(0,0, 0, false, dist , "(0,0)(0)"));
		q.add(new Node(0,0, 0, false, dist));
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.row == N-1 && cur.col == M -1) {			
				//System.out.println(cur.path);
				return cur.dist[N-1][M -1];
			}
			
			if(cur.dist[cur.row][cur.col] < cur.count) continue;
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				
				if(impossibleDir(nextRow, nextCol)) continue;
				if(map[nextRow][nextCol] == 0) continue;
				if(map[nextRow][nextCol] == 3 && !cur.hasShield) continue;
				
				int[][] curDist =cur.dist;
 			
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
					
					int nextCount = curDist[cur.row][cur.col] + sCount;
					
					if(curDist[newRow][newCol] < nextCount) continue;
					curDist[newRow][newCol] = nextCount;
					q.add(new Node(newRow, newCol, curDist[newRow][newCol], false, curDist));
					//q.add(new Node(newRow, newCol, curDist[newRow][newCol], false, curDist, cur.path +  "->S("+newRow+","+newCol+")("+curDist[newRow][newCol]+")"));
					
					
				} else {
					
					int nextCount =  cur.count + 1;
					if(curDist[nextRow][nextCol] <= nextCount) continue;
					
					
					if(map[nextRow][nextCol] == 2) {
						curDist = newDistArray();
						
						curDist[nextRow][nextCol] = nextCount;
						hasShield = true;
					} else {
						curDist[nextRow][nextCol] = nextCount;
					}
					q.add(new Node(nextRow, nextCol, curDist[nextRow][nextCol], hasShield, curDist));
					//q.add(new Node(nextRow, nextCol, curDist[nextRow][nextCol], hasShield, curDist, cur.path +  "->("+nextRow+","+nextCol+")("+curDist[nextRow][nextCol]+")"));
					
				}
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
		int[][] dist;

		String path;
		
		public Node(int row, int col, int count, boolean hasShield, int[][] dist, String path) {			
			this.row = row;
			this.col = col;
			this.count = count;
			this.hasShield = hasShield;
			this.dist = dist;
			this.path = path;
		}
		public Node(int row, int col, int count, boolean hasShield, int[][] dist) {			
			this.row = row;
			this.col = col;
			this.count = count;
			this.hasShield = hasShield;
			this.dist = dist;
			
		}
	}
}