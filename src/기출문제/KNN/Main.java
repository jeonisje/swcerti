package 기출문제.KNN;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	final int MAX_N = 20_001;
	final int MAX_LOC = 4001;
	final int MAX_CATEGORY = 11;
	final int SUMMARY_UNIT = 100;
	
	int K, L;
	
	HashMap<Integer, Integer> idToSeq;
	
	ArrayList<Point>[][] summaryMap;
	
	int[] delete;
	
	int summaryCount;
	int sequence;
	
	public void init(int K, int L) {
	    this.K = K;
	    this.L = L;
	    
	    idToSeq = new HashMap<>();	    
	    summaryCount = MAX_LOC / SUMMARY_UNIT + 1;	    
	    summaryMap= new ArrayList[summaryCount][summaryCount];
	    
	    for(int i=0; i<summaryCount; i++) {
	    	for(int j=0; j<summaryCount; j++) {
	    		summaryMap[i][j] = new ArrayList<>();
	    	}
	    }
	    
	    delete = new int[MAX_N];
	    sequence = 0;
	}

	public void addSample(int mID, int mX, int mY, int mC) {
		sequence++;
		idToSeq.put(mID, sequence);
		
		int summaryX = mX / SUMMARY_UNIT;
		int summaryY = mY / SUMMARY_UNIT;
		
		summaryMap[summaryY][summaryX].add(new Point(sequence, mX, mY, mC));		
	}

	public void deleteSample(int mID) {
		int seq = idToSeq.get(mID);
		delete[seq] = 1;
	}

	public int predict(int mX, int mY) {
		int summaryX = mX / SUMMARY_UNIT;
		int summaryY = mY / SUMMARY_UNIT;
		
		int startX = Math.max(0, summaryX - 1);
		int endX =  Math.min(summaryCount, summaryX + 1);
		int startY = Math.max(0, summaryY - 1);
		int endY =  Math.min(summaryCount, summaryY + 1);
		
		PriorityQueue<KNN> q = new PriorityQueue<>(
				(o1, o2) -> o1.distance == o2.distance ? o1.x == o2.x ? Integer.compare(o1.y, o2.y) : Integer.compare(o1.x, o2.x) : Integer.compare(o1.distance, o2.distance));
				
		for(int i=startY; i<=endY; i++) {
			for(int j=startX; j<=endX; j++) {
				for(Point point : summaryMap[i][j]) {
					if(delete[point.seq] == 1) continue;
					int distance = Math.abs(mX - point.x) + Math.abs(mY - point.y);
					if(distance > L) continue;
					q.add(new KNN(point.seq, point.x, point.y, distance, point.catory));
				}
			}
		}
		
		if(q.size() < K) return - 1;		
		
		
		KNN2[] knn = new KNN2[MAX_CATEGORY];
		
		for(int i=1; i<MAX_CATEGORY; i++) {
			knn[i] = new KNN2(i);
		}
		
		PriorityQueue<KNN2> set = new PriorityQueue<>(
				(o1, o2) -> o1.count == o2.count ? Integer.compare(o1.category, o2.category) : Integer.compare(o2.count, o1.count));
		
		
		for(int i=0; i<K; i++) {
			KNN k = q.poll();
			KNN2 knn2 = knn[k.category];
			knn2.count += 1;			
			knn2.minX = Math.min(knn2.minX, k.x);
			knn2.minY = Math.min(knn2.minY, k.y);			
		}
		
		for(int i=1; i<MAX_CATEGORY; i++) {
			set.add(knn[i]);
		}
		
	    return set.peek().category; 
	   
		
		//return -1;
	}
	
	class Point {
		int seq;
		int x;
		int y;
		int catory;
		public Point(int seq, int x, int y, int catory) {
			this.seq = seq;
			this.x = x;
			this.y = y;
			this.catory = catory;
		}		
	}
	
	class KNN {
		int seq;
		int x;
		int	y;
		int distance;
		int category;
		public KNN(int seq, int x, int y, int distance, int category) {
			this.seq = seq;
			this.x = x;
			this.y = y;
			this.distance = distance;
			this.category = category;
		}
		/*
		@Override
		public int compareTo(KNN o) {
			if(this.distance == o.distance) {
				if(this.x == o.x) {
					return Integer.com
				}
			}
			return 0;
		}*/
	}
	
	class KNN2 {		
		int minX;
		int	minY;
		int category;
		int count;
		public KNN2(int minX, int minY, int count) {
			this.minX = minX;
			this.minY = minY;
			this.count = count;
		}
		public KNN2(int catgory) {
			this.minX = Integer.MAX_VALUE;
			this.minY = Integer.MAX_VALUE;			
			this.count = 0;
			this.category = catgory;
		}
	}
}


public class Main {
	private static final int CMD_INIT 			= 100;
	private static final int CMD_ADD_SAMPLE 	= 200;
	private static final int CMD_DELETE_SAMPLE 	= 300;
	private static final int CMD_PREDICT 		= 400;

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		if(ans!=ret)  System.err.println("----------------------오류--------------------");
		System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	private static boolean run(BufferedReader br) throws Exception {
		
		int Q, K, L;
		int mID, mX, mY, mC; 
		int ret = -1; 
		int ans; 
		
		st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken());
		
		boolean okay = false; 

		for (int q = 0; q < Q; ++q) {
			
			st = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) {
			case CMD_INIT:
				K = Integer.parseInt(st.nextToken());
				L = Integer.parseInt(st.nextToken());
				userSolution.init(K, L); 
				okay = true; 
				break; 
			case CMD_ADD_SAMPLE:
				mID = Integer.parseInt(st.nextToken());
				mX = Integer.parseInt(st.nextToken());
				mY = Integer.parseInt(st.nextToken());
				mC = Integer.parseInt(st.nextToken());
				userSolution.addSample(mID, mX, mY, mC); 
				print(q, "addSample", q, q, mID, mX, mY, mC);
				break;
			case CMD_DELETE_SAMPLE:
				mID = Integer.parseInt(st.nextToken());
				userSolution.deleteSample(mID); 
				print(q, "deleteSample", q, q, mID);
				break;
			case CMD_PREDICT:
				mX = Integer.parseInt(st.nextToken());
				mY = Integer.parseInt(st.nextToken());
				ret = userSolution.predict(mX, mY);
				ans = Integer.parseInt(st.nextToken());
				print(q, "predict", ans, ret,  mX, mY);
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
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\KNN\\sample_input3.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());
		
		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		System.out.println("estimated=> " + (System.currentTimeMillis() - start));
	}
}