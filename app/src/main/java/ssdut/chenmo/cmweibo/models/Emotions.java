package ssdut.chenmo.cmweibo.models;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by chenmo on 2017/1/2.
 */

public class Emotions {
    //public ArrayList<Emotion> emotions = new ArrayList<>();
    @Nullable
    public static ArrayList<Emotion> parse(String jsonString) {

        ArrayList<Emotion> emotions = new ArrayList<>();
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            if(jsonArray != null && jsonArray.length()>0) {
                for(int i = 0;i<jsonArray.length();i++) {
                    if(jsonArray.getJSONObject(i).optString("common").equals("true"))
                        emotions.add(Emotion.parse(jsonArray.getJSONObject(i)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return emotions;
    }

}
