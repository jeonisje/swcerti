package 연습문제.당근마켓._1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

class UserSolution {
	int N;
	HashMap<String, ArrayList<Integer>> combinedTags;
	HashMap<Integer, Integer> priceBy;
	HashMap<Integer, Boolean> selled;
	//HashMap<String, ArrayList<Integer>> tags;
	HashMap<String, Integer> tagMapping;
	int[][] idByTags;
	int[] tagCount;
	
	int id, tagID;
	
	final int MAX_TAG =  30;
	final int MAX_ADD =  30_000;
	
	public void init(int N) {
		this.N = N;
		combinedTags = new HashMap<>();
		priceBy = new HashMap<>();
		selled = new HashMap<>();
		tagMapping  = new HashMap<>();
		
		idByTags = new int[MAX_TAG][MAX_ADD];
		tagCount = new int[MAX_TAG];
		
		tagID = 0;
		id = 0;
	}

	public void addCarrot(int price, int tagCnt, String tagName[]) {
		
		priceBy.put(id, price);
		
		String[] arr = new String[tagCnt];
		
		for(int i=0; i<tagCnt; i++) {
			String s = String.valueOf(tagName[i]).trim();
			arr[i] = s;
			
			if(tagMapping.containsKey(s)) {
				int tid = tagMapping.get(s);	
				
				idByTags[tid][tagCount[tid]] = id;
				tagCount[tid]++; 			
				
			} else {
				idByTags[tagID][0] = id;
				tagMapping.put(s, tagID);
				
				tagCount[tagID] = 1;
				tagID++;
			}
		}
		
		Arrays.sort(arr);
		for(int i=0; i<tagCnt-2; i++) {
			for(int j=i+1; j<tagCnt-1; j++) {
				for(int k=j+1; k<tagCnt; k++) {
					String tags = "[" + arr[i] + ", " + arr[j]+ ", " + arr[k]  + "]";			
					
					if(combinedTags.containsKey(tags)) {
						combinedTags.get(tags).add(id);
					} else {
						combinedTags.put(tags, new ArrayList<>(Arrays.asList(id)));
					}
				}				
			}			
		}
		
		id++;
	}

	public int sellCarrot(String tag1, String tag2, String tag3) {	
		
		String t1 = String.valueOf(tag1).trim();
		String t2 = String.valueOf(tag2).trim();
		String t3 = String.valueOf(tag3).trim();
		
		String[] arr = {t1, t2, t3};
		Arrays.sort(arr);
		
		String tags = Arrays.toString(arr);		
		
		
		ArrayList<Integer> list1 = combinedTags.get(tags);
		if(list1 == null || list1.isEmpty()) return -1;
		
		boolean found = false;
		int minPrice = Integer.MAX_VALUE;
		int selledId = Integer.MAX_VALUE;
		for(Integer id : list1) {
			if(selled.containsKey(id)) continue;
			int price = priceBy.get(id);
			if(minPrice > price) {
				minPrice = price;
				selledId = id;
			}
			found = true;
		}
		selled.put(selledId, true);
		return found ? minPrice : -1;
		
	}

	public void updatePrice(String tag1, int addPrice) {
		String t = String.valueOf(tag1).trim();	
		
		int tid = tagMapping.get(t);
		int countByTag = tagCount[tid];
			
		for(int i=0; i<countByTag; i++) {
			int id = idByTags[tid][i];
			int price = priceBy.get(id);
			priceBy.put(id, price + addPrice);

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
//		if(ans!=ret) System.err.println("===================오류=======================");
//		System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
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