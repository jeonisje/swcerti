package 기출문제.배송로봇._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	int MAX_D = 9;
	int N, M;
	ArrayList<Node>[] graph;
	
	
	int[][] costMap;
	int[][] deliveryMap;
	int[][] deliveried;
	int[][] visited;
	int[] dist;
	int[] arrFrom;
	
	int min;
	
	int cc;
	public void init(int N, int E, int[] sCity, int[] eCity, int[] mTime) {
		this.N = N;
		
		graph = new ArrayList[N+1];
		for(int i=0; i<N+1; i++) {
			graph[i] = new ArrayList<>();
		}
		
		dist = new int[N+1];
		costMap = new int[N+1][N+1];
		deliveryMap = new int[N+1][N+1];
		deliveried = new int[N+1][N+1];
		visited = new int[N+1][MAX_D];
		arrFrom = new int[N+1];
		
		for(int i=0; i<E; i++) {
			int from = sCity[i];
			int to = eCity[i];
			int time = mTime[i];
			
			graph[from].add(new Node(to, time));
		}
		
		for(int from=0; from<N+1; from++) {
			dijkstra(from);
			
			for(int to=0; to<N+1; to++) {
				if(dist[to] == Integer.MAX_VALUE) continue;
				if(from == to) continue;
				costMap[from][to] = dist[to];
			}
		}		
		
		return;
	}
	
	void dijkstra(int start) {
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
			
	}

	public void add(int sCity, int eCity, int mTime) {
		graph[sCity].add(new Node(eCity, mTime));
		
		dijkstra(sCity);

		for(int to=0; to<N+1; to++) {
			if(dist[to] == Integer.MAX_VALUE) continue;
			if(sCity == to) continue;
			costMap[sCity][to] = dist[to];
		}
		
		return;
	}

	public int deliver(int mPos, int M, int[] mSender, int[] mReceiver) {
		this.M = M;
		
		cc++;
		min = Integer.MAX_VALUE;
		
		
		for(int i=0; i<M; i++) {
			deliveryMap[mSender[i]][mReceiver[i]] = cc;
		}
		dfs(mPos, 0, 0, new ArrayList<Integer>(), mPos+"");
		return min;
	
	}
	
	void dfs(int cur, int count, int sum, ArrayList<Integer> dList, String path) {
		if(count == M) {
			System.out.println(path + ":" +  sum);
			min = Math.min(min, sum);
			return;
		}
		
		visited[cur][count] = cc;
		
		for(int next=0; next<N+1; next++) {
			if(costMap[cur][next] == 0) 
				continue;
			
			int nextCost = sum + costMap[cur][next];
			if(min < nextCost) continue;
			int nextCount = count;
			ArrayList<Integer> nextList = dList;
			if(deliveryMap[cur][next] == cc) {
				boolean found = true;
				
				if(!dList.isEmpty()) {
					for(int k=0; k<dList.size()-1; k+=2) {
						int num1 = dList.get(k);
						int num2 = dList.get(k+1);
						if(num1 == cur && num2 == next) {
							found = false;
							break;
						}
					}
				}
				
				if(found) {
					//System.out.println(path + ":" +  sum);
					nextList = new ArrayList<>();
					nextList.addAll(dList);
					nextList.add(cur);
					nextList.add(next);
					nextCount++;
				}
				
			}
			
			if(visited[next][nextCount] == cc) continue;
			visited[next][nextCount] = cc; 
			dfs(next, nextCount, nextCost, nextList, path + " > "+next);
			visited[cur][nextCount] = 0;
			
		}
		
		
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
    private static BufferedReader br;
    private static UserSolution userSolution = new UserSolution();

    private static final int MAX_E 					= 500;
    private final static int CMD_INIT 			= 100;
    private final static int CMD_ADD			= 200;
    private final static int CMD_DELIVER		= 300;

    private static boolean run() throws Exception {

        StringTokenizer stdin;

        int Q;
        int n, e, m, pos;
        String strTmp; 
        int[] sIdArr = new int[MAX_E];
        int[] eIdArr = new int[MAX_E];
        int[] mTimeArr = new int[MAX_E];
        int sId, eId, mTime;
        int cmd, ret, ans; 
        boolean okay = false;
        
        stdin = new StringTokenizer(br.readLine(), " ");
        Q = Integer.parseInt(stdin.nextToken());

        for (int q = 0; q < Q; q++) {
            stdin = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(stdin.nextToken());
            strTmp = stdin.nextToken();

            switch(cmd) {
			
			case CMD_INIT:
				okay = true; 
				stdin = new StringTokenizer(br.readLine(), " "); 
				strTmp = stdin.nextToken();
				n = Integer.parseInt(stdin.nextToken());
				strTmp = stdin.nextToken();
				e = Integer.parseInt(stdin.nextToken());
				for(int j = 0; j < e; ++j) {
					stdin = new StringTokenizer(br.readLine(), " "); 
					strTmp = stdin.nextToken();
					sIdArr[j] = Integer.parseInt(stdin.nextToken());
					strTmp = stdin.nextToken();
					eIdArr[j] = Integer.parseInt(stdin.nextToken());
					strTmp = stdin.nextToken();
					mTimeArr[j] = Integer.parseInt(stdin.nextToken());
				}
				userSolution.init(n, e, sIdArr, eIdArr, mTimeArr);
				break; 
				
			case CMD_ADD:
				stdin = new StringTokenizer(br.readLine(), " "); 
				strTmp = stdin.nextToken();
				sId = Integer.parseInt(stdin.nextToken());
				strTmp = stdin.nextToken();
				eId = Integer.parseInt(stdin.nextToken());
				strTmp = stdin.nextToken();
				mTime = Integer.parseInt(stdin.nextToken());
				userSolution.add(sId, eId, mTime);
				print(q, "add", q, q, sId, eId, mTime);
				break;
				
			case CMD_DELIVER:
				stdin = new StringTokenizer(br.readLine(), " "); 
				strTmp = stdin.nextToken();
				pos = Integer.parseInt(stdin.nextToken());
				strTmp = stdin.nextToken();
				m = Integer.parseInt(stdin.nextToken()); 
				for(int j = 0; j < m; ++j) {
					stdin = new StringTokenizer(br.readLine(), " ");
					strTmp = stdin.nextToken();
					sIdArr[j] = Integer.parseInt(stdin.nextToken());
					strTmp = stdin.nextToken();
					eIdArr[j] = Integer.parseInt(stdin.nextToken());
				}
				stdin = new StringTokenizer(br.readLine(), " ");
				strTmp = stdin.nextToken();
				ans = Integer.parseInt(stdin.nextToken());
				ret = userSolution.deliver(pos, m, sIdArr, eIdArr);
				print(q, "deliver", ans, ret, pos, m);
				if(ret != ans) 
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
    	int T, MARK;

        System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\배송로봇\\sample_input3.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");
        T = Integer.parseInt(stinit.nextToken());
        MARK = Integer.parseInt(stinit.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }
        br.close();
        System.out.println("ms => " + (System.currentTimeMillis() - start));
    }
}