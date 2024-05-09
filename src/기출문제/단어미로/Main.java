package 기출문제.단어미로;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeSet;

class UserSolution {
	int MAX_WORD = 35_001;
	HashMap<String, Integer> idByWord;
	Word[] wordInfo;
	
	HashMap<Integer, TreeSet<String>> postMap2;
	HashMap<Integer, TreeSet<String>> postMap4;
	HashMap<Integer, TreeSet<String>> midMap;
	HashMap<Integer, TreeSet<String>> preMap2;
	HashMap<Integer, TreeSet<String>> preMap4;
	
	HashMap<String, Integer>[] wordToSeq;
	
	int sequence;
	int current;
	
	void init() {
		idByWord = new HashMap<>();
		wordInfo = new Word[MAX_WORD];
		
		postMap2 = new HashMap<>();
		postMap4 = new HashMap<>();
		midMap = new HashMap<>();
		preMap2 = new HashMap<>();
		preMap4 = new HashMap<>();
		
		wordToSeq = new HashMap[6];
		
		sequence = 0;
		for(int i=0; i<6; i++) {
			wordToSeq[i] = new HashMap<>();
		}
	}

	void addRoom(int mID, char mWord[], int mDirLen[]) {		
		String word = String.valueOf(mWord);
		
		idByWord.put(word, mID);
		Word w = new Word(mID, word, mDirLen);
		wordInfo[mID] = w;
		setWord(word);
		
		return;
	}
	
	
	void setWord(String word) {
		String pre2 =  word.substring(0, 2);
		String pre4 = word.substring(0, 4);
		String mid = word.substring(4, 7);
		String post2 =  word.substring(12 - 3, 11);
		String post4 =   word.substring(12 - 5, 11);	
		
		int seq1 = setWordSeq(pre2, 1);
		int seq2 = setWordSeq(pre4, 2);
		int seq3 = setWordSeq(mid, 3);
		int seq4 = setWordSeq(post2, 4);
		int seq5 = setWordSeq(post4, 5);
		
		setMap(seq1, word, preMap2);
		setMap(seq2, word, preMap4);
		setMap(seq3, word, midMap);
		setMap(seq4, word, postMap2);
		setMap(seq5, word, postMap4);	
	}
	
	
	int setWordSeq(String word, int type) {
		if(wordToSeq[type].containsKey(word)) {
			return wordToSeq[type].get(word);
		}
		sequence++;
		wordToSeq[type].put(word, sequence);
		return sequence;
	}
	void setMap(int seq, String word, HashMap<Integer, TreeSet<String>> map) {
		TreeSet<String> set = map.getOrDefault(seq, new TreeSet<String>((o1, o2) -> o1.compareTo(o2)));
		set.add(word);
		map.put(seq, set);
	}

	void setCurrent(char mWord[]) {
		current = idByWord.get(String.valueOf(mWord));
	}

	int moveDir(int mDir) {
		Word curWord = wordInfo[current];
		
		String findingWord = curWord.dir[mDir];
		int length = findingWord.length();
		int type = getType(mDir, length);
		
		if(!wordToSeq[type].containsKey(findingWord)) return 0;
		int findingSeq = wordToSeq[type].get(findingWord);
		
		HashMap<Integer, TreeSet<String>> target = getTargetMap(mDir, findingWord.length());
		
		TreeSet<String> set = target.get(findingSeq);
		if(set.size() == 0) return 0;
		String foundWord = set.first();
		
		int foundId = idByWord.get(foundWord);
		
		if(curWord.id != foundId) {
			current = foundId;
			return current;
		}
		
		if(set.size() == 1) return 0;
		
		set.pollFirst();
		
		foundWord = set.first();			
		foundId = idByWord.get(foundWord);
		current = foundId;
		set.add(curWord.word);	
		
		return current;
	}
	
	int getType(int dir,  int length) {
		switch (dir) {
		case 0:
			if(length == 2) return 4;
			else  return 5;
			
		case 1:
			return 3;			
		case 2:
			if(length == 2) return 1;
			else  return 2;				
		default:
			break;
	}
	
	return 0;
	}
	
	HashMap<Integer, TreeSet<String>> getTargetMap(int dir, int length) {
		switch (dir) {
			case 0:
				if(length == 2) return postMap2;
				else  return postMap4;
				
			case 1:
				return midMap;			
			case 2:
				if(length == 2) return preMap2;
				else  return preMap4;				
			default:
				break;
		}
		
		return null;
	}

	void changeWord(char mWord[], char mChgWord[], int mChgLen[]) {
		
		String oldWord = String.valueOf(mWord);
		String newWord = String.valueOf(mChgWord);		
		
		int id = idByWord.get(oldWord);
		
		
		Word word = wordInfo[id];
		int[] dirLen = word.dirLen;
		
		String pre2 =  oldWord.substring(0, 2);
		String pre4 = oldWord.substring(0, 4);
		String mid = oldWord.substring(4, 7);
		String post2 =  oldWord.substring(12 - 3, 11);
		String post4 =   oldWord.substring(12 - 5, 11);	
		
		int seq1 = getWordSeq(pre2, 1);
		int seq2 = getWordSeq(pre4, 2);
		int seq3 = getWordSeq(mid, 3);
		int seq4 = getWordSeq(post2, 4);
		int seq5 = getWordSeq(post4, 5);
		
		preMap2.get(seq1).remove(oldWord);
		preMap4.get(seq2).remove(oldWord);
		midMap.get(seq3).remove(oldWord);
		postMap2.get(seq4).remove(oldWord);
		postMap4.get(seq5).remove(oldWord);
		
		idByWord.remove(oldWord);
		idByWord.put(newWord, id);
		
		wordInfo[id] = new Word(id, newWord, dirLen);
		
		setWord(newWord);
		
		return;
	}
	
	int getWordSeq(String word, int type) {		
		return wordToSeq[type].get(word);	
	}
	
	
	class Word {
		int id;
		String word;
		String[] dir;
		int[] dirLen;
		public Word(int id, String word, int[] dirLen) {		
			this.id = id;
			this.word = word;
			this.dirLen = dirLen;
			dir =  new String[3]; 
			dir[0]= word.substring(0, dirLen[0]);
			dir[1]= word.substring(4, 7);
			dir[2] = word.substring(12 - dirLen[2] - 1, 11);
		}
	}
}
public class Main {
    private static BufferedReader br;
    private static UserSolution usersolution = new UserSolution();
	
    private final static int INIT = 0;
    private final static int ADD = 1;
    private final static int SET = 2;
	private final static int MOVE = 3;
	private final static int CHANGE = 4;
	
	private static final int MAX_LENGTH = 11 + 1;
	
    private static int dir[] = new int[3];
	private static char mWord[] = new char[MAX_LENGTH];
	private static char mRetWord[] = new char[MAX_LENGTH];
	
	private static void String2Char(String s, char[] b)
	{
		int n = s.length();
		for (int i = 0; i < n; ++i)
			b[i] = s.charAt(i);
		for (int i = n; i < MAX_LENGTH; ++i)
			b[i] = '\0';
	}
	
    private static boolean run() throws Exception {

        StringTokenizer st = new StringTokenizer(br.readLine(), " ");		
       
		int cmd, ans, ret, id;
		
		int Q = Integer.parseInt(st.nextToken());
        boolean ok = false;

        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(st.nextToken());

            if (cmd == INIT) {
                usersolution.init();
                ok = true;
            } else if (cmd == ADD) {
				id = Integer.parseInt(st.nextToken());
            	String2Char(st.nextToken(), mWord);
                dir[0] = Integer.parseInt(st.nextToken());
                dir[1] = Integer.parseInt(st.nextToken());
                dir[2] = Integer.parseInt(st.nextToken());

				usersolution.addRoom(id, mWord, dir);
				print(i, "addRoom", i, i, id, mWord, dir);
            } else if (cmd == SET) {
            	String2Char(st.nextToken(), mWord);

				usersolution.setCurrent(mWord);
				print(i, "setCurrent", i, i, mWord);
            }
			else if (cmd == MOVE) {
				dir[0] = Integer.parseInt(st.nextToken());
				ans = Integer.parseInt(st.nextToken());

				ret = usersolution.moveDir(dir[0]);
				
				print(i, "moveDir", ans, ret, dir[0]);
				if (ret != ans) {
					ok = false;
				}
            }
			else if (cmd == CHANGE) {
				String2Char(st.nextToken(), mWord);
				String2Char(st.nextToken(), mRetWord);
                dir[0] = Integer.parseInt(st.nextToken());
                dir[1] = Integer.parseInt(st.nextToken());
                dir[2] = Integer.parseInt(st.nextToken());
                usersolution.changeWord(mWord, mRetWord, dir);
                
                print(i, "changeWord", i, i, mWord, mRetWord, dir);
            }
			else ok = false;
        }
        return ok;
    }
    
    static void print(int q, String cmd, int ans, int ret, Object...o) {
		//if(ans!=ret) System.err.println("====================오류========================");
		//System.out.println("[" + q +"] " + cmd + " " + ans + "=" + ret + "[" + Arrays.deepToString(o)+ "]" );
	}


    public static void main(String[] args) throws Exception {
    	
    	Long start = System.currentTimeMillis();
        
    	int T, MARK;
    	
		System.setIn(new java.io.FileInputStream("C://sw certi//workspace//swcerti//src//기출문제//단어미로//sample_input.txt"));

        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");
        T = Integer.parseInt(stinit.nextToken());
        MARK = Integer.parseInt(stinit.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
        
        System.out.println("estimated => " + (System.currentTimeMillis() - start));
    }
}