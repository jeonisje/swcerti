package 프로원정대심화반.day3.숫자퍼즐;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;

class UserSolution {
	int row, col;
	int[][] map;
	int[][] puzzle;
	
	HashMap<Integer, ArrayList<Node>>[] hm;
	
	int[][] hCode =  {{1, 10, 100}, {1_000, 10_000, 100_000}, {1_000_000, 10_000_000, 100_000_000}};
	
	
	int[] range = {-4, -3, -2, -1, 0};
	
	int cc;
	
	public void init(int mRows, int mCols, int[][] mCells) {
		this.row = mRows;
		this.col = mCols;
		this.map = mCells;
		
		hm = new HashMap[5];
		puzzle = new int[row][col];
		cc = 0;
		
		for(int i=0; i<5; i++) {
			hm[i] = new HashMap<>();
		}
		
		makeHashType0();
		makeHashType1();
		makeHashType2();
		makeHashType3();
		makeHashType4();
		return;
	}
	
	void makeHashType0() {
		for(int i=0; i<row; i++) {
			for(int j=0; j<col-1; j++) {
				int key = map[i][j] - map[i][j+1];   			
				ArrayList<Node> list = hm[0].getOrDefault(key, new ArrayList<>());
				list.add(new Node(i, j));
				hm[0].put(key, list);
			}
		}
	}
	//hash = hash * 10 + (cells[y][x] - cells[ny][nx]);
	void makeHashType1() {
		for(int i=0; i<row; i++) {
			for(int j=0; j<col-2; j++) {
				int key = (map[i][j] - map[i][j+1]) * hCode[0][1] -  map[i][j+2];   			
				ArrayList<Node> list = hm[1].getOrDefault(key, new ArrayList<>());
				list.add(new Node(i, j));
				hm[1].put(key, list);
			}
		}
	}
	
	void makeHashType2() {
		for(int i=0; i<row-2; i++) {
			for(int j=0; j<col; j++) {
				//int key = map[i][j] * hCode[0][0] +  map[i+1][j] * hCode[1][0] +  map[i+2][j] * hCode[2][0];
				int key = (map[i][j] - map[i+1][j]) * hCode[0][1] - map[i+2][j];
				ArrayList<Node> list = hm[2].getOrDefault(key, new ArrayList<>());
				list.add(new Node(i, j));
				hm[2].put(key, list);
			}
		}
	}
	
	void makeHashType3() {
		for(int i=0; i<row-1; i++) {
			for(int j=0; j<col-2; j++) {
				//int key = map[i][j] * hCode[0][0] +  map[i][j+1] * hCode[0][1] + map[i+1][j+1] * hCode[1][1] + map[i+1][j+2] * hCode[1][2];   			
				int key = (map[i][j] -  map[i][j+1]) * hCode[0][2] - map[i+1][j+1] * hCode[1][1] + map[i+1][j+2] * hCode[1][2];
				ArrayList<Node> list = hm[3].getOrDefault(key, new ArrayList<>());
				list.add(new Node(i, j));
				hm[3].put(key, list);
			}
		}
	}
	
	void makeHashType4() {
		for(int i=0; i<row-2; i++) {
			for(int j=0; j<col-2; j++) {
				int key = map[i][j] * hCode[0][0] 
						+ map[i+1][j] * hCode[1][0] + map[i+1][j+1] * hCode[1][1] + map[i+1][j+2] * hCode[1][2]
						+ map[i+2][j+2] * hCode[2][2];   			
				ArrayList<Node> list = hm[4].getOrDefault(key, new ArrayList<>());
				list.add(new Node(i, j));
				hm[4].put(key, list);
			}
		}
	}

	private Comparator<? super Node> ordered() {
		return (o1, o2) -> o1.y == o2.y ? Integer.compare(o1.x, o2.x) : Integer.compare(o1.y, o2.y);
	}

	public Main.Result putPuzzle(int[][] mPuzzle) {
		Main.Result ret = new Main.Result();
		ret.row = -1;
		ret.col = -1;
		
		int[] arrKey = new int[9];
		int hash = 0;
		for(int i=0; i<mPuzzle.length; i++) {
			for(int j=0; j<mPuzzle[i].length; j++) {
				hash += hCode[i][j] * mPuzzle[i][j];				
				if(mPuzzle[i][j] == 0) continue;
				arrKey[1] += hCode[i][j] * (mPuzzle[i][j]+1);
				arrKey[2] += hCode[i][j] * (mPuzzle[i][j]+2);
				arrKey[3] += hCode[i][j] * (mPuzzle[i][j]+3);
				arrKey[4] += hCode[i][j] * (mPuzzle[i][j]+4);
				arrKey[5] += hCode[i][j] * (mPuzzle[i][j]-1);
				arrKey[6] += hCode[i][j] * (mPuzzle[i][j]-2);
				arrKey[7] += hCode[i][j] * (mPuzzle[i][j]-3);
				arrKey[8] += hCode[i][j] * (mPuzzle[i][j]-4);
			}
		}
		arrKey[0] = hash;
		int type = checkType(hash);
		
		for(int i=0; i<9; i++) {		
			if(!hm[type].containsKey(arrKey[i])) continue;
			
			ArrayList<Node> list = hm[type].get(arrKey[i]);
			int de = 1;
			for(Node node : list) {
				if(!available(type, node, mPuzzle)) 
					continue;
				put(node, mPuzzle);
				ret.row = node.y;
				ret.col = node.x;
				break;
			}
			
		}
		
		
		/*
		if(hm[type].containsKey(hash)) 
			q.addAll(hm[type].get(hash));
			
		if(q.isEmpty()) return ret;
		
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			if(!available(type, cur, mPuzzle)) 
				continue;
			put(cur, mPuzzle);
			ret.row = cur.y;
			ret.col = cur.x;
			break;
		}
		*/
		
		return ret;
	}
	
	int checkType(int key) {
		int type = 0;
		if(key >= 100_000_000) {
			type = 4;
		} else if(key >= 1_000_000) {
			type = 2;
		} else if(key >= 100_000) {
			type = 3;
		} else if(key >= 100) {
			type = 1;
		} else {
			type = 0;
		}		
		
		return type;
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
				System.out.println(ret.row +"=" + ans_row + "," + ret.col + "=" + ans_col);
				if(ans_row != ret.row || ans_col != ret.col) {
					System.err.println("-------------오류---------------");
					ok = false; 
				}
				break;
			case CMD_CLR:
				userSolution.clearPuzzles();
				//System.out.println("clear");
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
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대심화반\\day3\\숫자퍼즐\\sample_input2.txt"));

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