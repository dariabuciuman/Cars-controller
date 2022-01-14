import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Car implements Runnable {
    private Image left;
    private Image right;
    private Image up;
    private Image down;
    private Random random;
    private Image display;
    private double xVelocity = 0;
    private double yVelocity = 0;
    private int xCoords;
    private int yCoords;
    private MyPanel mainPanel;
    private int width;
    private int height;
    private boolean threadStarted = false;
    private boolean isSelected = false;

    Car(MyPanel mainPanel) {
        initImages();
        this.mainPanel = mainPanel;
        random = new Random();
        xCoords = random.nextInt(836);
        yCoords = random.nextInt(480);
        switch (random.nextInt(4)) {
            case 1 -> {
                display = right;
                width = 150;
                height = 150;
                xVelocity = 1;
                yVelocity = 0;
            }
            case 2 -> {
                display = up;
                width = 60;
                height = 120;
                xVelocity = 0;
                yVelocity = -1;
            }
            case 3 -> {
                display = down;
                width = 60;
                height = 120;
                xVelocity = 0;
                yVelocity = 1;
            }
            default -> {
                display = left;
                width = 150;
                height = 150;
                xVelocity = -1;
                yVelocity = 0;
            }
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setDisplay(int image) {
        switch (image) {
            case 0 -> this.display = left;
            case 1 -> this.display = up;
            case 2 -> this.display = right;
            default -> this.display = down;
        }
    }

    private void initImages() {
        try {
            String path = new File("").getAbsolutePath();
            left = ImageIO.read(new File(path + "\\assets\\carLeft.png"));
            left = left.getScaledInstance(150, 150, Image.SCALE_DEFAULT);
            right = ImageIO.read(new File(path + "\\assets\\carRight.png"));
            right = right.getScaledInstance(150, 150, Image.SCALE_DEFAULT);
            up = ImageIO.read(new File(path + "\\assets\\carUp.png"));
            up = up.getScaledInstance(60, 120, Image.SCALE_DEFAULT);
            down = ImageIO.read(new File(path + "\\assets\\carDown.png"));
            down = down.getScaledInstance(60, 120, Image.SCALE_DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkClick(int xClick, int yClick) {
        isSelected = (xClick > xCoords && xClick < (xCoords + width)) && (yClick > yCoords && yClick < (yCoords + height));
    }

    public void draw(Graphics g) {
        g.drawImage(display, xCoords, yCoords, null);
        if (isSelected) {
            g.setColor(Color.RED);
            if (width == 60 && height == 120) {
                g.drawRect(xCoords - 45, yCoords - 15, 150, 150);
            } else g.drawRect(xCoords, yCoords, 150, 150);
        }
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public void move() {
        /* TO DO
         * integrate given velocity
         * left  : x = -1, y = 0
         * right : x = 1, y = 0
         * up    : x = 0, y = -1
         * down  : x = 0, y = 1
         */

        if (xVelocity == -1) {
            width = 150;
            height = 150;
            if (xCoords < 0) {
                this.xVelocity = 1;
                this.yVelocity = 0;
                display = right;
            }
        } else if (xVelocity == 1) {
            width = 150;
            height = 150;
            if (xCoords + width > mainPanel.getWidth()) {
                this.xVelocity = -1;
                this.yVelocity = 0;
                display = left;
            }
        } else if (yVelocity == -1) {
            width = 60;
            height = 120;
            if (yCoords < 0) {
                this.xVelocity = 0;
                this.yVelocity = 1;
                display = down;
            }
        } else if (yVelocity == 1) {
            width = 60;
            height = 120;
            if (yCoords + height > mainPanel.getHeight()) {
                this.xVelocity = 0;
                this.yVelocity = -1;
                display = up;
            }
        }
        xCoords += this.xVelocity;
        yCoords += this.yVelocity;
    }

    @Override
    public void run() {
        threadStarted = true;
        System.out.println("running...");
        while (threadStarted) {
            mainPanel.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.move();
        }
    }

    public void stop() {
        threadStarted = false;
    }
}
