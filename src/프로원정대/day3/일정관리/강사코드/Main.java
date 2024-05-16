package 프로원정대.day3.일정관리.강사코드;

import java.io.*;
import java.util.*;

// 일정관리
// -> treemap을 사용하는 유형의 문제중의 대표적인 유형 
// scheduling
// -> 예약 스케쥴링
// -> 메모리 / 메모장 스케쥴링

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    
	public static void main(String[] args) throws IOException {
		// 일정 관리를 위한 TreeMap
		// key : 등록된 강의의 시작시간
		// value : 등록된 강의의 종료시간
		TreeMap<Integer, Integer>schedule = new TreeMap<>(); 
		
		int Q = Integer.parseInt(br.readLine());
		// Q개의 강의 일정을 입력 받습니다.
		for(int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			int start = Integer.parseInt(st.nextToken());
			int end = Integer.parseInt(st.nextToken());
			
			// start를 기반으로 저장을 하니까,
			// 지금 이 강의의 바로 전 강의
			Integer prev = schedule.floorKey(start); 
			
			// 지금 이 강의의 바로 다음 강의
			Integer next = schedule.ceilingKey(start);
			
			// 겹치는가?
			// prev = 전 강의의 시작 시간 -> 이 강의의 종료 시간이 start랑 겹치는가?
			// 전 강의의 종료 시간이 start보다 늦게 끝나면
			if(prev != null && schedule.get(prev) >= start)
				continue; // 등록할 수 없는 스케쥴
			
			// next = 다음 강의의 시작 시간 
			// 다음 강의의 시작 시간이 지금 강의의 종료 시간 보다 이른가? 
			if(next != null && next <= end)
				continue; // 등록할 수 없는 스케쥴
			
			// 이 강의는 이제 등록할 수 있는 스케쥴이다!
			schedule.put(start, end); 
		}
		System.out.println(schedule.size());
	}
}