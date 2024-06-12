package 기출문제.딱지게임._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    
    int N, M;    
    int UNIT_CNT;
    
    ArrayList<Square>[][] summaryMap;    
        
    int[] countByPid;
    int[] countByPlayer;
    int[] tempCountByPlayer;
    int[] ownedById;
    int[] used;
        
    int cc;
    int squareId;
    
    public void init(int N, int M) {
    	squareId = 0;
    	cc = 0;
    	
    	this.N = N;
    	this.M = M;
    	
    	UNIT_CNT = N / M;
    	
    	countByPid = new int[LM];
    	ownedById = new int[LM];
    	used = new int[LM];
    	countByPlayer = new int[3];
    	tempCountByPlayer = new int[3];
    	
    	summaryMap = new ArrayList[UNIT_CNT][UNIT_CNT];
    	for(int i=0; i<UNIT_CNT; i++) {
    		for(int j=0; j<UNIT_CNT; j++) {
    			summaryMap[i][j] = new ArrayList<>();
    		}
    	}
    	
      	return; 
    }
    
    public int add(int row, int col, int size, int pid) {
    	squareId++;
    	cc++;
    
    	int locY = row / M;
    	int locX = col / M;
    	
    	int startY = Math.max(locY - 1, 0);
    	int startX = Math.max(locX - 1, 0);
    	int endY = Math.min(locY + 1, UNIT_CNT - 1);
    	int endX = Math.min(locX + 1, UNIT_CNT - 1);
    	
    	int player = 1;
    	if(pid == 1) {
    		player = 2;
    	}    	
    	
    	PARENT[squareId] = squareId;
    	countByPid[squareId] = 1;
    	ownedById[squareId] = pid;
    	
    	tempCountByPlayer[player] = 0;
    	tempCountByPlayer[pid] = 1;   	    
    	
    	ArrayList<Integer> overlap = new ArrayList<>();
    	for(int i=startY; i<=endY; i++) {
    		for(int j=startX; j<=endX; j++) {
    			for(Square square : summaryMap[i][j]) {
    				if(square.row >= row + size|| square.col >= col + size || square.row + square.size <= row || square.col + square.size <= col) continue;
    				//if(square.col >= col + size) continue;
    				//if(square.row + square.size <= row) continue;
    				//if(square.col + square.size <= col) continue;
    				
    				int sePid = Find(square.id);
    				if(used[sePid] == cc) continue;
    				overlap.add(sePid);
    				used[sePid] = cc;
    			}
    		}
    	}
    	summaryMap[locY][locX].add(new Square(squareId, row, col, size));
    	
    	for(int sqPid : overlap) {
    		Union(sqPid, squareId, pid);
    		countByPid[squareId] += countByPid[sqPid];
    		if(ownedById[sqPid] == pid) continue;
    		tempCountByPlayer[player] += countByPid[sqPid];
    		tempCountByPlayer[pid] += countByPid[sqPid];    		
    	}
    	
    	countByPlayer[player] -= tempCountByPlayer[player];
    	countByPlayer[pid] += tempCountByPlayer[pid];
    	
    	return countByPlayer[pid]; 
    }
    
    public int get(int row, int col) {    	
    	int locY = row / M;
    	int locX = col / M;
    	
    	int startY = Math.max(locY - 1, 0);
    	int startX = Math.max(locX - 1, 0);
    	int endY = Math.min(locY + 1, UNIT_CNT - 1);
    	int endX = Math.min(locX + 1, UNIT_CNT - 1);
    	
    	for(int i=startY; i<=endY; i++) {
    		for(int j=startX; j<=endX; j++) {
    			//for(Square square : summaryMap[i][j]) {
    			for(int k=0; k<summaryMap[i][j].size(); k++) {
    				Square square = summaryMap[i][j].get(k);
    				if(square.row > row || square.col > col || square.row + square.size <= row || square.col + square.size <= col) continue;
    				//if(square.row > row) continue;
    				//if(square.col > col) continue;
    				//if(square.row + square.size <= row) continue;
    				//if(square.col + square.size <= col) continue;
    				
    				int pid = Find(square.id);
    				return ownedById[pid];
    			}
    		}
    	}
    	
    	return 0; 
    }
    
    class Square {
    	int id;
    	int row;
    	int col;
    	int size;
		public Square(int id, int row, int col, int size) {		
			this.id = id;
			this.row = row;
			this.col = col;
			this.size = size;
		}
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
