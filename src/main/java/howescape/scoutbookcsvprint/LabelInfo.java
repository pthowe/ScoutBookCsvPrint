/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package howescape.scoutbookcsvprint;

//import com.itextpdf.text.FontFactory;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Phrase;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.sun.javafx.font.FontFactory;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Peter Maronda
 */
public class LabelInfo {
    String      lastName;
    String      firstName;
    String      patrol;
    int         quantity;
    String      sku_value;
    String      itemType;
    Currency    price;
    String      itemName;
    Date        dateEarned;
    int         fontValue;
    
    public LabelInfo() {
        setFirstName(" ");
        setLastName(" ");
        setPatrol(" ");
        setQuantity(0);
        setSKU(" ");
        setItemType(" ");
        //setPrice(Currency.);
        setItemName(" ");
        setDateEarned(" ");
        setFontValue(6);
        
    }
    public LabelInfo(String[] inCsvValues) {
        setFirstName(inCsvValues[0].replace("\"",""));
        setLastName(inCsvValues[1].replace("\"",""));
        setPatrol(inCsvValues[2].replace("\"",""));
        setQuantity(Integer.parseInt(inCsvValues[3].replace("\"","")));
        setSKU(inCsvValues[4].replace("\"",""));
        setItemType(inCsvValues[5].replace("\"",""));

        double value = Double.parseDouble(inCsvValues[6].replace("\"",""));
        //setPrice(Currency.);
        setItemName(inCsvValues[7].replace("\"",""));
        setDateEarned(inCsvValues[8].replace("\"",""));
    }
    public void setFontValue (int inFont) {
        fontValue = inFont;
    }
    public int getFontValue() {
        return fontValue;
    }
    public float getSelectedFontValueFloat() {
        return (float) fontValue;
    }
    public void setLastName (String inLastName) {
        lastName = inLastName;
    }
    public String getLastName () {
        return lastName;
    }
    public void setFirstName (String inFirstName) {
        firstName = inFirstName;
    }
    public String getFirstName () {
        return firstName;
    }
    public String getFullName() {
        return firstName+" "+lastName;
    }
    public void setPatrol (String inPatrol) {
        patrol = inPatrol;
    }
    
    public void setQuantity(int inQuantity) {
        quantity = inQuantity;
    }
    
    public void setSKU (String inSKU) {
        sku_value = inSKU;
    }
    
    public void setItemType (String inItemType) {
        itemType = inItemType;
    }
    public String getItemType () {
        return itemType;
    }
    
    public void setPrice (Currency inPrice) {
        price = inPrice;
    }
    
    public void setItemName (String inItemName) {
        itemName = inItemName;
    }
    public String getItemName() {
        return itemName;
    }
    public void setDateEarned (Date inDateEarned) {
        dateEarned = inDateEarned;
    }
    public void setDateEarned (String inDateEarned) {
        DateFormat format = new SimpleDateFormat ("yyyy-mm-dd");
        Date date;
        if (!inDateEarned.equals(" ")) {
        try {
            date = format.parse(inDateEarned);
            this.setDateEarned(date);
        } catch (ParseException ex) {
            Logger.getLogger(LabelInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    public String getDateEarned() {
        DateFormat df = new SimpleDateFormat("MM/dd/yy");
        if (dateEarned == null) {
            return " ";
        } 
        return df.format(dateEarned);
    }
    public boolean isDate() {
        boolean answer = false;
        if (dateEarned != null) {
            answer = true;
        }
        return answer;
    }
    
    public Paragraph blankLabel() {
        return this.formatAward(" ", " ", " ", " ");
    }
    
    public Paragraph formatAward(String inCouncilName, String inUnitTypeName, String inUnitNumber, String inCardMsg) {
        float fntSize, lineSpacing;
        fntSize = this.getSelectedFontValueFloat();
        Paragraph awardParagraph = null;
        lineSpacing = 10f;
        
        Style normal = new Style();
        try {
            PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            normal.setFont(font).setFontSize(fntSize);
            normal.setHorizontalAlignment(HorizontalAlignment.CENTER);
//          awardParagraph = new Paragraph (new Phrase(lineSpacing, this.formatAwardStr(inCouncilName, inUnitTypeName, inUnitNumber, inCardMsg), 
//                    FontFactory.getFont(FontFactory.COURIER, fntSize)));
            Text labelText = new Text(this.formatAwardStr(inCouncilName, inUnitTypeName, inUnitNumber, inCardMsg));
            awardParagraph = new Paragraph (labelText.addStyle(normal).setHorizontalAlignment(HorizontalAlignment.CENTER));
        } catch (IOException ex) {
            Logger.getLogger(LabelInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return awardParagraph;
    }

    public String formatAwardStr(String inCouncilName, String inUnitTypeName, String inUnitNumber, String inCardMsg) {
        String text = "";
        text = this.getFirstName()+" "+this.getLastName()+"\n";
        text = text + inCardMsg+"\n";
        text = text + inCouncilName+"\n";
        if (this.isDate()) {
            text = text + inUnitTypeName+" "+inUnitNumber+" Date "+this.getDateEarned();
        } else {
            text = text + " ";
        }
        return text;
    }
    // First Last
    // Has met the Reqt's for the <rank>  bronze palm
    // Council: Chester County
    // Unit: 0008 Date 06/29/17
    
    // First Last
    // Has met the Reqt's for the <merit badge name> MB
    // Council: Chester County
    // Unit: 0008 Date 06/29/17
    
    // First Last
    // Has met the Reqt's for the <merit badge name> MB
    // Council: Chester County
    // Unit: 0008 Date 06/29/17
    
}
