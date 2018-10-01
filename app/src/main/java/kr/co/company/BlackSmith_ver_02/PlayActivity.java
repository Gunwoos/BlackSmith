package kr.co.company.BlackSmith_ver_02;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PlayActivity extends Activity implements View.OnTouchListener {
    final static int SELECT_IMAGE = 1;
    public static final String PREFS_NAME= "GSSD";//저장소
    TimeC timeC; // 타이머쓰레드
    int MusicCheck=1; // 0이면 Music On

    BackView view;
    boolean isfight;
    boolean canAtk;

    boolean isrun;

    InventorynWeapone inventorynWeapone;
    Monster[] monster = new Monster[7];
    User user;
    int clickX, clickY;

    private int nowfloor;

    private boolean mHasPerformedLongPress;
    private CheckForLongPress mPendingCheckForLongPress;

    float scaleS;
    float scaleS2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new BackView(this);
        setContentView(view);

        startService(new Intent(this,MusicService.class));

        isrun = true;
        nowfloor = 1;

        isfight = true;
        canAtk = true;

        user = new User();
        inventorynWeapone = new InventorynWeapone();
        monster[0] = new Monster(95, 3,5,1);
        monster[1] = new Monster(252,6,9,0);
        monster[2] = new Monster(552,10,12,3);
        monster[3] = new Monster(825,15,15,0);
        monster[4] = new Monster(1332,20,18,5);
        monster[5] = new Monster(2280,28,24,0);
        monster[6] = new Monster(4240,40,40,0);

        timeC = new TimeC();
        timeC.setPa(this);
        timeC.setT(monster[0].TimeCheck()+5000);
        timeC.setUser(user);

        view.setO(timeC, user, monster[0], inventorynWeapone);
        Load();
        timeC.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        scaleS = view.getScaleS();
        scaleS2 = view.getScaleS2();

        SoundManager.getsInstance().init(this);

        SoundManager.getsInstance().addSound("attack", R.raw.attack);
        SoundManager.getsInstance().addSound("attack_ch", R.raw.attack_char);
        SoundManager.getsInstance().addSound("boss_ap",R.raw.boss_ap);
        SoundManager.getsInstance().addSound("button",R.raw.button);
        SoundManager.getsInstance().addSound("hammer",R.raw.hammer);
        SoundManager.getsInstance().addSound("success", R.raw.success);
        SoundManager.getsInstance().addSound("monster_die", R.raw.monster_die);
        SoundManager.getsInstance().addSound("skill", R.raw.skill);
        SoundManager.getsInstance().addSound("game_over", R.raw.game_over);
        SoundManager.getsInstance().addSound("fail", R.raw.fail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Save();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onStop() {
        super.onStop();

        SoundManager.getsInstance().clear();
        Save();
    }

    public void TimeEnd(){ // 시간 만료시
        SoundManager.getsInstance().play("game_over");
        Reset_all();

        canAtk = false;
        isfight = false;

        view.setGameover(true);
        view.setMonNum(-1);
    }

    public void Reset_all(){

        SharedPreferences prf = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = prf.edit();
        editor.clear();
        editor.commit();

        inventorynWeapone.die();
        user.die();
        timeC.reset();
        nowfloor = 1;
        ChangeFloor(1);

    }

    public void ChangeFloor(int x){
        if(timeC.returnV() <= 0.4 && isfight) return;
        if(view.getGameover()) view.setGameover(false);
        canAtk = false;
        nowfloor = x;
        user.reset();
        monster[x-1].reset();
        timeC.reset();
        view.setMonNum(x-1);
        view.setO2(monster[x-1]);
        timeC.setT(monster[x-1].TimeCheck());
        view.setLevelup(false);
        canAtk = true;
        isfight = true;
        if(nowfloor == 7) SoundManager.getsInstance().play("boss_ap");
    }

    public void Save(){ // 세이브 기능
        SharedPreferences setting = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt("Level", user.getLevel());
        editor.putInt("EXP", user.getEXP2());
        editor.putInt("Str", user.getStr());
        editor.putInt("Dex", user.getDex());
        editor.putInt("ExStat", user.getStatus());
        editor.putInt("UpgradeCount", inventorynWeapone.getUpgradeCount());
        editor.putInt("Dia", inventorynWeapone.getNowCount());
        editor.putInt("UpgradePoint", inventorynWeapone.getUpgradePoint());
        editor.putInt("nowMonster", nowfloor);
        editor.putInt("nowHP", monster[nowfloor-1].HealthCheck2());
        editor.putLong("nowTime", timeC.getTimecount());
        editor.putLong("maxTime", timeC.getFulltime());
        editor.putBoolean("isfight", isfight);
        editor.commit();
    }

    public void Load(){
        SharedPreferences setting = getSharedPreferences(PREFS_NAME,0);
        int x = setting.getInt("nowMonster", 1);
        ChangeFloor(x);
        user.userLoad(setting.getInt("Level",1), setting.getInt("EXP",0), setting.getInt("ExStat",3), setting.getInt("Str",1), setting.getInt("Dex",1));
        inventorynWeapone.invenLoad(setting.getInt("UpgradeCount", 1), setting.getInt("Dia", 0), setting.getInt("UpgradePoint", 0));
        monster[nowfloor-1].setHealthPoint(setting.getInt("nowHP", monster[nowfloor-1].HealthCheck2()));
        timeC.setT(setting.getLong("nowTime",10000), setting.getLong("maxTime", 10000));
        isfight = setting.getBoolean("isfight", true);
        if(!isfight) {
            canAtk = false;
            view.setMonNum(-1);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mHasPerformedLongPress = false;
                postCheckForLongClick(0);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                clickX = (int) event.getX();
                clickY = (int) event.getY();
                if (!mHasPerformedLongPress){
                    removeLongPressCallback();
                    if (clickY >= 345 * scaleS || clickY <= 228 * scaleS) {
                        Attack();// 메뉴 이외에 터치(Y좌표)
                        break;
                    }
                    else if (clickX <= 995 * scaleS && clickX >= 885 * scaleS) {
                        SoundManager.getsInstance().play("button");
                        final Dialog MenuDialog = new Dialog(this);
                        MenuDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        MenuDialog.setContentView(R.layout.menu_main);

                        Button ContinueBtn = (Button) MenuDialog.findViewById(R.id.m_m_continue);
                        Button SaveBtn = (Button) MenuDialog.findViewById(R.id.m_m_save);
                        ToggleButton MusicBtn = (ToggleButton) MenuDialog.findViewById(R.id.m_m_music);
                        Button EndBtn = (Button) MenuDialog.findViewById(R.id.m_m_end);

                        ContinueBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                Reset_all();
                                if(view.getGameover()) view.setGameover(false);
                                MenuDialog.dismiss();
                            }
                        });
                        SaveBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                Save();
                                MenuDialog.dismiss();
                            }
                        });
                        MusicBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                boolean on=((ToggleButton) v).isChecked();
                                if(on){
                                    Music(MusicCheck);
                                    MusicCheck=0;
                                }
                                else{
                                    Music(MusicCheck);
                                    MusicCheck=1;
                                }
                            }

                        });
                        EndBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                Save();
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        });
                        MenuDialog.show();
                    } else if (clickX <= 860 * scaleS && clickX >= 770 * scaleS) {
                        SoundManager.getsInstance().play("button");
                        final Dialog UpgradeDialog = new Dialog(this);
                        UpgradeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        UpgradeDialog.setContentView(R.layout.menu_upgrade);

                        TextView txt1 = (TextView) UpgradeDialog.findViewById(R.id.weaponeLevel);
                        txt1.setText(""+inventorynWeapone.getUpgradeCount());
                        TextView txt2 = (TextView) UpgradeDialog.findViewById(R.id.weaponePower);
                        txt2.setText(""+inventorynWeapone.getAtk());
                        TextView txt3 = (TextView) UpgradeDialog.findViewById(R.id.weaponeDia);
                        txt3.setText(""+(inventorynWeapone.getNowCount()-inventorynWeapone.getNeedCount()));
                        TextView txt4 = (TextView) UpgradeDialog.findViewById(R.id.weaponeNeed);
                        txt4.setText(inventorynWeapone.getUpgradeRating() + " %");
                        ImageView UpgradeBtn = (ImageView) UpgradeDialog.findViewById(R.id.weaponeATK);

                        UpgradeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int i = inventorynWeapone.upgrade();
                                if (i != 0) {SoundManager.getsInstance().play("hammer");}
                                try{wait(2000);}catch(Exception e){}
                                if( i == 1 ){
                                    SoundManager.getsInstance().play("fail");
                                    TextView txt3 = (TextView) UpgradeDialog.findViewById(R.id.weaponeDia);
                                    txt3.setText(""+(inventorynWeapone.getNowCount()-inventorynWeapone.getNeedCount()));
                                    TextView txt4 = (TextView) UpgradeDialog.findViewById(R.id.weaponeNeed);
                                    txt4.setText(inventorynWeapone.getUpgradeRating() + " %");
                                }
                                else if( i == 2){
                                    SoundManager.getsInstance().play("success");
                                    TextView txt1 = (TextView) UpgradeDialog.findViewById(R.id.weaponeLevel);
                                    txt1.setText(""+inventorynWeapone.getUpgradeCount());
                                    TextView txt2 = (TextView) UpgradeDialog.findViewById(R.id.weaponePower);
                                    txt2.setText(""+inventorynWeapone.getAtk());
                                    TextView txt3 = (TextView) UpgradeDialog.findViewById(R.id.weaponeDia);
                                    txt3.setText(""+(inventorynWeapone.getNowCount()-inventorynWeapone.getNeedCount()));
                                    TextView txt4 = (TextView) UpgradeDialog.findViewById(R.id.weaponeNeed);
                                    txt4.setText(inventorynWeapone.getUpgradeRating() + " %");
                                }
                                Save();
                            }
                        });

                        UpgradeDialog.show();
                    } else if (clickX <= 745 * scaleS && clickX >= 625 * scaleS) {
                        SoundManager.getsInstance().play("button");
                        final Dialog AbilityDialog = new Dialog(this);
                        AbilityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        AbilityDialog.setContentView(R.layout.menu_ability);

                        ImageView Btn2 = (ImageView) AbilityDialog.findViewById(R.id.m_a_btn2);
                        ImageView Btn3 = (ImageView) AbilityDialog.findViewById(R.id.m_a_btn3);

                        TextView txt1 = (TextView) AbilityDialog.findViewById(R.id.Level);
                        txt1.setText(String.valueOf(user.getLevel()));
                        TextView txt2 = (TextView) AbilityDialog.findViewById(R.id.Stat);
                        txt2.setText(String.valueOf(user.getStatus()));
                        TextView txt3 = (TextView) AbilityDialog.findViewById(R.id.str);
                        txt3.setText(String.valueOf(user.getStr()));
                        TextView txt4 = (TextView) AbilityDialog.findViewById(R.id.dex);
                        txt4.setText(String.valueOf(user.getDex()));
                        TextView txt5 = (TextView) AbilityDialog.findViewById(R.id.EXP);
                        txt5.setText(user.getEXP2()+" / "+user.getEXP());

                        Btn2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                user.putStr();
                                TextView txt2 = (TextView) AbilityDialog.findViewById(R.id.Stat);
                                txt2.setText(String.valueOf(user.getStatus()));
                                TextView txt3 = (TextView) AbilityDialog.findViewById(R.id.str);
                                txt3.setText(String.valueOf(user.getStr()));
                            }
                        });
                        Btn3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                user.putDex();
                                TextView txt2 = (TextView) AbilityDialog.findViewById(R.id.Stat);
                                txt2.setText(String.valueOf(user.getStatus()));
                                TextView txt4 = (TextView) AbilityDialog.findViewById(R.id.dex);
                                txt4.setText(String.valueOf(user.getDex()));
                            }
                        });
                        AbilityDialog.show();
                    } else if (clickX <= 600 * scaleS && clickX >= 484 * scaleS) {
                        SoundManager.getsInstance().play("button");
                        final Dialog StageDialog = new Dialog(this);
                        StageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        StageDialog.setContentView(R.layout.menu_stage);

                        ImageView Stage1Btn = (ImageView) StageDialog.findViewById(R.id.m_c_1);
                        ImageView Stage2Btn = (ImageView) StageDialog.findViewById(R.id.m_c_2);
                        ImageView Stage3Btn = (ImageView) StageDialog.findViewById(R.id.m_c_3);
                        ImageView Stage4Btn = (ImageView) StageDialog.findViewById(R.id.m_c_4);
                        ImageView Stage5Btn = (ImageView) StageDialog.findViewById(R.id.m_c_5);
                        ImageView Stage6Btn = (ImageView) StageDialog.findViewById(R.id.m_c_6);
                        ImageView Stage7Btn = (ImageView) StageDialog.findViewById(R.id.m_c_7);

                        Stage1Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                ChangeFloor(1);
                                StageDialog.dismiss();
                            }
                        });
                        Stage2Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                ChangeFloor(2);
                                StageDialog.dismiss();
                            }
                        });
                        Stage3Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                ChangeFloor(3);
                                StageDialog.dismiss();
                            }
                        });
                        Stage4Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                ChangeFloor(4);
                                StageDialog.dismiss();
                            }
                        });
                        Stage5Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                ChangeFloor(5);
                                StageDialog.dismiss();
                            }
                        });
                        Stage6Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                ChangeFloor(6);
                                StageDialog.dismiss();
                            }
                        });
                        Stage7Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SoundManager.getsInstance().play("button");
                                ChangeFloor(7);
                                StageDialog.dismiss();
                            }
                        });

                        StageDialog.show();
                    } else {
                        Attack();
                        //터치일경우 ~~
                    }
                }
                break;



        }
        return super.onTouchEvent(event);
    }

    public void Attack(){
        synchronized (this){
            if(!canAtk) return;
            if(!user.Attack()) return;
            SoundManager.getsInstance().play("attack_ch");
            view.setCharNum(1);
            try {
                wait(250);
            }
            catch (Exception e){ // 공격~
            }
            view.setCharNum(2);

            SoundManager.getsInstance().play("attack");
            int sum = user.Damage() + inventorynWeapone.getAtk();
            inventorynWeapone.setUpgradePoint();
            if(monster[nowfloor - 1].Damage(sum)) GotKill();
            try {
                wait(250);
            }
            catch (Exception e){ // 원래대로
            }
            view.setCharNum(0);
        }
    }

    public void Skill() {
        synchronized (this) {
            if (!canAtk) return;
            if (!user.Skill()) return;

            view.setCharNum(3);

            SoundManager.getsInstance().play("skill");
            inventorynWeapone.setUpgradePoint();
            int sum = user.Damage() + inventorynWeapone.getAtk();
            if (monster[nowfloor - 1].Damage(sum * 2)) GotKill();
            try {
                wait(500);
            } catch (Exception e) { // 이펙트 방출 / 원래대로
            }
            view.setCharNum(0);
        }
    }

    public void GotKill(){
        SoundManager.getsInstance().play("monster_die");
        isfight = false;
        canAtk = false;
        view.setMonNum(-1);
        inventorynWeapone.setNowCount(monster[nowfloor-1].Drop());
        if(user.setEXP(monster[nowfloor-1].GetExp())){
            view.setLevelup(true);
        }
        Save();
    }

    public void Music(int check){
        //음악 on/off 하는 기능
        if(check==1){
            stopService(new Intent(this,MusicService.class));
        }
        if(check==0){
            startService(new Intent(this,MusicService.class));
        }
    }


    //이하 터치시 롱클릭 구현
    Handler mHandler = new Handler();

    class CheckForLongPress implements Runnable {

        public void run() {
            if (performLongClick()) {
                mHasPerformedLongPress = true;
            }
        }
    }

    private void postCheckForLongClick(int delayOffset) {
        mHasPerformedLongPress = false;

        if (mPendingCheckForLongPress == null) {
            mPendingCheckForLongPress = new CheckForLongPress();
        }

        mHandler.postDelayed(mPendingCheckForLongPress,
                ViewConfiguration.getLongPressTimeout() - delayOffset);
    }

    private void removeLongPressCallback() {
        if (mPendingCheckForLongPress != null) {
            mHandler.removeCallbacks(mPendingCheckForLongPress);
        }
    }

    public boolean performLongClick() {
        Skill();

        return true;
    }

    protected void onUserLeaveHint() {
        Save();
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onUserLeaveHint();
    }


}


