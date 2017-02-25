package ssdut.chenmo.cmweibo.utils;

import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by chenmo on 2016/10/9.
 */

public class UserUtils {

    public static User user = null;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserUtils.user = user;
    }



}
