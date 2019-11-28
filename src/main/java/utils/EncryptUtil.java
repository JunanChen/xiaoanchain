package utils;


import java.security.*;
import java.util.Base64;

/**
 * @author junan
 * @version V1.0
 * @className EncryptUtil
 * @disc
 * @date 2019/9/16 14:50
 */
public class EncryptUtil {

    /**
     * 传入字符串，返回 SHA-256 加密字符串
     *
     * @param strText
     * @return
     */
    public static String getSHA256(final String strText) {
        return SHA(strText, "SHA-256");
    }

    /**
     * 传入字符串，返回 SHA-512 加密字符串
     *
     * @param strText
     * @return
     */
    public static String getSHA512(final String strText) {
        return SHA(strText, "SHA-512");
    }

    /**
     * 传入字符串，返回 MD5 加密字符串
     *
     * @param strText
     * @return
     */
    public String getMD5(final String strText) {
        return SHA(strText, "md5");
    }

    /**
     * 字符串 SHA 加密
     *
     * @param
     * @return
     */
    private static String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象，传入加密类型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 数组
                byte byteBuffer[] = messageDigest.digest();

                // 將 byte 数组转换 string 类型
                StringBuffer strHexString = new StringBuffer();
                // 遍历 byte 数组
                for (int i = 0; i < byteBuffer.length; i++) {
                    // 转换成16进制并存储在字符串中
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    //应用ECDSA签名并产生字符数组
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    //应用ECDSA验证数字签名
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}