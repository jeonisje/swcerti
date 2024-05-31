package 기출문제.배송로봇._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	int MAX_D = 9;
	int N;
	ArrayList<Node>[] graph;
	
	
	int[][] dMap;
	int[] visited;
	int[] dist;
	int[] arrFrom;
	
	int cc;
	public void init(int N, int E, int[] sCity, int[] eCity, int[] mTime) {
		this.N = N;
		
		graph = new ArrayList[N+1];
		for(int i=0; i<N+1; i++) {
			graph[i] = new ArrayList<>();
		}
		
		dist = new int[N+1];
		dMap = new int[N+1][N+1];
		visited = new int[N+1];
		arrFrom = new int[N+1];
		
		for(int i=0; i<E; i++) {
			int from = sCity[i];
			int to = eCity[i];
			int time = mTime[i];
			
			graph[from].add(new Node(to, time));
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
		return;
	}

	public int deliver(int mPos, int M, int[] mSender, int[] mReceiver) {
		cc++;
		
		for(int i=0; i<N+1; i++) {
			targets[i] = new ArrayList<>();
		}
		
		for(int i=0; i<M; i++) {
			int from = mSender[i];
			int to = mReceiver[i];
			
			dMap[from][to] = cc;
			arrFrom[from] = cc;
			targets[from].add(to);
		}
	
		
		return dijkstra(mPos, M);
	}
	
	int dijkstra(int start, int M) {
		int ans = Integer.MAX_VALUE;
		for(int i=0; i<N+1; i++) {
			for(int j=0; j<MAX_D; j++) {
				dist[i][j][0] = Integer.MAX_VALUE;
				dist[i][j][1] = Integer.MAX_VALUE;
			}
		}
		
		//dist[start][0][0] = 0;
		PriorityQueue<DNode> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		//q.add(new DNode(start, 0, -1, -1, 0, 0, 0));
		
		
		if(arrFrom[start] == 0) {
			q.add(new DNode(start, 0, -1, -1, 0, 0, 0, start+""));
		} else {
			for(int target : targets[start]) {						  
				q.add(new DNode(start, 0, start, target, 1, 0, 0, start+""));
			}
		}
		
		while(!q.isEmpty()) {
			DNode cur = q.remove();
			
			if(dist[cur.id][M][1] != Integer.MAX_VALUE) {
				System.out.println(cur.path);
				ans = Math.min(ans, dist[cur.id][M][1]);
				continue;
			}
			System.out.println(cur.path);
			if(dist[cur.id][cur.dCount][cur.deliveried] < cur.cost) continue;
			
			for(Node next : graph[cur.id]) {
				int startId = cur.startId;
				int targetId = cur.targetId;				
				int flag = cur.flag;
				int deliveried = cur.deliveried;
				int dCount = cur.dCount;
				
				int nextCost = cur.cost + next.cost;
				if(flag == 1) {
					// 배달물이 있음
					if(dMap[startId][next.id] == cc  && next.id == targetId) {							
							// 배달 위치가 맞음 -> 배달 처리
						
						if(visited[startId][next.id] != cc) {
							dCount++;
							deliveried = 1;
							visited[startId][next.id] = cc;
							flag = 0;
							startId = -1;
							targetId = -1;
						}
						
					}
				}
				
				if(dist[next.id][dCount][deliveried] <= nextCost) 
					continue;
				dist[next.id][dCount][deliveried] = nextCost;
				
				if(flag == 0) {
					if(arrFrom[next.id] == cc) {
						for(int target : targets[next.id]) {
							//q.add(new DNode(next.id, nextCost, next.id, target, 1, dCount, 0));							  
							q.add(new DNode(next.id, nextCost, next.id, target, 1, dCount, 0, cur.path + "->" + next.id + "("+dCount+")"));
						}
					} else {
						//q.add(new DNode(next.id, nextCost, -1, -1, 0, dCount, 0));
						q.add(new DNode(next.id, nextCost, -1, -1, 0, dCount, 0, cur.path + "->" + next.id + "("+dCount+")"));
					}
				} else {
					//q.add(new DNode(next.id, nextCost, startId, targetId, flag, dCount, 0));
					q.add(new DNode(next.id, nextCost, startId, targetId, flag, dCount, 0, cur.path + "->" + next.id + "("+dCount+")"));
				}
			}
			
		}
		
		return ans;
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

        System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\배송로봇\\sample_input2.txt"));
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