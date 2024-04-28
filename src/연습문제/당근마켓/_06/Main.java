package 연습문제.당근마켓._06;


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
	HashMap<String, ArrayList<Integer>> carrotByTagCombination;
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
		
		String[] tags = new String[tagCnt];
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
			tags[i] = tagName[i];
		}
		
		priceByCarrotId[carrotId] = price;
		
		Arrays.sort(tags);
		
		
		for(int i=0; i < tagCnt-2; i++) {
			for(int j=i+1; j < tagCnt - 1; j++) {
				for(int k=j+1; k <tagCnt; k++) {
					String cTag = combination(tags[i], tags[j], tags[k]);
					ArrayList<Integer> list = carrotByTagCombination.getOrDefault(cTag, new ArrayList<Integer>());
					list.add(carrotId);
					carrotByTagCombination.put(cTag, list);
				}
			}
		}
		
	}
	
	String combination(String s1, String s2, String s3) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(s1).append(",").append(s2).append(",").append(s3).append("]");
		return sb.toString();
	}

	public int sellCarrot(String tag1, String tag2, String tag3) {
		String[] tags = new String[3];
		tags[0] = tag1;
		tags[1] = tag2;
		tags[2] = tag3;
		Arrays.sort(tags);		
		
		String cTag = combination(tags[0], tags[1], tags[2]);
		
		if(!carrotByTagCombination.containsKey(cTag)) return -1;
		ArrayList<Integer> list = carrotByTagCombination.get(cTag);
		
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
		
		/*
		PriorityQueue<Carrot> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.price, o2.price));
		for(int id : list) {
			if(selled[id] == 1) continue;
			q.add(new Carrot(id, priceByCarrotId[id]));
		}
		
		if(q.size() == 0) return -1;
		
		while(!q.isEmpty()) {
			Carrot carrot = q.poll();
			selled[carrot.id] = 1;			
			return carrot.price;
			
		}*/

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