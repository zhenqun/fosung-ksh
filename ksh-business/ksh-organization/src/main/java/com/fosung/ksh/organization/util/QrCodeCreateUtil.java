package com.fosung.ksh.organization.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * @author toquery
 * @version 1
 */
public class QrCodeCreateUtil {


    public static String createQrCode(String content, int size, String imageFormat) {
        String imageBase64 = null;
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ) {
            BitMatrix bm = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, size, size);
            MatrixToImageWriter.writeToStream(bm, imageFormat, baos);
            byte[] bytes = baos.toByteArray();
            imageBase64 = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
        return imageBase64;
    }

    /**
     * 读二维码并输出携带的信息
     */
    public static void readQrCode(InputStream inputStream) throws IOException {
        //从输入流中获取字符串信息
        BufferedImage image = ImageIO.read(inputStream);
        //将图像转换为二进制位图源
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result = null;
        try {
            result = reader.decode(bitmap);
        } catch (ReaderException e) {
            e.printStackTrace();
        }
        System.out.println(result.getText());
    }

    public static void main(String[] args) {
        String imageBase64 = QrCodeCreateUtil.createQrCode("http://www.baidu.com", 300, "jpg");
        System.out.println(imageBase64);
    }

}
