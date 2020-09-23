package com.skyman.billiarddata;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.skyman.billiarddata.developer.DeveloperManager;
import com.skyman.billiarddata.management.billiard.data.BilliardData;
import com.skyman.billiarddata.management.billiard.database.BilliardDbManager;
import com.skyman.billiarddata.management.billiard.listview.BilliardLvManager;

import java.util.ArrayList;

public class BilliardDisplayActivity extends AppCompatActivity {

    // value : helper manager 객체 선언
    private BilliardDbManager billiardDbManager = null;

    // value : activity 에서 사용하는 객체 선언
    private ListView allBilliardData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billiard_display);

        // ListView : allBilliardData setting
        allBilliardData = (ListView) findViewById(R.id.billiard_display_lv_all_billiard_data);

        // BilliardDbManager : billiardDbManager setting - SQLiteOpenHelper 를 관리하는 클래스
        billiardDbManager = new BilliardDbManager(this);
        billiardDbManager.init_db();

        // BilliardDbManager : load_contents method - 저장 되어 있는 billiard 데이터를 billiardDbManager 를 통해 가져온다.
        ArrayList<BilliardData> billiardDataArrayList = billiardDbManager.load_contents();
        if(billiardDataArrayList.size() != 0) {
            // BilliardLvManager : allBilliardData setting - 위에서 가져온 데이터를 allBilliardData 에 뿌려준다.
            BilliardLvManager billiardLvManager = new BilliardLvManager(allBilliardData);
            billiardLvManager.setListViewToAllBilliardData(billiardDataArrayList);
        } else {
            DeveloperManager.displayLog("BilliardDisplayActivity", "content is not exist in billiard table.");
        }
    }
}