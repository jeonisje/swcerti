package 기출문제.광물합성._01;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

class UserSolution {
	final int MAX_ID = 4_000;
	final int SUMMARY_UNIT = 1_000;
	final int MAX_COST = 100_000;
	
	int shipFee;	
	int id;
	int[] removed;
	int[][] countByType;
	
	TreeMap<Integer, Integer>[][] combiMap; // 광물 0, 1
	TreeMap<Integer, Integer>[][] mat2Map;  // 광물 2	
	
	Material[] materialInfo;
	
	int sSize;
  
	public void init(int mShipFee) {		
		this.shipFee =  mShipFee;
		
		countByType = new int[2][3];
		id = 0;
		removed = new int[MAX_ID];
		sSize = MAX_COST / SUMMARY_UNIT;
		
		
		
	}
	 
	public int gather(int mMineId, int mType, int mCost, int mContent) {		
		id++;
		
		countByType[mMineId][mType]++;
		Material material = new Material(id, mMineId, mCost, mContent);		
		materialInfo2[id] = material;
		if(mType == 2) {
			map.put(material, id);
		} else {
			materialInfo[mType].add(material);
			setCombi(mMineId, mType, mCost, mContent);
		}
	    
		return countByType[mMineId][mType];
	}
	
	void setCombi(int mineId, int type, int cost, int content) {
		int otherType = 1;
		if(type == 1) {
			otherType = 0;
		}
	
		for(Material m : materialInfo[otherType]) {			
			int count = 1;
			if(mineId !=  m.mineId) {
				count = 2;
			}
			int combiCost = (cost + m.cost) + shipFee * count;
			int minContent = Math.min(m.content, content);
			MaterialCombi mc = new MaterialCombi(id, m.id, 0, combiCost, minContent, count);
			combiMap.put(mc, combiCost);
		}
	}
	

	public Main.Result mix(int mBudget) {
		Main.Result res = new Main.Result();
		
		MaterialCombi combi = getCombination(mBudget);
		if(combi == null) return res;
		
		res.mContent = combi.content;
		res.mCost = combi.cost;
		
	  
	  	return res; 
	}
	
	MaterialCombi getCombination( int budget) {
		
		int maxContent = 0;
		MaterialCombi combi = null;
		for(Entry<MaterialCombi, Integer> entryCombi : combiMap.entrySet()) {
			MaterialCombi mc = entryCombi.getKey();
			if(removed[mc.id0] == 1 || removed[mc.id1] == 1) continue;
			
			if(mc.cost> budget) break;
			int count = mc.mineCount;
			for(Entry<Material, Integer> entry : map.entrySet()) {
				Material m = entry.getKey();
				if(removed[m.id] == 1) continue;
				int cost = mc.cost + m.cost;
				if(mc.mineCount != 2) {
					if(materialInfo2[mc.id0].mineId != m.mineId) {
						cost += shipFee;
						count++;
					}
				}
				if(cost > budget) break;
				int content = Math.min(mc.content, entry.getKey().content);
				if(content > maxContent) {
					maxContent = content;				
					combi = new MaterialCombi(mc.id0, mc.id1, m.id, cost, content, count); 
				}
			}
		}
		
		return combi;
		
	}
	
	
	class MaterialCombi {
		int id0;
		int id1;
		int id2;		
		int cost;
		int content;
		int mineCount;
		
		public MaterialCombi(int id0, int id1, int id2, int cost, int content, int mineCount) {
			this.id0 = id0;
			this.id1 = id1;
			this.id2 = id2;
			this.cost = cost;
			this.content = content;
			this.mineCount = mineCount;
		} 
	}
	
	class  Material {	
		int id;
		int mineId;
		int cost;
		int content;
		public Material(int id, int mineId, int cost, int content) {
			this.id = id;
			this.mineId = mineId;
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