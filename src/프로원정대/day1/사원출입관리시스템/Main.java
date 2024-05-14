package 프로원정대.day1.사원출입관리시스템;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

class UserSolution {
	
	private final static int MAXNUM = 10000;
	final String ERROR = "ERROR";
	
	HashMap<Integer, String> map;
	String[] status;
	
	
	public void init() {
		map = new HashMap<>();
		status = new String[MAXNUM];
	}

	public String register(int id, String name)	{
		if(map.containsKey(id)) return ERROR; 
		
		map.put(id, name);
		status[id] = "OUT";
			
		
		return "OK"; 
	}

	public String[] inout(int id) {
		if(!map.containsKey(id)) return new String[]{ERROR, ERROR};
		
		if(status[id] == "IN") {
			status[id] = "OUT";
		} else {
			status[id] = "IN";
		}
		
		
		return new String[] {map.get(id), status[id]}; 
	}
}

public class Main {

	private final static int CMD_REGISTER 	= 1;
	private final static int CMD_INOUT	 	= 2;

	private static boolean run(BufferedReader br) throws IOException {

		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		int Q = Integer.parseInt(st.nextToken());

		usersolution.init();

		boolean isCorrect = true; 
		int cmd; 
		int id;
		String name; 

		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			switch(cmd) 
			{
				case CMD_REGISTER:
					id = Integer.parseInt(st.nextToken());
					name = st.nextToken();
					String userAns1 = usersolution.register(id, name);
					String ans1 = st.nextToken();
					if(!userAns1.equals(ans1)) 
						isCorrect = false; 
					break;
	
				case CMD_INOUT:
					id = Integer.parseInt(st.nextToken());
					String[] userAns2 = usersolution.inout(id); 
					String a1 = st.nextToken();
					String a2 = st.nextToken();  
					if (!userAns2[0].equals(a1) || !userAns2[1].equals(a2)) 
						isCorrect = false;
					break;
	
				default:
					isCorrect = false;
					break;
			}
		}
		return isCorrect;
	}

	private final static UserSolution usersolution = new UserSolution();

	public static void main(String[] args) throws Exception {

		// System.setIn(new java.io.FileInputStream("res/sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; testcase++) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
	}
}


/*

1 100
13
1 1000 TOM OK
1 1001 BOB OK
1 1001 JASON ERROR
1 1005 JASON OK
1 9000 JACKY OK
1 9001 SONEY OK
2 9000 JACKY IN
2 9000 JACKY OUT
2 5555 ERROR ERROR
2 1001 BOB IN
2 1000 TOM IN
1 1000 SHOW ERROR
2 1000 TOM OUT



*/