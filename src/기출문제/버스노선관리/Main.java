package 기출문제.버스노선관리;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution
{
	int MAX = 40_001;
	int MAX_LINE = 5;
	
	int N;
	
	ArrayList<Station>[] graphForStation;
	HashSet<Integer>[] graphForLine;
	
	int[][] dist;
	
	ArrayList<Integer>[] lineByStation;
 	
	public void init(int N, int mLastStation1[], int mLastStation2[]) {
		this.N = N;
		dist = new int[MAX_LINE][N+1];
		
		lineByStation = new ArrayList[MAX];
		for(int i=0; i<MAX; i++) {
			lineByStation[i] = new ArrayList<>();
		}
		
		graphForStation = new ArrayList[N+1];
		for(int i=0; i<N+1; i++) {
			graphForStation[i] = new ArrayList<>();
		}
		
		graphForLine = new HashSet[MAX_LINE];
		for(int i=0; i<MAX_LINE; i++) {
			graphForLine[i] = new HashSet<>();
		}
		
		for(int i=0; i<mLastStation1.length; i++) {
			int from = mLastStation1[i];
			int to = mLastStation2[i];
			
			graphForStation[from].add(new Station(to, i, from));
			graphForStation[to].add(new Station(from, i, 0));
			
			if(!lineByStation[from].contains(i)) lineByStation[from].add(i);
			if(!lineByStation[to].contains(i)) lineByStation[to].add(i);
			
			makeLineGraph(from);
			makeLineGraph(to);
		}
		
		return;
	}
	
	void makeLineGraph(int station) {
		if(lineByStation[station].size() < 2) return;
		for(int i=0; i<lineByStation[station].size()-1; i++) {
			for(int j=0; j<lineByStation[station].size(); j++) {
				int from = lineByStation[station].get(i);
				int to = lineByStation[station].get(j);
				graphForLine[from].add(to);
				graphForLine[to].add(from);
			}
		}
	}

	public void add(int mLine, int mPrevStation, int mStation) {
		Station removeTarget = null;
		int targetId = 0;
		for(Station s: graphForStation[mPrevStation]) {
			if(s.prevId != mPrevStation) continue;
			if(s.line != mLine) continue;
			removeTarget = s;
			targetId = s.id;
		}
		
		graphForStation[targetId].remove(new Station(mPrevStation, mLine, mPrevStation));
		graphForStation[targetId].add(new Station(mStation, mLine, mPrevStation));
		graphForStation[mPrevStation].remove(removeTarget);
		graphForStation[mPrevStation].add(new Station(mStation, mLine, mPrevStation));
		graphForStation[mStation].add(new Station(mPrevStation, mLine, 0));
		graphForStation[mStation].add(new Station(targetId, mLine, mStation));
		
		if(!lineByStation[mPrevStation].contains(mLine)) lineByStation[mPrevStation].add(mLine);
		if(!lineByStation[mStation].contains(mLine)) lineByStation[mStation].add(mLine);
		
		makeLineGraph(mPrevStation);
		makeLineGraph(mStation);
		
		return;
	}

	public int minTravelTime(int mStartStation, int mEndStation) {
		for(int i=0; i<MAX_LINE; i++) {
			Arrays.fill(dist[i], Integer.MAX_VALUE);
		}
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		for(int line : lineByStation[mStartStation]) {
			q.add(new Node(mStartStation, line, 0));
			dist[line][mStartStation] = 0;
		}
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			if(cur.id == mEndStation) return dist[cur.line][cur.id];
			if(dist[cur.line][cur.id] < cur.cost) continue;
			
			for(Station next : graphForStation[cur.id]) {
				int nextCost = cur.cost + 1;
				if(cur.line != next.line) nextCost++;
				if(dist[next.line][next.id] <= nextCost) continue;
				dist[next.line][next.id] = nextCost;
				q.add(new Node(next.id, next.line, dist[next.line][next.id]));
			}
		}
		
		return -1;
	}

	public int minTransfer(int mStartStation, int mEndStation) {
		int[] d = new int[MAX_LINE];
		Arrays.fill(d, Integer.MAX_VALUE);
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		for(int line : lineByStation[mStartStation]) {
			q.add(new Node(line, line, 0));
			d[line] = 0;
		}
		
		ArrayList<Integer> endLines = lineByStation[mEndStation];
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			if(endLines.contains(cur.line)) return d[cur.line];
			if(d[cur.line] < cur.cost) continue;
			
			for(int next : graphForLine[cur.id]) {
				int nextCost = cur.cost + 1;
				
				if(d[next] <= nextCost) continue;
				d[next] = nextCost;
				q.add(new Node(next, next, d[next]));
			}
		}
		
		return -1;
	}
	
	class Station {
		int id;
		int line;
		int prevId;
		public Station(int id, int line, int prevId) {		
			this.id = id;
			this.line = line;
			this.prevId = prevId;
		}
		@Override
		public int hashCode() {			
			return  Objects.hash(id, line);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Station other = (Station) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			return id == other.id && line == other.line;
		}
		private UserSolution getEnclosingInstance() {
			return UserSolution.this;
		}
	}
	
	class Node {
		int id;
		int line;
		int cost;
		public Node(int id, int line, int cost) {		
			this.id = id;
			this.line = line;
			this.cost = cost;
		}
	}
}

public class Main {
	private final static int CMD_INIT				= 1;
	private final static int CMD_ADD				= 2;
	private final static int CMD_MIN_TRAVEL_TIME	= 3;
	private final static int CMD_MIN_TRANSFER		= 4;
	
	private final static UserSolution usersolution = new UserSolution();

	private final static int numberOfLine = 5;
	private static int[] mLastStation1 = new int[numberOfLine];
	private static int[] mLastStation2 = new int[numberOfLine];

	private static boolean run(BufferedReader br) throws Exception
	{
		StringTokenizer st;

		int numQuery;

		int N;
		int mLine, mPrevStation, mStation;
		int mStartStation, mEndStation;

		int userAns, ans;

		boolean isCorrect = false;

		numQuery = Integer.parseInt(br.readLine());

		for (int q = 0; q < numQuery; ++q)
		{
			st = new StringTokenizer(br.readLine(), " ");

			int cmd;
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd)
			{
			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				for (int i = 0; i < numberOfLine; i++)
					mLastStation1[i] = Integer.parseInt(st.nextToken());
				for (int i = 0; i < numberOfLine; i++)
					mLastStation2[i] = Integer.parseInt(st.nextToken());
				usersolution.init(N, mLastStation1, mLastStation2);
				isCorrect = true;
				break;
			case CMD_ADD:
				mLine = Integer.parseInt(st.nextToken());
				mPrevStation = Integer.parseInt(st.nextToken());
				mStation = Integer.parseInt(st.nextToken());
				usersolution.add(mLine, mPrevStation, mStation);
				print(q, "add", q, q, mLine, mPrevStation, mStation);
				break;
			case CMD_MIN_TRAVEL_TIME:
				mStartStation = Integer.parseInt(st.nextToken());
				mEndStation = Integer.parseInt(st.nextToken());
				userAns = usersolution.minTravelTime(mStartStation, mEndStation);
				ans = Integer.parseInt(st.nextToken());
				print(q, "minTravelTime", ans, userAns, mStartStation, mEndStation);
				if (userAns != ans)
				{
					isCorrect = false;
				}
				break;
			case CMD_MIN_TRANSFER:
				mStartStation = Integer.parseInt(st.nextToken());
				mEndStation = Integer.parseInt(st.nextToken());
				userAns = usersolution.minTransfer(mStartStation, mEndStation);
				ans = Integer.parseInt(st.nextToken());
				print(q, "minTransfer", ans, userAns, mStartStation, mEndStation);
				if (userAns != ans)
				{
					isCorrect = false;
				}
				break;
			default:
				isCorrect = false;
				break;
			}
		}
		return isCorrect;
	}
	 static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception
	{
		
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\버스노선관리\\sample_input.txt"));
	

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