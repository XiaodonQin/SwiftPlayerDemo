package sg.com.conversant.swiftplayer.rxjava.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Xiaodong on 2017/7/28.
 */

public class HttpResult<T> implements Serializable {

    @SerializedName("start")
    private int start;

    @SerializedName("count")
    private int count;

    @SerializedName("total")
    private int total;

    @SerializedName("title")
    private String title;

    @SerializedName("subjects")
    private T data;


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
