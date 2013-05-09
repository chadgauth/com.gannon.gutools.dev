package com.gannon.gutools.activities;

import com.gannon.gutools.dev.R;
import com.spoledge.aacplayer.AACPlayer;
import com.spoledge.aacplayer.ArrayAACPlayer;
import com.spoledge.aacplayer.ArrayDecoder;
import com.spoledge.aacplayer.Decoder;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
public class StreamService extends Service implements com.spoledge.aacplayer.PlayerCallback {

	private final IBinder binder = new MyBinder();
    AACPlayer aacPlayer;
    boolean playerStarted=false;
    int progress;

    public class MyBinder extends Binder {
        StreamService getService() {
            return StreamService.this;
        }
    }
    
    public int onStartCommand(Intent intent, int flags, int startId){
    	
    	return START_STICKY;
    }
    
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	public void startStream(){
		start( Decoder.DECODER_FFMPEG_WMA );
	}
	public void stopStream(){
		stop();
	}

	private void start( int decoder ) {
        stop();
        aacPlayer = new ArrayAACPlayer( ArrayDecoder.create( decoder ), this,
                                        AACPlayer.DEFAULT_AUDIO_BUFFER_CAPACITY_MS, 
                                        AACPlayer.DEFAULT_DECODE_BUFFER_CAPACITY_MS);
        aacPlayer.playAsync(getString(R.string.live_stream));
        Notification note=new Notification(R.drawable.ic_stat_music_notify,
                "Music Playing",
                System.currentTimeMillis());
        Intent i=new Intent(this, StreamService.class);

		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
		Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		PendingIntent pi=PendingIntent.getActivity(this, 0,
		                        i, 0);
	
		note.setLatestEventInfo(this, "Now Playing 90.5 WERG",
		    "Erie's Rock Alternative",
		    pi);
		note.flags|=Notification.FLAG_NO_CLEAR;
		
		startForeground(1337, note);
	}

	private void stop() {
		if (aacPlayer != null) { aacPlayer.stop(); aacPlayer = null; }
		stopForeground(true);
	}

	public void onDestroy() {
		stop();
		super.onDestroy();	
	}

	public void playerStarted() {
		playerStarted = true;
    }

    public void playerStopped( final int perf ) {
    	playerStarted = false;
    }

    public void playerPCMFeedBuffer( final boolean isPlaying,
            final int audioBufferSizeMs, final int audioBufferCapacityMs ) {
    		progress = audioBufferSizeMs * 100 / audioBufferCapacityMs;
    }

	@Override
	public void playerException(Throwable t) {
		// TODO Auto-generated method stub

	}
}