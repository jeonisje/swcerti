package 기출문제.단어암기장;


import java.io.*;
import java.util.*;

class UserSolution {
	final int MAX_WORD = 50_001;
	final int SUMMARY_UNIT = 1000;
	int N, M;
	
	Word[] wordInfo;
	ArrayList<TreeSet<Word>> map;
	
	int[] availableCountByRow;	
	
	int[] writtenCellByRow;
	int page;
	
	
	public void init(int N, int M) {
		this.N = N;
		this.M = M;
		
		wordInfo = new Word[MAX_WORD];
	
		map = new ArrayList<>();
		
		availableCountByRow = new int[N];
		
		writtenCellByRow = new int[N];
		
		
		return;
	}

	public int writeWord(int mId, int mLen) {
		int curRow = 0;
		while(curRow < N) {
			if(availableCountByRow[curRow] >= mLen) {
				
			}
		}
		
		return -1; 
	}
	
	public int eraseWord(int mId) {
		return -1;
	}
	
	class Word {
		int row;
		int col;
		int len;
		public Word(int row, int col, int len) {		
			this.row = row;
			this.col = col;
			this.len = len;
		}
	}
}

public class Main {
	private static final int CMD_INIT 	= 1;
	private static final int CMD_WRITE 	= 2;
	private static final int CMD_ERASE 	= 3;
	
	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	private static boolean run(BufferedReader br) throws Exception {

		int query_num, mId, mLen, N, M; 
		int ans, ret; 
		boolean okay = false; 
		
		st = new StringTokenizer(br.readLine(), " ");
		int Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				userSolution.init(N, M); 
                okay = true; 
				break;
				
			case CMD_WRITE:
				mId = Integer.parseInt(st.nextToken());
				mLen = Integer.parseInt(st.nextToken());
				ret = userSolution.writeWord(mId, mLen);
				ans = Integer.parseInt(st.nextToken());
				print(q, "writeWord", ans, ret, mId, mLen);
				if(ans != ret) {
					okay = false; 
				}
				break;
				
			case CMD_ERASE:
				mId = Integer.parseInt(st.nextToken());
				ret = userSolution.eraseWord(mId); 
				ans = Integer.parseInt(st.nextToken());
				print(q, "writeWord", ans, ret, mId);
				if(ans != ret) {
					okay = false; 
				}
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
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\단어암기장\\sample_input3.txt"));


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
