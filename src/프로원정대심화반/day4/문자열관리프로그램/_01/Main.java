package 프로원정대심화반.day4.문자열관리프로그램._01;

import java.io.*;
import java.util.*; 

class UserSolution {	
	ArrayDeque<Character> deque;
	HashMap<Integer, Integer> hm;
	
	int dir; // 진행방향 0: 정방향 1: 역방향
	
	int[] hCode = {1, 100, 10_000, 1_000_000};
	int[] hCode2 = {100, 10_000, 1_000_000};
	
	public void init(char[] mainStr) {		
		deque  = new ArrayDeque<>();
		for(int i=0; i<mainStr.length; i++) {
			deque.add(mainStr[i]);
		}
		dir = 0;
		
		hm = new HashMap<>();
		init(mainStr, 1);
		return; 
	}	
	
	void init(char[] arr, int value) {
		int len = arr.length;
		for(int i=0; i<len; i++) {
			int key = 0;
			int end = Math.min(i+4, len);
			for(int j=i; j<end;j++) {				
				int diff = j - i;				
				key += (arr[j] - 'a' + 1) * hCode[diff];
				if(hm.containsKey(key)) {
					int cnt = hm.get(key);
					hm.put(key, cnt+value);
				} else {
					hm.put(key, 1);
				}
			}
		}
	}
	
	void hash(char[] arr, int value) {
		int len = arr.length;
		int limit = len - 3;
		int len2 = len;
		if(dir == 1) {
			len2 = limit;
		}
		
		for(int i=0; i<len2; i++) {
			int key = 0;
			int end = Math.min(i+4, len);
			for(int j=i; j<end;j++) {				
				int diff = j - i;				
				key += (arr[j] - 'a' + 1) * hCode[diff];
				if(dir == 0 && j < limit) 
					continue;
				if(hm.containsKey(key)) {
					int cnt = hm.get(key);
					hm.put(key, cnt+value);
				} else {
					hm.put(key, 1);
				}
			}
		}
	}
	
	public void pushBack(char[] newStr) {
		update(newStr);
		return; 
	}
	
	void update(char[] str) {
		int len1 = deque.size() < 3 ? deque.size() : 3;
		int len2 = len1 +  str.length;
		char[] arr = new char[len2];
		
		if(dir == 0) {
			for(int i=0; i<str.length; i++) {
				deque.addLast(str[i]);
			}
			Iterator<Character> it = deque.descendingIterator();
			int idx = len2-1;
			while(it.hasNext() && idx >= 0) {
				arr[idx] = it.next();
				idx--;
			}
			
		} else {
			for(int i=0; i<str.length; i++) {
				deque.addFirst(str[i]);
			}
			int idx = 0;
			Iterator<Character> it = deque.iterator();
			while(it.hasNext() && idx < len2) {
				arr[idx] = it.next();
				idx++;
			}
		}
		
		hash(arr, 1);
		/*
		if(dir == 0) {
			for(int i=0; i<len2; i++) {
				deque.addLast(arr[i]);
			}
		} else {
			for(int i=len2-1; i>=0; i--) {
				deque.addFirst(arr[i]);
			}			
		}*/
		
		return;
	}
	
	public void popBack(int n) {
		int len = n + 3;
		int count = 3;
		char[] arr = new char[len];
		if(dir == 0) {
			int idx = len - 1;
			for(int i=0; i<n; i++) {
				arr[idx--] = deque.removeLast();
			}
			
			Iterator<Character> it = deque.descendingIterator();
			
			while(it.hasNext() && count >0) {
				arr[idx--] = it.next();
				count--;
			}
			
		} else {
			int idx = 0;
			for(int i=0; i<n; i++) {
				arr[idx++] = deque.removeFirst();
			}
			
			Iterator<Character> it = deque.iterator();
			
			while(it.hasNext() && count > 0) {
				arr[idx++] = it.next();
				count--;
			}
		}
		
		hash(arr,-1);
		
		return; 
	}
	
	public void reverseStr() {
		if(dir == 0) dir = 1;
		else dir = 0;
		return; 
	}
	
	public int getCount(char[] subStr) {
		char[] arr = new char[subStr.length];
		if(dir == 0) {
			arr = subStr;
		} else {
			int idx = 0;
			for(int i=subStr.length-1; i>=0; i--) {
				arr[idx++] = subStr[i];
			}
		}
		
		int key = 0;
		for(int i=0; i<arr.length; i++) {
			key += (arr[i] - 'a' + 1) * hCode[i];
		}
		return hm.containsKey(key)  ? hm.get(key) : 0; 
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
		if(ans!=ret) System.err.println("===================오류=======================");
		System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception {
		Long start = System.currentTimeMillis();
		
		int T, MARK;
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대심화반\\day4\\문자열관리프로그램\\sample_input2.txt"));
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