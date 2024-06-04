package 프로원정대심화반.day2.광물합성;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

class UserSolution {	
	int shipFee;
	ArrayList<Material>[][]  materials;
	
	Material[] mixed;
	
	public void init(int mShipFee) {
		this.shipFee = mShipFee;
		materials = new ArrayList[2][3];
		for(int i=0; i<2; i++) {
			for(int j=0; j<3; j++) {
				materials[i][j] = new ArrayList<>();
			}
		}
		
		mixed = new Material[3];		
	}
	 
	public int gather(int mMineId, int mType, int mCost, int mContent) {		
		Material material = new Material(mMineId, mCost, mContent);
		materials[mMineId][mType].add(material);
	    return materials[mMineId][mType].size();
	}

	public Main.Result mix(int mBudget) {
	  	Main.Result res = new Main.Result();	  	
	  	
	  	int start = 1; 		
	  	int end = 1_000_001; 
	  	int cost = 0;
	  	
	  	while(start <= end) {
	  		int mid = (start + end) / 2;
	  		int ret = test(mBudget, mid);
	  		if(ret != -1) {
	  			start = mid + 1;
	  			cost = ret;
	  		} else {
	  			end = mid - 1;
	  		}
	  	}
	  	
	  	if(cost == 0) return res;
	  	
	  	for(int i=0; i<3; i++) {
	  		materials[mixed[i].id][i].remove(mixed[i]);
	  	}
	  	res.mCost = cost;
	  	res.mContent = end;
	  	return res; 	  
	}
	
	int test(int budget, int target) {
		Material[][] minCost = new Material[2][3];
		for(int i=0; i<2; i++) {
			for(int j=0; j<3; j++) {
				minCost[i][j] = new Material(0, budget+1, 0);
				for(Material material : materials[i][j]) {
					if(material.content < target) continue;
					if(material.cost > minCost[i][j].cost) continue;
					minCost[i][j] = material;
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
			mixed[0] = minCost[0][0];
			mixed[1] = minCost[0][1];
			mixed[2] = minCost[0][2];
		} else if(minCostFinal == mix1) {
			mixed[0] = minCost[1][0];
			mixed[1] = minCost[1][1];
			mixed[2] = minCost[1][2];
		} else {
			if(minCost[0][0].cost < minCost[1][0].cost) mixed[0] = minCost[0][0];
			else mixed[0] = minCost[1][0];			
			if(minCost[0][1].cost < minCost[1][1].cost)	mixed[1] = minCost[0][1];
			else mixed[1] = minCost[1][1];
			if(minCost[0][2].cost < minCost[1][2].cost)	mixed[2] = minCost[0][2];
			else mixed[2] = minCost[1][2];
		}
			
		return minCostFinal;
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