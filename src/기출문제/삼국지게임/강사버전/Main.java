package 기출문제.삼국지게임.강사버전;

import java.io.*;
import java.util.*; 
import java.io.*;
import java.util.*; 

class UserSolution {
	
	static int[] parent;
	static int[] soldiers; 
	// key : monarch name
	// value : monarch -> 부여한  id 
	static HashMap<String, Integer>hm;
	// 적대관계 (hashset)
	// index : id
	// key : 이 id와 적대관계에 있는 나라들
	// value : 안씀
	static HashMap<Integer, Integer>[] enemies; 
	static int mapSize; 
	
	static int[]ydir = {-1, 1, 0, 0, -1, -1, 1, 1};
	static int[]xdir = {0, 0, -1, 1, -1, 1, -1, 1}; 
	
	static int find(int node) {
		if(node == parent[node])
			return node;
		return parent[node] = find(parent[node]); 
	}
	
	static int enlist(int y, int x, int group) {
		int cnt = 0; 
		for(int i = 0; i < 8; i++) {
			int ny = y + ydir[i];
			int nx = x + xdir[i];
			if(ny < 0 || nx < 0 || ny >= mapSize || nx >= mapSize)
				continue; 
			// 여기 일단은 뭔가는 무조건 있을것. 
			// 이 국가의 id = ny * mapsize + nx
			int id = ny * mapSize + nx; 
			int pid = find(id); 
			// 만약 이 그룹에 소속된 동맹이라면
			if(pid == group) {
				// 이 id가 가지고 있는 병사에서 절반을 데려올겁니다. 
				cnt += soldiers[id] / 2; // 7
				soldiers[id] -= soldiers[id] / 2; // 8
			}
		}
		return cnt; 
	}
	
	public void init(int N, int[][] soldier, char[][][] monarch) {
		parent = new int[N * N * 2];
		soldiers = new int[N * N * 2];
		enemies = new HashMap[N * N * 2];
		for(int i = 0; i < N * N * 2; i++) {
			parent[i] = i;
			enemies[i]= new HashMap<>(); 
		}
		hm = new HashMap<>();

		// 주어진 정보들을 하나씩 삽입
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				int id = i * N + j; 
				String monarchName = String.valueOf(monarch[i][j]).trim(); 
				hm.put(monarchName, id);
				// 변하지 않는, 지역에 고정된 최상위 parent로 이어줄겁니다. 
				parent[id] = id + N * N; 
				// 대장은 자기 자신을 가리켜야 하니까
				parent[id + N * N] = id + N * N;
				soldiers[id] = soldier[i][j]; 
			}
		}
		mapSize = N;
		return;
	}
	
	public void destroy() {
		return; 
	}
	
	public int ally(char[] monarchA, char[] monarchB) {
		int de = 1; 
		
		// 동맹을 맺을거니까... 일단 대장 찾아가야 합니다.
		String ma = String.valueOf(monarchA).trim();
		String mb = String.valueOf(monarchB).trim();
		
		// id가져오기 
		int a = hm.get(ma);
		int b = hm.get(mb); 
		
		// 소속 찾기
		int pa = find(a);
		int pb = find(b); 
		
		// #1. 이미 둘이 동맹 관계라면 -1 return
		if(pa == pb)
			return -1; 
		
		// #2. 둘이 적대 관계라면 -> 동맹을 할 수 없다! -2 return
		// 양방향으로 적일것. (a는 b의 적이다, b는 a의 적이다)
		if(enemies[pa].get(pb) != null)
			return -2; 
		
		// 동맹이 이루어진다! 
		// 소속이 합병
		parent[pb] = pa; 
		// 이제부터 pa => 이 소속의 대장이 되는것. 
		// 그러니까 이제부터 pb, 너의 적들은 모두 나의 적이다!
		// pb의 enemy list를 모두 pa쪽에다 추가
		for(Map.Entry<Integer, Integer>ent : enemies[pb].entrySet()) {
			// pb의 적들 리스트를 모두 보면서
			int enemy = ent.getKey(); 
			// 적 정보를 알았는데.. 이 적들도 사실은 소속이 될것이다.
			int pe = find(enemy); 
			//이제, pa와 pe는 적대관계가 되는겁니다. 
			// hashmap은 고유하니까 -> 딱히 중복되는거 고려할것은 없음
			if(enemies[pa].get(pe) != null)
				continue; 
			enemies[pa].put(pe, 1);
			enemies[pe].put(pa, 1);
		}
		return 1; 
	}
	
	public int attack(char[] monarchA, char[] monarchB, char[] general) {
		
		int de = 1; 
		// 전쟁
		// 일단 대장 찾아가야 합니다.
		String ma = String.valueOf(monarchA).trim();
		String mb = String.valueOf(monarchB).trim();
		String g = String.valueOf(general).trim(); 
		
		if(g.equals("igija"))
			de = 1; 
				
		// id가져오기 
		int a = hm.get(ma);
		int b = hm.get(mb); 
				
		// 소속 찾기
		int pa = find(a);
		int pb = find(b); 
				
		// #1. 이미 둘이 동맹 관계라면 -1 return
		if(pa == pb)
			return -1; 
		
		// #2. return -2 -> 공격을 받는 지역의 주변을 살펴봐야 합니다.
		// 이 주변에 A 국가의 동맹국이 하나도 없다면 = -2 
		// id = y * N + x 
		int y = b / mapSize;
		int x = b % mapSize; 
		int flag = 0; 
		// 주변 8방향 확인 
		for(int i = 0; i < 8; i++) {
			int ny = y + ydir[i];
			int nx = x + xdir[i];
			if(ny < 0 || nx < 0 || ny >= mapSize || nx >= mapSize)
				continue; 
			// 여기 일단은 뭔가는 무조건 있을것. 
			// 이 국가의 id = ny * mapsize + nx
			int id = ny * mapSize + nx; 
			// 이제 이 국가의 소속을 확인한다.
			int pid = find(id);
			// 공격하는 나라의 소속과 비교했을떄 -> "같은 소속이라면" -> 동맹국이 인접하고 있다! 
			if(pid == pa) {
				flag = 1; // 즉, 전쟁은 발생한다!
				break; 
			}
		}
		
		// 8방향을 다 봤는데, A의 동맹국이 없다면 
		if(flag == 0)
			return -2; 
		
		// 전쟁은 진행된다!
		// A동맹국과 B동맹국은 이제 서로 원수지간
		enemies[pa].put(pb, 1);
		enemies[pb].put(pa, 1);
		
		// 공격을 성공하면 1
		// 공격을 실패하면 0 
		int res = 0;
		// 각 국가가 전쟁을할때, 몇명의 병사들을 가지고 전쟁을하는가? 
		// 다시 주변을 둘러보면서, 각 국가의 동맹국에서 병사를 절반씩 징병해옵니다.
		int acnt = enlist(y, x, pa);
		int bcnt = enlist(y, x, pb) + soldiers[b]; // 공격 당하는 곳 => 여기에 포함된 병사를 전부 씁니다.
		
		// #1. 공격 성공한다 = acnt > bcnt 
		if(acnt > bcnt) {
			// 군주 B는 처형이 된다
			hm.remove(mb); 
			// enemies[b].clear(); // 최상 동맹국의 적들만 알면 된다
			// 그리고, 이 b국가는 어차피 바로 A국가의 동맹으로 들어갈꺼니까... 
			hm.put(g, b);
			// * 주의 b동맹국이 a동맹국 아래로 들어가는게 아니라
			parent[b] = pa; 
			// 이 id가 가지게 되는 병사 수만 이제 정리
			soldiers[b] = acnt - bcnt; 
			res = 1; // 공격 성공
		} 
		
		else {
			// 방어 성공했으니까 -> 군사 수만 정리 
			soldiers[b] = bcnt - acnt; 
			// res = 0; 
		}
		return res; 
	}
	
	public int recruit(char[] monarchA, int num, int sign) {
		String ma = String.valueOf(monarchA).trim(); 
		int id = hm.get(ma);
		if(sign == 0) {
			// 이 id 땅에만 num명을 모집한다. 
			soldiers[id] += num;
			return soldiers[id]; 
		}
		else {
			int cnt = 0; 
			// 이 id가 속한 모든 동맹 국가가 num명만큼을 모집
			for(int i = 0; i < mapSize; i++) {
				for(int j = 0; j < mapSize; j++) {
					int xid = i * mapSize + j;
					// id와 동일한 동맹국이라면
					if(find(id) == find(xid)) {
						soldiers[xid] += num;
						cnt += soldiers[xid]; 
					}
				}
			}
			return cnt; 
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
