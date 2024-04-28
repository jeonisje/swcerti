package 기출문제.출근길라디오;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;


class UserSolution {
	
	int MAX = 100_001;
	int UNIT = 2000;
	int REQUIRED_SUMMARY = 4000;
	int N, M;
	int[] roadType;
	int[] time;
	
	int[] summaryBy1000;
	int summaryCount;
	
	ArrayList<Integer>[] idsByRoadType;
	
	boolean useSummary;
	
	void init(int N, int M, int mType[], int mTime[]) {
		this.N = N;
		this.M = M;
		roadType = mType;
		time = mTime;
		
		idsByRoadType = new ArrayList[M];
		
		summaryCount = N / UNIT;
		summaryBy1000 = new int[MAX/UNIT];
		
		useSummary = N >= REQUIRED_SUMMARY;
		
		for(int i=0; i<M; i++) {
			idsByRoadType[i] = new ArrayList<>();
		}
		
		for(int i=0; i<N-1; i++) {
			int roadType = mType[i];
			idsByRoadType[roadType].add(i);
		}
		
		if(useSummary) {
			initSummary();
		}
	}
	
	void initSummary() {
		int end = N / UNIT * UNIT;
		int preID = 0;
		int preIDX = 0;
		for(int i=0; i<end; i++) {
			int summayID = i / UNIT;			
			summaryBy1000[summayID] += time[i];
		}
	}

	void destroy() {
		return; 
	}

	void update(int mID, int mNewTime) {
		int oldValue = time[mID];
		
		time[mID] = mNewTime; 
		
		if(useSummary) {
			int summaryID = mID / UNIT;
			if(summaryBy1000[summaryID] == 0) return;
			summaryBy1000[summaryID] -= oldValue;
			summaryBy1000[summaryID] += time[mID];
		}
	}
	
	void updateSummary(int summaryID) {
		int start = summaryID * UNIT;
		int end = start + UNIT - 1;
		
		int sum = 0;
		for(int i = start; i<=end; i++) {
			sum += time[i];
		}
		summaryBy1000[summaryID] = sum;
	}

	int updateByType(int mTypeID, int mRatio256) {
		int sum = 0;
		HashSet<Integer> requiedSummaryUpdate = new HashSet<>();
		for(int roadID : idsByRoadType[mTypeID]) {
			int oldValue = time[roadID];			
			
			int newTime = time[roadID] * mRatio256 / 256;
			time[roadID] = newTime;
			
			sum += newTime;
			
			int summaryID = roadID/UNIT;
			if(useSummary && summaryID < summaryCount) {
				if(summaryBy1000[summaryID] == 0) continue;
				
				summaryBy1000[summaryID] -= oldValue ;
				summaryBy1000[summaryID] += time[roadID];
			}
		}		
		
		return sum;
	}

	int calculate(int mA, int mB) {
		int sum = 0;
		
		int start = mA;
		int end = mB;
		if(mA > mB) {
			start = mB;
			end = mA;
		}

		int summaryDiff = (end / UNIT) - (start / UNIT);
		
		if(!useSummary || summaryDiff <= 1) {		
			sum += getSum(start, end);
		} else {
			int startSummaryID = start / UNIT + 1;
			int endSummaryID = end / UNIT;			
			
			int end1 = startSummaryID * UNIT;
			sum += getSum(start, end1);
			
			for(int i=startSummaryID; i<endSummaryID; i++) {
				sum += summaryBy1000[i];
			}
			
			int start2 = endSummaryID * UNIT;
			sum += getSum(start2, end);			
		}
		
		
		
		return sum;
	}
	
	int getSum(int start, int end) {
		int sum = 0;
		for(int i = start; i < end; i++) {
			sum += time[i];
		}
		return sum;
	}
}


public class Main {
	private static UserSolution usersolution = new UserSolution();
	static final int CMD_INIT = 100;
	static final int CMD_DESTROY = 200;
	static final int CMD_UPDATE = 300;
	static final int CMD_UPDATE_TYPE = 400;
	static final int CMD_CALC = 500;
	static final int MAX_N = 100000;
	static int[] mType = new int [MAX_N];
	static int[] mTime = new int [MAX_N];
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("====================오류========================");
		//System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
	}
	
	private static boolean run(BufferedReader br) throws IOException  {
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		boolean isOK = false;
		int C = new Scanner(st.nextToken()).nextInt();
		int cmd, result, check;
		int N, M;
		int mID, mTypeID, mNewTime, mRatio256;
		int mA, mB;
		for (int c = 0; c < C; ++c) {
			st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch (cmd)
			{
			case CMD_INIT:
				N = new Scanner(st.nextToken()).nextInt();
				M = new Scanner(st.nextToken()).nextInt();
				for (int i = 0; i < N - 1; i++) mType[i] = Integer.parseInt(st.nextToken());
				for (int i = 0; i < N - 1; i++) mTime[i] = Integer.parseInt(st.nextToken());
				usersolution.init(N, M, mType, mTime);
				isOK = true;
				break;
			case CMD_UPDATE:
				mID = Integer.parseInt(st.nextToken());
				mNewTime = Integer.parseInt(st.nextToken());
				usersolution.update(mID, mNewTime);
				print(c, "update", c, c, mID, mNewTime);
				break;
			case CMD_UPDATE_TYPE:
				mTypeID = new Scanner(st.nextToken()).nextInt();
				mRatio256 = new Scanner(st.nextToken()).nextInt();
				result = usersolution.updateByType(mTypeID, mRatio256);
				
				check = new Scanner(st.nextToken()).nextInt();
				
				print(c, "updateByType", check, result, mTypeID, mRatio256);
				if (result != check)
					isOK = false;
				break;
			case CMD_CALC:
				mA = Integer.parseInt(st.nextToken());
				mB = Integer.parseInt(st.nextToken());
				result = usersolution.calculate(mA, mB);
				check = Integer.parseInt(st.nextToken());
				print(c, "calculate", check, result, mA, mB);
				if (result != check)
					isOK = false;
				break;
			default:
				isOK = false;
				break;
			}
		}
		usersolution.destroy();
		return isOK;
	}

	public static void main(String[] args) throws Exception {
		
		Long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//기출문제//출근길라디오//sample_input.txt"));
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer line = new StringTokenizer(br.readLine(), " ");
		int TC = Integer.parseInt(line.nextToken());
		int MARK = Integer.parseInt(line.nextToken());
		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}