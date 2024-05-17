package 프로원정대.day3.정중앙대학교;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	PriorityQueue<Integer> maxQ;
	PriorityQueue<Integer> minQ;
	int median;
	
	public void init() 	{		
		minQ = new PriorityQueue<>(Collections.reverseOrder());
		maxQ = new PriorityQueue<>();		
		maxQ.add(500);
	}
	
	public void enroll(int A, int B) {		
		minQ.add(A);
		maxQ.add(B);
		
		if(minQ.peek() > maxQ.peek()) {
			minQ.add(maxQ.remove());
			maxQ.add(minQ.remove());
		}		
	} 

	public int getMidValue() {
		return maxQ.peek(); 
	}
}


public class Main {

	private static boolean run(BufferedReader br) throws IOException {

		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		int Q = Integer.parseInt(st.nextToken());

		usersolution.init();

		boolean isCorrect = true; 
		
		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			int A = Integer.parseInt(st.nextToken());
			int B = Integer.parseInt(st.nextToken());
			usersolution.enroll(A, B); 
			int userAns = usersolution.getMidValue(); 
			int ans = Integer.parseInt(st.nextToken());
			if(userAns != ans)
				isCorrect = false; 
		}
		return isCorrect;
	}

	private final static UserSolution usersolution = new UserSolution();

	public static void main(String[] args) throws Exception {

		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대\\day3\\정중앙대학교\\sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; testcase++) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
	}
}