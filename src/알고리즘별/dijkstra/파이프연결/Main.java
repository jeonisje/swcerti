package 알고리즘별.dijkstra.파이프연결;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	static int N, M;
	static ArrayList<Node>[] graph;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		graph = new ArrayList[N+1];
		for(int i=0; i<N+1; i++) {
			graph[i] = new ArrayList<>();
		}
		
		for(int i=0; i<M; i++) {
			st = new StringTokenizer(br.readLine());
			int from = Integer.parseInt(st.nextToken());
			int to = Integer.parseInt(st.nextToken());
			int cost = Integer.parseInt(st.nextToken());
			int pressure = Integer.parseInt(st.nextToken());			
			graph[from].add(new Node(to, pressure, cost));
			graph[to].add(new Node(from, pressure, cost));
		}
		System.out.println(dijkstra());
	}
	
	static int dijkstra() {
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Double.compare(o1.efficency, o2.efficency));
		q.add(new Node(1, Integer.MAX_VALUE, 0));
		
		int[] dist = new int[N+1];
		Arrays.fill(dist, Integer.MAX_VALUE);
		dist[1] = 0;
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			
			if(cur.id == N) {
				return 1_000_000 - cur.efficency;
			}
			
			if(dist[cur.id] < cur.efficency) continue;
			for(Node next : graph[cur.id]) {
				int minPress = Math.min(cur.pressure, next.pressure);
				int nextCost = cur.cost + next.cost;
				//[수압-비용 효율 = 경로의 최대 수압 / 경로의 보강 비용의 합]
				int value = (int) Math.floor(((double)minPress/(double)nextCost) * 1_000_000);
				
				int eff = 1_000_000 - value;
				if(dist[next.id] < eff) continue;
				dist[next.id] = eff;
				q.add(new Node(next.id, minPress, nextCost, dist[next.id]));
				
			}
		} 
		return -1;
	}
	
	
	static class Node {
		int id;
		int pressure;
		int cost;
		int efficency;
		
		public Node(int id, int pressure, int cost) {		
			this.id = id;
			this.pressure = pressure;
			this.cost = cost;
		}	

		public Node(int id, int pressure, int cost, int efficency) {			
			this.id = id;
			this.pressure = pressure;
			this.cost = cost;
			this.efficency = efficency;
		}
	}

}

/*


3 2
2 1 2 4
2 3 5 3

/*
 
 
 428571
 

*/