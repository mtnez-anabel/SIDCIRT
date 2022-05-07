package model;

import gui.MainWindow;
import io.Config;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación
 *
 * @author Anabel
 */
public class App {

    //Indicadores    
    public static HashMap<Ind, Indicator> indicators = new HashMap<>();
    public static Point centerPoint; 
    
    private static void loadProperties() {
        Properties p = new Properties(System.getProperties());
        try {
            FileInputStream in = new FileInputStream(Const.PROPERTIES);
            p.loadFromXML(in);
            in.close();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.setProperties(p);
    }

    private static void loadIndicatorsConfig() {
        for (Ind i : Ind.values()) {
            indicators.put(i, Config.load(i));
        }
    }
    
    private static void setLocation(MainWindow window) {
        Dimension size = window.getSize();
        
        int leftUpper = centerPoint.x - (size.width / 2);        
//        window.setSize(2*centerPoint.x, size.height);
        window.setLocation(leftUpper, 5);
    }
    
    public static void setCentered(JFrame frame){
        Dimension size = frame.getSize();
        double height = size.getHeight();
        double width = size.getWidth();
        
        frame.setLocation(centerPoint.x - (int)(width/2), centerPoint.y - (int)(height/2));
    }

    private static void showGUI() {
        // Configuramos el LAF similar al del SO
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        centerPoint = ge.getCenterPoint();
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow window = new MainWindow();
                window.setAlwaysOnTop(true);
                setLocation(window);
                window.setVisible(true);
            }
        });
    }

    public static void main(String args[]) {
        loadProperties();
        loadIndicatorsConfig();
        showGUI();
    }
}
