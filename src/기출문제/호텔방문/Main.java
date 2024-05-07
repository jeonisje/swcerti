package 기출문제.호텔방문;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.TreeSet;

class UserSolution {
	int MAX_BRAND = 50;
	int MAX_CONNECT = 20_000; 
	
	int N;

	int[] parent;
	
	ArrayList<Node>[] graph;
	int[] countByBrand;	
	int[] hotelByBrand;
	
	void init(int N, int[] mBrands) {
		this.N = N;
		parent = new int[MAX_BRAND];
		countByBrand = new int[MAX_BRAND];
		hotelByBrand = new int[N];
		
		graph = new ArrayList[N];
		for(int i=0; i<N; i++) {
			graph[i] = new ArrayList<>();
			hotelByBrand[i] = mBrands[i];
		}
		
		for(int i=0; i<MAX_BRAND; i++) {
			parent[i] = i;
			countByBrand[mBrands[i]] += 1;
		}
		
		return;
	}
	
	int find(int a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	void union (int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		
		countByBrand[pa] += countByBrand[pb];
		parent[pb] = pa;
	}
	void connect(int mHotelA, int mHotelB, int mDistance) {
		graph[mHotelA].add(new Node(mHotelB, mDistance));
		graph[mHotelB].add(new Node(mHotelA, mDistance));
		return; 
	}

	int merge(int mHotelA, int mHotelB) {
		int brandA = hotelByBrand[mHotelA];
		int brandB = hotelByBrand[mHotelB];
		union(brandA, brandB);
		int pId = find(brandA);
		return countByBrand[pId];
	}

	int move(int mStart, int mBrandA, int mBrandB) {
		int totalDistance = 0;
		int pBrandA = find(mBrandA);
		int pBrandB = find(mBrandB);
	
		if(pBrandA == pBrandB) {
			for(int i=0; i<N; i++) {
				if(find(hotelByBrand[i]) == pBrandA) {
					
				}
			}
		}
		int brandA = find(mBrandA);
		int brandB = find(mBrandB);
		
		return totalDistance;
	}
	
	int dijkstra(int start, int target) {
		
		
		int[] dist = new int[N+1];
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		PriorityQueue<Node> q = new PriorityQueue<Node>((o1, o2) -> Integer.compare(o1.distance, o2.distance));
		q.add(new Node(start, 0));
		dist[0] = 0;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			if(cur.id == target) {
				return dist[cur.id];
			}
			for(Node next : graph[cur.id]) {
				int nextDistance = cur.distance + next.distance;				
				if(dist[next.id] <= nextDistance) continue;
				dist[next.id] = nextDistance;
				q.add(new Node(next.id, dist[next.id]));
			}
		}
		
		return -1;
	}
	
	class Node {
		int id;
		int distance;
		public Node(int id, int distance) {		
			this.id = id;
			this.distance = distance;
		}
	}
}


public class Main {
	private static final int CMD_INIT 		= 100;
	private static final int CMD_CONNECT 	= 200;
	private static final int CMD_MERGE 		= 300;
	private static final int CMD_MOVE 		= 400;

	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	private static boolean run(BufferedReader br) throws Exception {

		int query_num;
		st = new StringTokenizer(br.readLine(), " ");
		query_num = Integer.parseInt(st.nextToken());
		
		int ans, mHotelA, mHotelB, mDistance, ret; 
		int mStart, mBrandA, mBrandB; 
		boolean ok = false;

		for (int q = 0; q < query_num; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int query = Integer.parseInt(st.nextToken());

			switch (query) {

			case CMD_INIT:
				int N;
				N = Integer.parseInt(st.nextToken());
				int[] mBrands = new int[N]; 
				for(int i = 0; i < N; i++) {
					mBrands[i] = Integer.parseInt(st.nextToken());
				}
				userSolution.init(N, mBrands); 
				ok = true;
				break;
				
			case CMD_CONNECT:
				mHotelA = Integer.parseInt(st.nextToken());
				mHotelB = Integer.parseInt(st.nextToken());
				mDistance = Integer.parseInt(st.nextToken());
				userSolution.connect(mHotelA, mHotelB, mDistance); 
				print(q, "connect", q, q, mHotelA, mHotelB, mDistance);
				break;
				
			case CMD_MERGE:
				mHotelA = Integer.parseInt(st.nextToken());
				mHotelB = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.merge(mHotelA, mHotelB); 
				print(q, "merge", ans, ret, mHotelA, mHotelB);
				if(ans != ret)
					ok = false; 
				break;
				
			case CMD_MOVE:
				mStart = Integer.parseInt(st.nextToken());
				mBrandA = Integer.parseInt(st.nextToken());
				mBrandB = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());
				ret = userSolution.move(mStart, mBrandA, mBrandB); 
				print(q, "move", ans, ret, mStart,  mBrandA, mBrandB);
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
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		if(ans!=ret) System.err.println("====================오류========================");
		System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
	}

	public static void main(String[] args) throws Exception {
		
		Long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//기출문제//호텔방문//sample_input2.txt"));

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