package 기출문제.카페._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	int MAX = 15_001;	
	int N;
	
	HashMap<Integer, Integer> idToSeq;	
	PriorityQueue<Integer>[] cookingQ;	// 조리 대기
	PriorityQueue<Integer>[] serveingQ;	// 서빙 대기	
	
	int[] cookingTimes;
	int[] statusByOrder; // 주문별 상태 관리		0: 완료, -1:취소 1..N : 남은 요리 수	
	int[] finishByCooking;	// 요리별 종료시간
	
	int sequnce;	
	int orderCount;
	
	public void init(int N, int mCookingTimeList[]) {
		this.N = N;
		
		idToSeq = new HashMap<>();
		
		cookingTimes = new int[N+1];
		for(int i=0; i<N; i++) {
			cookingTimes[i+1] = mCookingTimeList[i];
		}
		
		cookingQ = new PriorityQueue[N+1];
		for(int i=0; i<N+1; i++) {
			cookingQ[i] = new PriorityQueue<>();
		}
		
		serveingQ = new PriorityQueue[N+1];
		for(int i=0; i<N+1; i++) {
			serveingQ[i] = new PriorityQueue<>();
		}		
		
		statusByOrder = new int[MAX];	
		finishByCooking = new int[N+1];
		Arrays.fill(finishByCooking, -1);
		
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
			cookingQ[dish].add(sequnce);
			
			if(finishByCooking[dish] != -1) continue;
			
			int finish = cookingTimes[dish] + mTime;
			finishByCooking[dish] = finish;
			 
		}
		
		return orderCount;
	}
	
	void passTime(int time) {
		for(int i=1; i<N+1; i++) {
			if(cookingQ[i].isEmpty()) continue;
			if(finishByCooking[i] >= time) continue;
			int seq = cookingQ[i].remove();
			
			statusByOrder[seq]--;
			if(statusByOrder[seq] == 0) {
				// 전체 요리 완성
				orderCount--;
				for(int k=1; k<N+1; k++) {
					if(!serveingQ[k].isEmpty() && serveingQ[k].peek() == seq) {
						serveingQ[k].remove();
					} else {
						serveingQ[k].add(seq);
					}
				} 
			}
			
			if(cookingQ[i].isEmpty()) {
				finishByCooking[i] = -1;
				continue;		
			}
			
			finishByCooking[i] = cookingTimes[i] + time;
			
		}
	}
	
	
	// 500
	public int cancel(int mTime, int mID) {
		orderCount--;
		
		int seq = idToSeq.get(mID);
		statusByOrder[seq] = -1;
		// 서빙 대기중인 요리 확인
		for(int i=1; i<N+1; i++) {
			if(cookingQ[i].peek() != seq) continue;
			
			cookingQ[i].remove();
			if(cookingQ[i].isEmpty()) {
				if(serveingQ[i].isEmpty()) continue;				
				if(serveingQ[i].peek() != seq) continue;
				serveingQ[i].remove();
				continue;
			}
			
			int nextSeq = cookingQ[i].peek();
			int finish = cookingTimes[i] + mTime;
			if(finishByCooking[i] <= finish) {
				cookingQ[i].remove();
				statusByOrder[nextSeq]--;
				serveingQ[i].add(nextSeq);
			}			
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