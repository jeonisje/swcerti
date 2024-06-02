package 프로원정대.day8.알뜰기차여행;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
	
	static int N, T;
	static ArrayList<Node>[] graph;
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		T = Integer.parseInt(st.nextToken());
		
		graph = new ArrayList[N];
		
		for(int i=0; i<N; i++) {
			graph[i] = new ArrayList<>();
		}
		
		for(int i=0; i<T; i++) {
			st = new StringTokenizer(br.readLine());
			int from = Integer.parseInt(st.nextToken());
			int to = Integer.parseInt(st.nextToken());
			int cost = Integer.parseInt(st.nextToken());
			
			graph[from].add(new Node(to, cost));			
		}
		
		int ret = dijkstra();
		if(ret == -1) {
			System.out.println("impossible");			
		} else {
			System.out.println(ret);
		}
	}
	
	static int dijkstra() {
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.cost, o2.cost));
		q.add(new Node(0, 0));
		
		int[] dist = new int[N+1];
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			
			if(cur.id == N-1) return dist[cur.id];			
			if(dist[cur.id] < cur.cost) continue;
			
			for(Node next : graph[cur.id]) {
				int nextCost = cur.cost + next.cost;
				if(dist[next.id] <= nextCost) continue;
				dist[next.id] = nextCost;
				q.add(new Node(next.id, dist[next.id]));
			}
			
		}
		
		return -1;
	}
	
	static class Node {
		int id;
		int cost;
		public Node(int id, int cost) {		
			this.id = id;
			this.cost = cost;
		}
	}
}

