package 기출문제.인플루언서._01;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap; 

class UserSolution {	
	final int[] P_POWER = {10, 5, 3, 2};
	
	int N;
	int[] pPower;
	int[] friends1;
	int[] friends2;
	
	int[] parent;	
	
	TreeMap<Member, Integer> influences;
	
	Member[] memberInfo;	
	ArrayList<Integer>[] memberByGroup;
	ArrayList<Integer>[] friendsByMember;
	
	public void init(int N, int[] mPurchasingPower, int M, int[] mFriend1, int[] mFriend2) {
    	this.N = N;
    	this.friends1 = mFriend1;
    	this.friends2 = mFriend2;
    	this.pPower = mPurchasingPower;    	
    	
    	memberByGroup = new ArrayList[N];
    	friendsByMember  = new ArrayList[N];
    	memberInfo = new Member[N];
    	parent =  new int[N];
    	
    	influences = new TreeMap<>(ordered());
    	
    	for(int i=0; i<N; i++) {
    		parent[i] = i;
    		memberByGroup[i] = new ArrayList<>(Arrays.asList(i));
    		friendsByMember[i] = new ArrayList<>();
    		Member m = new Member(i, mPurchasingPower[i], mPurchasingPower[i] * 10);
    		memberInfo[i] = m;
    		influences.put(m, 1);
    		
    	}    	    
    	
    	for(int i=0; i<M; i++) {
    		friendsByMember[mFriend1[i]].add(mFriend2[i]);
    		friendsByMember[mFriend2[i]].add(mFriend1[i]);
    		
    		if(friendsByMember[mFriend1[i]].size() < friendsByMember[mFriend2[i]].size()) {
    			union(mFriend2[i], mFriend1[i]);    			
    		} else {
    			union(mFriend1[i], mFriend2[i]);
    		}
    	}
    	
    	for(int i=0; i<N; i++) {
    		setPower(i);
    	}
    	
		return;
	}
	
	void setPower(int id) {
		int[] used = new int[N];				
		used[id] = 1;
		
		Member member = memberInfo[id];
		int sum1 = 0;
		int sum2 = 0;
		int sum3 = 0;
		int pId = find(id);
		
		
		for(int first : friendsByMember[id]) {
			if(used[first] == 1) continue;
			sum1 += memberInfo[first].pPower;
			used[first] = 1;
			for(int second : friendsByMember[first]) {
				if(friendsByMember[id].contains(second)) continue;
				if(used[second] == 1) continue;
				sum2 += memberInfo[second].pPower;
				used[second] = 1;
			}
		}
		
		
		
		for(int third : memberByGroup[pId]) {
			if(used[third] == 1) continue;
			sum3 += memberInfo[third].pPower;
			used[third] = 1;
		}
		
		int totalPower = member.pPower * 10 + sum1 * 5  + sum2 * 3 + sum3 * 2;
		
		
		influences.remove(member);
		member.totalPower  = totalPower;
		
		influences.put(member, totalPower);
		
		return;
	}
	
	
	
	
	private Comparator<? super Member> ordered() {
		return (o1, o2) -> o1.totalPower == o2.totalPower ? Integer.compare(o1.id, o2.id) : Integer.compare(o2.totalPower, o1.totalPower);
	}
	
	int find(int a) {
		if(a==parent[a]) return a;
		return find(parent[a]);
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		
		parent[pb] = pa;
		memberByGroup[pa].addAll(memberByGroup[pb]); 
	}
	
	
	public int influencer(int mRank) {
		int idx = 1;
		for(Map.Entry<Member, Integer> entry : influences.entrySet()) {
			if(idx == mRank) {
				return entry.getKey().id;
			}
			idx++;
		}
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
		int totalPower;
		
		public Member(int id, int pPower, int totalPower) {
			this.id = id;
			this.pPower = pPower;
			this.totalPower = totalPower;
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