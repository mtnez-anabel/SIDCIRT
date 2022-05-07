package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.Const;
import model.Indicator;
import model.SubIndicator;

/**
 *
 * @author Anabel
 */
public class InputWindow extends JFrame {

    private int subIndNumber;
    private Fragment[] fragments;
    private double[] initial;
    private JButton accept, cancel;
    private Indicator indicator;
    private boolean acceptOption = false;

    public Fragment[] getFragments() {
        return fragments;
    }

    public int getSubIndNumber() {
        return subIndNumber;
    }

    public double[] getInput() {
        double[] input = new double[subIndNumber];
        for (int i = 0; i < fragments.length; i++) {
            String str = fragments[i].getTextField().getText();
            input[i] = Double.parseDouble(str);
        }
        return input;
    }

    public InputWindow(Indicator indicator) throws Exception {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.indicator = indicator;
        config();

        initComponents();
    }

    public void refreshShownValues() {
        for (int i = 0; i < fragments.length; i++) {
            fragments[i].getTextField().setText("" + indicator.subInd[i].ini);
        }
    }
    
    public void refreshModelValues() {
        for (int i = 0; i < fragments.length; i++) {
            String str = fragments[i].getTextField().getText();
            indicator.subInd[i].ini = Double.parseDouble(str);
        }
    }

    private void config() {
        setTitle(indicator.name);
        subIndNumber = indicator.subInd.length;
        SubIndicator[] elements = indicator.subInd;
        fragments = new Fragment[subIndNumber];
        for (int i = 0; i < fragments.length; i++) {
            Fragment f = new Fragment(elements[i].min, elements[i].max);
            f.getLabel().setText(elements[i].name);
            f.getTextField().setToolTipText(elements[i].comment);
            fragments[i] = f;
        }
    }

    private void initComponents() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        GridLayout panelLayout = new GridLayout(0, 2);
        JPanel mainPanel = new JPanel();
        accept = new JButton(Const.ACCEPT);
        accept.addActionListener(new AcceptListener());
        cancel = new JButton(Const.CANCEL);
        cancel.addActionListener(new CancelListener());
        for (int i = 0; i < fragments.length; i++) {
            mainPanel.add(fragments[i]);
        }
        // Si la cantidad de elementos es impar
        if (subIndNumber % 2 == 1) {
            JPanel gap = new JPanel();
            mainPanel.add(gap);
        }

        mainPanel.setLayout(panelLayout);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.add(accept);
        buttons.add(cancel);
        add(mainPanel, BorderLayout.PAGE_START);
        add(buttons, BorderLayout.LINE_END);
        pack();
        setResizable(false);
    }

    public void open() {
        System.out.println("open");
        acceptOption = false;
        initial = new double[subIndNumber];
        for (int i = 0; i < subIndNumber; i++) {
            String str = fragments[i].getTextField().getText();
            initial[i] = Double.parseDouble(str);
        }
        setVisible(true);
    }

    public void close() {
        System.out.println("close");
        if (!acceptOption && someValueChanged()) {
            int option = JOptionPane.showConfirmDialog(this, "Si continua cerrando la ventana perderá los cambios realizados.\n"
                    + "¿Desea cerrar la ventana?", "¡Atención!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                for (int i = 0; i < subIndNumber; i++) {
                    fragments[i].getTextField().setText("" + initial[i]);
                }
            }
            else{
                return;
            }
        }
        setVisible(false);
    }

    public boolean someValueChanged(){
        for (int i = 0; i < subIndNumber; i++) {
            if(Double.parseDouble(fragments[i].getTextField().getText()) != initial[i]){
                return true;
            }                
        }
        return false;
    }
    
    public class AcceptListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            acceptOption = true;
            close();
        }
    }

    public class CancelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            close();
        }
    }
}
