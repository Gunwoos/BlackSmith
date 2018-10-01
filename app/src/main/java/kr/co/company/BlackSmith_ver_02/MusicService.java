package kr.co.company.BlackSmith_ver_02;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service{
    private static final String TAG="MusicService";
            MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onCreate(){
        Log.d(TAG,"onCreate()");

        player=MediaPlayer.create(this,R.raw.bgm_main);
        player.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.d(TAG,"onStart()");
        player.start();
        return super.onStartCommand(intent,flags,startId);
    }

    public void onDestroy(){
        super.onDestroy();
        if(player!=null) {
            Log.d(TAG, "onDestroy()");
            player.stop();
        }
    }
}
