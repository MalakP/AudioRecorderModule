package digitalfish.test.audiorecordermodule;


import android.app.Activity;
import android.os.Bundle;

import digitalfish.test.audiorecordermodule.fragments.AudioFragment;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_for_fragment, new AudioFragment())
                .commit();
    }


}
