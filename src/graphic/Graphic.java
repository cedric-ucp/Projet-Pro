package graphic;

import utils.Const;

import javax.swing.*;
import java.awt.*;

public class Graphic extends JFrame implements Runnable{
    public static final long serialVersionUID = 1L;
    private static final Font font = new Font(Font.MONOSPACED , Font.BOLD , 20);
    private final static Dimension preferredSize = new Dimension(Const.WINDOW_WIDTH / 2 , Const.WINDOW_HEIGHT / 2);
    private final JButton scan = new JButton(" SCAN ");
    private final JButton choosePayload = new JButton(" AUDIT ");
    private final JPanel principalPanel = new JPanel();

    public Graphic (){
        configGUI();
    }

    private void configGUI(){
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        principalPanel.setLayout(new BorderLayout());
        //principalPanel.setPreferredSize(preferredSize);
        principalPanel.add(scan);
        principalPanel.add(choosePayload);
        //principalPanel.setBackground (new Color ( 128, 162, 190 )) ;
        container.add(principalPanel);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //container.setPreferredSize(new Dimension(Const.WINDOW_WIDTH , Const.WINDOW_HEIGHT));
        pack();
        setVisible(true);
        setResizable(true);
    }
    @Override
    public void run() {

    }
}
