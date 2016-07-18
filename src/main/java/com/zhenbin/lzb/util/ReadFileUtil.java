package com.zhenbin.lzb.util;

import java.io.*;

/**
 * Created by zhenbin.lzb on 2016/7/13.
 */
public class ReadFileUtil {

    public static String readAsClassPath(String configPath, String charSet) {
        InputStream in = ReadFileUtil.class.getClassLoader().getResourceAsStream(configPath);
        return stream2String(in, charSet);
    }

    //��ȡjson�ļ������س�һ��String�ַ���
    public static String readAsFilePath(String configPath, String charSet) {
        File configFile = new File(configPath);
        String result = null;
        try {
            result = stream2String(new FileInputStream(configFile), charSet);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * �ļ�ת��Ϊ�ַ���
     *
     * @param in      �ֽ���
     * @param charset �ļ����ַ���
     * @return �ļ�����
     */
    private static String stream2String(InputStream in, String charset) {
        StringBuffer sb = new StringBuffer();
        try {
            Reader r = new InputStreamReader(in, charset);
            int length = 0;
            for (char[] c = new char[1024]; (length = r.read(c)) != -1; ) {
                sb.append(c, 0, length);
            }
            r.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
