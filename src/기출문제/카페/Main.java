package 기출문제.카페;


import java.io.*;
import java.util.*;

class UserSolution {
	int MAX = 15_001;	
	int N;
	
	HashMap<Integer, Integer> idToSeq;	
	PriorityQueue<Integer>[] orderQ;	// 주문 대기
	
	
	int[] cookingTimes;
	int[] statusByOrder; // 주문별 상태 관리		0: 완료, -1:취소 1..N : 남은 요리 수
	Cooking[] curCooking; // 주방별 현재 요리 중인 주문
	
	int[] waitingServe;	// 서빙 대기
	
	int sequnce;	
	int orderCount;
	
	public void init(int N, int mCookingTimeList[]) {
		this.N = N;
		this.cookingTimes = mCookingTimeList;
		idToSeq = new HashMap<>();
		
		orderQ = new PriorityQueue[N+1];
		for(int i=0; i<N+1; i++) {
			orderQ[i] = new PriorityQueue<>();
		}
		cookingTimes = new int[N+1];
		for(int i=0; i<N; i++) {
			cookingTimes[i+1] = mCookingTimeList[i];
		}
		
		statusByOrder = new int[MAX];		
		curCooking = new Cooking[N+1];
		waitingServe = new int[N+1];
		
		sequnce = 0;
		orderCount = 0;
		
		return;
	}
	// 15.000
	public int order(int mTime, int mID, int M, int mDishes[]) {
		passTime(mTime);
		
		sequnce++;
		idToSeq.put(mID, sequnce);
		orderCount++;
		
		for(int i=0; i<M; i++) {
			int dish = mDishes[i];
			statusByOrder[sequnce] = M;
			if(curCooking[dish] == null) {
				int finish = cookingTimes[dish] + mTime - 1;
				curCooking[dish] = new Cooking(sequnce, finish);
			} else {
				orderQ[dish].add(sequnce);
			}
		}
		
		return orderCount;
	}
	
	void passTime(int time) {
		for(int i=1; i<N+1; i++) {
			if(curCooking[i] == null) continue;
			if(curCooking[i].finishTime >= time) continue; 
			// 요리 완료 시간이 지났다면 완료 처리
			int seq = curCooking[i].orderSeq;
			statusByOrder[seq]--;
			waitingServe[i] = seq;
			if(statusByOrder[seq] == 0) { 
				orderCount--;
				for(int k=1; k<N+1; k++) {
					if(waitingServe[k] == seq) {
						waitingServe[k]= 0;
					}
				}
			}
			
			if(orderQ[i].isEmpty()) {
				curCooking[i] = null;
				continue;
			}
			int nextSeq = orderQ[i].remove();
			curCooking[i] = new Cooking(nextSeq, cookingTimes[i] + time - 1);			
		}
	}
	
	
	// 500
	public int cancel(int mTime, int mID) {
		orderCount--;
		
		int seq = idToSeq.get(mID);
		statusByOrder[seq] = -1;
		// 서빙 대기중인 요리 확인
		for(int i=1; i<N+1; i++) {
			int cur = waitingServe[i];
			if(cur == 0) continue;
			if(cur != seq) continue;
			if(orderQ[i].isEmpty()) {
				curCooking[i] = null;
				continue;
			}
			if(curCooking[i] == null) continue;
			if(curCooking[i].finishTime > mTime) continue;
			
			
			int nextSeq = orderQ[i].remove();			
			curCooking[i].orderSeq = nextSeq;
		}
		
		for(int i=1; i<N+1; i++) {
			Cooking cur = curCooking[i];
			if(cur == null) continue;
			if(cur.orderSeq != seq) continue;
			if(orderQ[i].isEmpty()) {
				curCooking[i] = null;
				continue;
			}
			int nextSeq = orderQ[i].remove();			
			curCooking[i].orderSeq = nextSeq;
		}
		
		
		
		passTime(mTime);
		
		
		return orderCount;
	}
	
	// 10,000
	public int getStatus(int mTime, int mID) {
		passTime(mTime);
		int seq = idToSeq.get(mID);
		
		return statusByOrder[seq];
	}
	
	class Cooking {
		int orderSeq;
		int finishTime;
		public Cooking(int orderSeq, int finishTime) {		
			this.orderSeq = orderSeq;
			this.finishTime = finishTime;
		}
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
				print(q, "order", ans, ret,  mTime, mID, M, mDishes);
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