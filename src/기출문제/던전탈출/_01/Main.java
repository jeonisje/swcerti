package 기출문제.던전탈출._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	final int MAX_GATE = 201;
	final int GATE_CONSTANT = 2;
	int N;
	int maxStemina;
	int[][] map;
	int[] removed;
	
	ArrayList<ArrayList<Gate>> graph;
	
	int[] directRow = {-1, 1, 0, 0};
	int[] directCol = {0, 0, -1, 1};	
	
	public void init(int N, int mMaxStamina, int[][]mMap) {
		this.N = N;
		this.maxStemina = mMaxStamina;
		
		this.map = mMap;
		removed = new int[MAX_GATE];
		
		graph = new ArrayList<>();		
		for(int i= 0; i < MAX_GATE; i++) {
			graph.add(new ArrayList<>());
		}		
		return;
	}
	
	public void addGate(int mGateID, int mRow, int mCol) {
		map[mRow][mCol] = mGateID + GATE_CONSTANT;		
		bfs(mGateID, mRow, mCol);
	}
	
	void bfs(int id, int row, int col) {
		int[][] visited = new int[N][N];
		
		ArrayDeque<Node> q = new ArrayDeque<>();		
		q.add(new Node(row, col, 0, maxStemina));
		visited[row][col] = 1;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			
			if(cur.stemina <= 0) {
				continue;
			}			
		
			for(int i=0; i<4; i++) {
				int nextRow = cur.row + directRow[i];
				int nextCol = cur.col + directCol[i];		
				
				if(map[nextRow][nextCol] == 1) continue;
				if(visited[nextRow][nextCol] == 1) continue;			
				
				int foundGate = map[nextRow][nextCol] - GATE_CONSTANT;
				
				if(foundGate > 0) {					
					if(removed[foundGate] != 1) {
						graph.get(foundGate).add(new Gate(id, cur.count + 1));
						graph.get(id).add(new Gate(foundGate, cur.count + 1));
					}				
				}
				
				q.add(new Node(nextRow, nextCol, cur.count + 1, cur.stemina - 1));
				visited[nextRow][nextCol]  = 1;				
			}			
		}		 
	}
	
	public void removeGate(int mGateID) {
		removed[mGateID] = 1;
	}
	
	public int getMinTime(int mStartGateID, int mEndGateID) {
		int[] dist = new int[MAX_GATE];
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		PriorityQueue<Gate> q = new PriorityQueue<>();
		q.add(new Gate(mStartGateID, 0));
		dist[mStartGateID] = 0;
		
		while(!q.isEmpty()) {
			Gate cur = q.poll();
			if(cur.id == mEndGateID) {
				return dist[cur.id];
			}
			
			if(dist[cur.id] < cur.cost) continue;
			
			//for(Gate next : graph.get(cur.id)) {
			for(int i=0; i<graph.get(cur.id).size(); i++) {
				Gate next = graph.get(cur.id).get(i);
				if(removed[next.id] == 1) continue;
				if(dist[next.id] <= cur.cost + next.cost) continue;
				dist[next.id] = cur.cost + next.cost;
				q.add(new Gate(next.id, dist[next.id]));
			}
		}
		
		
		return -1; 
	}
	
	class Node {
		int row;
		int col;
		int count;
		int stemina;
		
		public Node(int row, int col, int count, int stemina) {
			this.row = row;
			this.col = col;
			this.count = count;
			this.stemina = stemina;
		}		
	}
	
	class Gate implements Comparable<Gate>{
		int id;
		int cost;
		
		public Gate(int id, int cost) {
			this.id = id;
			this.cost = cost;
		}

		@Override
		public int compareTo(Gate o) {			
			return Integer.compare(this.cost, o.cost);
		}		
		
	}
}

public class Main {
	private static final int CMD_INIT 		 	= 0;
	private static final int CMD_ADD_GATE 	 	= 1;
	private static final int CMD_REMOVE_GATE 	= 2;
	private static final int CMD_GET_MIN_TIME 	= 3;

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	private static int[][] gMap = new int[350][350]; 

	private static boolean run(BufferedReader br) throws Exception {

		int cmd, ans, ret; 
		int N, maxStamina, gateID1, gateID2, row, col; 
		int Q = 0;
		boolean okay = false;
		
		st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken()); 
		
		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				maxStamina = Integer.parseInt(st.nextToken());
				for(int i = 0; i <= N-1; i ++) {
					st = new StringTokenizer(br.readLine(), " "); 
					for(int j = 0; j <= N-1; j++) {
						gMap[i][j] = Integer.parseInt(st.nextToken());
					}
				}
				userSolution.init(N,  maxStamina, gMap);
				okay = true; 
				break;
			case CMD_ADD_GATE:
				gateID1 = Integer.parseInt(st.nextToken());
				row = Integer.parseInt(st.nextToken());
				col = Integer.parseInt(st.nextToken());
				userSolution.addGate(gateID1, row, col);
				print(q, "addGate", q, q, gateID1);
				break;
			case CMD_REMOVE_GATE:
				gateID1 = Integer.parseInt(st.nextToken());
				userSolution.removeGate(gateID1);
				print(q, "removeGate", q, q, gateID1);
				break;
			case CMD_GET_MIN_TIME:
				gateID1 = Integer.parseInt(st.nextToken());
				gateID2 = Integer.parseInt(st.nextToken());
				ret = userSolution.getMinTime(gateID1, gateID2);
				ans = Integer.parseInt(st.nextToken());
				print(q, "getMinTime", ans, ret, gateID1, gateID2);
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

	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\던전탈출\\sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		System.out.println("estimated=> " + (System.currentTimeMillis() - start));
	}
}