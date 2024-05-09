package 강사자료.주차장;


import java.io.*;
import java.util.*; 

class UserSolution {
	
	static class Car implements Comparable <Car>{
		// 총 대기 시간 - 총 주차 시간
		int timeDiff; 
		// 입차 시간 
		int inTime;
		// 차량 번호 (우리가 부여한 carID) 
		int id; 
		
		Car(int timeDiff, int inTime, int id) {
			this.timeDiff = timeDiff;
			this.inTime = inTime; 
			this.id = id; 
		}
		
		@Override 
		public int compareTo(Car o) {
			// 총 대기 시간 - 총 주차 시간이 큰게 우선
			if(timeDiff > o.timeDiff) return -1;
			if(timeDiff < o.timeDiff) return 1;
			
			// 들어온 시간이 빠른게 먼저
			if(inTime < o.inTime) return -1;
			if(inTime > o.inTime) return 1;
			
			if(id < o.id) return -1;
			if(id > o.id) return 1; 
			
			return 0;
		}
	}
	
	// Key : 실제 차량 번호
	// value : 우리가 부여한 carID;
	static HashMap<Integer, Integer> hm;
	static int carID; 
	
	// index : carID
	// value : 상태 
	// 1 = 주차중, 0 = 대기중, -1 = 둘다 아닌 상태
	static int[] state;
	
	// 총 주차 시간 
	static int[] totalParkTime;
	// 총 대기 시간
	static int[] totalWaitTime; 
	
	static int parkCnt; 
	static int waitCnt; 
	
	// 차가 들어온 시간 
	static int[] inTime; 
	
	static PriorityQueue<Car>pq; 
	
	static int bt; // base time 
	static int bf; // base fee
	static int ut; // unit time
	static int uf; // unit fee
	static int capacity; // 주차장 용량
	
	public void init(int mBaseTime, int mBaseFee, int mUnitTime, int mUnitFee, int mCapacity) {
		bt = mBaseTime;
		bf = mBaseFee;
		ut = mUnitTime;
		uf = mUnitFee;
		capacity = mCapacity; 
		
		hm = new HashMap<>();
		carID = 1; 
		pq = new PriorityQueue<>();
		
		// DAT = arrive의 호출횟수 = 70,000 
		state = new int[70001];
		totalParkTime = new int[70001];
		totalWaitTime = new int[70001];
		inTime = new int[70001];
		
		parkCnt = 0;
		waitCnt = 0; 
		return;
	}

	public int arrive(int mTime, int mCar) {
		// #1. 등록 -> 처음 보는 차량 번호 => 일단 등록 
		if(hm.get(mCar) == null) {
			// 등록!
			hm.put(mCar, carID);
			state[carID] = -1;
			// 이 차량은 이 시간에 들어온다.  
			// inTime[carID] = mTime; // 무조건 들어감 --> 새로 들어오는 차량만 등록되는군  
			carID++; 
		}
		// 밑에서 state 주차 or 대기
		int cID = hm.get(mCar);
		inTime[cID] = mTime; 
		
		// #2. 주차를 할 수 있는가? 
		if(parkCnt < capacity) {
			// 이 차량은 주차를 한다!
			state[cID] = 1; 
			// 하나의 차량을 더 주차했다!
			parkCnt++; 
		}
		else {
			// 대기줄로 가야 합니다.
			state[cID] = 0;
			// 하나의 차량이 더 대기줄에 서있다! 
			waitCnt++; 
			// 대기줄에선 leave API에서는 우선순위대로 뺴와야 하니까
			// 대기줄에 삽입 => pq삽입
			// 저희는 mTime에 대한 영향이 없이 (twt - tpt) 
			int diff = totalWaitTime[cID] - totalParkTime[cID] - mTime;
			pq.add(new Car(diff, mTime, cID)); 
		}
		return waitCnt;
	}

	public int leave(int mTime, int mCar) {
		// mCar = 지금 대기중 또는 주차중인 차량임이 보장 
		// 얘가 지금 어떤 상태냐에 따라서 -> 행동이 바뀜
		int cID = hm.get(mCar); 
		
		// 이 차량의 상태를 확인
		if(state[cID] == 1) {
			// 주차중이였다면 
			state[cID] = -1; // idle상태
			parkCnt--; // 주차하던 차량 하나 나감
			totalParkTime[cID] += mTime - inTime[cID]; 
			
			// 여기에서 자리가 하나 났으니까, 가장 우선순위가 높은 차량 하나를 입차 시킵니다. 
			while(!pq.isEmpty()) {
				Car now = pq.remove(); // 가장 지금 우선순위가 높은 차량 
				
				// 검증 : 이 now라는 차가 실제로 존재하는 차냐? 
				// #1. 대기중이다가 (pq안에 있을텐데) -> 나갔다 (지금은 없는것)
				// 없어진 차량이라면
				if(state[now.id] != 0)
					continue; // 실존하지 않는 과거의 차
				
				// #2. 대기중이다가 (tw-tp,입차) -> 나갔다가 -> 다시 들어왔음 (tw-tp,입차)
				if(now.inTime != inTime[now.id])
					continue; // 실존하지 않는 과거의 차
				
				//int de =1; 
				// System.out.println(now.id); 
				
				// 이 차는 이제 주차한다! 
				state[now.id] = 1; // 주차상태
				waitCnt--; // 대기중인 차량 하나 줄어들고
				parkCnt++; // 주차중인 차량이 하나 늘어나느거
				// 새로 들어오는 시각 갱신 -> 대기 -> 주차로 들어가는 시간 갱신
				inTime[now.id] = mTime;  
				// 지금 들어가는 차량 = 총 대기시간 누적
				totalWaitTime[now.id] += mTime - now.inTime; 
				// 딱 하나만 주차하니까
				break; 
			}
			
			// 주차한 차 나갔고, 대기중인 차 들어왔다.
			// 정산
			// 주차 시간 기본 시간 이하라면 = 기본시간만
			int parkTime = mTime - inTime[cID];
			if(parkTime <= bt)
				return bf; 
			return bf + (int)Math.ceil((double)(parkTime - bt) / ut) * uf; 
		}
		else if(state[cID] == 0){
			// 대기중이였다면 -> 주차하는게 아니라 그냥 사라지는것. 
			state[cID] = -1; // idle 상태
			waitCnt--; // 기다리던 차량이 하나 줄었다! 
			
			// 이제 대기하던 시간이 있었을거니까->총 대기시간 누적
			totalWaitTime[cID] += mTime - inTime[cID]; 
			// pq.remove(new Car(_,_,_));  
			
			// 저희는 일단 삭제는 나중에 한번에 할거고, 
			return -1; 
		}
		return 0;
	}
}
public class Main {

}
