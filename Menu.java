import java.awt.*;

public class Menu {

    public static void drawMenu(Graphics offScreenGraphics, int width, int height) {
        Color backColor = new Color(30, 30, 30);
        offScreenGraphics.setColor(backColor);
        offScreenGraphics.fillRect(0, 0, 1000, 700);
        Font f1 = new Font("Arial", Font.PLAIN, 50);
        FontMetrics f1Metrics = offScreenGraphics.getFontMetrics(f1);
        Font f2 = new Font("Arial", Font.PLAIN, 30);
        FontMetrics f2Metrics = offScreenGraphics.getFontMetrics(f2);
        offScreenGraphics.setColor(Color.WHITE);
        offScreenGraphics.setFont(f1);
        offScreenGraphics.drawString("Main Menu", width / 2 - f1Metrics.stringWidth("Main Menu") / 2, height / 2 - f1Metrics.getHeight() / 2);
        offScreenGraphics.fillRect(width / 2 - 65, height / 2 + 25, 133, 50);
        offScreenGraphics.fillRect(width / 2 - 65, height / 2 + 85, 133, 50);
        offScreenGraphics.setFont(f2);
        offScreenGraphics.setColor(backColor);
        offScreenGraphics.drawString("1 Player", width / 2 - (f2Metrics.stringWidth("1 Player") / 2), height / 2 - f2Metrics.getHeight() / 2 + 80);
        offScreenGraphics.drawString("2 Players", width / 2 - (f2Metrics.stringWidth("2 Players") / 2), height / 2 - f2Metrics.getHeight() / 2 + 140);
    }

}
