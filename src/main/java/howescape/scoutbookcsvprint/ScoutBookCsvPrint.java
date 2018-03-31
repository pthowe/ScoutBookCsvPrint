/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package howescape.scoutbookcsvprint;

import javax.swing.JOptionPane;

/**
 *
 * @author Peter Maronda
 */
public class ScoutBookCsvPrint {
    ScoutBookRankCard sbrc;

    public ScoutBookCsvPrint() {
            JOptionPane.showMessageDialog(null, "infoMessage", "InfoBox: " , JOptionPane.INFORMATION_MESSAGE);
            
            sbrc = new ScoutBookRankCard();

    }
}
