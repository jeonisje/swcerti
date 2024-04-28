package 연습문제.당근마켓._07;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

class UserSolution {
	
	final int MAX_TAG = 31;
	final int MAX_CARROT_ID = 30_001;
	
	int N;
	
	int[] selled;
	ArrayList<Integer>[] carrotByTagId;
	HashMap<Integer, ArrayList<Integer>> carrotByTagCombination;
	int[] priceByCarrotId;
	
	HashMap<String, Integer> tagToId;
	
	int tagId;
	int carrotId;
	
	public void init(int N) {
		this.N = N;
		carrotByTagId = new ArrayList[N+1];
		carrotByTagCombination = new HashMap<>();
		priceByCarrotId = new int[MAX_CARROT_ID];
		
		tagToId = new HashMap<>();
		
		tagId = 0;
		carrotId = 0;
		
		selled = new int[MAX_CARROT_ID];
		
		for(int i=0; i<N+1; i++) {
			carrotByTagId[i] = new ArrayList<>();
		}
		
	}

	public void addCarrot(int price, int tagCnt, String tagName[]) {
		carrotId++;
	
		for(int i=0; i<tagCnt; i++) {
			String tag = tagName[i];
			if(!tagToId.containsKey(tag)) {
				tagId++;
				tagToId.put(tag, tagId);
				carrotByTagId[tagId].add(carrotId);
			} else {
				int tId = tagToId.get(tag);
				carrotByTagId[tId].add(carrotId);
			}
		}
		
		priceByCarrotId[carrotId] = price;
		
		//Arrays.sort(tags);
		
		
	
		for(int i=0; i < tagCnt-2; i++) {
			for(int j=i+1; j < tagCnt - 1; j++) {
				for(int k=j+1; k <tagCnt; k++) {					
					int tagkey = combination(tagName[i], tagName[j], tagName[k]);
					ArrayList<Integer> list = carrotByTagCombination.getOrDefault(tagkey, new ArrayList<Integer>());
					list.add(carrotId);
					carrotByTagCombination.put(tagkey, list);
				}
			}
		}
		
	}
	
	
	int combination(String tag1, String tag2, String tag3) {
		int tagId1 = tagToId.get(tag1);
		int tagId2 = tagToId.get(tag2);
		int tagId3 = tagToId.get(tag3);
		
		int tagkey = tagId1 << (tagId1 - 1) + tagId2 << (tagId2 - 1) + tagId3 << (tagId3 - 1);
		return tagkey;
	}

	public int sellCarrot(String tag1, String tag2, String tag3) {
		if(!tagToId.containsKey(tag1)) return -1;
		if(!tagToId.containsKey(tag2)) return -1;
		if(!tagToId.containsKey(tag3)) return -1;
		
		int tagkey = combination(tag1, tag2, tag3);
		
		if(!carrotByTagCombination.containsKey(tagkey)) return -1;
		ArrayList<Integer> list = carrotByTagCombination.get(tagkey);
		
		int minPrice = Integer.MAX_VALUE;
		int selledId = 0;
		boolean found = false;
		for(int id : list) {
			if(selled[id] == 1) continue;
			if(priceByCarrotId[id] < minPrice) {
				minPrice = priceByCarrotId[id];
				selledId = id;
				found = true;
			}
		}
		
		if(found) {
			selled[selledId] = 1;
		}
		
		return found ? minPrice : -1;
		
	}

	public void updatePrice(String tag1, int addPrice) {
		if(!tagToId.containsKey(tag1)) return;
		
		int id = tagToId.get(tag1);
		
		ArrayList<Integer> list = carrotByTagId[id];
		
		for(int i : list) {
			priceByCarrotId[i] += addPrice;
		}
	}
	
	
	
	class Carrot {
		int id;
		int price;
		public Carrot(int id, int price) {
			this.id = id;
			this.price = price;
		}
	}
		
}

public class Main {
	private final static int INIT = 0;
	private final static int ADD = 1;
	private final static int SELL = 2;
	private final static int UPDATE = 3;
	
	private final static UserSolution usersolution = new UserSolution();
	private static BufferedReader br;
	
	private static void print(int num, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("===================오류=======================");
		//System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
	}
	
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
				print(q, "sellCarrot", ans, ret, tagNames);
				if (ans != ret) {
					correct = false;
				}
				break;
			case UPDATE:
				tagNames = new String[1];
				tagNames[0] = st.nextToken();
				price = Integer.parseInt(st.nextToken());
				usersolution.updatePrice(tagNames[0], price);
				print(q, "updatePrice", q, q, tagNames[0], price);
				break;
			default:
				correct = false;
			}
		}
		return correct;
	}

	public static void main(String[] args) throws Exception {
		int TC, MARK;

		Long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//연습문제//당근마켓//sample_input2.txt"));
		
		br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
		
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}