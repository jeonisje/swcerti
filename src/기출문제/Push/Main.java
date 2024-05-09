package 기출문제.Push;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	int N;
	int[][] map;
	
	/*
	int[] directRow = {-1, 0, 1, 0};
	int[] directCol = {0, 1, 0, -1};
	int[] directRowforPerson = {1, 0, -1, 0};
	int[] directColforPerson = {0, -1, 0, 1};
	*/
	
	int[] directRow = {1, 0, -1, 0};
	int[] directCol = {0, -1, 0, 1};
	
	int[] directRowforPerson = {-1, 0, 1, 0};
	int[] directColforPerson = {0, 1, 0, -1};

	public void init(int N, int[][] mMap){
		this.N = N;
		this.map = mMap;
	    return;
	}
	 
	public int push(int mRockR, int mRockC, int mDir, int mGoalR, int mGoalC) {
	    return dijkstra(mRockR, mRockC, mDir, mGoalR, mGoalC);
	}
	
	int dijkstra(int startRow, int startCol, int dir, int endRow, int endCol) {
		int[][][] dist = new int[N+1][N+1][4];
		for(int i=0; i<N+1; i++) {
			for(int j=0; j<N+1; j++) {
				Arrays.fill(dist[i][j], 100_000);
			}
		}
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> o1.cost==o2.cost ? Integer.compare(o1.dirChange , o2.dirChange) : Integer.compare(o1.cost, o2.cost));
		//q.add(new Node(startRow, startCol, dir, 0));
		q.add(new Node(startRow, startCol, dir, 0, 0, "("+startRow + ", " + startCol +" : "+dir+")(0)"));
		dist[startRow][startCol][dir] = 0;
		String path = "";
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			path = cur.path;
			if(cur.row == endRow && cur.col == endCol) {
				System.out.println(cur.path);
				return dist[cur.row][cur.col][cur.dir];
			}
			
			if(dist[cur.row][cur.col][cur.dir] < cur.cost) continue;
			
			
			int nextRow = cur.row + directRow[cur.dir];
			int nextCol = cur.col + directCol[cur.dir];
			
			if(possibleDirect(nextRow, nextCol)) {			
				int nextCost = dist[cur.row][cur.col][cur.dir] + 1;
				
				dist[nextRow][nextCol][cur.dir] = nextCost;
				//q.add(new Node(nextRow, nextCol, i, dist[nextRow][nextCol][i]));
				q.add(new Node(nextRow, nextCol, cur.dir, 0, dist[nextRow][nextCol][cur.dir], cur.path + " => ("+nextRow + ", " + nextCol +" : "+cur.dir+")("+nextCost+")"));
			} else {
				for(int i=0; i<4; i++) {
					nextRow = cur.row + directRow[i];
					nextCol = cur.col + directCol[i];
					
					if(map[nextRow][nextCol] == 1) continue;
					
					if(!possibleDirect2(cur.row, cur.col, i)) continue;
					
					int nextCost = dist[cur.row][cur.col][cur.dir] + 1;
					if(dist[nextRow][nextCol][i] <= nextCost) continue;
					dist[nextRow][nextCol][i] = nextCost;
					
					 
					
					//q.add(new Node(nextRow, nextCol, i, dist[nextRow][nextCol][i]));
					q.add(new Node(nextRow, nextCol, i, 1, dist[nextRow][nextCol][i], cur.path + " => ("+nextRow + ", " + nextCol +" : "+i+")("+nextCost+")"));
				}
			}
			
			
			
			
		}
		
		
		return -1;
	}
	
	boolean possibleDirect(int row, int col) {
		
		if(map[row][col] == 1) return false;

		int evenCount = 0;
		int oddCount = 0;
		
		for(int i=0; i<4; i++) {
			int checkRow = row + directRow[i];
			int checkCol = col + directCol[i];
			
			if(map[checkRow][checkCol] == 1) {
				if(i % 2 == 0) evenCount++;
				else oddCount++;
			}
		}
		
		if(evenCount >= 1 && oddCount >= 1) return false;
		return true;
	}
	
	boolean possibleDirect2(int row, int col, int dir) {
		
		if(map[row][col] == 1) return false;

		int personRow = row + directRowforPerson[dir];
		int personCol = col + directColforPerson[dir];
		
		if(personRow < 0 || personCol < 0 || personCol >= N || personRow >= N) return false;
		if(map[personRow][personCol] == 1) return false;
		
		return true;
	}	
	
	
	
	class Node {
		int row;
		int col;
		int dir;
		int dirChange;
		int cost;
		String path;
		public Node(int row, int col, int dir, int dirChange, int cost) {			
			this.row = row;
			this.col = col;
			this.dir = dir;
			this.dirChange = dirChange;
			this.cost = cost;			
		}
		public Node(int row, int col, int dir, int dirChange, int cost, String path) {
			this.row = row;
			this.col = col;
			this.dir = dir;
			this.dirChange = dirChange;
			this.cost = cost;
			this.path = path;
		}
		
		
	}
}

public class Main {
	private static final int CMD_INIT = 100;
	private static final int CMD_PUSH = 200;

	private static UserSolution userSolution = new UserSolution();
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {
		
		int cmd, ans, ret;
		int N, r, c, dir, r2, c2;
		int Q = 0;
		boolean okay = false;
		int[][] region = new int[30][30]; 
		
		st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {
			
			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) {
			case CMD_INIT:
				N = Integer.parseInt(st.nextToken()); 
				for(int i = 0; i < N; i++) {
					st = new StringTokenizer(br.readLine());
					for(int j = 0; j < N; j++) {
						region[i][j] = Integer.parseInt(st.nextToken());
					}
				}
				userSolution.init(N, region); 
				okay = true;
				break; 
			case CMD_PUSH:
				r = Integer.parseInt(st.nextToken());
				c = Integer.parseInt(st.nextToken());
				dir = Integer.parseInt(st.nextToken());
				r2 = Integer.parseInt(st.nextToken());
				c2 = Integer.parseInt(st.nextToken());
				ret = userSolution.push(r, c, dir, r2, c2); 
				ans = Integer.parseInt(st.nextToken());
				print(q, "push", ans, ret, r, c, dir, r2, c2);
				if(ret != ans)
					okay = false;
				break;
			default:
				okay = false;
				break;
			}
		}

		return okay;
	}
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		if(ans!=ret) System.err.println("====================오류========================");
		System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
	}

	
	public static void main(String[] args) throws Exception {
		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//기출문제//Push//sample_input2.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());
		
		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		 System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}