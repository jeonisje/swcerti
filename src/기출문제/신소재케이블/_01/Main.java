package 기출문제.신소재케이블._01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	
	final int MAX_N = 10_002;
	final int MAX_DEVICE = 101;
	final int BIG_INT = 10_001;
	
	HashMap<Integer, Integer> idToSeq;
	int[] seqToId;
	
	ArrayList<Node>[] graph;
	int sequence;
	
	public void init(int mDevice) {
		sequence = 0;
		
		idToSeq = new HashMap<>();
		seqToId = new int[MAX_N];
		
		graph = new ArrayList[MAX_N];
		
		for(int i=0; i < MAX_N; i++) {
			graph[i] = new ArrayList<>();
		}
		
		sequence++;
		idToSeq.put(mDevice, sequence);
		seqToId[sequence] = mDevice;
	}
	 
	public void connect(int mOldDevice, int mNewDevice, int mLatency) {
	    int oldSeq = idToSeq.get(mOldDevice);
	    
	    sequence++;
	    idToSeq.put(mNewDevice, sequence);
	    seqToId[sequence] = mNewDevice;
	    
	    graph[oldSeq].add(new Node(sequence, mLatency));
	    graph[sequence].add(new Node(oldSeq, mLatency));
	    
	}
	 
	public int measure(int mDevice1, int mDevice2) {
		int[] dist = new int[MAX_N + 1];
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		int from = idToSeq.get(mDevice1);
		int to = idToSeq.get(mDevice2);
		
		return dijkstra(from, to);		
	}
	 
	public int test(int mDevice) {
		int testSeq = idToSeq.get(mDevice);
		
		ArrayList<Integer> edge = new ArrayList<>();
		for(int i=1; i<MAX_DEVICE; i++) {
			if(graph[i].size() == 1) {
				edge.add(i);
			}
		}
		
		int Answer = 0;
		
		if(edge.contains(testSeq)) {
			for(int i=0; i<edge.size(); i++) {
				int end = edge.get(i);
				if(end == testSeq) continue;
				int ret = dijkstra(testSeq, end);
				Answer = Math.max(Answer, ret);				
			} 
		} else {
			
			for(int i=0; i<edge.size()-1; i++) {
				int ret1 = 0;
				int ret2 = 0;
				for(int j=i+1; j<edge.size(); j++) {
					int start = edge.get(i);
					int end = edge.get(j);
					
					ret1 = dijkstra2(start, testSeq, end);
					ret2 = dijkstra2(testSeq, end, start);					
					
					Answer = Math.max(Answer, ret1 + ret2);
				}
			}
		}
	    return Answer;
	}
	
	int bfs(int start, int end, int test) {
		int[] visited = new int[MAX_N];
		ArrayDeque<Node> q = new ArrayDeque<>();
		boolean passTest = start == test || end == test ? true : false;
		
		q.add(new Node(start, 0, passTest));
		
		int max = 0;
		while(!q.isEmpty()) {
			Node cur = q.poll();
			if(cur.seq == end) {
				if(cur.passTest) {
					max = Math.max(max, cur.cost);
				}
			}
			
			for(Node next : graph[cur.seq]) {
				if(visited[next.seq] != 0) continue;
				boolean passT = cur.passTest;
				if(next.seq == test) {
					passT = true;
				}
				q.add(new Node(next.seq, cur.cost + next.cost, passT));
				visited[next.seq] = 1;
			}
					
		}
		
		return max;
	}
	
	int bfs2(int start, int end, int skip) {
		int[] visited = new int[MAX_N];
		ArrayDeque<Node> q = new ArrayDeque<>();
		
		q.add(new Node(start, 0));
		
		int max = 0;
		while(!q.isEmpty()) {
			Node cur = q.poll();
			if(cur.seq == end) {
				max = Math.max(max, cur.cost);
				continue;
			}
			
			for(Node next : graph[cur.seq]) {
				if(visited[next.seq] != 0) continue;
				if(next.seq == skip) continue;
								
				q.add(new Node(next.seq, cur.cost + next.cost));
				visited[next.seq] = 1;
			}
					
		}
		
		return max;
	}
	
	
	int dijkstra(int from, int to) {
		int[] dist = new int[MAX_N + 1];
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Node(from, 0));
		
		
		dist[from] = 0;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.seq == to) {				
				return dist[cur.seq];
			}
			
			if(dist[cur.seq] < cur.cost) continue;
			
			for(Node next : graph[cur.seq]) {				
				if(dist[next.seq] < cur.cost + next.cost)  continue;
				dist[next.seq] = cur.cost + next.cost;
				q.add(new Node(next.seq, dist[next.seq]));				
			}
		}
		
		return -1;
	}
	
	int dijkstra2(int from, int to, int skip) {
		int[] dist = new int[MAX_N + 1];
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		//q.add(new Node(from, 0));
		q.add(new Node(from, 0, String.valueOf(from)+"(0)"));
		
		dist[from] = 0;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.seq == to) {
				System.out.println(from + "->" + to);
				System.out.println("total cost : " + dist[cur.seq]);
				System.out.println(cur.path);
				return dist[cur.seq];
			}
			
			if(dist[cur.seq] < cur.cost) continue;
			
			for(Node next : graph[cur.seq]) {
				if(next.seq == skip) continue;
				if(dist[next.seq] < cur.cost + next.cost)  continue;
				dist[next.seq] = cur.cost + next.cost;
				//q.add(new Node(next.seq, dist[next.seq]));
				q.add(new Node(next.seq, dist[next.seq], cur.path + "->" + next.seq + "("+next.cost+")"));
			}
		}
		
		return -1;
	}
	
	
	class Node {
		int seq;
		int cost;
		boolean passTest;
		String path;
		
		public Node(int seq, int cost) {
			this.seq = seq;
			this.cost = cost;
		}

		public Node(int seq, int cost, boolean passTest) {
			this.seq = seq;
			this.cost = cost;
			this.passTest = passTest;
		}
		public Node(int seq, int cost, String path) {
			this.seq = seq;
			this.cost = cost;
			this.path = path;
		}
	}
}


public class Main {
	private static final int CMD_INIT 		= 100;
	private static final int CMD_CONNECT	= 200;
	private static final int CMD_MEASURE	= 300;
	private static final int CMD_TEST 		= 400; 

	private static UserSolution userSolution = new UserSolution();
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	static void print(int q, String cmd, int ans, int ret, Object...o) {
		if(ans!=ret)  System.err.println("----------------------오류--------------------");
		System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	
	private static boolean run(BufferedReader br) throws Exception {
		
		int Q;
		int mDevice, mOldDevice, mNewDevice, mDevice1, mDevice2;
		int mLatency;
		int ret = -1, ans; 
		boolean okay = false; 
		
		st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {
			
			st = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) {
			case CMD_INIT:
				mDevice = Integer.parseInt(st.nextToken());
				userSolution.init(mDevice); 
				okay = true;
				break; 
				
			case CMD_CONNECT:
				mOldDevice = Integer.parseInt(st.nextToken());
				mNewDevice = Integer.parseInt(st.nextToken());
				mLatency = Integer.parseInt(st.nextToken());
				userSolution.connect(mOldDevice, mNewDevice, mLatency);
				print(q, "connect", q, q, mOldDevice, mNewDevice, mLatency);
				break;
				
			case CMD_MEASURE:
				mDevice1 = Integer.parseInt(st.nextToken());
				mDevice2 = Integer.parseInt(st.nextToken());
				ret = userSolution.measure(mDevice1, mDevice2);
				ans = Integer.parseInt(st.nextToken());
				print(q, "measure", ans, ret, mDevice1, mDevice2);
				if(ans != ret)
					okay = false; 
				break;
			
			case CMD_TEST:
				mDevice = Integer.parseInt(st.nextToken());
				ret = userSolution.test(mDevice);
				ans = Integer.parseInt(st.nextToken());
				print(q, "test", ans, ret, mDevice);
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
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\신소재케이블\\sample_input2.txt"));

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