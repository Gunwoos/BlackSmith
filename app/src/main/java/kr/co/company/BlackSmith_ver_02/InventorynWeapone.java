package kr.co.company.BlackSmith_ver_02;

public class InventorynWeapone {
    private int atk, upgradeCount, needCount, nowCount, upgradePoint;
    private float upgradeRating;


    public InventorynWeapone(){
        atk = 1;
        upgradeCount = 1;
        needCount = 1;
        nowCount = 0;
        upgradeRating = 100;
        upgradePoint = 0;
    }
    public int getAtk(){
        return atk;
    }
    public int getUpgradeCount(){
        return upgradeCount;
    }
    public int getNeedCount(){
        return needCount;
    }
    public int getNowCount(){
        return nowCount;
    }
    public float getUpgradeRating(){ return upgradeRating; }
    public int getUpgradePoint(){return upgradePoint;}
    public float returnV(){ return ((float)upgradePoint/(float)100); }
    public void setNowCount(int a){nowCount += a;}
    public void setUpgradePoint(){
        if(upgradePoint > 100) return;
        upgradePoint += 4;
        if(upgradePoint > 100) upgradePoint = 100;
    }

    public void die(){
        atk = 1;
        upgradeCount = 1;
        needCount = 1;
        nowCount = 0;
        upgradeRating = 100;
        upgradePoint = 0;
    }

    public void invenLoad(int upC, int nowC, int dia){
        up(upC);
        nowCount = nowC;
        upgradePoint = dia;
    };

    public void up(int x){
        for(int i=1; i<x; i++){
            upgradeCount += 1;
            if(upgradeCount < 6) {
                atk += 1;
                needCount = atk - 1;
                upgradeRating -= 10;
            }
            else if(upgradeCount < 8) {
                atk += 2;
                needCount = atk - 2;
                upgradeRating -= 5;
            }
            else if(upgradeCount < 10) {
                atk += 3;
                needCount = atk - 3;
                upgradeRating -= 5;
            }
            else if(upgradeCount < 15) {
                atk += 4;
                needCount = atk - 4;
                upgradeRating -= 2;
            }
            else if(upgradeCount < 21) {
                atk += 5;
                needCount = atk - 5;
                upgradeRating -= 1;
            }
            else if(upgradeCount < 26) {
                atk += 8;
                needCount = atk - 8;
                upgradeRating -= 1;
            }
            else if(upgradeCount < 31) {
                atk += 10;
                needCount = atk - 10;
                upgradeRating -= 1;
            }
        }
    }

    public int upgrade(){ // 0재료부족, 1실패, 2성공
        if( nowCount >= needCount && upgradePoint == 100){
            nowCount -= needCount;
            upgradePoint = 0;
            if (Math.random() < (upgradeRating/100) && upgradeCount < 30) {
                upgradeCount += 1;
                if(upgradeCount < 6) {
                    atk += 1;
                    needCount = atk - 1;
                    upgradeRating -= 10;
                }
                else if(upgradeCount < 8) {
                    atk += 2;
                    needCount = atk - 2;
                    upgradeRating -= 5;
                }
                else if(upgradeCount < 10) {
                    atk += 3;
                    needCount = atk - 3;
                    upgradeRating -= 5;
                }
                else if(upgradeCount < 15) {
                    atk += 4;
                    needCount = atk - 4;
                    upgradeRating -= 2;
                }
                else if(upgradeCount < 21) {
                    atk += 5;
                    needCount = atk - 5;
                    upgradeRating -= 1;
                }
                else if(upgradeCount < 26) {
                    atk += 8;
                    needCount = atk - 8;
                    upgradeRating -= 1;
                }
                else if(upgradeCount < 31) {
                    atk += 10;
                    needCount = atk - 10;
                    upgradeRating -= 1;
                }
                return 2;
            }
            return 1;
        }
        return 0;
    }



}
