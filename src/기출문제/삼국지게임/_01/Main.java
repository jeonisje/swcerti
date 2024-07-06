package 기출문제.삼국지게임._01;

import java.io.*;
import java.util.*; 

class UserSolution {
	int N;
	int totalCount;
	int[] parent;
	int[] countBySoldier;
	HashMap<String, Integer> nameToId;
	
	HashSet<Integer>[] enermies;
	
	int[] directY = {-1, -1, -1, 0, 0, 1, 1, 1};
	int[] directX = {-1, 0, 1, -1, 1, -1, 0, 1};
	
	public void init(int N, int[][] soldier, char[][][] monarch) {
		this.N = N;
		totalCount = N * N  * 2 + 1;
		parent = new int[totalCount];
		enermies = new HashSet[totalCount];
		
		for(int i=1; i< totalCount; i++) {
			enermies[i] = new HashSet<>();			
		}
		countBySoldier = new int[totalCount];
		nameToId = new HashMap<>();
		
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				int idx = i * N + j + 1;
				countBySoldier[idx] = soldier[i][j];
				String name = String.valueOf(monarch[i][j]);
				nameToId.put(name, idx);
				parent[idx] = idx + N * N;
				parent[idx + N * N ] = idx + N * N;				
			}
		}
		
		return;
	}
	
	public void destroy() {
		return; 
	}
	// 8_000
	public int ally(char[] monarchA, char[] monarchB) {
		int a = nameToId.get(String.valueOf(monarchA));
		int b = nameToId.get(String.valueOf(monarchB));
		
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return  -1;		
		if(enermies[pa].contains(pb)) return -2;
		if(enermies[pb].contains(pa)) return -2;
		
		union(a, b);
		
		
		return 1; 
	}
	// 8_000
	public int attack(char[] monarchA, char[] monarchB, char[] general) {
		int a = nameToId.get(String.valueOf(monarchA));
		int b = nameToId.get(String.valueOf(monarchB));
		
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) 
			return  -1;		
		boolean found = false;
		
		int row = (b - 1) / N;
		int col = (b - 1) % N;	
			
		for(int i=0; i<8; i++) {
			int nr = row + directY[i];
			int nc = col + directX[i];
			if(nr < 0 || nc < 0 || nr >= N || nc >= N) continue;
			
			int idx = nr * N + nc + 1;
			int pid = find(idx);
			
			if(pid == pa) {				
				found = true;
				break;
			}
			
		}	
		if(!found) return -2;
		
		enermies[pa].add(pb);
		enermies[pb].add(pa);
		
		int countA = 0;
		int countB = countBySoldier[b];
		
		for(int i=0; i<8; i++) {
			int nr = row + directY[i];
			int nc = col + directX[i];
			if(nr < 0 || nc < 0 || nr >= N || nc >= N) continue;
			
			int idx = nr * N + nc + 1;
			int pid = find(idx);
			
			if(pid == pa) {
				countA += countBySoldier[idx] / 2;
				countBySoldier[idx] -= countBySoldier[idx] / 2;
			} else if(pid == pb) {
				countB += countBySoldier[idx] / 2;
				countBySoldier[idx] -= countBySoldier[idx] / 2;
			}
		}
		
		int diff = countA - countB;
		if(diff <= 0) {
			countBySoldier[b] = -diff;
			return 0;		
		}
		
		nameToId.remove(String.valueOf(monarchB));
		
		String name = String.valueOf(general);
		nameToId.put(name, b);
		countBySoldier[b] = diff;
		parent[b] = pa;
		
		
		return 1; 
	}
	// 13_000
	public int recruit(char[] monarchA, int num, int sign) {
		int a = nameToId.get(String.valueOf(monarchA));
		
		if(sign == 0) {
			countBySoldier[a] +=  num;
			return countBySoldier[a]; 
		}	
		
		int pa = find(a);
		int total = 0;
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				int idx = i * N + j + 1;
				if(pa != find(idx)) continue;
				countBySoldier[idx] += num;
				total += countBySoldier[idx];
			}
		}
		
		return total; 
	}
	
	int find(int a) {
		if(parent[a] == a) return a;
		return parent[a] = find(parent[a]);
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		parent[pb] = pa;	
		
		for(int idx : enermies[pb]) {
			int pId = find(idx);
			if(enermies[pa].contains(pId)) continue;
			enermies[pa].add(pId);
			enermies[pId].add(pa);
		}
	}
}


public class Main {
	private static final int CMD_INIT 		= 100;
	private static final int CMD_DESTROY 	= 200;
	private static final int CMD_ALLY 		= 300;
	private static final int CMD_ATTACK 	= 400;
	private static final int CMD_RECRUIT 	= 500;

	private static UserSolution userSolution = new UserSolution();
	private final static int MAX_N = 25;
	private final static int MAX_L = 10; 

	static BufferedReader br;
	static StringTokenizer st;
	
	static int[][] soldier = new int[MAX_N][MAX_N];
	static char[][][] generalList = new char[MAX_N][MAX_N][MAX_L+1]; 

	public static boolean run(BufferedReader br) throws IOException {
		
		boolean isOK = false;
		int Q, cmd, result, check, N; 
		char[] monarchA = new char[11];
		char[] monarchB = new char[11];
		char[] general = new char[11]; 
		int sign;
		int num; 
		
		Q = Integer.parseInt(br.readLine());

		for (int q = 0; q < Q; q++) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			
			switch (cmd) {
			case CMD_INIT:
				N = Integer.parseInt(st.nextToken());
				for(int j = 0; j < N; j++) 
					for(int i = 0; i < N; i++) 
						soldier[j][i] = Integer.parseInt(st.nextToken());
				for(int j = 0; j < N; j++) {
					for(int i = 0; i < N; i++) {
						generalList[j][i] = st.nextToken().toCharArray();
					}
				}
				userSolution.init(N, soldier, generalList); 
				isOK = true; 
				break;
			case CMD_ALLY:
				monarchA = st.nextToken().toCharArray();
				monarchB = st.nextToken().toCharArray();
				result = userSolution.ally(monarchA, monarchB); 
				check = Integer.parseInt(st.nextToken());
				print(q, "ally", check, result, monarchA, monarchB);
				if (result != check)
					isOK = false;
				break;
			case CMD_ATTACK:
				monarchA = st.nextToken().toCharArray();
				monarchB = st.nextToken().toCharArray();
				general = st.nextToken().toCharArray(); 
				result = userSolution.attack(monarchA, monarchB, general); 
				check = Integer.parseInt(st.nextToken());
				print(q, "attack", check, result, monarchA, monarchB, general);
				if (result != check)
					isOK = false;
				break;
			case CMD_RECRUIT:
				monarchA = st.nextToken().toCharArray();
				num = Integer.parseInt(st.nextToken());
				sign = Integer.parseInt(st.nextToken());
				result = userSolution.recruit(monarchA, num, sign); 
				check = Integer.parseInt(st.nextToken());
				print(q, "recruit", check, result, monarchA, num, sign);
				if (result != check)
					isOK = false;
				break;
			default:
				isOK = false; 
				break;
			}
		}
		return isOK;
	}
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\삼국지게임\\sample_input.txt"));
		
		br = new BufferedReader(new InputStreamReader(System.in));
		
		st = new StringTokenizer(br.readLine());
		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int tc = 1; tc <= TC; ++tc) {
			boolean result = run(br);
			int score = result ? MARK : 0;
			System.out.println("#" + tc + " " + score);
		}
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}
