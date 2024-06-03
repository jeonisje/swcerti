package 프로원정대.day8.dijkstra강의;

import java.io.*;
import java.util.*;

// Dijkstra
// -> dijkstra는 인접행렬 사용시 많이 느립니다.
/*
6 9
0 1 10
0 2 4
1 2 5
1 3 3
2 3 13
3 5 20
4 5 3
3 4 9 
1 4 18
 */

// 키워드 : 최단거리 / 최소비용
// -> 가중치의 합산인가? 유일 루트인가? BFs vs Dijkstra
// Dijsktra 탐욕적으로 선택해나가는 알고리즘
// --> 최장거리 구할수 있나? -> 못구합니다.
// --> 음수 가중치가 있으면 구할수 없습니다.

// 시간복잡도 : O(ElogV)

// *여러분들이 시간이 없다
// 나는 하나만 파서 운을 노려볼거다 => 2024년에는 dijsktra를 파세요

public class Main {

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	
	static class Edge implements Comparable <Edge>{
		int to; // 이 간선이 행하는 노드 번호
		int cost; // 이 간선의 가중치 (비용)
		Edge(int to, int cost){
			this.to = to;
			this.cost = cost;
		}
		@Override
		public int compareTo(Edge o) {
			if(this.cost < o.cost) return -1;
			if(this.cost > o.cost) return 1;
			return 0;
		}
	}
	
	static ArrayList<Edge>[] al; 
	static int V, E; 
	
	static void dijkstra(int start) {
		// #1. 준비단계
		// BFs - Queue : Dijkstra : Priority Queue
		// -> 가장 비용이 작은 루트부터 빼오기 위해 우선순위를 관리하는 PQ
		PriorityQueue<Edge>pq = new PriorityQueue<>();
		// 시작 노드를 pq에 넣을겁니다. 
		pq.add(new Edge(start, 0));
		
		// 출발노드로부터 다른 모든 노드까지의 최단 거리를 기록할 DAT
		// index : 노드 번호
		// value : 각 노드까지 내가 지금 찾은 최선의 거리 (루트) 를 저장
		// --> dijkstra 동작이 끝나면 -> 최단 거리만을 기록하고 있을겁니다.
		int[] dist = new int[V]; 
		for(int i = 0; i < V; i++) 
			dist[i] = Integer.MAX_VALUE;
		// 시작 노드까지의 최단 거리 (확정) = 0
		dist[start] = 0; 

		// #2. 동작단계
		while(!pq.isEmpty()) {
			// 지금까지 내가 찾은 루트중 가장 비용이 적은 (거리가 짧은) 루트를 빼옵니다.
			Edge now = pq.remove(); 
			
			// 확정된 노드 = 이미 dist에 어떤 값이 기록이 되어있을것이고,
			// 지금 나온 now의 cost가 dist[now]보다 크면 -> 이미 최단거리가 확정되어있다
			if(dist[now.to] < now.cost)
				// 이미 확정된 노드!
				continue;
			
			// 여기서부터 갈수 있는 다음 노드들에 대한 루트를 추가
			for(int i = 0; i < al[now.to].size(); i++) {
				Edge next = al[now.to].get(i); 
				// 이 다음 노드까지의 비용 먼저 구해보기 (next cost)
				// 다음 노드까지의 비용 = 지금 노드까지의 비용 + 지금 내가 타고 가는 간선의 비용 
				int nc = now.cost + next.cost; 
				
				// 최적화 => dist는 내가 지금까지 찾은 루트중 가장 최적의 루트만 갱신하면서 기록
				// --> 지금 내가 이미 찾은 루트보다 더 멀리 돌아가는 루트 => 기록하면 안된다.
				// 지금 상황 : next.to까지의 새로운 경로 (비용)을 찾았는데, 
				// --> 이게 내가 이전에 찾은 루트보다 (아직 확정되진 않았을수 있지만) 더 멀리 돌아가는거라면
				// --> 갱신하면 안됨
				
				// ******************
				if(dist[next.to] <= nc)
					continue;
				
				// 다음 목적지까지 확보한 거리는 얘다!
				dist[next.to] = nc; // 항상 갱신되면 안됩니다. 
				pq.add(new Edge(next.to, nc));
			}
		}
		for(int i = 0; i < V; i++) {
			System.out.println(i + " : " + dist[i]);
		}
	}

	public static void main(String[] args) throws IOException {
		st = new StringTokenizer(br.readLine());
		V = Integer.parseInt(st.nextToken());
		E = Integer.parseInt(st.nextToken());
		
		al = new ArrayList[V];
		for(int i = 0; i < V; i++)
			al[i] = new ArrayList<>();
		
		// 간선 정보 입력을 받아서 graph 구성
		for(int i = 0; i < E; i++) {
			st = new StringTokenizer(br.readLine());
			int from = Integer.parseInt(st.nextToken());
			int to = Integer.parseInt(st.nextToken());
			int cost = Integer.parseInt(st.nextToken());
			// 양방향 연결
			al[from].add(new Edge(to, cost)); 
			al[to].add(new Edge(from, cost));
		}
		// 0번 노드에서 출발하는 dijkstra
		dijkstra(0); 
	}
}