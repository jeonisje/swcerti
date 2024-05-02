package 기출문제.상징물설치;


import java.io.*;
import java.util.*;
import java.util.Map.Entry;

class UserSolution {
	final int MAX_BEAM = 201;
	
	HashMap<Integer, Integer> oneCombi;
	HashMap<Integer, ArrayList<Integer []>> twoCombi;
	HashMap<Integer, Integer> countByNumber;
		
	int[] beam;
	int sequence;
	
    public void init() {
    	oneCombi = new HashMap<>();
    	twoCombi = new HashMap<>();
    	countByNumber = new HashMap<>();
    	
    	beam = new int[MAX_BEAM];    	
    	
    	sequence = 0;
    	return; 
    }
    
    public void addBeam(int mLength) {
    	sequence++;
    	
    	beam[sequence] = mLength;
    	
    	oneCombi.put(mLength, mLength);
    	
    	int count = countByNumber.getOrDefault(mLength, 0);
    	countByNumber.put(mLength, count);
    	
    	if(sequence < 2) return;
    	
    	for(int i=1; i<=sequence-1; i++) {    		
    		int combi = beam[i] + mLength;
    		ArrayList<Integer []> list = twoCombi.getOrDefault(combi, new ArrayList<Integer []>());
    		list.add(new Integer[]{i, sequence});
    		twoCombi.put(combi, list);
    	}
        return;
    }
     
    public int requireSingle(int mHeight) {
    	for(Entry<Integer, Integer> entry : oneCombi.entrySet()) {
    		int diff = mHeight - entry.getKey();
    		
    		if(!twoCombi.containsKey(diff)) continue;
    		
    		ArrayList<Integer []> combiList = twoCombi.get(diff);
    		int[] used = new int[MAX_BEAM];    	
    		for(int i=0; i<combiList.size(); i++) {
    			int seq1 = combiList.get(i)[0];
    			int seq2 = combiList.get(i)[1];
    			if(used[seq1] == 1 || used[seq2] == 2) continue;
    			int num1= beam[seq1];
    			int num2= beam[seq2];
    		}
    		
    		
    		
    	}
        return -1;
    }
     
    public int requireTwin(int mHeight) {
        return -1;
    }
}

class Main {
    private static BufferedReader br;
    private static UserSolution userSolution = new UserSolution();

    private final static int CMD_INIT 			= 100;
    private final static int CMD_ADD_BEAM 		= 200;
    private final static int CMD_REQUIRE_SINGLE = 300;
    private final static int CMD_REQUIRE_TWIN 	= 400;

    private static boolean run() throws Exception {

        StringTokenizer stdin;

        int Q;
        int mLength, mHeight; 
        int ret = -1;
        int ans; 
        boolean okay = false;
        
        stdin = new StringTokenizer(br.readLine(), " ");
        Q = Integer.parseInt(stdin.nextToken());

        for (int q = 0; q < Q; q++) {
            stdin = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(stdin.nextToken());

            switch(cmd) {
			
			case CMD_INIT:
				userSolution.init(); 
				okay = true; 
				break; 
				
			case CMD_ADD_BEAM:
				mLength = Integer.parseInt(stdin.nextToken()); 
				userSolution.addBeam(mLength);
				print(q, "addBeam", q, q, mLength);
				break;
				
			case CMD_REQUIRE_SINGLE:
				mHeight = Integer.parseInt(stdin.nextToken());
				ret = userSolution.requireSingle(mHeight);
				ans = Integer.parseInt(stdin.nextToken()); 
				print(q, "requireSingle", ans, ret, mHeight);
				if(ret != ans) 
					okay = false; 
				break;
				
			case CMD_REQUIRE_TWIN:
				mHeight = Integer.parseInt(stdin.nextToken());
				ret = userSolution.requireTwin(mHeight);
				ans = Integer.parseInt(stdin.nextToken()); 
				print(q, "requireTwin", ans, ret, mHeight);
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
    	if(ans != ret) System.err.println("---------------------오류------------------------");
    	System.out.println("["+q+"] " + cmd +  " " + ans + "=" + ret + " [" + Arrays.deepToString(o) + "]" );
    }

    public static void main(String[] args) throws Exception {
    	long start = System.currentTimeMillis();
    	int T, MARK;

        System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\상징물설치\\sample_input2.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");
        T = Integer.parseInt(stinit.nextToken());
        MARK = Integer.parseInt(stinit.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
        System.out.println("ms => " + (System.currentTimeMillis() - start));
    }
}