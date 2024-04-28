package 연습문제.예약버스;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer; 

class UserSolution {
	
	final int MAX_N = 501;
	
	int N, K;
	int[] roadAs;
	int[] roadBs;
	int[] lens;
	
	ArrayList<Node>[] graph;
	
	public void init(int N, int K, int mRoadAs[], int mRoadBs[], int mLens[]) {
		this.N = N;
		this.K = K;
		this.roadAs = mRoadAs;
		this.roadBs = mRoadBs;
		this.lens = mLens;
		
		graph = new ArrayList[MAX_N];
		for(int i=1; i<MAX_N; i++) {
			graph[i] = new ArrayList<>();
		}
				
		for(int i=0; i<K; i++) {
			int a = roadAs[i];
			int b = roadBs[i];
			int cost = lens[i];
			
			graph[a].add(new Node(b, cost));
			graph[b].add(new Node(a, cost));
		}
		
		int a=1;
	}
	
	public void addRoad(int mRoadA, int mRoadB, int mLen) {
		graph[mRoadA].add(new Node(mRoadB, mLen));
		graph[mRoadB].add(new Node(mRoadA, mLen));
	}
    
	public int findPath(int mStart, int mEnd, int M, int mStops[]){
		//return dijkstra(mStart, mEnd, M, mStops) ;
		
		return bfs(mStart, mEnd, M, mStops) ;
		/*
		int[] visited = new int[M+1];
		int ret = 0;
		
		int visitedCount = 0;
		int start = mStart;
		int checkIdx = 0;
		int idx = 0;
		while(visitedCount < M) {
			int minCost = Integer.MAX_VALUE;
			
			for(int i=0; i<M; i++) {
				if(visited[i] == 1) continue;
				
				int d = dijkstra(start, mStops[i]);
				
				if(d == -1) return -1;
				
				if(d < minCost) {
					minCost = d;
					idx = mStops[i];xhlrm
					checkIdx = i;
				}
			}
			start = idx;
			visited[checkIdx] = 1;
			visitedCount++;
			ret += minCost;
		}
		
		int d =  dijkstra(idx, mEnd);
		if(d == -1) return -1;
		
		return ret + d;*/
	}
	
	int dijkstra(int start, int end, int M, int mStops[]) {
		int[] dist = new int[N+1];
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Node(start, 0));
		dist[0] = 0;
		
		int[] layover = new int[MAX_N+1];
		for(int i=0; i<M; i++) {
			layover[mStops[i]] = 1;
		}

		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.idx == end && cur.visitedCount == M) return dist[cur.idx];
			if(dist[cur.idx] < cur.cost) continue;
			
			for(Node next : graph[cur.idx]) {
				int nextCost = cur.cost + next.cost;
				if(layover[next.idx] == 0 && dist[next.idx] <= nextCost) continue;
				if(layover[cur.idx] == 1 && next.idx == cur.preNodeIdx) continue;
				
				if(layover[next.idx] == 0) dist[next.idx] = nextCost;
				
				
				
				q.add(new Node(next.idx, nextCost, 0, cur.visitedCount + layover[next.idx], cur.idx));
			}
		}
		
		return -1;
				
	}
	
	int bfs(int start, int end, int M, int mStops[]) {
		int vistied[] = new int[N+1];
		ArrayDeque<Node> q = new ArrayDeque<Node>();
		q.add(new Node(start, 0));
		
		vistied[start] = 1;
		
		int[] layover = new int[MAX_N+1];
		for(int i=0; i<M; i++) {
			layover[mStops[i]] = 1;
		}
		
		boolean found = false;
		int minCost = Integer.MAX_VALUE;
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.idx == end && cur.visitedCount == M) {
				found = true;
				minCost = Math.min(cur.cost, minCost);
			}
			
			for(Node next : graph[cur.idx]) {
				if(vistied[next.idx] == 1) continue;
				if(layover[cur.idx] == 0) vistied[next.idx] = 1;
				
				q.add(new Node(next.idx, cur.cost + next.cost, 0, cur.visitedCount + layover[next.idx], cur.idx));
			}
			
		}
		
		
		return -1;
	}
	
	class Node {
		int idx;
		int cost;
		int tempCost;
		int visitedCount;
		int preNodeIdx;
		
		public Node(int idx, int cost, int tempCost, int visitedCount, int preNodeIdx) {
			super();
			this.idx = idx;
			this.cost = cost;
			this.tempCost = tempCost;
			this.visitedCount = visitedCount;
			this.preNodeIdx = preNodeIdx;
		}

		public Node(int idx, int cost) {			
			this.idx = idx;
			this.cost = cost;
		}
	}
}


public class Main {
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
	
	private static void print(int num, String cmd, int ans, int ret, Object...o) {
		if(ans!=ret) System.err.println("===================오류=======================");
		System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
	}	
	
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
				print(q, "findPath", ret, ans, mStops, mStart, mEnd);
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

	public static void main(String[] args) throws Exception
	{
		int TC, MARK;

		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//연습문제//예약버스//sample_input2.txt"));
		
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
	}
}