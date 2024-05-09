package 강사자료.문명;


import java.io.*;
import java.util.*; 

class UserSolution {
	
	// key : 부족의 고유 번호 (1~10억)
	// value : 특정 id로 매핑 (newCivilization()에서 새로운 부족이 생길떄마다 -> 등록)
	static HashMap<Integer, Integer> hm; 
	static int id; 
	
	// 실제 시뮬레이션(?)을 할 맵
	// index = (r,c);
	// value = id (고유 번호) 
	static int[][] map;
	
	// union-find용 parent
	static int[] parent; 
	
	// index : id
	// value : 이 id번 부족이 소유하고 있는 땅의 개수
	static int[] landCnt; 
	
	// index : id
	// value : 이 id에 매핑된 실제 고유 부족 번호
	static int[] dat; 
	
	// 방향배열
	static int[] ydir = {-1, 1, 0, 0};
	static int[] xdir = {0, 0, -1, 1}; 
	static int n; 
	
	static int find(int node) {
		if(node == parent[node])
			return node;
		return parent[node] = find(parent[node]);  
	}
	
	void init(int N) {
		hm = new HashMap<>(); 
		id = 1; 
		// 좌표 체계는 (1~N)
		map = new int[N+1][N+1]; 
		// newCivilization의 호출 횟수 = 60,000 
		parent = new int[60001]; 
		landCnt = new int[60001];
		dat = new int[60001]; 
		// parent setting
		for(int i = 0; i <= 60000; i++)
			parent[i] = i; 
		n = N;
	}

	int newCivilization(int r, int c, int mID) {
		// (r,c)에 부족을 발생시켜봅니다. 
		// 만약 상하좌우 주변에 다른 부족이 이미 존재하면 -> 흡수가 된다.
		// 아니라면 -> 새로운 부족 발생 
		
		// counting = dat
		// int[]cnt = new int[60001]; 
		
		// key : id 
		// value : 상하좌우에 이 id가 몇개가 발생했는가? (count) 
		HashMap<Integer, Integer>temp = new HashMap<>(); 
		
		int maxCnt = Integer.MIN_VALUE;
		int minID = Integer.MAX_VALUE; 
		
		// #1. (r,c)주변을 먼저 둘러보자
		for(int i = 0; i < 4; i++) {
			int ny = r + ydir[i];
			int nx = c + xdir[i]; 
			// 방향배열을 사용했다면 -> 범위 체크
			if(ny < 1 || nx < 1 || ny > n || nx > n)
				continue; 
			
			// (ny, nx)가 0이다? -> 아무것도 없다
			if(map[ny][nx] == 0)
				continue; 
			
			// 그게 아니라면 -> 일단 map에는 뭔가는 기록이 되어있다
			// --> 뭔가는 있지만, 삭제된 녀석일수도 있으니 방심하면 안된다.
			int num = map[ny][nx]; // id 
			// 얘가 소속된 부족을 확인해야하는것이니까 
			int pa = find(num); // 소속을 찾아갑니다. // 소속 번호
			
			// 헷갈리지 않도록 모든 [존재] 확인은 그냥 hm을 통해서 할것.
			// hm에서 실제 pa의 고유 번호를 찾아봤는데 없다! 
			if(hm.get(dat[pa]) == null)
				// 실존했었는데 remove나 merge를 통해서 사라진 애
				continue; 
			
			// pa = 실존하는 ID -> counting 
			if(temp.get(pa) == null)
				temp.put(pa, 0);
			temp.put(pa, temp.get(pa) + 1); // 한개를 더 찾았다! 
			
			// #1. 이게 최대인가? 
			int cnt = temp.get(pa); 
			if(cnt > maxCnt) {
				maxCnt = cnt; 
				minID = pa; 
			}
			
			// #2. 만약 현존하는 최대값과 같다면 -> 고유 ID 기반으로 비교
			else if(cnt == maxCnt) {
				if(dat[pa] < dat[minID]) 
					minID = pa; 
			}
		}
		
		// 일단 뭔가는 있다 -> 얘는 새로 발생하는 부족이 아니고, 
		// minID쪽에 흡수가 될것이다.
		if(minID != Integer.MAX_VALUE) {
			map[r][c] = minID; // 흡수
			landCnt[minID]++; 
			return dat[minID]; // 고유번호 return
		}
		
		// 4방향을 다 확인했는데, 아무것도 없다? 
		// -> 새로운 부족이 발생한다!
		
		// 등록
		hm.put(mID, id); // 새로운 부족 등록
		dat[id] = mID; 
		landCnt[id] = 1; // 이 id의 부족은 한개의 땅을 가지게 된다. 
		map[r][c] = id; 
		id++; // 다음 
		return mID;
	}

	int removeCivilization(int mID) {
		// 존재하지 않는 겨우
		if(hm.get(mID) == null) 
			return 0; 
		
		// 존재하는 경우다!
		int num = hm.get(mID);
		int pa = find(num); 
		
		int res = landCnt[pa]; // 땅의 개수
		
		// 삭제할거 다 삭제
		hm.remove(mID);
		landCnt[pa] = 0;
		dat[pa] = 0; 
		//parent[pa] = 0; 
		return res;
	}

	int getCivilization(int r, int c) {
		// 일단 여기에 아무것도 기록이 없다면 => 0
		if(map[r][c] == 0)
			return 0; 
		// 그게 아니라면, 뭔가는 있는데 -> 
		// map은 직접 삭제 안해주고 있으니, 삭제된 친구인지 한번 확인
		int num = map[r][c];
		int pa = find(num); 
		// hm에 없으면 삭제된 친구
		if(hm.get(dat[pa]) == null)
			return 0; 
		return dat[pa];
	}

	int getCivilizationArea(int mID) {
		if(hm.get(mID) == null)
			return 0;
		int num = hm.get(mID);
		int pa = find(num); 
		return landCnt[pa];
	}

	int mergeCivilization(int mID1, int mID2) {
		// 전형적인 union
		int pa = find(hm.get(mID1)); 
		int pb = find(hm.get(mID2)); 
		
		// if(pa == pb) return; 
		parent[pb] = pa; 
		landCnt[pa] += landCnt[pb]; 
		
		// 처리절차
		landCnt[pb] = 0;
		dat[pb] = 0;
		hm.remove(mID2); 
		
		return landCnt[pa];
	}
}
public class Main {

}
