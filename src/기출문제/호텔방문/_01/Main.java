package 기출문제.호텔방문._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.TreeSet;

class UserSolution {
	
	int MAX_BRAND = 50;	
	int N;
	
	//ArrayList<Integer>[] hotelByBrand;
	ArrayList<Node>[] graph;
	ArrayList<Node>[][] graphByBrand;
	
	int[] brandByHotel;
	int[] countByBrand;
	
	int[] parent;
	int[] dist;
	
	
	void init(int N, int[] mBrands) {
		this.N = N;
		
		//hotelByBrand = new ArrayList[MAX_BRAND];
		
		for(int i=0; i<MAX_BRAND; i++) {
			//hotelByBrand[i] = new ArrayList<>();
			
		}
		
		brandByHotel = mBrands;
		graph = new ArrayList[N];
		for(int i=0; i<N; i++) {
			graph[i] = new ArrayList<>();			
		}
		
		parent = new int[MAX_BRAND];
		
		for(int i=0; i<MAX_BRAND; i++) {
			parent[i] = i;		
		}
		dist = new int[N+1];
		countByBrand = new int[MAX_BRAND];
		for(int i=0; i<N; i++) {
			//hotelByBrand[mBrands[i]].add(i);
			countByBrand[mBrands[i]]++;
		}
		
		return;
	}
	
	// 10_000
	void connect(int mHotelA, int mHotelB, int mDistance) {		
		graph[mHotelA].add(new Node(mHotelB, mDistance));
		graph[mHotelB].add(new Node(mHotelA, mDistance));
		return;
	}
	
	int find(int a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		
		parent[pb] = pa;
		//hotelByBrand[pa].addAll(hotelByBrand[pb]);		 
		countByBrand[pa] += countByBrand[pb];
	}
	
	
	// 100
	int merge(int mHotelA, int mHotelB) {
		//union()
		int brandA = brandByHotel[mHotelA];
		int brandB = brandByHotel[mHotelB];
		
		union(brandA, brandB);
		
		int pa = find(brandA);
		
		return countByBrand[pa];
	}

	// 1_000
	int move(int mStart, int mBrandA, int mBrandB) {
		int pa = find(mBrandA);
		int pb = find(mBrandB);
		
		int ans1 = 0;
		int ans2 = 0;
		
		if(pa == pb) {			
			Node ret1 = dijkstra(mStart, pa, -1);
			Node ret2 = dijkstra(mStart, pa, ret1.id);
			ans1 = ret1.dist;
			ans2 = ret2.dist;
			return ans1 + ans2; 
		}
		
		Node ret1  = dijkstra(mStart, pa, -1);
		Node ret2  = dijkstra(mStart, pb, -1);
		
		return ret1.dist + ret2.dist;
	}
	
	Node dijkstra(int start, int brand, int skip) {
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.dist, o2.dist));
		q.add(new Node(start, 0));
		dist[start] = 0;
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			
			int pBrand = find(brandByHotel[cur.id]);
			if(pBrand == brand) {
				if(cur.id != skip && cur.id != start)
					return new Node(cur.id, dist[cur.id]);
			}
			for(Node next : graph[cur.id]) {				
				int nextCost = cur.dist + next.dist;
				if(dist[next.id] <= nextCost) continue;
				dist[next.id] = nextCost;
				q.add(new Node(next.id, dist[next.id]));
			}
		}
		
		return new Node(0, 0);
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
		//if(ans!=ret) System.err.println("====================오류========================");
		//System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
	}

	public static void main(String[] args) throws Exception {
		
		Long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//기출문제//호텔방문//sample_input.txt"));

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