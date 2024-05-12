package 기출문제.감시탑;


import java.io.*;
import java.util.*;

class UserSolution {
	
	final int SUMMARY_UNIT = 1000;;
	
	int N;
	ArrayList<Tower>[][][] sMap;
	int[][] removed;
	
	int sSize;
	
	
	public void init(int N) {
		this.N = N;
		
		sSize = N / SUMMARY_UNIT + 1;
		
		sMap = new ArrayList[sSize][sSize][6];
		for(int i=0; i<sSize; i++) {
			for(int j=0;j <sSize; j++) {
				for(int k=1; k<6; k++) {
					sMap[i][j][k] = new ArrayList<>();
				}
			}
		}
		
		removed = new int[N+1][N+1];
		
		return;
	}

	public void buildTower(int mRow, int mCol, int mColor) {
		Tower tower = new Tower(mRow, mCol);
		
		removed[mRow][mCol] = 0;
		
		int sy = mRow / SUMMARY_UNIT;
		int sx = mCol / SUMMARY_UNIT;
		
		sMap[sy][sx][mColor].add(tower);
		
		return; 
	}
	
	public void removeTower(int mRow, int mCol) {		
		removed[mRow][mCol] = 1;
	}
	
	public int countTower(int mRow, int mCol, int mColor, int mDis) {
		int y = mRow / SUMMARY_UNIT;
		int x = mCol / SUMMARY_UNIT;
		
		int yDis1 = mRow - mDis / SUMMARY_UNIT;
		int yDis2 = mRow + mDis / SUMMARY_UNIT;
		
		int xDis = Math.abs(mCol - mDis) / SUMMARY_UNIT;
		
		int startRow = yDis1 == y ? y  : Math.max(0, yDis1);		
		int endRow =  yDis2 == y ? y  : Math.min(sSize - 1, yDis2);
		
		
		
		return 0; 
	}

	public int getClosest(int mRow, int mCol, int mColor) {
		return 0; 
	}
	
	class Tower {
		int row;
		int col;
		public Tower(int row, int col) {		
			this.row = row;
			this.col = col;
		}
	}
}


public class Main {
	private static final int CMD_INIT 	= 0;
	private static final int CMD_BUILD 	= 1;
	private static final int CMD_REMOVE = 2;
	private static final int CMD_COUNT	= 3;
	private static final int CMD_GET	= 4;
	
	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	private static boolean run(BufferedReader br) throws Exception {

		int cmd, n, row, col, color, dis, ret; 
		int ans; 
		int Q = 0; 
		boolean okay = false; 
		
		st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				n = Integer.parseInt(st.nextToken());
				userSolution.init(n); 
                okay = true; 
				break;
				
			case CMD_BUILD:
				row = Integer.parseInt(st.nextToken());
				col = Integer.parseInt(st.nextToken());
				color = Integer.parseInt(st.nextToken());
				userSolution.buildTower(row, col, color);
				print(q, "buildTower", q, q, row, col, color);
				break;
				
			case CMD_REMOVE:
				row = Integer.parseInt(st.nextToken());
				col = Integer.parseInt(st.nextToken());
				userSolution.removeTower(row, col); 
				print(q, "removeTower", q, q, row, col);
				break;
				
			case CMD_COUNT:
				row = Integer.parseInt(st.nextToken());
				col = Integer.parseInt(st.nextToken());
				color = Integer.parseInt(st.nextToken());
				dis = Integer.parseInt(st.nextToken());
				ret = userSolution.countTower(row, col, color, dis); 
				ans = Integer.parseInt(st.nextToken());
				print(q, "countTower", ans, ret, row, col, color, dis);
				if(ret != ans)
					okay = false; 
				break;
				
			case CMD_GET:
				row = Integer.parseInt(st.nextToken());
				col = Integer.parseInt(st.nextToken());
				color = Integer.parseInt(st.nextToken());
				ret = userSolution.getClosest(row, col, color); 
				ans = Integer.parseInt(st.nextToken());
				print(q, "getClosest", ans, ret, row, col, color);
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
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception {
		// System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\감시탑\\sample_input.txt"));


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