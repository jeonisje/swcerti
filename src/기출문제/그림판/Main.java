package 기출문제.그림판;


import java.io.*;
import java.util.*;

class UserSolution {
	
	int[][] map;
	int[][] visited;
	int N;
	
	int idx;
	int cc;
	
	int[] directY = {-1, 1, 0, 0};
	int[] directX = {0, 0, -1, 1};

	public void init(int N, int L, char[] mCode) {
		this.N = N;
		idx = 0;
		map = new int[N][N];
		visited = new int[N][N];
		decoding(0, 0, N, mCode);
		return;
	}
	
	void decoding(int y, int x, int size, char[] code) {
		if(code[idx] == '0' || code[idx] == '1') {
			if(code[idx] == '1') {
				for(int i=y; i<y+size; i++) {
					for(int j=x; j<x+size; j++) {
						map[i][j] = code[idx] - '0'; 
					}
				}
			}
		} else if(code[idx] == '(') {
			idx++;
			int half = size / 2;
			decoding(y, x, half, code);
			decoding(y, x + half, half, code);
			decoding(y + half, x, half, code);
			decoding(y + half, x + half, half, code);			
		}
		idx++;
		return;
	}
	

	public int encode(char[] mCode) {
		idx = 0;
		encoding(0, 0, N, mCode);
		return idx;
	}
	
	void encoding(int y, int x, int size, char[] code) {
		if(!check(y, x, size)) {
			code[idx] ='(';
			idx++;
			int half = size / 2;
			encoding(y, x, half, code);
			encoding(y, x + half, half, code);
			encoding(y + half, x, half, code);
			encoding(y + half, x + half, half, code);
			code[idx] = ')';
			idx++;
		} else {
			code[idx] = (char)(map[y][x] + '0');			
			idx++;
		}
	}
	
	boolean check(int y, int x, int size) {
		int cur = map[y][x];
		for(int i=y; i<y+size; i++) {
			for(int j=x; j<x+size; j++) {
				if(map[i][j] != cur) return false;
			}
		}
		return true;
	}

	public void makeDot(int mR, int mC, int mSize, int mColor) {
		cc++;
		
		ArrayDeque<Dot> q = new ArrayDeque<>();
		q.add(new Dot(mR, mC, 1));
		visited[mR][mC] = cc;
		while(!q.isEmpty()) {
			Dot cur = q.remove();
			map[cur.y][cur.x] = mColor;
			if(cur.dist == mSize) continue;
			for(int i=0; i<4;  i++) {
				int ny = cur.y + directY[i];
				int nx = cur.x + directX[i];
				
				if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
				if(visited[ny][nx] == cc) continue;
				q.add(new Dot(ny, nx, cur.dist + 1));
				visited[ny][nx] = cc;
			}
		}
		
		return;
	}

	public void paint(int mR, int mC, int mColor) {
		if(map[mR][mC] == mColor) return;
		
		cc++;
		ArrayDeque<Dot> q = new ArrayDeque<>();
		q.add(new Dot(mR, mC, 0));
		visited[mR][mC] = cc;
		while(!q.isEmpty()) {
			Dot cur = q.remove();
			map[cur.y][cur.x] = mColor;
			for(int i=0; i<4; i++) {
				int ny = cur.y + directY[i];
				int nx = cur.x + directX[i];
				if(ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
				if(map[ny][nx] == mColor) continue;
				if(visited[ny][nx] == cc) continue;
				q.add(new Dot(ny, nx, 0));
				visited[ny][nx] = cc;
			}
		}
		
		
		return;
	}

	public int getColor(int mR, int mC) {
		return map[mR][mC];
	}
	
	class Dot {
		int y;
		int x;
		int dist;
		public Dot(int y, int x, int dist) {		
			this.y = y;
			this.x = x;
			this.dist = dist;
		}
	}
}

public class Main{
	private final static int CMD_INIT 		= 100;
	private final static int CMD_ENCODE 	= 200;
	private final static int CMD_MAKEDOT 	= 300;
	private final static int CMD_PAINT 		= 400;
	private final static int CMD_GETCOLOR 	= 500;
	private final static int MAX_N 			= 200001;
	private static char[] mCode;
	private static char[] sCode;

	private final static UserSolution usersolution = new UserSolution();
	private static BufferedReader br;

	private static void readcode(StringTokenizer sc, int L, char[] code) throws IOException {
		for (int i = 0; i < L;) {
			sc = new StringTokenizer(br.readLine());
			String buf = sc.nextToken();
			for(int j = 0; j < buf.length(); j++)
				code[i++] = buf.charAt(j);
		}
	}

	private static int mstrncmp(char[] a, char[] b, int L) {
		for (int i = 0; i < L; i++) {
			if (a[i] != b[i])
				return a[i] - b[i];
		}
		return 0;
	}

	private static boolean run() throws Exception {

		StringTokenizer st;

		int Q;
		st = new StringTokenizer(br.readLine());
		Q = Integer.parseInt(st.nextToken());

		int userAns, ans;
		int N, L, mR, mC, mSize, mColor;
		mCode = new char[MAX_N];
		sCode = new char[MAX_N];

		boolean isCorrect = false;
		int cmd;

		for (int q = 0; q < Q; ++q) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {
			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				L = Integer.parseInt(st.nextToken());
				readcode(st, L, mCode);
				usersolution.init(N, L, mCode);
				isCorrect = true;
				break;
			case CMD_ENCODE:
				userAns = usersolution.encode(mCode);
				ans = Integer.parseInt(st.nextToken());
				readcode(st, ans, sCode);
				int K = mstrncmp(sCode, mCode, ans);
				print(q, "encode", ans, userAns);
				//System.out.println("ret ==> " + String.valueOf(mCode).trim());
				//System.out.println("ans ==> " + String.valueOf(sCode).trim());
				if (userAns != ans || K != 0) {
					isCorrect = false;
				}
				break;
			case CMD_MAKEDOT:
				mR = Integer.parseInt(st.nextToken());
				mC = Integer.parseInt(st.nextToken());
				mSize = Integer.parseInt(st.nextToken());
				mColor = Integer.parseInt(st.nextToken());
				usersolution.makeDot(mR, mC, mSize, mColor);
				print(q, "makeDot", q, q, mR, mC, mSize, mColor);
				break;
			case CMD_PAINT:
				mR = Integer.parseInt(st.nextToken());
				mC = Integer.parseInt(st.nextToken());
				mColor = Integer.parseInt(st.nextToken());
				usersolution.paint(mR, mC, mColor);
				print(q, "paint", q, q, mR, mC, mColor);
				break;
			case CMD_GETCOLOR:
				mR = Integer.parseInt(st.nextToken());
				mC = Integer.parseInt(st.nextToken());
				userAns = usersolution.getColor(mR, mC);
				ans = Integer.parseInt(st.nextToken());
				print(q, "getColor", ans, userAns, mR, mC);
				if (userAns != ans) {
					isCorrect = false;
				}
				break;
			default:
				isCorrect = false;
				break;
			}
		}
		return isCorrect;
	}
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		
		int TC, MARK;

		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대심화반\\day1\\그림판\\sample_input.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}