package 강사자료.화물운송.dijkstra;



import java.io.*;
import java.util.*;

class UserSolution {
	
	static class Edge implements Comparable <Edge>{
		int to; // 노드 번호
		int cost; // 중량 
		Edge(int to, int cost) {
			this.to = to;
			this.cost = cost;
		}
		@Override
		public int compareTo (Edge o) {
			// 중량이 큰거부터 나오도록
			if(cost > o.cost) return -1;
			if(cost < o.cost) return 1;
			return 0; 
		}
	}
	
	ArrayList<Edge>[] al; 
	int n; 
	int[] dist; 
	
	public void init(int N, int K, int[] sCity, int[] eCity, int[] mLimit) {
		// 노드 번호 = 0~N번까지 
		al = new ArrayList[N];
		for(int i = 0; i < N; i++) 
			al[i] = new ArrayList<>();
		// K개의 간선 정보를 입력
		for(int i = 0; i < K; i++) {
			int from = sCity[i];
			int to = eCity[i];
			int cost = mLimit[i];
			// 단방향 연결
			al[from].add(new Edge(to, cost)); 
		}
		n = N; 
		dist = new int[n]; 
		return;
	}

	public void add(int sCity, int eCity, int mLimit) {
		// 단순하게 그냥 도로 하나만 더 놔주면 됩니다.
		al[sCity].add(new Edge(eCity, mLimit)); 
		return; 
	}

	public int calculate(int sCity, int eCity) {
		for(int i = 0; i < n; i++)
			dist[i] = 0; 
		dist[sCity] = 30001;
		
		PriorityQueue<Edge>pq = new PriorityQueue<>();
		pq.add(new Edge(sCity, 30001));
		
		while(!pq.isEmpty()) {
			Edge now = pq.remove(); // 지금까지중 가장 높은 중량을 가진게
			
			if(dist[now.to] > now.cost)
				continue; 
			
			if(now.to == eCity) {
				return dist[now.to];
			}
			
			for(Edge next : al[now.to]) {
				// 다음까지의 비용 = 현재까지의 최대 중량 vs next 다리의 중량 중 더 작은것
				int nc = Math.min(dist[now.to], next.cost);
				if(dist[next.to] >= nc)
					continue;
				dist[next.to] = nc;
				pq.add(new Edge(next.to, nc));
			}
		}
		return -1; 
	}
}
public class Main {

}
