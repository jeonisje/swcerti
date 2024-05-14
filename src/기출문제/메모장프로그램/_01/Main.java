package 기출문제.메모장프로그램._01;



import java.io.*;
import java.util.*;

class UserSolution {
	int H, W;
	
	ArrayList<Character> list;
	int[] countByChar;
	int current;
	
  
	void init(int H, int W, char mStr[]) {
		this.H = H;
		this.W = W;
		
		list = new ArrayList<>();
		countByChar = new int[200];		
		
		for(int i=0; i<mStr.length - 1 ; i++) {
			if(mStr[i] == '\0') break;
			list.add(mStr[i]);
			countByChar[mStr[i]]++;
		}
		
		current = 0;
		return; 
	}
	
	void insert(char mChar) {	
		countByChar[mChar]++;
		list.add(current, mChar);
		current++;
		return; 
	}

	char moveCursor(int mRow, int mCol) {		
		int loc = (mRow-1) * W + (mCol-1);
		int total = list.size();
		if(loc > total) {
			loc = total;
		}
		
		current = loc;
		if(list.size() == 0) return '$';
		if(current == list.size()) return '$';
		return list.get(current);		
	}

	int countCharacter(char mChar) {
		int[] dat = new int[200];
		
		if(current == 0) return countByChar[mChar];
		if(current == list.size()) return 0;
		
		// 1000 단위 유닛으로..
		
		if(current >= list.size() / 2) {
			for(int i=current; i<list.size(); i++) {
				dat[list.get(i)]++;
			}
			return dat[mChar]; 
		} else {
			for(int i=0; i<current; i++) {
				dat[list.get(i)]++;
			}
			
			return countByChar[mChar] - dat[mChar];
		}
	}
}

public class Main
{
	private final static int CMD_INIT       = 100;
	private final static int CMD_INSERT     = 200;
	private final static int CMD_MOVECURSOR = 300;
	private final static int CMD_COUNT      = 400;
	
	private final static UserSolution usersolution = new UserSolution();
	
	private static void String2Char(char[] buf, String str, int maxLen)
	{
		for (int k = 0; k < str.length(); k++)
			buf[k] = str.charAt(k);
			
		for (int k = str.length(); k <= maxLen; k++)
			buf[k] = '\0';
	}
	
	private static char[] mStr = new char[90001];
	
	private static boolean run(BufferedReader br) throws Exception
	{
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		int queryCnt = Integer.parseInt(st.nextToken());
		boolean correct = false;
		
		for (int q = 0; q < queryCnt; q++)
		{
			st = new StringTokenizer(br.readLine(), " ");
			
			int cmd = Integer.parseInt(st.nextToken());
			
			if (cmd == CMD_INIT)
			{
				int H = Integer.parseInt(st.nextToken());
				int W = Integer.parseInt(st.nextToken());
				
				String2Char(mStr, st.nextToken(), 90000);
				
				usersolution.init(H, W, mStr);
				correct = true;
			}
			else if (cmd == CMD_INSERT)
			{
				char mChar = st.nextToken().charAt(0);
				
				usersolution.insert(mChar);
				print(q, "insert", q, q, mChar);
			}
			else if (cmd == CMD_MOVECURSOR)
			{
				int mRow = Integer.parseInt(st.nextToken());
				int mCol = Integer.parseInt(st.nextToken());
				
				char ret = usersolution.moveCursor(mRow, mCol);
				
				char ans = st.nextToken().charAt(0);
				print(q, "moveCursor", ans, ret, mRow, mCol);
				if (ret != ans)
				{
					correct = false;
				}
			}
			else if (cmd == CMD_COUNT)
			{
				char mChar = st.nextToken().charAt(0);
				
				int ret = usersolution.countCharacter(mChar);
				
				int ans = Integer.parseInt(st.nextToken());
				print(q, "countCharacter", ans, ret, mChar);
				if (ret != ans)
				{
					correct = false;
				}
			}
		}
		return correct;
	}
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	static void print(int q, String cmd, char ans, char ret, Object...o) {
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		int TC, MARK;
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\메모장프로그램\\sample_input.txt"));
		//System.setIn(new java.io.FileInputStream("res/sample_input.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());
		
		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run(br) ? MARK : 0;
			
			System.out.println("#" + testcase + " " + score);
		}
		
		br.close();
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}