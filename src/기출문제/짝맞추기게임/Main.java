package 기출문제.짝맞추기게임;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

class UserSolution {
	int N;
	
	int[] indexA;
	int[] indexB;
	int[] visited;
	int foundCount;
	  
	public void playGame(int N) {
		this.N = N;
		
		indexA = new int[N];
		indexB = new int[N];
		visited = new int[N*2];
		foundCount = 0;
		
		int start = 0;
		while(foundCount != N) {
			if(visited[start] == 1) {
				start++;
				continue;
			}
			search1(start);
			start++;
		}
		
		Main.init();
		
		for(int i=0; i<N; i++) {
			Main.checkCards(indexA[i], indexB[i], 0);
		}
		
		
      	// 두 카드의 숫자를 비교하기 위해 아래와 같이 checkCards 함수를 사용합니다.
    	// Main.checkCards(indexA, indexB, diff);
		return; 
	}
	
	void search1(int start) {		
		visited[start] = 1;
		
		for(int i=start+1; i<N*2; i++) {
			if(visited[i] == 1) continue;
			boolean result = Main.checkCards(start, i, 0);
			
			
			//System.out.println("indexA : " + start + ", indexB : " + i + " => result :" + result);
			if(result) {
				indexA[foundCount] = start;
				indexB[foundCount] = i;
				foundCount++;
				visited[i] = 1;
				return;
			}
		}
	}
	
	void search2(int start) {		
		visited[start] = 1;
		
		for(int i=start-1; i>0; i--) {
			if(visited[i] == 1) continue;
			
			if(checkResult(start,  i)) return;
			
		}
	}
	
	boolean checkResult(int idxA, int idxB) {
		boolean result = Main.checkCards(idxA, idxB, 0);		
		
		//System.out.println("indexA : " + start + ", indexB : " + i + " => result :" + result);
		if(result) {
			indexA[foundCount] = idxA;
			indexB[foundCount] = idxB;
			foundCount++;
			visited[idxB] = 1;
		}
		
		return result;
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