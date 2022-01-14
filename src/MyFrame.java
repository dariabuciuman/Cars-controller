import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MyFrame {
    private JFrame frame;
    private MyPanel mainPanel;
    private JPanel downPanel;
    private JButton startButton;
    private JButton stopButton;
    private JButton addButton;
    private JButton deleteButton;
    private ArrayList<Car> cars = new ArrayList<>();
    private int areSelected = 0;
    private int selectedIndex = -1;

    public MyFrame() {
        frame = new JFrame("Cars");
        mainPanel = new MyPanel(cars);
        downPanel = new JPanel();

        frame.setSize(1000, 700);
        mainPanel.setPreferredSize(new Dimension(1000, 600));
        mainPanel.setBackground(Color.WHITE);
        downPanel.setPreferredSize(new Dimension(1000, 65));
        downPanel.setBackground(Color.LIGHT_GRAY);
        frame.add(mainPanel, BorderLayout.NORTH);
        frame.add(downPanel, BorderLayout.SOUTH);

        initButtons();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void initButtons() {
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        startButton.setBackground(Color.GRAY);
        stopButton.setBackground(Color.GRAY);
        addButton.setBackground(Color.GRAY);
        deleteButton.setBackground(Color.GRAY);

        downPanel.setLayout(new BoxLayout(downPanel, BoxLayout.X_AXIS));
        downPanel.add(Box.createHorizontalStrut(20));
        downPanel.add(startButton);
        downPanel.add(Box.createHorizontalStrut(50));
        downPanel.add(stopButton);
        downPanel.add(Box.createHorizontalStrut(50));
        downPanel.add(addButton);
        downPanel.add(Box.createHorizontalStrut(50));
        downPanel.add(deleteButton);

        addMouseListener();
        addKeyListener();
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("adding 1 Car...");
                cars.add(new Car(mainPanel));
                cars.get(cars.size() - 1).draw(mainPanel.getGraphics());
                mainPanel.requestFocus();
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cars.forEach(car -> {
                    Thread thread = new Thread(car);
                    thread.start();
                    mainPanel.requestFocus();
                    //car.run();
                });
            }
        });
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cars.forEach(Car::stop);
                mainPanel.requestFocus();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex > -1) {
                    cars.get(selectedIndex).stop();
                    cars.remove(selectedIndex);
                    mainPanel.repaint();
                    areSelected = 0;
                    selectedIndex = -1;
                }
                mainPanel.requestFocus();
            }
        });
    }

    private void addMouseListener() {
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                cars.forEach(car -> {
                    car.checkClick(e.getX(), e.getY());
                    if (areSelected == 0) {
                        if (car.isSelected()) {
                            selectedIndex = cars.indexOf(car);
                            areSelected = 1;
                        }
                    } else if (car.isSelected()) {
                        cars.get(selectedIndex).setSelected(false);
                        selectedIndex = -1;
                        areSelected = 0;
                    }
                });
                mainPanel.repaint();
                //mainPanel.setFocusable(true);
            }
        });
    }

    private void addKeyListener() {
        mainPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                int key = e.getKeyCode();
                if (selectedIndex != -1) {
                    if (key == 37) {
                        cars.get(selectedIndex).setxVelocity(-1);
                        cars.get(selectedIndex).setyVelocity(0);
                        cars.get(selectedIndex).setDisplay(0);
                        cars.get(selectedIndex).move();
                        //cars.get(selectedIndex).draw(mainPanel.getGraphics());
                        mainPanel.repaint();
                    }
                    if (key == 38) {
                        cars.get(selectedIndex).setxVelocity(0);
                        cars.get(selectedIndex).setyVelocity(-1);
                        cars.get(selectedIndex).setDisplay(1);
                        mainPanel.repaint();
                    }
                    if (key == 39) {
                        cars.get(selectedIndex).setxVelocity(1);
                        cars.get(selectedIndex).setyVelocity(0);
                        cars.get(selectedIndex).setDisplay(2);
                    }
                    if (key == 40) {
                        cars.get(selectedIndex).setxVelocity(0);
                        cars.get(selectedIndex).setyVelocity(1);
                        cars.get(selectedIndex).setDisplay(3);
                    }
                    //mainPanel.repaint();
                }

                System.out.println("Key pressed... " + key);
                //mainPanel.setFocusable(true);
            }
        });
    }
}
