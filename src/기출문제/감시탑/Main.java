package 기출문제.감시탑;


import java.io.*;
import java.util.*;

class UserSolution {
	
	final int SUMMARY_UNIT = 100;
	
	int N;
	ArrayList<Tower>[][][] sMap;
	int[][] removed;
	
	int[] countByColor;
	
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
		countByColor = new int[6];
		
		return;
	}

	public void buildTower(int mRow, int mCol, int mColor) {
		Tower tower = new Tower(mRow, mCol);
		
		removed[mRow][mCol] = 0;
		
		int sy = mRow / SUMMARY_UNIT;
		int sx = mCol / SUMMARY_UNIT;
		
		sMap[sy][sx][mColor].add(tower);
		
		countByColor[0]++;
		countByColor[mColor]++;
		
		
		return; 
	}
	
	public void removeTower(int mRow, int mCol) {		
		removed[mRow][mCol] = 1;
	}
	
	public int countTower(int mRow, int mCol, int mColor, int mDis) {
		int count = 0;
		
		int y = mRow / SUMMARY_UNIT;
		int x = mCol / SUMMARY_UNIT;
		
		int yDis1 = (mRow - mDis) / SUMMARY_UNIT;
		int yDis2 = (mRow + mDis) / SUMMARY_UNIT;
		
		int xDis1 = (mCol - mDis) / SUMMARY_UNIT;
		int xDis2 = (mCol + mDis) / SUMMARY_UNIT;
		
		int startRow = yDis1 == y ? y  : Math.max(0, yDis1);		
		int endRow =  yDis2 == y ? y  : Math.min(sSize - 1, yDis2);
		
		int startCol = xDis1 == x ? x  : Math.max(0, xDis1);		
		int endCol =  xDis2 == x ? x  : Math.min(sSize - 1, xDis2);		
		
	
		int startColor = mColor;
		int endColor = mColor;
		if(mColor == 0) {
			startColor = 1;
			endColor = 5;
		}
		
		//System.out.println("startRow : " + startRow + ", endRow : " + endRow + " , startCol : " + startCol + ", endCol : " + endCol);
		
		int y1 = Math.max(0, mRow - mDis);
		int y2 = Math.min(N, mRow + mDis);
		
		int x1 = Math.max(0, mCol - mDis);
		int x2 = Math.min(N, mCol + mDis);
		
		
		for(int i=startRow; i<=endRow; i++) {
			for(int j=startCol; j<=endCol; j++) {				
				for(int k=startColor; k<=endColor; k++) {
					for(Tower tower : sMap[i][j][k]) {
						if(removed[tower.row][tower.col] == 1) continue;
						if(tower.row < y1 || tower.col < x1 || tower.row > y2 || tower.col > x2)  continue;
						count++;
					}
				}
				
			}
		}
		
		return count;
	}

	public int getClosest(int mRow, int mCol, int mColor) {
		
		int y = mRow / SUMMARY_UNIT;
		int x = mCol / SUMMARY_UNIT;
		
		if(countByColor[mColor] == 0) return -1;
		
		int startColor = mColor;
		int endColor = mColor;
		if(mColor == 0) {
			startColor = 1;
			endColor = 5;
		}
		
		int[][] used = new int[sSize][sSize];
		
		int sDis = sSize > 2 ? 2 : 1;
		int min = Integer.MAX_VALUE;
		while(sDis <= sSize) {
			
			int startRow = Math.max(0, y - sDis);		
			int endRow = Math.min(sSize - 1, y + sDis);
			
			int startCol = Math.max(0, x - sDis);		
			int endCol = Math.min(sSize - 1, x + sDis);		
			

			for(int i=startRow; i<=endRow; i++) {
				for(int j=startCol; j<=endCol; j++) {
					if(used[i][j] == 1) continue;
					for(int k=startColor; k<=endColor; k++) {
						for(Tower tower : sMap[i][j][k]) {
							if(removed[tower.row][tower.col] == 1) continue;
							int dis = Math.abs(tower.row - mRow) + Math.abs(tower.col - mCol);
							min = Math.min(dis, min);
						}
					}
					used[i][j] = 1;
				}
			}
			if(min != Integer.MAX_VALUE) return min;
			
			
			sDis++;
		}
		
		
		return min; 
	}
	
	boolean binarySearch(int dis, int row, int col, int color) {
		
		
		
		
		return false;
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
		//Q = Integer.parseInt(st.nextToken());
		Q = 2000;

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
				//print(q, "buildTower", q, q, row, col, color);
				break;
				
			case CMD_REMOVE:
				row = Integer.parseInt(st.nextToken());
				col = Integer.parseInt(st.nextToken());
				userSolution.removeTower(row, col); 
				//print(q, "removeTower", q, q, row, col);
				break;
				
			case CMD_COUNT:
				row = Integer.parseInt(st.nextToken());
				col = Integer.parseInt(st.nextToken());
				color = Integer.parseInt(st.nextToken());
				dis = Integer.parseInt(st.nextToken());
				ret = userSolution.countTower(row, col, color, dis); 
				ans = Integer.parseInt(st.nextToken());
				//print(q, "countTower", ans, ret, row, col, color, dis);
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
		if(ans!=ret)  System.err.println("----------------------오류--------------------");
		System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception {
		// System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\감시탑\\sample_input3.txt"));


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