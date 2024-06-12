package 기출문제.로그인대기열._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap; 

class UserSolution {
	int MAX_LOGIN = 50_001;
	
	HashMap<String, TreeMap<String, Integer>> waiting;
	
	int[] removed;
	
	int sequence;
	int curSeq;
	
	public void init() {
		waiting = new HashMap<>();
		removed = new int[MAX_LOGIN];
		sequence = 0;
		curSeq = 0;
	}
	
	public void loginID(char mID[]) {
		String id = String.valueOf(mID).trim();
		String prefix = id.substring(0, 3);
		
		TreeMap<String, Integer> tm = waiting.getOrDefault(prefix, new TreeMap<>());
		if(tm.containsKey(id)) 
			removed[tm.get(id)] = 1;	// 이전에 로그인했던 정보 삭제
		tm.put(id, sequence);
		waiting.put(prefix, tm);
		sequence++;
		
		return; 
	}
	
	public int closeIDs(char mStr[]) {			
		String prefix = String.valueOf(mStr).trim();
		String prefix3 = prefix.substring(0, 3);
		
		if(!waiting.containsKey(prefix3)) return 0;
		
		String key = waiting.get(prefix3).ceilingKey(prefix);
		if(key == null) return 0;
		
		int count = 0;
		for(Map.Entry<String, Integer> entry : waiting.get(prefix3).subMap(prefix, true, prefix + Character.MAX_VALUE, false).entrySet()) {
			int seq = entry.getValue();
			if(removed[seq] == 1) continue;
			count++;
			removed[seq] = 1;
		}
		
		return count;
	}
	
	public void connectCnt(int mCnt) {
		int cur = curSeq;
		int count = 0;
		
		while(mCnt != count) {
			if(removed[cur] == 1) {
				cur++;
				continue;
			}
			removed[cur] = 1;
			cur++;
			count++;
		}
		
		curSeq = cur;
		
      	return; 
	}
	
	public int waitOrder(char mID[]) {
		String id = String.valueOf(mID).trim();
		String prefix = id.substring(0, 3);
		
		Integer seq = waiting.get(prefix).get(id);
		if(seq == null) return 0;
		if(removed[seq] == 1) return 0;
		
		int count = 0;
		for(int i=curSeq; i<=seq; i++) {
			if(removed[i] == 1) continue;
			count++;
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