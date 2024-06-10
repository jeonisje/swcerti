package 기출문제.딱지게임;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

class Solution {
  
    private final static int LM = 20003; 
    private static int[] PARENT = new int[LM]; 
    
    public int Find(int x) {
    	if(PARENT[x] == x) return x;
    	return PARENT[x] = Find(PARENT[x]);
    }
    
    public void Union(int x, int y, int pid) {
    	x = Find(x);
    	y = Find(y);
    	if(x == y) return;
    	PARENT[x] = y; 
    }
    
    public void init(int N, int M) {
      	return; 
    }
    
    public int add(int row, int col, int size, int pid) {
    	return 0; 
    }
    
    public int get(int row, int col) {
    	return 0; 
    }
}
public class Main {

	private static int seed = 5;

	private final static Solution userSolution = new Solution();

	private static int board_size, area_size, query_cnt;
	public static BufferedReader br;
	public static StringTokenizer st;

	private static int pseudo_rand() {
		seed = seed * 214013 + 2531011;
		return (seed >> 16) & 0x7FFF;
	}

	private static int run(int score) throws Exception {
		st = new StringTokenizer(br.readLine());
		seed = Integer.parseInt(st.nextToken());
		board_size = Integer.parseInt(st.nextToken());
		area_size = Integer.parseInt(st.nextToken());
		query_cnt = Integer.parseInt(st.nextToken());
		userSolution.init(board_size, area_size);
      
		for (int q = 0; q < query_cnt; q++) {
			st = new StringTokenizer(br.readLine());
			int r, c, size, user_ans, correct_ans;
			for (int i = 1; i <= 2; i++)
			{
				size = (pseudo_rand() * pseudo_rand()) % area_size + 1;
				r = (pseudo_rand() * pseudo_rand()) % (board_size - size + 1);
				c = (pseudo_rand() * pseudo_rand()) % (board_size - size + 1);

				user_ans = userSolution.add(r, c, size, i);
				correct_ans = Integer.parseInt(st.nextToken());
				print(q, "add", correct_ans, user_ans, r, c, size, i);

				if (user_ans != correct_ans)
					score = 0;
			}
			r = (pseudo_rand() * pseudo_rand()) % board_size;
			c = (pseudo_rand() * pseudo_rand()) % board_size;
			user_ans = userSolution.get(r, c);
			correct_ans = Integer.parseInt(st.nextToken());
			print(q, "get", correct_ans, user_ans, r, c);
			if (user_ans != correct_ans)
				score = 0;
		}
		return score;
	}
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
    	//if(ans != ret) System.err.println("---------------------오류------------------------");
    	//System.out.println("["+q+"] " + cmd +  " " + ans + "=" + ret + " [" + Arrays.deepToString(o) + "]" );
    }

	public static void main(String[]args) throws Exception {
		
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\딱지게임\\sample_input.txt"));
		
		int tc, score;
		br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		tc = Integer.parseInt(st.nextToken());
		score = Integer.parseInt(st.nextToken());

		for (int t = 1; t <= tc; t++) {
			int t_score = run(score);
			System.out.println("#" + t + " " + t_score);
		}
		br.close();
		 System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}