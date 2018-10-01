package kr.co.company.BlackSmith_ver_02;

import java.sql.Time;

/**
 * Created by 민웅기 on 2017-06-02.
 */

public class TimeC extends Thread {

    long curTime, lastTime, staminatime, time, atkspeed;
    long timecount, fulltime;
    //현제 시간, 저장된 마지막 시간, 스테미나 체크, 공격체크, 공격속도(공격체크+), 콤보시간 체크, 남은 시간
    User user;
    PlayActivity Pa;

    boolean c_Atk,c_Skill; //쿨다운 체크크

    @Override
    public void start() {
        super.start();
    }

    public void setPa(PlayActivity a){
        Pa = a;
    }
    public long getTimecount(){ return timecount; }
    public long getFulltime(){ return fulltime; }

    public void setT(long tC){
        timecount = tC;
        fulltime = timecount;
    }
    public void setT(long tC, long tC2){
        timecount = tC;
        fulltime = tC2;
    }
    public void setUser(User u){
        user = u;
    }

    public TimeC(){
        curTime = 0;
        lastTime = 0;
        staminatime = 0;
        time = 0;
        atkspeed = 1000;
        timecount = 10000;
        fulltime = timecount;
        c_Atk = true;
        c_Skill = true;
    }

    public void reset(){
        staminatime = 0;
        time = 0;
        atkspeed = 1000;
        timecount = 10000;
        fulltime = timecount;
        c_Atk = true;
        c_Skill = true;
    }

    public void turnoff(){
        if(Pa.isfight) Pa.TimeEnd();
    }

    public float returnV(){
        return ((float)timecount / (float)fulltime);
    }

    @Override
    public synchronized void run() {
        super.run();
            lastTime = System.currentTimeMillis();
            staminatime = lastTime;
            for(;;) {
                curTime = System.currentTimeMillis(); // 현재 시간 바꾸고
                long sum = curTime - lastTime;
                if (sum > 100) { // 저장된 시간 - 현제시간 ( 지나간 시간 구함 )
                    if ((time < atkspeed)) { // 공격가능시간 안됐으면
                        time += sum; // 공격가능시간까지 시간 ++
                    } else c_Atk = true;// 됐으면 공격활성화
                     // 스테미나
                    if (curTime - staminatime >= 1000) { // 1초 넘으면 스테미나 1 회복
                        user.setNowStamina();
                        staminatime = curTime;
                    }
                     // 스테이지 남은 시간
                    if (timecount > 0) timecount -= sum;
                    else turnoff();

                    lastTime = curTime; // 남은 시간 현재 시간으로 바꿔주기

                }
            }
    }
}
