package 프로원정대.day3.wormslife._01;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	final int MAX_WORM = 100_001;	
	int[] infoByWorm;
	
	PriorityQueue<Worm>  pq;
	int totalCount;
	int passedYear;
	

	public void init() {
		infoByWorm = new int[MAX_WORM];
		pq = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.limittedYear, o2.limittedYear));
		totalCount = 0;
		passedYear = 0;
	}
	
	public void add(int id, int life) {
		if(!(infoByWorm[id] ==0 || infoByWorm[id] == -1)) return;
		pq.add(new Worm(id, life + passedYear));
		infoByWorm[id] = life + passedYear;
		totalCount++;
	}

	public int span(int year) {
		passedYear += year;
		int count = 0;
		while(!pq.isEmpty()) {			
			Worm cur = pq.peek();
			if(cur.limittedYear > passedYear) break;
			
			pq.remove();
			if(cur.limittedYear != infoByWorm[cur.id]) {
				continue;
			}
			count++;
			infoByWorm[cur.id] = 0;
		}
		
		totalCount -= count;
		return totalCount;
	}

	public int getLife(int id) {
		if(infoByWorm[id] == 0) return -1;
		return infoByWorm[id] - passedYear; 
	}
  
  	public int addLife(int id, int life) {
  		if(infoByWorm[id] == 0) return -1;
  		
  		infoByWorm[id] += life ; 
  		pq.add(new Worm(id, infoByWorm[id] ));
  		return infoByWorm[id] - passedYear; 
    }
  	
  	class Worm {
  		int id;
  		int limittedYear;
		public Worm(int id, int limittedYear) {		
			this.id = id;
			this.limittedYear = limittedYear;
		}		
  	}
}

public class Main {

	private final static int CMD_ADD 		= 1;
	private final static int CMD_SPAN	 	= 2;
	private final static int CMD_GET_LIFE 	= 3; 
    private final static int CMD_ADD_LIFE   = 4; 

	private static boolean run(BufferedReader br) throws IOException {

		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		int Q = Integer.parseInt(st.nextToken());

		usersolution.init();

		boolean isCorrect = true; 
		int cmd; 
		int id;
		int life; 
		int year; 
		int userAns;
		int ans; 

		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			switch(cmd) 
			{
				case CMD_ADD :
					id = Integer.parseInt(st.nextToken());
					life = Integer.parseInt(st.nextToken());
					usersolution.add(id, life);
					print(i, "add", i, i, id, life);
					break;
	
				case CMD_SPAN :
					year = Integer.parseInt(st.nextToken()); 
					userAns = usersolution.span(year); 
					ans = Integer.parseInt(st.nextToken());
					print(i, "span", ans, userAns, year);
					if(userAns != ans)
						isCorrect = false; 
					break;
					
				case CMD_GET_LIFE :
					id = Integer.parseInt(st.nextToken());
					userAns = usersolution.getLife(id); 
					ans = Integer.parseInt(st.nextToken());
					print(i, "getLife", ans, userAns, id);
					if(userAns != ans)
						isCorrect = false; 
					break;
                
                case CMD_ADD_LIFE :
					id = Integer.parseInt(st.nextToken());
                	life = Integer.parseInt(st.nextToken()); 
					userAns = usersolution.addLife(id, life); 
					ans = Integer.parseInt(st.nextToken());
					print(i, "addLife", ans, userAns, id, life);
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

	private final static UserSolution usersolution = new UserSolution();
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		if(ans!=ret) System.err.println("----------------오류-----------------");
		System.out.println("["+ q +"] " + cmd + ", " + ans +"=" + ret + "(" + Arrays.deepToString(o)+")") ;
	}

	public static void main(String[] args) throws Exception {

	    System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대\\day3\\wormslife\\sample_input.txt"));

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