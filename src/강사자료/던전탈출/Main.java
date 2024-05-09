package 강사자료.던전탈출;


import java.io.*;
import java.util.*;

class UserSolution {
	
	class Node {
		int y;
		int x;
		int dist;
		Node(int y, int x, int dist) {
			this.y = y;
			this.x = x;
			this.dist = dist; 
		}
	}
	
	class Edge implements Comparable <Edge>{
		int to;
		int cost;
		Edge (int to, int cost) {
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
	
	// index : 게이트 번호
	// value : 이 게이트가 유효하다면 1, 아니면 0 (삭제가 되었던, 아직 생성이 안되었던)
	int[] gateState; 
	
	// 현재 존재하는 게이트 정보 
	int[][] gateMap; 
	
	int[][] gameMap; 
	
	int n; 
	int stamina; 
	int[][] visited; 
	int c; 
	int[] ydir = {-1, 1, 0, 0};
	int[] xdir = {0, 0, -1, 1};
	ArrayList<Edge>[] al; 
	int[] dist; 
	
	public void init(int N, int mMaxStamina, int[][]mMap) {
		n = N;
		stamina = mMaxStamina;
		gameMap = mMap; 
		gateState = new int[201]; // addGate가 200번까지 호출
		gateMap = new int[n][n]; 
		visited= new int[n][n];
		c = 0; 
		al = new ArrayList[201];
		for(int i = 0; i <= 200; i++)
			al[i] = new ArrayList<>(); 
		dist = new int[201]; 
		return; 
	}
	
	public void addGate(int mGateID, int mRow, int mCol) {
		// mGateID번이 활성화가 된다!
		gateState[mGateID] = 1; 
		// 이 위치에 mGateID번 게이트가 있다! 
		gateMap[mRow][mCol] = mGateID;
		
		// (mRow, mCol)부터, 기사가 이동할수 있는 거리 = stamina만큼
		// 이 위치에서부터 stamina만큼 움직여보면서 -> 갈수있는 게이트를
		// 별도의 그래프로 연결을 시켜줄겁니다.
		// => FF
		ArrayDeque<Node>q = new ArrayDeque<>(); 
		q.add(new Node(mRow, mCol, 0));
		
		c++;
		visited[mRow][mCol] = c; 
		
		while(!q.isEmpty()) {
			Node now = q.remove();
			
			// 350 x 350짜리 다 볼 필요 X
			// 기사가 stamina가 동나는 시점부터 더 볼 필요 X
			if(now.dist > stamina) 
				break; 
			
			// 여기가 만약 다른 게이트에 도달했다!
			// -> 인접리스트에 서로를 연결해줍니다.
			int gID = gateMap[now.y][now.x]; // 아무것도 없으면 0이고, 뭔가 있으면 양수
			if(gateState[gID] == 1 && !(now.y == mRow && now.x == mCol)) {
				// 양방향 연결
				al[gID].add(new Edge(mGateID, now.dist));
				al[mGateID].add(new Edge(gID, now.dist));
			}
			
			for(int i = 0; i < 4; i++) {
				int ny = now.y + ydir[i];
				int nx = now.x + xdir[i];
				// 테두리가 모두 기둥이란 걸 압니다. 
				// -> 그래서 범위체크는 안해도 됩니다.
				if(gameMap[ny][nx] == 1)
					continue;
				if(visited[ny][nx] == c)
					continue; 
				visited[ny][nx] = c;
				q.add(new Node(ny, nx, now.dist + 1));
			}
		}
		int de = 1; 
		return; 
	}
	
	public void removeGate(int mGateID) {
		gateState[mGateID] = 0; 
		return; 
	}
	
	public int getMinTime(int mStartGateID, int mEndGateID) {
		// 만든 그래프로 단순 dijsktra
		for(int i = 0; i <= 200; i++)
			dist[i] = Integer.MAX_VALUE;
		dist[mStartGateID] = 0; 
		
		PriorityQueue<Edge>pq = new PriorityQueue<>();
		pq.add(new Edge(mStartGateID, 0));
		
		// 동작 
		while(!pq.isEmpty()) {
			Edge now = pq.remove();
			
			if(now.to == mEndGateID)
				return dist[now.to]; // now.cost; 
			
			if(dist[now.to] < now.cost)
				continue; 
			
			for(Edge next : al[now.to]) {
				// 원래는 있었는데, 삭제된 게이트가 존재할것. 
				// 다음에 갈려고하는 노드가 과거에는 연결되었었는데, 지금은 삭제된 애야. 
				if(gateState[next.to] == 0)
					continue;
				int nc = dist[now.to] + next.cost;
				if(dist[next.to] <= nc)
					continue; 
				dist[next.to] = nc;
				pq.add(new Edge(next.to, nc));
			}
		}
		// 경로 다봤는데, 못간다?
		return -1; 
	}
}
public class Main {

}
