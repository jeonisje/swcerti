package 기출문제.자유무역협정._01;


import java.io.*;
import java.util.*; 

class UserSolution {	
	int numA, numB;
	HashMap<String, Integer> idToSeq;
	
	int[] parent;	
	Company[] companyInfo;
	
	HashSet<Integer>[] graph;
	
 	int sequence;
 	
 	int companyCount;
	
	public void init(int numA, char[][] listA, int numB, char[][] listB) {
		 this.numA = numA;
		 this.numB = numB;
		 companyCount = numA + numB;
		 sequence = 0;
		 idToSeq = new HashMap<>();
		 
		 companyInfo = new Company[companyCount + 1];
		 parent = new int[companyCount + 1];
		 
		 for(int i=0; i< companyCount + 1; i++) {
			 parent[i] = i;
		 }		
		 
		 graph = new HashSet[companyCount + 1];
		 for(int i=0; i<companyCount + 1; i++) {
			 graph[i] = new HashSet<>();
		 }		 
		
		 for(int i=0; i < numA; i++) {
			 sequence++;
			 idToSeq.put(String.valueOf(listA[i]), sequence);
			 companyInfo[sequence] = new Company(sequence, 1, 1, new HashSet<>(Arrays.asList(sequence)));
		 }
		 
		 for(int i=0; i < numB; i++) {
			 sequence++;
			 idToSeq.put(String.valueOf(listB[i]), sequence);
			 companyInfo[sequence] = new Company(sequence, 2, 1, new HashSet<>(Arrays.asList(sequence)));
		 }
		 
	}
	public void startProject(char[] mCompanyA, char[] mCompanyB) {
		int seqA = idToSeq.get(String.valueOf(mCompanyA));
		int seqB = idToSeq.get(String.valueOf(mCompanyB));
		
		graph[seqA].add(seqB);
		graph[seqB].add(seqA);
		
	}
	public void finishProject(char[] mCompanyA, char[] mCompanyB) {
		int seqA = idToSeq.get(String.valueOf(mCompanyA));
		int seqB = idToSeq.get(String.valueOf(mCompanyB));
		
		graph[seqA].remove(seqB);
		graph[seqB].remove(seqA);		
	}
	
	public void ally(char[] mCompany1, char[] mCompany2) {
		int seq1 = idToSeq.get(String.valueOf(mCompany1));
		int seq2 = idToSeq.get(String.valueOf(mCompany2));		
		union(seq1, seq2);
	}
	
	public int conflict(char[] mCompany1, char[] mCompany2) {
		int seq1 = idToSeq.get(String.valueOf(mCompany1));
		int seq2 = idToSeq.get(String.valueOf(mCompany2));		
		
		int total = 0;			
	
		int pSeq1 = find(seq1);	
		int pSeq2 = find(seq2);	
		ArrayList<Integer> relatedCompany1 = new ArrayList<>();
		ArrayList<Integer> relatedCompany2 = new ArrayList<>();
		
		int[] used1 = new int[companyCount + 1];
		HashSet<Integer> companySet1 = companyInfo[pSeq1].companies;
		for(int seq : companySet1) {			
			for(int related : graph[seq]) {
				if(used1[related] == 1) continue;			
				int pRelated = find(related);
				relatedCompany1.add(pRelated);	
				used1[related] = 1;
			}
		}		
		
		int[] used2 = new int[companyCount + 1];
		HashSet<Integer> companySet2 = companyInfo[pSeq2].companies;
		for(int seq : companySet2) {			
			for(int related : graph[seq]) {
				if(used2[related] == 1) continue;
				int pRelated = find(related);				
				relatedCompany2.add(pRelated);				
				used2[related]  = 1;
			}
		}	
		
		int[] used3 = new int[companyCount + 1];
		for(int related1 : relatedCompany1) {
			if(used3[related1] == 1) continue;
			if(relatedCompany2.contains(related1)) {				
				total += companyInfo[related1].companies.size();
			}
			used3[related1] = 1;	
		}
	
		if(total > 0) return total;
		
		HashSet<Integer> project1 = graph[seq1];
		HashSet<Integer> project2 = graph[seq2];
		
		HashSet<Integer> otherPjt = new HashSet<Integer>();
		for(int pjt1 : project1) {
			for(int pjt2 : project2) {
				int p1 = find(pjt1);
				int p2 = find(pjt2);				
				if(p1 == p2) otherPjt.add(p1);				
			}
		}
		
		for(int seq : otherPjt) {
			total += companyInfo[seq].companies.size();
		}
		
		return  total;
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
		companyInfo[pa].allyCount += 1;
		
		companyInfo[pa].companies.addAll(companyInfo[pb].companies);
	}
	
	class Company {
		int seq;
		int country;
		int allyCount;
		
		HashSet<Integer> companies;
		
		public Company(int seq, int country, int allyCount, HashSet<Integer> companies) {
			this.seq = seq;
			this.country = country;
			this.allyCount = allyCount;
			this.companies = companies;
		}
	}
}

public class Main {
	private static final int CMD_INIT 			= 100;
	private static final int CMD_START_PROJECT	= 200;
	private static final int CMD_FINISH_PROJECT = 300;
	private static final int CMD_ALLY			= 400;
	private static final int CMD_CONFLICT 		= 500;
	
	private static final int MAXN = 500;
	private static final int MAXL = 11; 

	private static UserSolution userSolution = new UserSolution();
	static BufferedReader br;
	static StringTokenizer st;

	public static boolean run(BufferedReader br) throws IOException {
		
		int Q;
		int numA, numB;
		boolean okay = false;  
		
		char[][] listA = new char[MAXN][MAXL];
		char[][] listB = new char[MAXN][MAXL];
		
		char[] mCompanyA = new char[MAXL];
		char[] mCompanyB = new char[MAXL];
		char[] mCompany1 = new char[MAXL];
		char[] mCompany2 = new char[MAXL];
		
		int ret = -1;
		int ans; 
		
		Q = Integer.parseInt(br.readLine());

		for (int q = 0; q < Q; q++) {
			st = new StringTokenizer(br.readLine());
			int cmd = Integer.parseInt(st.nextToken());
			
			switch (cmd) {
			case CMD_INIT:
				numA = Integer.parseInt(st.nextToken());
				for(int i = 0; i < numA; i++)
					listA[i] = st.nextToken().toCharArray();
				numB = Integer.parseInt(st.nextToken());
				for(int i = 0; i < numB; i++)
					listB[i] = st.nextToken().toCharArray();
				userSolution.init(numA, listA, numB, listB);
				okay = true; 
				break;
			case CMD_START_PROJECT:
				mCompanyA = st.nextToken().toCharArray();
				mCompanyB = st.nextToken().toCharArray();
				userSolution.startProject(mCompanyA, mCompanyB); 
				print(q, "startProject", q, q, mCompanyA, mCompanyB);
				break;
			case CMD_FINISH_PROJECT:
				mCompanyA = st.nextToken().toCharArray();
				mCompanyB = st.nextToken().toCharArray();
				userSolution.finishProject(mCompanyA, mCompanyB); 
				print(q, "finishProject", q, q, mCompanyA, mCompanyB);
				break;
			case CMD_ALLY:
				mCompany1 = st.nextToken().toCharArray();
				mCompany2 = st.nextToken().toCharArray();
				userSolution.ally(mCompany1, mCompany2); 
				print(q, "ally", q, q, mCompany1, mCompany2);
				break;
			case CMD_CONFLICT:
				mCompany1 = st.nextToken().toCharArray();
				mCompany2 = st.nextToken().toCharArray();
				ret = userSolution.conflict(mCompany1, mCompany2); 
				ans = Integer.parseInt(st.nextToken());
				print(q, "conflict", ans, ret, mCompany1, mCompany2);
				if(ret != ans)
					okay = false; 
				break;
			default :
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

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\자유무역협정\\sample_input.txt"));
		br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());
		int TC = Integer.parseInt(st.nextToken());
		int MARK = Integer.parseInt(st.nextToken());

		for (int tc = 1; tc <= TC; ++tc) {
			boolean result = run(br);
			int score = result ? MARK : 0;
			System.out.println("#" + tc + " " + score);
		}
		
		System.out.println("estimated=> " + (System.currentTimeMillis() - start));
	}
}