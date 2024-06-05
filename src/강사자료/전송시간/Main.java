package 강사자료.전송시간;

import java.io.*;
import java.util.*;

class UserSolution {
	
	class Edge implements Comparable <Edge> {
		int to, cost;
		Edge(int to, int cost) {
			this.to = to;
			this.cost = cost;
		}
		@Override
		public int compareTo(Edge o) {
			if(cost < o.cost) return -1;
			if(cost > o.cost) return 1;
			return 0;
		}
	}
	
	// global network => 모든것을 담는 네트워크
	ArrayList<Edge>[] globalNetwork; 
	
	// index 1 : group #
	// index 2 : from (id = 1~30)
	// value : 인접리스트
	ArrayList<Edge>[][] groupNetwork;
	
	int[] dist; 
	int n; 
	
	int dijkstra(ArrayList<Edge>[]al, int start, int dest, int count) {
		for (int i = 0; i <= count; i++)
	        dist[i] = Integer.MAX_VALUE;
	    dist[start] = 0; 
	    PriorityQueue<Edge>pq = new PriorityQueue<>(); 
	    pq.add(new Edge(start, 0)); 

	    while (!pq.isEmpty()) {
	        Edge now = pq.remove();
	        if (dist[now.to] < now.cost)
	            continue;
	        if (now.to == dest)
	            return now.cost;
	        
	        for (Edge next : al[now.to]) {
	            int nc = next.cost + now.cost;
	            if (dist[next.to] <= nc)
	                continue;
	            dist[next.to] = nc;
	            pq.add(new Edge(next.to, nc)); 
	        }
	    }
	    return -1; 
	}
	
	void removeEdge(ArrayList<Edge>[]al, int a, int b) {
	    for (int i = 0; i < al[a].size(); i++) {
	        if (al[a].get(i).to == b) {
	            al[a].remove(i);
	            break;
	        }
	    }
	    for (int i = 0; i < al[b].size(); i++) {
	        if (al[b].get(i).to == a) {
	            al[b].remove(i);
	            break;
	        }
	    }
	}
	
	void update(int id) {
		// 도착 노드 (1, 2, 3)에 대한 조합만 확인
		for(int i = 1; i <= 3; i++) {
			for(int j = i+1; j <= 3; j++) {
				// 1-2, 1-3, 2-3
				int dist = dijkstra(groupNetwork[id], i, j, 30); 
				if(dist != -1) {
					int a = id * 100 + i; // id번 그룹과 i번이 연결되는 대표 노드 번호
					int b = id * 100 + j; // id번 그룹과 j번이 연결되는 대표 노드 번호
					
					// 거리가 업데이트 되었으니 -> 기존 간선을 global Network에서 삭제하고
					// 간선을 줄이기 위해 실제 삭제 진행
					// 소규모 그룹의 개수 = 300개 3개씩 => 900
					removeEdge(globalNetwork, a, b); 
					
					// 새로 가선 추가
					globalNetwork[a].add(new Edge(b, dist));
					globalNetwork[b].add(new Edge(a, dist));
				}
			}
		}
	}
  
	public void init(int N, int K, int[] mNodeA, int[] mNodeB, int[] mTime) {
		// 총 노드 번호
		n = N * 100 + 30;
		globalNetwork = new ArrayList[n+1];
		groupNetwork = new ArrayList[N+1][31]; 
		dist = new int[n+1];
		
		// 활성화 
		for(int i = 1; i <= n; i++) 
			globalNetwork[i] = new ArrayList<>();
		for(int i = 1; i <= N; i++) 
			for(int j = 1; j <= 30; j++)
				groupNetwork[i][j] = new ArrayList<>();
		
		// 간선 정보 처리
		for(int i = 0; i < K; i++) {
			int groupA = mNodeA[i] / 100; // mNodeA[i]의 소속
			int groupB = mNodeB[i] / 100; // mNodeB[i]의 소속
			int cost = mTime[i]; 

			// 같은 그룹이라면 -> 소규모 그룹 노드 업데이트
			if(groupA == groupB) {
				int from = mNodeA[i] % 100;
				int to = mNodeB[i] % 100; 
				groupNetwork[groupA][from].add(new Edge(to, cost));
				groupNetwork[groupA][to].add(new Edge(from, cost));
			}
			else {
				// 다른 그룹이라면 -> 어차피 외부로 통하는 대표 노드만 연결될테니 연결 
				globalNetwork[mNodeA[i]].add(new Edge(mNodeB[i], cost));
				globalNetwork[mNodeB[i]].add(new Edge(mNodeA[i], cost));
			}
		}
		
		// 현존하는 모든 노드에 대해 거리 업데이트
		for(int i = 1; i <= N; i++)
			update(i); 
		return; 
	}
	 
	// 200 x 
	public void addLine(int mNodeA, int mNodeB, int mTime) {
		int groupA = mNodeA / 100;
	    int groupB = mNodeB / 100; 
	    
	    // 같은 그룹이라면
	    if (groupA == groupB) {
	        // 간선 추가
	        int from = mNodeA % 100;
	        int to = mNodeB % 100;
	        groupNetwork[groupA][from].add(new Edge(to, mTime));
	        groupNetwork[groupA][to].add(new Edge(from, mTime));
	        // 경로 다시 체크
	        update(groupA); 
	    }
	    else {
	        globalNetwork[mNodeA].add(new Edge(mNodeB, mTime));
	        globalNetwork[mNodeB].add(new Edge(mNodeA, mTime));
	    }
	    return;
	}

	// 200 x
	public void removeLine(int mNodeA, int mNodeB) {
		int groupA = mNodeA / 100;
	    int groupB = mNodeB / 100;

	    // 같은 그룹이라면
	    if (groupA == groupB) {
	        // 간선 추가
	        int from = mNodeA % 100;
	        int to = mNodeB % 100;
	        removeEdge(groupNetwork[groupA], from, to); 
	        // 경로 다시 체크
	        update(groupA);
	    }
	    else {
	        removeEdge(globalNetwork, mNodeA, mNodeB);
	    }
	    return; 
	}
	
	public int checkTime(int mNodeA, int mNodeB) {
		return dijkstra(globalNetwork, mNodeA, mNodeB, n);
	}
}
public class Main {
	
}
