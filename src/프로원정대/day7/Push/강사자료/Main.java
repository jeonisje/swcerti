package 프로원정대.day7.Push.강사자료;

import java.util.ArrayDeque;

class UserSolution {
	
	class Node {
		int ry;
		int rx; // 돌의 위치
		// 사람의 위치
		int py;
		int px; 
		int dist; // 거리 책정 (최적화 위함) 
		Node(int ry, int rx, int py ,int px, int dist) {
			this.ry = ry;
			this.rx = rx;
			this.py = py;
			this.px = px;
			this.dist = dist;
		}
	}
	
	class pNode {
		int y;
		int x;
		pNode(int y, int x) {
			this.y = y;
			this.x = x; 
		}
	}
	
	// 상 우 하 좌 
	int[] ydir = {-1, 0, 1, 0};
	int[] xdir = {0, 1, 0, -1}; 
	
	// game map 
	int[][] puzzle; 
	int n; 
	
	// 돌에 대한 방문 기록을 할 DAT
	// index 1, 2 = [y][x]
	// index 3 = 각 (y,x)에 4방향으로 올수 있었으니-> 이에대한 관리를 하기 위해 
	//int[][][] visited;
	//int cc; 
	
	//int[][] pVisited;
	//int ccc; 
	
	public boolean canGo(int dy, int dx, int y, int x, int ry, int rx) {
		// ff로 (y,x) -> (dy, dx) 갈수 있는지 확인
		ArrayDeque<pNode>q = new ArrayDeque<>();
		q.add(new pNode(y, x));
		
		int[][] visited = new int[n][n];
		visited[y][x] = 1; 
		//ccc++;
		//pVisited[y][x] = ccc; 
		
		while(!q.isEmpty()) {
			pNode now = q.remove();
			
			// 내가 (dy, dx)에 도달할수 있는지말 알면 된다.
			if(now.y == dy && now.x == dx)
				return true; // 사람이 여기로 갈수 있다!
			
			for(int i = 0; i < 4; i++) {
				int ny = now.y + ydir[i];
				int nx = now.x + xdir[i];
				if(ny < 0 || nx < 0 || ny >= n || nx >= n)
					continue;
				if(visited[ny][nx] == 1)
					continue;
				// 사람도 돌과 똑같이 벽은 못뚫을겁니다.
				if(puzzle[ny][nx] == 1)
					continue;
				// 사람은 지금 돌이 위치한 곳은 건너뛸수 없습니다.
				if(now.y == ry && now.x == rx)
					continue;
				visited[ny][nx] = 1;
				q.add(new pNode(ny ,nx));
			}
		}
		// 경로가 없다 (dy, dx)로 갈수가 없다!
		return false; 
	}
	
	public void init(int N, int[][] mMap){
		puzzle = mMap;
		n = N; 
		//visited = new int[N][N][4];
		//pVisited = new int[N][N];
		//ccc = 0; 
		//cc = 0; 
	    return;
	}
	 
	public int push(int mRockR, int mRockC, int mDir, int mGoalR, int mGoalC) {
		// 돌의 위치를 최소한의 이동으로, (mGoalR, mGoalC)에 도달하게 하는것이 목표
		// -> 설계상으로 돌 기반의 ff이 돌아갈것이다. 
		ArrayDeque<Node>q = new ArrayDeque<>();
		q.add(new Node(mRockR, mRockC, mRockR+ydir[mDir], mRockC+xdir[mDir], 0));
		
		// visited 세팅
		//cc++; 
		// visited[mRockR][mRockc][지금 방향?] = cc;
		int[][][] visited = new int[n][n][4];
		
		while(!q.isEmpty()) {
			Node now = q.remove();
			
			if(now.ry == mGoalR && now.rx == mGoalC)
				// 이게 정답이 되는 상황
				return now.dist;
			
			// now로부터 돌을 상하좌우로 이동시켜볼겁니다.
			for(int i = 0; i < 4; i++) {
				int ny = now.ry + ydir[i];
				int nx = now.rx + xdir[i]; 
				// 방향배열 사용 -> 범위체크
				if(ny < 0 || nx < 0 || ny >= n || nx >= n)
					continue; 
				if(visited[ny][nx][i] == 1)
					continue;
				// 벽인 공간을 갈수 없다!
				if(puzzle[ny][nx] == 1)
					continue; 
				// (ny,nx)로 돌을 옮기려고 하는데, 
				// #1. 사람은 now.ry-ydir[i], now.rx -xdir[i] 에 갈수 있는지 확인
				// #2. 사람의 시작 위치인지 알아야 하구요
				// #3. 지금 돌은 건너뛸수 없다.
				if(!canGo(now.ry-ydir[i], now.rx -xdir[i], now.py, now.px, now.ry, now.rx))
					continue;
				visited[ny][nx][i] = 1; 
				q.add(new Node(ny, nx, now.ry, now.rx, now.dist + 1));
			}
		}
		//못가는 경우는 없다 했으니 -> 그냥 여기서는 아무거나 return
	    return 0;
	}
}
public class Main {

}
