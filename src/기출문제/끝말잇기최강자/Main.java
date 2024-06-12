package 기출문제.끝말잇기최강자;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class Solution {
  
	private final static int MAXN = 50000;
	private final static int WORD_MAXLEN = 11;
		
	HashSet<String> used;
	ArrayList<Integer> player;
	PriorityQueue<String>[] wordQueue;
	
	ArrayList<String> reversedWord;

	public void init(int N, int M, char[][] words) {
		
		used = new HashSet<>();
		player = new ArrayList<>();
		wordQueue = new PriorityQueue[26];
		
		for(int i=0; i<26; i++) {
			wordQueue[i] = new PriorityQueue<>();
		}
		for(int i=1; i<N+1; i++) 
			player.add(i);
		
		for(int i=0; i<M; i++) {
			int cnum = words[i][0] - 'a';
			String word = String.valueOf(words[i]);
			wordQueue[cnum].add(word);
		}
		
		
      	return; 
	}
	
	public int playRound(int pid, char ch) {
		int idx = getIndex(pid);		
		int cnum = ch - 'a';
		reversedWord = new ArrayList<>();
		
		while(!wordQueue[cnum].isEmpty()) {
			String found = foundBy(cnum);
			
			if(found.equals("")) 
				break;
			
			idx++;
			if(idx == player.size())
				idx = 0;
			
			used.add(found);
			
			cnum = found.charAt(found.length() - 1) - 'a';
			
			StringBuffer sb = new StringBuffer(found);
			String rWord = sb.reverse().toString();
			if(used.contains(rWord)) continue;			
			reversedWord.add(rWord);
		}
		
		for(String rWord : reversedWord) {
			int newCnum = rWord.charAt(0) - 'a';
			wordQueue[newCnum].add(rWord);
		}
		
		int ans = player.get(idx);
		player.remove(idx);
		
		return ans;
	}
	
	int getIndex(int pid) {
		int start = 0;
		int end = player.size() - 1;
		while(start <= end) {
			int mid = (start + end) / 2;
			int ret = player.get(mid); 
			if(ret == pid) return mid;
			else if(ret < pid) start = mid + 1;
			else end = mid - 1;
		}
		
		return 0;
	}
	
	String foundBy(int cnum) {
		while(!wordQueue[cnum].isEmpty()) {
			String w = wordQueue[cnum].remove();
			if(used.contains(w)) continue;
			return w;
		}
		
		return "";
	}
	
	
}


public class Main {
	private final static int MAX_N = 50000;
	private final static int MAX_M = 50000;
	private final static int WORD_MAXLEN = 11;

	private final static Solution solution = new Solution();

	private static char[][] words = new char[MAX_M][WORD_MAXLEN];
	private static BufferedReader br;
	private static StringTokenizer st;

	private static boolean run() throws Exception {
		boolean ok = true;
		int N, M;
		int gameCnt;

		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		for (int m = 0; m < M; m++) {
			words[m] = br.readLine().toCharArray();
		}

		solution.init(N, M, words);

		gameCnt = Integer.parseInt(br.readLine());
		for (int i = 0; i < gameCnt; i++) {
			int playerId, ret, ans;
			char startChar;
			st = new StringTokenizer(br.readLine());
			playerId = Integer.parseInt(st.nextToken());
			startChar = st.nextToken().charAt(0);

			ret = solution.playRound(playerId, startChar);

			ans = Integer.parseInt(st.nextToken());
			if (ret != ans)
				ok = false;
		}
		return ok;
	}
	static void print(int q, String cmd, int ans, int ret, Object...o) {
    	//if(ans != ret) System.err.println("---------------------오류------------------------");
    	//System.out.println("["+q+"] " + cmd +  " " + ans + "=" + ret + " [" + Arrays.deepToString(o) + "]" );
    }

	public static void main(String[]args) throws Exception {
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\끝말잇기최강자\\sample_input.txt"));
				
		int T, SUCCESS;
		br = new BufferedReader(new InputStreamReader(System.in));
		st = new StringTokenizer(br.readLine());

		T = Integer.parseInt(st.nextToken());
		SUCCESS = Integer.parseInt(st.nextToken());

		for (int tc = 1; tc <= T; tc++) {
			int score = run() ? SUCCESS : 0;
			System.out.println("#" + tc + " " + score);
		}
		br.close();
		System.out.println("ms => " + (System.currentTimeMillis() - start));
	}
}