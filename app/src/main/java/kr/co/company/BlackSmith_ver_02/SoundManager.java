package kr.co.company.BlackSmith_ver_02;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import java.security.PrivateKey;
import java.util.HashMap;

public class SoundManager {
    private static SoundManager sInstance;
    private SoundPool soundPool;
    private HashMap<String, Integer> map, map_s;
    private Context context;

    private SoundManager(){}

    public static SoundManager getsInstance(){
        if(sInstance==null){
            sInstance = new SoundManager();
        }
        return sInstance;
    }

    public void init(Context context){
        this.context = context;
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        map = new HashMap<String, Integer>();
        map_s = new HashMap<String, Integer>();
    }
    public void addSound(String index, int resId){
        int id = soundPool.load(context, resId, 1);
        map.put(index, id);
    }

    public void play(String index){
        int id_s = soundPool.play(map.get(index), 1f,1f,0,0,1f);
        map_s.put(index, id_s);
    }

    public void stopSound(String index){
        soundPool.stop(map_s.get(index));
    }

    public void clear(){
        soundPool.release();
        soundPool = null;
    }
}