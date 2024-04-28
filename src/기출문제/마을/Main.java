package 기출문제.마을;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer; 

class UserSolution {
	final int MAX_L = 200;
	final int MAX_ID = 25_001;
	int L, R;
	ArrayList<Town> summaryMap[][];
	
	int[] seqToId;
	HashMap<Integer, Integer> idToSeq;
	
	//Town[] townInfo;	
	int[] parent;	
	int[] used;
	int[] removed;
	
	int[] countBySeq;
	int[] minIdBySeq;
	
	int sequence;
	
	int summaryCount;
	
	int townCount;
	
	public void init(int L, int R) {
	    this.L = L;
	    this.R = R;
	    
	    sequence = 0;
	    townCount = 0;
	    summaryCount = R / L + 1;
	    
	    summaryMap = new ArrayList[summaryCount][summaryCount];
	    for(int i=0; i<summaryCount; i++) {
	    	for(int j=0; j<summaryCount; j++) {
	    		summaryMap[i][j] = new ArrayList<>();
	    	}
	    }
	    
	    seqToId = new int[MAX_ID];
	    idToSeq = new HashMap<>();
	    
	    //townInfo = new Town[MAX_ID];
	    parent = new int[MAX_ID];
	    used = new int[MAX_ID];
	    removed = new int[MAX_ID];
	    
	    countBySeq = new int[MAX_ID];
	    minIdBySeq = new int[MAX_ID];
	    for(int i=0; i<MAX_ID; i++) {
	    	parent[i] = i;
	    	countBySeq[i] = 1;
	    	minIdBySeq[i] = Integer.MAX_VALUE;
	    }
	    
	}
	
	public int add(int mId, int mX, int mY) {
		sequence++;
		idToSeq.put(mId, sequence);
		seqToId[sequence] = mId;
				
		int foundCount = findTown(sequence, mX, mY, mId);
		if(foundCount == 0) {
			used[sequence] = 1;
			townCount++;
			countBySeq[sequence] = 1;
			minIdBySeq[sequence] = mId;
		}
		int summarySecX = mX / L;
		int summarySecY = mY / L;
		summaryMap[summarySecY][summarySecX].add(new Town(sequence, mY, mX));
		
	   return countBySeq[find(sequence)];
	}
	
	int findTown(int seq, int x, int y, int id) {
		int summarySecX = x / L;
		int summarySecY = y / L;
		
		int startSecX = Math.max(summarySecX - 1, 0);
		int endSecX = Math.min(summarySecX + 1, summaryCount - 1);
		int startSecY = Math.max(summarySecY - 1, 0);
		int endSecY = Math.min(summarySecY + 1, summaryCount - 1);
		
		int foundCount = 0;
		for(int i=startSecY; i<=endSecY; i++) {
			for(int j=startSecX; j<=endSecX; j++) {
				for(Town town : summaryMap[i][j]) {
					if(removed[town.seq] == 1) continue;
					int distance = Math.abs(x - town.x) + Math.abs(y  - town.y);
					if(distance > L ) continue;
					minIdBySeq[seq] = id;
					foundCount += union(town.seq, seq);						
					
				}
			}
		}
		
		return foundCount;
	}
	
	
	
	
	public int remove(int mId) {
		if(!idToSeq.containsKey(mId)) return -1;	
		int seq = idToSeq.get(mId);
		if(removed[seq] == 1) return -1;
		
		int pSeq = find(seq);
			
		removed[seq] = 1;
		int newCount = countBySeq[pSeq] - 1;
		
		if(newCount == 0) {
			townCount--;
			countBySeq[seq] = 0;
			minIdBySeq[seq] = Integer.MAX_VALUE;
			used[seq] = 0;
			return 0;
		}
		
		
		if(minIdBySeq[pSeq] == mId) {
			int minId = Integer.MAX_VALUE;
			for(int i=1; i<=sequence; i++) {	
				int p = find(i);
				if(p != pSeq) continue;
				if(removed[i] == 1) continue;
				
				
				int id = seqToId[i];
				minId = Math.min(minId, id);
			}
			minIdBySeq[pSeq] = minId;
		}
		used[seq] = 0;
		countBySeq[pSeq] = newCount;
		
	    return newCount;
	}
	
	public int check(int mId) {
		if(!idToSeq.containsKey(mId)) return -1;
		
		int seq = idToSeq.get(mId);
		if(removed[seq] == 1) return -1;
		
		int pSeq = find(seq);
		
		
		
	    return minIdBySeq[pSeq];
	}
	
	public int count() {
	    return townCount;
	}
	
	int find(int a) {
		if(a == parent[a]) return a;
		return parent[a] = find(parent[a]);	
	}
	
	int union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return 0;
		
		if(a != pa && b != pb) {
			townCount--;
		} else if(a == pa && b != pb) {
			if(used[pa] == 1) {
				townCount--;
			}
		} else if(a != pa && b == pb) {
			if(used[pb] == 1) {
				townCount--;
			}
		} else {
			if(used[pa] == 1 && used[pb] == 1) {
				townCount--;
			}
		}
		
		int count = countBySeq[pa] + countBySeq[pb];
		int minId = Math.min(minIdBySeq[pa], minIdBySeq[pb]);
		
		minIdBySeq[pa] = minId;
		countBySeq[pa] = count;
		
		parent[pb] = pa;
		
		return 1;
	}
	
	
	class Town {
		int seq;
		int y;
		int x;
		
		public Town(int seq, int y, int x) {
			this.seq = seq;
			this.y = y;
			this.x = x;
		}
	}
}


public class Main
{
	private final static int CMD_INIT 					= 100;
	private final static int CMD_ADD		 			= 200;
	private final static int CMD_REMOVE 				= 300;
	private final static int CMD_CHECK					= 400;
	private final static int CMD_COUNT					= 500;
	private final static UserSolution userSolution = new UserSolution();
	
	private static BufferedReader br;
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	private static boolean run() throws Exception
	{
		StringTokenizer st;

		int l, r, mId, mX, mY;
		int cmd, ans, ret;
		boolean okay = false;

		int Q = Integer.parseInt(br.readLine());

		for (int q = 0; q < Q; ++q)
		{
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd)
			{
			case CMD_INIT:
				okay = true;
				l = Integer.parseInt(st.nextToken());
				r = Integer.parseInt(st.nextToken());
				userSolution.init(l, r); 
				break;
			case CMD_ADD :
				mId = Integer.parseInt(st.nextToken());
				mX = Integer.parseInt(st.nextToken());
				mY = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.add(mId, mX, mY);
				print(q, "add", ans, ret, mId, mX, mY);
				if(ans != ret)
					okay = false;
				break;
			case CMD_REMOVE:
				mId = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.remove(mId);
				print(q, "remove", ans, ret, mId);
				if(ans != ret)
					okay = false;
				break;
			case CMD_CHECK:
				mId = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.check(mId);
				print(q, "check", ans, ret, mId);
				if(ans != ret)
					okay = false;
				break;
			case CMD_COUNT :
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.count();
				print(q, "count", ans, ret);
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

	public static void main(String[] args) throws Exception
	{
		
		//long start = System.currentTimeMillis();
		int TC, MARK;

		//System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\마을\\sample_input.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
		
		//System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}