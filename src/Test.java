import waterflowsim.Cell;
import waterflowsim.Simulator;

import javax.swing.*;
import java.awt.*;

public class Test extends JPanel {

    Cell[] data;

    public Test(Cell[] data) {
        this.data = data;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        System.out.println("W x H: " + Simulator.getDimension().x + "/" + Simulator.getDimension().y);
        //System.out.println(Simulator.getDelta().x + "/" + Simulator.getDelta().y);
        //System.out.println("Start??? Sx / Sy: " + Simulator.getStart().x + "/" + Simulator.getStart().y);

        for (Cell cell : data) {
            if (!cell.isDry())
            System.out.println("je");
        }

        g.setColor(Color.PINK);
        Cell[][] mapa = new Cell[Simulator.getDimension().x][Simulator.getDimension().y];
        for (int y = 0; y < mapa.length;y++) {
            for (int x = 0; x < mapa[0].length;x++) {
                mapa[x][y] = data[mapa[0].length*y+x];
                if (!data[mapa[0].length*y+x].isDry())
                g.drawLine(x,y,x,y);
            }
        }

    }
}
