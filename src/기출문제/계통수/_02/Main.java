package 기출문제.계통수._02;


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
	
	ArrayList<Integer>[] graph;
	
	int sequence;
	int groupId;
	
	public void init(char[] mRootSpecies) {
		
		idToSeq = new HashMap<String, Integer>();
		speciesInfo = new Species[MAX_N];
		
		graph = new ArrayList[MAX_N];
		speciesByLevel  = new ArrayList[MAX_LEVEL];
	
		sequence = 0;
		groupId = 0;
		
		graph[sequence] = new ArrayList<>();
		speciesByLevel[sequence] = new ArrayList<>();
		
		String rootSpecies = String.valueOf(mRootSpecies);
		idToSeq.put(rootSpecies, 0);		

		speciesInfo[sequence] = new Species(sequence, sequence, 0, 0, groupId);
		speciesByLevel[sequence].add(sequence);
	}

	public void add(char[] mSpecies, char[] mParentSpecies) {
		
		sequence++;
		String species = String.valueOf(mSpecies);
		String parentSpecies = String.valueOf(mParentSpecies);
		
		idToSeq.put(species, sequence);
		
		graph[sequence] = new ArrayList<>();
		
		int pSeq = idToSeq.get(parentSpecies);
		int level = speciesInfo[pSeq].level + 1;
		
		if(speciesByLevel[level] == null) speciesByLevel[level] = new ArrayList<>();
		int group = 0;
		if(level == 1) {
			groupId++;
			group = groupId;
		} else {
			group = speciesInfo[pSeq].group;
		}
		
		speciesInfo[sequence] = new Species(sequence, pSeq, 0, level, group);
		speciesByLevel[level].add(sequence);		
	
		speciesInfo[pSeq].count += 1;					

		graph[pSeq].add(sequence);
		
	}
	
	public int getDistance(char[] mSpecies1, char[] mSpecies2) {
		String species1 = String.valueOf(mSpecies1);
		String species2 = String.valueOf(mSpecies2);
		
		int seq1 = idToSeq.get(species1);
		int seq2 = idToSeq.get(species2);
		
		int group1 = speciesInfo[seq1].group;
		int group2 =  speciesInfo[seq2].group;
		
		int level1 = speciesInfo[seq1].level;
		int level2 = speciesInfo[seq2].level;

		if(group1 == group2) {
			
			if(level1 == level2) {				
				return findCommonParent(seq1, seq2);
			}
			
			int levelDiff = Math.abs(level1 - level2);
			
			int newSeq = 0;
			if(level1 > level2) {
				newSeq = findParentByLevel(seq1, level2);		
				if(newSeq == seq2) return 1;
				return levelDiff + findCommonParent(newSeq, seq2);
			} else {
				newSeq = findParentByLevel(seq2, level1);		
				if(seq1 == newSeq) return 1;
				return levelDiff + findCommonParent(seq1, newSeq);
			}
		} else {
			return level1 + level2;
		}
		
		
	}
	
	int findCommonParent(int seq1, int seq2) {
		int count = 1;
		int se1 = seq1;
		int se2 = seq2;
		while(speciesInfo[se1].pSeq != speciesInfo[se2].pSeq) {
			se1 = speciesInfo[se1].pSeq;
			se2 = speciesInfo[se2].pSeq;
			count++;
		}
		
		return count * 2;
	}
	
	int findParentByLevel(int seq, int targetLevel) {
		
		int se1 = seq;
		
		while(speciesInfo[se1].level != targetLevel) {
			se1 = speciesInfo[se1].pSeq;			
		}
		
		return se1;
	}
	
	public int getCount(char[] mSpecies, int mDistance) {
		
		String species = String.valueOf(mSpecies);
		int seq = idToSeq.get(species);
		int total = 0;
		
		Species sInfo = speciesInfo[seq];
		int level = sInfo.level;
		
		if(level == 0) {		
			if( speciesByLevel[mDistance] == null) return 0;
			return	 speciesByLevel[mDistance].size();
		}
		
		if(mDistance == 1) {
			return graph[seq].size() + 1;
		} 
		//return 0;
		//return bfs(seq, mDistance);		
		
		//int downCount = bfs(seq, mDistance - 1);
		int downCount = 0;
		int upCount = 0;
		
		int diff = level - mDistance;
		if(diff > 0) {
			int findSeq = seq;
			for(int i=0; i<diff - 1; i++) {
				findSeq = speciesInfo[findSeq].pSeq;		
				//upCount += bfs(findSeq, diff - 1);
			}
			//upCount = graph[findSeq].size();
		} else if (diff == 0) {
			upCount = graph[0].size() - 1;
		} else {
			int newLevel = Math.abs(diff) - 1;
			
			if(newLevel == 0) {
				return graph[0].size() - 1 + downCount;
			}
			
		/*
			if(speciesByLevel[newLevel] != null) {
				for(int seqByLevel : speciesByLevel[newLevel]) {
					
					if(speciesInfo[seqByLevel].group == sInfo.group) continue;
					if(graph[seqByLevel] == null) continue;
					upCount += graph[seqByLevel].size();
				}
			}	*/
		}
		
		return downCount + upCount;
		
	}
	
	
	int bfs(int startSeq, int distance) {
		int[] visited = new int[MAX_N];
		
		ArrayDeque<Integer> q = new ArrayDeque<>();
		q.add(startSeq);
		visited[startSeq] = 1;
		
		int total = 0;
		while(!q.isEmpty()) {
			int cur = q.poll();
			
			if(visited[cur] == distance) {				
				total += speciesInfo[cur].count;
				continue;
			}
			
			if(visited[cur] > distance) {	
				continue;
			}
			
			
			if(graph[cur] == null) continue;
			for(int next : graph[cur]) {				
				if(visited[next] != 0) continue;
				if(next == 0) continue;
				q.add(next);
				visited[next] = visited[cur] + 1;
			}
		}
		
		return total;
	}
	
	
	class Species {
		int seq;		
		int pSeq;
		int count;
		int level;
		int group;
		public Species(int seq, int pSeq, int count, int level, int group) {
			this.seq = seq;
			this.pSeq = pSeq;
			this.count = count;
			this.level = level;
			this.group = group;
		}
			
	}
	
	class Node {
		int seq;
		int mark;
		public Node(int seq, int mark) {
			this.seq = seq;
			this.mark = mark;
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
		//if(ans!=ret)  System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
    
	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		
		int TC, MARK;
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\계통수\\sample_input.txt"));
		
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