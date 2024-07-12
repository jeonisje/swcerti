package 기출문제.렌터카조회;

import java.io.*;
import java.util.*;

class UserSolution {
	int MAX = 100_001;
	HashMap<Integer, TreeMap<RentCar, Integer>> searchMap;
	ArrayList<Integer>[] idByCompany;
	int[] priceByCar;
	int[] limitByCar;

	public void init(int N) {
		searchMap = new HashMap<>();
		idByCompany = new ArrayList[N+1];
		priceByCar =  new int[MAX];
		limitByCar =  new int[MAX];
		return;
	}
	// 100_000
	public void add(int mCarID, int mCompanyID, int[] mCarInfo) {
		
		int key = mCarInfo[0] * 100_000 + mCarInfo[1] * 1_000 + mCarInfo[2] * 10 + mCarInfo[3];
		
		TreeMap<RentCar, Integer> tm = searchMap.getOrDefault(key, 
				new TreeMap<RentCar, Integer>((o1, o2) -> o1.price == o2.price ? Integer.compare(o1.id, o2.id) : Integer.compare(o1.price, o2.price)));
		tm.put(new RentCar(mCarID, mCarInfo[5]), key);
		searchMap.put(key, tm);
		
		return;
	}
	
	
	// 10_000
	public int rent(int[] mCondition) {
		return -1;
	}
	// 600
	public int promote(int mCompanyID, int mDiscount) {
		return -1;
	}
	
	class RentCar {
		int id;
		int price;
		TreeMap<Integer, Integer> rented = new TreeMap<>();
		
		public RentCar(int id, int price) {		
			this.id = id;
			this.price = price;
		}		
	}
}

public class Main {
	private final static int CMD_INIT = 100;
	private final static int CMD_ADD = 200;
	private final static int CMD_RENT = 300;
	private final static int CMD_PROMOTE = 400;

	private final static UserSolution usersolution = new UserSolution();
	private static BufferedReader br;
	private static StringTokenizer st;

	private static boolean run() throws Exception {
		boolean isCorrect = true;
		int cmd, user_ans, correct_ans;
		int mDiscount, mCompanyID = 0, mCarID = 0, mCarInfo[] = new int[6], mCondition[] = new int[6];

		int N;
		st = new StringTokenizer(br.readLine(), " ");
		int Q = Integer.parseInt(st.nextToken());
		for (int q = 0; q < Q; ++q) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {
			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				usersolution.init(N);
				break;
			case CMD_ADD:
				mCarID = Integer.parseInt(st.nextToken());
				mCompanyID = Integer.parseInt(st.nextToken());
				for (int i = 0; i < 6; i++)
					mCarInfo[i] = Integer.parseInt(st.nextToken());
				usersolution.add(mCarID, mCompanyID, mCarInfo);
				break;
			case CMD_RENT:
				for (int i = 0; i < 6; i++)
					mCondition[i] = Integer.parseInt(st.nextToken());
				user_ans = usersolution.rent(mCondition);
				correct_ans = Integer.parseInt(st.nextToken());
				if (user_ans != correct_ans)
					isCorrect = false;
				break;
			case CMD_PROMOTE:
				mCompanyID = Integer.parseInt(st.nextToken());
				mDiscount = Integer.parseInt(st.nextToken());
				user_ans = usersolution.promote(mCompanyID, mDiscount);
				correct_ans = Integer.parseInt(st.nextToken());
				if (user_ans != correct_ans)
					isCorrect = false;
				break;
			default:
				isCorrect = false;
				break;
			}
		}
		return isCorrect;
	}
	static void print(int q, String cmd, int ans, int ret, Object...o) {
    	//if(ans != ret) System.err.println("---------------------오류------------------------");
    	//System.out.println("["+q+"] " + cmd +  " " + ans + "=" + ret + " [" + Arrays.deepToString(o) + "]" );
    }
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\렌터카조회\\sample_input.txt"));
		
		int TC, MARK;
		// System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}
