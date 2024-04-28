package 기출문제.상품권분배;


import java.io.*;
import java.util.*;

class UserSolution {
	final int MAX_N = 18_003;
	
	int N;
	HashMap<Integer, Integer> idToSeq;
	
	ArrayList<Department>[] graph;	
	Department[] deptInfo;
	
	int[] parent;	
	int[] removed;
	
	int sequence;	
	int[] countByGroup;	
	int[] countBySeq;
	
    void init(int N, int[]mId, int[]mNum) {
    	this.N = N;
    	graph = new ArrayList[MAX_N];
    	deptInfo = new Department[MAX_N];
    	removed =  new int[MAX_N];
    	parent = new int[MAX_N];
    	countByGroup =  new int[N + 1];
    	countBySeq =  new int[MAX_N];
    			
    	for(int i=0; i<MAX_N; i++) {
    		graph[i] = new ArrayList<>();
    		parent[i] = i;
    	}
    	
    	idToSeq = new HashMap<Integer, Integer>();
    	
    	sequence=0;
    	
    	for(int i=0; i<N; i++) {
    		sequence++;
    		idToSeq.put(mId[i], sequence);
    		Department d = new Department(sequence, mNum[i]);
    		graph[0].add(d);
    		deptInfo[sequence] = d;
    		
    		countByGroup[sequence] = mNum[i];
    	}
    	
    }

    int add(int mId, int mNum, int mParent) {
    	sequence++;
    	idToSeq.put(mId, sequence);
    	
    	int pSeq = idToSeq.get(mParent);
    	
    	int count = 0;
    	//if(graph[pSeq].size() == 3) return -1;
    	for(Department d : graph[pSeq]) {
    		if(removed[d.seq] == 1) continue;
    		count++;
    	}
    	
    	if(count >= 3) return -1;    	
    	
    	Department d = new Department(sequence, mNum, pSeq);
    	
    	graph[pSeq].add(d);
    	deptInfo[sequence] = d;
    	
    	
    	union(pSeq, sequence);
    	int rootSeq = find(sequence);
    	
    	countByGroup[rootSeq] += mNum;    	
    	countBySeq[sequence] = mNum;
    	
    	if(pSeq == rootSeq) {
    		return countByGroup[rootSeq];
    	} else {
    		boolean meetRoot = false;
    		int parentSeq = pSeq;
    		while(parentSeq != rootSeq) {    			
    			countBySeq[parentSeq] += mNum;
    			parentSeq = deptInfo[parentSeq].pSeq;
    		}
    	}
    
    	return countBySeq[pSeq];
    	
    }    
    
    int remove(int mId) {
    	if(!idToSeq.containsKey(mId)) return -1;
    	int seq = idToSeq.get(mId);
    	if(removed[seq] == 1) return -1;
    	
    	removed[seq] = 1;
    	
    	int count = bfsForRemove(seq);    	
    	int rootSeq = find(seq);    	
    	int pSeq = deptInfo[seq].pSeq;
    	
    	if(rootSeq != pSeq) {
    		int parentSeq = pSeq;
    		while(parentSeq != rootSeq) {
    			countBySeq[parentSeq] -= count;
    			parentSeq = deptInfo[parentSeq].pSeq;
    		}    		
    	}
    	
    	countByGroup[rootSeq] -= count;
    	
    	return count;
    }
    
    int bfsForRemove(int startSeq)  {
    	int total = 0;
    	ArrayDeque<Department> q = new ArrayDeque<>();
    	q.add(new Department(startSeq, deptInfo[startSeq].count));
    	
    	int[] visited = new int[MAX_N];
    	visited[startSeq] = 1;
    	while(!q.isEmpty()) {
    		Department cur = q.poll();
    		removed[cur.seq] = 1;
    		total += cur.count;
    		for(Department next : graph[cur.seq]) {
    			if(visited[next.seq] == 1) continue;
    			if(removed[next.seq] == 1) continue;
    			
    			q.add(next);
    			visited[next.seq] = 1;
    		}
    	}
    	return total;
    }
    
    
    int distribute(int K) {
    	
    	int max1 = 0;
    	int max2 = 0;
    	int min = Integer.MAX_VALUE;
    	ArrayList<Integer> list = new ArrayList<>();  
    	int total = 0;
    	
    	for(int i=1; i<N+1; i++) {
    		min = Math.min(min, countByGroup[i]);
    		max1 = Math.max(max1, countByGroup[i]);
    		total += countByGroup[i];
    	}
    	
    	if(total <= K) return max1;
    	
    	max2 = binarySearch(K, min);
    	
    	return max2;
    }
    
    int binarySearch(int K, int min) {
    	int start = 0;
    	int end = K;
    	int mid = 0;
    	int total = 0;
    	int maxTotal = 0;
    	int maxLimit = 0;
    	while(start <= end) {
    		total = 0;
    		mid = (start + end) / 2;
    		    		
    		for(int count : countByGroup) {
    			if(count <= mid) total += count;
    			else total += mid;
    		}
    		
    		if(total > K) {
    			end = mid - 1;
    			
    		} else if (total < K)  {
    			start = mid + 1;
    			if(total > maxTotal) {
    				maxTotal = total;
    				maxLimit = mid;
    			}
    		} else {
    			return mid;
    		}
    		
    	}
    	return maxLimit;
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
    
    class Department {
    	int seq;
    	int count;    	
    	int pSeq;
		public Department(int seq, int count) {
			this.seq = seq;
			this.count = count;
		}
		public Department(int seq, int count, int pSeq) {		
			this.seq = seq;
			this.count = count;
			this.pSeq = pSeq;
		}    	
    }
}
class Main {
	private static final int CMD_INIT				= 1;
	private static final int CMD_ADD				= 2;
	private static final int CMD_REMOVE				= 3;
	private static final int CMD_DISTRIBUTE			= 4; 

	private static UserSolution usersolution = new UserSolution();
	
	
	private static boolean run(BufferedReader br) throws Exception {
		int Q;
	
        StringTokenizer stdin = new StringTokenizer(br.readLine(), " ");

		Q = Integer.parseInt(stdin.nextToken());
		
		int[] midArr = new int[1000]; 
		int[] mnumArr = new int[1000]; 
		int mid, mnum, mparent, n, k;
		int cmd, ans, ret = 0; 
		boolean okay = false;
		
		for (int q = 0; q < Q; ++q) {
            stdin = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(stdin.nextToken());
			
			switch(cmd) {
			case CMD_INIT:
				n = Integer.parseInt(stdin.nextToken()); 
				for(int j = 0; j < n; j++) {
					stdin = new StringTokenizer(br.readLine(), " ");
					midArr[j] = Integer.parseInt(stdin.nextToken()); 
					mnumArr[j] = Integer.parseInt(stdin.nextToken()); 
				}
				usersolution.init(n, midArr, mnumArr);
				okay = true;
				break;
			case CMD_ADD:
				mid = Integer.parseInt(stdin.nextToken()); 
				mnum = Integer.parseInt(stdin.nextToken());
				mparent = Integer.parseInt(stdin.nextToken());
				ans = Integer.parseInt(stdin.nextToken()); 
				ret = usersolution.add(mid, mnum, mparent); 
				print(q, "add", ans, ret, mid, mnum, mparent);
				if (ans != ret)
					okay = false;
				break;
			case CMD_REMOVE:
				mid = Integer.parseInt(stdin.nextToken());
				ans = Integer.parseInt(stdin.nextToken());
				ret = usersolution.remove(mid); 
				print(q, "remove", ans, ret, mid);
				if (ans != ret)
					okay = false;
				break;
			case CMD_DISTRIBUTE: 
				k = Integer.parseInt(stdin.nextToken());
				ans = Integer.parseInt(stdin.nextToken());
				ret = usersolution.distribute(k); 
				print(q, "distribute", ans, ret, k);
				if(ans != ret)
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
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		 System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\상품권분배\\sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());
		
		for (int testcase = 1; testcase <= TC; ++testcase)
		{
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		
		br.close();
		
		System.out.println("estimated=> " + (System.currentTimeMillis() - start));
	}
}