package com.example.alohaandroid.ui.call;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.alohaandroid.R;
import com.example.alohaandroid.ui.linphone.LinphoneGenericActivity;
import com.example.alohaandroid.utils.linphone.AndroidAudioManager;
import com.example.alohaandroid.utils.linphone.Compatibility;
import com.example.alohaandroid.utils.linphone.ContactAvatar;
import com.example.alohaandroid.utils.linphone.ContactsManager;
import com.example.alohaandroid.utils.linphone.ContactsUpdatedListener;
import com.example.alohaandroid.utils.linphone.LinphoneContact;
import com.example.alohaandroid.utils.linphone.LinphoneManager;
import com.example.alohaandroid.utils.linphone.LinphonePreferences;
import com.example.alohaandroid.utils.linphone.LinphoneService;
import com.example.alohaandroid.utils.linphone.LinphoneUtils;
import com.example.alohaandroid.utils.linphone.VideoZoomHelper;

import org.linphone.core.Call;
import org.linphone.core.ChatMessage;
import org.linphone.core.ChatRoom;
import org.linphone.core.Core;
import org.linphone.core.CoreListener;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.tools.Log;

public class CallActivity extends LinphoneGenericActivity implements ContactsUpdatedListener, View.OnClickListener {

    private static final int SECONDS_BEFORE_HIDING_CONTROLS = 4000;
    private static final int SECONDS_BEFORE_DENYING_CALL_UPDATE = 30000;

    private static final int CAMERA_TO_TOGGLE_VIDEO = 0;
    private static final int MIC_TO_DISABLE_MUTE = 1;
    private static final int WRITE_EXTERNAL_STORAGE_FOR_RECORDING = 2;
    private static final int CAMERA_TO_ACCEPT_UPDATE = 3;

    CheckBox cbDial, cbMic, cbLoud;
    TextView tvDial, mContactName, tvMic, tvLoud, tvNumberCallOutGoing;
    ImageView ivDeny;

    private Core mCore;
    private CoreListener mListener;
    private Chronometer mCallTimer;
    private CountDownTimer mCallUpdateCountDownTimer;
    private AndroidAudioManager mAudioManager;
    private VideoZoomHelper mZoomHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mAbortCreation) {
            return;
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Compatibility.setShowWhenLocked(this, true);

        setContentView(R.layout.activity_call);

        cbDial = findViewById(R.id.cbDial);
        cbMic = findViewById(R.id.cbMic);
        cbLoud = findViewById(R.id.cbLoud);
        tvDial = findViewById(R.id.tvDial);
        tvMic = findViewById(R.id.tvMic);
        tvLoud = findViewById(R.id.tvLoud);
        ivDeny = findViewById(R.id.ivDeny);

        mContactName = findViewById(R.id.tvNameCallOutGoing);
        tvNumberCallOutGoing = findViewById(R.id.tvNumberCallOutGoing);
        mCallTimer = findViewById(R.id.current_call_timer);
        cbLoud.setOnClickListener(this);
        cbMic.setOnClickListener(this);
        cbDial.setOnClickListener(this);
        ivDeny.setOnClickListener(this);

        cbDial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbDial.isChecked()) {
                    cbDial.setBackgroundResource(R.drawable.ic_dial_click);
                    tvDial.setTextColor(getResources().getColor(R.color.blue_light));
                } else {
                    cbDial.setBackgroundResource(R.drawable.ic_dial_not_click);
                    tvDial.setTextColor(getResources().getColor(R.color.gray_60));
                }
            }
        });

        cbMic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbMic.isChecked()) {
                    cbMic.setBackgroundResource(R.drawable.ic_mix_click);
                    tvMic.setTextColor(getResources().getColor(R.color.blue_light));
                } else {
                    cbMic.setBackgroundResource(R.drawable.ic_mix_not_click);
                    tvMic.setTextColor(getResources().getColor(R.color.gray_60));
                }
            }
        });

        cbLoud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!cbLoud.isChecked()) {
                    cbLoud.setBackgroundResource(R.drawable.ic_loud_click);
                    tvLoud.setTextColor(getResources().getColor(R.color.blue_light));
                } else {
                    cbLoud.setBackgroundResource(R.drawable.ic_loud_not_click);
                    tvLoud.setTextColor(getResources().getColor(R.color.gray_60));

                }
            }
        });

        mListener =
                new CoreListenerStub() {
                    @Override
                    public void onMessageReceived(Core core, ChatRoom cr, ChatMessage message) {
                    }

                    @Override
                    public void onCallStateChanged(
                            Core core, Call call, Call.State state, String message) {
                        if (state == Call.State.End || state == Call.State.Released) {
                            if (core.getCallsNb() == 0) {
                                finish();
                            }
                        } else if (state == Call.State.PausedByRemote) {
                            if (core.getCurrentCall() != null) {

                            }
                        } else if (state == Call.State.Pausing || state == Call.State.Paused) {
                            if (core.getCurrentCall() != null) {
                            }
                        } else if (state == Call.State.StreamsRunning) {

                            setCurrentCallContactInformation();
                        } else if (state == Call.State.UpdatedByRemote) {
                            // If the correspondent asks for video while in audio call
                            boolean videoEnabled = LinphonePreferences.instance().isVideoEnabled();
                            if (!videoEnabled) {
                                // Video is disabled globally, don't even ask user
                                acceptCallUpdate(false);
                                return;
                            }

                        }
                        updateCallsList();
                    }
                };
    }


    @Override
    protected void onStart() {
        super.onStart();

        mCore = LinphoneManager.getCore();
        if (mCore != null) {

            mCore.addListener(mListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAudioManager = LinphoneManager.getAudioManager();

        updateCallsList();
        ContactsManager.getInstance().addContactsListener(this);

        if (mCore.getCallsNb() == 0) {
            Log.w("[Call Activity] Resuming but no call found...");
            finish();
        }

        LinphoneService.instance().destroyOverlay();

        if (checkAndRequestPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE_FOR_RECORDING)) {
            toggleRecording();
        }
    }

    @Override
    protected void onPause() {
        ContactsManager.getInstance().removeContactsListener(this);
        LinphoneManager.getCallManager().setCallInterface(null);

        Core core = LinphoneManager.getCore();
        if (LinphonePreferences.instance().isOverlayEnabled()
                && core != null
                && core.getCurrentCall() != null) {
            Call call = core.getCurrentCall();
            if (call.getState() == Call.State.StreamsRunning) {
                // Prevent overlay creation if video call is paused by remote
                LinphoneService.instance().createOverlay();
            }
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Core core = LinphoneManager.getCore();
        if (core != null) {
            core.removeListener(mListener);
            core.setNativeVideoWindowId(null);
            core.setNativePreviewWindowId(null);
        }

        if (mZoomHelper != null) {
            mZoomHelper.destroy();
            mZoomHelper = null;
        }
        if (mCallUpdateCountDownTimer != null) {
            mCallUpdateCountDownTimer.cancel();
            mCallUpdateCountDownTimer = null;
        }

        mCallTimer.stop();
        mCallTimer = null;

        mListener = null;


        mContactName = null;


        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAudioManager.onKeyVolumeAdjust(keyCode)) return true;
        return super.onKeyDown(keyCode, event);
    }


    private void acceptCallUpdate(boolean accept) {
        if (mCallUpdateCountDownTimer != null) {
            mCallUpdateCountDownTimer.cancel();
        }
        LinphoneManager.getCallManager().acceptCallUpdate(accept);
    }

    private void toggleMic() {
        mCore.enableMic(!mCore.micEnabled());
        cbMic.setSelected(!mCore.micEnabled());
    }

    private void toggleSpeaker() {
        if (mAudioManager.isAudioRoutedToSpeaker()) {
            mAudioManager.routeAudioToEarPiece();
        } else {
            mAudioManager.routeAudioToSpeaker();
        }
        cbLoud.setSelected(mAudioManager.isAudioRoutedToSpeaker());
    }

    private void updateCallsList() {
        Call currentCall = mCore.getCurrentCall();
        if (currentCall != null) {
            setCurrentCallContactInformation();
        }

    }

    private void toggleRecording() {
        Call call = mCore.getCurrentCall();
        if (call == null) return;

        if (call.isRecording()) {
            call.stopRecording();
        } else {
            call.startRecording();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Permission not granted, won't change anything
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) return;

        switch (requestCode) {
            case CAMERA_TO_TOGGLE_VIDEO:
                LinphoneUtils.reloadVideoDevices();
                break;
            case MIC_TO_DISABLE_MUTE:
                toggleMic();
                break;
            case WRITE_EXTERNAL_STORAGE_FOR_RECORDING:
                toggleRecording();
                break;
            case CAMERA_TO_ACCEPT_UPDATE:
                LinphoneUtils.reloadVideoDevices();
                acceptCallUpdate(true);
                break;
        }
    }

    private boolean checkPermission(String permission) {
        int granted = getPackageManager().checkPermission(permission, getPackageName());
        Log.i(
                "[Permission] "
                        + permission
                        + " permission is "
                        + (granted == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));
        return granted == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkAndRequestPermission(String permission, int result) {
        if (!checkPermission(permission)) {
            Log.i("[Permission] Asking for " + permission);
            ActivityCompat.requestPermissions(this, new String[]{permission}, result);
            return false;
        }
        return true;
    }

    private void updateCurrentCallTimer() {
        Call call = mCore.getCurrentCall();
        if (call == null) return;

        mCallTimer.setBase(SystemClock.elapsedRealtime() - 1000 * call.getDuration());
        mCallTimer.start();
    }

    private void setCurrentCallContactInformation() {
        updateCurrentCallTimer();

        Call call = mCore.getCurrentCall();
        if (call == null) return;

        LinphoneContact contact =
                ContactsManager.getInstance().findContactFromAddress(call.getRemoteAddress());
        if (contact != null) {
            ContactAvatar.displayAvatar(contact, findViewById(R.id.contact_avatar));
            mContactName.setText(contact.getFullName());
        } else {
            String displayName = LinphoneUtils.getAddressDisplayName(call.getRemoteAddress());
            ContactAvatar.displayAvatar(displayName, findViewById(R.id.contact_avatar));
            mContactName.setText(displayName);
        }
        tvNumberCallOutGoing.setText(LinphoneUtils.getDisplayableAddress(call.getRemoteAddress()));
    }

    @Override
    public void onContactsUpdated() {
        setCurrentCallContactInformation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cbMic:
                if (checkAndRequestPermission(Manifest.permission.RECORD_AUDIO, MIC_TO_DISABLE_MUTE)) {
                    toggleMic();
                }
                break;
            case R.id.cbDial:

                break;
            case R.id.cbLoud:
                toggleSpeaker();
                break;
            case  R.id.ivDeny:
                LinphoneManager.getCallManager().terminateCurrentCallOrConferenceOrAll();
                finish();
                break;
        }
    }
}
