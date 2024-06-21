package 기출문제.미생물;


import java.io.*;
import java.util.*;

class UserSolution {
	int MAX_TIME = 1_000_001;
	int MAX_ID = 30_001;
	
	PriorityQueue<Bacteria> timeQ;
	PriorityQueue<Bacteria> lifeQ;
	
	int[] halfTimeById; 
	
	int[] time;
	int[] tree;
	
	public void init() {
		timeQ = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.halfTime, o2.halfTime));
		lifeQ = new PriorityQueue<>((o1, o2) -> o1.lifeSpan == o2.lifeSpan ? Integer.compare(o1.id, o2.id) : Integer.compare(o1.lifeSpan, o2.lifeSpan));
		
		halfTimeById = new int[MAX_ID];
		
		int h = (int) Math.ceil(Math.log(MAX_TIME)/ Math.log(2));
		int treeSize = (int) Math.pow(2,h+1);
		
		time = new int[MAX_TIME];
		tree = new int[treeSize];
		
	    return;
	}
	 
	public void addBacteria(int tStamp, int mID, int mLifeSpan, int mHalfTime){
		passTime(tStamp);
		
		Bacteria b = new Bacteria(mID, mHalfTime + tStamp, mLifeSpan);
		timeQ.add(b);
		lifeQ.add(b);
		
		halfTimeById[mID] = mHalfTime;
		updateTree(1, MAX_TIME-1, 1, mLifeSpan, 1);
		
      	return; 
	}
	
	void passTime(int tStamp) {
		while(!timeQ.isEmpty()) {
			if(timeQ.peek().halfTime > tStamp) break;
			Bacteria b = timeQ.remove();
			lifeQ.remove(b);
			updateTree(1, MAX_TIME-1, 1, b.lifeSpan, -1);
			int lifeSpan = b.lifeSpan / 2;			
			
			if(lifeSpan <= 99) continue;
			
			int halfTime = b.halfTime + halfTimeById[b.id];
			
			Bacteria newB = new Bacteria(b.id, halfTime, lifeSpan);
			timeQ.add(newB);
			lifeQ.add(newB);
			
			updateTree(1, MAX_TIME-1, 1, lifeSpan, 1);
		}
	}
	
	void updateTree(int start, int end, int node, int index, int value) {
		if(index < start || index > end) return;
		if(start == end) {
			tree[node] += value;
			time[index] += value;
			return;
		}
		
		int mid = (start + end) / 2;
		updateTree(start, mid, node*2, index, value);
		updateTree(mid+1, end, node*2+1, index, value);
		tree[node] = tree[node*2] + tree[node*2+1];		
	}
	 
	public int getMinLifeSpan(int tStamp) {
		passTime(tStamp);
	    return lifeQ.peek().id;
	}
	 
	public int getCount(int tStamp, int mMinSpan, int mMaxSpan) {
		passTime(tStamp);
		return query(1, MAX_TIME-1, 1, mMinSpan, mMaxSpan); 
	}
	
	int query(int start, int end, int node, int left, int right) {
		if(end < left || right < start) return 0;
		if(left <= start && end <= right) return tree[node];
		
		int mid = (start + end) / 2;
		int lv = query(start, mid, node*2, left, right);
		int rv  = query(mid+1, end, node*2+1, left, right);
		return lv + rv;
	}
	
	class Bacteria {
		int id;
		int halfTime;	// 반감기 시간
		int lifeSpan;
		public Bacteria(int id, int halfTime, int lifeSpan) {		
			this.id = id;
			this.halfTime = halfTime;
			this.lifeSpan = lifeSpan;
		}
	}
}


public class Main {
	private static final int CMD_INIT	 	= 0;
	private static final int CMD_ADD 		= 1;
	private static final int CMD_MINSPAN	= 2;
	private static final int CMD_GET   		= 3; 

	private static UserSolution userSolution = new UserSolution();
	static BufferedReader br;
	static StringTokenizer st;

	public static boolean run(BufferedReader br) throws IOException {
		int Q, time, id, minSpan, maxSpan, halftime;
		int ret, ans; 
		boolean ok = false;
		st = new StringTokenizer(br.readLine());
		Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; q++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
			case CMD_INIT:
				userSolution.init();
				ok = true;
				break; 
			case CMD_ADD:
				time = Integer.parseInt(st.nextToken());
				id = Integer.parseInt(st.nextToken());
				maxSpan = Integer.parseInt(st.nextToken());
				halftime = Integer.parseInt(st.nextToken());
				userSolution.addBacteria(time, id, maxSpan, halftime); 
				print(q, "addBacteria", q,q, time, id, maxSpan, halftime);
				break; 
			case CMD_MINSPAN:
				time = Integer.parseInt(st.nextToken());
				ret = userSolution.getMinLifeSpan(time);
				ans = Integer.parseInt(st.nextToken());
				print(q, "getMinLifeSpan",ans, ret, time);
				if(ans != ret)
					ok = false;
				break; 
			case CMD_GET:
				time = Integer.parseInt(st.nextToken());
				minSpan = Integer.parseInt(st.nextToken());
				maxSpan = Integer.parseInt(st.nextToken());
				ret = userSolution.getCount(time, minSpan, maxSpan);
				ans = Integer.parseInt(st.nextToken());
				print(q, "getCount", ans,ret, time,  minSpan, maxSpan);
				if(ans != ret)
					ok = false;
				break;
			default:
				ok = false;
				break;
			}
		}
		return ok;
	}
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}

	public static void main(String[] args) throws IOException {
		
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\미생물\\sample_input.txt"));
		
		br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int tc = 1; tc <= TC; ++tc) {
			boolean result = run(br);
			int score = result ? MARK : 0;
			System.out.println("#" + tc + " " + score);
		}
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}
