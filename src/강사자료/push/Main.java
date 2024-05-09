package 강사자료.push;


import java.io.*;
import java.util.*;

class UserSolution {
	
	class Node {
		int ry; // 돌 좌표 
		int rx; 
		int cy;
		int cx; // 캐릭터 좌표
		int dist; 
		Node(int ry, int rx, int cy, int cx, int dist) {
			this.ry = ry; 
			this.rx = rx;
			this.cy = cy;
			this.cx = cx;
			this.dist = dist; 
		}
	}
	
	int[][] gameMap; 
	//             상 우 하 좌 
	int[] ydir = {-1, 0, 1, 0};
	int[] xdir = {0, 1, 0, -1};
	int[][][] visited;
	int[][] cvisited; 
	int c; 
	int cc; 
	
	public void init(int N, int[][] mMap){
		gameMap = mMap; // 0 = 이동가능한 길, 1 = 이동 불가능한 길
		// 제약사항 보면 2. 주어지는 평면은 이동 불가능한 구역으로 둘러 쌓여있다.
		// ==> 사실상 저희가 나중에 방향배열 쓸때 -> 범위 체크 안해도 된다는 의미
		visited = new int[N][N][4]; 
		cvisited = new int[N][N];
		c = 0; 
		cc = 0; 
	    return;
	}
	 
	// 10번
	public int push(int mRockR, int mRockC, int mDir, int mGoalR, int mGoalC) {
	    // 돌 = (mRockR, mRockC) 
		// 사람 = 돌을 기반으로 mDir위치에 있다.
		int cY = mRockR + ydir[mDir];
		int cX = mRockC + xdir[mDir]; 
		
		// 돌에 기반하는 플러드필 시작
		ArrayDeque<Node>q = new ArrayDeque<>();
		q.add(new Node(mRockR, mRockC, cY, cX, 0));
		
		// 방문 기록을 할 visited
		c++;
		// visited[mRockR][mRockC][mDir] = c; 
		// 일단 하나만 기록 안함 -> 초기 위치는 동일한 위치 + 방향으로 돌아올 수 있다.
		// 한칸정도야 뭐.. 크게 문제 없을것.
		
		while(!q.isEmpty()) {
			Node now = q.remove();
			
			if(now.ry == mGoalR && now.rx == mGoalC)
				return now.dist; 
			
			// 여기서부터 갈수 있는 모든 위치들을 확인 -> 삽입
			for(int i = 0; i < 4; i++) {
				int ny = now.ry + ydir[i];
				int nx = now.rx + xdir[i];
				// 방향배열 썼으니까 가장 먼저 해야되는 것 = 범위체크
				//if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
				
				// 못가는 경우가 있죠 -> 돌은 여기로 갈 수 없다.
				if(gameMap[ny][nx] == 1)
					continue; 
				
				// (ny,nx)에 i방향에서 온적이 있어? 
				if(visited[ny][nx][i] == c)
					continue; 
				
				// (ny,nx)에는 일단 돌이 가본적도 없고, 갈 수 있는 공간이다.
				// 사람이 여기로 밀 수 있는가?
				// -> test하기 위해 알아야 하는 정보 
				// #1. 지금 사람의 위치
				// #2. 지금 돌의 위치 (못 건너뜀)
				// #3. 이 사람이 어디로 가야 하는가? (돌을 ny,nx로 밀기 위해서 위치해야하는 곳)
				if(!test(now.cy, now.cx, now.ry, now.rx, now.ry - ydir[i], now.rx - xdir[i]))
					continue; 
				
				// (ny, nx) 위치에 i방향으로 처음 오는것 => 예약
				visited[ny][nx][i] = c; 
				// (ny,nx)에 돌이 갔으면, 사람은 지금 돌이 있는 위치에 존재
				q.add(new Node(ny, nx, now.ry, now.rx, now.dist + 1));
			}
		}
		// 무조건 경로가 존재함이 보장. 
		return -1;
	}
	
	boolean test(int cy, int cx, int ry, int rx, int ty, int tx) {
      
      	if(gameMap[ty][tx] == 1)
          return false; 
      
		// ff => (ty, tx)에 갈수 있냐 없냐
		ArrayDeque<Integer>q = new ArrayDeque<>(); 
		q.add(cy * 100 + cx); 
      
		cc++;
		cvisited[cy][cx] = cc; 
		
		while(!q.isEmpty()) {
			int now = q.remove(); 
			int y = now / 100;
			int x = now % 100; 
			
			if(y == ty && x == tx)
				return true; 
			
			for(int i = 0; i < 4; i++) {
				int ny = y + ydir[i];
				int nx = x + xdir[i]; 
				if(gameMap[ny][nx] == 1)
					continue;
				if(cvisited[ny][nx] == cc)
					continue; 
				// 지금 돌이 있는 위치는 못건너감
				if(ny == ry && nx == rx)
					continue; 
				q.add(ny * 100 + nx);
				cvisited[ny][nx] = cc; 
			}
		}
		return false; 
	}
}
 

이호성 프로님 Ver.

import java.io.*;
import java.util.*;

class UserSolution {
	static class Stone {
		int row;
		int col;
		int person;
		int cnt;

		public Stone(int row, int col, int person, int cnt) {
			this.row = row;
			this.col = col;
			this.person = person;
			this.cnt = cnt;
		}
	}

	static class Person {
		int y;
		int x;

		public Person(int y, int x) {
			this.y = y;
			this.x = x;
		}
	}

	static int[][] map;
	static int[][][] visited;
	static int rRow, rCol, gRow, gCol, dir;
	static int n;

	public void init(int N, int[][] mMap) {
		map = mMap.clone();
		n = N;
		// visited = new int[N][N];

		return;
	}

	public int push(int mRockR, int mRockC, int mDir, int mGoalR, int mGoalC) {

		visited = new int[n][n][4];
		rRow = mRockR;
		rCol = mRockC;
		dir = mDir;
		gRow = mGoalR;
		gCol = mGoalC;
		int result = bfs();
		return result;
	}

	static int[] ydir = { -1, 0, 1, 0 };
	static int[] xdir = { 0, 1, 0, -1 };

	public int bfs() {

		ArrayDeque<Stone> q = new ArrayDeque<>();
		q.add(new Stone(rRow, rCol, dir, 0));

		// visited[rRow][rCol][dir] = 1;

		while (!q.isEmpty()) {
			// 상하좌우 이동 가능 여부 확인해야됌

			Stone now = q.remove();

			if (now.row == gRow && now.col == gCol) {
				return now.cnt;
			}

			int direction = now.person;

			for (int i = 0; i < 4; i++) {
				// 범위 밖부터 확인해보자
				int ny = now.row + ydir[i];
				int nx = now.col + xdir[i];
				if (ny < 0 || nx < 0 || ny >= n || ny >= n)
					continue;

				// 돌이 가고자 하는곳이 막혀있으면 못감.
				if (map[ny][nx] == 1)
					continue;

				// 상,하,좌,우 가 막혀있는곳인지
				if (i == 0) { // 돌이 위로 가고자 할때

					if (visited[ny][nx][2] == 1)
						continue;

					if (direction != 2 && !isGo(now, 2)) {
						// 사람이 돌의 아래에 없는데 아래로 못가면 패스
						continue;
					} else {
						// 사람이 돌의 아래에 있거나, 아래로 갈수있으면 예약
						q.add(new Stone(ny, nx, 2, now.cnt + 1));
						// 방문처리
						visited[ny][nx][2] = 1;
					}

				} else if (i == 1) { // 돌이 오른쪽으로 가고싶어

					if (visited[ny][nx][3] == 1)
						continue;

					if (direction != 3 && !isGo(now, 3))
						continue;
					// 사람이 돌의 왼쪽에 있거나, 왼쪽으로 갈수 있으면 예약
					q.add(new Stone(ny, nx, 3, now.cnt + 1));
					// 방문처리
					visited[ny][nx][3] = 1;

				} else if (i == 2) {// 돌이 아래로 가고싶어

					if (visited[ny][nx][0] == 1)
						continue;

					if (direction != 0 && !isGo(now, 0))
						continue;
					// 사람이 돌의 위쪽에 있거나, 위쪽으로 갈수 있으면 예약
					q.add(new Stone(ny, nx, 0, now.cnt + 1));
					// 방문처리
					visited[ny][nx][0] = 1;
				} else if (i == 3) { // 돌이 왼쪽으로 가고싶어

					if (visited[ny][nx][1] == 1)
						continue;

					if (direction != 1 && !isGo(now, 1))
						continue;
					// 사람이 돌의 오른쪽에 있거나, 오른쪽으로 갈수 있으면 예약
					q.add(new Stone(ny, nx, 1, now.cnt + 1));
					// 방문처리
					visited[ny][nx][1] = 1;
				}
			}
		}

		return -1;
	}

	public boolean isGo(Stone now, int goal) {

		// 사람이 가고자 하는 목표 위치정보
		int golY = now.row + ydir[goal];
		int golX = now.col + xdir[goal];

		if (map[golY][golX] == 1)
			return false;

		// 일단 현재 돌의 위치도 사람이 이동못하니까 이동불가 때림
		ArrayDeque<Person> q = new ArrayDeque<>();
		int visite[][] = new int[n][n];

		q.add(new Person(now.row + ydir[now.person], now.col + xdir[now.person]));

		// 현재 사람이랑 돌 위치 둘다 체크해둠
		visite[now.row + ydir[now.person]][now.col + xdir[now.person]] = 1;
		visite[now.row][now.col] = 1;

		while (!q.isEmpty()) {
			Person p = q.remove();

			if (p.y == golY && p.x == golX) {
				return true;
			}
			for (int i = 0; i < 4; i++) {
				int ny = p.y + ydir[i];
				int nx = p.x + xdir[i];

				// 경계밖
				if (ny < 0 || nx < 0 || ny >= n || ny >= n)
					continue;

				// 이동 불가구역
				if (map[ny][nx] == 1)
					continue;

				// 이미 방문했음
				if (visite[ny][nx] == 1)
					continue;

				q.add(new Person(ny, nx));
				visite[ny][nx] = 1;
			}
		}
		
		
		return false;
	}
}
public class Main {

}
