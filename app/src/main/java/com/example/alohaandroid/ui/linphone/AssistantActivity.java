package com.example.alohaandroid.ui.linphone;

/*
AssistantActivity.java
Copyright (C) 2019 Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import android.widget.EditText;
import android.widget.ImageView;

import com.example.alohaandroid.R;
import com.example.alohaandroid.ui.ad_main.MainActivity;
import com.example.alohaandroid.utils.linphone.LinphoneManager;
import com.example.alohaandroid.utils.linphone.LinphonePreferences;
import com.example.alohaandroid.utils.linphone.LinphoneService;


import org.linphone.core.AccountCreator;
import org.linphone.core.Core;
import org.linphone.core.DialPlan;
import org.linphone.core.Factory;
import org.linphone.core.ProxyConfig;
import org.linphone.core.tools.Log;

public abstract class AssistantActivity extends LinphoneGenericActivity {
    static AccountCreator mAccountCreator;

    protected ImageView mBack;
    private AlertDialog mCountryPickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mAbortCreation) {
            return;
        }

        if (mAccountCreator == null) {
            String url = LinphonePreferences.instance().getXmlrpcUrl();
            Core core = LinphoneManager.getCore();
            core.loadConfigFromXml(LinphonePreferences.instance().getDefaultDynamicConfigFile());
            mAccountCreator = core.createAccountCreator(url);
        }
    }



    void createProxyConfigAndLeaveAssistant() {
        Core core = LinphoneManager.getCore();
        boolean useLinphoneDefaultValues =
                getString(R.string.default_domain).equals(mAccountCreator.getDomain());
        if (useLinphoneDefaultValues) {
            core.loadConfigFromXml(LinphonePreferences.instance().getLinphoneDynamicConfigFile());
        }

        ProxyConfig proxyConfig = mAccountCreator.createProxyConfig();

        if (useLinphoneDefaultValues) {
            // Restore default values
            core.loadConfigFromXml(LinphonePreferences.instance().getDefaultDynamicConfigFile());
        } else {
            // If this isn't a sip.linphone.org account, disable push notifications and enable
            // service notification, otherwise incoming calls won't work (most probably)
            LinphonePreferences.instance().setServiceNotificationVisibility(true);
            LinphoneService.instance().getNotificationManager().startForeground();
        }

        if (proxyConfig == null) {
            Log.e("[Assistant] Account creator couldn't create proxy config");
            // TODO: display error message
        } else {
            if (proxyConfig.getDialPrefix() == null) {
                DialPlan dialPlan = getDialPlanForCurrentCountry();
                if (dialPlan != null) {
                    proxyConfig.setDialPrefix(dialPlan.getCountryCallingCode());
                }
            }

            LinphonePreferences.instance().firstLaunchSuccessful();
            goToLinphoneActivity();
        }
    }

    void goToLinphoneActivity() {
        boolean needsEchoCalibration =
                LinphoneManager.getCore().isEchoCancellerCalibrationRequired();
        boolean echoCalibrationDone =
                LinphonePreferences.instance().isEchoCancellationCalibrationDone();
        Log.i(
                "[Assistant] Echo cancellation calibration required ? "
                        + needsEchoCalibration
                        + ", already done ? "
                        + echoCalibrationDone);

        Intent intent;
            /*boolean openH264 = LinphonePreferences.instance().isOpenH264CodecDownloadEnabled();
            boolean codecFound =
                    LinphoneManager.getInstance().getOpenH264DownloadHelper().isCodecFound();
            boolean abiSupported =
                    Version.getCpuAbis().contains("armeabi-v7a")
                            && !Version.getCpuAbis().contains("x86");
            boolean androidVersionOk = Version.sdkStrictlyBelow(Build.VERSION_CODES.M);

            if (openH264 && abiSupported && androidVersionOk && !codecFound) {
                intent = new Intent(this, OpenH264DownloadAssistantActivity.class);
            } else {*/
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
    }



    DialPlan getDialPlanForCurrentCountry() {
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String countryIso = tm.getNetworkCountryIso();
            return getDialPlanFromCountryCode(countryIso);
        } catch (Exception e) {
            Log.e("[Assistant] " + e);
        }
        return null;
    }

    String getDevicePhoneNumber() {
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            return tm.getLine1Number();
        } catch (Exception e) {
            Log.e("[Assistant] " + e);
        }
        return null;
    }

    DialPlan getDialPlanFromPrefix(String prefix) {
        if (prefix == null || prefix.isEmpty()) return null;

        for (DialPlan c : Factory.instance().getDialPlans()) {
            if (prefix.equalsIgnoreCase(c.getCountryCallingCode())) return c;
        }
        return null;
    }

    private DialPlan getDialPlanFromCountryCode(String countryCode) {
        if (countryCode == null || countryCode.isEmpty()) return null;

        for (DialPlan c : Factory.instance().getDialPlans()) {
            if (countryCode.equalsIgnoreCase(c.getIsoCountryCode())) return c;
        }
        return null;
    }

    int arePhoneNumberAndPrefixOk(EditText prefixEditText, EditText phoneNumberEditText) {
        String prefix = prefixEditText.getText().toString();
        if (prefix.startsWith("+")) {
            prefix = prefix.substring(1);
        }

        String phoneNumber = phoneNumberEditText.getText().toString();
        return mAccountCreator.setPhoneNumber(phoneNumber, prefix);
    }

}
