package 기출문제.카드게임;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

class Solution {	
	int MAX_PLAYER = 51;	
	int MAX_SUBJECT = 21;
	
	int N;
	
	int[] pointByPlayer;
	int[] countByCard;
	
	HashMap<String, Integer> subjectsToId;
	HashMap<String, Integer> wordToId;
	HashMap<String, Integer> subjectByWord;
	
	String[] wordById;
	
	HashMap<String, HashMap<String, Integer>>[][] cardByPlayer;	// 플레이어별 카드	
	HashMap<String, TreeMap<Card, Integer>>[] searchMap;	
	
	
	int subjectId;
	
  
	public void init(int N, char mWordList[][], char mSubjectList[][]) {
		this.N = N;
		
		subjectId = 1;
		pointByPlayer = new int[MAX_PLAYER];
		countByCard = new int[N+1];
		wordById = new String[N+1];
		
		subjectsToId = new HashMap<>();
		wordToId = new HashMap<>();
		subjectByWord = new HashMap<>();
		
		cardByPlayer = new HashMap[MAX_PLAYER][MAX_SUBJECT];
		for(int i=0; i<MAX_PLAYER; i++) {
			for(int j=0; j<MAX_SUBJECT; j++) {
				cardByPlayer[i][j] = new HashMap<>();
			}
		}
		
		searchMap = new HashMap[MAX_SUBJECT];
		for(int i=0; i<MAX_SUBJECT; i++) {
			searchMap[i] = new HashMap<>();
		}
		
		for(int i=0; i<N; i++) {
			String word = String.valueOf(mWordList[i]).trim();
			String subject = String.valueOf(mSubjectList[i]).trim();
			
			int subId = 0;
			if(subjectsToId.containsKey(subject)) {
				subId = subjectsToId.get(subject);
			} else {
				subId = subjectId++;
				subjectsToId.put(subject, subId);
			}		
			subjectByWord.put(word, subId);
			wordToId.put(word, i+1);
			wordById[i+1] = word;
		}
		
		
		return; 	 
	}
	
	// 50
	public void join(int mID, int M, int mCardList[]) {
		
		for(int i=0; i<M; i++) {
			String word = wordById[mCardList[i]];
			String key1 = word.substring(0, 1);
			String key2 = word.substring(0, 2);
			
			int subId = subjectByWord.get(word);
			
			// 플레이어 카드 세팅
			HashMap<String, Integer> hm = cardByPlayer[mID][subId].getOrDefault(key1, new HashMap<>());
			//if(hm.containsKey(key2)
		}
		
		// 전체카드 정보 세팅
		return; 
	}
	
	// 1_500
	public int playRound(char mBeginStr[], char mSubject[]) {
		 return 0;
	}
	
	// 50
	public int leave(int mID) {
		 return 0;
	}
	
	class Card {
		String word;
		int id;
		int playerCount;
		public Card(String word, int id, int playerCount) {
			this.word = word;
			this.id = id;
			this.playerCount = playerCount;
		}
	}
}


public class Main {

	private final static int CMD_INIT = 100;
    private final static int CMD_JOIN = 200;
    private final static int CMD_PLAY_ROUND = 300;
    private final static int CMD_LEAVE = 400;
    
	private final static int MAXN = 10000;
	private final static int MAXM = 1500;
	private final static int MAXL = 10;

    
	private final static Solution userSolution = new Solution();
	
	private static void String2Char(char[] buf, String str) {
		for (int k = 0; k < str.length(); ++k)
			buf[k] = str.charAt(k);
		buf[str.length()] = '\0';
	}
	
	private static char mWordList[][];
	private static char mSubjectList[][];
	private static int[] mCardList;
	private static char mBeginStr[];
	private static char mSubject[];
	
	private static BufferedReader br;
	private static StringTokenizer st;
	 

	private static boolean run() throws Exception {
		int numQuery, cmd; 
		int N, mID, M;
		int userAns, ans; 
		boolean isCorrect = false;
		
		st = new StringTokenizer(br.readLine());
		numQuery = Integer.parseInt(st.nextToken());
		
		for(int i = 0; i < numQuery; i++) {
			st = new StringTokenizer(br.readLine());
			cmd = Integer.parseInt(st.nextToken());
			
			switch(cmd) 
			{
			case CMD_INIT:
				mWordList = new char[MAXN][MAXL+1];
				mSubjectList = new char[MAXN][MAXL+1];
				N = Integer.parseInt(st.nextToken());
				for(int j = 0; j < N; j++) 
					String2Char(mWordList[j],st.nextToken());
				for(int j = 0; j < N; j++) 
					String2Char(mSubjectList[j],st.nextToken());
				userSolution.init(N, mWordList, mSubjectList); 
				isCorrect = true;
				break;
			case CMD_JOIN :
				mCardList = new int[MAXM];
				mID = Integer.parseInt(st.nextToken());
				M = Integer.parseInt(st.nextToken());
				for(int j=0; j<M; j++) {
					mCardList[j] = Integer.parseInt(st.nextToken());
				}
				userSolution.join(mID, M, mCardList);
				break; 
			case CMD_PLAY_ROUND :
				mBeginStr = new char[3];
				mSubject = new char[MAXL+1];
				String2Char(mBeginStr,st.nextToken());
				String2Char(mSubject,st.nextToken());
				userAns = userSolution.playRound(mBeginStr, mSubject);
				ans = Integer.parseInt(st.nextToken());
				if(userAns != ans)
					isCorrect = false;
				break;
			case CMD_LEAVE : 
				mID = Integer.parseInt(st.nextToken());
				userAns = userSolution.leave(mID);
				ans = Integer.parseInt(st.nextToken());
				if(userAns != ans)
					isCorrect = false;
				break;
			default:
				isCorrect = false;
				break;
			}
		}
		return isCorrect; 
	}
	static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("----------------------오류--------------------");
		//System.out.println("["+q+"] " +  cmd + ":" + ans + "=" + ret + "(" + Arrays.deepToString(o)+")");
	}
	
	public static void main(String[]args) throws Exception {
		
		long start = System.currentTimeMillis();
		System.setIn(new java.io.FileInputStream("C:\\sw certi\\workspace\\swcerti\\src\\기출문제\\카드게임\\sample_input.txt"));


		br = new BufferedReader(new InputStreamReader(System.in));
		
		int T, SUCCESS;
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
