package org.zenfile.utils;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具类
 */
public class StringUtils {

    public static final char DELIMITER = '/';

    public static final String DELIMITER_STR = "/";

    public static final String HTTP_PROTOCOL = "http://";

    public static final String HTTPS_PROTOCOL = "https://";


    /**
     * 拼接出入过来的URL字符串数组，并返回除去重复'/'的URL结果
     */
    public static String concat(String... strs){
        StringBuilder sb = new StringBuilder(DELIMITER_STR);
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            if(StrUtil.isEmpty(str)){
                continue;
            }
            sb.append(str);
            if(i != strs.length - 1){
                sb.append(DELIMITER);
            }
        }

        return removeDuplicateSlashes(sb.toString());
    }

    /**
     * 去除路径中重复的 '/', 注意路径的最后一个 '/'不会被除去
     * @param path 路径
     */
    public static String removeDuplicateSlashes(String path) {
        if(StrUtil.isEmpty(path)){
            return path;
        }
        StringBuilder sb = new StringBuilder();

        boolean containProtocol = StrUtil.containsAnyIgnoreCase(path, HTTPS_PROTOCOL, HTTP_PROTOCOL);

        if(containProtocol){
            path = trimStartSlashes(path);
        }

        boolean httpsProtocol = StrUtil.startWithIgnoreCase(path, HTTPS_PROTOCOL);
        boolean httpProtocol = StrUtil.startWithIgnoreCase(path, HTTP_PROTOCOL);

        if(httpProtocol){
            sb.append(HTTP_PROTOCOL);
        } else if (httpsProtocol) {
            sb.append(HTTPS_PROTOCOL);
        }

        for (int i = sb.length(); i < path.length() - 1; i++){
            char current = path.charAt(i);
            char next = path.charAt(i + 1);
            if(!(current == DELIMITER && next == DELIMITER)){
                sb.append(current);
            }
        }
        sb.append(path.charAt(path.length() - 1));
        return sb.toString();
    }

    /**
     * 删除URL开头的 '/'
     * @param path URL
     * @return 不以 '/'开头的URL
     */
    public static String trimStartSlashes(String path) {
        if(StrUtil.isEmpty(path)){
            return  path;
        }

        // 用于处理开头有多个/
        while (path.startsWith(DELIMITER_STR)){
            path = path.substring(1);
        }

        return path;
    }

    /**
     * 获取指定路径的父路径， 如果最后为 / ，则最后也返回 /
     * @param path 路径
     * @return 上一级父级目录
     */
    public static String getParentPath(String path){
        int index = StrUtil.lastIndexOfIgnoreCase(path, ZenFileConstant.PATH_SEPARATOR);
        if(index <= 0){
            return "/";
        }else{
            return StrUtil.sub(path, 0, index);
        }
    }

    /**
     * 将一个路径+名称 分离开，返回一个长度为2的数组
     * /abc/abc.jpg [/abc][abc.jpg]
     * 如果路径为/abc 返回的是 [/][abc]
     * 如果路径为 / 返回的是[/][]
     * @param pathAndName 路径 + 名称
     */
    public static String[] separatePathAndName(String pathAndName){
        int index = StrUtil.lastIndexOfIgnoreCase(pathAndName, ZenFileConstant.PATH_SEPARATOR);
        String[] strings = new String[2];
        if(index <= 0){
            strings[0] = "/";
            if(pathAndName.length() > 1){
                strings[1] = StrUtil.sub(pathAndName, index + 1, pathAndName.length());
            }
        }else{
            strings[0] = StrUtil.sub(pathAndName, 0, index);
            strings[1] = StrUtil.sub(pathAndName, index + 1, pathAndName.length());
        }
        return strings;
    }
}
