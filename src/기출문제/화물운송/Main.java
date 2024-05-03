package 기출문제.화물운송;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	final int LIMIT_WEIGHT = 30_001;
	int N, K;
	
	ArrayList<Node>[] graph;
	ArrayList<Node>[] graph2;
	
	public void init(int N, int K, int[] sCity, int[] eCity, int[] mLimit) {
		this.N = N;
		this.K = K;
		
		graph = new ArrayList[N+1];
		graph2 = new ArrayList[N+1];
		for(int i=0; i<N+1; i++) {
			graph[i] = new ArrayList<>();
			graph2[i] = new ArrayList<>();
		}
		
		for(int i=0; i<K; i++) {
			graph[sCity[i]].add(new Node(eCity[i], mLimit[i]));			
		}
		
		for(int i=0; i<N+1; i++) {
			if(graph[i].size() == 0) continue;
			
			if(graph[i].size() == 1) {
				graph2[i].addAll(graph[i]);
			}
			
			
			
		}
		
		
		return;
	}
	
	
	int bfs(int start, int end) {
		int[] visited = new int[N];
		visited[start]  = 1;
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(start,0));
		
		int max = 0;
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.id == end) {
				max = Math.max(cur.limit, max);
			}
			
			for(Node next : graph[cur.id]) {
				if(visited[next.id] == 1) continue; 
				int limit = Math.min(cur.limit, next.limit);
				q.add(new Node(next.id, limit));
			}
		}
		
		return max;
	}
	

	public void add(int sCity, int eCity, int mLimit) {
		graph[sCity].add(new Node(eCity, mLimit));
		return; 
	}

	public int calculate(int sCity, int eCity) {
		return 0; 
	}
	
	int dijkstra(int start, int end) {
		int[] dist = new int[N+1];
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		PriorityQueue<Node> q = new PriorityQueue<Node>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Node(start, 0, Integer.MAX_VALUE));
		
		
		
		
		return -1;
	}
	
	
	class Node {
		int id;
		int limit;
		public Node(int id, int limit) {		
			this.id = id;
			this.limit = limit;
		}
	}
}

public class Main {
	private static final int CMD_INIT 	= 100;
	private static final int CMD_ADD 	= 200;
	private static final int CMD_CALC 	= 300;

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {

		int query_num;
		st = new StringTokenizer(br.readLine(), " ");
		query_num = Integer.parseInt(st.nextToken());
		boolean ok = false;
		int n, k;
		int[] sCityArr, eCityArr, mLimitArr;
		int sCity, eCity, mLimit; 
		int cmd, ans, ret; 

		for (int q = 0; q < query_num; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int query = Integer.parseInt(st.nextToken());

			switch (query) {

			case CMD_INIT:
				n = Integer.parseInt(st.nextToken());
				k = Integer.parseInt(st.nextToken());
				sCityArr = new int[k];
				eCityArr = new int[k];
				mLimitArr = new int[k];
				for(int j = 0; j < k; j++) {
					st = new StringTokenizer(br.readLine());
					sCityArr[j] = Integer.parseInt(st.nextToken());
					eCityArr[j] = Integer.parseInt(st.nextToken());
					mLimitArr[j] = Integer.parseInt(st.nextToken());
				}
				userSolution.init(n, k, sCityArr, eCityArr, mLimitArr);
                ok = true; 
				break;
			case CMD_ADD:
				sCity = Integer.parseInt(st.nextToken());
				eCity = Integer.parseInt(st.nextToken());
				mLimit = Integer.parseInt(st.nextToken());
				userSolution.add(sCity, eCity, mLimit);
				print(q, "add", q, q, sCity, eCity, mLimit);
				break;
			case CMD_CALC:
				sCity = Integer.parseInt(st.nextToken());
				eCity = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.calculate(sCity, eCity);
				print(q, "add", ans, ret, sCity, eCity);
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

	static void print(int q, String cmd, int ans, int ret, Object... o) {
		 //if(ans != ret)	 System.err.println("---------------------오류------------------------");
		 //System.out.println("["+q+"] " + cmd + " " + ans + "=" + ret + " [" + Arrays.deepToString(o) + "]" );
	}
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\화물운송\\sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}