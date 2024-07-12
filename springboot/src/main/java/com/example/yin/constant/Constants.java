package com.example.yin.constant;

public class Constants {
    /* 歌曲图片，歌手图片，歌曲文件，歌单图片等文件的存放路径 */
    //System.getProperty("user.dir");返回的是执行运行Java程序命令的路径D:\IntelliJ IDEA 2021.2.1\Project\music\music-server
    public static String ASSETS_PATH = System.getProperty("user.dir");

    public static String AVATOR_IMAGES_PATH = "file:" + ASSETS_PATH + "/img/avatorImages/";
    public static String SONGLIST_PIC_PATH = "file:" + ASSETS_PATH + "/img/songListPic/";
    public static String SONG_PIC_PATH = "file:" + ASSETS_PATH + "/img/songPic/";
    public static String SONG_PATH = "file:" + ASSETS_PATH + "/song/";
    public static String SINGER_PIC_PATH = "file:" + ASSETS_PATH + "/img/singerPic/";
    public static String BANNER_PIC_PATH = "file:" + ASSETS_PATH + "/img/swiper/";


    /* 盐值加密 */
    public static String SALT = "zyt";
}
