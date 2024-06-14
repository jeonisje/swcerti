package 기출문제.전자상가._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

class UserSolution {
	
	int charge;
	ArrayList<Parts>[][] partsByWH;
	
	
	int price;
	//int performance;
		
	void init(int mCharge) {
		this.charge = mCharge;
		partsByWH = new ArrayList[2][3];
		
		for(int i=0; i<2; i++) {
			for(int j=0; j<3; j++) {
				partsByWH[i][j] = new ArrayList<>();
			}
		}
	}
	
	int stock(int mType, int mPrice, int mPerformance, int mPosition) {
		partsByWH[mPosition][mType].add(new Parts(mPrice, mPerformance));
		return partsByWH[mPosition][mType].size();
	}

	Main.Result order(int mBudget) {
		price = 0;
		Main.Result res = new Main.Result();
		
		int start = 1;
		int end = 1_000_000;
		int cost = 0;
		while(start <= end) {
			int mid = (start + end) / 2;
			int ret = test(mid, mBudget);
			if(ret == -1) {
				end = mid -1;
			} else {
				cost = ret;
				start = mid + 1;
			}
		}
		
		if(cost == 0) return res;
		
		res.mPrice = cost;
		res.mPerformance = end;
		
		return res;
	}
	
	int test(int target, int mBudget) {
		Parts[][] minCost = new Parts[2][3]; // 창고별 최소값
		
		for(int i=0; i<2; i++) {
			for(int j=0; j<3; j++) {
				minCost[i][j] = new Parts(mBudget + 1, 0);
				for(Parts part : partsByWH[i][j]) {
					if(part.price > minCost[i][j].price) continue;
					if(part.perfomance < target) continue;					
					minCost[i][j] = part;
				}
			}
		}
		
		int mix0 = minCost[0][0].price + minCost[0][1].price + minCost[0][2].price;
		int mix1 = minCost[1][0].price + minCost[1][1].price + minCost[1][2].price;
		int mix12 = Math.min(minCost[0][0].price, minCost[1][0].price)
					+ Math.min(minCost[0][1].price, minCost[1][1].price)
					+ Math.min(minCost[0][2].price, minCost[1][2].price) + charge;
		
		int min = Math.min(mix0, Math.min(mix1, mix12));
		if(min > mBudget) return -1;
		
		price = min;
		
		return min;
	}

	class Parts {
		int price;
		int perfomance;
		public Parts(int price, int perfomance) {		
			this.price = price;
			this.perfomance = perfomance;
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