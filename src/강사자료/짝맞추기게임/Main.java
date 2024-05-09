package 강사자료.짝맞추기게임;


import java.io.*;
import java.util.*; 

class UserSolution {
	
	static int ps(int a, int b, int n) {
		// mid = 0번과 i번이 이만큼 차이난다! 
		int left = 0; // 최소 차이 
		int right = n-1; // 최대 차이
		
		while(left <= right) {
			int mid = (left + right) / 2; 
			// mid = 0번과 1번은 이만큼 차이난다! 
			// 이 가설에 대한 증명 => ps => test() 
			// 저희는 test가 이미 구성되어있다 -> 심판에게 물어보는 기능 = checkards
			if(Main.checkCards(a, b, mid)) {
				// a와 b의 차이가 mid 이하라면 : 
				// 저희는 무엇을 찾으려고 하는가? 실제 차이를 찾으려고 한다
				// 점점 적은 차이를 얘기해보면서 실제 찾이를 찾아가자! 
				right = mid - 1; 
			}
			else {
				// 내가 말한 mid 차이보다 크다면 
				// 너무 작은 차이를 말했다!
				left = mid + 1; 
			}
		}
		// a와 b 카드의 정확한 차이 => true일떄 옮겼던 포인터 말고 다른거
		return left; 
	}
  
	public void playGame(int N) {
      	// 두 카드의 숫자를 비교하기 위해 아래와 같이 checkCards 함수를 사용합니다.
    	// Main.checkCards(indexA, indexB, diff)
		
		// index : 0번을 기준으로 x만큼 차이가 난다! (차이 = 1~N-1까지의 차이)
		// value : x만큼의 차이가 나는 카드의 index 
		// --> arraylist 로 만드는 이유 : 최대 4개, 2개, 
		ArrayList<Integer>[]dat = new ArrayList[N];  
		for(int i = 0; i < N; i++)
			dat[i] = new ArrayList<>(); 
		
		// 시간복잡도 : O(2N) x O(log(5만)) x O(1)
		// i번 카드와 0번 카드의 차이를 찾아볼겁니다.
		for(int i = 1; i < 2 * N; i++) {
			// 0번과 i번 카드의 "정확한 차이"를 찾아낼겁니다
			// 생각 (설계) : 0번과 i번은 1만큼 차이난다... 2만큼 차이난다..
			// 이런것을 가정해보면서 경우의수를 돌리는 과정 -> parametric search
			int diff = ps(0, i, N); 
			// int de = 1; 
			
			// 0번과 i번은 이만큼 차이난다!
			// i번은 이만큼 차이나는 곳에 저장
			dat[diff].add(i); 
		}
		
		// int de = 1; 
		
		// 이제 뭐랑 뭐랑 같은지를 매칭만 시켜주면 되겠죠. 
		// 한번씩 [차이만큼] 다 돌려보면서 매칭
		// O(N)
		for(int i = 0; i < N; i++) {
			// 경우 
			// 없는 경우 = 신경 안써도 됨
			
			// 1개 들어있는 경우 = 0만큼 차이난다.
			// 이미 parameteric search 과정에서 Main.checkCards(x, y, 0) 이미 해봄
			// 신경 안써도 됨
			
			// 2개가 들어있는경우 
			if(dat[i].size() == 2) {
				// 그냥 들어있는 두개는 무조건 같은 카드다 
				Main.checkCards(dat[i].get(0), dat[i].get(1), 0); 
			}
			
			// 4개가 들어있는 경우
			else if(dat[i].size() == 4) {
				Main.checkCards(dat[i].get(0), dat[i].get(1), 0); 
				Main.checkCards(dat[i].get(2), dat[i].get(3), 0); 
				
				Main.checkCards(dat[i].get(0), dat[i].get(2), 0); 
				Main.checkCards(dat[i].get(1), dat[i].get(3), 0); 
				
				Main.checkCards(dat[i].get(0), dat[i].get(3), 0); 
				Main.checkCards(dat[i].get(1), dat[i].get(2), 0); 
			}
		}
		
		return; 
	}
}
public class Main {

}
