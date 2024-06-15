package 기출문제.지하도시건설;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

class UserSolution {
	int MAX = 10_001;
	
	int H, W;
	
	TreeMap<Box, Integer>[] tm;
	ArrayList<Integer>[] graph;
	
	int[] visited;
	
	int curFloor;
	int cc;

	public void init(int mH, int mW) {
		H = mH;
		W = mW;
		
		tm = new TreeMap[H];
		graph = new ArrayList[MAX];
		visited = new int[MAX];
		
		for(int i=0; i<H; i++) {
			tm[i] = new TreeMap<>((o1, o2) -> Integer.compare(o1.left, o2.left));
		}
		
		for(int i=0; i<MAX; i++) {
			graph[i] = new ArrayList<>();
		} 
		
		curFloor = H - 1;
		
		return; 
	}

	public int dropBox(int mId, int mLen, int mExitA, int mExitB, int mCol) {
		
		int possibleFloor = -1;
		Box newBox = new Box(mId, mExitA + mCol, mExitB + mCol, mCol, mCol + mLen);
		boolean possible = false;
		
		Box leftBox = null;
		Box rightBox = null;
		for(int i=curFloor; i<H; i++) {
			possible = true;
			
			Box lBox = tm[i].floorKey(newBox);
			Box rBox = tm[i].ceilingKey(newBox);
			
			if(lBox != null && lBox.right > mCol )
				possible = false;			
			else if(rBox != null && rBox.left < mCol + mLen ) {
				possible = false;				
			} else if(lBox != null && rBox != null 
					&& (lBox.right >  mCol || rBox.left < mCol + mLen)) {
				possible = true;				
			}
			
			if(!possible) break;
			
			possibleFloor = i;		
			
			leftBox = lBox;
			rightBox = rBox;
		}
		
		if(possibleFloor == -1) {
			possibleFloor = curFloor - 1;
		}
		
		// 현재 층 설정
		curFloor = Math.min(possibleFloor, curFloor);				
				
		tm[possibleFloor].put(newBox, mId);
		
		// 좌축 연결
		if(leftBox != null && leftBox.right == mCol) {
				graph[mId].add(leftBox.id);
				graph[leftBox.id].add(mId);
		}
		// 우측 연결
		if(rightBox != null && rightBox.left == mCol + mLen) {
			graph[mId].add(rightBox.id);
			graph[rightBox.id].add(mId);
		}
		
		// 상하 연결
		if(possibleFloor != H-1) {
			int exit1 = Math.min(mExitA, mExitB);
			int exit2 = Math.max(mExitA, mExitB);
			
			Box fromKey = new Box(0, 0, 0, mCol , 0);
			Box toKey = new Box(0, 0, 0, mCol + mLen, 0);				
			
			Box from = tm[possibleFloor + 1].floorKey(fromKey);
			Box to = tm[possibleFloor + 1].floorKey(toKey);
			
			if(from == null) 
				from = newBox;
			if(to == null)
				to = newBox;
			
			for(Map.Entry<Box, Integer> entry : tm[possibleFloor + 1].subMap(from, true, to, true).entrySet()) {
				Box downBox = entry.getKey();
				if(downBox.exitA >= mCol && downBox.exitA < mCol + mLen 
						|| downBox.exitB>= mCol && downBox.exitB < mCol + mLen) {					
					graph[downBox.id].add(mId);
				}
				
				if(mExitA + mCol >= downBox.left && mExitA  + mCol < downBox.right
						|| mExitB  + mCol >= downBox.left && mExitB  + mCol < downBox.right) {
					graph[mId].add(downBox.id);
				}
			}
		}
		
		
		return possibleFloor;		
		
	}

	public int explore(int mIdA, int mIdB) {
		cc++;
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(mIdA, 0));
		visited[mIdA] = cc;
		while(!q.isEmpty()) {
			Node cur = q.remove();
			if(cur.id == mIdB) 
				return cur.dist;
			
			for(int next : graph[cur.id]) {
				if(visited[next] == cc) continue;
				q.add(new Node(next, cur.dist + 1));
				visited[next] = cc;
			}
		}
		
		return -1;
	}
	
	class Box {
		int id;		
		int exitA;
		int exitB;
		int left;
		int right;
		public Box(int id, int exitA, int exitB, int left, int right) {
			this.id = id;
			this.exitA = exitA;
			this.exitB = exitB;
			this.left = left;
			this.right = right;
		}		
	}
	
	class Node {
		int id;
		int dist;
		public Node(int id, int dist) {		
			this.id = id;
			this.dist = dist;
		}
	}
}

public class Main {
	private static final int CMD_INIT = 1;
	private static final int CMD_DROPBOX = 2;
	private static final int CMD_EXPLORE = 3;

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {

		int query_num, mId, mLen, mH, mW, mIdA, mIdB, mExitA, mExitB, mCol;
		int ans, ret;
		boolean okay = false;

		st = new StringTokenizer(br.readLine(), " ");
		int Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				mH = Integer.parseInt(st.nextToken());
				mW = Integer.parseInt(st.nextToken());
				userSolution.init(mH, mW);
				okay = true;
				break;

			case CMD_DROPBOX:
				mId = Integer.parseInt(st.nextToken());
				mLen = Integer.parseInt(st.nextToken());
				mExitA = Integer.parseInt(st.nextToken());
				mExitB = Integer.parseInt(st.nextToken());
				mCol = Integer.parseInt(st.nextToken());
				ret = userSolution.dropBox(mId, mLen, mExitA, mExitB, mCol);				
				ans = Integer.parseInt(st.nextToken());
				print(q, "dropBox", ans, ret, mId, mLen, mExitA, mExitB, mCol);
				if (ans != ret) {
					okay = false;
				}
				break;

			case CMD_EXPLORE:
				mIdA = Integer.parseInt(st.nextToken());
				mIdB = Integer.parseInt(st.nextToken());
				ret = userSolution.explore(mIdA, mIdB);
				ans = Integer.parseInt(st.nextToken());
				print(q, "explore", ans, ret, mIdA, mIdB);

				if (ans != ret) {
					okay = false;
				}
				break;

			default:
				okay = false;
				break;
			}
		}
		return okay;
	}
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("====================오류========================");
		//System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
	}
	public static void main(String[] args) throws Exception {
		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\지하도시건설\\sample_input.txt"));
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