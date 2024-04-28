package 연습문제.승강제리그;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

class UserSolution {
	int N, L;
	int[] ability;
	ArrayList<Player>[] data;
	//TreeMap<Integer,Player>[] data;
	
	int numPerLeague, mid;
	
	int notNeed;
	public void init(int N, int L, int[] mAbility) {
		this.N = N;
		this.L = L;
		ability = mAbility;
		
		numPerLeague = N/L;
		mid = numPerLeague / 2;
		
		data = new ArrayList[L];
		for(int i=0; i<L; i++) {
			data[i] = new ArrayList<>();
		}
		
		for(int i=0; i<N; i++) {
			int league = i / numPerLeague;
			data[league].add(new Player(i, ability[i]));
		}
		notNeed = 0;
		sortPlayer(0);
		
	}

	public int move() {		
		
		int sum = 0;
		for(int i=1; i<L-1; i++) {				
			Player p1 = data[i].get(0);
			Player p2 = data[i-1].get(numPerLeague-1);
						
			data[i-1].set(numPerLeague-1, p1);
			data[i].set(0, p2);			
			
			sum += p1.id + p2.id;
		}
		
		Player p1 = data[L-2].get(numPerLeague-1);
		Player p2 = data[L-1].get(0);
		data[L-2].set(numPerLeague-1, p2);
		data[L-1].set(0, p1);
		
		sum += p1.id + p2.id;
		
		sortPlayer(1);
		
		return sum;
	}

	public int trade() {
		
		int sum = 0;
		
		
		for(int i=1; i<L-1; i++) {				
			Player p1 = data[i].get(0);
			Player p2 = data[i-1].get(mid);
						
			data[i-1].set(mid, p1);
			data[i].set(0, p2);			
			
			sum += p1.id + p2.id;
		}
		
		Player p1 = data[L-2].get(mid);
		Player p2 = data[L-1].get(0);
		data[L-2].set(mid, p2);
		data[L-1].set(0, p1);
		
		sum += p1.id + p2.id;
		
		sortPlayer(2);				
		return sum;
	}
	
	private void sortPlayer(int type) {
		// type 0 : init , type 1: move , type2 trade
		
		
		if(type == 0) {
			for(int i=0; i<L; i++) {						
				Collections.sort(data[i], reverseOrdered());				
			}
			return;
		}
		boolean requiredSort = false;		
		for(int i=0; i<L; i++) {
			for(int j=0; j<numPerLeague; j++) {
				if(j==0) {
					if(data[i].get(j).ability <= data[i].get(j+1).ability) { 
						requiredSort = true;
						break;
					}					
				} else if (j==numPerLeague-1) {
					if(data[i].get(j-1).ability <= data[i].get(j).ability) { 
						requiredSort = true;
						break;
					}  					
				} else if (j==mid) {
					if(type == 1) continue;				
					
					if(data[i].get(j-1).ability < data[i].get(j).ability) { 
						requiredSort = true;
						break;
					} 
					if(data[i].get(j).ability <= data[i].get(j+1).ability) { 
						requiredSort = true;
						break;
					} 
				} else {
					continue;
				}				
			}	
			if(requiredSort) {
				Collections.sort(data[i], reverseOrdered());
			}
		}
	}

	private Comparator<? super Player> reverseOrdered() {
		return (o1, o2) -> o1.ability == o2.ability ? Integer.compare(o1.id, o2.id) : Integer.compare(o2.ability, o1.ability);
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