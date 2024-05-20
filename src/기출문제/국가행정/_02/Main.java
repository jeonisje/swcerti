package 기출문제.국가행정._02;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	int N;
	City[] cityInfo;
	PriorityQueue<City> pq;	
	int[] populationByCity;
	
	public void init(int N, int[] mPopulation) {
		this.N = N;
		cityInfo = new City[N];
		populationByCity = new int[N];
		pq = new PriorityQueue<>((o1, o2) -> o1.moveTime == o2.moveTime ? Integer.compare(o1.id, o2.id) : Integer.compare(o2.moveTime, o1.moveTime));
		
		for(int i=0; i<N; i++) {
			City city = new City(i, mPopulation[i], mPopulation[i]);
			cityInfo[i] = city;
			populationByCity[i] = mPopulation[i];
			if(i==0) continue;
			cityInfo[i-1].moveTime += mPopulation[i];
			pq.add(cityInfo[i-1]);
		}
		
	    return;
	}
	 
	public int expand(int M) {		
		int count = 0;	
		int newMoveTime = 0;
		while(!pq.isEmpty()) {
			City city = pq.remove();			
			city.lineCnt++;
			int population = city.population + cityInfo[city.id + 1].population;
			newMoveTime = (int)Math.floor((double)population / city.lineCnt);
			city.moveTime = newMoveTime;
			pq.add(city);
			
			count++;
			if(count == M) break;			
		}
	    return newMoveTime;
	}
	 
	public int calculate(int mFrom, int mTo) {
		int from = mFrom;
		int to = mTo;
		if(mFrom > mTo) {
			from = mTo;
			to = mFrom;
		}
		
		int sum = 0;
		for(int i=from; i<to; i++) {
			sum += cityInfo[i].moveTime;
		}
		
	    return sum;
	}
	 
	public int divide(int mFrom, int mTo, int K) {		
		int start = 1;
		int end = 10_000_000;
		while(start <= end) {
			int mid = (start + end) / 2;
			int ret = test(mFrom, mTo, mid, K);
			//System.out.println("mid => " + mid + " , result => " + ret);
			if(ret <= K) {				
				end = mid - 1;
			} else {
				start = mid + 1;
			}
		}
		
	    return end;
	    
	 
	}
	
	int test(int mFrom, int mTo, int target, int K) {		
		int cnt = 0;
		int sum = 0;
		
		for(int i=mFrom; i<=mTo; i++) {
			if(populationByCity[i] > target) return K+1;
			sum += populationByCity[i];			
			if(sum >= target) {
				cnt++;			
				sum = 0;
				i--;				
			}
			if(i == mTo) {
				cnt++;
			}
			
			if(cnt > K) return cnt;
		}
		
		return cnt;
	}
	
	class City {
		int id;
		int population;
		int moveTime;
		int lineCnt;
		public City(int id, int population, int moveTime) {		
			this.id = id;
			this.population = population;
			this.moveTime = moveTime;
			this.lineCnt = 1;
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
				print(q, "calculate", ans, ret, from, to);
				if(ret != ans)
					okay = false; 
				break;
				
			case CMD_DIVIDE:
				from = Integer.parseInt(st.nextToken());
				to = Integer.parseInt(st.nextToken());
				num = Integer.parseInt(st.nextToken());
				ret = userSolution.divide(from, to, num);  
				ans = Integer.parseInt(st.nextToken());
				print(q, "divide", ans, ret, from, to, num);
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
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\국가행정\\sample_input.txt"));

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