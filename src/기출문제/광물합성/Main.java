package 기출문제.광물합성;


import java.io.*;
import java.util.*;

class UserSolution {
  
	public void init(int mShipFee) {
	}
	 
	public int gather(int mMineId, int mType, int mCost, int mContent) {
	    return 0;
	}

	public Main.Result mix(int mBudget) {
	  	Main.Result res = new Main.Result();
	  	return res; 
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