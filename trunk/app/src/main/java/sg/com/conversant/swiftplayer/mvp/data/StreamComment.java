package sg.com.conversant.swiftplayer.mvp.data;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Copyright (c) 2012 Conversant Solutions. All rights reserved.
 * <p>
 * Created on 2017/11/12.
 */
@Entity (tableName = "stream_comment")
public class StreamComment implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("comment")
    private String comment;

    @SerializedName("device_model")
    private String deviceModel;

    @SerializedName("favorite_count")
    private int favoriteCount;

    @SerializedName("favorite")
    private int favorite;

    @SerializedName("stream_id")
    private long streamId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public long getStreamId() {
        return streamId;
    }

    public void setStreamId(long streamId) {
        this.streamId = streamId;
    }
}
