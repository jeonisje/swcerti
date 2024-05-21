package 프로원정대.day6.식당의가치._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	
	int MAX = 10_001;
	
	int N, M;	
	HashMap<String, Integer> nameToId;	
	HashMap<String, Integer> bestByStr;
	
	ArrayList<Integer>[] graph;
	int[] valueById;
	int[] townById;	
	
	ArrayList<Integer>[] idByTown;	
	int restaurantId;
	
	public void init(int N, int M, int mRoads[][]) {		
		restaurantId = 0; 
		nameToId = new HashMap<>();
		bestByStr = new HashMap<>();
		graph = new ArrayList[N+1];
		idByTown = new ArrayList[N+1];
		for(int i=0; i<N+1; i++) {
			graph[i] = new ArrayList<>();
			idByTown[i] = new ArrayList<>();
		}		
		valueById = new int [MAX];
		townById = new int [MAX];		
		
		for(int i=0; i<M; i++) {
			int from = mRoads[i][0];
			int to = mRoads[i][1];
			graph[from].add(to);
			graph[to].add(from);
		}
		
      	return; 
	}

	public void addRestaurant(int mCityID, char mName[]) {
		restaurantId++;
		String s = String.valueOf(mName);
		nameToId.put(s, restaurantId);
		townById[restaurantId] = mCityID;
		
		for(int k=0; k < mName.length; k++ ) {
			for(int i=0; i < mName.length-k; i++) {
				String s1 = s.substring(i, i+1+k);			
				if(bestByStr.containsKey(s1)) continue;
				bestByStr.put(s1, 0);
				
			}
		}		
		idByTown[mCityID].add(restaurantId);
      	return; 
	}	
	
	public void addValue(char mName[], int mScore) {
		String s = String.valueOf(mName);
		int id = nameToId.get(s);
		valueById[id] += mScore;
		int score = 0;
		for(int k=0; k < mName.length; k++ ) {
			for(int i=0; i < mName.length-k; i++) {
				String s1 = s.substring(i, i+1+k);	
				score = bestByStr.get(s1);
				bestByStr.put(s1, Math.max(valueById[id], score));
			}
		}		
		
		//int town = townById[id];
		
		
      	return; 
	}

	public int bestValue(char mStr[]) {
		String s = String.valueOf(mStr);
		return bestByStr.get(s);
	}

	public int regionalValue(int mCityID, int mDist) {
		int[] visited = new int[51];
		visited[mCityID] = 1;
		
		PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
		for(int id : idByTown[mCityID]) {
			pq.add(valueById[id]);
		}
		
		ArrayDeque<Node> q = new ArrayDeque<>();
		q.add(new Node(mCityID, 0));		
		
		while(!q.isEmpty()) {
			Node cur = q.remove();
			
			if(cur.dist == mDist) continue;
			
			for(int next : graph[cur.id]) {
				if(visited[next] == 1) continue;
				for(int id : idByTown[next]) {
					pq.add(valueById[id]);
				}
				
				q.add(new Node(next, cur.dist + 1));
				visited[next] = 1;
			}
		}
		
		int sum = 0;
		int idx = 0;
		while(!pq.isEmpty()) {
			sum += pq.remove();
			idx++;
			if(idx == 3) break;
		}
		return sum;
	}
	
	class Node {
		int id; 
		int dist;
		public Node(int id, int dist) {		
			this.id = id;
			this.dist = dist;
		}
	}
}
public class Main {
	private final static int CMD_INIT			= 1;
	private final static int CMD_ADD_RESTAURANT	= 2;
	private final static int CMD_ADD_VALUE		= 3;
	private final static int CMD_BEST_VALUE		= 4;
	private final static int CMD_REGIONAL_VALUE	= 5;
	
	private final static UserSolution usersolution = new UserSolution();
	
	private static void String2Char(char[] buf, String str) {
		for (int k = 0; k < str.length(); ++k)
			buf[k] = str.charAt(k);
	}

	private static int[][] mRoads = new int[50][2];

	private static boolean run(BufferedReader br) throws Exception
	{
		StringTokenizer st;

		int numQuery;
		int N, M, mCityID, mScore, mDist;
		int userAns, ans;
		String name; 
		char[] mName;
		char[] mStr; 

		boolean isCorrect = false;

		numQuery = Integer.parseInt(br.readLine());

		for (int q = 0; q < numQuery; ++q)
		{
			st = new StringTokenizer(br.readLine(), " ");

			int cmd;
			cmd = Integer.parseInt(st.nextToken());

			switch (cmd)
			{
			case CMD_INIT:
				N = Integer.parseInt((st.nextToken()));
				M = Integer.parseInt((st.nextToken()));
				for (int i = 0; i < M; i++) {
					mRoads[i][0] = Integer.parseInt((st.nextToken()));
					mRoads[i][1] = Integer.parseInt((st.nextToken()));
				}
				usersolution.init(N, M, mRoads);
				isCorrect = true;
				break;
			case CMD_ADD_RESTAURANT:
				mCityID = Integer.parseInt((st.nextToken()));
				name = st.nextToken();
				mName = new char[name.length()];
				String2Char(mName, name);
				usersolution.addRestaurant(mCityID, mName);
				print(q, "addRestaurant", q, q, mCityID,  name, mName);
				break;
			case CMD_ADD_VALUE:
				name = st.nextToken();
				mName = new char[name.length()];
				String2Char(mName, name);
				mScore = Integer.parseInt((st.nextToken()));
				usersolution.addValue(mName, mScore);
				print(q, "addValue", q, q,  mName, mScore);
				break;
			case CMD_BEST_VALUE:
				name = st.nextToken();
				mStr = new char[name.length()];
				String2Char(mStr, name);
				userAns = usersolution.bestValue(mStr);
				ans = Integer.parseInt((st.nextToken()));
				print(q, "bestValue", ans, userAns, name,  mStr);
				if (userAns != ans)
				{
					isCorrect = false;
				}
				break;
			case CMD_REGIONAL_VALUE:
				mCityID = Integer.parseInt((st.nextToken()));
				mDist = Integer.parseInt((st.nextToken()));
				userAns = usersolution.regionalValue(mCityID, mDist);
				ans = Integer.parseInt((st.nextToken()));
				print(q, "regionalValue", ans, userAns, mCityID,  mDist);
				if (userAns != ans)
				{
					isCorrect = false;
				}
				break;
			default:
				isCorrect = false;
				break;
			}
		}
		return isCorrect;
	}
	

	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}

	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		
		int TC, MARK;
	
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\프로원정대\\day6\\식당의가치\\sample_input.txt"));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		
		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
		}

		br.close();
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}