package 기출문제.조별경기._01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

class UserSolution
{
	int N;
	int[] parent;
	int[] scores;
	ArrayList<Integer>[] teams;
	
	int[] addScore;
	int count;
	
	int find(int a) {
		if(a == parent[a]) return a;
		return parent[a] = find(parent[a]);
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		if(pa == pb) return;
		
		if(teams[pa].size() <= teams[pb].size()) {
			parent[pa] = pb;
			teams[pb].addAll(teams[pa]);			
		} else {
			parent[pb] = pa;
			teams[pa].addAll(teams[pb]);
		}
	}
	
	void setTeam(int pa, int pb) {
		//parent[pb] = pa;
		//for(int idx : )
	}
	
    public void init(int N) {
    	this.N = N;
    	
    	parent = new int[N+1];
    	scores = new int[N+1];
    	//addScore = new int[N+1];
    	teams = new ArrayList[N+1];
    	count = 0;
    	for(int i=0; i<N+1; i++) {
    		parent[i] = i;
    		teams[i] = new ArrayList<>();    		
    		teams[i].add(i);
    	}
    	
    	return;
    }
    // 50,000
    public void updateScore(int mWinnerID, int mLoserID, int mScore) {
    	
    	int pa = find(mWinnerID);
    	int pb = find(mLoserID);
    	/*
    	
    	for(int idx : teams[pa]) {
    		scores[idx] += mScore;
    	}
    	
    	for(int idx : teams[pb]) {
    		scores[idx] -= mScore;
    	}
    	*/
    	    	
    	return;
    	
    }
    // < N
    public void unionTeam(int mPlayerA, int mPlayerB) {
    	//count++;
    	
    	union(mPlayerA, mPlayerB);
    	return;
    }
    // 50,000
    public int getScore(int mID) {

        return scores[mID];
    }
}

public class Main {
    private static BufferedReader br;
    private static final UserSolution userSolution = new UserSolution();

    private final static int CMD_INIT = 100;
    private final static int CMD_UPDATE_SCORE = 200;
    private final static int CMD_UNION_TEAM = 300;
    private final static int CMD_GET_SCORE = 400;

    private static boolean run() throws IOException
    {
        int queryCnt = Integer.parseInt(br.readLine());
        boolean okay = false;
        int res, ans;

        for (int i = 0; i < queryCnt; i++)
        {
            StringTokenizer stdin = new StringTokenizer(br.readLine());
            switch (Integer.parseInt(stdin.nextToken()))
            {
                case CMD_INIT:
                    int N = Integer.parseInt(stdin.nextToken());
                    userSolution.init(N);
                    okay = true;
                    break;
                case CMD_UPDATE_SCORE:
                    int mWinnerID = Integer.parseInt(stdin.nextToken());
                    int mLoserID = Integer.parseInt(stdin.nextToken());
                    int mScore = Integer.parseInt(stdin.nextToken());
                    userSolution.updateScore(mWinnerID, mLoserID, mScore);
                    break;
                case CMD_UNION_TEAM:
                    int mPlayerA = Integer.parseInt(stdin.nextToken());
                    int mPlayerB = Integer.parseInt(stdin.nextToken());
                    userSolution.unionTeam(mPlayerA, mPlayerB);
                    break;
                case CMD_GET_SCORE:
                    int mID = Integer.parseInt(stdin.nextToken());
                    res = userSolution.getScore(mID);
                    ans = Integer.parseInt(stdin.nextToken());
                    print(i, "getScore", ans, res, mID);
                    if (ans != res)
                    {
                        okay = false;
                    }
                    break;
            }
        }

        return okay;
    }
    static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
    public static void main(String[] args) throws IOException {
    	long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\조별경기\\sample_input.txt"));
	
        br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

        int T, MARK;
        T = Integer.parseInt(stinit.nextToken());
        MARK = Integer.parseInt(stinit.nextToken());

        for (int tc = 1; tc <= T; tc++)
        {
            int score = run() ? MARK : 0;
            System.out.printf("#%d %d\n", tc, score);
        }

        br.close();
    	System.out.println("ms => " + (System.currentTimeMillis() - start));
    }
}