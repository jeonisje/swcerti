package 기출문제.예약버스._01;


import java.io.*;
import java.util.*;

class UserSolution {
	
	int N;
	ArrayList<Node>[] graph;
	ArrayList<Node>[] summaryGraph;
	
	ArrayList<Integer> stops;
	
	int[] dist;
	int[][] made;
		
	int cc;
	
	public void init(int N, int K, int mRoadAs[], int mRoadBs[], int mLens[]) {
		this.N = N;
		graph = new ArrayList[N+1];
		summaryGraph = new ArrayList[N+1];
		dist = new int[N+1];
		made = new int[N+1][N+1];
		
		for(int i=0;  i<N+1; i++) {
			graph[i] = new ArrayList<>();
		}
		
		for(int i=0; i<K ; i++) {
			int from = mRoadAs[i];
			int to = mRoadBs[i];
			int cost = mLens[i];
			
			graph[from].add(new Node(to, cost));
			graph[to].add(new Node(from, cost));
		}
		
		cc = 0;
		return;
	}
	
	public void addRoad(int mRoadA, int mRoadB, int mLen) {
		graph[mRoadA].add(new Node(mRoadB, mLen));
		graph[mRoadB].add(new Node(mRoadA, mLen));
		return;
	}
    
	public int findPath(int mStart, int mEnd, int M, int mStops[]){		
		cc++;
		int[] arr = new int[M+2];
		int idx = 0;
		arr[idx++] = mStart;
		for(int i=0; i<M; i++) {
			arr[idx++] = mStops[i];
		}
		arr[idx] = mEnd;
		
		for(int i=0; i<N+1; i++) {
			summaryGraph[i] = new ArrayList<>();
		}
		
		for(int i=0; i<M+1; i++) {
			int from = arr[i];
			dijkstra(from, mEnd, mStart);
			
			for(int j=0; j<M+2; j++) {
				int to  = arr[j];
				if(from == to) continue;
				if(dist[to] == Integer.MAX_VALUE) continue;
				
				if(made[from][to] == cc) continue;
				
				summaryGraph[from].add(new Node(to, dist[to]));
				summaryGraph[to].add(new Node(from, dist[to]));
				
				made[from][to] = cc;
				made[to][from] = cc;
			}
		}
	
		return bfs(mStart, mEnd, M+2);
	}
	
	void dijkstra(int from, int to, int start) {
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[from] = 0;
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Node(from, 0));
		while(!q.isEmpty()) {
			Node cur = q.remove();
			if(cur.id == to) continue;
			if(dist[cur.id] < cur.cost) continue;
			
			for(Node next : graph[cur.id]) {
				int nextCost = cur.cost + next.cost;
				if(dist[next.id] <= nextCost) continue;
				if(next.id == start) continue;
				dist[next.id] = nextCost;
				q.add(new Node(next.id, dist[next.id]));
			}
		}
	}
	
	int bfs(int start, int end, int M) {
		ArrayDeque<Node2> q = new ArrayDeque<Node2>();
		q.add(new Node2(start, 0, new ArrayList<Integer>(Arrays.asList(start))));
		
		int min = Integer.MAX_VALUE;
		while(!q.isEmpty()) {
			Node2 cur = q.remove();
			if(cur.id == end) {
				if(cur.path.size() == M) {
					min = Math.min(min, cur.cost);
				}
				continue;
			}
			
			for(Node next : summaryGraph[cur.id]) {
				if(cur.path.contains(next.id)) continue;
				ArrayList<Integer> path = new ArrayList<>();
				path.addAll(cur.path);
				path.add(next.id);
				q.add(new Node2(next.id, cur.cost + next.cost, path));
			}
		}
		
		return min == Integer.MAX_VALUE ? -1 : min;
	}
	
	class Node {
		int id;
		int cost;
		public Node(int id, int cost) {
			this.id = id;
			this.cost = cost;
		}
	}
	
	class Node2 {
		int id;
		int cost;
		ArrayList<Integer> path;
		public Node2(int id, int cost, ArrayList<Integer> path) {
			this.id = id;
			this.cost = cost;
			this.path = path;
		}		
	}
}

public class Main
{
	private final static int CMD_INIT 					= 1;
	private final static int CMD_ADD		 			= 2;
	private final static int CMD_FIND 				 	= 3;
	private final static int MAX_K						= 1000;
	private final static int MAX_M						= 5;
	private final static UserSolution usersolution = new UserSolution();
	
	private static int[] mRoadAs;
	private static int[] mRoadBs;
	private static int[] mLens; 
	private static int[] mStops;

	private static BufferedReader br;
	private static boolean run() throws Exception
	{
		StringTokenizer st;

		int numQuery;
		int N, K,mRoadA, mRoadB, mLen, mStart, mEnd, M;
		int ans, ret; 
		boolean isCorrect = false; 

		numQuery = Integer.parseInt(br.readLine());

		for (int q = 0; q < numQuery; ++q)
		{
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());

			switch (cmd)
			{
			case CMD_INIT:
				mRoadAs = new int[MAX_K];
				mRoadBs = new int[MAX_K]; 
				mLens = new int[MAX_K];
				N = Integer.parseInt(st.nextToken());
				K = Integer.parseInt(st.nextToken());
				for(int i = 0; i < K; i++) 
					mRoadAs[i] = Integer.parseInt(st.nextToken());
				for(int i = 0; i < K; i++) 
					mRoadBs[i] = Integer.parseInt(st.nextToken());
				for(int i = 0; i < K; i++) 
					mLens[i] = Integer.parseInt(st.nextToken());
				usersolution.init(N, K, mRoadAs, mRoadBs, mLens);
				isCorrect = true;
				break;
			case CMD_ADD :
				mRoadA = Integer.parseInt(st.nextToken());
				mRoadB = Integer.parseInt(st.nextToken());
				mLen = Integer.parseInt(st.nextToken());
				usersolution.addRoad(mRoadA, mRoadB, mLen);
				print(q, "addRoad", q, q, mRoadA, mRoadB, mLen);
				break;
			case CMD_FIND:
				mStops = new int[MAX_M];
				mStart = Integer.parseInt(st.nextToken());
				mEnd = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				for(int i = 0; i < M; i++) 
					mStops[i] = Integer.parseInt(st.nextToken());
				ret = usersolution.findPath(mStart, mEnd, M, mStops); 
				ans = Integer.parseInt(st.nextToken());
				print(q, "findPath", ans, ret, mStart, mEnd, M, mStops);
				if(ret != ans) {
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
	
	static void print(int q, String cmd, int ans, int ret, Object...objects) {
		//if(ans != ret) System.err.println("---------------오류----------------");
		//System.out.println("[" + q +"] " + cmd + " : " + ans + "=" + ret + "("+Arrays.deepToString(objects)+")" );
	}

	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		int TC, MARK;

		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\예약버스\\sample_input.txt"));
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
		
		System.out.println("ms =>" + (System.currentTimeMillis() - start));
	}
}