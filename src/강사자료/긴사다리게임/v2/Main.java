package 강사자료.긴사다리게임.v2;


import java.io.*;
import java.util.*; 

//Ver 2. -> < 1,000ms -> 90% 통과
class UserSolution {
	
	// index : 열 번호 (1~100)
	// Key : 행 번호 (낮은거부터 오름차순으로 정렬되서 관리
	// Value : 이 index 열에서 이 행에는 이 value로 이동하는 가로줄이 있다! 
	TreeMap<Integer,Integer>[] tm; 
  
	public void init() {
		// 초기화
		tm = new TreeMap[101];
		for(int i = 0; i <= 100; i++)
			tm[i] = new TreeMap<>(); 
  		return;   
	}
	
	public void add(int mX, int mY) {
		// 어차피 여기서는 mX열과 mX+1열이 mY행에서 연결되는 가로줄이 있다!
		tm[mX].put(mY, 1);
    	return; 
	}
  
    public void remove(int mX, int mY) {
    	tm[mX].remove(mY); 
      	return; 
    }
    
    // 해결 끝
	public int getCrossCnt(int lineID) {
		int y = 0;
		int x = lineID;
		int cnt = 0; 
		while(true) {
			// 지금 내 열 
			Integer a = tm[x].higherKey(y); 
			Integer b = tm[x-1].higherKey(y);
			// 만나는 가로줄이 더 없다!
			if(a == null && b == null)
				break;
			else if(a != null && b == null) {
				x++;
				y = a;
			}
			else if(a == null && b != null) {
				x--;
				y = b; 
			}
			else {
				// 둘다 있다 
				if(a < b) {
					y = a;
					x++; 
				}
				else {
					// b < a
					y = b;
					x--; 
				}
			}
			cnt++; 
		}
		return cnt; 
	}

	public int getLineID(int mX, int mY) {
		int y = mY; 
		int x = mX;
		while(true) {
			// 지금 내 열 
			Integer a = tm[x].lowerKey(y); 
			Integer b = tm[x-1].lowerKey(y);
			// 만나는 가로줄이 더 없다!
			if(a == null && b == null)
				break;
			else if(a != null && b == null) {
				x++;
				y = a;
			}
			else if(a == null && b != null) {
				x--;
				y = b; 
			}
			else {
				// 둘다 있다 -> 더 아래에 있는것을 먼저 타고 가야 한다.
				if(a > b) {
					y = a;
					x++; 
				}
				else {
					// b < a
					y = b;
					x--; 
				}
			}
		}
		return x; 
	}
}
public class Main {

}
