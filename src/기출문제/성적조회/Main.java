package 기출문제.성적조회;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

class UserSolution {
	public void init() {
		return;
	}

	public int addScore(int mId, int mGrade, char mGender[], int mScore) {
		return 0;
	}

	public int removeScore(int mId) {
		return 0;
	}

	public int get(int mGradeCnt, int mGrade[], int mGenderCnt, char mGender[][], int mScore) {
		return 0;
	}
}

public class Main {
	private final static int CMD_INIT = 100;
	private final static int CMD_ADD = 200;
	private final static int CMD_REMOVE = 300;
	private final static int CMD_QUERY = 400;

	private final static UserSolution usersolution = new UserSolution();

	private static void String2Char(char[] buf, String str) {
		for (int k = 0; k < str.length(); ++k)
			buf[k] = str.charAt(k);
		buf[str.length()] = '\0';
	}
	private static boolean run(BufferedReader br) throws Exception {
		int q = Integer.parseInt(br.readLine());

		int id, grade, score;
		int cmd, ans, ret;
		boolean okay = false;

		for (int i = 0; i < q; ++i) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
				case CMD_INIT:
					usersolution.init();
					okay = true;
					break;
				case CMD_ADD:
					char[] gender = new char[7];
					id = Integer.parseInt(st.nextToken());
					grade = Integer.parseInt(st.nextToken());
					String2Char(gender, st.nextToken());
					score = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.addScore(id, grade, gender, score);
					if (ret != ans)
						okay = false;
					break;
				case CMD_REMOVE:
					id = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.removeScore(id);
					if (ret != ans)
						okay = false;
					break;
				case CMD_QUERY:
					int gradeCnt, genderCnt;
					int[] gradeArr = new int[3];
					char[][] genderArr = new char[2][7];
					gradeCnt = Integer.parseInt(st.nextToken());
					for (int j = 0; j < gradeCnt; ++j) {
						gradeArr[j] = Integer.parseInt(st.nextToken());
					}
					genderCnt = Integer.parseInt(st.nextToken());
					for (int j = 0; j < genderCnt; ++j) {
						String2Char(genderArr[j], st.nextToken());
					}
					score = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.get(gradeCnt, gradeArr, genderCnt, genderArr, score);
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
	
	static void print(int q, String cmd, int ans, int ret, Object...objects) {
		if(ans!=ret) System.err.println();
		System.out.println("["+q+"] " + cmd + " " + ans + "=" + ret + " (" +Arrays.deepToString(objects)+ ")") ;
	} 

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		int TC, MARK;

		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\성적조회\\sample_input2.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}
