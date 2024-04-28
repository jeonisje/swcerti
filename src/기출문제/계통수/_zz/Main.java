package 기출문제.계통수._zz;


import java.util.Scanner;

class UserSolution {
  
	public void init(char[] mRootSpecies) {
      	return; 
	}

	public void add(char[] mSpecies, char[] mParentSpecies) {
		return; 
	}
	
	public int getDistance(char[] mSpecies1, char[] mSpecies2) {
		return 0;
	}
	
	public int getCount(char[] mSpecies, int mDistance) {
		return 0;
	}
}

class Main {
	private static final int MAXL	= 11;

	private static final int CMD_INIT 			= 100;
	private static final int CMD_ADD 			= 200;
	private static final int CMD_GET_DISTANCE	= 300;
	private static final int CMD_GET_COUNT		= 400;

	private static void String2Char(String s, char[] b) {
        int n = s.length();
        for (int i = 0; i < n; ++i)
            b[i] = s.charAt(i);
        for (int i = n; i < MAXL; ++i)
        	b[i] = '\0';
    }
	
    private static UserSolution usersolution = new UserSolution();

    private static boolean run(Scanner sc) throws Exception {
    	int Q;
    	
    	char[] mRootSpecies = new char[MAXL];
    	char[] mSpecies = new char[MAXL];   	
    	char[] mParentSpecies = new char[MAXL];   	
    	char[] mSpecies1 = new char[MAXL];
    	char[] mSpecies2 = new char[MAXL];
    	
    	int mDistance;

    	int ret = -1, ans;

    	Q = sc.nextInt();

    	boolean okay = false;
    	
    	for (int q = 0; q < Q; ++q) {    				
            int cmd = sc.nextInt();
            
            switch(cmd) {
            case CMD_INIT:
            	String2Char(sc.next(), mRootSpecies);           	
            	usersolution.init(mRootSpecies);
            	okay = true;
            	break;
            case CMD_ADD:
            	String2Char(sc.next(), mSpecies);
            	String2Char(sc.next(), mParentSpecies);
            	usersolution.add(mSpecies, mParentSpecies);
            	break;
            case CMD_GET_DISTANCE:
            	String2Char(sc.next(), mSpecies1);
            	String2Char(sc.next(), mSpecies2);
            	ret = usersolution.getDistance(mSpecies1, mSpecies2);
            	ans = sc.nextInt();
            	if (ans != ret)
            		okay = false;
            	break;
            case CMD_GET_COUNT:
            	String2Char(sc.next(), mSpecies);
            	mDistance = sc.nextInt();
            	ret = usersolution.getCount(mSpecies, mDistance);
            	ans = sc.nextInt();
            	if (ans != ret)
            		okay = false;
            	break;
            default:
            	okay = false;
            	break;
            }
            
    	}
 
    	return okay;
    }
    
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		int TC, MARK;
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\계통수\\sample_input.txt"));
		
		Scanner sc = new Scanner(System.in);
		
		TC = sc.nextInt();
        MARK = sc.nextInt();

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(sc) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		sc.close();
		System.out.println("estimated=> " + (System.currentTimeMillis() - start));
	}
}