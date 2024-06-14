package 기출문제.출근길라디오._01;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


class UserSolution {
	
	int N;
	
	int[] array;
	int[] tree;
	
	ArrayList<Integer>[] indexByType;
	
	
	void init(int N, int M, int mType[], int mTime[]) {
		this.N= N;
		
		
		array = new int[N+1];
		tree = new int[N*4];
		
		indexByType = new ArrayList[M];
		for(int i=0; i<M; i++) {
			indexByType[i] = new ArrayList<>();
		}
		
		for(int i=0; i<N; i++) {
			indexByType[mType[i]].add(i+1);
		}
		
		initTree(1, N-1, 1, mTime);
		return;
				
	}
	
	void initTree(int start, int end, int node, int[] mTime) {
		if(start == end) {
			array[start] = mTime[start-1];
			tree[node] = mTime[start-1];
			return;
		}
		
		int mid = (start + end) / 2;
		initTree(start, mid, node*2, mTime);
		initTree(mid+1, end, node*2+1, mTime);			
		
		tree[node] = tree[node*2] + tree[node*2+1];
	}
	

	void destroy() {
		return; 
	}

	void update(int mID, int mNewTime) {
		updateTree(1, N-1, 1, mID+1, mNewTime);
		return;
	}
	
	void updateTree(int start, int end, int node, int idx, int value) {
		if(idx < start || idx > end) return;
		if(start == end) {
			tree[node] = value;
			array[start] = value;
			return ;	
		}
		
		int mid = (start + end) / 2;
		updateTree(start, mid, node*2, idx, value);
		updateTree(mid+1, end, node*2+1, idx, value);
		tree[node] = tree[node*2] + tree[node*2+1];
		
	}
	

	int updateByType(int mTypeID, int mRatio256) {
		int sum = 0;
		for(int idx : indexByType[mTypeID]) {
			int oldValue = array[idx];
			int newValue = oldValue * mRatio256 / 256;
			sum += newValue;
			
			updateTree(1, N-1, 1, idx, newValue);
		}
		
		return sum;
	}

	int calculate(int mA, int mB) {
		int start = Math.min(mA, mB);
		int end = Math.max(mA, mB);
		return query(1, N-1, 1, start+1, end);		
	}
	
	int query(int start, int end, int node, int left, int right) {
		if(end < left || right < start) return 0;
		if(left <= start && end <= right) return tree[node];
		int mid = (start + end) / 2;
		int lv = query(start, mid, node*2, left, right);
		int rv = query(mid+1, end, node*2+1, left, right);
		return lv + rv;
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
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("====================오류========================");
		//System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
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