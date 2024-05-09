package 기출문제.카페;


import java.io.*;
import java.util.*;

class UserSolution {
	public void init(int N, int mCookingTimeList[]) {
		return;
	}

	public int order(int mTime, int mID, int M, int mDishes[]) {
		return -1;
	}

	public int cancel(int mTime, int mID) {
		return -1;
	}

	public int getStatus(int mTime, int mID) {
		return -1;
	}
}
public class Main {
	private final static int CMD_INIT = 100;
	private final static int CMD_ORDER = 200;
	private final static int CMD_CANCEL = 300;
	private final static int CMD_STATUS = 400;
	private final static int t_Max = 10;
	private final static UserSolution usersolution = new UserSolution();

	private static boolean run(BufferedReader br) throws Exception {
		int t = Integer.parseInt(br.readLine());

		int N, mID, M, mTime;
		int[] mDishes;
		int[] mCookingTimeList = new int[t_Max];
		int cmd, ans, ret = 0;
		boolean okay = false;

		for (int q = 0; q < t; ++q) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				for (int k = 0; k < N; k++)
					mCookingTimeList[k] = Integer.parseInt(st.nextToken());
				usersolution.init(N, mCookingTimeList);
				okay = true;
				break;
			case CMD_ORDER:
				mTime = Integer.parseInt(st.nextToken());
				mID = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				mDishes = new int[t_Max];
				for (int k = 0; k < M; k++)
					mDishes[k] = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = usersolution.order(mTime, mID, M, mDishes);
				print(q, "order", ans, ret, mTime, mID, M, mDishes);
				if (ret != ans)
					okay = false;
				break;
			case CMD_CANCEL:
				mTime = Integer.parseInt(st.nextToken());
				mID = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = usersolution.cancel(mTime, mID);
				print(q, "cancel", ans, ret, mTime, mID);
				if (ret != ans)
					okay = false;
				break;
			case CMD_STATUS:
				mTime = Integer.parseInt(st.nextToken());
				mID = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = usersolution.getStatus(mTime, mID);
				print(q, "getStatus", ans, ret, mTime, mID);
				if (ret != ans)
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
		
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//기출문제//카페//sample_input2.txt"));
		
		int TC, MARK;

		// System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}