package 사전테스트.디저트페스티벌._01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;


public class Main {
	
	static int N;
	public static void main(String[] args) throws IOException {
		//Long start = System.currentTimeMillis();
		
		//System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//사전테스트//디저트페스티벌//sample_input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		
		//요청 리스트
		//PriorityQueue<Task> taskPQ = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.start, o2.start));
		Task[] tasks = new Task[N];
		
		// 대기열
		PriorityQueue<Task> waiting = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.id, o2.id));

		int maxTime = 0;
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			int a = Integer.parseInt(st.nextToken());
			int t = Integer.parseInt(st.nextToken());
				
			tasks[i] = new Task(i+1, a, t);
		}
		Arrays.sort(tasks, (o1,o2) -> Integer.compare(o1.start, o2.start));
		
		
		int completed = 0;
		int end = 0;
		int idx = 0;
		int maxWaiting = 0;
		
		// 전체 요청이 처리 될때까지
		while(completed < N) {
			// end 시간을 기준으로 작업 가능한 요청을 Q에 추가
			if(idx < N) {
				while(tasks[idx].start <= end) {
					waiting.add(tasks[idx]);
					idx++;
					if(idx == N) break;
				}
			}
			
			// end 시간 기준 대기중 요청이 없을때 다음 요청을 실행
			if(waiting.isEmpty()) {
				if(idx < N) {
					waiting.add(tasks[idx]);
					idx++;
				}
			}
			
			// 대기 요청중 요청 순서 우선 순위로 처리
			if(!waiting.isEmpty()) {
				Task current = waiting.poll();
				
				maxWaiting = Math.max(maxWaiting, end - current.start);
				// 종료시간 업데이트
				end = Math.max(end + current.duration, current.start + current.duration);
				completed++;		
				
			}
			
		}
		
		System.out.println(maxWaiting);

	}
	
	static class Task {
		int id;
		int start;
		int duration;
		public Task(int id, int start, int duration) {
			this.id = id;
			this.start = start;
			this.duration = duration;
		}
		
	}
}
