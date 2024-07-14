package 기출문제.세포실험;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.StringTokenizer;

class UserSolution {
	int MAX = 8_001;
	int N;
	
	int[][] map;	
	int[][] visited;
	
	int[] directY1 = {-1, -1, -1, 0, 0, 1, 1, 1};
	int[] directX1 = {-1, 0, 1, -1, 1, -1, 0, 1};	
	int[] directY2 = {-1, 1, 0, 0};
	int[] directX2 = {0, 0, -1, 1};
	
	ArrayList<Node>[][] graph;
	int[] parent;	
	
	int cc;
	int sequence;
	
	int find(int a) {
		if(a == parent[a]) return a;
		return parent[a] = find(parent[a]);
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		if(pa == pb) return;
		parent[pb] = pa;
	}

	void init(int N) {
		this.N = N;
		map = new int[N+1][N+1];
		visited = new int[N+1][N+1];
		cc= 0;
		sequence = 0;
		
		parent = new int[MAX];
		for(int i=0; i<MAX; i++) {
			parent[i] = i;
		}
			
	}

	int cell(int mRow, int mCol) {
		sequence++;
		
		map[mRow][mCol] = sequence;
		
		for(int i=0; i<8; i++) {
			int nr = mRow + directY1[i];
			int nc = mCol + directX1[i];
			
			if(nr < 1 || nc < 1 || nr > N || nc > N) continue;
			if(map[nr][nc] == 0) continue;
			union(sequence, map[nr][nc]);
		}
		
		for(int i=0; i<8; i++) {
			int nr = mRow + directY1[i];
			int nc = mCol + directX1[i];
			
			if(nr < 1 || nc < 1 || nr > N || nc > N) 
				continue;
			if(map[nr][nc] != 0) 
				continue;
			int ret = ff(nr, nc, sequence);
			if(ret != -1) return ret; 
			
		}
		
		return 0;
	}
	
	int ff(int sRow, int sCol, int group) {
		cc++;
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(sRow, sCol));
		visited[sRow][sCol] = cc;
		int count = 1;
		while(!q.isEmpty()) {
			Node cur = q.remove();
			
			if(cur.row == 1 || cur.col == 1 || cur.row == N || cur.col == N)
				return -1;
			
			for(int i=0; i<4; i++) {
				int nr = cur.row + directY2[i];
				int nc = cur.col + directX2[i];
				
				if(nr < 1 || nc < 1 || nr > N || nc > N) continue;
				
				
				if(map[nr][nc] != 0 && find(map[nr][nc]) != group) 
					return -1;
				
				if(map[nr][nc] != 0) continue;
				if(visited[nr][nc] == cc) continue;
					
				q.add(new Node(nr, nc));
				visited[nr][nc] = cc;
				count++;
				if(count > 600)
					return -1;
			}
		}
		
		return count;
	}
	
	class Node {
		int row;
		int col;
		public Node(int row, int col) {		
			this.row = row;
			this.col = col;
		}
	}
}

public class Main {
	private static BufferedReader br;
	private static UserSolution usersolution = new UserSolution();

	private final static int CMD_INIT = 100;
	private final static int CMD_PUT = 200;

	private static boolean run() throws Exception {

		StringTokenizer stdin = new StringTokenizer(br.readLine(), " ");

		int query_num = Integer.parseInt(stdin.nextToken());
		int ret, ans;
		boolean ok = false;

		for (int q = 0; q < query_num; q++) {
			stdin = new StringTokenizer(br.readLine(), " ");
			int query = Integer.parseInt(stdin.nextToken());

			if (query == CMD_INIT) {
				int N = Integer.parseInt(stdin.nextToken());
				usersolution.init(N);
				ok = true;
			} else if (query == CMD_PUT) {
				int mRow = Integer.parseInt(stdin.nextToken());
				int mCol = Integer.parseInt(stdin.nextToken());
				ans = Integer.parseInt(stdin.nextToken());
				ret = usersolution.cell(mRow, mCol);
				print(q, "cell", ans, ret, mRow, mCol);
				if (ans != ret) {
					ok = false;
				}
			}
		}
		return ok;
	}
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + " " + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\세포실험\\sample_input.txt"));
				
		int T, MARK;

		// System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
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