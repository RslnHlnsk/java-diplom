package ru.netology.graphics.image;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsW implements TextGraphicsConverter {
    private int maxRatio = 0;      // 0 — ограничение не применяется
    private int maxWidth = 0;
    private int maxHeight = 0;
    private TextColorSchema colorSchema = new TextColorW();

    public void setMaxRatio(int maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        try {
            URL urlObj = new URL(url);
            return convert(urlObj);
        } catch (java.net.MalformedURLException e) {
            throw new IOException("Invalid URL: " + url, e);
        }
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = (int) maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema colorSchema) {
        if (colorSchema != null) {
            this.colorSchema = colorSchema;
        }
    }


    public String convert(URL url) throws BadImageSizeException {
        try {
            BufferedImage image = ImageIO.read(url);
            if (image == null) {
                throw new IllegalArgumentException("Could not read image from URL");
            }

            // 1) Проверка соотношения сторон
            if (maxRatio > 0) {
                int w = image.getWidth();
                int h = image.getHeight();
                double ratio = Math.max((double) w / h, (double) h / w);
                if (ratio > maxRatio) {
                    throw new BadImageSizeException(ratio, maxRatio);
                }
            }

            // 2) Уменьшаем до допустимых размеров, если нужно
            int width = image.getWidth();
            int height = image.getHeight();
            int newWidth = width;
            int newHeight = height;

            if (maxWidth > 0 || maxHeight > 0) {
                double scaleW = maxWidth > 0 ? (double) maxWidth / width : 1.0;
                double scaleH = maxHeight > 0 ? (double) maxHeight / height : 1.0;
                double scale = Math.min(scaleW, scaleH);
                if (scale < 1.0) {
                    newWidth = (int) Math.round(width * scale);
                    newHeight = (int) Math.round(height * scale);
                    Image tmp = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = resized.createGraphics();
                    g2d.drawImage(tmp, 0, 0, null);
                    g2d.dispose();
                    image = resized;
                    width = newWidth;
                    height = newHeight;
                }
            }

            // 3) Преобразование к оттенкам серого
            StringBuilder sb = new StringBuilder((width + 1) * height);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                    char ch = colorSchema.convert(gray);
                    sb.append(ch);
                }
                sb.append(System.lineSeparator());
            }
            return sb.toString();

        } catch (BadImageSizeException e) {
            // пробросим дальше как исключение нужного типа
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
