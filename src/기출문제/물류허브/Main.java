package 기출문제.물류허브;

import java.io.*;
import java.util.*;

class UserSolution {
	
	int MAX = 3_001;	
	HashMap<Integer, Integer> idToSeq;
	
	ArrayList<Node>[] graph;
	int[] dist;
	int[][] costMap;
	
	int sequence;
	
	public int init(int N, int[] sCity, int[] eCity, int[]mCost) {
		
		idToSeq = new HashMap<>();
		graph = new ArrayList[MAX];
		for(int i=0; i<MAX; i++) {
			graph[i] = new ArrayList<>();
		}
		
		dist = new int[MAX];
		costMap = new int[MAX][MAX];
		sequence = 0;
		
		for(int i=0; i<N; i++) {
			int from = 0;
			int to = 0;
			if(idToSeq.containsKey(sCity[i])) {
				from = idToSeq.get(sCity[i]);
			} else {
				sequence++;
				from = sequence;
				idToSeq.put(sCity[i], sequence);
			}
			
			if(idToSeq.containsKey(eCity[i])) {
				to = idToSeq.get(eCity[i]);
			} else {
				sequence++;
				to = sequence;
				idToSeq.put(eCity[i], sequence);
			}
			
			graph[from].add(new Node(to, mCost[i]));
		}
		
		
		return idToSeq.size();
	}
	
	public void add(int sCity, int eCity, int mCost) {
		int from = 0;
		int to = 0;
		if(idToSeq.containsKey(sCity)) {
			from = idToSeq.get(sCity);
		} else {
			sequence++;
			from = sequence;
			idToSeq.put(sCity, sequence);
		}
		
		if(idToSeq.containsKey(eCity)) {
			to = idToSeq.get(eCity);
		} else {
			sequence++;
			to = sequence;
			idToSeq.put(eCity, sequence);
		}
		
		graph[from].add(new Node(to, mCost));
		return; 
	}
	
	public int cost(int mHub) {
		int start = idToSeq.get(mHub);
		
		Arrays.fill(dist, Integer.MAX_VALUE);
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Node(start, 0));
		dist[start] = 0;
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			if(dist[cur.id] < cur.cost) continue;
			for(Node next : graph[cur.id]) {
				int nextCost = cur.cost + next.cost;
				if(dist[next.id] <= nextCost) continue;
				dist[next.id] = nextCost;
				q.add(new Node(next.id, dist[next.id]));
			}
		}
		
		int cost = 0;
		for(int i=0; i<=sequence; i++) {
			if(dist[i] == Integer.MAX_VALUE) continue;
			cost += dist[i];
		}
		
		for(int i=1; i<=sequence; i++) {			
			if(i == start) continue;
			cost += dijkstra(i, start);
			
		}
		
		
		return cost; 
	}
	
	int dijkstra(int start, int end) {
		Arrays.fill(dist, Integer.MAX_VALUE);
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Node(start, 0));
		dist[start] = 0;
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			if(cur.id == end) return dist[cur.id];
			if(dist[cur.id] < cur.cost) continue;
			for(Node next : graph[cur.id]) {
				int nextCost = cur.cost + next.cost;
				if(dist[next.id] <= nextCost) continue;
				dist[next.id] = nextCost;
				q.add(new Node(next.id, dist[next.id]));
			}
		}
		
		return -1;
	}
	
	class Node {
		int id;
		int cost;
		public Node(int id, int cost) {		
			this.id = id;
			this.cost = cost;
		}
	}
}

public class Main {
	private final static int CMD_INIT 	= 1;
	private final static int CMD_ADD 	= 2;
	private final static int CMD_COST 	= 3;
	private final static int MAX_N 		= 1400; 

	private final static UserSolution usersolution = new UserSolution();
	
	private static boolean run(BufferedReader br) throws Exception {
		StringTokenizer st;
		int Q; 
		int N;
		int[] sCityArr = new int[MAX_N];
		int[] eCityArr = new int[MAX_N];
		int[] mCostArr = new int[MAX_N];
		int sCity, eCity, mCost, mHub;
		int cmd, ans, userAns; 
		boolean isCorrect = false; 
		
		st = new StringTokenizer(br.readLine());
		Q = Integer.parseInt(st.nextToken());
		
		for (int q = 0; q <Q; ++q) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) {
				case CMD_INIT:
					isCorrect = true;
					N = Integer.parseInt(st.nextToken());
					for(int i = 0; i < N; i++) {
						st = new StringTokenizer(br.readLine());
						sCityArr[i] = Integer.parseInt(st.nextToken());
						eCityArr[i] = Integer.parseInt(st.nextToken());
						mCostArr[i] = Integer.parseInt(st.nextToken());
					}
					st = new StringTokenizer(br.readLine());
					ans = Integer.parseInt(st.nextToken());
					userAns = usersolution.init(N, sCityArr, eCityArr, mCostArr);
					print(q, "init", ans, userAns, N);
					if(userAns != ans)
						isCorrect = false; 	
					break;
				case CMD_ADD:
					sCity = Integer.parseInt(st.nextToken());
					eCity = Integer.parseInt(st.nextToken());
					mCost= Integer.parseInt(st.nextToken());
					usersolution.add(sCity, eCity, mCost);
					print(q, "add", q, q, sCity, eCity, mCost);
					break;
				case CMD_COST:
					mHub = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					userAns = usersolution.cost(mHub);
					print(q, "cost", ans, userAns, mHub);
					if(userAns != ans)
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
		//if(ans!=ret) System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\물류허브\\sample_input.txt"));
		
		
		int TC, MARK;
	
		//System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}