package 연습문제.파이프연결;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	
	static int N, M;
	static ArrayList<Area>[] graph;
	
	static class Area {
		int idx;
		double cost;
		double press;
		
		public Area(int idx, double cost, double press) {		
			this.idx = idx;
			this.cost = cost;
			this.press = press;
		}
	}
	
	static class Node {
		int idx;
		double minPress;
		double totalCost;
		double efficiency;
		public Node(int idx, double minPress, double totalCost, double efficiency) {		
			this.idx = idx;
			this.minPress = minPress;
			this.totalCost = totalCost;
			this.efficiency = efficiency;
		}	
	}
	
	private static int dijkstra() {
		double dist[] = new double[N+2];
		Arrays.fill(dist, 1);
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Double.compare(o1.efficiency, o2.efficiency)) ;
		q.add(new Node(1, 1_000, 0, 1));
		dist[1] = 1;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			if(cur.idx == N) {
				return (int) Math.floor((1.0 - dist[cur.idx]) * 1_000_000);
			}
			if(dist[cur.idx] < cur.efficiency) continue;
			
			for(Area next : graph[cur.idx]) {
				double minPress = Math.min(cur.minPress, next.press);
				double totalSum = cur.totalCost + next.cost;
				double newEfficiency = 1.0 - minPress/totalSum;
				
				if(dist[next.idx] < newEfficiency) continue;
				dist[next.idx] = newEfficiency;
				q.add(new Node(next.idx, minPress, totalSum, dist[next.idx]));
			}
			
		}
		
		return 0;
	}
	
	
	public static void main(String[] args) throws Exception {
		
		//System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		graph = new ArrayList[N+1];
		for(int i=1; i<=N; i++) {
			graph[i] = new ArrayList<>();
		}
		
		
		for(int i=0; i<M; i++) {
			st = new StringTokenizer(br.readLine());
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int f = Integer.parseInt(st.nextToken());
			
			graph[a].add(new Area(b, c, f));
			graph[b].add(new Area(a, c, f));
		}		
	
		System.out.println(dijkstra());

		br.close();
	}
}
