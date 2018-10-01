package kr.co.company.BlackSmith_ver_02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class BackView extends SurfaceView implements SurfaceHolder.Callback {

    private BackViewThread thread;
    private Bitmap backGround;
    private Bitmap[] character;
    private Bitmap[] monster;
    private Bitmap[] bar2;
    private Bitmap ef_atk;
    private int charNum;
    private int monNum;
    private Bitmap[] bar; // 0 = time, 1 = monsterHP, 2 = Stamina, 3 = Resource
    private int bar_max;
    private Paint p;
    private Bitmap levelup;
    private Bitmap gameover;
    private Bitmap skill;

    boolean levelupc, gameoverc;
    DisplayMetrics dm;
    int width, height;

    float scaleS;
    float scaleS2;

    TimeC tc;
    User us;
    Monster mt;
    InventorynWeapone iw;

    public BackViewThread returnT(){
        return thread;
    }

    public float getScaleS(){return scaleS;}
    public float getScaleS2(){return scaleS2;}

    Bitmap ImageScaleSet(Bitmap a, int width, int height){
        Bitmap result = Bitmap.createScaledBitmap(a,width,height,false);
        return result;
    }

    Bitmap ImageScalseSet2(Bitmap a, float scale, float scale2){
        Bitmap result = Bitmap.createScaledBitmap(a,(int)(a.getWidth()*scale),(int)(a.getHeight()*scale2),false);
        return result;
    }

    public Bitmap ImageXset(Bitmap b, int max, float value){ // max = 원래 길이
        float i = (float)max*value;
        if(i < 1) i = 1;
        Bitmap result = Bitmap.createScaledBitmap(b, (int)i, b.getHeight(), false);
        return result;
    }

    public BackView(Context context){
        super(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        p = new Paint();

        dm = context.getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;

        charNum = 0;
        monNum = 0;

        //backGround = BitmapFactory.decodeResource(context.getResources(),
        //        R.drawable.background);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;

        backGround = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.background, opts);
        backGround = ImageScaleSet(backGround, width, height);
        scaleS = (float)width / (float)1080;
        scaleS2 = (float)height / (float)1920;

        character = new Bitmap[3];
        character[0] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.idle, opts);
        character[1] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.dash, opts);
        character[2] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.atk, opts);

        for(int i=0; i<3; i++){
           character[i] = ImageScalseSet2(character[i], scaleS, scaleS2);
        }

        monster = new Bitmap[7];
        monster[0] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.m_slime, opts);
        monster[1] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.m_spider, opts);
        monster[2] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.m_snake, opts);
        monster[3] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.m_ali, opts);
        monster[4] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.m_golem, opts);
        monster[5] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.m_flower, opts);
        monster[6] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.m_tree, opts);

        for(int i=0; i<7; i++){
            monster[i] = ImageScalseSet2(monster[i], scaleS, scaleS2);
        }

        bar = new Bitmap[4];
        bar[0] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.bar_time, opts);
        bar[1] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.bar_hp, opts);
        bar[2] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.bar_stamina, opts);
        bar[3] = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.bar_upgradecost, opts);

        for(int i=0; i<4; i++){
            bar[i] = ImageScalseSet2(bar[i], scaleS, scaleS2);
        }
        bar_max = bar[2].getWidth();
        bar[3] = ImageXset(bar[3], bar_max , 0);

        bar2 = new Bitmap[4];
        for(int i=0; i<4; i++){
            bar2[i] = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.bar, opts);
            bar2[i] = ImageScalseSet2(bar2[i], scaleS, scaleS2);
        }

        ef_atk = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.hit_effect, opts);
        ef_atk = ImageScalseSet2(ef_atk, scaleS, scaleS2);

        levelup = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.levelup, opts);
        levelup = ImageScalseSet2(levelup, scaleS, scaleS2);
        levelupc = false;

        gameover = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.gameover, opts);
        gameover = ImageScalseSet2(gameover, scaleS, scaleS2);
        gameoverc = false;

        skill = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.skill, opts);
        skill = ImageScalseSet2(skill, scaleS, scaleS2);

        thread = new BackViewThread(holder);

    }

    public BackViewThread getThread() {return thread;}
    public void setGameover(boolean a) {gameoverc = a;}
    public void setLevelup(boolean a) {levelupc = a;}
    public boolean getGameover() {return gameoverc;}
    public boolean getLevelup() {return levelupc;}

    public void setO(TimeC t, User u, Monster m, InventorynWeapone i){
        tc = t;
        us = u;
        mt = m;
        iw = i;
    }
    public void setO2(Monster m){
        mt = m;
    }
    public void setCharNum(int a){
        charNum = a;
    }
    public void setMonNum(int a){
        monNum = a;
    }

    public void surfaceCreated(SurfaceHolder holder){
        thread.setRunning(true);
        thread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {} // 구현 해놔라
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread.setRunning(false);
    }

    void DrawA(Canvas Cs){
        synchronized (this) {
            Cs.drawBitmap(backGround, 0, 0, p);

            if (monNum == 0) Cs.drawBitmap(monster[0], (624 * scaleS), (1137 * scaleS2), p);
            else if (monNum == 1) Cs.drawBitmap(monster[1], (683 * scaleS), (1107 * scaleS2), p);
            else if (monNum == 2) Cs.drawBitmap(monster[2], (693 * scaleS), (1080 * scaleS2), p);
            else if (monNum == 3) Cs.drawBitmap(monster[3], (683 * scaleS), (1080 * scaleS2), p);
            else if (monNum == 4) Cs.drawBitmap(monster[4], (549 * scaleS), (945 * scaleS2), p);
            else if (monNum == 5) Cs.drawBitmap(monster[5], (673 * scaleS), (1059 * scaleS2), p);
            else if (monNum == 6) Cs.drawBitmap(monster[6], (610 * scaleS), (831 * scaleS2), p);

            if (charNum == 0) Cs.drawBitmap(character[0], (18 * scaleS), (692 * scaleS2), p);
            else if (charNum == 1) Cs.drawBitmap(character[1], (207 * scaleS), (780 * scaleS2), p);
            else if (charNum == 2){
                Cs.drawBitmap(character[2], (438 * scaleS), (819 * scaleS2), p);
                Cs.drawBitmap(ef_atk, (674 * scaleS), (949 * scaleS2), p);
            }
            else if (charNum == 3){
                Cs.drawBitmap(character[2], (18 * scaleS), (692 * scaleS2), p);
                Cs.drawBitmap(skill, (722 * scaleS), (518 * scaleS2), p);
            }

            bar[0] = ImageXset(bar[0], bar_max, tc.returnV());
            Cs.drawBitmap(bar[0], (97 * scaleS), (78 * scaleS2), p);
            bar[1] = ImageXset(bar[1], bar_max, mt.returnV());
            Cs.drawBitmap(bar[1], (97 * scaleS), (158 * scaleS2), p);
            bar[2] = ImageXset(bar[2], bar_max, us.returnV());
            Cs.drawBitmap(bar[2], (97 * scaleS), (1737 * scaleS2), p);
            bar[3] = ImageXset(bar[3], bar_max, iw.returnV());
            Cs.drawBitmap(bar[3], (97 * scaleS), (1839 * scaleS2), p);

            Cs.drawBitmap(bar2[0], (85 * scaleS), (67 * scaleS2), p);
            Cs.drawBitmap(bar2[1], (85 * scaleS), (147 * scaleS2), p);
            Cs.drawBitmap(bar2[2], (85 * scaleS), (1726 * scaleS2), p);
            Cs.drawBitmap(bar2[3], (85 * scaleS), (1828 * scaleS2), p);

            if (levelupc) Cs.drawBitmap(levelup, (65 * scaleS), (599 * scaleS2), p);
            if (gameoverc) Cs.drawBitmap(gameover, (59 * scaleS), (801 * scaleS2), p);
        }
    }

    public class BackViewThread extends Thread {
        SurfaceHolder holder;
        boolean isrun;

        public BackViewThread(SurfaceHolder holderr){
            holder = holderr;
        }

        public synchronized void run() {
            while (isrun) {
                try{sleep(100);}catch(Exception e){}
                Canvas c = holder.lockCanvas();
                DrawA(c);
                holder.unlockCanvasAndPost(c);
            }
        }

        public void setRunning(boolean b){
            isrun = b;
        }
    }
}

