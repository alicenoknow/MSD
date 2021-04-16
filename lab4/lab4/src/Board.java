import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
    private static final long serialVersionUID = 1L;
    private Point[][] points;
    private final int size = 10;
    public int editType = 0;
    private int MAX_VELOCITY = 5;
    private int cars = 0;
    private int it = 0;

    public Board(int length, int height) {
        addMouseListener(this);
        addComponentListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    private void randclick(){
        Random generator = new Random();
        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y) {
                if(generator.nextDouble() <= 0.95) {
                    points[x][y].clicked();
                    this.cars++;
                }
            }
        System.out.println("Density: " + 1.0 * this.cars / (points.length * points[0].length));

    }

    public void iteration() {
        List<Point> active = Arrays.stream(points)
                .flatMap(Arrays::stream)
                .filter(p -> p.getStatus())
                .collect(toList());

        int before = 0;
        it++;


        active.parallelStream().forEach(p -> p.setBeforeMove());
        active.parallelStream().forEach(p -> p.acceleration());
        active.parallelStream().forEach(p -> p.slowingDownOrChangingLane());
        active.parallelStream().forEach(p -> p.randomization());
        active.parallelStream().forEach(p -> p.carMotion());

        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y) {
                before += points[x][y].velocity;
            }

        if(it == 300) {
            System.out.println(it + ",  " + (float) (1.0 * before / cars));
            System.exit(0);
        }

        this.repaint();
    }

    public void clear() {
        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y].clear();
            }
        this.repaint();
    }

    private void initialize(int length, int height) {
        points = new Point[length][height];

        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y)
                points[x][y] = new Point(MAX_VELOCITY);

        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y)
                for (int i = -MAX_VELOCITY; i <= MAX_VELOCITY; i++) {
                    if(i != 0)
                        points[x][y].addCenterNeighbor(points[(x + i + length) % length][y]);
                    points[x][y].addRightNeighbor(points[(x + i + length) % length][(y + 1 + height) % height]);
                    points[x][y].addLeftNeighbor(points[(x + i + length) % length][(y - 1 + height) % height]);
                }
        randclick();
    }

    protected void paintComponent(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        g.setColor(Color.GRAY);
        drawNetting(g, size);
    }

    private void drawNetting(Graphics g, int gridSpace) {
        Insets insets = getInsets();
        int firstX = insets.left;
        int firstY = insets.top;
        int lastX = this.getWidth() - insets.right;
        int lastY = this.getHeight() - insets.bottom;

        int x = firstX;
        while (x < lastX) {
            g.drawLine(x, firstY, x, lastY);
            x += gridSpace;
        }

        int y = firstY;
        while (y < lastY) {
            g.drawLine(firstX, y, lastX, y);
            y += gridSpace;
        }

        for (x = 0; x < points.length; ++x) {
            for (y = 0; y < points[x].length; ++y) {
                float a = 1.0F;
                if (points[x][y].getStatus())
                    a = 0.0F;
                g.setColor(new Color(a, a, a, 0.7f));
                g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
            }
        }

    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if (editType == 0) {
                points[x][y].clicked();
            }
            this.repaint();
        }
    }

    public void componentResized(ComponentEvent e) {
        int dlugosc = (this.getWidth() / size) + 1;
        int wysokosc = (this.getHeight() / size) + 1;
        initialize(dlugosc, wysokosc);
    }

    public void mouseDragged(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if (editType == 0) {
                points[x][y].clicked();
            }
            this.repaint();
        }
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

}
