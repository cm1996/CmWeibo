package ssdut.chenmo.cmweibo.models;

import org.json.JSONObject;

/**
 * Created by chenmo on 2017/1/2.
 */

public class Emotion {
    public String value = null;
    public String url = null;
    public Emotion(String value, String url) {
        this.value = value;
        this.url = url;
    }
    public static Emotion parse(JSONObject jsonObject) {
        return new Emotion(jsonObject.optString("value"),jsonObject.optString("url"));
    }
}
