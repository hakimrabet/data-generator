import nu.pattern.OpenCV;
import org.apache.commons.text.RandomStringGenerator;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Main {

    private static final int MAX = 16;
    private static final int MIN = 10;

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        OpenCV.loadLocally();

        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .selectFrom('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ').build();
        List<String> randomNumberList = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            int r = random.nextInt(MAX - MIN + 1) + MIN;
            randomNumberList.add(generator.generate(r));
        }

        int c = 0;
        for (String line : randomNumberList) {
            String name = UUID.randomUUID().toString();
            c++;
            BufferedImage bufferedImage = new BufferedImage(320, 35, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = bufferedImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, 400, 50);
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Verdana", Font.PLAIN, 30));
            graphics.drawString(line, 10, 25);
            ImageIO.write(bufferedImage, "tif", new File("E://train//bnk-ground-truth//" + name + ".tif"));
            Files.write(Paths.get("E://train//bnk-ground-truth//" + name + ".gt.txt"), line.trim().replace(" ", "").getBytes());
            if (c < randomNumberList.size() / 2) {
                Mat src = Imgcodecs.imread("E://train//bnk-ground-truth//" + name + ".tif");
                Mat dst = new Mat();
                Imgproc.GaussianBlur(src, dst, new Size(3, 3), 0);
                Imgcodecs.imwrite("E://train//bnk-ground-truth//" + name + ".tif", dst);
            }
            System.out.println(c + "/" + randomNumberList.size());
        }

    }
}
