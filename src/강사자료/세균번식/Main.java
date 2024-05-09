package 강사자료.세균번식;


import java.io.*;
import java.util.*; 

class UserSolution {
	
	static class Bacteria implements Comparable<Bacteria>{
		int y;
		int x;
		int cost; // 현재 이 박테리아의 에너지 소비량
		Bacteria(int y, int x, int cost) {
			this.y = y;
			this.x = x;
			this.cost = cost; 
		}
		// 추가 번식을 위한 우선 순위 설정
		// #1. 에너지 소비량 
		// #2. 행 작은 순
		// #3. 열 작은 순
		public int compareTo(Bacteria o) {
			if(cost > o.cost) return -1;
			if(cost < o.cost) return 1;
			if(y < o.y) return -1;
			if(y > o.y) return 1;
			if(x < o.x) return -1;
			if(x > o.x) return 1;
			return 0; 
		}
	}
	
	// 실제 사용할 N x N 크기의 맵 
	static int[][] MAP;
	// 맵의 각 위치에 대한 에너지 소비량의 정보
	static int[][] cost; 
	// 맵의 크기 N
	static int size; 
	// 각 타입의 박테리아의 개수를 저장
	// 1 : 파랑, 2 : 빨강
	static int[] cnt; 
	
	// 몇번째 박테리아인가? 
	static int num; 
	
	static int[]ydir = {-1, 1, 0, 0};
	static int[]xdir = {0, 0, -1, 1};
	
	static int[][] visited; 
	
	public void init(int N, int[][] Dish)
	{
		cost = Dish; 
		size = N;
		MAP = new int[N][N]; 
		visited = new int[N][N];
		cnt = new int[3]; // 1, 2박테리아
		num = 0; 
	}
	
	public int dropMedicine(int target, int R, int C, int energy)
	{
		// energy 번식 에너지를 가진 target타입 박테리아를 (R, C) 위치에 투입하는 함수
		// 일단, R, C를 MAP의 크기에 반영
		int y = R - 1;
		int x = C - 1; 
		
		// 규칙 #3 : 만약 다른 타입이 존재한다면 번식 X (종료)
		if(MAP[y][x] != target && MAP[y][x] != 0)
			// 현재 target type의 박테리아의 개수를 반환
			return cnt[target]; 
		
		// 규칙 #2 : 비어있는 칸이라면 바로 활성화
		/*
		이 때, 해당 CELL의 에너지 소비량 만큼 번식 에너지를 즉각 소모하게 됩니다. 
		번식 에너지보다 에너지 소비량이 더 크다면, 남은 번식 에너지는 0이 됩니다.   
		 */
		else if(MAP[y][x] == 0) {
			// target type의 박테리아가 하나 더 추가
			cnt[target]++;
			// 현재 칸의 cost만큼 소모
			energy -= cost[y][x];
			// 그리고 박테리아를 투입
			MAP[y][x] = target; 
		}
		
		// 이제 flood fill (퍼지기)
		ArrayDeque<Bacteria>q = new ArrayDeque<>(); 
		q.add(new Bacteria(y, x, 0)); 
		PriorityQueue<Bacteria>pq = new PriorityQueue<>(); 
		
		// 현재 박테리아 번호
		num++; 
		visited[y][x] = num;
		
		// 이 박테리아가 아직 에너지가 남아있을 동안
		while(energy > 0) {
			while(!q.isEmpty()) {
				Bacteria now = q.removeFirst();
				// 퍼지는 방향 탐색
				for(int i = 0; i < 4; i++) {
					int ny = now.y + ydir[i];
					int nx = now.x + xdir[i];
					if(ny < 0 || nx < 0 || ny >= size || nx >= size)
						continue;
					// 이번에 퍼져온 공간이라면
					if(visited[ny][nx] == num)
						continue; 
					// 비어있지 않은데, 같은 타입의 박테리아가 아니라면 continue;
					if(MAP[ny][nx] != 0 && MAP[ny][nx] != target)
						continue;
					
					// 같은 박테리아가 있던 곳으로는 퍼질 수 있다. 
					// "이번" flood fill에서 퍼졌다고 기록한다.
					visited[ny][nx] = num;
					
					// 지금 이 곳이 빈 곳이라면
					if(MAP[ny][nx] == 0) {
						// 새로 번식 가능한 위치가 된다.
						// 여기서는 "우선순위" 에 따라 한 군데서 퍼지기 시작할것이므로
						// pq에 일단 담는다.
						pq.add(new Bacteria(ny, nx, cost[ny][nx]));
					}
					// 같은 target의 공간이라면 활성화
					if(MAP[ny][nx] == target) {
						q.addLast(new Bacteria(ny, nx, 0));
					}
				}
			}
			// 다 퍼트려본 다음
			// 새로 번식 가능한 위치를 찾아본다. 
			// 만약 새로 번식이 가능한 빈 칸이 없었다면 번식 종료
			if(pq.isEmpty())
				break; 
			
			// 번식이 가능하다면 가장 우선순위가 높은 박테리아를 추출
			Bacteria b = pq.remove();
			// 이 칸에는 target type의 박테리아가 이제 침범 
			MAP[b.y][b.x] = target;  
			// target type의 박테리아가 하나 추가
			cnt[target]++; 
			
			// 즉각 활성화
			q.addLast(b);
			// 이만큼의 에너지 차감
			energy -= b.cost;
		}
		// target 박테리아의 개수 반환
		return cnt[target]; 
	}
	
	public int cleanBacteria(int R, int C) 
	{
		// 일단 좌표 매핑
		int y = R - 1;
		int x = C - 1; 
		
		// 백신을 떨어뜨린 위치의 박테리아 확인
		int target = MAP[y][x];
		
		// 빈 칸에 떨어뜨렸으면 소멸되는 박테리아 없음
		if(target == 0)
			return -1; 
		
		// 그게 아니면 다시 flood fill
		ArrayDeque<Bacteria>q = new ArrayDeque<>();
		q.addLast(new Bacteria(y, x, 0));
		
		// 이 위치를 초기화 -> 박테리아가 사라진다
		MAP[y][x] = 0;
		// 지워진 박테리아의 개수 counting
		// 이 위치의 박테리아가 하나 지워졌으니 1부터 시작
		int bcnt = 1; 
		
		while(!q.isEmpty()) {
			Bacteria now = q.removeFirst();
			for(int i = 0; i < 4; i++) {
				int ny = now.y + ydir[i];
				int nx = now.x + xdir[i];
				if(ny < 0 || nx < 0 || ny >= size || nx >= size)
					continue;
				// 같은 타입의 박테리아가 존재하는 칸이 아니면 -> 반응 없음 
				if(MAP[ny][nx] != target)
					continue;
				q.addLast(new Bacteria(ny, nx, 0));
				// visited 체크 없이 같은 칸으로만 퍼지면 된다. (다 0으로 바꿔주면서 가니까)
				MAP[ny][nx] = 0;
				bcnt++; 
			}
		}
		// target type 박테리아는 bcnt개 만큼 줄었다.
		cnt[target] -= bcnt;
		// target type 박테리아의 개수 반환
		return cnt[target]; 
	}
}

public class Main {
	
}
