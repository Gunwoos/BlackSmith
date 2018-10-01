package kr.co.company.BlackSmith_ver_02;

import android.widget.ImageView;

public class Monster {
    private int maxHealthPoint, healthPoint, dropItem, exp;
    private long timeToEnd;

    public Monster(int max, int expp, float time, int dropI){
        maxHealthPoint = max;
        healthPoint = max;
        dropItem = dropI;
        exp = expp;
        timeToEnd = (long)time*1000;
    }

    public boolean Damage(int dmg){
        healthPoint -= dmg;
        if (healthPoint <= 0) return true;
        else return false;
    }

    public void setHealthPoint(int x){healthPoint = x;}
    public float returnV(){
        return ((float)healthPoint/(float)maxHealthPoint);
    }

    public int GetExp(){
        return exp;
    }

    public long TimeCheck(){
        return timeToEnd;
    }

    public int HealthCheck(){
        return maxHealthPoint;
    }
    public int HealthCheck2(){
        return healthPoint;
    }


    public int Drop(){ // 드롭 실패시 -1 반환
        if ( Math.random() > 0.1 ) return dropItem;
        else return dropItem*2;
    }

    public void reset(){
        healthPoint = maxHealthPoint;
    }
}