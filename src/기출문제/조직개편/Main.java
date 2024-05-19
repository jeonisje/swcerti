package 기출문제.조직개편;


import java.io.*;
import java.util.*;

class UserSolution {
	int MAX = 10000;
	
	HashMap<Integer, Integer> idToSeq;
	ArrayList<Node>[] graph;
	
	int[] removed;
	int sequence;
	Node[] nodes;
	int rootId;
	int[] childrenSumBySeq;
	int[] numBySeq;
	int totalCount;
	int totalNode;
  
    public void init(int mId, int mNum) {
        sequence = 0;
        graph = new ArrayList[MAX];
        for(int i=0; i<MAX; i++) {
        	graph[i] = new ArrayList<>();
        }
        
        removed = new int[MAX];
        idToSeq = new HashMap<>();
        idToSeq.put(mId, sequence);
        
        rootId = mId;
        
        nodes = new Node[MAX];
        nodes[sequence] = new Node(sequence, mNum, sequence);
        childrenSumBySeq = new int[MAX];
        numBySeq = new int[MAX];
        
        totalCount = mNum;
        totalNode++;
        childrenSumBySeq[sequence] = mNum;
        numBySeq[sequence] = mNum;
    }

    public int add(int mId, int mNum, int mParent) {    
    	int pSeq = idToSeq.get(mParent);    	
    
    	if(graph[pSeq].size() <= 2)  {
    		int ccount = 0;
    		for(Node child : graph[pSeq]) {
    			if(removed[child.seq] == 0) ccount++;
    		}
    		
    		if(ccount >= 2) return -1;
    	}
    	
    	sequence++;
    	idToSeq.put(mId, sequence);
    	Node node = new Node(sequence, mNum, pSeq);
    	graph[pSeq].add(node);
    	nodes[sequence] = node;    	
    	totalCount += mNum;    	
    
    	totalNode++;
    	childrenSumBySeq[sequence] = mNum;
    	numBySeq[sequence] = mNum;
    
    	addChildNum(sequence, mNum);
        return childrenSumBySeq[pSeq];
    }
    
    void leftSum(int num) {
    	
    }
    
    void addChildNum(int seq, int num) {
    	if(seq == 0) return;
    	int pSeq = nodes[seq].pSeq;
    	childrenSumBySeq[pSeq] += num;
    	addChildNum(pSeq, num);
    }

    public int remove(int mId) {
    	if(!idToSeq.containsKey(mId)) return -1;
    	int seq = idToSeq.get(mId);
    	if(removed[seq] == 1) return -1;    	
    	
    	ArrayDeque<Node> q = new ArrayDeque<>();
    	q.add(nodes[seq]);    	
    	
    	int count = 0;
    	while(!q.isEmpty()) {
    		Node cur = q.poll();
    		count += cur.count;
    		removed[cur.seq] = 1;
    		totalNode--;		
    		for(Node next : graph[cur.seq]) {
    			if(removed[next.seq] == 1) continue;
    			q.add(next);
    		}
    	}
    	addChildNum(seq, -count);
    	totalCount -= count;
    	 
        return count;
    }

    public int reorganize(int M, int K) {
    	ArrayDeque<Node> q = new ArrayDeque<>();
    	q.add(nodes[0]);   	
    	
    	
    	int count = 0;
    	while(!q.isEmpty()) {
    		Node cur = q.poll();
    		count += cur.count;
    		
    		for(Node next : graph[cur.seq]) {
    			if(removed[next.seq] == 1) continue;
    			q.add(next);
    		}
    	}
    	
    	
        return 0;
    }
    
    
    public int binarySearch(int M, int target) {
    	
    	return 0;
    }
    
    class Node {
    	int seq;
    	int count;
    	int pSeq;
		public Node(int seq, int count, int pSeq) {		
			this.seq = seq;
			this.count = count;
			this.pSeq = pSeq;
		}
    }
}


public class Main {
	private final static int CMD_INIT = 1;
	private final static int CMD_ADD = 2;
	private final static int CMD_REMOVE = 3;
	private final static int CMD_REORGANIZE = 4;

	private final static UserSolution usersolution = new UserSolution();

	private static boolean run(BufferedReader br) throws Exception {
		int q = Integer.parseInt(br.readLine());

		int mid, mnum, mparent, m, k;
		int cmd, ans, ret = 0;
		boolean okay = false;

		for (int i = 0; i < q; ++i) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			cmd = Integer.parseInt(st.nextToken());
			switch (cmd) {
				case CMD_INIT:
					mid = Integer.parseInt(st.nextToken());
					mnum = Integer.parseInt(st.nextToken());
					usersolution.init(mid, mnum);
					okay = true;
					break;
				case CMD_ADD:
					mid = Integer.parseInt(st.nextToken());
					mnum = Integer.parseInt(st.nextToken());
					mparent = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.add(mid, mnum, mparent);
					print(i, "add", ans, ret, mid, mnum, mparent);
					if (ret != ans)
						okay = false;
					break;
				case CMD_REMOVE:
					mid = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.remove(mid);
					print(i, "remove", ans, ret, mid);
					if (ret != ans)
						okay = false;
					break;
				case CMD_REORGANIZE:
					m = Integer.parseInt(st.nextToken());
					k = Integer.parseInt(st.nextToken());
					ans = Integer.parseInt(st.nextToken());
					ret = usersolution.reorganize(m, k);
					print(i, "reorganize", ans, ret, m , k);
					if (ret != ans)
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

		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\조직개편\\sample_input.txt"));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		TC = Integer.parseInt(st.nextToken());
		MARK = Integer.parseInt(st.nextToken());

		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}

		br.close();
		
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}