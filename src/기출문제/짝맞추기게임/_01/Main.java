package 기출문제.짝맞추기게임._01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;



class UserSolution {
	public void playGame(int N) {
		ArrayList<Integer>[] listOfDiff = new ArrayList[N];
		for(int i=0; i<N; i++) {
			listOfDiff[i] = new ArrayList<>();
		}
		
		listOfDiff[0].add(0);
		
		for(int i=1; i<N*2; i++) {
			int left = 0;
			int right = N-1;
			int diff = 0;
			while(left <= right) {
				int mid = (left + right) / 2;
				if(Main.checkCards(0, i, mid)) {
					diff = mid;
					right = mid - 1;
				} else {
					left = mid + 1;
				}				
			} 
			listOfDiff[diff].add(i);
		}
		
		for(int diff=0; diff<N; diff++) {
			if(listOfDiff[diff].size() == 0) continue;
			if(listOfDiff[diff].size() == 2) {
				Main.checkCards(listOfDiff[diff].get(0), listOfDiff[diff].get(1), 0);
			} else {
				if(Main.checkCards(listOfDiff[diff].get(0), listOfDiff[diff].get(1), 0)) {
					Main.checkCards(listOfDiff[diff].get(2), listOfDiff[diff].get(3), 0);
				} else if(Main.checkCards(listOfDiff[diff].get(0), listOfDiff[diff].get(2), 0)) {
					Main.checkCards(listOfDiff[diff].get(1), listOfDiff[diff].get(3), 0);
				} else if(Main.checkCards(listOfDiff[diff].get(0), listOfDiff[diff].get(3), 0)) {
					Main.checkCards(listOfDiff[diff].get(1), listOfDiff[diff].get(2), 0);
				}
			}

		}
		
		return; 
	}
	
}

public class Main {
	private final static UserSolution usersolution = new UserSolution();
	private static BufferedReader br;

	private final static int MAX_N = 50000;

	private static int N;
	private static int[] cards = new int[MAX_N * 2];
	private static boolean[] found = new boolean[MAX_N + 1];
	private static int foundCnt;
	private static boolean isCorrect;

	public static boolean checkCards(int indexA, int indexB, int diff) {
		if (!isCorrect || indexA < 0 || indexA >= N * 2 || indexB < 0 || indexB >= N * 2) {
			isCorrect = false;
			return false;
		}
		if (Math.abs(cards[indexA] - cards[indexB]) > diff) {
			return false;
		}
		int val = cards[indexA];
		if (diff == 0 && indexA != indexB && !found[val]) {
			foundCnt++;
			found[val] = true;
		}
		return true;
	}

	public static void init() {
		foundCnt = 0;
		isCorrect = true;
		for (int i = 1; i <= N; i++)
			found[i] = false;
	}

	private static boolean run() throws Exception {
		StringTokenizer st;

		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N * 2; i++)
			cards[i] = Integer.parseInt(st.nextToken());

		init();
		usersolution.playGame(N);
		return isCorrect && foundCnt == N;
	}

	public static void main(String[] args) throws Exception {
		Long start = System.currentTimeMillis();
		
		int TC, MARK;		

		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//기출문제//짝맞추기게임//sample_input.txt"));
		
		br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}