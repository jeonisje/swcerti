package 연습문제.푸시;

import java.io.*;
import java.util.*;

class UserSolution {
	public void init(int N, int[][] mMap){
	    return;
	}
	 
	public int push(int mRockR, int mRockC, int mDir, int mGoalR, int mGoalC) {
	    return 0;
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
	
	public static void main(String[] args) throws Exception {
		// System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());
		
		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
	}
}