package 프로원정대.day3.wormslife;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

class UserSolution {
	final int MAX_WORM = 100_001;
	
	int[] infoByWorm;
	
	TreeMap<Integer, HashSet<Integer>> treeMap;
	int totalCount;
	int maxLife;

	public void init() {
		infoByWorm = new int[MAX_WORM];
		treeMap = new TreeMap<>();
		totalCount = 0;
		maxLife = 0;
	}
	
	public void add(int id, int life) {
		if(!(infoByWorm[id] ==0 || infoByWorm[id] == -1)) return;
		
		HashSet<Integer> list = treeMap.getOrDefault(life, new HashSet<Integer>());
		list.add(id);
		treeMap.put(life, list);
		infoByWorm[id] = life;
		totalCount++;
		maxLife = Math.max(maxLife, life);
	}

	public int span(int year) {		
		if(year == 10) {
			treeMap = new TreeMap<>();
			totalCount = 0;
			maxLife = 0;			
			
			for(Entry<Integer, HashSet<Integer>> entry : treeMap.entrySet()) {
				if(entry == null) continue;
				if(entry.getValue() == null) continue;
				for(int id : entry.getValue()) {
					infoByWorm[id] = -1;
				}
			}
			
			return 0;
		}
		
		ArrayList<Integer> target1 = new ArrayList<>();
		int count = 0;
		for(Entry<Integer, HashSet<Integer>> entry : treeMap.subMap(1, year + 1).entrySet()) {
			if(entry == null) continue;
			if(entry.getValue() == null) continue;
			count += entry.getValue().size();
			target1.add(entry.getKey());
		}
		
		for(int i : target1) {
			for(int id : treeMap.get(i)) {
				infoByWorm[id] = -1;
			}
			treeMap.put(i, new HashSet<>());
		}		
		
		//int year1 = 1;
		HashSet[] worms = new HashSet[treeMap.lastKey() + 1];
		for(Entry<Integer, HashSet<Integer>> entry : treeMap.tailMap(year + 1).entrySet()) {
			HashSet<Integer> set = entry.getValue();
			
			
			//if(set == null) continue;
			
			worms[entry.getKey() - year] = set;			
			
			for(int id : set) {
				infoByWorm[id] -= year;
			}
			
		}
		for(int i=year; i<=treeMap.lastKey(); i++) {
			treeMap.put(i, new HashSet<>());
		}		
		
		for(int  i=1; i <= treeMap.lastKey() - year; i++) {
			if(worms[i] == null) continue;
			treeMap.put(i, worms[i]);
		}
		
		
		
		totalCount -= count;
		return totalCount;
	}

	public int getLife(int id) {
		if(infoByWorm[id] == 0) return -1;
		return infoByWorm[id]; 
	}
  
  	public int addLife(int id, int life) {
  		if(infoByWorm[id] == 0) return -1; 
  		if(infoByWorm[id] == -1) return -1;
  		
  		int oldLife = infoByWorm[id];
  		int newLife = oldLife + life;
  		infoByWorm[id] = newLife;
  		
  		treeMap.get(oldLife).remove(id);
  		HashSet<Integer> set = treeMap.getOrDefault(newLife, new HashSet<Integer>());
  		set.add(id);
  		treeMap.put(newLife, set);
  		
      return newLife; 
    }
}

public class Main {

	private final static int CMD_ADD 		= 1;
	private final static int CMD_SPAN	 	= 2;
	private final static int CMD_GET_LIFE 	= 3; 
    private final static int CMD_ADD_LIFE   = 4; 

	private static boolean run(BufferedReader br) throws IOException {

		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
		int Q = Integer.parseInt(st.nextToken());

		usersolution.init();

		boolean isCorrect = true; 
		int cmd; 
		int id;
		int life; 
		int year; 
		int userAns;
		int ans; 

		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			switch(cmd) 
			{
				case CMD_ADD :
					id = Integer.parseInt(st.nextToken());
					life = Integer.parseInt(st.nextToken());
					usersolution.add(id, life);
					print(i, "add", i, i, id, life);
					break;
	
				case CMD_SPAN :
					year = Integer.parseInt(st.nextToken()); 
					userAns = usersolution.span(year); 
					ans = Integer.parseInt(st.nextToken());
					print(i, "span", ans, userAns, year);
					if(userAns != ans)
						isCorrect = false; 
					break;
					
				case CMD_GET_LIFE :
					id = Integer.parseInt(st.nextToken());
					userAns = usersolution.getLife(id); 
					ans = Integer.parseInt(st.nextToken());
					print(i, "getLife", ans, userAns, id);
					if(userAns != ans)
						isCorrect = false; 
					break;
                
                case CMD_ADD_LIFE :
					id = Integer.parseInt(st.nextToken());
                	life = Integer.parseInt(st.nextToken()); 
					userAns = usersolution.addLife(id, life); 
					ans = Integer.parseInt(st.nextToken());
					print(i, "addLife", ans, userAns, id, life);
					if(userAns != ans)
						isCorrect = false; 
					break;

				default:
					isCorrect = false;
					break;
			}
		}
		return isCorrect;
	}

	private final static UserSolution usersolution = new UserSolution();
	
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("----------------오류-----------------");
		//System.out.println("["+ q +"] " + cmd + ", " + ans +"=" + ret + "(" + Arrays.deepToString(o)+")") ;
	}

	public static void main(String[] args) throws Exception {

	    System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대\\day3\\wormslife\\sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; testcase++) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
	}
}
