package ru.netology.graphics;

import ru.netology.graphics.image.TextColorW;
import ru.netology.graphics.image.TextGraphicsConverter;
import ru.netology.graphics.image.TextGraphicsW;
import ru.netology.graphics.server.GServer;

import java.io.File;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        //TextGraphicsConverter converter = null; // Создайте тут объект вашего класса конвертера

        TextGraphicsConverter converter = new TextGraphicsW();
        //converter.setMaxRatio(4);              // максимальное соотношение сторон
        //converter.setMaxWidth(300);
        // максимальная ширина итогового изображения
        //converter.setMaxHeight(300);            // максимальная высота итогового изображения
        converter.setTextColorSchema(new TextColorW()); // можно заменить на свою реализацию

        String url = "https://raw.githubusercontent.com/netology-code/java-diplom/main/pics/simple-test.png";
        String imgTxt = converter.convert(url);
        System.out.println(imgTxt);

        GServer server = new GServer(converter); // Создаём объект сервера
        server.start(); // Запускаем

        // Или то же, но с выводом на экран:
        //String url = "https://raw.githubusercontent.com/netology-code/java-diplom/main/pics/simple-test.png";
        //String imgTxt = converter.convert(url);
        //System.out.println(imgTxt);
    }
}
