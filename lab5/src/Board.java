import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

public class Board extends JComponent implements MouseInputListener, ComponentListener {
    private static final long serialVersionUID = 1L;
    private Point[][] points;
    private int size = 10;
    public int editType=0;

    public Board(int length, int height) {
        addMouseListener(this);
        addComponentListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
        setOpaque(true);
    }

    public void iteration() {
        ArrayList<Point> randomOrder = new ArrayList<>();

        for (int x = 1; x < points.length - 1; ++x)
            for (int y = 1; y < points[x].length - 1; ++y) {
                points[x][y].unblock();
                randomOrder.add(points[x][y]);
            }
        Collections.shuffle(randomOrder);
        for(Point next : randomOrder)
            next.move();

        this.repaint();
    }

    public void clear() {
        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y) {
                points[x][y].clear();
            }

        calculateField();
//        for (int x = 0; x < points.length; ++x)
//            for (int y = 0; y < points[x].length; ++y) {
//                System.out.println("Type " + points[x][y].type + " field" + points[x][y].staticField + "\n");
//            }
        this.repaint();
    }

    private void initialize(int length, int height) {
        points = new Point[length][height];

        for (int x = 0; x < points.length; ++x)
            for (int y = 0; y < points[x].length; ++y)
                points[x][y] = new Point();

        for (int x = 1; x < points.length-1; ++x) {
            for (int y = 1; y < points[x].length-1; ++y) {
                // Neumann neighborhood
                points[x][y].addNeighbor(points[x][y+1]); // N
                points[x][y].addNeighbor(points[x][y-1]); // S
                points[x][y].addNeighbor(points[x+1][y]); // E
                points[x][y].addNeighbor(points[x-1][y]); // W

                // Uncomment for Moore neighborhood
//                points[x][y].addNeighbor(points[x+1][y+1]); // NE
//                points[x][y].addNeighbor(points[x-1][y+1]); // NW
//                points[x][y].addNeighbor(points[x+1][y-1]); // SE
//                points[x][y].addNeighbor(points[x-1][y-1]); // SW
            }
        }
    }

    private void calculateField(){
        ArrayList<Point> toCheck = new ArrayList<>();

        for(int x = 0; x < points.length; ++x)
            for(int y = 0; y < points[x].length; ++y)
                if (points[x][y].type == 2) {
                    points[x][y].staticField = 0;
                    toCheck.addAll(points[x][y].neighbors);
                }

        while(!toCheck.isEmpty()){
            Point p = toCheck.remove(0);
            if(p.calcStaticField())
                toCheck.addAll(p.neighbors);
        }
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

        for (x = 1; x < points.length-1; ++x) {
            for (y = 1; y < points[x].length-1; ++y) {
                if(points[x][y].type==0){
                    float staticField = points[x][y].staticField;
                    float intensity = staticField/100;
                    if (intensity > 1.0) {
                        intensity = 1.0f;
                    }
                    g.setColor(new Color(intensity, intensity,intensity ));
                }
                else if (points[x][y].type==1){
                    g.setColor(new Color(1.0f, 0.0f, 0.0f, 0.7f));
                }
                else if (points[x][y].type==2){
                    g.setColor(new Color(0.0f, 1.0f, 0.0f, 0.7f));
                }
                if (points[x][y].isPedestrian){
                    g.setColor(new Color(0.0f, 0.0f, 1.0f, 0.7f));
                }
                g.fillRect((x * size) + 1, (y * size) + 1, (size - 1), (size - 1));
            }
        }

    }

    public void mouseClicked(MouseEvent e) {
        markPoint(e);
    }

    private void markPoint(MouseEvent e) {
        int x = e.getX() / size;
        int y = e.getY() / size;
        if ((x < points.length) && (x > 0) && (y < points[x].length) && (y > 0)) {
            if(editType==3){
                points[x][y].isPedestrian=true;
            }
            else{
                points[x][y].type= editType;
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
        markPoint(e);
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

