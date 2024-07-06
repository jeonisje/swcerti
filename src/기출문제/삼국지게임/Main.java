package 기출문제.삼국지게임;

import java.io.*;
import java.util.*; 

class UserSolution {
	int N;
	int soldierCount;
	int[] parent;
	int[] countBySoldier;
	String[] nameBySoldier;
	HashMap<String, Integer> nameToId;
	
	HashSet<Integer>[] enermies;
	HashSet<Integer>[] allies;
	
	int[][] map;
	int[] directY = {-1, -1, -1, 0, 0, 1, 1, 1};
	int[] directX = {-1, 0, 1, -1, 1, -1, 0, 1};
	
	public void init(int N, int[][] soldier, char[][][] monarch) {
		this.N = N;
		soldierCount = N * N + 1;
		parent = new int[soldierCount];
		enermies = new HashSet[soldierCount];
		allies = new HashSet[soldierCount];
		
		for(int i=1; i< soldierCount; i++) {
			parent[i] = i;
			enermies[i] = new HashSet<>();
			allies[i] = new HashSet<>();
			allies[i].add(i);
		}
		countBySoldier = new int[soldierCount];
		nameBySoldier = new String[soldierCount];
		nameToId = new HashMap<>();
		map = new int[N][N];
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				int idx = i * N + j + 1;
				countBySoldier[idx] = soldier[i][j];
				String name = String.valueOf(monarch[i][j]);
				nameBySoldier[idx] = name;
				nameToId.put(name, idx);
				map[i][j] = idx;
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
		//if(enermies[pa].contains(pb)) return -2;
		//if(enermies[pb].contains(pa)) return -2;
		
		for(int idx : enermies[pa]) {			
			//if(allies[pb].contains(idx)) return -2;
			int pid = find(idx);
			if(pid == pb) return -2;
		}

		for(int idx : enermies[pb]) {			
			int pid = find(idx);
			if(pid == pa) return -2;
		}
		
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
		int eRow = -1;
		int eCol = -1;
		for(Integer idx : allies[pa]) {
			int row = (idx - 1) / N;
			int col = (idx - 1) % N;
			
			for(int i=0; i<8; i++) {
				int nr = row + directY[i];
				int nc = col + directX[i];
				if(nr < 0 || nc < 0 || nr >= N || nc >= N) continue;
				
				//int pid = find(map[nr][nc]);
				
				if(map[nr][nc] == b) {
					eRow = nr;
					eCol = nc;
					found = true;
					break;
				}
			}
		}
		
		if(!found) return -2;
		
		enermies[pa].addAll(allies[pb]);
		enermies[pb].addAll(allies[pa]);
		
		int countA = 0;
		int countB = countBySoldier[b];
		
		for(int i=0; i<8; i++) {
			int nr = eRow + directY[i];
			int nc = eCol + directX[i];
			if(nr < 0 || nc < 0 || nr >= N || nc >= N) continue;
			
			int idx = map[nr][nc];
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
		if(b == pb && allies[pb].size() != 1) {
			int newP = 0;
			for(int idx : allies[pb]) {
				if(idx == b) continue;
				if(newP == 0) newP = idx;
				parent[idx] = newP;
			}
			
			allies[newP] = new HashSet<>();
			enermies[newP] = new HashSet<>();
			allies[newP].addAll(allies[pb]);
			enermies[newP].addAll(enermies[pb]);
			pb = newP;
		}
		
		for(int idx : enermies[pb]) {
			enermies[idx].remove(b);
		}
		
		for(int idx : allies[b]) {
			if(idx == b) continue;
			parent[idx] = pb;			
		}
		
		
		// 적 관계 리셋
		enermies[pa].remove(b);
		//enermies[a].remove(b);
		enermies[b] = new HashSet<>();
		//enermies[b].add(a);
		//enermies[b].addAll(enermies[pa]);
		
		// 동맹관계 리셋
		allies[pb].remove(b);
		allies[b] = new HashSet<>();
		allies[b].add(b);
		parent[b] = pa;
		allies[pa].add(b);
		if(pb != b)
			enermies[pb].add(b);
		
		String name = String.valueOf(general);
		nameBySoldier[b] = name;
		nameToId.put(name, b);
		countBySoldier[b] = diff;
		
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
		for(Integer idx : allies[pa]) {
			countBySoldier[idx] +=  num;
			total += countBySoldier[idx];
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
		
		
		if(allies[pa].size() >= allies[pb].size()) {
			parent[pb] = pa;
			allies[pa].addAll(allies[pb]);
			allies[pa].add(pb);
			enermies[pa].addAll(enermies[pb]);
			enermies[pb].addAll(enermies[pa]);
		} else {
			parent[pa] = pb;
			allies[pb].addAll(allies[pa]);
			allies[pb].add(pa);
			enermies[pb].addAll(enermies[pa]);
			enermies[pa].addAll(enermies[pb]);
		}
	}
	
	class Node {
		int id;
		int row;
		int col;
		public Node(int id, int row, int col) {		
			this.id = id;
			this.row = row;
			this.col = col;
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
		if(ans!=ret) System.err.println("----------------------오류--------------------");
		System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\삼국지게임\\sample_input5.txt"));
		
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
