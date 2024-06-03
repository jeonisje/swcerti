package 프로원정대.day8.전기차대여소;


import java.io.*;
import java.util.*;

class UserSolution {	
	private static int MAX_N = 350;
	
	int MAX = 201;
	
	int N;
	int range;
	
	int[][] map;
	ArrayList<Edge>[] graph;
	
	int[][] visited;
	
	int cc;
	
	int[] directY = {-1, 1, 0, 0};
	int[] directX = {0, 0, -1, 1};
	
	public void init(int N, int mRange, int[][] mMap) {
		this.N = N;
		this.range = mRange;
		this.map = mMap;		
		
		visited = new int[N][N];
		graph = new ArrayList[MAX];
		for(int i=0; i<MAX; i++) {
			graph[i] = new ArrayList<>();
		}
	}
	
	public void add(int mID, int mRow, int mCol) {
		int id = mID + 1001;
		map[mRow][mCol] = id;
		
		bfs(mID, mRow, mCol);
		return; 
	}
	
	void bfs(int from, int sy, int sx) {
		cc++;
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(sy, sx, 0));
		
		visited[sy][sx] = cc;
		while(!q.isEmpty()) {
			Node cur = q.remove();
			
			if(cur.cost == range) continue;
			
			for(int i=0; i<4; i++) {
				int ny = cur.y + directY[i];
				int nx = cur.x + directX[i];
				
				if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
				if(map[ny][nx] == 1) continue;
				if(visited[ny][nx] == cc) continue;
				int nextCost = cur.cost + 1;
				q.add(new Node(ny, nx, nextCost));
				visited[ny][nx] = cc;
				
				if(map[ny][nx] >= 1001) {
					int to = map[ny][nx] - 1001; 
					graph[from].add(new Edge(to, nextCost));
					graph[to].add(new Edge(from, nextCost));
				}
			}
			
		}
	}
	
	public int distance(int mFrom, int mTo) {
		PriorityQueue<Edge> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Edge(mFrom, 0));
		
		int[] dist = new int[MAX];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[mFrom] = 0;
		while(!q.isEmpty()) {
			Edge cur = q.remove();
			
			if(cur.id == mTo) {
				return dist[cur.id];
			}
			
			if(dist[cur.id] < cur.cost) continue;
			
			for(Edge next : graph[cur.id]) {
				int nextCost = cur.cost + next.cost;
				if(dist[next.id] <= nextCost) continue;
				dist[next.id] = nextCost;
				q.add(new Edge(next.id, dist[next.id]));
			}
		}
		
		
		return -1; 
	}
	
	class Node {
		int y;
		int x;
		int cost;
		public Node(int y, int x, int cost) {		
			this.y = y;
			this.x = x;
			this.cost = cost;
		}
	}
	
	class Edge {
		int id;
		int cost;
		public Edge(int id, int cost) {		
			this.id = id;
			this.cost = cost;
		}
	}
}


public class Main {
	private static final int CMD_INIT				= 0;
	private static final int CMD_ADD				= 1;
	private static final int CMD_DISTANCE			= 2;

	private static UserSolution usersolution = new UserSolution();

	private static boolean run(BufferedReader br) throws Exception {
		
		final int Q;
		final int MAX_N = 350; 
		
        StringTokenizer stdin = new StringTokenizer(br.readLine(), " ");

		Q = Integer.parseInt(stdin.nextToken());
		
		int cmd, ans, ret = 0;
		int N, range, id, id2, r, c; 
		int[][] region = new int[MAX_N][MAX_N]; 
		boolean okay = false;
		
		for (int q = 0; q < Q; ++q) {
            stdin = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(stdin.nextToken());
			
			switch(cmd) {
			case CMD_INIT:
				N = Integer.parseInt(stdin.nextToken());
				range = Integer.parseInt(stdin.nextToken());
				for(int i = 0; i < N; i++) {
					stdin = new StringTokenizer(br.readLine(), " ");
					for(int j = 0; j < N; j++) {
						region[i][j] = Integer.parseInt(stdin.nextToken());
					}
				}
				usersolution.init(N, range, region); 
				okay = true;
				break;
			case CMD_ADD:
				id = Integer.parseInt(stdin.nextToken()); 
				r = Integer.parseInt(stdin.nextToken());
				c = Integer.parseInt(stdin.nextToken());
				usersolution.add(id, r, c); 
				print(q, "add", q, q, r, c);
				break;
			case CMD_DISTANCE:
				id = Integer.parseInt(stdin.nextToken());
				id2 = Integer.parseInt(stdin.nextToken());
				ans = Integer.parseInt(stdin.nextToken());
				ret = usersolution.distance(id, id2);
				print(q, "distance", ans, ret, id, id2);
				if (ans != ret)
					okay = false;
				break;
			default:
				okay = false;
				break;
			}
		}
		return okay;
	}
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans != ret) 	System.err.println("---------------오류---------------");
		//System.out.println("["+q+"] " + cmd + " : " + ans + "=" + ret + "("+Arrays.deepToString(o) +")");
	}
	
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대\\day8\\전기차대여소\\sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());
		
		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		
		br.close();
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}