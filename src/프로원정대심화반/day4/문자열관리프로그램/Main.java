package 프로원정대심화반.day4.문자열관리프로그램;

import java.io.*;
import java.util.*; 

class UserSolution {	
	ArrayDeque<Character> deque;

	HashMap<String, Integer> hm; 
	int dir; // // 진행방향 0: 정방향 1: 역방향	
	void update(int val) {
		
		Iterator<Character> it;
		if(dir == 0) 
			it = deque.descendingIterator();
		else
			it = deque.iterator(); 
		
		String substr = "";
		int key = 0;
		for(int i = 0; i < Math.min(deque.size(), 4); i++) {
			char c = it.next();
			if(dir == 0) {
				
				substr = c + substr; 
			}
			else if(dir == 1) {
				substr += c; 
			}
			
			if(hm.get(substr) == null)
				hm.put(substr, 0);
			hm.put(substr, hm.get(substr) + val); 			
			
				
		}
	}
  
	public void init(char[] mainStr) {
		deque = new ArrayDeque<>();
		hm = new HashMap<>();
		dir = 0; 
		
		for(int i = 0; i < mainStr.length; i++) {
			deque.add(mainStr[i]);
			update(1);
		}
		
		return; 
	}
	
	public void pushBack(char[] newStr) {
		for(int i = 0; i < newStr.length; i++) {
			if(dir == 0) {
				deque.addLast(newStr[i]); 
				update(1);
			}
			else if(dir == 1) {
				deque.addFirst(newStr[i]);
				update(1); 
			}
		}
		return; 
	}
	
	public void popBack(int n) {
		for(int i = 0; i < n; i++) {
			if(dir == 0) {				
				update(-1); 
				deque.removeLast();
			}
			else if(dir == 1) {
				update(-1); 
				deque.removeFirst();
			}
		}
		return; 
	}
	
	public void reverseStr() {
		if(dir == 0) dir = 1;
		else dir = 0; 
		return; 
	}
	
	public int getCount(char[] subStr) {	
		String tar = ""; 
		if(dir == 0)
			tar = String.valueOf(subStr); 	
		else {			
			for(int i = 0; i < subStr.length; i++)
				tar += subStr[subStr.length - 1 - i];
		}		
		
		if(hm.get(tar) == null)
			return 0;
		return hm.get(tar);
	}
}

public class Main {

	private final static int CMD_INIT = 1;
	private final static int CMD_PUSH = 2;
	private final static int CMD_POP = 3;
	private final static int CMD_REVERSE = 4;
	private final static int CMD_COUNT = 5;

	private static BufferedReader br;
	private static StringTokenizer st;

	private final static UserSolution usersolution = new UserSolution();

	private static boolean run() throws Exception {
		
		boolean correct = false;
		int queryCnt;
		int ans;
		int userAns; 
	
		st = new StringTokenizer(br.readLine());
		queryCnt = Integer.parseInt(st.nextToken()); 

		for (int qc = 1; qc <= queryCnt; qc++) {

			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());

			if (cmd == CMD_INIT) 
			{
				char[] mainStr = st.nextToken().toCharArray();
				usersolution.init(mainStr);
				correct = true;
			}
			else if (cmd == CMD_PUSH) 
			{
				char[] str = st.nextToken().toCharArray();
				usersolution.pushBack(str);
				print(qc, "pushBack", qc, qc,  "dir="+ usersolution.dir, str);
			}
			else if (cmd == CMD_POP) 
			{
				int n = Integer.parseInt(st.nextToken());
				usersolution.popBack(n);
				print(qc, "popBack", qc, qc, "dir="+ usersolution.dir, n);
			}
			else if (cmd == CMD_REVERSE) 
			{
				usersolution.reverseStr();
				print(qc, "reverseStr", qc, qc, "dir="+usersolution.dir);
			}
			else if (cmd == CMD_COUNT) 
			{
				char[] str = st.nextToken().toCharArray();
				userAns = usersolution.getCount(str);
				ans = Integer.parseInt(st.nextToken());
				print(qc, "getCount", ans, userAns,  "dir="+ usersolution.dir,  str);
				if (userAns != ans)
					correct = false;
			}
		}
		return correct;
	}
	private static void print(int num, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("===================오류=======================");
		//System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception {
		Long start = System.currentTimeMillis();
		
		int T, MARK;
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대심화반\\day4\\문자열관리프로그램\\sample_input.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		T = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int tc = 1; tc <= T; tc++) {
			int score = run() ? MARK : 0;
			System.out.println("#" + tc + " " + score);
		}
		br.close();
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}