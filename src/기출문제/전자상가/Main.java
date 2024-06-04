package 기출문제.전자상가;


import java.io.*;
import java.util.*;

class UserSolution {
	int charge;
	ArrayList<Parts>[][] parts;	
	
	void init(int mCharge) {
		this.charge = mCharge;
		parts = new ArrayList[2][3];
		for(int i=0; i<2; i++) {
			for(int j=0; j<3; j++) {
				parts[i][j] = new ArrayList<>();
			}
		}
		return;
	}
	
	int stock(int mType, int mPrice, int mPerformance, int mPosition) {
		Parts p = new Parts(mPrice, mPerformance);
		parts[mPosition][mType].add(p);
		return parts[mPosition][mType].size();
	}

	Main.Result order(int mBudget) {
		Main.Result res = new Main.Result();
		
		int start = 1;
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
		
		res.mPrice = cost;
		res.mPerformance = end;
		return res;
	}
	
	int test(int mBudget, int target) {
		Parts[][] minCombi = new Parts[2][3];
		for(int i=0; i<2; i++) {
			for(int j=0; j<3; j++) {
				minCombi[i][j] = new Parts(mBudget + 1, 0);
				for(Parts p : parts[i][j]) {
					if(p.performance < target) continue;
					if(minCombi[i][j].price < p.price) continue;
					minCombi[i][j] = p;
				}
			}
		}
		
		int mix0 = minCombi[0][0].price + minCombi[0][1].price + minCombi[0][2].price;
		int mix1 = minCombi[1][0].price + minCombi[1][1].price + minCombi[1][2].price;
		int mix01 = Math.min(minCombi[0][0].price, minCombi[1][0].price) 
				  + Math.min(minCombi[0][1].price, minCombi[1][1].price)
				  + Math.min(minCombi[0][2].price, minCombi[1][2].price) + charge;
		
		int minPrice = Math.min(Math.min(mix0, mix1), mix01);
		
		if(minPrice > mBudget) 
			return -1;
		
		return minPrice;
	}

	class Parts {
		int price;
		int performance;
		public Parts(int price, int performance) {		
			this.price = price;
			this.performance = performance;
		}
	}
}


public class Main
{
	private static final int CMD_INIT				= 0;
	private static final int CMD_STOCK				= 1;
	private static final int CMD_ORDER				= 2;

	public static final class Result
	{
		int mPrice;
		int mPerformance;

		Result() {
			mPrice = 0;
			mPerformance = 0;
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
			case CMD_STOCK:
				in = Integer.parseInt(stdin.nextToken());
				in2 = Integer.parseInt(stdin.nextToken());
				in3 = Integer.parseInt(stdin.nextToken());
				in4 = Integer.parseInt(stdin.nextToken());
				ret = usersolution.stock(in, in2, in3, in4);
				ans = Integer.parseInt(stdin.nextToken());
				print(q, "stock", ans, ret, in, in2, in3, in4);
				if (ret != ans)
					okay = false;
				break;
			case CMD_ORDER:
				in = Integer.parseInt(stdin.nextToken());
				res = usersolution.order(in);
				ans = Integer.parseInt(stdin.nextToken());
				ans2 = Integer.parseInt(stdin.nextToken());
				print(q, "order", ans, res.mPrice,in);
				print(q, "order", ans2, res.mPerformance, in);
				if (res.mPrice != ans || res.mPerformance != ans2)
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
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\전자상가\\sample_input.txt"));

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