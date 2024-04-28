package 사전테스트.드래곤토벌._06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;


public class Main {
	static final int MAX = 1_000_000;
	
	static int N, M;
	static int[][] map;
	 
	static int[][] iceMap_H;
	static int[][] iceMap_V;
	
	static int[] directRow = {-1, 1, 0, 0};
	static int[] directCol = {0, 0, -1, 1};
	
	static int[] parent_H;
	static int[] parent_V;
	static int iceId;
	static Ice[] horizonIce;
	static Ice[] verticalIce;
	
	public static void main(String[] args) throws IOException {
		//Long start = System.currentTimeMillis();
		
		//System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\사전테스트\\드래곤토벌\\sample_input7.txt"));
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
		parent_H = new int[MAX];
		parent_V = new int[MAX];
		for(int i = 0; i<MAX; i++) {
			parent_H[i] = i;
			parent_V[i] = i;
		}
		
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
							union(iceMap_H[i][j - 1] , iceId, parent_H, horizonIce);
						}
					}
					
					if(i != 0) {
						if(iceMap_V[i - 1][j] != 0) {
							union(iceMap_V[i-1][j] , iceId, parent_V, verticalIce);
						}
					}
					
								
				} else if(map[i][j] == 1 || map[i][j] == 2) {
					if(j != 0) {
						if(map[i][j-1] == 4) {
							iceId++;								
							horizonIce[iceId] = new Ice(iceId, i, i, j, j, 1);
							iceMap_H[i][j] = iceId;
							union(iceMap_H[i][j-1] , iceId, parent_H, horizonIce);
						}
					}	
					
					if(i != 0) {
						if(map[i-1][j] == 4) {
							iceId++;								
							verticalIce[iceId] = new Ice(iceId, i-1, i-1, j, j, 1);
							iceMap_V[i-1][j] = iceId;
							union(iceMap_V[i-1][j] , iceId, parent_V, verticalIce);
						}
					}		
				}
			}
		}
		
		
		
		
		System.out.println(bfs());
		//System.out.println("estimated : " + (System.currentTimeMillis() - start));
		
	}
	
	
	
	
	
	static int bfs() {
		int[][] visited = new int[N][M];
		int[][][] slided = new int[N][M][2];
		
		int[] visitedHorizon = new int[MAX];
		int[] visitedVertical = new int[MAX];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(0,0, 0, false, visited , "(0,0)(0)"));
		//visited[0][0] = 1;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.row == N-1 && cur.col == M-1) {
				//System.out.println(cur.path);
				return cur.visited[cur.row][cur.col];
			}
			
			//if(cur.visited[cur.row][cur.col] == 1) continue;
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];			
				
				if(impossibleDir(nextRow, nextCol)) continue;
				if(cur.visited[nextRow][nextCol] != 0) continue;
				if(map[nextRow][nextCol] == 0) continue;
				if(map[nextRow][nextCol] == 3 && !cur.hasShield) continue;
				
				
				int dir = 0; // 0 상하 , 1 좌우
				if(directRow[i] == 0)  {
					dir = 1;
				}
				
				if(slided[nextRow][nextCol][dir] == 1) continue;
				
				int[][] curVisited =cur.visited; 			
				boolean hasShield = cur.hasShield;
				
				if(map[nextRow][nextCol] == 4) {		
					hasShield = false;
					
					int iceId = 0;
					int size = 0;
					int newRow = 0;
					int newCol = 0;
					ArrayList<Point> nextPoint = new ArrayList<>();
					if(dir == 0) {
						iceId = find(iceMap_V[nextRow][nextCol], parent_V);
						
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
						iceId = find(iceMap_H[nextRow][nextCol], parent_H);
						
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
						curVisited[p.row][p.col] = curVisited[cur.row][cur.col] + p.size;
						//q.add(new Node(p.row, p.col, curVisited[p.row][p.col], hasShield, curVisited, cur.path +  "->("+p.row+","+p.col+")("+curVisited[p.row][p.col]+")"));
						q.add(new Node(p.row, p.col, curVisited[p.row][p.col], hasShield, curVisited));
						
					}
					
				} else {
					//int nextCount =  cur.count + 1;
					//if(curVisited[nextRow][nextCol] <= nextCount) continue;
					
					
					if(map[nextRow][nextCol] == 2) {
						curVisited = new int[N][M];
						
						curVisited[nextRow][nextCol] = cur.visited[cur.row][cur.col] + 1;
						hasShield = true;
					} else {
						curVisited[nextRow][nextCol] = curVisited[cur.row][cur.col] + 1;
					}
					
					//q.add(new Node(nextRow, nextCol, curVisited[nextRow][nextCol], hasShield, curVisited, cur.path +  "->("+nextRow+","+nextCol+")("+curVisited[nextRow][nextCol]+")"));
					q.add(new Node(nextRow, nextCol, curVisited[nextRow][nextCol], hasShield, curVisited));
					
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
	
	static int find(int a, int[] parent) {
		if(a == parent[a]) return a;
		return parent[a] = find(parent[a], parent);
	}
	
	static void union(int a, int b, int[] parent,  Ice[] iceInfo) {
		int pa = find(a, parent);
		int pb = find(b, parent);
		
		if(pa == pb) return;
		
		int size = iceInfo[pa].size + iceInfo[pb].size;
		int startRow = Math.min(iceInfo[pa].startRow, iceInfo[pb].startRow);
		int endRow = Math.max(iceInfo[pa].endRow, iceInfo[pb].endRow);
		int startCol = Math.min(iceInfo[pa].startCol, iceInfo[pb].startCol);
		int endCol = Math.max(iceInfo[pa].endCol, iceInfo[pb].endCol);
		
		iceInfo[pa] = new Ice(pa, startRow, endRow, startCol, endCol, size);
		
		parent[pb] = pa;
	}

	
	static boolean impossibleDir(int row, int col) {
		if(row < 0 || col < 0 || row >= N || col >= M) return true;
		return false;
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
	
	static class Node {
		int row;
		int col;	
		int count;
		boolean hasShield;
		
		int[][] visited;
		String path;		
		
		public Node(int row, int col, int count, boolean hasShield, int[][] visited) {
			super();
			this.row = row;
			this.col = col;
			this.count = count;
			this.hasShield = hasShield;
			this.visited = visited;
		}
		
		
		public Node(int row, int col, int count, boolean hasShield, int[][] visited, String path) {
			super();
			this.row = row;
			this.col = col;
			this.count = count;
			this.hasShield = hasShield;
			this.visited = visited;
			this.path = path;
		}
	}
}
