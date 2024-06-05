package 프로원정대심화반.day3.KNN;


import java.io.*;
import java.util.*;


class UserSolution {
	int SIZE = 4_001;
	int MAX = 30_001;
	int UNIT = 100;
	
	HashMap<Integer, Integer> idtoSeq;	
	ArrayList<Node>[][] summaryMap;
	int[] remove;		
	int[] category;
	
	int sequence;
	
	int K, L;
	int unitCount = SIZE / UNIT + 1;

	public void init(int K, int L) {
		
		this.K = K;
		this.L = L;
		sequence = 0;
		idtoSeq = new HashMap<>();
		
		remove = new int[MAX];
		category = new int[MAX];
		
		summaryMap = new ArrayList[unitCount][unitCount];	
		for(int i=0; i<unitCount; i++) {
			for(int j=0; j<unitCount; j++) {
				summaryMap[i][j] = new ArrayList<>();
			}
		}
	    return; 
	}

	public void addSample(int mID, int mX, int mY, int mC) {
		sequence++;
		
		idtoSeq.put(mID, sequence);
		
		Node node = new Node(sequence, mY, mX, mC);
		
		int unitY = mY / UNIT;
		int unitX = mX / UNIT;
		
		summaryMap[unitY][unitX].add(node);
		
		
		return; 
	}

	public void deleteSample(int mID) {
		int seq = idtoSeq.get(mID);
		remove[seq] = 1;
	    return; 
	}

	public int predict(int mX, int mY) {
		int unitY = mY / UNIT;
		int unitX = mX / UNIT;
		
		int startY = Math.max(0, unitY - 1);
		int endY = Math.min(unitCount, unitY + 1);
		int startX = Math.max(0, unitX - 1);
		int endX = Math.min(unitCount, unitX + 1);

		
		PriorityQueue<KNN> q = new PriorityQueue<>((o1, o2) -> o1.dist == o2.dist ? o1.x == o2.x ? Integer.compare(o1.y, o2.y) : Integer.compare(o1.x, o2.x) : Integer.compare(o1.dist, o2.dist));
		
		for(int i=startY; i<=endY; i++) {
			for(int j=startX; j<=endX; j++) {
				for(Node node : summaryMap[i][j]) {
					int dist = Math.abs(node.y - mY) + Math.abs(node.x - mX);					
					if(dist > L) continue;
					if(remove[node.seq] == 1) continue;
					KNN knn = new KNN(node.y, node.x, dist, node.category);
					q.add(knn);
				}
			}
		}
		
		if(q.size() < K) return -1;		
		
		int ans = 0;
		int maxCount = 0;
		int[] countByCate = new int[11];
		for(int i=0; i<K; i++) {
			KNN cur = q.remove();
			countByCate[cur.category]++;
			
			if(maxCount < countByCate[cur.category]) {
				maxCount = countByCate[cur.category];
				ans = cur.category;				
			} else if(maxCount == countByCate[cur.category]) {
				ans = Math.min(cur.category, ans);	
			}
			
		}
	    return ans;
	}
	
	class Node {
		int seq;
		int y;
		int x;
		int category;
		public Node(int seq, int y, int x, int category) {
			this.seq = seq;
			this.y = y;
			this.x = x;
			this.category = category;
		}
	}
	
	class KNN {
		int y;
		int x;
		int dist;
		int category;
		public KNN(int y, int x, int dist, int category) {		
			this.y = y;
			this.x = x;
			this.dist = dist;
			this.category = category;
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
				print(q, "predict", ans, ret, mX, mY);
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
	private static void print(int num, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("===================오류=======================");
		//System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception {
		Long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대심화반\\day3\\KNN\\sample_input.txt"));

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