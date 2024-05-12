package 강사자료.화물운송;


import java.io.*;
import java.util.*;

class UserSolution {
	
	static class Edge {
		int to; // 노드 번호
		int cost; // 중량 
		Edge(int to, int cost) {
			this.to = to;
			this.cost = cost;
		}
	}
	
	ArrayList<Edge>[] al; 
	int n; 
	
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
		return;
	}

	public void add(int sCity, int eCity, int mLimit) {
		// 단순하게 그냥 도로 하나만 더 놔주면 됩니다.
		al[sCity].add(new Edge(eCity, mLimit)); 
		return; 
	}

	public int calculate(int sCity, int eCity) {
		// 여러 경로 중 -> 해당 경로의 최소 중량이 최대가 되는 경우
		// ** 만약 경로가 없으면 -1
		// 만약 문제에서 실패하는 경우가 아마 많을거다 -> 여기서 한번 체크하고 넘어가는게 효율적일텐데..
//		if(경로가 없다면)
//			return -1; 
		
		// 지금 문제에서는 위에서 저렇게 체크 안해도 큰 문제 없을거다
		// --> 문제에서 힌트가 주어지지 않았다면 고려하지 마세요.
		// 경로는 일단 존재하니까 -> 어떠한 답이라도 나올테니
		// parametric search
		
		// mid = 이 중량이 최대이다 -> 즉 이 중량이면 sCity- >eCity 
		int left = 1; // 최소 중량 
		int right = 30000; // 최대 중량
		
		while(left <= right) {
			int mid = (left + right) / 2;
			if(test(mid, sCity, eCity)) {
				// 이 중량이면 -> sCity -> eCity 갈수 있어?
				// 통과했다면 -> 목표 : [최대] 중량이니까
				left = mid + 1; 
			}
			else {
				// 실패 -> 너무 무거워 -> 다리 다 무너짐
				// 조금더 가볍게 가볼까?
				right = mid -1; 
			}
		}
		if(right == 0)
			return -1; 
		return right; 
	}
	
	boolean test(int limit, int start, int dest) {
		// limit => 최대 중량
		// 즉, 이 limit을 초과하는 다리는 건널 수 없다.
		
		// start -> dest 가는 길이 있냐? -> bfs
		ArrayDeque<Edge>q = new ArrayDeque<>();
		q.add(new Edge(start, 0));
		
		int[] visited=  new int[n];
		visited[start] = 1; 
		
		while(!q.isEmpty()) {
			Edge now = q.remove();
			
			// 중간에라도 now.to == eCity가 되는 순간이 있다면 -> 갈수 있다는것. 
			if(now.to == dest)
				return true; 
			
			for(Edge next : al[now.to]) {
				if(visited[next.to] == 1)
					continue; 
				// 최대 중량 = limit이니까
				// 얘보다 더 낮은 다리를 건너가면 무너집니다. -> 여기는 건너갈 수 가 없습니다.
				if(next.cost < limit)
					continue; 
				q.add(next);
				visited[next.to] = 1;  
			}
		}
//		if(visited[dest] == 1)
//			return true; 
		return false; 
	}
}


public class Main {

}
