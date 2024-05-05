package 기출문제.인플루언서;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeSet; 

class UserSolution {
	final int P_POWER_0 = 10;
	final int P_POWER_1 = 5;
	final int P_POWER_2 = 3;
	final int P_POWER_3 = 2;
	
	int N, M;
	int[] pPower;
	int[] friends1;
	int[] friends2;
	
	TreeSet<Member> lowerInfluence;
	TreeSet<Member> higherInfluence;
	
	TreeSet<Member> influences;
	
	Member[] objMapping;
	
	ArrayList<Member>[] graph;
	
	public void init(int N, int[] mPurchasingPower, int M, int[] mFriend1, int[] mFriend2) {
    	this.N = N;
    	this.M = M;
    	this.friends1 = mFriend1;
    	this.friends2 = mFriend2;
    	this.pPower = mPurchasingPower;    	
    	
    	objMapping = new Member[N];
    	graph = new ArrayList[N];
    	
    	for(int i=0; i<N; i++) {
    		graph[i] = new ArrayList<>();
    	}
    	
    	
    	higherInfluence = new TreeSet<>((o1, o2) -> o1.pPower == o2.pPower ? Integer.compare(o2.id, o1.id) : Integer.compare(o1.pPower, o2.pPower));
    	lowerInfluence = new TreeSet<>(ordered());
    	
    	influences = new TreeSet<>(ordered());
    	
    	for(int i=0; i<M; i++) {    
    		graph[friends1[i]].add(new Member(friends2[i], pPower[friends2[i]]));
    		graph[friends2[i]].add(new Member(friends1[i], pPower[friends1[i]]));
    	}
		
    	for(int i=0; i<N; i++) {
    		int influence  = bfs(i);
    		Member m = new Member(i, influence);
    		
    		influences.add(m);
    	}
    	
    	//Collections.sort(influences, ordered());
    	
	}

	private Comparator<? super Member> ordered() {
		return (o1, o2) -> o1.pPower == o2.pPower ? Integer.compare(o1.id, o2.id) : Integer.compare(o2.pPower, o1.pPower);
	}
	
	int bfs(int start) {
		int sum = 0;
		ArrayDeque<Influence> q = new ArrayDeque<>();
		q.add(new Influence(start, 0));
		
		int[] visited = new int[N];
		visited[start] = 1;
		
		int sum0 = pPower[start] * P_POWER_0;
		int sum1 = 0;
		int sum2 = 0;
		int sum3 = 0;
		
		while(!q.isEmpty()) {
			Influence cur = q.poll();
			int count = Math.min(3, cur.count + 1);
			
			for(Member next : graph[cur.id]) {
				if(visited[next.id] == 1) continue;						
				if(count == 1) {
					sum1 += next.pPower ;
				} else if(count == 2) {
					sum2 += next.pPower ;
				} else if(count == 3) {
					sum3 += next.pPower;
				}
				
				q.add(new Influence(next.id, count));
				visited[next.id] = 1;
			}
		}
		
		return sum0 + sum1 * P_POWER_1 + sum2 * P_POWER_2 + sum3 * P_POWER_3 ;
		
	}
	
	
	
	public int influencer(int mRank) {
		//return influences.get(mRank-1).id;
		return 0;
	}
    
	public int addPurchasingPower(int mID, int mPower) {
		return 0; 
	}

	public int addFriendship(int mID1, int mID2) {
		return 0 ; 
	}
	
	class Member {
		int id;
		int pPower;
		
		public Member(int id, int pPower) {
			this.id = id;
			this.pPower = pPower;
		}
	}
	
	class Influence {
		int id;		
		int count;

		public Influence(int id, int count) {		
			this.id = id;
			this.count = count;
		}	
	}
}	


public class Main
{
	private final static int CMD_INIT 					= 1;
	private final static int CMD_INFLUENCER 			= 2;
	private final static int CMD_ADD_PURCHASING_POWER 	= 3;
	private final static int CMD_ADD_FRIENDSHIP			= 4;

	private final static UserSolution usersolution = new UserSolution();
	
	private static int[] mPurchasingPower;
	private static int[] mFriend1;
	private static int[] mFriend2; 

	private static BufferedReader br;
	
	
	private static boolean run() throws Exception
	{
		StringTokenizer st;

		int numQuery;
		int N, M, mRank, mID, mPower, mID1, mID2;
		int userAns, ans; 
		boolean isCorrect = false; 

		numQuery = Integer.parseInt(br.readLine());

		for (int q = 0; q < numQuery; ++q)
		{
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());

			switch (cmd)
			{
			case CMD_INIT:
				mPurchasingPower = new int[20000];
				mFriend1 = new int[20000]; 
				mFriend2 = new int[20000];
				N = Integer.parseInt(st.nextToken());
				for(int i = 0; i < N; i++) 
					mPurchasingPower[i] = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				for(int i = 0; i < M; i++) 
					mFriend1[i] = Integer.parseInt(st.nextToken());
				for(int i = 0; i < M; i++) 
					mFriend2[i] = Integer.parseInt(st.nextToken());
				usersolution.init(N, mPurchasingPower, M, mFriend1, mFriend2);
				isCorrect = true;
				break;
			case CMD_INFLUENCER :
				mRank = Integer.parseInt(st.nextToken());
				userAns = usersolution.influencer(mRank);
				ans = Integer.parseInt(st.nextToken());
				print(q, "influencer", ans, userAns, mRank);
				if(userAns != ans) {
					isCorrect = false; 
				}
				break;
			case CMD_ADD_PURCHASING_POWER:
				mID = Integer.parseInt(st.nextToken());
				mPower = Integer.parseInt(st.nextToken());
				userAns = usersolution.addPurchasingPower(mID, mPower);
				ans = Integer.parseInt(st.nextToken());
				print(q, "addPurchasingPower", ans, userAns, mID, mPower);
				if(userAns != ans) {
					isCorrect = false; 
				}
				break;
			case CMD_ADD_FRIENDSHIP	:
				mID1 = Integer.parseInt(st.nextToken());
				mID2 = Integer.parseInt(st.nextToken());
				userAns = usersolution.addFriendship(mID1, mID2);
				ans = Integer.parseInt(st.nextToken());
				print(q, "addFriendship", ans, userAns, mID1, mID2);
				if(userAns != ans) {
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
		//if(ans!=ret) System.err.println("====================오류========================");
		//System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
	}
	public static void main(String[] args) throws Exception
	{
		Long start = System.currentTimeMillis();
		int TC, MARK;

		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//기출문제//인플루언서//sample_input.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run() ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}