package 강사자료.Floyd_Warshall.로봇배송;

import java.io.*;
import java.util.*;

class UserSolution {
	
	int[][] dist;
	int ans;
	int[] sender;
	int[] receiver;
	int[] visited;
	int cc;
	int n; 


	void dfs(int lvl, int m, int pos, int cost) {
		if(cost >= ans)
			return;
		
		if(lvl >= m) {
			ans = Math.min(ans, cost);
			return; 
		}
		
		for(int i = 0; i < m; ++i) {
			if(visited[i] == cc)
				continue;
			visited[i] = cc;
			dfs(lvl+1, m, receiver[i], cost + dist[pos][sender[i]] + dist[sender[i]][receiver[i]]); 
			visited[i] = 0; 
		}
		
	}

	public void init(int N, int E, int[] sCity, int[] eCity, int[] mTime) {
		n = N;
		dist = new int[N][N];
		visited = new int[N];
		cc = 0; 
		for(int i = 0; i < N; ++i) {
			for(int j = 0; j < N; ++j) {
				dist[i][j] = 123456789; 
			}
			dist[i][i] = 0; 
		}
		for(int i = 0; i < E; ++i) 
			dist[sCity[i]][eCity[i]] = mTime[i]; 
		//floyd (O(N^3)) 
		for(int k = 0; k < N; ++k) 
			for(int i = 0; i < N; ++i) 
				for(int j = 0; j < N; ++j) 
					dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
		return;
	}
	
	// 700 x O(N^2) = 1,750,000
	public void add(int sCity, int eCity, int mTime) {
		if(dist[sCity][eCity] <= mTime)
			return;
		dist[sCity][eCity] = mTime;
		// update
		for(int i = 0; i < n; ++i) 
			for(int j = 0; j < n; ++j) 
				// k = 경유지 = sCity -> eCity
				dist[i][j] = Math.min(dist[i][j], dist[i][sCity] + mTime + dist[eCity][j]);
		return;
	}

	// 300 x 8 permutation (40,320) = 12,096,000
	public int deliver(int mPos, int M, int[] mSender, int[] mReceiver) {
		ans = 123456789;
		cc++;
		sender = mSender;
		receiver = mReceiver; 
		dfs(0, M, mPos, 0);
		return ans;
	}
}

public class Main {

}
