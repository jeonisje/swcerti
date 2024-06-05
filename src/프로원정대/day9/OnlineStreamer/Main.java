package 프로원정대.day9.OnlineStreamer;


import java.io.*;
import java.util.*;

class UserSolution {
	int[] arr;
	int[] treeForSum;
	int[] treeForMin;
	int[] treeForMax;
	
	int N;
	
	public void init(int N, int[] mSubscriber) {
		this.N = N;
		arr = new int [N+1];
		for(int i=0; i<N; i++) {
			arr[i+1] = mSubscriber[i];
		}
		
		treeForSum = new int[N*4];
		treeForMin = new int[N*4];
		treeForMax = new int[N*4];
		
		initTree(1, N, 1);
	    return;
	}
	 
	void initTree(int start, int end, int node) {
		if(start == end) {
			treeForSum[node] = arr[start];
			treeForMin[node] = arr[start];
			treeForMax[node] = arr[start];
			return;
		}
		int mid = (start + end) / 2;
		initTree(start, mid, node*2);
		initTree(mid+1, end, node*2 + 1);
		treeForSum[node] = treeForSum[node*2] + treeForSum[node*2+1];
		treeForMin[node] = Math.min(treeForMin[node*2], treeForMin[node*2+1]); 
		treeForMax[node] = Math.max(treeForMax[node*2], treeForMax[node*2+1]); 
	}

	public int subscribe(int mId, int mNum) {
		updateTree(1, N, 1, mId, mNum);
		return arr[mId]; 
	}
	
	void updateTree(int start, int end, int node, int index, int value) {
		if(index < start || index > end) return;
		if(start == end) {
			treeForSum[node] += value;
			treeForMin[node] += value;
			treeForMax[node] += value;
			arr[index] += value;
			return;
		}
		
		int mid = (start + end) / 2;
		updateTree(start, mid, node*2, index, value);
		updateTree(mid+1,end, node*2+1, index, value);
		treeForSum[node] = treeForSum[node*2] + treeForSum[node*2+1];
		treeForMin[node] = Math.min(treeForMin[node*2], treeForMin[node*2+1]); 
		treeForMax[node] = Math.max(treeForMax[node*2], treeForMax[node*2+1]); 		
	}
	 
	public int unsubscribe(int mId, int mNum) {
		updateTree(1, N, 1, mId, -mNum);
		return arr[mId]; 
	}
	 
	public int count(int sId, int eId) {
		return queryForSum(1, N, 1, sId, eId);
	}
	
	int queryForSum(int start, int end, int node, int left, int right) {
		if(end < left || right < start) return 0;
		if(left <= start && end <= right) return treeForSum[node];
		int mid = (start + end) / 2;
		int lv = queryForSum(start, mid, node*2, left, right);
		int rv = queryForSum(mid+1, end, node*2+1, left, right);
		return lv + rv;
	}
	
	
	int queryForMin(int start, int end, int node, int left, int right) {
		if(end < left || right < start) return Integer.MAX_VALUE;
		if(left <= start && end <= right) return treeForMin[node];
		int mid = (start + end) / 2;
		int lv = queryForMin(start, mid, node*2, left, right);
		int rv = queryForMin(mid+1, end, node*2+1, left, right);
		return Math.min(lv, rv);
	}
	
	int queryForMax(int start, int end, int node, int left, int right) {
		if(end < left || right < start) return 0;
		if(left <= start && end <= right) return treeForMax[node];
		int mid = (start + end) / 2;
		int lv = queryForMax(start, mid, node*2, left, right);
		int rv = queryForMax(mid+1, end, node*2+1, left, right);
		return Math.max(lv, rv);
	}
	
	public int calculate(int sId, int eId) {
		int min = queryForMin(1, N, 1, sId, eId);
		int max = queryForMax(1, N, 1, sId, eId);
		return max - min;  
	}
}

class Main {
	private static final int CMD_INIT 		 = 100;
	private static final int CMD_SUBSCRIBE	 = 200;
	private static final int CMD_UNSUBSCRIBE = 300;
	private static final int CMD_COUNT		 = 400; 
	private static final int CMD_CALCULATE 	 = 500; 

	private static UserSolution userSolution = new UserSolution();
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {
		
		int n, mId, mNum, sId, eId;
		int cmd, ans, ret;
		int[] mSubscriber; 
		boolean okay = false; 
		
		st = new StringTokenizer(br.readLine(), " ");
		int Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {
			
			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) {
			
			case CMD_INIT:
				okay = true;
				n = Integer.parseInt(st.nextToken());
				mSubscriber = new int[n]; 
				for(int j = 0; j < n; j++) 
					mSubscriber[j] = Integer.parseInt(br.readLine());
                userSolution.init(n, mSubscriber); 
				break; 
				
			case CMD_SUBSCRIBE:
				mId = Integer.parseInt(st.nextToken());
				mNum = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.subscribe(mId, mNum);
				print(q, "subscribe", ans, ret, mId, mNum);
				if(ans != ret) 
					okay = false; 
				break;
				
			case CMD_UNSUBSCRIBE:
				mId = Integer.parseInt(st.nextToken());
				mNum = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.unsubscribe(mId, mNum);
				print(q, "unsubscribe", ans, ret, mId, mNum);
				if(ans != ret) 
					okay = false; 
				break;
				
			case CMD_COUNT:
				sId = Integer.parseInt(st.nextToken());
				eId = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.count(sId, eId);
				print(q, "count", ans, ret, sId, eId);
                if(ans != ret)
					okay = false; 
				break;
			
			case CMD_CALCULATE:
				sId = Integer.parseInt(st.nextToken());
				eId = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.calculate(sId, eId);
				print(q, "calculate", ans, ret, sId, eId);
				if(ans != ret)
					okay = false; 
				break;
			
			default:
				okay = false;
				break;
			}
		}

		return okay;
	}
	private static void print(int num, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("===================오류=======================");
		//System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대\\day9\\OnlineStreamer\\sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());
		
		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}