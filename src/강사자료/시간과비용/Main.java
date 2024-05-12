package 강사자료.시간과비용;

import java.io.*;
import java.util.*;

class UserSolution {
	
	static class Node implements Comparable <Node>{
		int num;
		int cost;
		int time;
		Node(int num, int cost, int time) {
			this.num = num;
			this.cost = cost;
			this.time = time;
		}
		@Override
		public int compareTo(Node o) {
			if(time < o.time) return -1;
			if(time > o.time) return 1;
			if(cost < o.cost) return -1;
			if(cost > o.cost) return 1;
			return 0; 
		}
	}
	
	static ArrayList<Node>[] al; 
	static int n; 
	 
	public void init(int N, int K, int[]sCity, int[]eCity, int[]mCost, int[]mTime) {
		al = new ArrayList[N];
		for(int i = 0; i < N; i++)
			al[i] = new ArrayList<>(); 
		
		for(int i = 0; i < K; i++) 
			al[sCity[i]].add(new Node(eCity[i], mCost[i], mTime[i]));
		
		n=N; 
	    return;
	}
	 
	public void add(int sCity, int eCity, int mCost, int mTime) {
		al[sCity].add(new Node(eCity, mCost, mTime)); 
	    return;
	}
	 
	public int cost(int M, int sCity, int eCity) {
		PriorityQueue<Node>pq = new PriorityQueue<>();
		pq.add(new Node(sCity, 0, 0)); 
		
		int[][] dist = new int[n][M+1];
		for(int i = 0; i < n; i++) {
			for(int j = 0; j <= M; j++) {
				dist[i][j] = Integer.MAX_VALUE;
				if(i == sCity)
					dist[i][j] = 0; 
			}
		}
		
		while(!pq.isEmpty()) {
			Node now = pq.remove();
			
			if(now.num == eCity) 
				return now.time; 
			
			if(dist[now.num][now.cost] < now.time)
				continue;
		
			for(int i = 0; i < al[now.num].size(); i++) {
				Node next = al[now.num].get(i);
				int nCost = next.cost + now.cost;
				if(nCost > M)
					continue;
				int nTime = next.time + now.time;
				if(dist[next.num][nCost] <= nTime)
					continue;
				dist[next.num][nCost] = nTime;
				pq.add(new Node(next.num, nCost, nTime)); 
			}
		}
	    return -1;
	}
}

public class Main {

}
