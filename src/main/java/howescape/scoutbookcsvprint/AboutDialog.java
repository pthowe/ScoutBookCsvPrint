/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package howescape.scoutbookcsvprint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Peter Maronda
 */
public class AboutDialog  extends JDialog implements ActionListener {
    public AboutDialog(JFrame parent, String title, String message) {
        super(parent, title, true);
        if (parent != null) {
            Dimension parentSize = parent.getSize(); 
            Point p = parent.getLocation(); 
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("OK"); 
        buttonPane.add(button); 
        button.addActionListener((ActionListener) this);



        JPanel messagePane = new JPanel();
        messagePane.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        messagePane.setBorder(BorderFactory.createMatteBorder(4,4,4,4,Color.BLACK));
        messagePane.setLayout(new java.awt.BorderLayout());
        messagePane.add(new JLabel(new ImageIcon(ScoutBookCsvPrint.class.getResource("../../HoweScapeLogo.png"))), BorderLayout.WEST);
            JPanel messagePane2 = new JPanel();
            messagePane2.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
            messagePane2.setLayout(new java.awt.GridLayout(0,1));
            messagePane2.add(new JLabel(title, SwingConstants.CENTER));
            messagePane2.add(new JLabel("Version: "+message, SwingConstants.CENTER));
            messagePane2.add(new JLabel("URL: http://www.HoweScape.com", SwingConstants.CENTER));
            messagePane2.add(buttonPane);
            messagePane.add(messagePane2, BorderLayout.CENTER);
        getContentPane().add(messagePane);
        //getContentPane().add(buttonPane, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack(); 
        setVisible(true);
//        File curDir = new File("../../../../HoweScapeLogo.png");
//        JLabel label = new JLabel(new ImageIcon(ScoutBookCsvPrint.class.getResource("../../HoweScapeLogo.png")));
    
    }
    public void actionPerformed(ActionEvent e) {
        setVisible(false); 
        dispose(); 
    }    
}
