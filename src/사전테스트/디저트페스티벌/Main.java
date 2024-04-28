package 사전테스트.디저트페스티벌;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {

	static int N;

	public static void main(String[] args) throws IOException {
		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//사전테스트//디저트페스티벌//sample_input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());

		PriorityQueue<Node> jobPQ = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.start, o2.start));

		int maxTime = 0;
		for (int i = 0; i < N; i++) {
			try {
			st = new StringTokenizer(br.readLine(), " ");
			int a = Integer.parseInt(st.nextToken());
			int b = Integer.parseInt(st.nextToken());

			maxTime = Math.max(maxTime, a + b);
			jobPQ.add(new Node(i, a, b));
			} catch (Exception e) {
				System.out.println(i);
				throw e;
			}
		}

		maxTime += 1_000;

		PriorityQueue<Node> waitingPQ = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.duration, o2.duration));
		ArrayDeque<Node> workingQ = new ArrayDeque<>();

		int curTime = 0;
		int maxWaiting = 0;
		while (!(jobPQ.isEmpty() && waitingPQ.isEmpty() && workingQ.isEmpty())) {
			curTime++;
			if (!jobPQ.isEmpty()) {
				Node temp = jobPQ.peek();
				if (temp.start == curTime) {

					Node node = jobPQ.poll();
					if (workingQ.isEmpty()) {
						workingQ.add(new Node(node.id, node.start, node.duration));
					} else {
						waitingPQ.add(new Node(node.id, node.start, node.duration));
					}
				}
			}

			if (!workingQ.isEmpty()) {
				Node node = workingQ.peek();
				int scheduled = node.start + node.duration + node.waiting;

				if (scheduled == curTime) {
					workingQ.poll();

					if (!waitingPQ.isEmpty()) {
						Node waiting = waitingPQ.poll();
						int waitTime = curTime - waiting.start;
						maxWaiting = Math.max(waitTime, maxWaiting);
						workingQ.add(new Node(waiting.id, waiting.start, waiting.duration, waitTime));
					}
				}
			}
		}

		System.out.println(maxWaiting);
		
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}

	static class Node {
		int id;
		int start;
		int duration;
		int waiting;

		public Node(int id, int start, int takenTime) {
			this.id = id;
			this.start = start;
			this.duration = takenTime;
		}

		public Node(int id, int start, int takenTime, int waiting) {
			this.id = id;
			this.start = start;
			this.duration = takenTime;
			this.waiting = waiting;
		}

	}
}
