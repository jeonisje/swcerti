package 기출문제.단어미로._02;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

class UserSolution {
	int MAX = 35_001;
	
	HashMap<String, Room> strToRoom;
	HashMap<String, TreeMap<String, Integer>>[] searchMap;  // 0 : prefix, 1 : mid, 2: postfix
	HashSet<String> removed;
 	Room[] rooms;
	
	int sequence;
	Room current;
	
	int[] findDir = {2, 1, 0};
	int[][] keyArray = {{0, 2}, {4, 7}, {9, 11}};
	int[][] locStart = {{0, 0, 0, 0, 0}, {0, 0, 0, 4, 0}, {0, 0, 9, 0, 7}};
	int[][] locEnd = {{0, 0, 2, 0, 4}, {0, 0, 0, 7, 0}, {0, 0, 11, 0, 11}};
	
	void init() {
		strToRoom = new HashMap<>();
		searchMap = new HashMap[3];
		for(int i=0; i<3; i++) {
			searchMap[i] = new HashMap<>();			
		}
		removed = new HashSet<>();
		
		rooms = new Room[MAX];
		sequence = 0;
		current = null;
	}

	void addRoom(int mID, char mWord[], int mDirLen[]) {
		String word = String.valueOf(mWord).trim();		
		add(mID, word, mDirLen);		
	}
	
	void add(int mID, String word, int mDirLen[]) {
		String prefix2 = word.substring(locStart[0][2], locEnd[0][2]);		
		TreeMap<String, Integer> tm = searchMap[2].getOrDefault(prefix2, new TreeMap<>(ordered()));
		tm.put(word, mID);
		searchMap[2].put(prefix2, tm);				
		
		String prefix4 = word.substring(locStart[0][4], locEnd[0][4]);		
		tm = searchMap[2].getOrDefault(prefix4, new TreeMap<>(ordered()));	
		tm.put(word, mID);
		searchMap[2].put(prefix4, tm);		
		
		String mid = word.substring(locStart[1][3], locEnd[1][3]);		
		tm = searchMap[1].getOrDefault(mid, new TreeMap<>(ordered()));	
		tm.put(word, mID);
		searchMap[1].put(mid, tm);		
		
		String postfix2 = word.substring(locStart[2][2], locEnd[2][2]);		
		tm = searchMap[0].getOrDefault(postfix2, new TreeMap<>(ordered()));
		tm.put(word, mID);
		searchMap[0].put(postfix2, tm);				
		
		String postfix4 = word.substring(locStart[2][4], locEnd[2][4]);		
		tm = searchMap[0].getOrDefault(postfix4, new TreeMap<>(ordered()));	
		tm.put(word, mID);
		searchMap[0].put(postfix4, tm);		
		
		Room room = new Room(mID, word, mDirLen);
		strToRoom.put(word, room);		
		return;
	}

	private Comparator<? super String> ordered() {
		return (o1, o2) -> o1.compareTo(o2);
	}
	
	void setCurrent(char mWord[]) {	
		current =  strToRoom.get(String.valueOf(mWord).trim());
		
		return ;
	}

	int moveDir(int mDir) {
		String key = current.dirWord[mDir];
		
		if(!searchMap[mDir].containsKey(key)) 
			return 0;
		
		for(Map.Entry<String, Integer> entry : searchMap[mDir].get(key).entrySet()) {			
			Room foundRoom = strToRoom.get(entry.getKey());
			if(foundRoom == null) continue;
			if(current.roomId == foundRoom.roomId) {
				continue;
			}			
			current =  foundRoom;
			return current.roomId;
		}		
		
		return 0;		
	}
	void changeWord(char mWord[], char mChgWord[], int mChgLen[]) {		
		String oldWord = String.valueOf(mWord).trim();
		String newWord = String.valueOf(mChgWord).trim();
		Room oldRoom = strToRoom.get(oldWord);
		
		remove(oldRoom.word);		
		strToRoom.remove(oldWord);
		
		add(oldRoom.roomId, newWord, mChgLen);		
		
		if(current.roomId == oldRoom.roomId) {
			setCurrent(mChgWord);
		}
	}
	
	void remove(String word) {
		String prefix2 = word.substring(locStart[0][2], locEnd[0][2]);		
		searchMap[2].get(prefix2).remove(word);				
		
		String prefix4 = word.substring(locStart[0][4], locEnd[0][4]);
		searchMap[2].get(prefix4).remove(word);				
		
		String mid = word.substring(locStart[1][3], locEnd[1][3]);		
		searchMap[1].get(mid).remove(word);		
		
		String postfix2 = word.substring(locStart[2][2], locEnd[2][2]);				
		searchMap[0].get(postfix2).remove(word);				
		
		String postfix4 = word.substring(locStart[2][4], locEnd[2][4]);				
		searchMap[0].get(postfix4).remove(word);		
		return;		
	}

	class Room {
		int roomId;
		String word;
		String[] dirWord = new String[3];
		public Room(int roomId,String word, int[] dirLen) {
			this.roomId = roomId;
			this.word = word;
			this.dirWord[0] = word.substring(locStart[0][dirLen[0]], locEnd[0][dirLen[0]]);
			this.dirWord[1] = word.substring(locStart[1][dirLen[1]], locEnd[1][dirLen[1]]);
			this.dirWord[2] = word.substring(locStart[2][dirLen[2]], locEnd[2][dirLen[2]]);
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