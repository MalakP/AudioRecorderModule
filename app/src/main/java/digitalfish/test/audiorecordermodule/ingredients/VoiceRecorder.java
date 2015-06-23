package digitalfish.test.audiorecordermodule.ingredients;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by Piotr Malak on 2015-06-19.
 */
public class VoiceRecorder {

    private static final String LOG_TAG = "AudioRecorder";
    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;
    public final static String DIRECTORY_FOR_RECORDINGS = Environment.getExternalStorageDirectory().getAbsolutePath()+"/audio_recorder/audio_notes/";
    String TempFileName = "tmp.3gp";
    final int MAX_RECORDINGS_DURATION = 30000;

    boolean mIsRecording = false;
    boolean mIsPlaying = false;

    IOnStopRecording mIOnStopRecording;
    IOnStopPlaying mIOnStopPlaying;
    IOnPositionChange mIOnPositionChange;

    PmTimer mTimer;

    public VoiceRecorder() {
        File folder = new File(DIRECTORY_FOR_RECORDINGS);
        if(!folder.exists()){
            if(!folder.mkdirs())
                Log.e(LOG_TAG,"problem crating recordings dir");
        }
        mTimer = new PmTimer();
    }

    private void startPlaying(String pFileName) {
        String lFullFileName = DIRECTORY_FOR_RECORDINGS +pFileName;
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(lFullFileName);
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer pMediaPlayer) {
                        mIOnStopPlaying.onStopPlaying();
                        setIsPlaying(false);
                }
            });

            setIsPlaying(true);

        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
        setIsPlaying(false);

    }


    private void startRecording(String pFileName) {

        String lFullFileName = DIRECTORY_FOR_RECORDINGS +pFileName;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(lFullFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setMaxDuration(MAX_RECORDINGS_DURATION);
        mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder pMediaRecorder, int pWhat, int i1) {

                Log.i(LOG_TAG, "info listener, what: " + pWhat + ", error: " + i1);
                stopRecording();
            }
        });

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed: +"+e.getMessage());
        }

        mRecorder.start();
        mIsRecording = true;
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;

        mIOnStopRecording.onStopRecording(TempFileName);
        mIsRecording = false;
    }

    public void onRecord(boolean pIsRecording) {
        if (!pIsRecording) {
            startRecording(TempFileName);
        } else {
            stopRecording();
        }
    }

    public void onPlay(boolean pIsPlaying) {
        if (!pIsPlaying) {
            startPlaying(TempFileName);
        } else {
            stopPlaying();
        }
    }

    public boolean isRecording() {
        return mIsRecording;
    }

    public void setIsRecording(boolean pIsRecording) {
        mIsRecording = pIsRecording;
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    public void setIsPlaying(boolean pIsPlaying) {
        mIsPlaying = pIsPlaying;
        if(mIsPlaying){
            mTimer.startTimer(500, new PmTimer.timerCallback() {
                @Override
                public void onClick() {
                    if(mPlayer!=null)
                        mIOnPositionChange.onPositionChange(mPlayer.getDuration(),mPlayer.getCurrentPosition());
                }
            });
        }else{
            mTimer.stopTimer();
            mIOnPositionChange.onPositionChange(0,0);
        }

    }

    public void seekTo(int pProgress){
        if(mPlayer !=null){
            mPlayer.seekTo(pProgress);
        }
    }



    public interface IOnStopRecording{
        void onStopRecording(String recordName);
    }

    public void setIOnStopRecording(IOnStopRecording pIOnStopRecording) {
        mIOnStopRecording = pIOnStopRecording;
    }

    public interface IOnStopPlaying{
        void onStopPlaying();
    }

    public void setIOnStopPlaying(IOnStopPlaying pIOnStopPlaying) {
        mIOnStopPlaying = pIOnStopPlaying;
    }

    public interface IOnPositionChange{
        void onPositionChange(int pLength, int pPosition);
    }

    public void setIOnPositionChange(IOnPositionChange pIOnPositionChange) {
        mIOnPositionChange = pIOnPositionChange;
    }
}
