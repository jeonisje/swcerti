package 연습문제.승강제리그._2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	int N, L;
	int[] ability;
	PriorityQueue<Player>[] pq;
	//PriorityQueue<Player>[] reversedPQ;	
	
	int numPerLeague;

	public void init(int N, int L, int[] mAbility) {
		this.N = N;
		this.L = L;
		ability = mAbility;
		
		numPerLeague = N/L;
		
		pq = new PriorityQueue[L];
		//reversedPQ = new PriorityQueue[L];
		for(int i=0; i<L; i++) {
			pq[i] = new PriorityQueue<>(ordered());
			//reversedPQ[i] = new PriorityQueue<>((o1, o2) -> o1.ability == o2.ability ? Integer.compare(o1.id, o2.id) : Integer.compare(o2.ability, o1.ability));
		}
		
		for(int i=0; i<N; i++) {
			int idx = i / numPerLeague;
			pq[idx].add(new Player(i, ability[i]));
			//reversedPQ[idx].add(new Player(i, ability[i]));
		}		
	}


	private Comparator<? super Player> ordered() {
		return (o1, o2) -> o1.ability == o2.ability ? Integer.compare(o2.id, o1.id) : Integer.compare(o1.ability, o2.ability);
	}
	

	public int move() {		
		Player[] tempTopAbility = new Player[L];
		Player[] tempBottomAbility = new Player[L];
		
		for(int i=0; i<L; i++) {						 
			tempBottomAbility[i] = pq[i].poll();			
		}
		
		for(int i=0; i<L; i++) {						 
			PriorityQueue<Player> reversedPQ = new PriorityQueue<>(orderedReverse());
			reversedPQ.addAll(pq[i]);
			
			tempTopAbility[i] = reversedPQ.poll();
			
			pq[i] = new PriorityQueue<>(ordered());
			pq[i].addAll(reversedPQ);
		}
			
		int sum = tempBottomAbility[0].id + tempTopAbility[L-1].id;
		pq[0].add(tempTopAbility[0]);	
		pq[L-1].add(tempBottomAbility[L-1]);
				
		for(int i=1; i<L-1; i++) {
			pq[i-1].add(tempTopAbility[i]);
			pq[i].add(tempBottomAbility[i-1]);
			pq[i].add(tempTopAbility[i+1]);
			pq[i+1].add(tempBottomAbility[i]);
			
			sum+= tempTopAbility[i].id + tempBottomAbility[i].id;
		}		
		
		return sum;
	}
	

	
	public int trade() {	
		int mid = numPerLeague / 2;
		
		ArrayList<Player>[] leagues = new ArrayList[L];
		
		for(int i=0; i<L; i++) {
			leagues[i] = new ArrayList<>();
			leagues[i].addAll(pq[i]);
			Collections.sort(leagues[i], orderedReverse());
		}
		
		//int sum = leagues[0].get(mid).id + leagues[L-1].get(0).id;
		
		int sum = 0;
		for(int i=1; i<L-1; i++) {
			Player topMidLeaguer = leagues[i-1].get(mid);
			Player curTopLeaguer = leagues[i].get(0);
			Player curMidLeaguer = leagues[i].get(mid);
			Player lowerTopLeaguer = leagues[i+1].get(0);
			
			sum += topMidLeaguer.id + curTopLeaguer.id + curMidLeaguer.id + lowerTopLeaguer.id;
			
			leagues[i-1].set(mid, curTopLeaguer);
			leagues[i].set(0, topMidLeaguer);
			leagues[i].set(mid, lowerTopLeaguer);
			leagues[i+1].set(mid, curMidLeaguer);			
		}
		
		
		
		return sum;
	}


	private Comparator<? super Player> orderedReverse() {
		return (o1, o2) -> o1.ability == o2.ability ? Integer.compare(o1.id, o2.id) : Integer.compare(o2.ability, o1.ability);
	}
	
	public int trade(int idx) {
		int mid = numPerLeague / 2;
		
		ArrayList<Player> list = new ArrayList<>();
		list.addAll(pq[idx]);
		Collections.sort(list, orderedReverse());
				
		
		
		// 하위리그
		Player p1 = list.get(mid);
		
		// 상위리그
		Player p2 = pq[idx-1].poll();
		
		list.set(mid, p2);
		pq[idx-1].add(p1);
		
		
		
		return 0;
	}
	
	
	class Player{
		int id;
		int ability;
		Player(int id, int ability) {
			this.id = id;
			this.ability = ability;
		}
	}
}

public class Main {
	private static final int CMD_INIT = 100;
	private static final int CMD_MOVE = 200;
	private static final int CMD_TRADE = 300;

	private static UserSolution userSolution = new UserSolution();
	private final static int MAX_N = 39990;

	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer st;
	
	private static void print(int num, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("===================오류=======================");
		//System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
	}

	public static boolean run(BufferedReader br) throws IOException {
		int Q = Integer.parseInt(br.readLine());
		int ans, ret;
		boolean okay = false;

		for (int q = 0; q < Q; q++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
			case CMD_INIT:
				int N = Integer.parseInt(st.nextToken());
				int L = Integer.parseInt(st.nextToken());
				int[] mAbility = new int[MAX_N];
				for (int i = 0; i < N; i++)
					mAbility[i] = Integer.parseInt(st.nextToken());
				userSolution.init(N, L, mAbility);
                okay = true; 
				break;
			case CMD_MOVE:
				ret = userSolution.move();
				ans = Integer.parseInt(st.nextToken());
				print(q, "move", ans, ret);
				if (ret != ans)
					okay = false;
				break;
			case CMD_TRADE:
				ret = userSolution.trade();
				ans = Integer.parseInt(st.nextToken());
				print(q, "trade", ans, ret);
				if (ret != ans)
					okay = false;
				break;
			default:
				okay = false;
				break;
			}
		}
		return okay;
	}

	public static void main(String[] args) throws IOException {
		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//연습문제//승강제리그//sample_input.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		
		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int tc = 1; tc <= TC; ++tc) {
			boolean result = run(br);
			int score = result ? MARK : 0;
			System.out.println("#" + tc + " " + score);
		}
		
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}