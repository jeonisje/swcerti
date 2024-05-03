package 기출문제.상징물설치;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.StringTokenizer;

class UserSolution {
	final int MAX_BEAM = 201;
	
	HashMap<Integer, Integer> oneCombi;
	HashMap<Integer, ArrayList<Integer []>> twoCombi;
	HashMap<Integer, ArrayList<Integer>> seqByNumber;
		
	int[] beam;
	int sequence;
	
    public void init() {
    	oneCombi = new HashMap<>();
    	twoCombi = new HashMap<>();
    	seqByNumber = new HashMap<>();
    	
    	beam = new int[MAX_BEAM];    	
    	
    	sequence = 0;
    	return; 
    }
    
    public void addBeam(int mLength) {
    	sequence++;
    	
    	beam[sequence] = mLength;    	
    	oneCombi.put(sequence, mLength);    	
    	ArrayList<Integer> list0 = seqByNumber.getOrDefault(mLength, new ArrayList<Integer>());
    	list0.add(sequence);
    	seqByNumber.put(mLength, list0);
    	
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
    	int maxLong = Integer.MAX_VALUE;
    	int subMaxLong = 0;
    	for(Entry<Integer, Integer> entry : oneCombi.entrySet()) {
    		subMaxLong = 0;
    		int[] used = new int[MAX_BEAM];    	
    		used[entry.getKey()] = 1;
    		
    		int seq0 = entry.getKey();
    		int num0 = entry.getValue();
    		int diff = mHeight - num0;
    		
    		if(diff == 0) {
    			subMaxLong = Math.max(subMaxLong, num0);
    			maxLong = Math.min(subMaxLong, maxLong);
    		}  else {
    			if(seqByNumber.containsKey(diff)) {
    				for(int seq : seqByNumber.get(diff)) {
    					if(seq != seq0) {
    						int num1 = diff;
    	    				subMaxLong = Math.max(subMaxLong, num0);
    	    				subMaxLong = Math.max(subMaxLong, num1);
    					}
    				}
    			}
    		}
    		if(subMaxLong != 0) {
    			maxLong = Math.min(maxLong, subMaxLong);
    		}
    		
    		if(twoCombi.containsKey(diff)) {    		
	    		ArrayList<Integer []> combiList = twoCombi.get(diff);	    		
	    		for(int i=0; i<combiList.size(); i++) {
	    			int seq1 = combiList.get(i)[0];
	    			int seq2 = combiList.get(i)[1];
	    			if(seq0 == seq1 || seq0 == seq2) continue;	  
	    			int num1= beam[seq1];
	    			int num2= beam[seq2];
	    			subMaxLong = Math.max(subMaxLong, num0);
	    			subMaxLong = Math.max(subMaxLong, num1);
	    			subMaxLong = Math.max(subMaxLong, num2);
	    			maxLong = Math.min(subMaxLong, maxLong);
	    		}	
    		}
    	}    	
    	
        return maxLong == Integer.MAX_VALUE ? -1 : maxLong;
    }
     
    public int requireTwin(int mHeight) {    	
    	ArrayList<Combination> combinations = new ArrayList<>();    
    	HashSet<Integer> usedDiff = new HashSet<>();
    	int[] used = new int[MAX_BEAM];
    	for(Entry<Integer, Integer> entry : oneCombi.entrySet()) {  
    		int seq0 = entry.getKey();
    		int num0 = entry.getValue();    
    		
    		int diff = mHeight - num0;    		
    		if(diff == 0) continue;
    		if(usedDiff.contains(diff)) continue;
    		usedDiff.add(diff);
    		if(twoCombi.containsKey(diff)) {    		
	    		ArrayList<Integer []> combiList = twoCombi.get(diff);
	    		for(int i=0; i<combiList.size(); i++) {
	    			int seq1 = combiList.get(i)[0];
	    			int seq2 = combiList.get(i)[1];	    		
	    			if(seq0 == seq1 || seq0 == seq2) continue;	    
	    			if(used[seq0] == 1 && used[seq1] == 1 && used[seq2] == 1) continue;
	    			Combination c = new Combination(seq0, seq1, seq2);
	    			combinations.add(c);	  
	    			setUsed(c, used);
	    		}
    		}
    	}    		
    		
		if(twoCombi.containsKey(mHeight)) { 
    		for(Integer[] seq : twoCombi.get(mHeight)) {
    			combinations.add(new Combination(seq[0], seq[1], 0));
    		}
		}
    		
    	if(seqByNumber.containsKey(mHeight)) {
    		for(int seq : seqByNumber.get(mHeight)) {
    			combinations.add(new Combination(seq, 0, 0));
    		}
    	}    	
    	
    	if(combinations.size() < 2) return -1;
    	
    	
    	int maxLong = 0;    	
    	if(combinations.size() == 2) {
    		used = new int[MAX_BEAM];
    		Combination combi1 = combinations.get(0);
    		Combination combi2 = combinations.get(1);
    		
    		maxLong = getMax(combi1);
    		setUsed(combi1, used);
    		used[combi1.seq1] = 1;    		
    		
    		if(!isAvailable(combi2, used)) return -1;     		
    		maxLong = Math.max(maxLong,getMax(combi2));
    		
    		return maxLong;
    	}
    	
    	maxLong = Integer.MAX_VALUE;
		for (int i = 0; i < combinations.size() - 1; i++) {
			used = new int[MAX_BEAM];
			setUsed(combinations.get(i), used);
			int subMax1 = getMax(combinations.get(i));
			for (int j = i + 1; j < combinations.size(); j++) {
				if (isAvailable(combinations.get(j), used)) {
					int subMax2 = getMax(combinations.get(j));
					maxLong = Math.min(maxLong, Math.max(subMax1, subMax2));
				}
			}
		}
    	
        return maxLong == Integer.MAX_VALUE ? -1 : maxLong; 
    }
    
    void setUsed(Combination combi, int[] used) {
    	used[combi.seq1] = 1;
		if(combi.seq2 != 0) {
			used[combi.seq2] = 1;
		}    		
		if(combi.seq3 != 0) {
			used[combi.seq3] = 1;
		}
    }
    
    boolean isAvailable(Combination combi, int[] used) {
    	return used[combi.seq1] == 0 && used[combi.seq2] == 0 && used[combi.seq3] == 0;
    }
    
    int getMax(Combination combi) {
    	int max = 0;
    	max = Math.max(max, beam[combi.seq1]);
    	max = Math.max(max, beam[combi.seq2]);
    	max = Math.max(max, beam[combi.seq3]);
    	return max;
    }   
    
    class Combination {
    	int seq1;
    	int seq2;
    	int seq3;
		public Combination(int seq1, int seq2, int seq3) {
			this.seq1 = seq1;
			this.seq2 = seq2;
			this.seq3 = seq3;
		}
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
    	//if(ans != ret) System.err.println("---------------------오류------------------------");
    	//System.out.println("["+q+"] " + cmd +  " " + ans + "=" + ret + " [" + Arrays.deepToString(o) + "]" );
    }

    public static void main(String[] args) throws Exception {
    	long start = System.currentTimeMillis();
    	int T, MARK;

        System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\상징물설치\\sample_input.txt"));
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