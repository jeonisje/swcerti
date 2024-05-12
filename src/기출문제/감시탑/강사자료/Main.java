package 기출문제.감시탑.강사자료;

import java.io.*;
import java.util.*;

// Java -> 더 빠른 방법이 있는지 연구 필요 ( C++은 정상범위 안, 자료구조 구동 속도의 차이)
class UserSolution {
	
	// index : color (1~5 + 0) 
	// key : hash
	// value : none 
	static TreeMap<Integer, Integer>[] tm;
	
	// key : hash
	// value : color 
	static HashMap<Integer, Integer>hm; 
	static int n; 
	
	static int getHash(int y, int x) {
		return y * 10000 + x; 
	}
	
	static int getDist(int a, int b) {
		int ay = a / 10000;
		int ax = a % 10000;
		int by = b / 10000;
		int bx = b % 10000;
		return Math.abs(ay - by) + Math.abs(ax - bx); 
	}
	
	public void init(int N) {
		n = N;
		hm = new HashMap<>();
		tm = new TreeMap[6];
		for(int i = 0; i < 6; i++)
			tm[i] = new TreeMap<>(); 
		return;
	}

	public void buildTower(int mRow, int mCol, int mColor) {
		// hash 생성 
		int hash = getHash(mRow, mCol);
		// 0 : 모든 점을 관리하는 공간
		tm[0].put(hash, 1); 
		// mColor에 해당하는 공간 
		tm[mColor].put(hash, 1);
		// 삭제를 위해 color 기록
		hm.put(hash, mColor); 
		return; 
	}
	
	public void removeTower(int mRow, int mCol) {
		// hash 생성 
		int hash = getHash(mRow, mCol);
		// 아무것도 존재하지 않으면 아무것도 안함. 
		if(hm.get(hash) == null)
			return; 
		int color = hm.get(hash); 
		// hm에서 삭제
		hm.remove(hash);
		// 0과 color번에서 삭제
		tm[0].remove(hash);
		tm[color].remove(hash); 
		return; 
	}
	
	public int countTower(int mRow, int mCol, int mColor, int mDis) {
		// 구간 확인 (문제에서 주어진대로)
		int sy = Math.max(mRow - mDis, 1);
		int sx = Math.max(mCol - mDis, 1); // 시작 (왼쪽위) 좌표
		int ey = Math.min(mRow + mDis, n);
		int ex = Math.min(mCol + mDis, n); // 끝 (오른쪽 아래) 좌표
		
		// 이 구간에 존재하는 탑의 개수 확인
		int sHash = getHash(sy, sx); 
		int eHash = getHash(ey, ex); 
		
		int cnt = 0;
		// 해당 구간에 포함될 수 있는 탑들만 확인 
		Iterator it = tm[mColor].subMap(sHash, true, eHash, true).keySet().iterator();
		while(it.hasNext()) {
			Integer now = (Integer)it.next();
			int y = now / 10000;
			int x = now % 10000;
			// 만약 사각 영역을 벗어난다면 continue
			if(y < sy || x < sx || y > ey || x > ex)
				continue;
			cnt++;
		}
		return cnt;
	}

	public int getClosest(int mRow, int mCol, int mColor) {
		// 아무 탑도 해당 색에 존재하지 않으면 -1
		if(tm[mColor].size() == 0)
			return -1;
		
		int curRow = mRow * 10000;
		int hash = getHash(mRow, mCol); 
		int res = Integer.MAX_VALUE;
		
		// 지금 row를 기반으로 아래 흝기
		Iterator it = tm[mColor].subMap(curRow, true, n*10000 + n, true).keySet().iterator();
		while(it.hasNext()) {
			int now = (Integer)it.next();
			// 만약 현재까지 찾은 탑까지의 거리보다 row(세로로 1직선 거리)가 더 멀어지면, 이후로는 더 볼 필요 없다. 
			if(Math.abs(now / 10000 - mRow) > res)
				break; 
			int dist = getDist(hash, now);
			res = Math.min(dist, res); 
		}
		
		// 지금 row를 기반으로 위로 흝기
		it = tm[mColor].subMap(1, true, curRow, false).descendingKeySet().iterator();
		while(it.hasNext()) {
			int now = (Integer)it.next();
			// 만약 현재까지 찾은 탑까지의 거리보다 row(세로로 1직선 거리)가 더 멀어지면, 이후로는 더 볼 필요 없다. 
			if(Math.abs(now / 10000 - mRow) > res)
				break; 
			int dist = getDist(hash, now);
			res = Math.min(dist, res); 
		}
		
		return res; 
	}
}
public class Main {

}
