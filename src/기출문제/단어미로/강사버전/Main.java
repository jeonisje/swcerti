package 기출문제.단어미로.강사버전;

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
	
	static class Room {
		int ID; // ID
		String word; 
		String substr[] = new String[3]; 
		
		Room(int ID, String word, int[] dir) {
			this.ID = ID;
			this.word = word;
			
			this.substr[0] = word.substring(0, dir[0]);
			this.substr[1] = word.substring(4, 7);
			this.substr[2] = word.substring(word.length() - dir[2], word.length()); 
		}
	}
	
	// KEY : 0 = 앞, 1 = 중간 2 = 뒤
	// => KEY : word (substring)
	// => VALUE : 이 substring으로 시작하는 문자열 중 사전순으로 가장 빠른 것
	static HashMap<String, TreeMap<String, Integer>>rooms[]; 
	
	static Room curRoom; 
	
	// KEY : word
	// VALUE : 이 단어가 있는 방
	static HashMap<String, Room>hm; 
	
	static void add(String word) {
		String firstTwo = word.substring(0, 2); 
		if(rooms[2].get(firstTwo) == null)
			rooms[2].put(firstTwo, new TreeMap<>()); 
		rooms[2].get(firstTwo).put(word, 1); 
		
		// 앞 4개 
		String firstFour = word.substring(0, 4); 	
		if(rooms[2].get(firstFour) == null)
			rooms[2].put(firstFour, new TreeMap<>()); 
		rooms[2].get(firstFour).put(word, 1); 
		
		// 중간 
		String middle = word.substring(4, 7); 	
		if(rooms[1].get(middle) == null)
			rooms[1].put(middle, new TreeMap<>()); 
		rooms[1].get(middle).put(word, 1); 
				
		// 뒤 2개
		String lastTwo = word.substring(9, word.length()); 	
		if(rooms[0].get(lastTwo) == null)
			rooms[0].put(lastTwo, new TreeMap<>()); 
		rooms[0].get(lastTwo).put(word, 1); 
		
		// 뒤 4개
		String lastFour = word.substring(7, word.length()); 	
		if(rooms[0].get(lastFour) == null)
			rooms[0].put(lastFour, new TreeMap<>()); 
		rooms[0].get(lastFour).put(word, 1); 		
	}
	
	static void remove(String word) {
		
		// 삭제
		String firstTwo = word.substring(0, 2); 
		rooms[2].get(firstTwo).remove(word); 
		
		// 앞 4개 
		String firstFour = word.substring(0, 4); 	
		rooms[2].get(firstFour).remove(word); 
		
		// 중간 
		String middle = word.substring(4, 7); 	
		rooms[1].get(middle).remove(word); 
				
		// 뒤 2개
		String lastTwo = word.substring(9, word.length()); 	
		rooms[0].get(lastTwo).remove(word); 
		
		// 뒤 4개
		String lastFour = word.substring(7, word.length()); 	
		rooms[0].get(lastFour).remove(word); 	
	}
	
	void init()
	{
		// 0 = 앞, 1 = 중간, 2 = 뒤
		rooms = new HashMap[3];
		for(int i = 0; i < 3; i++)
			rooms[i] = new HashMap<>();
		curRoom = null; 
		hm = new HashMap<>();
	}
	
	// 30,000 x 
	void addRoom(int mID, char mWord[], int mDirLen[])
	{
		// 일단 들어온 word를 모두 잘라서 관리
		String word = String.valueOf(mWord).trim();
		hm.put(word, new Room(mID, word, mDirLen));
		add(word); 
	}

	// 500 x 
	void setCurrent(char mWord[])
	{
		String word = String.valueOf(mWord).trim(); 
		curRoom = hm.get(word); 
	}

	// 50,000 x 
	int moveDir(int mDir)
	{
		String substr = curRoom.substr[mDir];
		
		if(rooms[mDir].get(substr) == null)
			return 0; 
		
		for(Map.Entry<String, Integer>ent : rooms[mDir].get(substr).entrySet()) {
			String next = ent.getKey();
			if(next.equals(curRoom.word))
				continue;
			curRoom = hm.get(next);
			return curRoom.ID; 
		}
		return 0; 
	}

	// 3,000 
	void changeWord(char mWord[], char mChgWord[], int mChgLen[])
	{
		String word = String.valueOf(mWord).trim(); 
		int id = hm.get(word).ID; 
		hm.remove(word); 
		remove(word);
		
		// 추가
		String newWord = String.valueOf(mChgWord).trim(); 
		add(newWord);
		hm.put(newWord, new Room(id, newWord, mChgLen));
		
		if(curRoom.word.equals(word))
			curRoom = hm.get(newWord); 
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