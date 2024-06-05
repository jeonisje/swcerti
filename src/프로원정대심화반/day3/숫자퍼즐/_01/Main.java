package 프로원정대심화반.day3.숫자퍼즐._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

class UserSolution {
	int row, col;
	int[][] map;
	int[][] puzzle;
	
	HashMap<Integer, ArrayList<Node>>[] hm;
	
	int[][] hCode =  {{1, 10, 100}, {1_000, 10_000, 100_000}, {1_000_000, 10_000_000, 100_000_000}};	
	
	int[][][] dirs = {
			{{0,0},{ 0,1}}, //0번 shape에 대한 offset 	
			{{0,0},{ 0,1}, {0, 2}}, 
			{{0,0}, {1,0}, {2,0}},
			{{0, 0}, {0, 1}, {1, 1}, {1, 2}},
			{{0, 0}, {1, 0}, {1, 1}, {1, 2}, {2,2}}
		};
	
	int cc;
	
	int getHash(int y, int x, int shape, int[][] cells) {
		int hash = 0; 
		// 이 shape의 블록의 개수 및 방향 만큼 좌표를 체크해볼겁니다.
		for(int i = 0; i < dirs[shape].length; i++) {
			int ny = y + dirs[shape][i][0];
			int nx = x + dirs[shape][i][1]; 
			// index가 벗어나는 위치 = 어차피 못놓는 위치 => hash가 생성되면 안되니
			if(ny < 0 || nx < 0 || ny >= row || nx >= col)
				return Integer.MAX_VALUE; 
			hash = hash * 10 + (cells[y][x] - cells[ny][nx]);
		}
		return hash;
	}
	
	public void init(int mRows, int mCols, int[][] mCells) {
		hm = new HashMap[5];
		row = mRows;
		col = mCols; 
		for(int i = 0; i < 5; i++) 
			hm[i] = new HashMap<>();
		// 모든 위치에서 모든 모양에 대한 hashing
		for(int i = 0; i < mRows; i++) {
			for(int j = 0; j < mCols; j++) {
				// 모양 5개
				for(int k = 0; k < 5; k++) {
					//(i, j) 좌표에서 k모양에 대한 hash
					int hash = getHash(i, j, k, mCells); 
					if(hash == Integer.MAX_VALUE)
						continue;
					// 이제 이 모양의 이 hash에 등록을 해줍니다.
					if(hm[k].get(hash) == null)
						hm[k].put(hash, new ArrayList<>());
					hm[k].get(hash).add(new Node(i, j)); 
					//int de = 1; 
				}
			}
		}
		puzzle = new int[mRows][mCols];
		cc = 1; 
		return;
	}

	public Main.Result putPuzzle(int[][] mPuzzle) {
		Main.Result ret = new Main.Result();
		ret.row = -1;
		ret.col = -1;
		
		int type = checkType(mPuzzle);
		
		int hash = getHash(0, 0, type, mPuzzle);		
		
		if(!hm[type].containsKey(hash)) return ret;
		
		for(Node node : hm[type].get(hash)) {			
			if(!available(type, node, mPuzzle)) 
				continue;
			put(node, mPuzzle);
			ret.row = node.y;
			ret.col = node.x;
			break;
		}		
		
		return ret;
	}
	
	int checkType(int[][] mPuzzle) {
		if(mPuzzle[2][2] != 0) return 4;
		else if(mPuzzle[1][2] != 0) return 3;
		else if(mPuzzle[2][0] != 0) return 2;
		else if(mPuzzle[0][2] != 0) return 1;
		return 0;
		
	}
	
	
	boolean available(int type, Node node, int[][] mPuzzle) {
		for(int i=node.y; i<=node.y+2; i++) {
			for(int j=node.x; j<=node.x+2; j++) {
				if(mPuzzle[i - node.y][j - node.x] == 0) continue;
				if(puzzle[i][j] == 1) 
					return false;
			}
		}	
		
		return true;
	}
	
	void put(Node node, int[][] mPuzzle) {		
		for(int i=node.y; i<=node.y+2; i++) {
			for(int j=node.x; j<=node.x+2; j++) {
				if(mPuzzle[i - node.y][j - node.x] == 0) continue;
				puzzle[i][j] = 1;
			}
		}	
	}
	

	public void clearPuzzles() {
		puzzle = new int[row][col];
		return;
	}
	
	class Node {
		int y;
		int x;
		public Node(int y, int x) {		
			this.y = y;
			this.x = x;
		}
	}
}

public class Main {
	private static final int CMD_INIT = 1;
	private static final int CMD_PUT = 2;
	private static final int CMD_CLR = 3;

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	public static final class Result {
		int row;
		int col; 
		Result() {
			this.row = -1;
			this.col = -1; 
		}
	}

	private static boolean run(BufferedReader br) throws Exception {

		int query_num;
		st = new StringTokenizer(br.readLine(), " ");
		query_num = Integer.parseInt(st.nextToken());
		boolean ok = false;

		for (int q = 0; q < query_num; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int query = Integer.parseInt(st.nextToken());

			switch (query) {

			case CMD_INIT:
				int mRows = Integer.parseInt(st.nextToken());
				int mCols = Integer.parseInt(st.nextToken());
				int[][] mCells = new int[mRows][mCols];
				for(int i = 0; i < mRows; i++) {
					st = new StringTokenizer(br.readLine());
					for(int j = 0; j < mCols; j++) {
						mCells[i][j] = Integer.parseInt(st.nextToken());
					}
				}
				userSolution.init(mRows, mCols, mCells);
                ok = true; 
				break;
			case CMD_PUT:
				String strPuzzle;
				int[][]mPuzzle = new int[3][3];
				strPuzzle = st.nextToken();
				int cnt = 0;
				for(int i = 0; i < 3; i++) {
					for(int j = 0; j < 3; j++) {
						mPuzzle[i][j] = strPuzzle.charAt(cnt) - '0'; 
						cnt++; 
					}
				}
				Result ret = userSolution.putPuzzle(mPuzzle); 
				int ans_row = Integer.parseInt(st.nextToken());
				int ans_col = Integer.parseInt(st.nextToken());
				if(ans_row != ret.row || ans_col != ret.col)
					ok = false; 
				break;
			case CMD_CLR:
				userSolution.clearPuzzles();
				break;
			default:
				ok = false;
				break;
			}
		}
		return ok;
	}
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대심화반\\day3\\숫자퍼즐\\sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}