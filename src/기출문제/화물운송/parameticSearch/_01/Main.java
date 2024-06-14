package 기출문제.화물운송.parameticSearch._01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;


class UserSolution {
	
	int N; 
	ArrayList<Node>[] graph;
	
	int[] visited;
	int cc;
	
	public void init(int N, int K, int[] sCity, int[] eCity, int[] mLimit) {
		this.N = N;
		
		graph = new ArrayList[N];
		for(int i=0; i<N; i++) {
			graph[i] = new ArrayList<>();
		}
		
		for(int i=0; i<K; i++) {
			graph[sCity[i]].add(new Node(eCity[i], mLimit[i]));
		}
		
		visited = new int[N];
		cc = 0;
		
		return;
	}
	
	public void add(int sCity, int eCity, int mLimit) {
		graph[sCity].add(new Node(eCity, mLimit));
		return; 
	}

	public int calculate(int sCity, int eCity) {
		int start = 1;
		int end = 30_001;
		
		boolean found = false;
		while(start <= end) {
			int mid = (start + end) / 2;
			boolean ret = test(mid, sCity, eCity);
			if(ret) {
				found = true;
				start = mid + 1;
			} else {
				end = mid - 1;
			}
		}
		
		return found ? end : -1;
	}
	
	
	
	
	boolean test(int target, int start, int end) {
		cc++;
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(start, 0));
		visited[start] = cc;
		while(!q.isEmpty()) {
			Node cur = q.remove();
			if(cur.id == end) return true;
			for(Node next : graph[cur.id]) {
				if(next.limit < target) continue;
				
				if(visited[next.id] == cc) continue;
				q.add(next);
				visited[next.id] = cc;
			}
		}
		
		
		return false;
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
				print(q, "calculate", ans, ret, sCity, eCity);
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