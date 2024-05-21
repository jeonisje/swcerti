package 프로원정대.day5.국가행정.강사자료;


import java.io.*;
import java.util.*;

class UserSolution {
	
	class Road implements Comparable <Road>{
		int time; // 이동 시간
		int id; // 고유 번호
		Road(int time, int id) {
			this.time = time;
			this.id = id; 
		}
		@Override
		public int compareTo(Road o) {
			// 시간 긴거 우선
			if(this.time > o.time) return -1;
			if(this.time < o.time) return 1;  
			
			// id는 작은거 우선 
			if(this.id < o.id) return -1;
			if(this.id > o.id) return 1;
			return 0; 
		}
	}

	// index : 도로의 번호 (0~N-2)
	// value : 이 도로에는 차선이 지금 몇개 있는가? 
	int[] roadCnt; 
	
	// calcuation 용 => for문으로 모든 도로의 합을 구해볼겁니다.
	// index : 도로 번호
	// value : 이 도로의 이동 시간 
	int[] roadTime; 
	
	// expand용 -> 확장할떄 (pop[id] + pop[id+1]) / roadCnt[id]
	// divide용으로도 사용
	// index : 도시 번호 (0~N-1)
	// value : 이 도시에 사는 인구 수 ( 변동 없음 )
	int[] pop;  
	
	PriorityQueue<Road>pq;
	
	public void init(int N, int[] mPopulation) {
		pop = mPopulation; 
		roadCnt = new int[N];
		roadTime = new int[N];
		pq = new PriorityQueue<>();
		
		// init에서 무언가 있으니까 => 초기 세팅
		for(int i = 0; i < N-1; i++) {
			roadCnt[i] = 1; // i번 도로에는 처음에는 차선이 1개 있다!
			// i번 도로의 이동 시간 = i번 도시의 인구수 + i+1번 도시의 인구수
			roadTime[i] = pop[i] + pop[i+1]; 
			pq.add(new Road(roadTime[i], i));
		}
		int de = 1;
	    return;
	}
	 
	public int expand(int M) {
		// expand= priority queue 사용해서 가장 높은 우선순위를 가진 도로 뺴와서 갱신 재삽입
	    // M번 확장
		int res = 0; 
		for(int i = 0; i < M; i++) {
			// 일단 가장 높은 우선순위를 가지는 도로를 가져옵니다.
			Road now = pq.remove();
			// 이 도로에 차선이 하나 더 생긴다! 
			roadCnt[now.id]++; 
			// 새로운 비용을 확인
			int newTime = (pop[now.id] + pop[now.id+1]) / roadCnt[now.id];
			roadTime[now.id] = newTime; 
			pq.add(new Road(newTime, now.id));
			res = newTime;
		}
		return res;
	}
	 
	public int calculate(int mFrom, int mTo) {
		// mFrom -> mTo까지를 그냥 for문으로 합하면 2,500만번의 연산수로 해결
		int sum = 0; 
		int from = Math.min(mFrom, mTo);
		int to = Math.max(mFrom, mTo); 
	
		for(int i = from; i < to; i++) 
			sum += roadTime[i];
	    return sum;
	}
	 
	public int divide(int mFrom, int mTo, int K) {
		// parametric search :
		// mFrom -> mTo 도시를 K개의 선거구로 나눌때,
		// 한 구역의 최대 인구수가 최소가 되도록 만들겁니다.
		// mid = 인구수 (찾으려고 하는건 인구 수)
		int left = 1; // 한 선거구에 들어갈수 있는 최소 인구 수 
		int right = 10000000; // 한 선거구에 들어갈 수 있는 최대 인구 수 (K = 1, mFrom = 0, mTo = N-1)
		
		while(left <= right) {
			int mid = (left + right) / 2;
			if(test(mid, mFrom, mTo, K)) {
				// mFrom -> mTo까지 K개의 선거구로 나누었을때, 최대 인구수 = mid가 된다!
				// 성공 -> 가능성을 찾았다!
				// 목표 : mid가 최소가 되는것이 목표
				// --> 더 적은 mid로 K개의 선거구를 나눌수 있는지 확인
				right = mid - 1; 
			}
			else {
				// 실패 -> 너무 많은 선거구가 만들어졌다.
				// 너무 인구수를 작게 잡았다.
				left = mid + 1;
			}
		}
	    return left;
	}
	public boolean test(int mid, int from, int to, int K) {
		// 각 선거구는 이제 mid 명이 limit 
		// 순차적으로 저희가 각 도시의 인구수를 취합해보면서 몇개의 선거구가 만들어지는지를 확인
		int cnt = 1; // 최소
		// 이 선거구의 인구수 
		int sum = 0; 
		for(int i = from; i <= to; i++) {
			// 이 i번 도시의 인구를 지금 선거구에 포함시킬수 있는가?
			sum += pop[i];
			// mid = limit
			// 즉, mid를 초과하는 인구수가 된다면,
			// 지금 이 i번 구역 => 다음 선거구에 포함되어야 한다.
			if(sum > mid) {
				sum = pop[i]; // 이 지역이 처음 포함되어 있다.
				cnt++; // cnt+1번 선거구
			}
			
			// 선거구가 너무 많아졌다 -> 인구수를 너무 적게 잡았다는것
			// --> fail을 시켜줍니다.
			if(cnt > K)
				return false;
		}
		// K개 이하의 선거구가 생성되었다? -> 가능성을 보았다.
		return true;
	}
}

public class Main {

}
