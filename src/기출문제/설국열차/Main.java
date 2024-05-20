package 기출문제.설국열차;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.TreeMap;

class userSolution {
	
	int N, M, J;
	int classCount;
	
	int id;
	
	TreeMap<Passenser, Integer>[] data;
	ArrayList<Integer>[] idByJob;
	
	Passenser[] passenserInfo;
	
	
	public void init(int N, int M, int J, int[] Point, int[] JobID) {
		this.N = N;
		this.M = M;
		this.J = J;
		
		classCount = N / M;
		data = new TreeMap[classCount];
		idByJob = new ArrayList[J];
		passenserInfo = new Passenser[N];

		for(int i=0; i<classCount; i++) {
			data[i] = new TreeMap<>((o1, o2) -> o1.point == o2.point ? Integer.compare(o1.id, o2.id) : Integer.compare(o2.point, o1.point));			
		}
		
		for(int i=0; i<J; i++) {
			idByJob[i] = new ArrayList<>();
		}		
		
		for(int i=0; i<N; i++) {
			int trainClass = i / M;
			Passenser passenser = new Passenser(i, Point[i], JobID[i], trainClass);
			
			passenserInfo[i] = passenser;
			data[trainClass].put(passenser, i);
					
			idByJob[JobID[i]].add(i);
		}		
		
		return; 
	}
	
	public void destroy() {
		return; 
	}
	
	public int update(int uID, int Point) {
		Passenser passenser = passenserInfo[uID];
		
		data[passenserInfo[uID].trainClass].remove(passenser);
		
		passenser.point += Point;
		data[passenser.trainClass].put(passenser, uID);
		
		return passenser.point; 
	}
	
	public int updateByJob(int JobID, int Point) {
		int sum = 0;
		for(int id : idByJob[JobID]) {
			Passenser passenser = passenserInfo[id];			
			data[passenser.trainClass].remove(passenser);			
			passenser.point += Point;
			data[passenser.trainClass].put(passenser, id);
			
			sum += passenser.point;
		}		
		
		return sum; 
	}
	
	public int move(int num) {
		Passenser[][] moveToUp = new Passenser[classCount][num];
		Passenser[][] moveToDown = new Passenser[classCount][num];
		
		
		for(int i=0; i<classCount; i++) {			
			for(int j=0; j<num; j++) {
				if(i != classCount - 1) {
					moveToDown[i+1][j] = data[i].pollLastEntry().getKey();
				}
				if(i != 0) {
					moveToUp[i-1][j] = data[i].pollFirstEntry().getKey();
				}
			}
		}
	
		
		int sum =  0;
		for(int i=0; i<classCount; i++) {
			for(int j=0; j<num; j++) {	
				if(moveToUp[i][j] != null) {
					Passenser passenser = moveToUp[i][j];
					passenser.trainClass = i;
					data[i].put(passenser, passenser.id);
					sum += passenser.point;
				}	
				if(moveToDown[i][j] != null) {
					Passenser passenser = moveToDown[i][j];
					passenser.trainClass = i;
					data[i].put(passenser, passenser.id);
					sum += passenser.point;
				}
			}
		}
		
		return sum; 
	}
	
	class Passenser {
		int id;
		int point;
		int jobid;
		int trainClass;
		public Passenser(int id, int point, int jobid, int trainClass) {		
			this.id = id;
			this.point = point;
			this.jobid = jobid;
			this.trainClass = trainClass;
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
	public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	public static StringTokenizer st;

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
	
	private static void print(int num, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("===================오류=======================");
		//System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
	}
	

	public static void main(String[]args) throws Exception {
		Long start = System.currentTimeMillis();
		
		int TC, MARK;
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\설국열차\\sample_input.txt"));
		
		br = new BufferedReader(new InputStreamReader(System.in));

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