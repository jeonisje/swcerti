package 기출문제.프린터공장;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	int MAX = 30_001;
	HashMap<Integer, Integer> idToSeq;
	
	int[] timeBySeq;
	int[] removed;
	
	PriorityQueue<Printer> pqForPrinter;
	
	int totalCount;
	
	int sequence;
  
	public void init(int N, int[] mId, int[] pTime) {
		idToSeq = new HashMap<>();
		timeBySeq = new int[MAX];
		removed = new int[MAX];
		
		pqForPrinter = new PriorityQueue<>((o1, o2) -> o1.time == o2.time ? Integer.compare(o2.id, o1.id) : Integer.compare(o2.time, o1.time));
		
		sequence = 0;
		totalCount = 0;
		
		for(int i=0; i<N; i++) {
			sequence++;
			idToSeq.put(mId[i], sequence);
			
			timeBySeq[sequence] = pTime[i];
			pqForPrinter.add(new Printer(mId[i], sequence, pTime[i]));
			totalCount++;
		}
		
		return; 
	}
	
	public int add(int mId, int pTime) {
		if(idToSeq.containsKey(mId)) {
			int seq = idToSeq.get(mId);
			removed[seq] = 1;			
		} else {
			totalCount++;
		}
		sequence++;
		idToSeq.put(mId, sequence);
		timeBySeq[sequence] = pTime;
		pqForPrinter.add(new Printer(mId, sequence, pTime));
		
		
		return totalCount; 
	}
	
	public int remove(int k) {
		int count = 0;
		
		while(!pqForPrinter.isEmpty()) {
			Printer cur = pqForPrinter.remove();
			
			if(removed[cur.seq] == 1) continue;
			removed[cur.seq] = 1;
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
		PriorityQueue<Work> pq = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.completeTime, o2.completeTime));
		
		for(int i=1; i<sequence + 1;i++) {
			if(removed[i] == 1) continue;
			pq.add(new Work(i, timeBySeq[i]));
		}
		
		int count = 0;
		while(!pq.isEmpty()) {
			Work cur = pq.remove();
			count++;
			//System.out.println("seq : " + cur.seq + ", time : " + cur.completeTime);
			if(count == M) return cur.completeTime;
			cur.completeTime += timeBySeq[cur.seq];
			pq.add(cur);
			
		}
		
		
		return 0; 
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