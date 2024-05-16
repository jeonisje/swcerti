package 기출문제.광물합성.정답소스._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

class UserSolution {	
	int shipFee;	
	int id;
	int[] removed;
	
	ArrayList<Mineral> [][] mineralByMine;	
	Mineral[][] minCosts;
	Mineral[] mixedMineral;
	
	int[][] mineSet = {{0, 0, 0}, {1, 1, 1},  {0, 1, 0}, {0, 0, 1}, {0, 1, 1},  {1, 0, 1}, {1, 1, 0}, {1, 0, 0,}};
  
	public void init(int mShipFee) {		
		this.shipFee =  mShipFee;
		minCosts = new Mineral[2][3];
		mineralByMine = new ArrayList[2][3];
		
		for(int i=0; i<2; i++) {
			for(int j=0; j<3; j++) {
				mineralByMine[i][j] = new ArrayList<>();
			}
		}
		
		mixedMineral = new Mineral[3];
	}
	 
	public int gather(int mMineId, int mType, int mCost, int mContent) {
		mineralByMine[mMineId][mType].add(new Mineral(mMineId, mType, mCost, mContent));
		return mineralByMine[mMineId][mType].size();
	}

	
	public Main.Result mix(int mBudget) {
		Main.Result res = new Main.Result();
		int start = 0;
		int end = 1_000_001;
		
		while(start < end) {
			int mid = (start + end) / 2;
			// 구성가능하지 체크하려고 하는 함유량
			Arrays.fill(minCosts[0], new Mineral(0, 0, mBudget + 1, 0));
			Arrays.fill(minCosts[1], new Mineral(0, 0, mBudget + 1, 0));
			// 광산, 타입별 기준함유량 이상의 광물 중 최저 가격의 광물 확인
			for(int mine = 0; mine < 2; mine++) {
				for(int type = 0; type < 3; type++) {
					for(Mineral mineral : mineralByMine[mine][type]) {
						if(mineral.content < mid) {
							continue;
						}
						if(mineral.cost < minCosts[mine][type].cost) {
							minCosts[mine][type] = mineral;
						}
					}
				}
			}
			int minCost = mBudget + 1;
			// 광산 조합별 비용 계산
			for(int i=0; i<8; i++) {
				int cost = minCosts[mineSet[i][0]][0].cost + minCosts[mineSet[i][1]][1].cost + minCosts[mineSet[i][2]][2].cost + this.shipFee;;
				if(i > 1) {
					cost += this.shipFee;
				}
				if(cost < minCost) {
					minCost = cost;
					setMineral(mineSet[i]);				
				}
			}
			
			// 최저가격 예선이내인지 확인
			if(minCost <= mBudget) {
				res.mContent = mid;
				res.mCost = minCost;
				
				start = mid + 1;
				continue;
			}
			end = mid;
		}
		
		// 광물합성이 가능한 경우 사용돤 광물정보 삭제
		if(res.mCost > 0) {
			for(Mineral mineral : mixedMineral) {
				mineralByMine[mineral.mine][mineral.type].remove(mineral);
			}
		}
		
	  	return res; 
	}
	
	void setMineral(int[] mine) {
		mixedMineral[0] = minCosts[mine[0]][0];
		mixedMineral[1] = minCosts[mine[1]][1];
		mixedMineral[2] = minCosts[mine[2]][2];
	}
	
	
	
	class Mineral {
		int mine;
		int type;
		int cost;
		int content;
		public Mineral(int mine, int type, int cost, int content) {		
			this.mine = mine;
			this.type = type;
			this.cost = cost;
			this.content = content;
		}
	}
}

public class Main {
	private static final int CMD_INIT				= 0;
	private static final int CMD_GATHER				= 1;
	private static final int CMD_MIX				= 2;

	public static final class Result
	{
		int mCost;
		int mContent; 
		
		Result() {
			mCost = 0; 
			mContent = 0; 
		}
	}

	private static UserSolution usersolution = new UserSolution();

	private static boolean run(BufferedReader br) throws Exception	{
		int Q;
	
        StringTokenizer stdin = new StringTokenizer(br.readLine(), " ");

		Q = Integer.parseInt(stdin.nextToken());
		
		boolean okay = false;
		
		for (int q = 0; q < Q; ++q)
		{
            stdin = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(stdin.nextToken());
			int ret, ans, ans2, in, in2, in3, in4;
			Result res;
			
			switch(cmd)
			{
			case CMD_INIT:
				in = Integer.parseInt(stdin.nextToken());
				usersolution.init(in);
				okay = true;
				break;
			case CMD_GATHER:
				in = Integer.parseInt(stdin.nextToken());
				in2 = Integer.parseInt(stdin.nextToken());
				in3 = Integer.parseInt(stdin.nextToken());
				in4 = Integer.parseInt(stdin.nextToken());
				ret = usersolution.gather(in, in2, in3, in4);
				ans = Integer.parseInt(stdin.nextToken());
				print(q, "gather", ans, ret, in, in2, in3, in4);
				if (ret != ans)
					okay = false;
				break;
			case CMD_MIX:
				in = Integer.parseInt(stdin.nextToken());
				res = usersolution.mix(in);
				ans = Integer.parseInt(stdin.nextToken());
				ans2 = Integer.parseInt(stdin.nextToken());
				print(q, "mix", ans, res.mCost, ans2, res.mContent, in, res.mCost, res.mContent);
				if (res.mCost != ans || res.mContent != ans2)
					okay = false;
				break;
			default:
				okay = false;
				break;
			}
			
		}
 
		return okay;
	}
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception	{
		long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\광물합성\\sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());
		
		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		
		br.close();
		System.out.println("estimated=> " + (System.currentTimeMillis() - start));
	}
}