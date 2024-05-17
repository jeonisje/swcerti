package 기출문제.국가행정;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

class UserSolution {
	int N;
	City[] cityInfo;
	TreeMap<City, Integer> treeMap;	
 
	public void init(int N, int[] mPopulation) {
		this.N = N;
		cityInfo = new City[N];
		treeMap = new TreeMap<>((o1, o2) -> o1.moveTime == o2.moveTime ? Integer.compare(o1.id, o2.id) : Integer.compare(o1.moveTime, o2.moveTime));
		
		for(int i=0; i<N; i++) {
			City city = new City(i, mPopulation[i], mPopulation[i]);
			treeMap.put(city, mPopulation[i]);
			cityInfo[i] = city;
		}
		
	    return;
	}
	 
	public int expand(int M) {
		int count = 0;
		int sum = 0;
		ArrayList<City> target = new ArrayList<>();
		for(Map.Entry<City, Integer> entry : treeMap.entrySet()) {
			
			
			
			if(count == M) break;
		}
		
		
	    return 0;
	}
	 
	public int calculate(int mFrom, int mTo) {
	    return 0;
	}
	 
	public int divide(int mFrom, int mTo, int K) {
	    return 0;
	}
	
	class City {
		int id;
		int population;
		int moveTime;
		public City(int id, int population, int moveTime) {		
			this.id = id;
			this.population = population;
			this.moveTime = moveTime;
		}
	}
}


public class Main {
	private static final int CMD_INIT 		= 100;
	private static final int CMD_EXPAND 	= 200;
	private static final int CMD_CALCULATE 	= 300;
	private static final int CMD_DIVIDE		= 400;
	
	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	private static boolean run(BufferedReader br) throws Exception {

		int[] population = new int[10000];
		int cmd, ans, ret;
		int Q = 0;
		int N, from, to, num; 
		boolean okay = false; 
		
		st = new StringTokenizer(br.readLine(), " ");
		Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				st = new StringTokenizer(br.readLine(), " ");
				for(int i = 0; i < N; i++)
					population[i] = Integer.parseInt(st.nextToken());
				userSolution.init(N, population);
                okay = true; 
				break;
				
			case CMD_EXPAND:
				num = Integer.parseInt(st.nextToken());
				ret = userSolution.expand(num); 
				ans = Integer.parseInt(st.nextToken());
				print(q, "expand", ans, ret, num);
				if(ret != ans)
					okay = false; 
				break;
				
			case CMD_CALCULATE:
				from = Integer.parseInt(st.nextToken());
				to = Integer.parseInt(st.nextToken());
				ret = userSolution.calculate(from, to);  
				ans = Integer.parseInt(st.nextToken());
				print(q, "expand", ans, ret, from, to);
				if(ret != ans)
					okay = false; 
				break;
				
			case CMD_DIVIDE:
				from = Integer.parseInt(st.nextToken());
				to = Integer.parseInt(st.nextToken());
				num = Integer.parseInt(st.nextToken());
				ret = userSolution.divide(from, to, num);  
				ans = Integer.parseInt(st.nextToken());
				print(q, "expand", ans, ret, from, to, num);
				if(ret != ans)
					okay = false; 
				break;

			default:
				okay = false;
				break;
			}
		}
		return okay;
	}
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		if(ans!=ret)  System.err.println("----------------------오류--------------------");
		System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\국가행정\\sample_input2.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}