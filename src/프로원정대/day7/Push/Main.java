package 프로원정대.day7.Push;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	int N;
	int[][] map;
	
	int[] directRow = {-1, 0, 1, 0};
	int[] directCol = {0, 1, 0, -1};
	
	int[] directRowforPerson = {1, 0, -1, 0};
	int[] directColforPerson = {0, -1, 0, 1};
	
	int[][][] visited;
	int[][] visited2;
	int cc1;
	int cc2;

	public void init(int N, int[][] mMap){
		this.N = N;
		this.map = mMap;
		
		this.visited = new int[4][N][N];
		this.visited2 = new int[N][N];
		
		cc1 = 0;
		cc2 = 0;
	    return;
	}
	 
	public int push(int mRockR, int mRockC, int mDir, int mGoalR, int mGoalC) {
	    return ff(mRockR, mRockC, mDir, mGoalR, mGoalC);
	}
	
	int ff(int startRow, int startCol, int dir,  int endRow, int endCol) {
		cc1++;	
		
		int pRow = startRow + directRow[dir];
		int pCol = startCol + directCol[dir];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		
		//q.add(new Node(startRow, startCol, 0, pRow, pCol, "(" +startRow + "," +startCol+")"));		
		q.add(new Node(startRow, startCol, 0, pRow, pCol));
		//visited[dir][startRow][startCol] = cc1;
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			
			if(cur.row == endRow &&  cur.col == endCol) {
				//System.out.println(cur.path);
				return cur.cost;
			}
			
			
			//System.out.println(cur.path);
		
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				int rowP = cur.row + directRowforPerson[i];
				int colP = cur.col + directColforPerson[i];
				
				if(map[nextRow][nextCol] == 1) 
					continue;
				if(map[rowP][colP] == 1) 
					continue;
				if(visited[i][nextRow][nextCol] == cc1) 
					continue;
				
				if(!possible(cur.row, cur.col, cur.pRow, cur.pCol, rowP, colP)) continue;
				
				visited[i][nextRow][nextCol] = cc1;
				//q.add(new Node(nextRow, nextCol, cur.cost + 1, cur.row, cur.col,  cur.path + "(" +nextRow + "," +nextCol+")"));
				q.add(new Node(nextRow, nextCol, cur.cost + 1, cur.row, cur.col));
			}
		}
			
		
		
		
		return -1;
	}
	
	boolean possible(int rr, int rc, int pr, int pc, int tr, int tc) {
		if(pr == tr && pc == tc) 
			return true;
		
		cc2++;
		
		ArrayDeque<Persion> q = new ArrayDeque<>();
		q.add(new Persion(pr, pc));
		visited2[pr][pc] = cc2;
		
		while(!q.isEmpty()) {
			Persion cur = q.remove();
			if(cur.row == tr && cur.col == tc) {
				return true;
			}
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				
			
				if(nextRow == rr && nextCol == rc) 
					continue;
				if(map[nextRow][nextCol] == 1) 
					continue;
				if(visited2[nextRow][nextCol] == cc2) 
					continue;
				
				q.add(new Persion(nextRow, nextCol));
				visited2[nextRow][nextCol] = cc2;
			}
				
		}
		
		
		return false;
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
		int cost;
		int pRow;
		int pCol;
		String path;
		public Node(int row, int col, int cost, int pRow, int pCol, String path) {
			this.row = row;
			this.col = col;
			this.cost = cost;
			this.pRow = pRow;
			this.pCol = pCol;
			this.path = path;
		}
		
		public Node(int row, int col, int cost, int pRow, int pCol) {
			this.row = row;
			this.col = col;
			this.cost = cost;
			this.pRow = pRow;
			this.pCol = pCol;
		}
	}
	
	class Persion {
		int row;
		int col;
		public Persion(int row, int col) {		
			this.row = row;
			this.col = col;
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
		//if(ans!=ret) System.err.println("====================오류========================");
		//System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
	}

	
	public static void main(String[] args) throws Exception {
		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대\\day7\\Push\\sample_input.txt"));

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