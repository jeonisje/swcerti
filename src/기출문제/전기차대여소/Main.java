package 기출문제.전기차대여소;

import java.io.*;
import java.util.*;

class UserSolution {
	
	private static int MAX_N = 350; 
	
	int N, range;
	int[][] map;
	int[][] point;
	
	ArrayList<Office>[] graph;
	
	int[] directRow = {-1, 1, 0, 0};
	int[] directCol = {0, 0, -1, 1};
		
	public void init(int N, int mRange, int[][] mMap) {
		this.N = N;
		this.range = mRange;
		this.map = mMap;
		this.point = new int[N][N];
		
		graph = new ArrayList[MAX_N + 1];
		
		for(int i=0; i<MAX_N + 1; i++) {
			graph[i] = new ArrayList<>();
		}		
		
		for(int i=0; i<N; i++) {
			Arrays.fill(point[i], -1);
		}
	}
	
	public void add(int mID, int mRow, int mCol) {
		point[mRow][mCol] = mID;
		
		int[][] visited = new int[N][N];
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(mRow, mCol, 0));
		visited[mRow][mCol] = 1;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			if(cur.cost == range) continue;
			
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];
				
				if(nextRow < 0 || nextCol < 0 || nextRow >= N || nextCol >= N) continue;
				if(visited[nextRow][nextCol] == 1)  continue;
				if(map[nextRow][nextCol] == 1) continue;
				
				if(point[nextRow][nextCol] != -1) {
					int id2 = point[nextRow][nextCol];
					graph[mID].add(new Office(id2, cur.cost + 1));
					graph[id2].add(new Office(mID, cur.cost + 1));
				}
				
				q.add(new Node(nextRow, nextCol, cur.cost + 1));
				visited[nextRow][nextCol] = 1;				
			}
		}		
		
	}
	
	public int distance(int mFrom, int mTo) {
		int[] dist = new int[MAX_N + 1];
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		PriorityQueue<Office> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Office(mFrom, 0));
		dist[mFrom] = 0;
		while(!q.isEmpty()) {
			Office cur = q.poll();
			
			if(cur.id == mTo) return dist[cur.id];
			
			if(dist[cur.id] < cur.cost) continue;
			
			for(Office next : graph[cur.id]) {
				if(dist[next.id] <= cur.cost + next.cost) continue;
				dist[next.id] = cur.cost + next.cost;
				q.add(new Office(next.id, dist[next.id]));
			} 
		}
		
		
		
		return -1; 
	}
	
	class Node {
		int row;
		int col;
		int cost;
		public Node(int row, int col, int cost) {
			this.row = row;
			this.col = col;
			this.cost = cost;
		}		
	}
	
	class Office {
		int id;
		int cost;
		public Office(int id, int cost) {		
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
	
	private static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("-------------------오류--------------------");
		//System.out.println("["+q+"] " + cmd + " " + ans + "=" + ret +"[" + Arrays.deepToString(o)+"]");
	}
	
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
				print(q, "add", id, id, r, c);
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
	
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\전기차대여소\\sample_input.txt"));

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
		
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}