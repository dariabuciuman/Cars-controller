import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MyPanel extends JPanel {
    private ArrayList<Car> cars;

    MyPanel(ArrayList<Car> cars) {
        this.cars = cars;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        cars.forEach(car -> {
            car.draw(g);
        });
    }
}
