package com.example.alohaandroid.utils.linphone;

/*
LinphoneShortcutManager.java
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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.util.ArraySet;

import com.example.alohaandroid.R;
import com.example.alohaandroid.ui.ad_main.MainActivity;

import org.linphone.core.tools.Log;

import java.util.Set;

@TargetApi(25)
public class LinphoneShortcutManager {
    private Context mContext;
    private Set<String> mCategories;

    public LinphoneShortcutManager(Context context) {
        mContext = context;
        mCategories = new ArraySet<>();
        mCategories.add(ShortcutInfo.SHORTCUT_CATEGORY_CONVERSATION);
    }

    public void destroy() {
        mContext = null;
    }

    public ShortcutInfo createChatRoomShortcutInfo(
            LinphoneContact contact, String chatRoomAddress) {
        if (contact == null) return null;

        Bitmap bm = null;
        if (contact.getThumbnailUri() != null) {
            bm = ImageUtils.getRoundBitmapFromUri(mContext, contact.getThumbnailUri());
        }
        Icon icon =
                bm == null
                        ? Icon.createWithResource(mContext, R.drawable.logo_app)
                        : Icon.createWithBitmap(bm);

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClass(mContext, MainActivity.class);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("RemoteSipUri", chatRoomAddress);

            return new ShortcutInfo.Builder(mContext, chatRoomAddress)
                    .setShortLabel(contact.getFullName())
                    .setIcon(icon)
                    .setCategories(mCategories)
                    .setIntent(intent)
                    .build();
        } catch (Exception e) {
            Log.e("[Shortcuts Manager] ShortcutInfo.Builder exception: " + e);
        }

        return null;
    }
}
