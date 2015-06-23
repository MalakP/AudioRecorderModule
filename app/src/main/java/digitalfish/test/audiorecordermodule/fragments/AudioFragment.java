package digitalfish.test.audiorecordermodule.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import digitalfish.test.audiorecordermodule.R;
import digitalfish.test.audiorecordermodule.ingredients.VoiceRecorder;


public class AudioFragment extends Fragment {

    VoiceRecorder mVoiceRecorder;
    @InjectView(R.id.text_record_name)
    TextView mTextRecordName;
    @InjectView(R.id.button_record)
    Button mButtonRecord;
    @InjectView(R.id.button_play)
    Button mButtonPlay;
    @InjectView(R.id.seekBar)
    SeekBar mSeekBar;


    public static AudioFragment newInstance(String param1, String param2) {
        AudioFragment fragment = new AudioFragment();
        return fragment;
    }

    public AudioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_recorder, container, false);
        ButterKnife.inject(this, view);
        mVoiceRecorder = new VoiceRecorder();

        mButtonRecord.setOnClickListener(mClickRecordingListener);
        mButtonPlay.setOnClickListener(mClickPlayListener);
        mVoiceRecorder.setIOnStopRecording(new VoiceRecorder.IOnStopRecording() {
            @Override
            public void onStopRecording(String recordName) {
                mButtonRecord.setText(R.string.audio_notes_start_recording);
                mTextRecordName.setText(recordName);
            }
        });
        mVoiceRecorder.setIOnStopPlaying(new VoiceRecorder.IOnStopPlaying() {
            @Override
            public void onStopPlaying() {
                mButtonPlay.setText(R.string.audio_notes_start_play);
                mSeekBar.setVisibility(View.INVISIBLE);
            }
        });
        mVoiceRecorder.setIOnPositionChange(new VoiceRecorder.IOnPositionChange() {
            @Override
            public void onPositionChange(int pLength, int pPosition) {
                mSeekBar.setMax(pLength);
                mSeekBar.setProgress(pPosition);
            }
        });
        mSeekBar.setVisibility(View.INVISIBLE);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar pSeekBar, int i, boolean b) {
                mVoiceRecorder.seekTo(i);
            }
            @Override
            public void onStartTrackingTouch(SeekBar pSeekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar pSeekBar) {}
        });
        return view;
    }

    View.OnClickListener mClickRecordingListener = new View.OnClickListener() {
        public void onClick(View v) {

            if (!mVoiceRecorder.isRecording()) {
                mButtonRecord.setText(R.string.audio_notes_stop_recording);
            } else {
                mButtonRecord.setText(R.string.audio_notes_start_recording);
            }
            mVoiceRecorder.onRecord(mVoiceRecorder.isRecording());
        }
    };

    View.OnClickListener mClickPlayListener = new View.OnClickListener() {
        public void onClick(View v) {

            mVoiceRecorder.onPlay(mVoiceRecorder.isPlaying());
            if (!mVoiceRecorder.isPlaying()) {
                mButtonPlay.setText(R.string.audio_notes_start_play);
                mSeekBar.setVisibility(View.INVISIBLE);
            } else {
                mSeekBar.setVisibility(View.VISIBLE);
                mButtonPlay.setText(R.string.audio_notes_stop_play);
            }

        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
