package 기출문제.계통수;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

class UserSolution {
  
	final int MAX_N = 50_002;
	final int MAX_LEVEL = 3_002;
	//final int MAX_N = 20;
	
	HashMap<String, Integer> idToSeq;
	Species[] speciesInfo;
	
	ArrayList<Integer>[] speciesByLevel;
	
	ArrayList<Species>[] graph;
	ArrayList<Species>[] summaryGraph;
	
	int[] parent;
	
	int sequence;
	
	public void init(char[] mRootSpecies) {
		idToSeq = new HashMap<String, Integer>();
		speciesInfo = new Species[MAX_N];
		
		graph = new ArrayList[MAX_N];
		speciesByLevel  = new ArrayList[MAX_LEVEL];
		
		parent = new int[MAX_N];
		for(int i=0; i<MAX_N; i++) {
			parent[i] = i;
		}
		
		sequence = 0;
		
		graph[sequence] = new ArrayList<>();
		speciesByLevel[sequence] = new ArrayList<>();
		
		String rootSpecies = String.valueOf(mRootSpecies).trim();
		idToSeq.put(rootSpecies, 0);		

		speciesInfo[sequence] = new Species(sequence, sequence, 0, 0);
		speciesByLevel[sequence].add(sequence);
	}

	public void add(char[] mSpecies, char[] mParentSpecies) {
		sequence++;
		String species = String.valueOf(mSpecies).trim();
		String parentSpecies = String.valueOf(mParentSpecies).trim();
		
		idToSeq.put(species, sequence);
		
		graph[sequence] = new ArrayList<>();
		
		int pSeq = idToSeq.get(parentSpecies);
		int level = speciesInfo[pSeq].level + 1;
		
		if(speciesByLevel[level] == null) speciesByLevel[level] = new ArrayList<>();
		
		speciesInfo[sequence] = new Species(sequence, pSeq, 0, level);
		speciesByLevel[level].add(sequence);		
	
		speciesInfo[pSeq].count += 1;
		if(pSeq == 0) {
			graph[0].add(speciesInfo[sequence]);
			graph[sequence].add(speciesInfo[0]);
		} else {			
			graph[pSeq].add(speciesInfo[sequence]);
			graph[sequence].add(speciesInfo[pSeq]);
		}
		
		if(pSeq == 0) return;
		union(pSeq, sequence);
		
	}
	
	public int getDistance(char[] mSpecies1, char[] mSpecies2) {
		String species1 = String.valueOf(mSpecies1).trim();
		String species2 = String.valueOf(mSpecies2).trim();
		
		int seq1 = idToSeq.get(species1);
		int seq2 = idToSeq.get(species2);
		
		int pSeq1 = find(seq1);
		int pSeq2 = find(seq2);
		
		int level1 = speciesInfo[seq1].level;
		int level2 = speciesInfo[seq2].level;
		
		int startNode = seq1;
		int endNode = seq2;
		if(level1 > level2) {
			startNode = seq2;
			endNode = pSeq1;
		}
		
		if(pSeq1 == pSeq2) {
			int[] visited = new int[MAX_N];
			
			ArrayDeque<Species> q = new ArrayDeque<>();
			q.add(speciesInfo[seq1]);
			visited[seq1] = 1;
			while(!q.isEmpty()) {
				Species cur = q.poll();
				
				if(cur.seq == seq2) {
					return visited[cur.seq] - 1;
				}
				
				for(Species next : graph[cur.seq]) {
					if(visited[next.seq] != 0) continue;
					if(speciesInfo[next.seq].level == 0) continue;
					
					q.add(next);
					visited[next.seq] = visited[cur.seq] + 1;
				}
			}
		} else {
			return level1 + level2;
		}
		
		/*
		*/
		
		return 0;
	}
	
	public int getCount(char[] mSpecies, int mDistance) {
		String species = String.valueOf(mSpecies).trim();
		int seq = idToSeq.get(species);
		int total = 0;
		
		Species sInfo = speciesInfo[seq];
		int level = sInfo.level;
		
		if(level == 0) {			
			for(int i=1; i<=mDistance; i++) {
				if(speciesByLevel[i] == null) return 0;
				total += speciesByLevel[i].size();
			}
			return total;
		}
		
		if(mDistance == 1) {
			return sInfo.count + 1;
		} 
		
		return bfs(seq, mDistance);		
	}
	
	
	int bfs(int startSeq, int distance) {
		int[] visited = new int[MAX_N];
		
		ArrayDeque<Species> q = new ArrayDeque<>();
		q.add(speciesInfo[startSeq]);
		visited[startSeq] = 1;
		
		int total = 0;
		while(!q.isEmpty()) {
			Species cur = q.poll();
			
			if(visited[cur.seq] == distance) {				
				total += speciesInfo[cur.seq].count;
				continue;
			}
			
			if(visited[cur.seq] > distance) {
				continue;
			}
			if(graph[cur.seq] == null) continue;
			for(Species next : graph[cur.seq]) {				
				if(visited[next.seq] != 0) continue;
				if(next.count == 0) continue;
				//if(next.level < level) continue;
				q.add(next);
				visited[next.seq] = visited[cur.seq] + 1;
			}
		}
		
		return total;
	}
	
	int find(int a) {
		if(a == parent[a]) return a;
		return parent[a] = find(parent[a]);
	}
	
	void union(int a, int b) {
		int pa = find(a);
		int pb = find(b);
		
		if(pa == pb) return;
		
		parent[pb] = pa;
	}
	
	
	class Species {
		int seq;		
		int pSeq;
		int count;
		int level;
		public Species(int seq, int pSeq, int count, int level) {
			super();
			this.seq = seq;
			this.pSeq = pSeq;
			this.count = count;
			this.level = level;
		}			
	}
}

public class Main {
	private static final int MAXL	= 11;

	private static final int CMD_INIT 			= 100;
	private static final int CMD_ADD 			= 200;
	private static final int CMD_GET_DISTANCE	= 300;
	private static final int CMD_GET_COUNT		= 400;

	private static void String2Char(String s, char[] b) {
        int n = s.length();
        for (int i = 0; i < n; ++i)
            b[i] = s.charAt(i);
        for (int i = n; i < MAXL; ++i)
        	b[i] = '\0';
    }
	
    private static UserSolution usersolution = new UserSolution();

    private static boolean run(Scanner sc) throws Exception {
    	int Q;
    	
    	char[] mRootSpecies = new char[MAXL];
    	char[] mSpecies = new char[MAXL];   	
    	char[] mParentSpecies = new char[MAXL];   	
    	char[] mSpecies1 = new char[MAXL];
    	char[] mSpecies2 = new char[MAXL];
    	
    	int mDistance;

    	int ret = -1, ans;

    	Q = sc.nextInt();

    	boolean okay = false;
    	
    	for (int q = 0; q < Q; ++q) {    				
            int cmd = sc.nextInt();
            
            switch(cmd) {
            case CMD_INIT:
            	String2Char(sc.next(), mRootSpecies);           	
            	usersolution.init(mRootSpecies);
            	okay = true;
            	break;
            case CMD_ADD:
            	String2Char(sc.next(), mSpecies);
            	String2Char(sc.next(), mParentSpecies);
            	usersolution.add(mSpecies, mParentSpecies);
            	print(q, "add", q, q, mSpecies, mParentSpecies);
            	break;
            case CMD_GET_DISTANCE:
            	String2Char(sc.next(), mSpecies1);
            	String2Char(sc.next(), mSpecies2);
            	ret = usersolution.getDistance(mSpecies1, mSpecies2);
            	ans = sc.nextInt();
            	print(q, "getDistance", ans, ret, mSpecies1, mSpecies2);
            	if (ans != ret)
            		okay = false;
            	break;
            case CMD_GET_COUNT:
            	String2Char(sc.next(), mSpecies);
            	mDistance = sc.nextInt();
            	ret = usersolution.getCount(mSpecies, mDistance);
            	ans = sc.nextInt();
            	print(q, "getCount", ans, ret, mSpecies, mDistance);
            	if (ans != ret)
            		okay = false;
            	break;
            default:
            	okay = false;
            	break;
            }
            
    	}
 
    	return okay;
    }
    
    static void print(int q, String cmd, int ans, int ret, Object...o) {
		if(ans!=ret)  System.err.println("----------------------오류--------------------");
		System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
    
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		
		int TC, MARK;
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\계통수\\sample_input2.txt"));
		
		Scanner sc = new Scanner(System.in);
		
		TC = sc.nextInt();
        MARK = sc.nextInt();

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(sc) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		sc.close();
		
		System.out.println("estimated=> " + (System.currentTimeMillis() - start));
	}
}