package 연습문제.당근마켓._2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

class UserSolution {
	int MAX = 30_000;
	
	int N;
	
	TreeMap<String, TreeSet<String>> tagMap;
	
	HashMap<String, ArrayList<Integer>> searchMapByTag;
	
	int[] selled;
	int[] priceByID;
	
	TreeSet<String>[] tagsById;
	
	int id;
	
	public void init(int N) {
		this.N = N;
		tagMap = new TreeMap<>();
		searchMapByTag = new HashMap<>();
		
		id = 0;
		tagsById = new TreeSet[MAX];
		selled = new int[MAX];
		priceByID = new int[MAX];
		
		for(int i=0; i<MAX ; i++) {
			tagsById[i] = new TreeSet<>();
		}
	}

	public void addCarrot(int price, int tagCnt, String tagName[]) {
		
		priceByID[id] = price;		
		TreeSet<String> tagSet = new TreeSet<>();
		
		for(int i=0; i<tagCnt; i++) {			
			tagSet.add(tagName[i]);
			ArrayList<Integer> list = searchMapByTag.getOrDefault(tagSet.first(), new ArrayList<>());
			list.add(id);
			searchMapByTag.put(tagName[i], list);	
			tagsById[id].add(tagName[i]);
		}
		
		//TreeSet<Carrot> set = tagMap.getOrDefault(tagSet.first(), ordered());
		//set.add(new Carrot(id, price, tagSet));
		//tagMap.put(tagSet.first(), set);
		
		
		id++;
	}

	private TreeSet<Carrot> ordered() {
		return new TreeSet<Carrot>((o1, o2) -> o1.price == o2.price ? Integer.compare(o1.id, o2.id) : Integer.compare(o1.price, o2.price));
	}



	public int sellCarrot(String tag1, String tag2, String tag3) {
		
		String[] findTags = new String[3];
		findTags[0] = tag1;
		findTags[1] = tag2;
		findTags[2] = tag3;
		
		Arrays.sort(findTags);
		
		String t1 = findTags[0];
		//TreeSet<Carrot> set = tagMap.get(t1);
		
		
		/*
		
		
		while(!q.isEmpty()) {
			Carrot c = q.poll();
			if(selled[c.id] == 1) continue;
		
			TreeSet<String> candidateTags = tagsById[c.id];
			int matched = 1;
			int count = 1;
			
			while(count < candidateTags.size()) {
				//loop++;
				String fTag = findTags[1];				
				
				if(matched == 2) {
					fTag = findTags[2];
				}					
				String tt = candidateTags.ceiling(fTag);
				count++;
				if(tt == null) break;
				
				
				if(fTag.equals(tt)) {
					matched++;
				} else if (candidateTags.size() == 3 && count == 2 && count == 2) {
					 break;
				} else if (candidateTags.size() == 4 && count == 3) {
					 break;
				} else if (candidateTags.size() == 5 && count == 4) {
					 break;
				}
				
				if(matched == 3) {
					//System.out.println("loop : " + loop) ;
					
					selled[c.id] = 1;
					return c.price;
				}				
			}			
		}
		*/
		return -1;
		
		
	}

	public void updatePrice(String tag1, int addPrice) {
		String t = String.valueOf(tag1).trim();		
		
		ArrayList<Integer> list = searchMapByTag.get(t);
		
		if(list == null) return;
		TreeSet<Carrot> set = new TreeSet<>(ordered());
		for(int id: list) {
			int newPrice = priceByID[id] + addPrice;
			priceByID[id] = newPrice;
			set.add(new Carrot(id, newPrice, tagsById[id]));
		}
		tagMap.remove(t);
		//tagMap.put(t, set);
		int a = 1;
	}
	
	class Carrot {
		
		int id;
		int price;
		TreeSet<String> tagSet;
		public Carrot(int id, int price, TreeSet<String> tags) {		
			this.id = id;
			this.price = price;
			tagSet = tags;
		}
		
		@Override
		public boolean equals(Object o) {			
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
				
			Carrot c = (Carrot) o;			
				return id == c.id;			
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
		if(ans!=ret) System.err.println("===================오류=======================");
		System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
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
		
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//연습문제//당근마켓//sample_input1.txt"));
		
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