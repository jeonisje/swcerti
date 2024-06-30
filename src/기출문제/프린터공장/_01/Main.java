package 기출문제.프린터공장._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	int MAX = 30_001;
	int MAX_TIME = 3_001;
	HashMap<Integer, Integer> idToSeq;
	
	int[] timeBySeq;
	int[] removed;
	int[] countByTime;
	
	PriorityQueue<Printer> pqForPrinter;
	
	int totalCount;
	
	int sequence;
  
	public void init(int N, int[] mId, int[] pTime) {
		idToSeq = new HashMap<>();
		timeBySeq = new int[MAX];
		countByTime = new int[MAX_TIME];
		removed = new int[MAX];
		
		pqForPrinter = new PriorityQueue<>((o1, o2) -> o1.time == o2.time ? Integer.compare(o2.id, o1.id) : Integer.compare(o2.time, o1.time));
		
		sequence = 0;
		totalCount = 0;
		
		for(int i=0; i<N; i++) {
			sequence++;
			idToSeq.put(mId[i], sequence);
			
			timeBySeq[sequence] = pTime[i];
			countByTime[pTime[i]]++;
			pqForPrinter.add(new Printer(mId[i], sequence, pTime[i]));
			totalCount++;
		}
		
		return; 
	}
	
	public int add(int mId, int pTime) {
		if(idToSeq.containsKey(mId)) {
			int seq = idToSeq.get(mId);
			removed[seq] = 1;
			countByTime[timeBySeq[seq]]--;
		} else {
			totalCount++;
		}
		sequence++;
		idToSeq.put(mId, sequence);
		timeBySeq[sequence] = pTime;
		countByTime[pTime]++;
		pqForPrinter.add(new Printer(mId, sequence, pTime));
		
		
		return totalCount; 
	}
	
	public int remove(int k) {
		int count = 0;
		
		while(!pqForPrinter.isEmpty()) {
			Printer cur = pqForPrinter.remove();
			
			if(removed[cur.seq] == 1) continue;
			removed[cur.seq] = 1;
			countByTime[cur.time]--;
			count++;
			totalCount--;
			if(count == k) break;
		}
		
		while(!pqForPrinter.isEmpty()) {
			if(removed[pqForPrinter.peek().seq] == 0)
				break;
			if(removed[pqForPrinter.peek().seq] == 1)
				pqForPrinter.remove();
		}
		return pqForPrinter.peek().id; 
	}
	
	public int produce(int M) {
		int start = 3;
		int end = 10_000_000;
		while(start <= end) {
			int mid = (start + end) / 2;
			int ret = test(mid, M);
			if(ret == -1) {
				end = mid - 1;
			} else {
				start = mid + 1;
			}
		}
		return start;
	}
	
	int test(int target, int M) {
		int count = 0;
		
		for(int i=3; i<MAX_TIME; i++) {
			if(countByTime[i] == 0) continue;
			count += ((target / i) * countByTime[i]);
			if(count >= M) 
				return -1;
		}
		
		return 1;
	}
	
	class Printer {
		int id;
		int seq;
		int time;
		public Printer(int id, int seq, int time) {		
			this.id = id;
			this.seq = seq;
			this.time = time;
		}
	}
	
	class Work {
		int seq;
		int completeTime;
		public Work(int seq, int completeTime) {		
			this.seq = seq;
			this.completeTime = completeTime;
		}
	}
}


public class Main
{
	private final static int CMD_INIT 		= 1;
	private final static int CMD_ADD 		= 2;
	private final static int CMD_REMOVE 	= 3;
	private final static int CMD_PRODUCE	= 4; 

	private final static UserSolution usersolution = new UserSolution();
	
	private static boolean run(BufferedReader br) throws Exception
	{
		StringTokenizer st;
		
		int Q; 
		int n, mid, mtime, k, m;
		int[] midArr = new int[100];
		int[] mtimeArr = new int[100]; 
		int cmd, ans, userAns;
		boolean isCorrect = false; 
		
		st = new StringTokenizer(br.readLine());
		Q = Integer.parseInt(st.nextToken());
		
		for (int q = 0; q < Q; ++q)
		{
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd)
			{
				case CMD_INIT:
					isCorrect = true;
					n = Integer.parseInt(st.nextToken());
					for(int i = 0; i < n; i++) {
						midArr[i] = Integer.parseInt(st.nextToken());
						mtimeArr[i] = Integer.parseInt(st.nextToken());
					}
					usersolution.init(n, midArr, mtimeArr);
					break;
				case CMD_ADD:
					mid = Integer.parseInt(st.nextToken());
					mtime = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					userAns = usersolution.add(mid, mtime);  
					print(q, "add", ans, userAns, mid, mtime);
					if(ans != userAns)
						isCorrect = false; 
					break;
				case CMD_REMOVE:
					k = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					userAns = usersolution.remove(k);
					print(q, "remove", ans, userAns, k);
					if(userAns != ans)
						isCorrect = false; 
					break;
				case CMD_PRODUCE:
					m = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					userAns = usersolution.produce(m);
					print(q, "produce", ans, userAns, m);
					if(userAns != ans)
						isCorrect = false; 
					break;
				default:
					isCorrect = false;
					break;
			}
		}
		return isCorrect;
	}
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\프린터공장\\sample_input.txt"));
		
		int TC, MARK;
	
		//System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
		}

		br.close();
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}