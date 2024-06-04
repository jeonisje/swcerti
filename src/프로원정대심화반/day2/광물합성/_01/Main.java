package 프로원정대심화반.day2.광물합성._01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;


class UserSolution {
	
	int fee;
	
	ArrayList<Material>[][] materials;
	Material[] finalMixed;
	
	public void init(int mShipFee) {
		this.fee = mShipFee;
		materials = new ArrayList[2][3];
		
		for(int i=0; i<2; i++) {
			for(int j=0; j<3; j++) {
				materials[i][j] = new ArrayList<>();
			}
		}
		
		finalMixed = new Material[3];
	}
	 
	public int gather(int mMineId, int mType, int mCost, int mContent) {
		Material m = new Material(mMineId, mCost, mContent);
		materials[mMineId][mType].add(m);
		return materials[mMineId][mType].size();
	}

	public Main.Result mix(int mBudget) {
		Main.Result res = new Main.Result();	
		
		int start = 0;
		int end = 1_000_001;
		int cost = 0;
		while(start <= end) {
			int mid = (start + end) / 2;
			int ret = test(mBudget, mid);
			if(ret == -1) {
				end = mid -1;
			} else {
				start = mid + 1;
				cost = ret;
			}
		}
		if(cost == 0) return res;
		
		for(int i=0; i<3; i++) {
			materials[finalMixed[i].id][i].remove(finalMixed[i]);
		}
		res.mContent = end;
		res.mCost = cost;
		
	  	return res; 	  
	}
	
	

	int test(int budget, int target) {
		Material[][] minCostArr = new Material[2][3];
		
		
		for(int i=0; i<2; i++) {
			for(int j=0; j<3; j++) {
				minCostArr[i][j] = new Material(0, budget + 1, 0);
				for(Material material : materials[i][j]) {
					if(material.content < target) continue;
					if(minCostArr[i][j].cost  < material.cost) continue;
					minCostArr[i][j] = material;
				}
			}
		}
		int mine0Mix = minCostArr[0][0].cost + minCostArr[0][1].cost + minCostArr[0][2].cost + fee;
		int mine1Mix = minCostArr[1][0].cost + minCostArr[1][1].cost + minCostArr[1][2].cost + fee;
		int mixCost = Math.min(minCostArr[0][0].cost, minCostArr[1][0].cost) 
					+ Math.min(minCostArr[0][1].cost, minCostArr[1][1].cost) 
					+ Math.min(minCostArr[0][2].cost, minCostArr[1][2].cost)
					+ fee * 2;
		
		int minCost = Math.min(Math.min(mine0Mix, mine1Mix), mixCost);
		
		if(minCost > budget) return  -1;
		
		if(minCost == mine0Mix) {
			finalMixed[0] = minCostArr[0][0];
			finalMixed[1] = minCostArr[0][1];
			finalMixed[2] = minCostArr[0][2];
		} else if(minCost == mine1Mix) {
			finalMixed[0] = minCostArr[1][0];
			finalMixed[1] = minCostArr[1][1];
			finalMixed[2] = minCostArr[1][2];
		} else {
			if( minCostArr[0][0].cost <  minCostArr[1][0].cost) {
				finalMixed[0] = minCostArr[0][0];
			} else {
				finalMixed[0] = minCostArr[1][0];
			}
			
			if( minCostArr[0][1].cost <  minCostArr[1][1].cost) {
				finalMixed[1] = minCostArr[0][1];
			} else {
				finalMixed[1] = minCostArr[1][1];
			}

			if( minCostArr[0][2].cost <  minCostArr[1][2].cost) {
				finalMixed[2] = minCostArr[0][2];
			} else {
				finalMixed[2] = minCostArr[1][2];
			}
		}
		
		return minCost;
	}



	class Material {
		int id;
		int cost;
		int content;
		public Material(int id, int cost, int content) {
			this.id = id;
			this.cost = cost;
			this.content = content;
		}
	}
}

public class Main
{
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

	private static boolean run(BufferedReader br) throws Exception
	{
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
				print(q, "mix", ans, ans2, in);
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
	
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대심화반\\day2\\광물합성\\sample_input.txt"));

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
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}