package 강사자료.예약버스;

import java.io.*;
import java.util.*;

class Edge implements Comparable<Edge> {
    int to;
    int cost;

    Edge(int to, int cost) {
        this.to = to;
        this.cost = cost;
    }

    public int compareTo(Edge o) {
        if (this.cost < o.cost) return -1;
        if (this.cost > o.cost) return 1;
        return 0;
    }
}

class UserSolution {
    
	static ArrayList<Edge>[] al;
    static int n;
    static int[][] dist; 
    static int[] fromStart;
    static int[] fromEnd; 
    static int[][] fromStops;
    static ArrayList<Integer>path; 
    static int[] used;
    static int res;

    public void init(int N, int K, int mRoadAs[], int mRoadBs[], int mLens[]) {
        al = new ArrayList[N + 1];
        for (int i = 0; i <= N; i++) {
            al[i] = new ArrayList<>();
        }
        for (int i = 0; i < K; i++) {
            int from = mRoadAs[i];
            int to = mRoadBs[i];
            int cost = mLens[i];
            al[from].add(new Edge(to, cost));
            al[to].add(new Edge(from, cost));
        }
        n = N;
        fromStart = new int[n+1];
        fromEnd = new int[n+1];
        fromStops = new int[5][n+1]; 
        path = new ArrayList<>(); 
        return;
    }

    // 1,000 x O(1)
    public void addRoad(int mRoadA, int mRoadB, int mLen) {
        al[mRoadA].add(new Edge(mRoadB, mLen));
        al[mRoadB].add(new Edge(mRoadA, mLen));
        return;
    }
    
    static int[] dijkstra(int start, int ms, int me) {
    	int[] dist = new int[n+1];
    	for(int i = 0; i <= n; i++)
    		dist[i] = Integer.MAX_VALUE;
    	dist[start] = 0; 
    	PriorityQueue<Edge>pq = new PriorityQueue<>();
    	pq.add(new Edge(start, 0));
    	while(!pq.isEmpty()) {
    		Edge now = pq.remove();
    		if(dist[now.to] < now.cost)
    			continue;
    		for(Edge next : al[now.to]) {
    			if(next.to == ms || next.to == me)
    				continue; 
    			int nc = now.cost + next.cost;
    			if(dist[next.to] <= nc)
    				continue;
    			dist[next.to] = nc;
    			pq.add(new Edge(next.to, nc));
    		}
    	}
    	return dist; 
    }
    
    static void dfs(int M, int[] stops) {
    	// 경로가 편성되었다면
    	if(path.size() == M) {
    		int ret = 0;
    		// 시작 -> 첫 경로 || 도착 -> 마지막 경로로 못간다면 -> 종료
    		if(fromStart[stops[path.get(0)]] == Integer.MAX_VALUE || fromEnd[stops[path.get(M-1)]] == Integer.MAX_VALUE)
    			return; 
    		// 두 거리 합
    		ret += fromStart[stops[path.get(0)]];
    		ret += fromEnd[stops[path.get(M-1)]];
    		// 경로 하나씩 누적
    		for(int i = 0; i < M-1; i++) {
    			// i->i+1번, i+1->i+2번.... 
    			int nc = fromStops[path.get(i)][stops[path.get(i+1)]];
    			if(nc == Integer.MAX_VALUE)
    				return;
    			ret += nc; 
    		}
    		// 최소값 갱신
    		res = Math.min(ret, res);
    		return; 
    	}
    	for(int i = 0; i < M; i++) {
    		if(used[i] == 1)
    			continue;
    		used[i] = 1;
    		path.add(i);
    		dfs(M, stops);
    		path.remove(path.size()-1);
    		used[i] = 0; 
    	}
    }

    // 50 x dijkstra 7번 + 120개의 permutation x 5
    // 50 x (O(2000log(2000)) x 7 + 120 x 5) = 약 40,000,000
    public int findPath(int mStart, int mEnd, int M, int mStops[]) {
    	fromStart = dijkstra(mStart, mStart, mEnd);
    	fromEnd = dijkstra(mEnd, mStart, mEnd); 
    	for(int i = 0; i < M; i++)
    		fromStops[i] = dijkstra(mStops[i], mStart, mEnd);
    	
    	res = Integer.MAX_VALUE; 
    	path = new ArrayList<>();
    	used = new int[M]; 

    	// 5개에 대한 모든 조합 생성(permutation)
    	dfs(M, mStops); // mStops의 정류장을 건너가는 모든 순서 생성 후 경로 거리 체크
    	
    	if(res == Integer.MAX_VALUE) 
    		return -1;
    	return res;
    }
}

public class Main {

}
