package 기출문제.전기차대여소.강사코드;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	
	private static int MAX_N = 350; 
	
	static int[][] MAP;
	static int size; 
	static int fuel;
	static int[] ydir = {-1, 1, 0, 0};
	static int[] xdir = {0, 0, -1, 1}; 
	static ArrayList<dNode>[] al; 
	
	static class Node {
		int y;
		int x; 
		Node(int y, int x) {
			this.y = y;
			this.x = x; 
		}
	}
	
	static class dNode implements Comparable <dNode> {
		int num;
		int cost;
		dNode(int num, int cost) {
			this.num = num;
			this.cost = cost; 
		}
		@Override
		public int compareTo(dNode o) {
			if(this.cost < o.cost) return -1;
			if(this.cost > o.cost) return 1;
			return 0; 
		}
	}
	
	public void init(int N, int mRange, int[][] mMap) {
		
		MAP = mMap; 
		fuel = mRange;
		size = N;
		al = new ArrayList[201];
		for(int i = 0; i < 201; i++)
			al[i] = new ArrayList<>(); 
		return;
	}
	
	public void add(int mID, int mRow, int mCol) {
		MAP[mRow][mCol] = mID + 2;
		bfs(mRow, mCol, mID + 2); 
		return; 
	}
	
	public int distance(int mFrom, int mTo) {
		  
		int[] dist = new int[201]; 
	    for (int i = 0; i < 201; i++)
	        dist[i] = Integer.MAX_VALUE;
	    dist[mFrom] = 0;

	    PriorityQueue<dNode>pq = new PriorityQueue<>(); 
	    pq.add(new dNode(mFrom, 0)); 

	    while (!pq.isEmpty()) {
	        dNode now = pq.remove(); 
	        
	        for (int i = 0; i < al[now.num].size(); i++) {
	            dNode next = al[now.num].get(i); 
	            int nextCost = dist[now.num] + next.cost;
	            if (dist[next.num] <= nextCost)
	                continue;
	            dist[next.num] = nextCost;
	            pq.add(new dNode(next.num, nextCost)); 
	        }
	    }

	    if(dist[mTo] == Integer.MAX_VALUE)
	        return -1;
	    return dist[mTo]; 
	}
	
	static void bfs(int y, int x, int id) {
		ArrayDeque<Node>q = new ArrayDeque<>(); 
	    q.add(new Node(y, x)); 

	    int[][] visited = new int[size][size]; 
	    visited[y][x] = 1;

	    while (!q.isEmpty()) {
	        Node now = q.remove(); 

	        if (visited[now.y][now.x] - 1 > fuel)
	            return; 

	        if (MAP[now.y][now.x] != 0 && MAP[now.y][now.x] != 1 && MAP[now.y][now.x] != id) {
	        	int dist = visited[now.y][now.x] - 1;  
	        	al[MAP[now.y][now.x]-2].add(new dNode(id-2, dist)); 
	        	al[id-2].add(new dNode(MAP[now.y][now.x]-2, dist)); 
	        }

	        for (int i = 0; i < 4; i++) {
	            int ny = now.y + ydir[i];
	            int nx = now.x + xdir[i];
	            if (ny < 0 || nx < 0 || ny >= size || nx >= size)
	                continue;
	            if (visited[ny][nx] != 0)
	                continue;
	            if (MAP[ny][nx] == 1)
	                continue;
	            visited[ny][nx] = visited[now.y][now.x] + 1;
	            q.add(new Node(ny, nx)); 
	        }
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