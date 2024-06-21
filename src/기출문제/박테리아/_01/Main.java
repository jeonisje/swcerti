package 기출문제.박테리아._01;


import java.io.*;
import java.util.*;

class UserSolution {
	int MAX = 20_001;
	int N;
	
	PriorityQueue<Bacteria> pq;
	TreeSet<Bacteria2> tset;
	HashMap<String, Integer> strToId;
	
	int[] halfTime;
	
	int[] countByBacteria;	// 박테리아별 남은 수
	int[] countBySeq;		// 입고별 박테리아 개수
	int[] lifeBySeq;	// 입고별 남은 생명력
	int[] idBySeq;		// seq별 박테리아 아이디	
	
	int sequence;
	
  
    public void init(int N, char bNameList[][], int mHalfTime[]) {
    	this.N = N;
    	
    	pq = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.halfTime, o2.halfTime));
    	tset = new TreeSet<>((o1, o2) -> o1.life == o2.life ? Integer.compare(o1.seq, o2.seq) : Integer.compare(o1.life, o2.life));
    	
    	strToId = new HashMap<>();
    	countByBacteria = new int[N+1];
    	halfTime = new int[N+1];
    	countBySeq = new int[MAX];
    	lifeBySeq = new int[MAX];
    	idBySeq = new int[MAX];
    	
    	
    	sequence = 0;
    	for(int i=0; i<N; i++) {
    		String s = String.valueOf(bNameList[i]).trim();
    		strToId.put(s, i+1);
    		halfTime[i+1] = mHalfTime[i];
    	}
    	
    	return; 
	}
    
    // 20,000 + a ?
	void addBacteria(int tStamp, char bName[], int mLife, int mCnt) {
		passTime(tStamp);
		
		sequence++;
		
		int id = strToId.get(String.valueOf(bName).trim());
		idBySeq[sequence] = id;
		lifeBySeq[sequence] = mLife;
		
		pq.add(new Bacteria(sequence, tStamp + halfTime[id]));
		tset.add(new Bacteria2(sequence, lifeBySeq[sequence]));
		
		countByBacteria[id] += mCnt;
		countBySeq[sequence] = mCnt;
		
		return; 
	}
	
	void passTime(int tStamp) {
		while(!pq.isEmpty()) {
			if(pq.peek().halfTime > tStamp) break;
			
			Bacteria b = pq.remove();
			if(countBySeq[b.seq] == 0) continue;
			tset.remove(new Bacteria2(b.seq, lifeBySeq[b.seq]));
			int id = idBySeq[b.seq];
			
			int diff = tStamp - b.halfTime;
			int times = diff / halfTime[id]  + 1;			
			int life = (int) (lifeBySeq[b.seq] / Math.pow(2, times));	
			
			if(life <= 9) {
				countByBacteria[id] -= countBySeq[b.seq];
				continue;
			}
			int remain = diff % halfTime[id];
			lifeBySeq[b.seq] = life;		
			pq.add(new Bacteria(b.seq, tStamp + halfTime[id] - remain));
			tset.add(new Bacteria2(b.seq, lifeBySeq[b.seq]));
		}
	}
	
	// 15,000
	int takeOut(int tStamp, int mCnt) {
		passTime(tStamp);
		
		int sum = 0;
		int cnt = 0;
		
		while(!tset.isEmpty()) {
			Bacteria2 b = tset.first();
			if(countBySeq[b.seq] == 0) {
				tset.pollFirst();
				continue;
			}
			
			countByBacteria[idBySeq[b.seq]]--;
			sum += b.life;
			//removed[b.seq] = 1;
			countBySeq[b.seq]--;
			cnt++;
			
			if(cnt == mCnt) break;
		}
		
		return sum;
	}
	// 15,000
	int checkBacteria(int tStamp, char bName[]) {
		passTime(tStamp);
		
		int id = strToId.get(String.valueOf(bName).trim());	
		
		return countByBacteria[id];
	}
	
	class Bacteria {
		int seq; 	// 입고 순서
		int halfTime;	// 반감시간
		public Bacteria(int seq, int halfTime) {	
			this.seq = seq;
			this.halfTime = halfTime;		
		}
	}
	
	class Bacteria2 {
		int seq;
		int life;
		public Bacteria2(int seq, int life) {		
			this.seq = seq;
			this.life = life;
		}
	}
}


public class Main
{	
	private final static int MAX_BCNT		= 100;
	private final static int MAX_NAME		= 10;

    private static char bname[][] = new char [MAX_BCNT][MAX_NAME];
    private static int halftime[] = new int [MAX_BCNT];
	
    private final static int CMD_INIT		= 0;
    private final static int CMD_ADD		= 1;
    private final static int CMD_OUT		= 2;
    private final static int CMD_CHECK 		= 3;

    private static UserSolution usersolution = new UserSolution();

    private static void String2Char(String s, char[] b)
    {
        int n = s.length();
        for (int i = 0; i < n; ++i) b[i] = s.charAt(i);
        for (int i = n; i < MAX_NAME; ++i) b[i] = '\0';
    }   

    private static boolean run (BufferedReader br) throws Exception 
    {
		int time, life, cnt, cmd, ans, ret;

        int Q = 0;
        boolean okay = false;

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        Q = Integer.parseInt(st.nextToken());

        for (int i = 0; i < Q; ++i)
        {
            st = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(st.nextToken());
            switch (cmd)
            {
            case CMD_INIT:
				cnt = Integer.parseInt(st.nextToken());
				for (int k = 0; k < cnt; k++) {
					st = new StringTokenizer(br.readLine(), " ");
					String2Char(st.nextToken(), bname[k]);
					halftime[k] = Integer.parseInt(st.nextToken());
				}
				usersolution.init(cnt, bname, halftime);
				okay = true;
    			break;
    		case CMD_ADD:
				time = Integer.parseInt(st.nextToken());
				String2Char(st.nextToken(), bname[0]);
				life = Integer.parseInt(st.nextToken());
				cnt = Integer.parseInt(st.nextToken());
				usersolution.addBacteria(time, bname[0], life, cnt);
				print(i, "addBacteria", i, i, time, bname[0], life, cnt);
    			break;
    		case CMD_OUT:
				time = Integer.parseInt(st.nextToken());
				cnt = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());				
				ret = usersolution.takeOut(time, cnt);
				print(i, "takeOut", ans, ret, time, cnt);
				if (ans != ret)
					okay = false;
    			break;
    		case CMD_CHECK:
				time = Integer.parseInt(st.nextToken());
				String2Char(st.nextToken(), bname[0]);
				ans = Integer.parseInt(st.nextToken());
				ret = usersolution.checkBacteria(time, bname[0]);
				print(i, "checkBacteria", ans, ret, time, bname[0]);
				if (ans != ret)
					okay = false;
				break;
    		default:
    			okay = false;
    		}
    	}

    	return okay;
    }
    
    static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
    
    public static void main(String[] args) throws Exception
    {

    	long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\박테리아\\sample_input.txt"));
	

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
        System.out.println("ms => " + (System.currentTimeMillis() - start));
    }
}