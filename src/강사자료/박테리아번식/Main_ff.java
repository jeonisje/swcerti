package 강사자료.박테리아번식;


import java.io.*;
import java.util.*;


//#2. 2800번 실패할것을 고려하여 이 부분은 단순 ff로 진행한다 (약 755 ms)

class UserSolution {
	
	static class Node implements Comparable <Node>{
		int y;
		int x;
		int dist; 
		Node(int y, int x, int dist) {
			this.y = y;
			this.x = x; 
			this.dist = dist; 
		}
		@Override
		public int compareTo(Node o) {
			// #1. 거리 우선
			if(dist < o.dist) return -1;
			if(dist > o.dist) return 1; 
			
			// #2. 행 작은 거 우선
			if(y < o.y) return -1;
			if(y > o.y) return 1;  
			
			// #3. 열 작은 거 우선 
			if(x < o.x) return -1;
			if(x > o.x) return 1; 
			return 0; 
		}
	}
	
	// simluation용 map (200 x 200) 
	static int[][] map;
	
	// index : id 
	// value : 언제까지 살아남을 수 있는가? (bac.time + mTime)	
	static int[] life; 
	
	static int n; 
	
	static int[] ydir = {-1, 1, 0, 0};
	static int[] xdir = {0, 0, -1, 1}; 
	
	static boolean canBreed(int y, int x, Main.Bacteria bac, int t) {
		// flood fill 준비
		ArrayDeque<Node>q = new ArrayDeque<>(); 
		q.add(new Node(y, x, 0)); 
				
		int[][] visited = new int[n+1][n+1];
		visited[y][x] = 1; 
				
		// 성공하면 기록 그대로 유지
		// 실패하면-> 기록한거 다 물러줘야 한다. -> 경우 ; bac.size 만큼을 차지하지 못할때
		int cnt = 0; 
				
		while(!q.isEmpty()) {
			Node now = q.remove(); // 퍼지는 곳!
			
			cnt++; // now는 번식이 가능한 위치니까 -> 개수 확인
			if(cnt == bac.size)
				return true; 
					
			for(int i = 0; i < 4; i++) {
				int ny = now.y + ydir[i];
				int nx = now.x + xdir[i];
				// 범위 체크 
				if(ny < 1 || nx < 1 || ny > n || nx > n)
					continue;
				// 방문 체크
				if(visited[ny][nx] == 1)
					continue;
				// 다른 셀이 이미 이 공간을 차지하고 있다면 -> 못가겠죠 
				// map[ny][nx] != 0 -> 얘가 살아있나 죽어있나도 체크하면서 가야 합니다.
				if(life[map[ny][nx]] > t)
					continue; 
				visited[ny][nx] = 1;
				q.add(new Node(ny, nx, 123));
			}
		}
		// 여기까지 왔는데 위에서 return을 못만났다? cnt개수만큼의 크기가 없다
		return false; 
	}
	
	static Main.Result breed(int y, int x, Main.Bacteria bac, int t) {
		
		Main.Result ret = new Main.Result(); 
		
		// flood fill 준비
		// #1. 우선순위가 있으니 -> Priority Queue로 대체해서 활용
		PriorityQueue<Node>pq = new PriorityQueue<>();
		pq.add(new Node(y, x, 0)); 
		
		int[][] visited = new int[n+1][n+1];
		visited[y][x] = 1; 
		
		// 성공하면 기록 그대로 유지
		// 실패하면-> 기록한거 다 물러줘야 한다. -> 경우 ; bac.size 만큼을 차지하지 못할때
		int cnt = 0; 
		
		while(!pq.isEmpty()) {
			Node now = pq.remove(); // 퍼지는 곳!
			
			map[now.y][now.x] = bac.id; // 여기는 이제 퍼진다 -> 기록
			cnt++; // 한칸을 더 번식시켰다! 
			
			// bac.size만큼을 채워낼수 있다!
			if(cnt == bac.size) {
				// 성공한다! 
				ret.row = now.y;
				ret.col = now.x;
				return ret; // 여기에 return 있음
			}
			
			for(int i = 0; i < 4; i++) {
				int ny = now.y + ydir[i];
				int nx = now.x + xdir[i];
				// 범위 체크 
				if(ny < 1 || nx < 1 || ny > n || nx > n)
					continue;
				// 방문 체크
				if(visited[ny][nx] == 1)
					continue;
				// 다른 셀이 이미 이 공간을 차지하고 있다면 -> 못가겠죠 
				// map[ny][nx] != 0 -> 얘가 살아있나 죽어있나도 체크하면서 가야 합니다.
				if(life[map[ny][nx]] > t)
					continue; 
				visited[ny][nx] = 1;
				pq.add(new Node(ny, nx, Math.abs(ny - y) + Math.abs(nx - x)));
			}
		}
		
		// 위에 있는 return을 만나지 못했다? -> bac.size만큼을 채울 수 없었을때
		life[bac.id] = 0; // 아 기록 다했는데... 이거 무효 해야 하니까 -> 그냥 죽이면 됩니다.
		return ret; 
	}

    public void init(int N) {
    	n = N; 
    	map = new int[N+1][N+1]; 
    	life = new int[3001]; // id = 1~3000
    	return; 
    }

    public Main.Result putBacteria(int mTime, int mRow, int mCol, Main.Bacteria mBac) {
    	Main.Result ret = new Main.Result(); 	
    	
    	// 만약 무언가 존재하는 공간이라면 -> 그냥 (0,0) 태초상태의 ret을 return
    	if(life[map[mRow][mCol]] > mTime)
    		// 여기는 번식이 불가능!
    		return ret; 
    	
    	// 이번에는 먼저 [번식이 가능한 만큼의 크기가 확보되는가?] 
    	if(canBreed(mRow, mCol, mBac, mTime)) {
    		life[mBac.id] = mBac.time + mTime; 
    		ret = breed(mRow, mCol, mBac, mTime); 
    	}

        return ret;
    }
    
    public int killBacteria(int mTime, int mRow, int mCol) {
    	// 없으면 아무것도 동작하지 않는다 -> 소멸된 경우도 아무것도 하지 않는다.
    	if(map[mRow][mCol] == 0 || life[map[mRow][mCol]] <= mTime)
    		return 0; 
    	// 그게 아니면 죽여야 하는것
    	life[map[mRow][mCol]] = 0; // dat를 통해 죽었다 라고만 판정하고 
    	return map[mRow][mCol]; // 기존의 ID그대로 return
    }	
    
    public int checkCell(int mTime, int mRow, int mCol) {
    	// 없으면 아무것도 동작하지 않는다 -> 소멸된 경우도 아무것도 하지 않는다.
    	if(map[mRow][mCol] == 0 || life[map[mRow][mCol]] <= mTime)
    		return 0; 
    	// 그게 아니면 죽여야 하는것
    	//life[map[mRow][mCol]] = 0; // dat를 통해 죽었다 라고만 판정하고 
    	return map[mRow][mCol]; // 기존의 ID그대로 return
    }
}
public class Main_ff {

}
