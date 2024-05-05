package 기출문제.시간과비용;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	int N, K;
	
	ArrayList<Node>[] graph;
	
	 
	public void init(int N, int K, int[]sCity, int[]eCity, int[]mCost, int[]mTime) {
	    this.N = N;
	    graph = new ArrayList[N];
	    
	    for(int i=0; i<N; i++) {
	    	graph[i] = new ArrayList<>();
	    }
	    
	    for(int i=0; i<K; i++) {
	    	graph[sCity[i]].add(new Node(eCity[i], mCost[i], mTime[i]));
	    }
	    
		return;
	}
	 
	public void add(int sCity, int eCity, int mCost, int mTime) {
		graph[sCity].add(new Node(eCity, mCost, mTime));
	    return;
	}
	 
	public int cost(int M, int sCity, int eCity) {
	    int[][] dist = new int[N+1][2];
	    for(int i=0; i<N+1; i++) {
	    	dist[i][1] = Integer.MAX_VALUE;	    	
	    }
	    
	    PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.time, o2.time));
	    q.add(new Node(sCity, 0, 0));
	    dist[sCity][0] = 0;
	    dist[sCity][1] = 0;
	    
	    while(!q.isEmpty()) {
	    	Node cur = q.poll();
	    	
	    	if(cur.id == eCity) {
	    		return dist[cur.id][1];
	    	}
	    	
	    	for(Node next : graph[cur.id]) {
	    		int nextTime = next.time + cur.time;
	    		if(dist[next.id][1] <= nextTime) continue;
	    		
	    		int nextCost = next.cost + cur.cost;
	    		if(nextCost > M) continue;
	    		
	    		dist[next.id][0] = nextCost;
	    		dist[next.id][1] = nextTime;	    		
	    		q.add(new Node(next.id, dist[next.id][0], dist[next.id][1]));
	    		
	    	}
	    }
	    
	    return -1;
	}
	
	class Node {
		int id;
		int cost;
		int time;
		public Node(int id, int cost, int time) {		
			this.id = id;
			this.cost = cost;
			this.time = time;
		}
	}
	
	class  Line {
		int from;
		int to;
		int cost;
		int time;
		public Line(int from, int to, int cost, int time) {			
			this.from = from;
			this.to = to;
			this.cost = cost;
			this.time = time;
		}
		
	}
}

public class Main {
	private static final int CMD_INIT 	= 100;
	private static final int CMD_ADD 	= 200;
	private static final int CMD_COST 	= 300;

	private static UserSolution userSolution = new UserSolution();
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {
		
		int Q;
		int n, k, m;
		int[] sCityArr, eCityArr, mCostArr, mTimeArr;
		int sCity, eCity, mCost, mTime;
		int cmd, ans, ret;
		boolean okay = false;
		
		st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {
			
			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) {
			case CMD_INIT:
				n = Integer.parseInt(st.nextToken());
				k = Integer.parseInt(st.nextToken());
				sCityArr = new int[k];
				eCityArr = new int[k];
				mCostArr = new int[k];
				mTimeArr = new int[k];
				for(int i = 0; i < k; i++) {
					st = new StringTokenizer(br.readLine());
					sCityArr[i] = Integer.parseInt(st.nextToken());
					eCityArr[i] = Integer.parseInt(st.nextToken());
					mCostArr[i] = Integer.parseInt(st.nextToken());
					mTimeArr[i] = Integer.parseInt(st.nextToken());
				}
				userSolution.init(n, k, sCityArr, eCityArr, mCostArr, mTimeArr); 
				okay = true;
				break; 
				
			case CMD_ADD:
				sCity = Integer.parseInt(st.nextToken());
				eCity = Integer.parseInt(st.nextToken());
				mCost = Integer.parseInt(st.nextToken());
				mTime = Integer.parseInt(st.nextToken());
				userSolution.add(sCity, eCity, mCost, mTime); 
				print(q, "add", q, q, sCity, eCity, mCost, mTime);
				break;
				
			case CMD_COST:
				m = Integer.parseInt(st.nextToken());
				sCity = Integer.parseInt(st.nextToken());
				eCity = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.cost(m, sCity, eCity); 
				print(q, "cost", ans, ret, sCity, eCity, m);
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
	static void print(int q, String cmd, int ans, int ret, Object... o) {
		if(ans != ret)	 System.err.println("---------------------오류------------------------");
		System.out.println("["+q+"] " + cmd + " " + ans + "=" + ret + " [" + Arrays.deepToString(o) + "]" );
	}
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\시간과비용\\sample_input2.txt"));

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