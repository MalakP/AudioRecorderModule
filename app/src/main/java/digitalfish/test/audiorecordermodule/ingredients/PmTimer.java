package digitalfish.test.audiorecordermodule.ingredients;

import android.os.Handler;

/**
 * Created by Piotr on 04.05.14.
 */
public class PmTimer {

   timerCallback mTimerCallback;
    Handler timerHandler;
    int mPeriod;
    public void startTimer(int period, final timerCallback cb) {

        mPeriod = period;
        mTimerCallback = cb;
        timerHandler = new Handler();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void stopTimer(){
        if(timerHandler!=null){
            timerHandler.removeCallbacks(timerRunnable);
        }
    }

    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            if(mTimerCallback!=null){
                mTimerCallback.onClick();
            }
            if(mPeriod>0)
                timerHandler.postDelayed(this, mPeriod);
        }
    };

    public interface timerCallback{
        void onClick();
    }
}
