package com.example.alohaandroid.ui.ae_chat.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.alohaandroid.ui.ae_chat.models.IMessage;
import com.example.alohaandroid.ui.ae_chat.models.MessageContentType;

import java.util.Date;

/*
 * Created by troy379 on 04.04.17.
 */
public class Message implements IMessage,
        MessageContentType.Image, /*this is for default image messages implementation*/
        MessageContentType/*and this one is for custom content type (in this case - voice message)*/ {

    private String id;
    private String text;
    private Date createdAt;
    private User user;
    private Image image;
    private Voice voice;

    public Message(String id, User user, String text) {
        this(id, user, text, new Date());
    }

    public Message(String id, User user, String text, Date createdAt) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.createdAt = createdAt;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public String getImageUrl() {
        return image == null ? null : image.url;
    }

    public Voice getVoice() {
        return voice;
    }

    public String getStatus() {
        return "Sent";
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public static class Image {

        private String url;

        public Image(String url) {
            this.url = url;
        }
    }

    public static class Voice implements Parcelable {
        private int mId;
        private String url;
        private long duration;

        public Voice() {
        }

        public Voice(int mId, String url, long duration) {
            this.mId = mId;
            this.url = url;
            this.duration = duration;
        }

        protected Voice(Parcel in) {
            mId = in.readInt();
            url = in.readString();
            duration = in.readLong();
        }

        public static final Creator<Voice> CREATOR = new Creator<Voice>() {
            @Override
            public Voice createFromParcel(Parcel in) {
                return new Voice(in);
            }

            @Override
            public Voice[] newArray(int size) {
                return new Voice[size];
            }
        };

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            mId = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            url = url;
        }

        public long getDuration() {
            return duration;
        }
        public void setDuration(long duration) {
            duration = duration;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mId);
            dest.writeString(url);
            dest.writeLong(duration);
        }
    }
}
