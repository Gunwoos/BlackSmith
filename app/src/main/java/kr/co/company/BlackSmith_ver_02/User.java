package kr.co.company.BlackSmith_ver_02;

import android.widget.ImageView;

public class User{
    private int needEXP, nowEXP, level, leastStatus, status, str, dex, maxStamina, nowStamina, atk;
    private float atkSpeed;

    public User(){
        needEXP = 10;
        nowEXP = 0;
        level = 1;
        leastStatus = 3;
        status = 3;
        str = 1;
        dex = 1;
        maxStamina = 10;
        nowStamina = maxStamina;
        atk = 10;
        atkSpeed = 1000;
    }

    public void die(){
        needEXP = 10;
        nowEXP = 0;
        level = 1;
        leastStatus = 3;
        status = 3;
        str = 1;
        dex = 1;
        maxStamina = 10;
        nowStamina = maxStamina;
        atk = 10;
        atkSpeed = 1000;
    }

    public float returnV(){
        return ((float)nowStamina/(float)maxStamina);
    }
    public int Damage(){
        return atk;
    }
    public void reset(){
        nowStamina = maxStamina;
    }
    public int getStatus(){
        return leastStatus;
    }
    public int getLevel(){ return level; }
    public int getEXP(){return needEXP;}
    public int getEXP2(){return nowEXP;}

    public boolean setEXP(int x){
        nowEXP += x;
        if(nowEXP >= needEXP){
            level ++;
            needEXP = 10 + level * 5;
            nowEXP = 0;
            status = level * 2;
            leastStatus += 2;
            return true;
        }
        else return false;
    }

    public void userLoad(int lv, int exp, int stat, int st, int de){
        level = lv;
        needEXP = 10 + level * 5;
        status = level * 2;
        nowEXP = exp;
        leastStatus = stat;
        str = st;
        atk = 8 + st*2;
        dex = de;
        maxStamina += de - 1;
    }

    public int getStr(){return str;}
    public int getDex(){return dex;}


    public boolean Attack(){
        synchronized (this) {
            if (nowStamina >= 2) {
                nowStamina -= 2;
                return true;
            } else return false;
        }
    }

    public boolean Skill(){
        if(nowStamina >= 5){
            nowStamina -= 5;
            return true;
        }
        else return false;
    }


    public void putStr(){
        if(leastStatus > 0) {
            leastStatus--;
            str++;
            atk += 2;
        }
    }

    public void putDex(){
        if(leastStatus > 0) {
            leastStatus--;
            maxStamina += 1;
            nowStamina = maxStamina;
            dex++;
        }
    }

    public void setNowStamina(){
        nowStamina += 2;
        if(nowStamina > maxStamina) nowStamina = maxStamina;
        //스테미나 바 조정
    }
}