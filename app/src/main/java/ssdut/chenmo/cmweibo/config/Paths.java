package ssdut.chenmo.cmweibo.config;

import android.os.Environment;

/**
 * Created by chenmo on 2016/9/9.
 */
public class Paths {

    private static String basePath = Environment.getExternalStorageDirectory()+"/CmWeibo";
    //private static String basePath = "/data/CmWeibo";

    //图片缓存
    public static String imagePath = basePath+"/images";
    //数据库缓存
    public static String dbPath = basePath+"/dbs";
    //文档缓存
    public static String downloadPath = basePath+"/docs";
}
