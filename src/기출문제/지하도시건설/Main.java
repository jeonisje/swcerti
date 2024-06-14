package 기출문제.지하도시건설;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class UserSolution {

	public void init(int mH, int mW) {
		return; 
	}

	public int dropBox(int mId, int mLen, int mExitA, int mExitB, int mCol) {
		return 0;
	}

	public int explore(int mIdA, int mIdB) {
		return -1;
	}
}

public class Main {
	private static final int CMD_INIT = 1;
	private static final int CMD_DROPBOX = 2;
	private static final int CMD_EXPLORE = 3;

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {

		int query_num, mId, mLen, mH, mW, mIdA, mIdB, mExitA, mExitB, mCol;
		int ans, ret;
		boolean okay = false;

		st = new StringTokenizer(br.readLine(), " ");
		int Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				mH = Integer.parseInt(st.nextToken());
				mW = Integer.parseInt(st.nextToken());
				userSolution.init(mH, mW);
				okay = true;
				break;

			case CMD_DROPBOX:
				mId = Integer.parseInt(st.nextToken());
				mLen = Integer.parseInt(st.nextToken());
				mExitA = Integer.parseInt(st.nextToken());
				mExitB = Integer.parseInt(st.nextToken());
				mCol = Integer.parseInt(st.nextToken());
				ret = userSolution.dropBox(mId, mLen, mExitA, mExitB, mCol);
				ans = Integer.parseInt(st.nextToken());
				if (ans != ret) {
					okay = false;
				}
				break;

			case CMD_EXPLORE:
				mIdA = Integer.parseInt(st.nextToken());
				mIdB = Integer.parseInt(st.nextToken());
				ret = userSolution.explore(mIdA, mIdB);
				ans = Integer.parseInt(st.nextToken());
				if (ans != ret) {
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

	public static void main(String[] args) throws Exception {
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