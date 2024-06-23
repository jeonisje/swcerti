package 기출문제.문명;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

class UserSolution {
	int MAX = 70_001;
	int N;
	
	int[][] map;
	HashMap<Integer, Integer> idToSeq;
	int[] seqToId;
	
	int[] parent;
	int[] countBySeq;
	
	int[] removed;
	
	int[] directY = {-1, 1, 0, 0};
	int[] directX = {0, 0, -1, 1};
	
	int sequence;
	
	Civil[] civilInfo;
	
	int find(int a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		parent[pb] = pa;		
		countBySeq[pa] += countBySeq[pb];
		idToSeq.remove(b);
		idToSeq.remove(pb);
		
		ArrayList<Loc> locA = civilInfo[pa].locations;
		ArrayList<Loc> locB = civilInfo[pb].locations;
		
		if(locA.size() < locB.size()) {
			locB.addAll(locA);
			civilInfo[pa].locations = locB;
		} else {
			locA.addAll(locB);
			civilInfo[pa].locations = locA;
		}		
	}
	
	void init(int N) {
		this.N = N;
		
		map = new int[N+1][N+1];
		
		idToSeq = new HashMap<>();
		seqToId = new int[MAX];		
		parent = new int[MAX];
		countBySeq = new int[MAX];
		removed = new int[MAX];
		
		civilInfo = new Civil[MAX];
		
		for(int i=0; i<MAX; i++) {
			parent[i] = i;
			countBySeq[i] = 1;
		}
		
		sequence = 0;
		
	}
	// 60_000
	int newCivilization(int r, int c, int mID) {		
		HashMap<Integer, Integer> found = new HashMap<>();
		
		for(int i=0; i<4; i++) {
			int nr = r + directY[i];
			int nc = c + directX[i];
			
			if(nr < 1 || nc < 1 || nr > N || nc > N) continue;			
			if(map[nr][nc] == 0) continue;
			
			int pseq = find(map[nr][nc]);
			if(removed[pseq] == 1) continue;
			
			if(found.containsKey(pseq)) {
				found.put(pseq, found.get(pseq) + 1);
			} else {
				found.put(pseq, 1);
			}
		}
		
		
		
		if(found.isEmpty()) {
			int seq = getSequence(mID);
			int pseq = find(seq);
			
			map[r][c] = pseq;
			if(civilInfo[pseq] == null) {
				civilInfo[pseq] = new Civil(mID, pseq, new ArrayList<>(Arrays.asList(new Loc(r, c))));
			} else {
				civilInfo[pseq].locations.add(new Loc(r, c));
			}
			
			return mID;
		} 
			
		int max = 0;
		int maxId = 0;
		int maxSeq = 0;
		for(Map.Entry<Integer, Integer> entry : found.entrySet()) {
			int pseq = entry.getKey();
			int count = entry.getValue();
			
			if(max < count) {
				max = count;
				maxId = seqToId[pseq];
				maxSeq = pseq;
			} else if (max == count){
				if(maxId > seqToId[pseq]) {
					maxId = seqToId[pseq];
					maxSeq = pseq;
				}
			}
		}
		
		map[r][c] = idToSeq.get(maxId);
		countBySeq[maxSeq]++;
		
		if(civilInfo[maxSeq] == null) {
			civilInfo[maxSeq] = new Civil(mID, maxSeq, new ArrayList<>(Arrays.asList(new Loc(r, c))));
		} else {
			civilInfo[maxSeq].locations.add(new Loc(r, c));
		}
		
		return maxId;
	}
	
	int getSequence(int id) {
		int seq = 0;
		if(idToSeq.containsKey(id)) {
			seq = idToSeq.get(id);
		} else {
			sequence++;
			seq = sequence;
			idToSeq.put(id, sequence);
			seqToId[sequence] = id;
		}
		
		return seq;
	}
	
	// 3_000
	int removeCivilization(int mID) {
		if(!idToSeq.containsKey(mID)) return 0;
		int seq = idToSeq.get(mID);
		int pseq = find(seq);
		
		if(removed[pseq] == 1) return 0;
		removed[pseq] = 1;	
		idToSeq.remove(mID);
		
		for(Loc loc : civilInfo[pseq].locations) {
			map[loc.row][loc.col] = 0;
		}
		
		return countBySeq[pseq];
	}
	// 10_000
	int getCivilization(int r ,int c) {
		if(map[r][c] == 0) return 0;
		
		int pseq = find(map[r][c]);
		if(removed[pseq] == 1) return 0;
		
		return seqToId[pseq];
	}
	// 10_000
	int getCivilizationArea(int mID) {
		if(!idToSeq.containsKey(mID)) return 0;
		int seq = idToSeq.get(mID);
		int pseq = find(seq);
		
		if(removed[pseq] == 1) return 0;
		
		return countBySeq[pseq];
	}
	// 30_000
	int mergeCivilization(int mID1, int mID2) {
		int seq1 = idToSeq.get(mID1);
		int seq2 = idToSeq.get(mID2);
		
		int pseq1 = find(seq1);
		int pseq2 = find(seq2);
		
		union(pseq1, pseq2);
		
		return countBySeq[pseq1];
	}
	
	class Civil {
		int id;
		int seq;
		ArrayList<Loc> locations;
		public Civil(int id, int seq, ArrayList<Loc> locations) {		
			this.id = id;
			this.seq = seq;
			this.locations = locations;
		}
	}
	
	class Loc {
		int row;
		int col;
		public Loc(int row, int col) {		
			this.row = row;
			this.col = col;
		}
	}
}


public class Main
{
	private final static int INIT					= 100;
	private final static int NEW_CIVILIZATION		= 200;
	private final static int REMOVE_CIVILIZATION	= 300;
	private final static int GET_CIVILIZATION		= 400;
	private final static int GET_CIVILIZATION_AREA	= 500;
	private final static int MERGE_CIVILIZATION		= 600;
	
	private final static UserSolution usersolution = new UserSolution();

	private static boolean run(BufferedReader br) throws Exception
	{
		int Q, N, r, c, cmd;
		int mID, mID1, mID2;
			
		int ans, ret = 0;
	
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken());
		
		boolean okay = false;
    
		for (int i = 0; i <= Q; ++i)
		{
			st = new StringTokenizer(br.readLine(), " ");

			cmd = Integer.parseInt(st.nextToken());		
			switch(cmd)
			{
			case INIT:
				N = Integer.parseInt(st.nextToken());		
				usersolution.init(N);
				okay = true;
				break;
			case NEW_CIVILIZATION:
				r   = Integer.parseInt(st.nextToken());
				c   = Integer.parseInt(st.nextToken());
				mID = Integer.parseInt(st.nextToken());
				ret = usersolution.newCivilization(r, c, mID);
				ans = Integer.parseInt(st.nextToken());
				print(i, "newCivilization", ans, ret, r, c, mID);
				if (ans != ret)
					okay = false;
				break;
			case REMOVE_CIVILIZATION:
				mID = Integer.parseInt(st.nextToken());
				ret = usersolution.removeCivilization(mID);
				ans = Integer.parseInt(st.nextToken());
				print(i, "removeCivilization", ans, ret, mID);
				if (ans != ret)
					okay = false;
				break;
			case GET_CIVILIZATION:
				r = Integer.parseInt(st.nextToken());
				c = Integer.parseInt(st.nextToken());
				ret = usersolution.getCivilization(r, c);
				ans = Integer.parseInt(st.nextToken());
				print(i, "getCivilization", ans, ret, r, c);
				if (ans != ret)
					okay = false;
				break;
			case GET_CIVILIZATION_AREA:
				mID = Integer.parseInt(st.nextToken());
				ret = usersolution.getCivilizationArea(mID);
				ans = Integer.parseInt(st.nextToken());	
				print(i, "getCivilizationArea", ans, ret, mID);
				if (ans != ret)
					okay = false;
				break;
			case MERGE_CIVILIZATION:
				mID1 = Integer.parseInt(st.nextToken());
				mID2 = Integer.parseInt(st.nextToken());		
				ret = usersolution.mergeCivilization(mID1, mID2);
				ans = Integer.parseInt(st.nextToken());
				print(i, "mergeCivilization", ans, ret, mID1, mID2);
				if (ans != ret)
					okay = false;
				break;
			default:
				okay = false;
			}
		}
		    
		return okay;
	}
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		if(ans!=ret) System.err.println("----------------------오류--------------------");
		System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}


	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\문명\\sample_input3.txt"));
		
		
		int TC, MARK;
	
		//System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run(br) ? MARK : 0;

			System.out.println("#" + testcase + " " + score);
		}

		br.close();
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}