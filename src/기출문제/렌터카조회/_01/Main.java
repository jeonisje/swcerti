package 기출문제.렌터카조회._01;

import java.io.*;
import java.util.*;

class UserSolution {
	int MAX = 100_001;
	HashMap<Integer, TreeMap<RentCar, Integer>> searchMap;
	ArrayList<Integer>[] idByCompany;
	int[] hashByCar;
	int[] priceByCar;
	int[] limitByCar;
	TreeMap<Integer, Integer>[] rentedByCar;

	public void init(int N) {
		searchMap = new HashMap<>();
		idByCompany = new ArrayList[N+1];
		
		for(int i=0; i<N+1; i++) {
			idByCompany[i] = new ArrayList<>();
		}
		hashByCar = new int[MAX];		
		priceByCar =  new int[MAX];
		limitByCar =  new int[MAX];
		rentedByCar = new TreeMap[MAX];
		
		for(int i=0; i<MAX; i++) {
			rentedByCar[i] = new TreeMap<>();
		}
		return;
	}
	// 100_000
	public void add(int mCarID, int mCompanyID, int[] mCarInfo) {
		
		int key = mCarInfo[0] * 100_000 + mCarInfo[1] * 1_000 + mCarInfo[2] * 10 + mCarInfo[3];
		RentCar rc = new RentCar(mCarID, mCarInfo[5]);
		
		TreeMap<RentCar, Integer> tm = searchMap.getOrDefault(key, 
				new TreeMap<RentCar, Integer>((o1, o2) -> o1.price == o2.price ? Integer.compare(o1.id, o2.id) : Integer.compare(o1.price, o2.price)));
		tm.put(rc, key);
		searchMap.put(key, tm);
		
		limitByCar[mCarID] = mCarInfo[4];
		priceByCar[mCarID] = mCarInfo[5];	
		hashByCar[mCarID] = key;
		idByCompany[mCompanyID].add(mCarID);
		
		return;
	}
	
	
	// 10_000
	public int rent(int[] mCondition) {
		int key = mCondition[2] * 100_000 + mCondition[3] * 1_000 + mCondition[4] * 10 + mCondition[5];
		if(!searchMap.containsKey(key)) return -1;
		
		TreeMap<RentCar, Integer> tm = searchMap.get(key);
		
		while(!tm.isEmpty()) {
			RentCar rc = tm.firstKey();
			if(priceByCar[rc.id] == 0 || priceByCar[rc.id] != rc.price || limitByCar[rc.id] == 0 ||  rc.price == 0) {
				tm.pollFirstEntry();
				continue;
			}			
			break;
		}
		
		for(Map.Entry<RentCar, Integer> entry : tm.entrySet()) {
			RentCar rc = entry.getKey();
			Integer date1 = rentedByCar[rc.id].floorKey(mCondition[0]);
			Integer date2 = rentedByCar[rc.id].ceilingKey(mCondition[0]);			
			
			if(date1 != null && rentedByCar[rc.id].get(date1) > mCondition[0]) continue;
			if(date2 != null && date2 < mCondition[1]) continue;
			
			rentedByCar[rc.id].put(mCondition[0], mCondition[1]);
			limitByCar[rc.id]--;
			return rc.id;
		}
		
		return -1;
	}
	// 600
	public int promote(int mCompanyID, int mDiscount) {
		int sum = 0;
		for(int id : idByCompany[mCompanyID]) {
			if(priceByCar[id] == 0) continue;
			if(limitByCar[id] == 0) continue;
			
			TreeMap<RentCar, Integer> tm = searchMap.get(hashByCar[id]);
			priceByCar[id] -= mDiscount;
			
			if(priceByCar[id] < 0) {
				priceByCar[id] = 0;
				continue;
			}
			sum += priceByCar[id];
			tm.put(new RentCar(id, priceByCar[id]), 1);
			
			
			
			//searchMap.put(hashByCar[id], tm);
		}
		
		return sum;
	}
	
	class RentCar {
		int id;
		int price;
	
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
				//print(q, "add", q, q, mCarID, mCompanyID, mCarInfo);
				break;
			case CMD_RENT:
				for (int i = 0; i < 6; i++)
					mCondition[i] = Integer.parseInt(st.nextToken());
				user_ans = usersolution.rent(mCondition);
				correct_ans = Integer.parseInt(st.nextToken());
				print(q, "rent", correct_ans, user_ans, mCondition);
				if (user_ans != correct_ans)
					isCorrect = false;
				break;
			case CMD_PROMOTE:
				mCompanyID = Integer.parseInt(st.nextToken());
				mDiscount = Integer.parseInt(st.nextToken());
				user_ans = usersolution.promote(mCompanyID, mDiscount);
				correct_ans = Integer.parseInt(st.nextToken());
				print(q, "promote", correct_ans, user_ans, mCompanyID, mDiscount);
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
