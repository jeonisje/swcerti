package 기출문제.광물합성.정답소스._02;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

class UserSolution {
	int shipFee;
	ArrayList<Mineral>[][] minerals;
	Mineral[] mixedMinerals;  // 최종 조합
	
	public void init(int mShipFee) {
		minerals = new ArrayList[2][3];
		for(int i=0; i<2; i++) {
			for(int j=0; j<3; j++) {
				minerals[i][j]  = new ArrayList<>();
			}
		}
		
		this.shipFee = mShipFee;
		mixedMinerals = new Mineral[3]; 
	}
	 
	public int gather(int mMineId, int mType, int mCost, int mContent) {
		minerals[mMineId][mType].add(new Mineral(mMineId, mCost, mContent));
	    return minerals[mMineId][mType].size();
	}

	public Main.Result mix(int mBudget) {
	  	Main.Result res = new Main.Result();
	  	
	  	int start = 1; 		// 최소 content;
	  	int end = 1_000_001;  // 최대 content
	  	int cost = 0;
	  	
	  	while(start <= end) {
	  		int mid = (start + end) / 2;
	  		int ret = binarySearch(mBudget, mid);
	  		if(ret != -1) {
	  			start = mid + 1;
	  			cost = ret;
	  		} else {
	  			end = mid - 1;
	  		}
	  	}
	  	
	  	if(cost == 0) return res;
	  	
	  	for(int i=0; i<3; i++) {
	  		minerals[mixedMinerals[i].id][i].remove(mixedMinerals[i]);
	  	}
	  	res.mCost = cost;
	  	res.mContent = end;
	  	return res; 
	}
	
	int binarySearch(int budget, int target) {
		ArrayList<Integer> ret = new ArrayList<>();
		Mineral[][] minCost = new Mineral[2][3];
		for(int i=0; i<2; i++) {
			for(int j=0; j<3; j++) {
				minCost[i][j] = new Mineral(0, budget+1, 0);
				for(Mineral mineral : minerals[i][j]) {
					if(mineral.content < target) continue;
					if(mineral.cost > minCost[i][j].cost) continue;
					minCost[i][j] = mineral;
				}
			}
		}
		
		int mix0 = minCost[0][0].cost + minCost[0][1].cost + minCost[0][2].cost + this.shipFee; 
		int mix1 = minCost[1][0].cost + minCost[1][1].cost + minCost[1][2].cost + this.shipFee;
		int mix2 = Math.min(minCost[0][0].cost, minCost[1][0].cost) 
				+ Math.min(minCost[0][1].cost, minCost[1][1].cost) 
				+ Math.min(minCost[0][2].cost, minCost[1][2].cost) 
				+ this.shipFee * 2;
		
		int minCostFinal = Math.min(mix0, Math.min(mix1, mix2));
		if(minCostFinal > budget) {
			return -1;
		}
		
		if(minCostFinal == mix0) {
			// 0번 광산에서만 구했을 경우
			mixedMinerals[0] = minCost[0][0];
			mixedMinerals[1] = minCost[0][1];
			mixedMinerals[2] = minCost[0][2];
		} else if(minCostFinal == mix1) {
			// 1번 광산에서만 구했을 경우
			mixedMinerals[0] = minCost[1][0];
			mixedMinerals[1] = minCost[1][1];
			mixedMinerals[2] = minCost[1][2];
		} else {
			/// 섞여있을 경우 -> 작은 값으로 선택
			if(minCost[0][0].cost < minCost[1][0].cost) mixedMinerals[0] = minCost[0][0];
			else mixedMinerals[0] = minCost[1][0];			
			if(minCost[0][1].cost < minCost[1][1].cost)	mixedMinerals[1] = minCost[0][1];
			else mixedMinerals[1] = minCost[1][1];
			if(minCost[0][2].cost < minCost[1][2].cost)	mixedMinerals[2] = minCost[0][2];
			else mixedMinerals[2] = minCost[1][2];
		}
	
		// 가격정보 리턴
		return minCostFinal;
	}
	
	class Mineral {
		int id;
		int cost;
		int content;
		public Mineral(int id, int cost, int content) {		
			this.id = id;
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