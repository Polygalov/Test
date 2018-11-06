package ua.com.adr.android.myapplication.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Hyperlinks {

    private int id;

    @PrimaryKey
    @NonNull
    private String mUrl;

    private int mStatus;

    private long mTime;

    public Hyperlinks() {
    }

    @Ignore
    public Hyperlinks(String url, int status, long time) {
        this.mUrl = url;
        this.mStatus = status;
        this.mTime = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }
}
