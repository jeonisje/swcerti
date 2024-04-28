package 연습문제.설국열차;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

class userSolution {
	
	int N, M, J;
	int[] pointByPasseger;
	int[] jobByPasseger;
	TreeMap<Passenger, Integer>[] data;
	
	Passenger[] info;
	ArrayList<Passenger>[] passengerByJob;
	
	int classCount = 0;
	
	
	
	public void init(int N, int M, int J, int[] Point, int[] JobID) {
		this.N = N;
		this.M = M;
		this.J = J;
		this.pointByPasseger = Point;
		this.jobByPasseger = JobID;
		
		classCount = N/M;
		
		data = new TreeMap[classCount];
		for(int i=0; i<classCount; i++) {
			data[i] = new TreeMap<>((o1, o2) -> o1.points == o2.points ? Integer.compare(o1.id, o2.id) : Integer.compare(o2.points, o1.points));			
		}
		
		info = new Passenger[N];
		passengerByJob = new ArrayList[J];
		
		for(int i=0; i<J; i++) {
			passengerByJob[i] = new ArrayList<Passenger>();
		}
		
		for(int id=0; id<N; id++) {
			int trainClass = id / M;
			
			Passenger p = new Passenger(id, trainClass, pointByPasseger[id], jobByPasseger[id]);
			passengerByJob[jobByPasseger[id]].add(p);
			info[id] = p;
			
			data[trainClass].put(p, id);
		}
		
	}	
		
	
	public void destroy() {
		return; 
	}
	
	public int update(int uID, int Point) {
//		System.out.println("before update");		
//		printAllMap();
		Passenger p = info[uID];
		
		
		data[p.trainClass].remove(p);
		p.points += Point;
		data[p.trainClass].put(p, p.id);
		
		
//		System.out.println("after update");
//		printAllMap();
		return p.points; 
	}
	
	public int updateByJob(int JobID, int Point) {
		int sum = 0;
		//System.out.println("before updateByJob");
		//printAllMap();
		for(Passenger p : passengerByJob[JobID]) {
			//p.points += Point;
			int trainClass = p.trainClass;
			data[trainClass].remove(p);
			p.points += Point;
			data[trainClass].put(p, p.id);
			
			sum += p.points;
		}
		//System.out.println("after updateByJob");
		//printAllMap();
		return sum;
	}
	
	private void printAllMap() {
		
		for(int i=0; i<classCount; i++) {
			System.out.println();
			System.out.println("trainClass : " + i);
			print(data[i]);	
			
		}
			
	
	}
	
	public int move(int num) {
		int sum = 0;
		
		//System.out.println("before move ===========================>");
		//printAllMap();
		
		//StringBuffer sb = new StringBuffer();
		
		Passenger[][] higher  = new Passenger[classCount][num];
		Passenger[][] lower  = new Passenger[classCount][num];
		
		for(int i=0; i<classCount-1; i++) {
			for(int j=0; j<num; j++) {
				higher[i+1][j] = data[i].pollLastEntry().getKey();
			}
		}
		
		for(int i=1; i<classCount; i++) {
			for(int j=0; j<num; j++) {
				lower[i-1][j] = data[i].pollFirstEntry().getKey();
			}
		}
		
		for(int i=0; i<classCount; i++) {
			for(int j=0; j<num; j++) {
				if(higher[i][j] != null) {
					higher[i][j].trainClass = i;
					data[i].put(higher[i][j], higher[i][j].id);
					
					sum += higher[i][j].points;
				}
				
				
				if(lower[i][j] != null) {
					lower[i][j].trainClass = i;
					data[i].put(lower[i][j], lower[i][j].id);
					
					sum += lower[i][j].points;
				}
				
			}
		}		
		
		return sum; 
	}
	
	private void print(TreeMap<Passenger, Integer> map) {
		
		System.out.println("------------------------------------");
		
		for( Map.Entry<Passenger, Integer> entry : map.entrySet() ){
			Passenger p = entry.getKey();
			
		    System.out.println( "id :" + p.id +", points : "+ p.points + ", train class : " + p.trainClass);
		}
		
	}
	
	class Passenger {
		int id;
		int trainClass;
		int points;
		int jobId;
		public Passenger(int id, int trainClass, int points, int jobId) {
			this.id = id;
			this.trainClass = trainClass;
			this.points = points;
			this.jobId = jobId;
		}
		
	}
}

public class Main {

	private final static int CMD_INIT 		= 100;
	private final static int CMD_DESTROY 	= 200;
	private final static int CMD_UPDATE 	= 300;
	private final static int CMD_UPDATE_JOB = 400;
	private final static int CMD_MOVE 		= 500;
	private final static int MAX_N 			= 100000;

	private final static userSolution userSolution = new userSolution();
	public static BufferedReader br;
	public static StringTokenizer st;
	
	private static void print(int num, String cmd, int ans, int ret, Object...o) {
		//if(ans != ret) System.err.println("==================오류===================");
		//System.out.println("[" + num +"] " + cmd + " : " +  ans + "=" + ret + "[" + Arrays.deepToString(o) + "]" );
		
	}

	private static boolean run() throws Exception {
		
		boolean isCorrect = false; 
		int N;
		int cmd; 
		int mN, mM, mJ;
		int mID, mJobID;
		int mPoint, mNum; 
		int userAns, ans; 
		
		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		
		for (int q = 0; q < N; q++) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) 
			{
				case CMD_INIT :
					mN = Integer.parseInt(st.nextToken());
					mM = Integer.parseInt(st.nextToken());
					mJ = Integer.parseInt(st.nextToken());
					int[] Point = new int[MAX_N];
					int[] JobID = new int[MAX_N];
					for(int i = 0; i < mN; i++)
						Point[i] = Integer.parseInt(st.nextToken());
					for(int i = 0; i < mN; i++)
						JobID[i] = Integer.parseInt(st.nextToken());
					userSolution.init(mN, mM, mJ, Point, JobID);
					isCorrect = true; 
					break; 
					
				case CMD_UPDATE :
					mID = Integer.parseInt(st.nextToken());
					mPoint = Integer.parseInt(st.nextToken());
					userAns = userSolution.update(mID, mPoint);
					ans = Integer.parseInt(st.nextToken());
					print(q, "update", ans, userAns, mID, mPoint);
					if(userAns != ans)
						isCorrect = false;
					break; 
					
				case CMD_UPDATE_JOB :
					mJobID = Integer.parseInt(st.nextToken());
					mPoint = Integer.parseInt(st.nextToken());
					userAns = userSolution.updateByJob(mJobID, mPoint);
					ans = Integer.parseInt(st.nextToken());
					print(q, "updateByJob", ans, userAns, mJobID, mPoint);
					if(userAns != ans)
						isCorrect = false;
					break; 
					
				case CMD_MOVE : 
					mNum = Integer.parseInt(st.nextToken());
					userAns = userSolution.move(mNum); 
					ans = Integer.parseInt(st.nextToken());
					print(q, "move", ans, userAns, mNum);
					if(userAns != ans)
						isCorrect = false;
					break; 
					
				default :
					isCorrect = false;
					break; 
			}
		
		}
		userSolution.destroy();
		return isCorrect; 
	}

	public static void main(String[]args) throws Exception {
		
		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//연습문제//설국열차//sample_input2.txt"));
		
		br = new BufferedReader(new InputStreamReader(System.in));
		//StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		int TC, MARK;

		st = new StringTokenizer(br.readLine());
		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());
		
		for (int testcase = 1; testcase <= TC; testcase++) {
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}