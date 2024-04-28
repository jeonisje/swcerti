package 사전테스트.드래곤토벌._15;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	
	static final int MAX = 500_000;
	
	static int N, M;
	static int[][] map;
	
	static int[][] iceMap_H;
	static int[][] iceMap_V;
	
	static int[] directRow = {-1, 1, 0, 0};
	static int[] directCol = {0, 0, -1, 1};
	
	static int iceId;	
	static Ice[] horizonIce;
	static Ice[] verticalIce;
	
	public static void main(String[] args) throws IOException {
		//Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\사전테스트\\드래곤토벌\\sample_input6.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[N][M];
		iceMap_H = new int[N][M];
		iceMap_V = new int[N][M];
		
		
		iceId = 0;
		horizonIce = new Ice[MAX];
		verticalIce = new Ice[MAX];
		
		for(int i=0; i<N; i++) {
			st = new StringTokenizer(br.readLine());
			
			for(int j=0; j<M; j++) {
				map[i][j] =  Integer.parseInt(st.nextToken());
				
				if(map[i][j] == 4) {
					iceId++;					
					
					horizonIce[iceId] = new Ice(iceId, i, i, j, j, 1);
					verticalIce[iceId] = new Ice(iceId, i, i, j, j, 1);
					iceMap_H[i][j] = iceId;
					iceMap_V[i][j] = iceId;
					
					if(j != 0) {
						if(iceMap_H[i][j - 1] != 0) {							
							int curId = iceMap_H[i][j - 1];
							iceMap_H[i][j] = curId;
							Ice ice = horizonIce[curId];
							horizonIce[curId] = new Ice(curId, i, i, ice.startCol, j, ice.size + 1);
						}
					}
					
					if(i != 0) {
						if(iceMap_V[i - 1][j] != 0) {
							int curId = iceMap_V[i-1][j];
							iceMap_V[i][j] = curId;
							Ice ice = verticalIce[curId];
							verticalIce[curId] = new Ice(curId, ice.startRow, i, j, j, ice.size + 1);
						}
					}
								
				} else if(map[i][j] == 1 || map[i][j] == 2) {
					if(j != 0) {
						if(map[i][j-1] == 4) {
							iceId++;								
							horizonIce[iceId] = new Ice(iceId, i, i, j, j, 1);
							
							int curId = iceMap_H[i][j - 1];
							iceMap_H[i][j] = curId;
							Ice ice = horizonIce[curId];
							horizonIce[curId] = new Ice(curId, i, i, ice.startCol, j, ice.size + 1);
						}
					}	
					
					if(i != 0) {
						if(map[i-1][j] == 4) {
							iceId++;								
							verticalIce[iceId] = new Ice(iceId, i-1, i-1, j, j, 1);
							
							int curId = iceMap_V[i-1][j];
							iceMap_V[i][j] = curId;
							Ice ice = verticalIce[curId];
							verticalIce[curId] = new Ice(curId, ice.startRow, i, j, j, ice.size + 1);
						}
					}		
				}
			}
		}
		
		System.out.println(bfs());
		//System.out.println("estimated : " + (System.currentTimeMillis() - start));
		
		
	}
	
	static int bfs() {
		int[][][] visited = new int[N][M][2];
		int[][][] slided = new int[N][M][2];
		
		int[] visitedHorizon = new int[MAX];
		int[] visitedVertical = new int[MAX];
		
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
				
				int dir = 0; // 0 상하 , 1 좌우
				if(directRow[i] == 0)  {
					dir = 1;
				}
				
				if(slided[nextRow][nextCol][dir] == 1) continue;
 			
				int hasShield = cur.hasShield;
				if(map[nextRow][nextCol] == 4) {		
					hasShield = 0;
					
					int iceId = 0;
					int size = 0;
					int newRow = 0;
					int newCol = 0;
					ArrayList<Point> nextPoint = new ArrayList<>();
					if(dir == 0) {
						iceId = iceMap_V[nextRow][nextCol];
						
						if(visitedVertical[iceId] == 1) continue;
						
						Ice ice = verticalIce[iceId];
						
						newRow = ice.endRow;
						newCol = ice.endCol;
						size = ice.size;
						if(ice.startRow == nextRow) {							
							if(map[newRow][newCol] == 1 || map[newRow][newCol] == 2) {
								slided[newRow-1][newCol][dir] = 1;
							} else {
								slided[newRow][newCol][dir] = 1;
							}
							
							nextPoint.add(new Point(newRow, newCol, size));
						} else {
							// 중간에 들어왔을때
							int pSize = ice.size - (ice.endRow - nextRow) - 1;		
							int nSize = ice.size - (nextRow - ice.startRow);		
							
							if(pSize > 1) {
								nextPoint.add(new Point(ice.startRow, newCol, pSize));
							}
							if(nSize > 1) {
								nextPoint.add(new Point(ice.endRow, newCol, nSize));
							}					
						}
						
						visitedVertical[iceId] = 1;
					} else {
						iceId = iceMap_H[nextRow][nextCol];
						
						if(visitedHorizon[iceId] == 1) continue;
						
						Ice ice = horizonIce[iceId];
						
						newRow = ice.endRow;
						newCol = ice.endCol;
						size = ice.size;
						if(ice.startCol == nextCol) {
							
							
							if(map[newRow][newCol] == 1 || map[newRow][newCol] == 2) {
								slided[newRow][newCol-1][dir] = 1;
							} else {
								slided[newRow][newCol][dir] = 1;
							}
							nextPoint.add(new Point(newRow, newCol, size));
						} else {
							// 중간에 들어왔을때
						
							int pSize = ice.size - (ice.endCol - nextCol) - 1;		
							int nSize = ice.size - nextCol;		
							
						
							if(pSize > 1) {
								nextPoint.add(new Point(newRow, ice.startCol, pSize));
							}
							if(nSize > 1) {
								nextPoint.add(new Point(newRow, ice.endCol, nSize));
							}	
						}
						visitedHorizon[iceId] = 1;
					}
					
					for(Point p : nextPoint) {
						//if(curVisited[p.row][p.col] <= cur.count + p.size) continue;
						//curVisited[p.row][p.col] = cur.count + p.size;
						visited[p.row][p.col][0] = visited[cur.row][cur.col][cur.hasShield] + p.size;
						//q.add(new Node(p.row, p.col, curVisited[p.row][p.col], hasShield, curVisited, cur.path +  "->("+p.row+","+p.col+")("+curVisited[p.row][p.col]+")"));
						q.add(new Node(p.row, p.col, 0));
					}
						
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
	
	static class Ice {
		int num;
		int startRow;
		int endRow;
		int startCol;
		int endCol;
		int size;
		public Ice(int num, int startRow, int endRow, int startCol, int endCol, int size) {			
			this.num = num;
			this.startRow = startRow;
			this.endRow = endRow;
			this.startCol = startCol;
			this.endCol = endCol;
			this.size = size;
		}
		
	}
	
	static class Point {
		int row;
		int col;
		int size;
		
		public Point(int row, int col, int size) {
			this.row = row;
			this.col = col;
			this.size = size;
		}		
	}
}