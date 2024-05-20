package 기출문제.당근마켓;


import java.io.*;
import java.util.*;

class UserSolution {
	int MAX = 40000;
	
	int carrotId;
	int tagId;
	HashMap<Integer, ArrayList<Integer>> tagCombiMap;
	
	HashMap<String , Integer> tagToId;
	HashMap<Integer, ArrayList<Integer>> idByTag;
	
	int[] priceById;
	
	int[] selled;
	
	
	public void init(int N) {
		carrotId = 0;
		tagId = 0;
		
		tagCombiMap = new HashMap<>();
		tagToId = new HashMap<>();
		idByTag = new HashMap<>();
		
		priceById = new int[MAX];
		selled = new int[MAX];
		
		
	}
	
	int hashCode(int tag1, int tag2, int tag3) {
		int[] code = {10000, 100, 1};
		int[] tags = new int[3];
		tags[0] = tag1;
		tags[1] = tag2;
		tags[2] = tag3;
		
		Arrays.sort(tags);
		
		int hash = 0;
		for(int i=0 ; i<3; i++) {
			 hash += tags[i] * code[i]; 
		}
		
		return hash;
	}
	public void addCarrot(int price, int tagCnt, String tagName[]) {
		carrotId++;
		
		int[] tags = new int[tagCnt];
		for(int i=0; i<tagCnt; i++) {
			if(tagToId.containsKey(tagName[i])) {
				tags[i] = tagToId.get(tagName[i]);
			} else {
				tagId++;
				tags[i] = tagId;
				tagToId.put(tagName[i], tagId);
			}
			
			ArrayList<Integer> list = idByTag.getOrDefault(tags[i], new ArrayList<Integer>());
			list.add(carrotId);
			idByTag.put(tags[i], list);
		}
		
		priceById[carrotId] = price;
		
		for(int i=0; i < tagCnt - 2; i++) {
			for(int j=i+1; j<tagCnt-1; j++) {
				for(int k=j+1; k<tagCnt; k++) {
					int hash = hashCode(tags[i], tags[j], tags[k]);
					ArrayList<Integer> list = tagCombiMap.getOrDefault(hash, new ArrayList<Integer>());
					list.add(carrotId);
					tagCombiMap.put(hash, list);
				}
			}
		}
		
	}

	public int sellCarrot(String tag1, String tag2, String tag3) {
		Integer t1 = tagToId.get(tag1);
		Integer t2 = tagToId.get(tag2);
		Integer t3 = tagToId.get(tag3);
		
		if(t1 == null || t2 == null || t3 == null) return -1;
		
		int hash = hashCode(t1, t2, t3);
		
		if(!tagCombiMap.containsKey(hash)) return -1;
		
		int min = Integer.MAX_VALUE;
		int minId = 0;
		
		for(int id : tagCombiMap.get(hash)) {
			if(selled[id] == 1) continue;
			if(priceById[id] < min) {
				min = priceById[id];
				minId = id; 
			}
		}
		
		if(minId == 0) return -1;
		
		selled[minId] = 1;		
		
		return min;
	}

	public void updatePrice(String tag1, int addPrice) {		
		if(!tagToId.containsKey(tag1)) return;
		
		int tagId = tagToId.get(tag1);

		for(int id : idByTag.get(tagId)) {
			if(selled[id] == 1) continue;
			priceById[id] += addPrice;
		}		
	}
	
	
}


class Main {
	private final static int INIT = 0;
	private final static int ADD = 1;
	private final static int SELL = 2;
	private final static int UPDATE = 3;
	
	private final static UserSolution usersolution = new UserSolution();
	private static BufferedReader br;
	
	private static boolean run() throws Exception {
		
		StringTokenizer st;
		int N, cmd, ans, ret, tagNum, price;
		String tagNames[];

		int Q = 0;
		boolean correct = false;

		ret = ans = 0;
		
		int n;
		Q = Integer.parseInt(br.readLine());
		
		for (int q = 0; q < Q; ++q) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd) {
			case INIT:
				N = Integer.parseInt(st.nextToken());
				usersolution.init(N);
				correct = true;
				break;
			case ADD:
				price = Integer.parseInt(st.nextToken());
				tagNum = Integer.parseInt(st.nextToken());
				tagNames = new String[tagNum];
				for (int m = 0; m < tagNum; m++) {
					tagNames[m] = st.nextToken();
				}
				usersolution.addCarrot(price, tagNum, tagNames);
				print(q, "addCarrot", q, q, price, tagNum, tagNames);
				break;
			case SELL:
				ans = Integer.parseInt(st.nextToken());
				tagNames = new String[3];
				for (int m = 0; m < 3; m++) {
					tagNames[m] = st.nextToken();
				}
				ret = usersolution.sellCarrot(tagNames[0], tagNames[1], tagNames[2]);
				print(q, "sellCarrot", ans, ret, tagNames[0], tagNames[1], tagNames[2]);
				if (ans != ret) {
					correct = false;
				}
				break;
			case UPDATE:
				tagNames = new String[1];
				tagNames[0] = st.nextToken();
				price = Integer.parseInt(st.nextToken());
				usersolution.updatePrice(tagNames[0], price);
				print(q, "updatePrice", q, q, tagNames, price);
				break;
			default:
				correct = false;
			}
		}
		return correct;
	}
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		
		int TC, MARK;

		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\당근마켓\\sample_input.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}