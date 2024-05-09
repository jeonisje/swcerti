package 강사자료.긴사다리게임;


import java.io.*;
import java.util.*; 

class UserSolution {
  
	// DAT 
	// index : 열 번호 (1~100)
	// value : treemap 
	//         -> key : 행 번호 (가로줄이 존재한다)
	//         -> value : 이 행에서 (이 가로줄을 타면) 어떤 열(lineID)으로 가는가? 
	static TreeMap<Integer, Integer>[] tm; 
	
	public void init() {
		tm = new TreeMap[101]; 
		for(int i = 0; i <= 100; i++)
			tm[i] = new TreeMap<>();
 		return;   
	}
	
	// 200,000 x log(200,000)
	public void add(int mX, int mY) {
		// (mX,mY)와 (mX+1,mY)가 이어지는 가로줄을 긋는다. 
		tm[mX].put(mY, mX+1);
		// 반대방향으로도
		tm[mX+1].put(mY, mX); 
   	return; 
	}
 
	// 5,000 x log(200,000)
   public void remove(int mX, int mY) {
   	// (mX,mY)와 (mX+1,mY)가 이어지는 가로줄을 삭제한다
   	tm[mX].remove(mY); 
   	tm[mX+1].remove(mY);
     	return; 
   }
   
   // 500 x 5,000 x log(200,000)
	public int getCrossCnt(int lineID) {
		// (lineID, 0)에서 출발해서, 10억번행까지 갈때, 몇개의 가로줄을 거쳐가는가? 
		int cnt = 0; 
		int x = lineID; // lineID번 열 
		int y = 0; // 0번 행
		
		while(true) {
			// 언제까지 내려가봐야할까요? 
			// A. 지금 내가 위치하고있는 행을 기준으로 나보다 더 아래에 가로줄이 없다면 -> 그만하면 된다.
			// 왜냐? 가로줄이 없으면 지금 열에서 10억번 행까지 쭈욱 내려가면 됨. 
			
			// * 지금 내가 존재하는 행 아래에 있는 가로줄 중 가장 위에 있는 것.
			// 내려갈수록 크다 => x 보다 큰것 중 가장 작은것 
			Integer ny = tm[x].higherKey(y); 
			
			if(ny == null) 
				// 지금 내가 있는 행 아래에는 가로줄이 없음!
				break; 
			
			y = ny;
			x = tm[x].get(ny); // ny행에서 존재하는 가로줄이 인도하는 열로 갑니다. 
			// 하나의 가로줄을 건너갔다!
			cnt++; 
		}
		return cnt; 
	}

	public int getLineID(int mX, int mY) {
		// (mX, mY)를 지나는 lineID 반환 
		// 즉, 위에서는 (lineID, 0)부터 시작해서 내려갔다면,
		// 얘는 (mX,mY)부터 시작해서 (??, 0)까지 올라가면 -> 정답을 알수 있겠네요
		
		int x = mX;
		int y = mY; 
		
		while(true) {
			// 언제까지? 우리가 0번 행에 도착할때까지
			// 또는 --> 지금 내가 존재하는 행 위에 더 이상 가로줄이 존재하지 않을때까지
			// 왜? 위에 가로줄이 없으면 지금 열에서 쭈욱 위로 올라가면 되잖아요. 
			
			// 지금 내 행 보다 위에 있는 가로줄 중 가장 아래에 있는것을 만나서 건너가면서 올라가면 된다.
			// -> 더 작은것 중 가장 큰것
			Integer ny = tm[x].lowerKey(y);
			
			if(ny == null)
				break; // 가로줄이 없으면 0까지 쭈욱 가자!
			
			// 건너가긴 해야겠죠
			y = ny;
			x = tm[x].get(ny);
		}
		
		return x; // 지금 내가 마지막까지 있던 열 
	}
}
public class Main {

}
