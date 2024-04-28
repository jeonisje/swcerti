package 연습문제.승강제리그._3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

class UserSolution {
	int N, L;
	int[] ability;
	Player[] players;
	
	TreeMap<Player, Integer>[] lower;	// 하위권    - 내림차순
	TreeMap<Player, Integer>[] higher;	// 상위권	   - 오름차순
	
	int numPerLeague;
	 
	public void init(int N, int L, int[] mAbility) {
		this.N = N;
		this.L = L;
		ability = mAbility;
		
		numPerLeague = N/L;
		
		lower = new TreeMap[L];
		higher = new TreeMap[L];
		players = new Player[N];
		
		for(int i=0; i<L; i++) {
			higher[i] = new TreeMap<>((o1, o2) -> o1.ability == o2.ability ? Integer.compare(o2.id, o1.id) : Integer.compare(o1.ability, o2.ability));
			lower[i] = new TreeMap<>((o1, o2) -> o1.ability == o2.ability ? Integer.compare(o1.id, o2.id) : Integer.compare(o2.ability, o1.ability));
		}
		
		for(int i=0; i<N; i++) {
			int group = i / numPerLeague;
			Player p1 = new Player(i, ability[i]);
			
			players[i] = p1;
			if(i % numPerLeague  == 0) {
				lower[group].put(p1, i);
			} else {
				int next = i+1;
				Player p2 = new Player(next, ability[next]);
				
				players[next] = p2;
				
				lower[group].put(p1, i);
				higher[group].put(p2, next);
				
				int lowerAbility = lower[group].firstKey().ability;
				int higherAility = higher[group].firstKey().ability;
				
				if(lowerAbility > higherAility) {
					p1 = lower[group].pollFirstEntry().getKey();
					p2 = higher[group].pollFirstEntry().getKey();
					
					lower[group].put(p2, p2.id);
					higher[group].put(p1, p1.id);					
				}
				i++;
			}
		}
		
		//print(higher[0]);
			
	}
	
	private void print(TreeMap<Player, Integer> map) {
		
		System.out.println("------------------------------------");
		
		for( Map.Entry<Player, Integer> entry : map.entrySet() ){
			Player p = entry.getKey();
			
		    System.out.println( "id :" + p.id +", ability : "+ p.ability );
		}
		
	}
	
	private void median() {
		for(int group=0; group<L; group++) {
			
			int lowerAbility = lower[group].firstKey().ability;
			int higherAility = higher[group].firstKey().ability;
			
			if(lowerAbility > higherAility) {
				Player p1 = lower[group].pollFirstEntry().getKey();
				Player p2 = higher[group].pollFirstEntry().getKey();
				
				lower[group].put(p2, p2.id);
				higher[group].put(p1, p1.id);					
			}
		}
//		
		for(int i=0; i<L; i++) {
			System.out.println();
			System.out.println("lower : " + i);
			print(lower[i]);
			System.out.println();
			System.out.println("higher : " + i);
			print(higher[i]);
		}
	} 

	public int move() {		
		int sum = 0;
		
		Player topLeagueLowest = lower[0].pollLastEntry().getKey();
		Player secLeagueTop = higher[1].pollLastEntry().getKey();
		
		for(int i=1; i<L-1; i++) {
			Player topLeaguer = lower[i].pollLastEntry().getKey();
			Player lowLeaguer = higher[i+1].pollLastEntry().getKey();
			
			higher[i+1].put(topLeaguer, topLeaguer.id);
			lower[i].put(lowLeaguer, lowLeaguer.id);
			
			sum += topLeaguer.id + lowLeaguer.id;
		}
		lower[0].put(secLeagueTop, secLeagueTop.id);
		higher[1].put(topLeagueLowest, topLeagueLowest.id);
		sum += secLeagueTop.id + topLeagueLowest.id;
		
		median();
		return sum;
	}

	public int trade() {
		int sum = 0;
		
		Player topLeagueMid = lower[0].pollFirstEntry().getKey();
		Player secLeagueTop = higher[1].pollLastEntry().getKey();
		
		for(int i=1; i<L-1; i++) {
			Player topLeaguer = lower[i].pollFirstEntry().getKey();
			Player lowLeaguer = higher[i+1].pollLastEntry().getKey();
			
			higher[i+1].put(topLeaguer, topLeaguer.id);
			lower[i].put(lowLeaguer, lowLeaguer.id);
			
			sum += topLeaguer.id + lowLeaguer.id;
		}
		lower[0].put(secLeagueTop, secLeagueTop.id);
		higher[1].put(topLeagueMid, topLeagueMid.id);
		sum += secLeagueTop.id + topLeagueMid.id;
		
		median();
		return sum;
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
//		if(ans!=ret) System.err.println("===================오류=======================");
//		System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
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
		
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//연습문제//승강제리그//sample_input2.txt"));
		
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