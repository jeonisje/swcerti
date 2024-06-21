package 기출문제.단어암기장._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

class UserSolution {
	int UNIT;
	int MAX = 50_001;
	int N, M;
	
	TreeMap<Word, Integer>[][] tm;
	TreeSet<Removed> removed;	// key 행번호, value 삭제회수
	
	HashMap<Integer, Integer> hm;
	
	int[] rowById;	// 단어별 행
	int[] colById;	// 단어별 duf
	int[][] curColByRow;
	
	int[] fulledRow;	// row별 꽉찬정도 확인	 
	int pageCount;
	
	int curPage;
	
	public void init(int N, int M) {
		this.N = N;
		this.M = M;
		
		UNIT = Math.max(10, N);
		pageCount = N / UNIT + 1;
		
		hm = new HashMap<>();
		tm = new TreeMap[pageCount][UNIT];
		for(int i=0; i<pageCount; i++) {
			for(int j=0; j<UNIT; j++) {
				tm[i][j] = new TreeMap<>((o1, o2) -> Integer.compare(o1.left, o2.left));
			}			
		}
		
		removed = new TreeSet<>((o1, o2) -> o1.col == o2.col ? Integer.compare(o1.col, o2.col) : Integer.compare(o1.row, o2.row));
		
		rowById = new int[MAX];		
		colById = new int[MAX];
		curColByRow = new int[pageCount][UNIT];
		
		fulledRow = new int[pageCount];
		
		curPage = 0;
		
		return;
	}

	// 50_000
	public int writeWord(int mId, int mLen) {
		hm.put(mId, mId);
		
		//int minRow = Integer.MAX_VALUE;
		Removed removeLoc = null;
		int minRow = Integer.MAX_VALUE;
		//int removedDiff = 0;
		// 삭제된 행이 있으면 삭제된 행을 먼저 검사
		if(!removed.isEmpty()) {
			boolean found = false;
			
			for(Removed r : removed) {
				if(r.len < mLen) continue;
				/*
				found = true;
				int page = r.row / UNIT;
				int rowPage = r.row % UNIT;
				tm[page][rowPage].put(new Word(mId, r.col, r.col + mLen -1), mId);
				*/
				//removedDiff = r.len - mLen;
				
				removeLoc = r;
				break;
				/*
				if(diff < 2) {					
					removed.remove(r);
					return r.row;
				}			
				removed.remove(r);
				cur = new Removed(r.row, r.col + mLen, diff);
				if(found) break;
				*/
			}
			/*
			if(cur != null) {
				removed.add(cur);
				return cur.row;
			}*/
		}	
		
		if(removeLoc != null) {
			minRow = removeLoc.row;
		}
		
		for(int i=0; i<pageCount; i++) {
			if(fulledRow[i] == UNIT) continue;
			for(int j=0; j<UNIT; j++) {	
				int row = i * pageCount + j;
				
				if(row >= minRow) {
					int page = removeLoc.row / UNIT;
					int rowPage = removeLoc.row % UNIT;
					
					tm[page][rowPage].put(new Word(mId, removeLoc.col, removeLoc.col + mLen -1), mId);
					removed.remove(removeLoc);		
					int diff = removeLoc.len - mLen + 1;
					if(diff >= 2) {
						removed.add(new Removed(removeLoc.row, removeLoc.col + mLen, diff));
					}
					return removeLoc.row;
				}
				
				
				if(row > N - 1) return -1;
			
				if(tm[i][j].isEmpty()) {
					tm[i][j].put(new Word(mId, 0, mLen-1), mId);
					curColByRow[i][j] = mLen - 1;
				
					int diff = M - mLen;
					if(diff < 2)
						fulledRow[i]++;
					if(fulledRow[i] == UNIT) 
						curPage++;
					
					rowById[mId] = row;
					colById[mId] = 0;
				
					return row;
				}
			
				int left = curColByRow[i][j] + 1;
				int right = left + mLen - 1;
				if(right > M - 1) continue;
				tm[i][j].put(new Word(mId, left, right), mId);
				curColByRow[i][j] = right;
				
				int diff = M - right - 1;
				if(diff < 2)
					fulledRow[i]++;
				if(fulledRow[i] == UNIT) 
					curPage++;
				
				rowById[mId] = row;
				colById[mId] = left;

				return row;
			}
		}
		
		return -1; 
	}
	
	// 5_000
	public int eraseWord(int mId) {
		if(!hm.containsKey(mId)) return -1;
		
		hm.remove(mId);
		
		int row = rowById[mId];
		int col = colById[mId];		
		int page = row / UNIT;
		int rowByPage = row % UNIT;
		
		fulledRow[page]--;
		
		Word word = tm[page][rowByPage].floorKey(new Word(mId, col, 0));
		tm[page][rowByPage].remove(word);
		
		removed.add(new Removed(row, word.left, word.right - word.left + 1));
		
		return row;
	}
	
	class Word {
		int id;
		int left;
		int right;
		public Word(int id, int left, int right) {		
			this.id = id;
			this.left = left;
			this.right = right;
		}
	}
	
	class Removed {
		int row;
		int col;
		int len;
		public Removed(int row, int col, int len) {		
			this.row = row;
			this.col = col;
			this.len = len;
		}		
	}
}

public class Main {
	private static final int CMD_INIT 	= 1;
	private static final int CMD_WRITE 	= 2;
	private static final int CMD_ERASE 	= 3;
	
	private static UserSolution userSolution = new UserSolution();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;
	
	private static boolean run(BufferedReader br) throws Exception {

		int query_num, mId, mLen, N, M; 
		int ans, ret; 
		boolean okay = false; 
		
		st = new StringTokenizer(br.readLine(), " ");
		int Q = Integer.parseInt(st.nextToken());

		for (int q = 0; q < Q; ++q) {

			st = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {

			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				userSolution.init(N, M); 
                okay = true; 
				break;
				
			case CMD_WRITE:
				mId = Integer.parseInt(st.nextToken());
				mLen = Integer.parseInt(st.nextToken());
				ret = userSolution.writeWord(mId, mLen);
				ans = Integer.parseInt(st.nextToken());
				print(q, "writeWord", ans, ret, mId, mLen);
				if(ans != ret) {
					okay = false; 
				}
				break;
				
			case CMD_ERASE:
				mId = Integer.parseInt(st.nextToken());
				ret = userSolution.eraseWord(mId); 
				ans = Integer.parseInt(st.nextToken());
				print(q, "eraseWord", ans, ret, mId);
				if(ans != ret) {
					okay = false; 
				}
				break;

			default:
				okay = false;
				break;
			}
		}
		return okay;
	}
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		if(ans!=ret)  System.err.println("----------------------오류--------------------");
		System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception {
		// System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\단어암기장\\sample_input3.txt"));


		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}
