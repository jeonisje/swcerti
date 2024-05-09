package 강사자료.긴사다리게임.v1;



import java.io.*;
import java.util.*; 


//Ver 1. -> 1400ms -> TLE 발생 가능

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
		// mX열, mY행에서 mX+1열, mY행으로 이동하는 가로줄이 생긴다!
		tm[mX].put(mY, mX+1); 
		tm[mX+1].put(mY, mX); 
    	return; 
	}
  
    public void remove(int mX, int mY) {
    	tm[mX].remove(mY); 
    	tm[mX+1].remove(mY);
      	return; 
    }
    
    // 해결 끝
	public int getCrossCnt(int lineID) {
		// counting
		int cnt = 0; 
		int y = 0; // 0번행에서 시작
		int x = lineID; // 열에서 시작
		
		// 계속 내려가보면서, 건너가는 다리 개수 cn++; 
		// 얘는 아무리 많아도 5000개일거라는 것 압니다.
		while(true) {
			// 사다리 게임은 지금 열에서 계속 내려가면서 
			// 가장 빨리 만나는 가로줄에서 갈아타는 것
			// 지금 열에서 지금 행 기준으로 나보다 더 아래에 있는 행 중 제일 위에 있는 것
			Integer ny = tm[x].higherKey(y); 
			
			// 내 열에서 지금 행 기반으로 아래를 보니까 아무것도 없어?
			// 그럼 갈아탈거 없네? --> 1자로 10억까지 내려간다는 소리네?
			// --> 나는 내가 지금 있는 열에서 종료될거란 얘기.
			if(ny == null)
				break; 
			
			y = ny; // 이제 이 행으로 갈거고
			// 이 행에서는 이제 이 가로줄로 연결된 열로 갈아타야 합니다.
			x = tm[x].get(ny); 
			// 하나의 가로줄을 더 넘어갔죠
			cnt++; 
		}
		return cnt; 
	}

	public int getLineID(int mX, int mY) {
		int x = mX;
		int y = mY; 
		// 이번에는 반대로 이 좌표에서부터 올라갈겁니다.
		// --> (x, y)에는 가로줄이 없음이 보장된다.
		while(true) {
			// 이 x 열에서 내가 지금 있는 y행 기준으로 더 작은 행에 있는 가로줄 중 가장 아래에 있는것
			// 즉, 지금 y보다 작은 값중 제일 큰거
			Integer ny = tm[x].lowerKey(y); 
	
			// 더이상 위에 가로줄 X -> 중단시키고
			if(ny == null)
				break; 
			
			y = ny; // 이제 이 행으로 갈거고
			x = tm[x].get(ny);
		}
		// 내가 최종적으로 있던 열이 정답
		return x; 
	}
}
public class Main {

}
