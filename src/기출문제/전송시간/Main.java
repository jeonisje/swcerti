package 기출문제.전송시간;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class UserSolution {
	
	int N, K;
	
	ArrayList<Node>[][] graph;	// 전체
	ArrayList<Node>[] graphForRoot;	// 루트노드용
	ArrayList<Node>[][] summaryGraph;	// group간 연결
	
	
	int MAX_NODE = 31;
	
	
  
	public void init(int N, int K, int[] mNodeA, int[] mNodeB, int[] mTime) {
		this.N = N;
		this.K = K;
		
		graph = new ArrayList[N+1][MAX_NODE];
		summaryGraph = new ArrayList[N+1][MAX_NODE];		
		
		for(int i=0; i<N+1; i++) {
			for(int j=0; j<MAX_NODE; j++) {
				graph[i][j] = new ArrayList<>();
				summaryGraph[i][j] = new ArrayList<>();
			}
		}
		
		for(int i=0; i<K; i++) {
			int nodeA = mNodeA[i];
			int nodeB = mNodeB[i];
			int time = mTime[i];
			
			int nA = nodeA % 100;
			int nB = nodeB % 100;
			
			int groupA = nodeA / 100;
			int groupB = nodeB / 100;
			
			graph[groupA][nA].add(new Node(groupB, nB, time, false));
			graph[groupB][nB].add(new Node(groupA, nA, time, false));
		}
		
		return; 
	}
	
	void setRootSummary() {
		//summaryGraph
	}
	
	void makeSummary(int group) {
		int[] dist = new int[32];
		Arrays.fill(dist, Integer.MAX_VALUE);
		
		PriorityQueue<Node> q = new PriorityQueue<>((o1, o2) -> Integer.compare(o1.time, o2.time));
		
	}
	 
	public void addLine(int mNodeA, int mNodeB, int mTime) {
	    return ;
	}

	public void removeLine(int mNodeA, int mNodeB) {
	    return ;
	}
	
	public int checkTime(int mNodeA, int mNodeB) {
		return 0;
	}
	
	class Node {
		int group;
		int id;
		int time;
		boolean deleted = false;
		public Node(int group, int id, int time, boolean deleted) {		
			this.group = group;
			this.id = id;
			this.time = time;
			this.deleted = deleted;
		}	
	}
}


public class Main {
  	private static final int CMD_INIT					= 0;
	private static final int CMD_ADD					= 1;
	private static final int CMD_REMOVE					= 2;
	private static final int CMD_CHECK					= 3; 

	private static UserSolution usersolution = new UserSolution();

	private static boolean run(BufferedReader br) throws Exception {
		
		int Q, N, K, ans; 
		int[] nodeA = new int[30000];
		int[] nodeB = new int[30000];
		int[] Time = new int[30000]; 
        StringTokenizer stdin = new StringTokenizer(br.readLine(), " ");

		Q = Integer.parseInt(stdin.nextToken());
		boolean okay = false;
		
		for (int q = 0; q < Q; ++q) {
            stdin = new StringTokenizer(br.readLine(), " ");
			int cmd = Integer.parseInt(stdin.nextToken());
			
			switch(cmd)
			{
			case CMD_INIT:
				N = Integer.parseInt(stdin.nextToken());
				K = Integer.parseInt(stdin.nextToken());
				for(int i = 0; i < K; i++) {
					stdin = new StringTokenizer(br.readLine());
					nodeA[i] = Integer.parseInt(stdin.nextToken());
					nodeB[i] = Integer.parseInt(stdin.nextToken());
					Time[i] = Integer.parseInt(stdin.nextToken());
				}
				usersolution.init(N, K, nodeA, nodeB, Time); 
				okay = true;
				break;
			case CMD_ADD:
				nodeA[0] = Integer.parseInt(stdin.nextToken());
				nodeB[0] = Integer.parseInt(stdin.nextToken());
				Time[0] = Integer.parseInt(stdin.nextToken());
				usersolution.addLine(nodeA[0], nodeB[0], Time[0]);
				print(q, "addLine", q, q, nodeA[0], nodeB[0], Time[0]);
				break;
			case CMD_REMOVE:
				nodeA[0] = Integer.parseInt(stdin.nextToken());
				nodeB[0] = Integer.parseInt(stdin.nextToken());
				usersolution.removeLine(nodeA[0], nodeB[0]);
				print(q, "removeLine", q, q, nodeA[0], nodeB[0]);
				break;
			case CMD_CHECK:
				nodeA[0] = Integer.parseInt(stdin.nextToken());
				nodeB[0] = Integer.parseInt(stdin.nextToken());
				int ret = usersolution.checkTime(nodeA[0], nodeB[0]);
				ans = Integer.parseInt(stdin.nextToken());
				print(q, "removeLine", ans, ret, nodeA[0], nodeB[0]);
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
	private static void print(int num, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("===================오류=======================");
		//System.out.println("[" + num +"] " + cmd + " : " + ans + "=" + ret + "(" +Arrays.deepToString(o)+")");
	}
	public static void main(String[] args) throws Exception {
		Long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\전송시간\\sample_input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

		int TC = Integer.parseInt(stinit.nextToken());
		int MARK = Integer.parseInt(stinit.nextToken());
		
		for (int testcase = 1; testcase <= TC; ++testcase) {
			int score = run(br) ? MARK : 0;
			System.out.println("#" + testcase + " " + score);
		}
		br.close();
		System.out.println("estimated => " + (System.currentTimeMillis() - start));
	}
}
