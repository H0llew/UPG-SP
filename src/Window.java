import javax.swing.*;

public class Window {

    private Window() {};

    /**
     * Vytvoří nové prázdné okno;
     */
    public static JFrame createBasicWindow() {
        return createBasicWindow(0,0, "new Window");
    }

    /**
     * Vytvoří nové prázdné okno o zadaných rozměrech
     *
     * @param x šířka okna v pixelech
     * @param y výška okna v pixelech
     */
    public static JFrame createBasicWindow(int x, int y, String windowTitle) {
        JFrame jframe = new JFrame();

        jframe.setTitle(windowTitle);
        jframe.setSize(x,y);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        return jframe;
    }
}
