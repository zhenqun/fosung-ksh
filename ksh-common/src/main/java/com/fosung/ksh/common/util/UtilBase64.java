package com.fosung.ksh.common.util;

import com.fosung.framework.common.util.UtilString;
import com.mzlion.easyokhttp.HttpClient;

import java.io.*;
import java.util.Base64;

/**
 * @author wangyihua
 * @date 2019-05-09 15:02
 */
public class UtilBase64 {


    /**
     * 本地图片转换成base64字符串
     *
     * @param imgFile 图片本地路径
     * @return
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:40:46
     */
    public static String ImageToBase64ByLocal(String imgFile) throws IOException {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理


        InputStream in = null;
        byte[] data = null;

        // 读取图片字节数组
        in = new FileInputStream(imgFile);

        data = new byte[in.available()];
        in.read(data);
        in.close();

        // 对字节数组Base64编码
        return Base64.getEncoder().encodeToString(data);// 返回Base64编码过的字节数组字符串
    }


    /**
     * 在线图片转换成base64字符串
     *
     * @param imgURL 图片线上路径
     * @return
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:43:18
     */
    public static String ImageToBase64ByOnline(String imgURL) {
        byte[] bytes = HttpClient.get(imgURL).asByteData();

        // 对字节数组Base64编码
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * base64字符串转换成图片
     *
     * @param imgStr      base64字符串
     * @param imgFilePath 图片存放路径
     * @return
     * @author ZHANGJL
     * @dateTime 2018-02-23 14:42:17
     */
    public static boolean Base64ToImage(String imgStr, String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片

        if (UtilString.isEmpty(imgStr)) // 图像数据为空
            return false;

        try {
            // Base64解码
            byte[] b = Base64.getDecoder().decode(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }

            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();

            return true;
        } catch (Exception e) {
            return false;
        }

    }


}
