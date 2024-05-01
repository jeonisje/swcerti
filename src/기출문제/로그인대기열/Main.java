package 기출문제.로그인대기열;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap; 

class UserSolution {
	int MAX_LOGIN = 50_001;
	
	TreeMap<String, Integer> idToSeq;
	TreeMap<Integer, String> seqToId;
	
	boolean[] isWaiting;
	
	int sequence;
	int lastLoginSeq;	
	
	public void init() {
		idToSeq = new TreeMap<>();
		seqToId = new TreeMap<>();
		isWaiting = new boolean[MAX_LOGIN];
		
		sequence = 0;
		lastLoginSeq = 0;
	}
	
	public void loginID(char mID[]) {
		sequence++;
		String id = String.valueOf(mID);
		if(idToSeq.containsKey(id)) {
			int seq = idToSeq.get(id);
			isWaiting[seq] = false;
			seqToId.remove(seq);			
		}
		
		idToSeq.put(id, sequence);
		seqToId.put(sequence, id);
		
		isWaiting[sequence] = true;
		
		return; 
	}
	
	public int closeIDs(char mStr[]) {			
		int length = 0;
		for(int i=0; i<mStr.length; i++) {
			if(mStr[i] == '\0') {
				length = i;
				break;
			}
		}
		
		String prefixStart = String.valueOf(mStr).trim();
		String prefixEnd = prefixStart.substring(0, length - 1 ) + (char)(mStr[length-1] + 1);
		
		ArrayList<String> target = new ArrayList<>();
		//Map<String, Integer> sub = idToSeq.subMap(prefixStart, prefixEnd);
		for(Entry<String, Integer> entry : idToSeq.subMap(prefixStart, prefixEnd).entrySet()) {
		//for(Entry<String, Integer> entry : sub.entrySet()) {
			String key = entry.getKey();
			int seq = entry.getValue();
			target.add(key);
			isWaiting[seq] = false;
			seqToId.remove(seq);
		}
		
		for(String  id : target) {
			idToSeq.remove(id);
		}
		
		return target.size();
	}
	
	public void connectCnt(int mCnt) {
		int count = 0;
		ArrayList<Integer> target = new ArrayList<>();
		for(Map.Entry<Integer, String> entry : seqToId.entrySet()) {
			int seq = entry.getKey();
			String id = entry.getValue();			
			idToSeq.remove(id);
			count++;
			target.add(seq);
			if(count == mCnt) break;
		}
		
		for(int seq : target) {
			isWaiting[seq] = false;
			lastLoginSeq = seq;
			seqToId.remove(seq);
		}
		
      	return; 
	}
	
	public int waitOrder(char mID[]) {
		String id = String.valueOf(mID);
		if(!idToSeq.containsKey(id)) return 0;
		
		int seq = idToSeq.get(id);
		int count = 0;
		for(int i=lastLoginSeq+1; i<=seq; i++) {
			if(isWaiting[i]) count++;			
		}
		
		return count;
	}
}

public class Main
{	
	private final static int MAX_NAME		= 10;

	private final static int CMD_INIT		= 0;
    private final static int CMD_LOGIN		= 1;
    private final static int CMD_CLOSE		= 2;
    private final static int CMD_CONNECT	= 3;
    private final static int CMD_ORDER		= 4;
    
    private static char[] nameID = new char [MAX_NAME];
    
    private static UserSolution usersolution = new UserSolution();
    
    private static void String2Char(String s, char[] b)
    {
        int n = s.length();
        for (int i = 0; i < n; ++i) b[i] = s.charAt(i);
        for (int i = n; i < MAX_NAME; ++i) b[i] = '\0';
    }  

    private static boolean run (BufferedReader br) throws Exception 
    {
		int cmd, ans, ret;

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
				usersolution.init();
				okay = true;
    			break;
   
    		case CMD_LOGIN:
				String2Char(st.nextToken(), nameID);
				usersolution.loginID(nameID);
				print(i, "loginID", i, i, nameID);
    			break;
    
    		case CMD_CLOSE:
				ans = Integer.parseInt(st.nextToken());
				String2Char(st.nextToken(), nameID);
				ret = usersolution.closeIDs(nameID);
				print(i, "closeIDs", ans, ret, nameID);
				if (ans != ret)
					okay = false;
    			break;
    
    		case CMD_CONNECT:
				ans = Integer.parseInt(st.nextToken());
				usersolution.connectCnt(ans);
				print(i, "connectCnt", i, i, ans);
				break;
	
    		case CMD_ORDER:
				ans = Integer.parseInt(st.nextToken());
				String2Char(st.nextToken(), nameID);
				ret = usersolution.waitOrder(nameID);
				print(i, "waitOrder", ans, ret, nameID);
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
    	//if(ans != ret) System.err.println("---------------------오류------------------------");
    	//System.out.println("["+q+"] " + cmd +  " " + ans + "=" + ret + " [" + Arrays.deepToString(o) + "]" );
    }
    
    public static void main(String[] args) throws Exception
    {

    	long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\로그인대기열\\sample_input.txt"));

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