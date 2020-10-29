package com.skyman.billiarddata.management.friend.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skyman.billiarddata.developer.DeveloperManager;
import com.skyman.billiarddata.management.friend.data.FriendData;
import com.skyman.billiarddata.management.projectblue.database.ProjectBlueDBManager;

import java.util.ArrayList;


/**
 * [class] project_blue.db 의 friend 테이블을 관리하기 위한 클래스이다.
 *
 * <p>
 * ProjectBlueDBManger 를 상속 받아
 * ProjectBlueDBHelper 를 생성한다.
 * 이 openDBHelper 에서 readableDatabase 와 writeableDatabase 를 가져와서 query 문을 실행한다.
 */
public class FriendDbManager extends ProjectBlueDBManager {

    // constant
    private final String CLASS_NAME_LOG = "[DbM]_FriendDbManager";

    // constructor
    public FriendDbManager(Context targetContext) {
        super(targetContext);
    }


    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-


    /**
     * [method] [insert] ProjectBlueDBHelper 에서 writeableDatabase 를 가져와 billiard 테이블에 데이터를 저장한다.
     *
     * @param userId                  [1] user 의 id (친구를 추가한 주체)
     * @param name                    [2] 이름
     * @param gameRecordWin           [3] 승리 횟수
     * @param gameRecordLoss          [4] 패배 횟수
     * @param recentGameBilliardCount [5] 최근 게임의 billiard count
     * @param totalPlayTime           [6] 총 게임 시간
     * @param totalCost               [7] 총 비용
     * @return friend 테이블에 데이터를 입력한 결과를 반환한다.
     */
    public long saveContent(long userId, String name, int gameRecordWin, int gameRecordLoss, long recentGameBilliardCount, int totalPlayTime, int totalCost) {

        final String METHOD_NAME = "[saveContent] ";

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "<userId> <name> <gameRecordWin> <gameRecordLoss> <recentGameBilliardCount> <totalPlayTime> <totalCost> 를 저장합니다.");

        // [lv/l]newRowId : 이 메소드의 결과 값 저장
        long newRowId = 0;

        // [check 1] : openDbHelper 가 초기화 되었다.
        if (this.isInitializedDB()) {

            // check : 매개변수의 내용 중에 빈 곳이 없나 검사
            if ((userId > 0) &&                         // 1. user id
                    !name.equals("") &&                 // 2. name
                    (gameRecordWin >= 0) &&             // 3. game record win
                    (gameRecordLoss >= 0) &&            // 4. game record loss
                    (recentGameBilliardCount >= 0) &&   // 5. recent game billiard count
                    (totalPlayTime >= 0) &&             // 6. total play time
                    (totalCost >= 0)                    // 7. total cost
            ) {

                // [lv/C]SQLiteDatabase : openDbHelper 를 이용하여 writeableDatabase 가져오기            ==> SQLiteDatabase 는 모든 조건이 만족 했을 때 가져와서
                SQLiteDatabase writeDb = this.getDbOpenHelper().getWritableDatabase();

                // [lv/C]ContentValues : query 의 값들을 매개변수의 값들로 셋팅한다.
                ContentValues insertValues = new ContentValues();
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_USER_ID, userId);                                         // 1. user id
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_NAME, name);                                              // 2. name
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_WIN, gameRecordWin);                          // 3. game record win
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_LOSS, gameRecordLoss);                        // 4. game record loss
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_RECENT_GAME_BILLIARD_COUNT, recentGameBilliardCount);     // 5. recent game billiard count
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_PLAY_TIME, totalPlayTime);                          // 6. total play time
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_COST, totalCost);                                   // 7. total cost


                // [lv/l]newRowId : writeDb 를 insert 한 값 결과 값을 받는다. 실패하면 '-1' 이고 성공하면 1 이상의 값이 반환된다.                == > 사용하고
                newRowId = writeDb.insert(FriendTableSetting.Entry.TABLE_NAME, null, insertValues);

                // SQLiteDatabase : close
                writeDb.close();

                // check : 데이터베이스 입력이 실패, 성공 했는지 구분하여
                if (newRowId == -1) {
                    // 데이터 insert 실패
                    DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "DB 저장 실패 : " + newRowId + " 값을 리턴합니다.");
                } else {
                    // 데이터 insert 성공
                    DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + newRowId + " 번째 입력이 성공하였습니다.");
                }

            } else {
                DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "매개변수들의 형식이 맞지 않습니다.");
                newRowId = -2;
            }

        } else {
            DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "openDBHelper 가 생성되지 않았습니다. 초기화 해주세요.");
        } // [check 1]

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "The method is complete");
        return newRowId;

    } // End of method [saveContent]


    /**
     * [method] [insert] ProjectBlueDBHelper 에서 writeableDatabase 를 가져와 billiard 테이블에 데이터를 저장한다.
     *
     * <p>
     * ContentValues 의 nullColumnHack 이 'null' 이라면, values 객체의 어떤 열에 값이 없으면 지금 내용을 insert query 가 실행 안 된다.
     * 이 '열 이름' 이라면, 해당 열에 값이 없다면 'null' 값을 넣는다.
     *
     * @param friendData friend 데이터가 들어있는 DTO
     * @return friend 테이블에 데이터를 입력한 결과를 반환한다.
     */
    public long saveContent(FriendData friendData) {

        final String METHOD_NAME = "[saveContent] ";

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "<friendData> 를 저장합니다.");

        // [lv/l]newRowId : 이 메소드의 결과 값 저장
        long newRowId = 0;

        // [check 1] : openDbHelper 가 초기화 되었다.
        if (this.isInitializedDB()) {

            // check : 매개변수의 내용 중에 빈 곳이 없나 검사
            if ((friendData.getUserId() > 0) &&                         // 1. user id
                    !friendData.getName().equals("") &&                 // 2. name
                    (friendData.getGameRecordWin() >= 0) &&             // 3. game record win
                    (friendData.getGameRecordLoss() >= 0) &&            // 4. game record loss
                    (friendData.getRecentGameBilliardCount() >= 0) &&   // 5. recent game billiard count
                    (friendData.getTotalPlayTime() >= 0) &&             // 6. total play time
                    (friendData.getTotalCost() >= 0)                    // 7. total cost
            ) {

                // [lv/C]SQLiteDatabase : openDbHelper 를 이용하여 writeableDatabase 가져오기            ==> SQLiteDatabase 는 모든 조건이 만족 했을 때 가져와서
                SQLiteDatabase writeDb = this.getDbOpenHelper().getWritableDatabase();

                // [lv/C]ContentValues : query 의 값들을 매개변수의 값들로 셋팅한다.
                ContentValues insertValues = new ContentValues();
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_USER_ID, friendData.getUserId());                                         // 1. user id
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_NAME, friendData.getName());                                              // 2. name
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_WIN, friendData.getGameRecordWin());                          // 3. game record win
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_LOSS, friendData.getGameRecordLoss());                        // 4. game record loss
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_RECENT_GAME_BILLIARD_COUNT, friendData.getRecentGameBilliardCount());     // 5. recent game billiard count
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_PLAY_TIME, friendData.getTotalPlayTime());                          // 6. total play time
                insertValues.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_COST, friendData.getTotalCost());                                   // 7. total cost


                // [lv/l]newRowId : writeDb 를 insert 한 값 결과 값을 받는다. 실패하면 '-1' 이고 성공하면 1 이상의 값이 반환된다.                == > 사용하고
                newRowId = writeDb.insert(FriendTableSetting.Entry.TABLE_NAME, null, insertValues);

                // SQLiteDatabase : close
                writeDb.close();

                // check : 데이터베이스 입력이 실패, 성공 했는지 구분하여
                if (newRowId == -1) {
                    // 데이터 insert 실패
                    DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "DB 저장 실패 : " + newRowId + " 값을 리턴합니다.");
                } else {
                    // 데이터 insert 성공
                    DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + newRowId + " 번째 입력이 성공하였습니다.");
                }

            } else {
                DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "매개변수들의 형식이 맞지 않습니다.");
                newRowId = -2;
            }

        } else {
            DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "openDBHelper 가 생성되지 않았습니다. 초기화 해주세요.");
        } // [check 1]

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "The method is complete");
        return newRowId;

    } // End of method [saveContent]


    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-


    /**
     * [method] [select] ProjectBlueDBHelper 에서 readableDatabase 를 가져와 friend 테이블에 저장된 데이터를 모두 가져온다.
     *
     * @return friend 테이블에 저장된 모든 데이터
     */
    public ArrayList<FriendData> loadAllContent() {

        final String METHOD_NAME = "[loadAllContent] ";

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "모든 데이터를 가져옵니다.");

        // [lv/C]ArrayList<friendData> : friend 테이블에 저장된 모든 데이커가 담길 ArrayList
        ArrayList<FriendData> friendDataArrayList = new ArrayList<>();

        // [check  1] : openDbHelper 가 초기화 되었다.
        if (this.isInitializedDB()) {

            // [lv/C]SQLiteDatabase : openDbHelper 를 이용하여 writeableDatabase 가져오기 / declaration & create
            SQLiteDatabase readDb = super.getDbOpenHelper().getReadableDatabase();

            // [lv/C]Cursor : select query 문의 실행 결과가 담길 Cursor / use
            Cursor cursor = readDb.rawQuery(FriendTableSetting.SQL_SELECT_TABLE_ALL_ITEM, null);

            // [cycle 1] : cursor 의 객체의 moveToNext method 를 이용하여 가져온 데이터가 있을 때까지
            while (cursor.moveToNext()) {

                // [lv/C]FriendData : friend 테이블의 '한 행'의 정보를 담는다.
                FriendData friendData = new FriendData();
                friendData.setId(cursor.getLong(0));
                friendData.setUserId(cursor.getLong(1));
                friendData.setName(cursor.getString(2));
                friendData.setGameRecordWin(cursor.getInt(3));
                friendData.setGameRecordLoss(cursor.getInt(4));
                friendData.setRecentGameBilliardCount(cursor.getLong(5));
                friendData.setTotalPlayTime(cursor.getInt(6));
                friendData.setTotalCost(cursor.getInt(7));

                // [lv/C]ArrayList<friendData> : 위 의 '한 행'의 내용을 배열 형태로 담는다.
                friendDataArrayList.add(friendData);
            } // [cycle 1]

            // [lv/C]SQLiteDatabase : close / end
            readDb.close();

        } else {
            DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "openDBHelper 가 생성되지 않았습니다. 초기화 해주세요.");
        } // [check 1]

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "The method is complete!");
        return friendDataArrayList;

    } // End of method [loadAllContent]


    /**
     * [method] [select] ProjectBlueDBHelper 에서 readableDatabase 를 가져와 friend 테이블에 저장된 데이터를 해당 userId 가 같은 데이터를 모두 가져온다.
     *
     * @param userId [1] 친구를 추가한 user 의 id
     * @return friend 테이블에 저장된 모든 데이터
     */
    public ArrayList<FriendData> loadAllContentByUserId(long userId) {

        final String METHOD_NAME = "[loadAllContentByUserId] ";

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "<userId> 에 해당하는 데이터를 가져옵니다.");

        // [lv/C]ArrayList<friendData> : friend 테이블에 저장된 모든 데이커가 담길 ArrayList
        ArrayList<FriendData> friendDataArrayList = new ArrayList<>();

        // [check  1] : openDbHelper 가 초기화 되었다.
        if (this.isInitializedDB()) {

            // [check 2] : 매개변수 id 의 형식이 맞다
            if (userId > 0) {

                // [lv/C]SQLiteDatabase : openDbHelper 를 이용하여 writeableDatabase 가져오기 / declaration & create
                SQLiteDatabase readDb = super.getDbOpenHelper().getReadableDatabase();

                DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "Query 문은 : " + FriendTableSetting.SQL_SELECT_WHERE_USER_ID + userId);

                // [lv/C]Cursor : select query 문의 실행 결과가 담길 Cursor / use
                Cursor cursor = readDb.rawQuery(FriendTableSetting.SQL_SELECT_WHERE_USER_ID + userId, null);

                // [cycle 1] : cursor 의 객체의 moveToNext method 를 이용하여 가져온 데이터가 있을 때까지
                while (cursor.moveToNext()) {

                    // [lv/C]FriendData : friend 테이블의 '한 행'의 정보를 담는다.
                    FriendData friendData = new FriendData();
                    friendData.setId(cursor.getLong(0));
                    friendData.setUserId(cursor.getLong(1));
                    friendData.setName(cursor.getString(2));
                    friendData.setGameRecordWin(cursor.getInt(3));
                    friendData.setGameRecordLoss(cursor.getInt(4));
                    friendData.setRecentGameBilliardCount(cursor.getLong(5));
                    friendData.setTotalPlayTime(cursor.getInt(6));
                    friendData.setTotalCost(cursor.getInt(7));

                    // [lv/C]ArrayList<friendData> : 위 의 '한 행'의 내용을 배열 형태로 담는다.
                    friendDataArrayList.add(friendData);

                } // [cycle 1]

                // [lv/C]SQLiteDatabase : close / end
                readDb.close();

            } else {
                DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "매개변수들의 형식이 맞지 않습니다.");
            } // [check 2]

        } else {
            DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "openDBHelper 가 생성되지 않았습니다. 초기화 해주세요.");
        } // [check 1]

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "The method is complete!");
        return friendDataArrayList;

    } // End of method [loadAllContentByUserId]


    /**
     * [method] [select] ProjectBlueDBHelper 에서 readableDatabase 를 가져와 friend 테이블에 저장된 데이터를 해당 userId 와 id 가 모두 같은 데이터를 가져온다.
     *
     * @param id [0] user 의 friend id
     * @return friend 테이블에 저장된 모든 데이터
     */
    public FriendData loadContentById(long id) {

        final String METHOD_NAME = "[loadContentById] ";

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "<id> 에 해당하는 데이터를 가져옵니다.");

        // [lv/C]ArrayList<friendData> : friend 테이블에 저장된 모든 데이커가 담길 ArrayList
        // [lv/C]FriendData : friend 테이블에 저장된 특정 userId 의 특정 친구의 id 로 데이터를 가져와 담을 객체
        FriendData friendData = new FriendData();

        // [check  1] : openDbHelper 가 초기화 되었다.
        if (this.isInitializedDB()) {

            // [check 2] : 매개변수 id 의 형식이 맞다
            if (id > 0) {

                // [lv/C]SQLiteDatabase : openDbHelper 를 이용하여 writeableDatabase 가져오기 / declaration & create
                SQLiteDatabase readDb = super.getDbOpenHelper().getReadableDatabase();

                // [lv/C]Cursor : select query 문의 실행 결과가 담길 Cursor / use
                Cursor cursor = readDb.rawQuery(FriendTableSetting.SQL_SELECT_WHERE_ID + "=" + id, null);

                // [check 3] : cursor 객체의 moveToFirst method 를 이용하여 첫 번째 데이터가 있다는 걸 확인했다.
                if (cursor.moveToFirst()) {

                    // [lv/C]FriendData : friend 테이블의 '한 행'의 정보를 담는다.
                    friendData.setId(cursor.getLong(0));
                    friendData.setUserId(cursor.getLong(1));
                    friendData.setName(cursor.getString(2));
                    friendData.setGameRecordWin(cursor.getInt(3));
                    friendData.setGameRecordLoss(cursor.getInt(4));
                    friendData.setRecentGameBilliardCount(cursor.getLong(5));
                    friendData.setTotalPlayTime(cursor.getInt(6));
                    friendData.setTotalCost(cursor.getInt(7));

                } else {

                    // [lv/C]FriendData : 가져온 FriendData 가 없다.
                    friendData = null;

                } // [check 3]

                // [lv/C]SQLiteDatabase : close / end
                readDb.close();

            } else {
                DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "매개변수들의 형식이 맞지 않습니다.");
            } // [check 2]

        } else {
            DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "openDBHelper 가 생성되지 않았습니다. 초기화 해주세요.");
        } // [check 1]

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "The method is complete!");
        return friendData;

    } // End of method [loadContentById]


    /**
     * [method] [select] ProjectBlueDBHelper 에서 readableDatabase 를 가져와 friend 테이블에 저장된 데이터 중 userId 와 id 값과 똑같은 데이터를 모두 가져온다.
     *
     * @param id     [0] user 의 friend id
     * @param userId [1] 친구를 추가한 user 의 id
     * @return friend 테이블에 저장된 모든 데이터
     */
    public FriendData loadContentByIdAndUserId(long id, long userId) {

        final String METHOD_NAME = "[loadContentByIdAndUserId] ";

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "<id> 와 <userId> 에 해당하는 데이터를 가져옵니다.");

        // [lv/C]ArrayList<friendData> : friend 테이블에 저장된 모든 데이커가 담길 ArrayList
        // [lv/C]FriendData : friend 테이블에 저장된 특정 userId 의 특정 친구의 id 로 데이터를 가져와 담을 객체
        FriendData friendData = new FriendData();

        // [check  1] : openDbHelper 가 초기화 되었다.
        if (this.isInitializedDB()) {

            // [check 2] : 매개변수 id, userId 의 형식이 맞다
            if (id > 0 && userId > 0) {

                // [lv/C]SQLiteDatabase : openDbHelper 를 이용하여 writeableDatabase 가져오기 / declaration & create
                SQLiteDatabase readDb = super.getDbOpenHelper().getReadableDatabase();

                // [lv/C]Cursor : select query 문의 실행 결과가 담길 Cursor / use
                Cursor cursor = readDb.rawQuery(createQueryToIdAndUserId(id, userId), null);

                // [check 3] : cursor 객체의 moveToFirst method 를 이용하여 첫 번째 데이터가 있다는 걸 확인했다.
                if (cursor.moveToFirst()) {

                    // [lv/C]FriendData : friend 테이블의 '한 행'의 정보를 담는다.
                    friendData.setId(cursor.getLong(0));
                    friendData.setUserId(cursor.getLong(1));
                    friendData.setName(cursor.getString(2));
                    friendData.setGameRecordWin(cursor.getInt(3));
                    friendData.setGameRecordLoss(cursor.getInt(4));
                    friendData.setRecentGameBilliardCount(cursor.getLong(5));
                    friendData.setTotalPlayTime(cursor.getInt(6));
                    friendData.setTotalCost(cursor.getInt(7));

                } else {

                    // [lv/C]FriendData : 가져온 FriendData 가 없다.
                    friendData = null;

                } // [check 3]

                // [lv/C]SQLiteDatabase : close / end
                readDb.close();

            } else {
                DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "매개변수들의 형식이 맞지 않습니다.");
            } // [check 2]

        } else {
            DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "openDBHelper 가 생성되지 않았습니다. 초기화 해주세요.");
        } // [check 1]

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "The method is complete!");
        return friendData;

    } // End of method [loadContentByIdAndUserId]


    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-


    /**
     * [method] [update] gameRecordWin, gameRecordLoss, recentGameBilliardCount, totalPlayTime, totalCost 를 해당 id 의 데이터를 갱신한다.
     *
     * @param id             [0] id ( friend 의 id 임)
     * @param gameRecordWin  [3] 게임 승리 수
     * @param gameRecordLoss [4] 게임 패배 수
     * @param recentGameBilliardCount [5] 최근 게임의 billiard count
     * @param totalPlayTime  [6] 총 게임 시간
     * @param totalCost      [7] 총 비용
     * @return update query 를 실행한 결과
     */
    public int updateContentById(long id, int gameRecordWin, int gameRecordLoss, long recentGameBilliardCount, int totalPlayTime, int totalCost) {

        final String METHOD_NAME = "[updateContentById] ";

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "<id> 에당하는 <gameRecordWin> <gameRecordLoss> <recentGameBilliardCount> <totalPlayTime> <totalCost> 를 갱신합니다.");

        // [lv/l]methodResult : 이 메소드의 결과 값 저장
        int methodResult = 0;

        // [check 1] : openDbHelper 가 초기화 되었다.
        if (this.isInitializedDB()) {

            // [check 2] : 매개변수들의 형식이 일치한다.
            if ((id > 0)                                // 0. id
                    && (gameRecordWin >= 0)             // 3. game record win
                    && (gameRecordLoss >= 0)            // 4. game record loss
                    && (recentGameBilliardCount >= 0)   // 5. recent game billiard count
                    && (totalPlayTime >= 0)             // 6. total play time
                    && (totalCost >= 0)) {              // 7. total cost

                // [lv/C]SQLiteDatabase : openDbHelper 를 이용하여 writeableDatabase 가져오기
                SQLiteDatabase updateDb = this.getDbOpenHelper().getWritableDatabase();

                // [lv/C]ContentValues : 매개변수 값을 담을 객체 생성과 초기화
                ContentValues updateValue = new ContentValues();
                updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_WIN, gameRecordWin);
                updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_LOSS, gameRecordLoss);
                updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_RECENT_GAME_BILLIARD_COUNT, recentGameBilliardCount);
                updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_PLAY_TIME, totalPlayTime);
                updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_COST, totalCost);

                // [lv/i]methodResult : update query 문을 실행한 결과
                methodResult = updateDb.update(FriendTableSetting.Entry.TABLE_NAME, updateValue, FriendTableSetting.Entry._ID + "=" + id, null);

                // [lv/C]SQLiteDatabase : close
                updateDb.close();

            } else {
                DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "매개변수들의 형식이 맞지 않습니다.");
            } // [check 2]

        } else {
            DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "openDBHelper 가 생성되지 않았습니다. 초기화 해주세요.");
        } // [check 1]

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "The method is complete!");
        return methodResult;

    } // End of method [updateContentById]


    /**
     * [method] [update] gameRecordWin, gameRecordLoss, recentPlayDate, totalPlayTime, totalCost 를 해당 userId 의 데이터를 갱신한다.
     *
     * @param userId         [1] user id ( friend 를 추가한 주체)
     * @param gameRecordWin  [3] 게임 승리 수
     * @param gameRecordLoss [4] 게임 패배 수
     * @param recentGameBilliardCount [5] 최근 게임의 billiard count
     * @param totalPlayTime  [6] 총 게임 시간
     * @param totalCost      [7] 총 비용
     * @return update query 를 실행한 결과
     */
    public int updateContentByUserId(long userId, int gameRecordWin, int gameRecordLoss, long recentGameBilliardCount, int totalPlayTime, int totalCost) {

        final String METHOD_NAME = "[updateContentByUserId] ";

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "<userId> 에 해당하는 모든 레코드의 <gameRecordWin> <gameRecordLoss> <recentGameBilliardCount> <totalPlayTime> <totalCost> 를 수정한다. ");

        // [lv/l]methodResult : 이 메소드의 결과 값 저장
        int methodResult = 0;

        // [check 1] : openDbHelper 가 초기화 되었다.
        if (this.isInitializedDB()) {

            // [check 2] : 매개변수들의 형식이 일치한다.
            if ((userId > 0)                            // 1. user id
                    && (gameRecordWin >= 0)             // 3. game record win
                    && (gameRecordLoss >= 0)            // 4. game record loss
                    && (recentGameBilliardCount >= 0)    // 5. recent game billiard count
                    && (totalPlayTime >= 0)             // 6. total play time
                    && (totalCost >= 0)) {              // 7. total cost

                // [lv/C]SQLiteDatabase : openDbHelper 를 이용하여 writeableDatabase 가져오기
                SQLiteDatabase updateDb = this.getDbOpenHelper().getWritableDatabase();

                // [lv/C]ContentValues : 매개변수 값을 담을 객체 생성과 초기화
                ContentValues updateValue = new ContentValues();
                updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_WIN, gameRecordWin);                           // 3. game record win
                updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_GAME_RECORD_LOSS, gameRecordLoss);                         // 4. game record loss
                updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_RECENT_GAME_BILLIARD_COUNT, recentGameBilliardCount);      // 5. recent game billiard count
                updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_PLAY_TIME, totalPlayTime);                           // 6. total play time
                updateValue.put(FriendTableSetting.Entry.COLUMN_NAME_TOTAL_COST, totalCost);                                    // 7. total cost

                // [lv/i]methodResult : update query 문을 실행한 결과
                methodResult = updateDb.update(FriendTableSetting.Entry.TABLE_NAME, updateValue, FriendTableSetting.Entry.COLUMN_NAME_USER_ID + "=" + userId, null);

                // [lv/C]SQLiteDatabase : close
                updateDb.close();

            } else {
                DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "매개변수들의 형식이 맞지 않습니다.");
            } // [check 2]

        } else {
            DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "openDBHelper 가 생성되지 않았습니다. 초기화 해주세요.");
        } // [check 1]

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "The method is complete!");
        return methodResult;

    } // End of method [updateContentByUserId]


    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-


    /**
     * [method] [delete] ProjectBlueDBHelper 로 writeableDatabase 를 가져와서 friend 테이블의 모든 내용을 삭제한다.
     *
     * @return user 테이블의 데이터를 삭제한 개수 or 실패
     */
    public int deleteContent() {

        final String METHOD_NAME = "[deleteContent] ";

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "모든 데이터를 삭제한다.");

        // [lv/l]methodResult : 이 메소드의 결과 값 저장
        int methodResult = 0;

        // [check 1] : openDbHelper 가 초기화 되었다.
        if (this.isInitializedDB()) {

            // [lv/C]SQLiteDatabase : openDbHelper 를 이용하여 writeableDatabase 가져오기
            SQLiteDatabase deleteDb = this.getDbOpenHelper().getWritableDatabase();

            // [lv/i]methodResult : delete query 문이 실행 된 결과를 받는다.
            methodResult = deleteDb.delete(FriendTableSetting.Entry.TABLE_NAME, null, null);

            // [lv/C]SQLiteDatabase : close
            deleteDb.close();

        } else {
            DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "openDBHelper 가 생성되지 않았습니다. 초기화 해주세요.");
        } // [check 1]

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "The method is complete");
        return methodResult;

    } // End of method [deleteContent]


    /**
     * [method] [delete] ProjectBlueDBHelper 로 writeableDatabase 를 가져와서 friend 테이블에서 userId 에 해당하는 모든 데이터를 삭제한다.
     *
     * @param id [0] user 의 friend id
     * @return user 테이블의 데이터를 삭제한 개수 or 실패
     */
    public int deleteContentById(long id) {

        final String METHOD_NAME = "[deleteContentById] ";

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "<id> 에 해당하는 데이터를 삭제합니다.");

        // [lv/l]methodResult : 이 메소드의 결과 값 저장
        int methodResult = 0;

        // [check 1] : openDbHelper 가 초기화 되었다.
        if (this.isInitializedDB()) {

            // [check 2] : 매개변수 userId 는 0보다 크다.
            if (id > 0) {

                // [lv/C]SQLiteDatabase : openDbHelper 를 이용하여 writeableDatabase 가져오기
                SQLiteDatabase deleteDb = this.getDbOpenHelper().getWritableDatabase();

                // [lv/i]methodResult : delete query 문이 실행 된 결과를 받는다.
                methodResult = deleteDb.delete(FriendTableSetting.Entry.TABLE_NAME, FriendTableSetting.Entry._ID + "=" + id, null);

                // [lv/C]SQLiteDatabase : close
                deleteDb.close();

            } else {
                DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "매개변수의 형식에 맞지 않습니다.");
            } // [check 2]

        } else {
            DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "openDBHelper 가 생성되지 않았습니다. 초기화 해주세요.");
        } // [check 1]

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "The method is complete");
        return methodResult;

    } // End of method [deleteContentById]


    /**
     * [method] [delete] ProjectBlueDBHelper 로 writeableDatabase 를 가져와서 friend 테이블에서 userId 에 해당하는 모든 데이터를 삭제한다.
     *
     * @param userId [1] user id ( friend 를 추가한 주체)
     * @return user 테이블의 데이터를 삭제한 개수 or 실패
     */
    public int deleteContentByUserId(long userId) {

        final String METHOD_NAME = "[deleteContentByUserId] ";

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "<userId> 에 해당하는 모든 데이터를 삭제한다.");

        // [lv/l]methodResult : 이 메소드의 결과 값 저장
        int methodResult = 0;

        // [check 1] : openDbHelper 가 초기화 되었다.
        if (this.isInitializedDB()) {

            // [check 2] : 매개변수 userId 는 0보다 크다.
            if (userId > 0) {

                // [lv/C]SQLiteDatabase : openDbHelper 를 이용하여 writeableDatabase 가져오기
                SQLiteDatabase deleteDb = this.getDbOpenHelper().getWritableDatabase();

                // [lv/i]methodResult : delete query 문이 실행 된 결과를 받는다.
                methodResult = deleteDb.delete(FriendTableSetting.Entry.TABLE_NAME, FriendTableSetting.Entry.COLUMN_NAME_USER_ID + "=" + userId, null);

                // [lv/C]SQLiteDatabase : close
                deleteDb.close();

            } else {
                DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "매개변수의 형식에 맞지 않습니다.");
            } // [check 2]

        } else {
            DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "openDBHelper 가 생성되지 않았습니다. 초기화 해주세요.");
        } // [check 1]

        DeveloperManager.displayLog(CLASS_NAME_LOG, METHOD_NAME + "The method is complete");
        return methodResult;

    } // End of method [deleteContentByUserId]


    // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-


    /**
     * [method] select Where id='' AND userId='' 형식으로 만들기
     */
    private String createQueryToIdAndUserId(long id, long userId) {
        // [lv/C]StringBuilder : 문자열을 더하기 위한 객체 생성
        StringBuilder query = new StringBuilder();

        // [lv/C]StringBuilder : SELECT * FROM WHERE id='' AND userId='' 형태의 query 문을 만든다.
        query.append("SELECT * FROM WHERE ");
        query.append(FriendTableSetting.Entry._ID);
        query.append("=");
        query.append(id);
        query.append(" AND ");
        query.append(FriendTableSetting.Entry.COLUMN_NAME_USER_ID);
        query.append("=");
        query.append(userId);

        return query.toString();
    } // End of method [createQueryToIdAndUserId]

}
