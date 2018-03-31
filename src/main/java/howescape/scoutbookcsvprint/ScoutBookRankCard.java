/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package howescape.scoutbookcsvprint;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Properties;
import javafx.application.Application;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;    
/**
 *
 * @author Peter Maronda
 */
public class ScoutBookRankCard extends javax.swing.JDialog {

    // Hold list of labels
    private ArrayList<printerLabelValues> comboBoxItems_labelStyle;
    private final String msg_meritbadgeTag = "<mb_name>";
    private final String msg_rankTag = "<rank>";
    
    private final String ParamFileName = "Config_ScoutBook.properties";
    private final String LABEL_STRING_TAG = "LabelStyle";
    private final String LABEL_STRING_CSV = "LabelCSV";
    private final String LABEL_STRING_FONT = "FontSize";
    private final String LABEL_STRING_POSITION = "LabelPosition";
    private final String LABEL_STRING_UNITTYPE = "UnitType";
    private final String LABEL_STRING_UNITNUMBER = "UnitNumber";
    private final String LABEL_STRING_COUNCIL = "Council";
    private final String LABEL_STRING_MSG_RANK = "RankMsg";
    private final String LABEL_STRING_MSG_MB = "MBMsg";
    private final String DIALOG_TITLE = "HoweScape.com CSV Boy Scout Label Printer";

    /**
     * Creates new form ScoutBookRankCard
     */
    public ScoutBookRankCard(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setStyleLabels();
        this.loadFontSizeComboBox();
        this.setNumberLabels();
        this.loadCouncilList();
        
        try {    
             //String IMAGE_URL = "C:\\Users\\Peter Maronda\\Documents\\BoyScouts\\scoutbook\\ScoutBookCSVPrint\\ScoutBookCsvPrint\\src\\main\\resources\\HoweScapeLogo.png";
             String IMAGE_URL = "/HoweScapeLogo.png";
             Image image = ImageIO.read(ScoutBookRankCard.class.getResource(IMAGE_URL));
             this.setIconImage(image);
        //java.net.URL url = ClassLoader.getSystemResource("C:\\Users\\Peter Maronda\\Documents\\BoyScouts\\scoutbook\\ScoutBookCSVPrint\\ScoutBookCsvPrint\\src\\main\\resources\\HoweScapeLogo.png");
        //    this.setIconImage(ImageIO.read(url));
        } catch (IOException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }

        //this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("HoweScapeLogo.png")));
        // Check for param file if present set initial values.
        File paramFile = new File(ParamFileName);
        if (paramFile.exists()) {
            try {
                FileReader reader = new FileReader(paramFile);
                Properties props = new Properties();
                props.load(reader);
                ScoutBookCSV_textFile.setText(props.getProperty(LABEL_STRING_CSV));
                LabelStyle_jComboBox1.setSelectedItem(props.getProperty(LABEL_STRING_TAG));
                fontSize_jComboBox.setSelectedItem(props.getProperty(LABEL_STRING_FONT));
                labelPosition_jComboBox.setSelectedItem(props.getProperty(LABEL_STRING_POSITION));
                UnitType_jComboBox.setSelectedItem(props.getProperty(LABEL_STRING_UNITTYPE));
                TroopNumber_jTextField.setText(props.getProperty(LABEL_STRING_UNITNUMBER));
                CouncilList_jComboBox.setSelectedItem(props.getProperty(LABEL_STRING_COUNCIL));
                RankMsg_jTextField.setText(props.getProperty(LABEL_STRING_MSG_RANK));
                MBMsg_jTextField.setText(props.getProperty(LABEL_STRING_MSG_MB));
                reader.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
                
    }

    ScoutBookRankCard() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void loadCouncilList() throws SecurityException {
        BufferedReader br = null;
        String nl;
        ArrayList<String> councilList = new ArrayList<String>();
        try {
            
            String respath = "/CouncilList.xml";
            InputStream in = ScoutBookRankCard.class.getResourceAsStream(respath);
            if ( in == null )
                try {
                    return;
                    //throw new Exception("resource not found: " + respath);
                } catch (Exception ex) {
                    Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
                }
            // read file and print to output window
            //BufferedReader bufRead = new BufferedReader(new InputStreamReader(in));
            //StringBuilder builder = new StringBuilder();
            //String line=null;
            //while((line=bufRead.readLine())!=null){
            //    builder.append(line).append("\n");
            //}
            //System.out.println(builder.toString());        

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(in);
            Element doc1 = doc.getDocumentElement();
            NodeList nodeList = doc1.getElementsByTagName("Council");
            for(int x=0,size= nodeList.getLength(); x<size; x++) {
                Element councilElm = (Element) nodeList.item(x);
                NodeList councilNameChild = councilElm.getChildNodes(); //getAttributes().getNamedItem("name").getNodeValue();
                int childLength = councilNameChild.getLength();
                for (int i=0;i<childLength;i++) {
                    if (councilNameChild.item(i).getNodeName().equalsIgnoreCase("Name")) {
                        String councilName = councilNameChild.item(i).getTextContent();
                        councilList.add(councilName);
                        //CouncilList_jComboBox.addItem(councilName);
                    }
                }
            }
            Collections.sort(councilList);
            for (int i=0;i<councilList.size();i++) {
                CouncilList_jComboBox.addItem(councilList.get(i).toString());
            }            
        } catch (IOException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setStyleLabels() {
        // Allow seletion of format
        //comboBoxItems_labelStyle = loadNamesFromXML ();
        comboBoxItems_labelStyle = loadNamesFromXMLAvery ();
        //int initSize = LabelStyle_jComboBox1.getItemCount();
        //LabelStyle_jComboBox1.removeAll();
        int i;
        for(i=0;i<comboBoxItems_labelStyle.size();i++) {
            LabelStyle_jComboBox1.addItem(comboBoxItems_labelStyle.get(i).getName());
        }                
        //LabelStyle_jComboBox1.setSelectedItem(comboBoxItems_labelStyle.get(0).getName());
        //for (i=initSize-1; i>=0;i--) {
        //    LabelStyle_jComboBox1.removeItemAt(i);
        //}
    }
    private void setNumberLabels() {
        if (LabelStyle_jComboBox1.getItemCount() > 0) {
            int lineStyle_index = LabelStyle_jComboBox1.getSelectedIndex();
            int x = comboBoxItems_labelStyle.get(lineStyle_index).getNumberX();
            int y = comboBoxItems_labelStyle.get(lineStyle_index).getNumberY();
            int total = x * y;
            int initSize = labelPosition_jComboBox.getItemCount();
            for (int i=initSize-1; i>=0;i--) {
                labelPosition_jComboBox.removeItemAt(i);
            }
            for (int i=1;i<=total;i++) {
                labelPosition_jComboBox.addItem(Integer.toString(i));
            }        
        }
    }
    private void loadFontSizeComboBox() {
        String[] definedFonts = {"6","7","8","9","10","11","12","13","14"};
        int initSize = fontSize_jComboBox.getItemCount();
        for (int i=initSize-1; i>=0;i--) {
            fontSize_jComboBox.removeItemAt(i);
        }
        for (int i=0;i<definedFonts.length;i++) {
            fontSize_jComboBox.addItem(definedFonts[i]);
        }
    }
    private void updateLayout() {
        labelPreviewPanel.setBackground(Color.white);
    }
    private int getSelectedFontValue() {
        String fs_value = (String) fontSize_jComboBox.getSelectedItem();
        return Integer.parseInt(fs_value);
    }
    private int getSelectedLabelPosition() {
        String label_pos = (String) labelPosition_jComboBox.getSelectedItem();
        return Integer.parseInt(label_pos);
    }
    private float getMarginValue() {
        float margin = comboBoxItems_labelStyle.get(LabelStyle_jComboBox1.getSelectedIndex()).getMarginFloat();
        return margin;
    }
    private int getSelectedNumberOfColumns() {
        printerLabelValues plv = comboBoxItems_labelStyle.get(LabelStyle_jComboBox1.getSelectedIndex());
        return plv.getNumberX();
    }
    private int getSelectedNumberOfRows() {
        printerLabelValues plv = comboBoxItems_labelStyle.get(LabelStyle_jComboBox1.getSelectedIndex());
        return plv.getNumberY();
    }
    private ArrayList<printerLabelValues> loadNamesFromXMLAvery() {
        ArrayList<printerLabelValues> comboBoxItems_labelStyle = new ArrayList();
        try {
            String averySettingFile = "../../avery-us-templates.xml"; // works in IDE
            averySettingFile = "/avery-us-templates.xml";

//            String  line;
//            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(averySettingFile)));
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//            }                        

            InputStream in = ScoutBookRankCard.class.getResourceAsStream(averySettingFile);

            if (in == null) 
                try {
                    return comboBoxItems_labelStyle;
                    //throw new Exception ("Resource not found: " + averySettingFile);
                } catch (Exception ex) {
                    Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
                }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(in);
            
            Element doc1 = doc.getDocumentElement();
            
            NodeList nodeList = doc1.getElementsByTagName("Template");
            int shortEntries = 0;
            int index = -1;
            for(int x=0,size= nodeList.getLength(); x<size; x++) {
                //System.out.println(nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue());
                printerLabelValues newLabel = new printerLabelValues();
                index++;
                Element templateElm = (Element) nodeList.item(x);
                //newLabel.setName (templateElm.getAttributes().getNamedItem("name").getNodeValue());
                newLabel.setName (templateElm.getAttributes().getNamedItem("brand").getNodeValue()+" "+
                                  templateElm.getAttributes().getNamedItem("part").getNodeValue());
                if (templateElm.getAttributes().getNamedItem("size") == null) {
                    shortEntries++;
                } else {
                    newLabel.setSize (templateElm.getAttributes().getNamedItem("size").getNodeValue());
                    if (templateElm.getAttributes().getNamedItem("_description") != null ) {
                        newLabel.setDescription (templateElm.getAttributes().getNamedItem("_description").getNodeValue());
                    } else {
                        newLabel.setDescription (templateElm.getAttributes().getNamedItem("description").getNodeValue());
                    }
                    NodeList childNodes =  templateElm.getElementsByTagName("Label-rectangle");
                    int ch_i = childNodes.getLength();
                    for (int j=0;j<ch_i;j++) {
                        String ch_Name = childNodes.item(j).getNodeName();
                        if (ch_Name.equals("Label-rectangle")) {
                            newLabel.setId(Integer.parseInt(childNodes.item(j).getAttributes().getNamedItem("id").getNodeValue()));
                            newLabel.setWidth (childNodes.item(j).getAttributes().getNamedItem("width").getNodeValue());
                            newLabel.setHeight (childNodes.item(j).getAttributes().getNamedItem("height").getNodeValue());
                            newLabel.setRound (childNodes.item(j).getAttributes().getNamedItem("round").getNodeValue());
                            if (null != childNodes.item(j).getAttributes().getNamedItem("x_waste")) {
                            newLabel.setXWaste(childNodes.item(j).getAttributes().getNamedItem("x_waste").getNodeValue());
                            } else {
                                newLabel.setXWaste("0pt");
                            }
                            if (null != childNodes.item(j).getAttributes().getNamedItem("y_waste")) {
                                newLabel.setYWaste(childNodes.item(j).getAttributes().getNamedItem("y_waste").getNodeValue());
                            } else {
                                newLabel.setYWaste("0pt");
                            }
                        } 
                    }
                    childNodes =  templateElm.getElementsByTagName("Label-round");
                    ch_i = childNodes.getLength();
                    for (int j=0;j<ch_i;j++) {
                        String ch_Name = childNodes.item(j).getNodeName();
                        if (ch_Name.equals("Label-round")) {
                            newLabel.setId (Integer.parseInt(childNodes.item(j).getAttributes().getNamedItem("id").getNodeValue()));
                            newLabel.setRadius (childNodes.item(j).getAttributes().getNamedItem("radius").getNodeValue());                    
                        }
                    }         
                    childNodes = templateElm.getElementsByTagName("Markup-margin");
                    ch_i = childNodes.getLength();
                    for (int j=0;j<ch_i;j++) {
                        newLabel.setMargin(childNodes.item(j).getAttributes().getNamedItem("size").getNodeValue());
                    }
/*               
                NodeList MarkupList = doc1.getElementsByTagName("Markup-margin");
                //for (int y=0,sizeY= MarkupList.getLength();y<sizeY; y++) {
                    newLabel.setMargin (MarkupList.item(x).getAttributes().getNamedItem("size").getNodeValue());
                //}
*/
                //System.out.println(newLabel.getName());
                NodeList layoutList = doc1.getElementsByTagName("Layout");
                //for (int y=0,sizeY= layoutList.getLength();y<sizeY; y++) {
                    newLabel.setNumberX (Integer.parseInt(layoutList.item(x-shortEntries).getAttributes().getNamedItem("nx").getNodeValue()));
                    newLabel.setNumberY (Integer.parseInt(layoutList.item(x-shortEntries).getAttributes().getNamedItem("ny").getNodeValue()));
                    
                    newLabel.setX0 (layoutList.item(x-shortEntries).getAttributes().getNamedItem("x0").getNodeValue());
                    newLabel.setY0 (layoutList.item(x-shortEntries).getAttributes().getNamedItem("y0").getNodeValue());
                    newLabel.setDeltaX (layoutList.item(x-shortEntries).getAttributes().getNamedItem("dx").getNodeValue());
                    newLabel.setDeltaY (layoutList.item(x-shortEntries).getAttributes().getNamedItem("dy").getNodeValue());
                //}               

                comboBoxItems_labelStyle.add(newLabel);
                }
            //comboBoxItems_labelStyle.add(nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue());
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }catch (SAXException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }

        return comboBoxItems_labelStyle;        
    }
    
    private ArrayList<printerLabelValues> loadNamesFromXML () {
        ArrayList<printerLabelValues> comboBoxItems_labelStyle = new ArrayList();
        try {
            String labelSettingFile = "../../Labels-templates.xml";
            String averySettingFile = "../../avery-us-templates.xml";
            InputStream in = ScoutBookRankCard.class.getResourceAsStream(averySettingFile);
            if (in == null) 
                try {
                    throw new Exception ("Resource not found: " + labelSettingFile);
                } catch (Exception ex) {
                    Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
                }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(in);
            //File fXmlFile = new File("C:\\Users\\Peter Maronda\\Documents\\BoyScouts\\scoutbook\\ScoutBookCSVPrint\\ScoutBookCsvPrint\\src\\main\\java\\howescape\\scoutbookcsvprint\\newXMLDocument.xml");
            //File fXmlFile = new File("C:\\Users\\Peter Maronda\\Documents\\BoyScouts\\scoutbook\\ScoutBookCSVPrint\\ScoutBookCsvPrint\\src\\main\\java\\resources\\testDocument.xml");
            
            //FileReader text = new FileReader ( fXmlFile.getAbsolutePath());
            //String msg = System.out.println(this.getClass().getResourceAsStream("newXMLDocument.xml").toString());
            //InputStream in = this.getClass().getResourceAsStream("newXMLDocument");
            //File file = new File("Labels-templates.xml");
            //String labelSettingFile = "C:\\Users\\Peter Maronda\\Documents\\BoyScouts\\scoutbook\\ScoutBookCSVPrint\\ScoutBookCsvPrint\\src\\main\\java\\resources\\Labels-templates.xml";
///            String labelSettingFile = "../../Labels-templates.xml";
///            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
///            DocumentBuilder db = dbf.newDocumentBuilder();
///            org.w3c.dom.Document doc = db.parse(new File(labelSettingFile));
            
            Element doc1 = doc.getDocumentElement();
            
            //String role1 = null;
            //role1 = getTextValue(role1, doc1, "user");
            //String usr = doc1.getElementsByTagName("Glabels-templates").item(0).getTextContent();
            //String pwd = doc1.getElementsByTagName("Template").item(0).getTextContent();
            NodeList nodeList = doc1.getElementsByTagName("Template");
            int index = -1;
            for(int x=0,size= nodeList.getLength(); x<size; x++) {
                //System.out.println(nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue());
                printerLabelValues newLabel = new printerLabelValues();
                index++;
                Element templateElm = (Element) nodeList.item(x);
                //newLabel.setName (templateElm.getAttributes().getNamedItem("name").getNodeValue());
                newLabel.setName (templateElm.getAttributes().getNamedItem("brand").getNodeValue()+" "+
                                  templateElm.getAttributes().getNamedItem("part").getNodeValue());
                newLabel.setSize (templateElm.getAttributes().getNamedItem("size").getNodeValue());
                if (templateElm.getAttributes().getNamedItem("_description") != null ) {
                    newLabel.setDescription (templateElm.getAttributes().getNamedItem("_description").getNodeValue());
                } else {
                    newLabel.setDescription (templateElm.getAttributes().getNamedItem("description").getNodeValue());
                }
                NodeList childNodes =  templateElm.getElementsByTagName("Label-rectangle");
                int ch_i = childNodes.getLength();
                for (int j=0;j<ch_i;j++) {
                    String ch_Name = childNodes.item(j).getNodeName();
                    if (ch_Name.equals("Label-rectangle")) {
                        newLabel.setId(Integer.parseInt(childNodes.item(j).getAttributes().getNamedItem("id").getNodeValue()));
                        newLabel.setWidth (childNodes.item(j).getAttributes().getNamedItem("width").getNodeValue());
                        newLabel.setHeight (childNodes.item(j).getAttributes().getNamedItem("height").getNodeValue());
                        newLabel.setRound (childNodes.item(j).getAttributes().getNamedItem("round").getNodeValue());                                            
                    } 
                }
                childNodes =  templateElm.getElementsByTagName("Label-round");
                ch_i = childNodes.getLength();
                for (int j=0;j<ch_i;j++) {
                    String ch_Name = childNodes.item(j).getNodeName();
                    if (ch_Name.equals("Label-round")) {
                        newLabel.setId (Integer.parseInt(childNodes.item(j).getAttributes().getNamedItem("id").getNodeValue()));
                        newLabel.setRadius (childNodes.item(j).getAttributes().getNamedItem("radius").getNodeValue());                    
                    }
                }         
                childNodes = templateElm.getElementsByTagName("Markup-margin");
                ch_i = childNodes.getLength();
                for (int j=0;j<ch_i;j++) {
                    newLabel.setMargin(childNodes.item(j).getAttributes().getNamedItem("size").getNodeValue());
                }
/*               
                NodeList MarkupList = doc1.getElementsByTagName("Markup-margin");
                //for (int y=0,sizeY= MarkupList.getLength();y<sizeY; y++) {
                    newLabel.setMargin (MarkupList.item(x).getAttributes().getNamedItem("size").getNodeValue());
                //}
*/
                NodeList layoutList = doc1.getElementsByTagName("Layout");
                //for (int y=0,sizeY= layoutList.getLength();y<sizeY; y++) {
                    newLabel.setNumberX (Integer.parseInt(layoutList.item(x).getAttributes().getNamedItem("nx").getNodeValue()));
                    newLabel.setNumberY (Integer.parseInt(layoutList.item(x).getAttributes().getNamedItem("ny").getNodeValue()));
                    
                    newLabel.setX0 (layoutList.item(x).getAttributes().getNamedItem("x0").getNodeValue());
                    newLabel.setY0 (layoutList.item(x).getAttributes().getNamedItem("y0").getNodeValue());
                    newLabel.setDeltaX (layoutList.item(x).getAttributes().getNamedItem("dx").getNodeValue());
                    newLabel.setDeltaY (layoutList.item(x).getAttributes().getNamedItem("dy").getNodeValue());
                //}               

                comboBoxItems_labelStyle.add(newLabel);
            //comboBoxItems_labelStyle.add(nodeList.item(x).getAttributes().getNamedItem("name").getNodeValue());
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }catch (SAXException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }

        return comboBoxItems_labelStyle;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        ScoutBookCSV_textFile = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        GenerateLabel_jButton = new javax.swing.JButton();
        LabelStyle_jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fontSize_jComboBox = new javax.swing.JComboBox<>();
        labelPosition_jComboBox = new javax.swing.JComboBox<>();
        TroopNumber_jTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        RankMsg_jTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        MBMsg_jTextField = new javax.swing.JTextField();
        UnitType_jComboBox = new javax.swing.JComboBox<>();
        CouncilList_jComboBox = new javax.swing.JComboBox<>();
        StoreSettings_jButton = new javax.swing.JButton();
        labelPreviewPanel = new howescape.scoutbookcsvprint.LabelPreviewPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        Open = new javax.swing.JMenuItem();
        jMenuItem_about = new javax.swing.JMenuItem();
        Exit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("HoweScape.com CSV Boy Scout Label Printer");

        ScoutBookCSV_textFile.setText("jTextField1");

        jLabel1.setText("Scout Book File:");

        GenerateLabel_jButton.setText("Generate Labels");
        GenerateLabel_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenerateLabel_jButtonActionPerformed(evt);
            }
        });

        LabelStyle_jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                LabelStyle_jComboBox1ItemStateChanged(evt);
            }
        });
        LabelStyle_jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabelStyle_jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Label Style: ");

        jLabel3.setText("Font Size:");

        jLabel4.setText("Label Position:");

        fontSize_jComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        labelPosition_jComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        TroopNumber_jTextField.setText("0008");

        jLabel6.setText("Council Name");

        RankMsg_jTextField.setText("Has met the Reqt's for the <rank>");
        RankMsg_jTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RankMsg_jTextFieldActionPerformed(evt);
            }
        });

        jLabel7.setText("Rank Msg");

        jLabel8.setText("MB Msg");

        MBMsg_jTextField.setText("Has met the Reqt's for the <mb_name>");

        UnitType_jComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pack", "Troop", "Post", "Crew", "Team", "Ship" }));
        UnitType_jComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UnitType_jComboBoxActionPerformed(evt);
            }
        });

        StoreSettings_jButton.setText("Store Settings");
        StoreSettings_jButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StoreSettings_jButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout labelPreviewPanelLayout = new javax.swing.GroupLayout(labelPreviewPanel);
        labelPreviewPanel.setLayout(labelPreviewPanelLayout);
        labelPreviewPanelLayout.setHorizontalGroup(
            labelPreviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        labelPreviewPanelLayout.setVerticalGroup(
            labelPreviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 306, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        Open.setText("Open");
        Open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenActionPerformed(evt);
            }
        });
        jMenu1.add(Open);

        jMenuItem_about.setText("About");
        jMenuItem_about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_aboutActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem_about);

        Exit.setText("Exit");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        jMenu1.add(Exit);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(UnitType_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ScoutBookCSV_textFile)
                            .addComponent(LabelStyle_jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(RankMsg_jTextField)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(TroopNumber_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CouncilList_jComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(MBMsg_jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(fontSize_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelPosition_jComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(StoreSettings_jButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(GenerateLabel_jButton))
                            .addComponent(labelPreviewPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ScoutBookCSV_textFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelStyle_jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(fontSize_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(labelPosition_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TroopNumber_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(UnitType_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CouncilList_jComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(RankMsg_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MBMsg_jTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelPreviewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(GenerateLabel_jButton)
                    .addComponent(StoreSettings_jButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenActionPerformed
        // TODO add your handling code here:
        File currentPath = new File(System.getProperty("user.dir"));
        String textFieldValue = "";
        if (ScoutBookCSV_textFile.getText().lastIndexOf("\\") > 0) {
            textFieldValue = ScoutBookCSV_textFile.getText().substring(0,ScoutBookCSV_textFile.getText().lastIndexOf("\\"));
        } else {
            textFieldValue = "";
        }
        File selectedPath = new File(textFieldValue);
        if (selectedPath.isDirectory()) {
            currentPath = selectedPath;
        }
        FileFilter filter = new FileNameExtensionFilter("Scout Book", "csv");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(currentPath);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ScoutBookCSV_textFile.setText(file.getPath());
        } else {
            System.out.println("File access cancelled by user.");
        }

    }//GEN-LAST:event_OpenActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_ExitActionPerformed

    private void LabelStyle_jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LabelStyle_jComboBox1ActionPerformed
        // TODO add your handling code here:
        //Vector comboBoxItems_labelStyle = new Vector();
        //comboBoxItems_labelStyle.add("itemMine");
        //LabelStyle_jComboBox1.removeAll();
        //int i;
        //for(i=0;i<comboBoxItems_labelStyle.size();i++) {
        //    LabelStyle_jComboBox1.addItem(comboBoxItems_labelStyle.get(i).toString());
        //}
        printerLabelValues li = comboBoxItems_labelStyle.get(LabelStyle_jComboBox1.getSelectedIndex());
        labelPreviewPanel.setLabelParameters(li.getNumberX(), li.getNumberY(), li.getWidthFloat());
        //JLabel jlabel = new JLabel("This is a label");
        //labelPreviewPanel.add(jlabel);
        //labelPreviewPanel.setBorder(new LineBorder(Color.BLACK)); // make it easy to see
        //labelPreviewPanel.repaint(10);
        labelPreviewPanel.repaint();
    }//GEN-LAST:event_LabelStyle_jComboBox1ActionPerformed

    private void LabelStyle_jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_LabelStyle_jComboBox1ItemStateChanged
        // TODO add your handling code here:
        this.setNumberLabels();
        this.updateLayout();
    }//GEN-LAST:event_LabelStyle_jComboBox1ItemStateChanged

    private void GenerateLabel_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenerateLabel_jButtonActionPerformed
        // TODO add your handling code here:
        String line = "";
        String outputFile = "";
        String outputFileList = "";
        String cvsSplitBy = ",";
        LabelInfo li = null;
        ArrayList<LabelInfo> awardList = new ArrayList<LabelInfo>();
        File f = new File(ScoutBookCSV_textFile.getText());
        if (f.exists()) {
            String p_fn = f.getAbsolutePath().substring(0,f.getAbsolutePath().lastIndexOf('.'));
            outputFile = p_fn + ".pdf";
            outputFileList = p_fn + "_list.pdf";
            
            awardList = loadCsvFile();
            createPdfFile(awardList, outputFile);
            createPdfListFile(awardList, outputFileList);
        } else {
            // Input file missing
            
        }
        
//        awardList = loadCsvFile();
/*        
        try {
            BufferedReader br = new BufferedReader(new FileReader(ScoutBookCSV_textFile.getText()));
            //OutputStream output = new FileOutputStream(outputFile);

            while ((line = br.readLine()) != null) {
                String[] badgeValues = line.split(cvsSplitBy);
                
                if (!"\"First Name\"".equalsIgnoreCase(badgeValues[0])) {
                    li = new LabelInfo(badgeValues);
                    awardList.add(li);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }
*/        
//        createPdfFile(awardList, outputFile);
/*        //
        int nc = this.getSelectedNumberOfColumns();
        int numLabels = awardList.size();
        int firstLabel = this.getSelectedLabelPosition() - 1;
        float marginValue = this.getMarginValue();
        int currentLabelIndex = 1;
        Document document = new Document(PageSize.LETTER, marginValue, marginValue, marginValue, marginValue );
        try {
            int aw = 0;
            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();
            PdfPTable table = new PdfPTable(nc);
            table.getDefaultCell().setBorder(0); // no border
            table.getDefaultCell().setHorizontalAlignment(ALLIGN_CENTER);
            for (int blank=0;blank<firstLabel;blank++){
                table.addCell(" ");
            }
            //if (currentLabelIndex < firstLabel) {
            //    table.addCell(" ");
            //    currentLabelIndex++;
            //}
                for(aw = 0; aw < numLabels; aw++) {
                
//            for(LabelInfo singleLabel : awardList) {
                table.addCell(awardList.get(aw).formatAward());
                currentLabelIndex++;
            }
            for(aw = numLabels+1; aw < 16; aw++){
                table.addCell("hi");
                currentLabelIndex++;
            }            
            table.completeRow();
            document.add(table);
            document.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }
*/      
/*
            Document document = new Document();
            PdfWriter.getInstance(document, output);
            document.open();
            int nc = this.getSelectedNumberOfColumns();
            PdfPTable table = new PdfPTable(nc);
            for (LabelInfo awardList1 : awardList) {
                //table.addCell(awardList1.formatAward().toString());
                table.addCell("Hi");
            }
            document.add(table);
            document.close(); 
*/

    }//GEN-LAST:event_GenerateLabel_jButtonActionPerformed

    private ArrayList<LabelInfo> loadCsvFile() {
        String line = "";
        String cvsSplitBy = ",";
        LabelInfo li = null;
        ArrayList<LabelInfo> awardList = new ArrayList<LabelInfo>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(ScoutBookCSV_textFile.getText()));
            //OutputStream output = new FileOutputStream(outputFile);

            while ((line = br.readLine()) != null) {
                String[] badgeValues = line.split(cvsSplitBy);
                
                
                //if (!"\"First Name\"".equalsIgnoreCase(badgeValues[0])) {
                if (!badgeValues[0].contains("First Name")) {
                    li = new LabelInfo(badgeValues);
                    awardList.add(li);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return awardList;
    }
    
    private void createPdfListFile(ArrayList<LabelInfo> awardList, String outputFileList) {
        PdfDocument pdfDoc = null;
        PageSize ps = PageSize.LETTER;
        Document document = null;
        Table table = null;
        int nc = 3;
        String previousScout = "";
        float marginLeft = (float)0.5 * (float)72.0;
        float marginTop = (float)0.5 * (float)72.0;

        try {
            pdfDoc = new PdfDocument(new PdfWriter(outputFileList));
            pdfDoc.addNewPage(ps);
            document = new Document(pdfDoc, ps);
            document.setMargins(marginLeft, marginTop, 0f, marginTop );
            table = new Table(nc, false);
            Cell cell;
            for (LabelInfo label : awardList) {
                if (!previousScout.equalsIgnoreCase(label.getFullName())) {
                    cell = new Cell(1,3);
                    cell.setFontSize(12);
                    cell.setBorder(Border.NO_BORDER);
                    table.addCell(cell);
                    cell = new Cell(1,2);
                    cell.setFontSize(12);
                    cell.setBorder(Border.NO_BORDER);
                    cell.add(new Paragraph(label.getFullName()).setFontSize(12));
                    table.addCell(cell);
                    cell = new Cell();
                    cell.setFontSize(12);
                    cell.setBorder(Border.NO_BORDER);
                    table.addCell(cell);      
                    previousScout = label.getFullName();
                }
                //
                cell = new Cell();
                cell.setFontSize(12);
                cell.setBorder(Border.NO_BORDER);
                table.addCell(cell);
                cell = new Cell(1,2);
                cell.setFontSize(12);
                cell.setBorder(Border.NO_BORDER);
                cell.add(new Paragraph(label.getItemName()));
                table.addCell(cell);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }
        document.add(table);
        document.flush();
        document.close();
    }
            
    private void createPdfFile(ArrayList<LabelInfo> awardList, String outputFile) {
        int nc = this.getSelectedNumberOfColumns();
        int nr = this.getSelectedNumberOfRows();
        int numLabels = awardList.size();
        int firstLabel = this.getSelectedLabelPosition() - 1;
        float marginValue = this.getMarginValue();
        PageSize ps = PageSize.LETTER;

        int currentLabelIndex = 1;
        int labelPositionUsed = 0;
        int columnCount = 0;
        Document document = null;
        Table table = null;
        PdfDocument pdfDoc = null;
        
        try {
            pdfDoc = new PdfDocument(new PdfWriter(outputFile));
            pdfDoc.addNewPage(ps);
            document = new Document(pdfDoc, ps);
            int aw = 0;
            
            float labelWidth = comboBoxItems_labelStyle.get(LabelStyle_jComboBox1.getSelectedIndex()).getWidthFloat();
            float labelHeight = comboBoxItems_labelStyle.get(LabelStyle_jComboBox1.getSelectedIndex()).getHeightFloat();
            float marginLeft = comboBoxItems_labelStyle.get(LabelStyle_jComboBox1.getSelectedIndex()).getX0();
            float marginTop = comboBoxItems_labelStyle.get(LabelStyle_jComboBox1.getSelectedIndex()).getY0();
            float paddingRight = comboBoxItems_labelStyle.get(LabelStyle_jComboBox1.getSelectedIndex()).getXWaste();
            float paddingBottom = comboBoxItems_labelStyle.get(LabelStyle_jComboBox1.getSelectedIndex()).getYWaste();
            document.setMargins(marginLeft, marginTop, 0f, marginTop );
            table = new Table(nc, false);
            //table.addHeaderCell("");
            //table.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            // Put blank labels into output
            LabelInfo emptyOne = new LabelInfo();
            for (int blank=0;blank<firstLabel;blank++){
                table.addCell(emptyOne.blankLabel());
                labelPositionUsed++;
                columnCount++;
            }
            // Add labels from csv file
            for(aw = 0; aw < numLabels; aw++) {
                String councilName = CouncilList_jComboBox.getSelectedItem().toString();
                String unitTypeName = UnitType_jComboBox.getSelectedItem().toString();
                String unitNumber = TroopNumber_jTextField.getText();
                String rankMSG = RankMsg_jTextField.getText();
                String mbMsg = MBMsg_jTextField.getText();
                String cardMSG = "";
                int currentFontSize = this.getSelectedFontValue();
                awardList.get(aw).setFontValue(currentFontSize);
                if (awardList.get(aw).getItemType().equals("Merit Badges")) {
                    String mbName = awardList.get(aw).getItemName();
                    mbName = mbName.substring(0, mbName.indexOf("Emblem"));
                    cardMSG = mbMsg.replace(msg_meritbadgeTag, mbName);
                } else {
                    String rankName =awardList.get(aw).getItemName().substring(0,awardList.get(aw).getItemName().indexOf("Emblem"));
                    cardMSG = rankMSG.replace(msg_rankTag, rankName);
                }
                Paragraph awardText = awardList.get(aw).formatAward(councilName, unitTypeName, unitNumber, cardMSG); 
                Cell cell = new Cell();
                cell.setBorder(Border.NO_BORDER);
                cell.setHeight(labelHeight);
                cell.setWidth(labelWidth);
                cell.setPaddingRight(paddingRight);
                //cell.setPaddingBottom(paddingBottom);
                cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
                table.addCell(cell.add(awardText.setTextAlignment(TextAlignment.CENTER)));
                //cell.add(new Paragraph("Hello").setTextAlignment(TextAlignment.RIGHT));
                //cell.add(new Paragraph("again"));

                labelPositionUsed++;
                currentLabelIndex++;
                columnCount++;
                if (labelPositionUsed >= (nc * nr)) {
                    //Page full
                    labelPositionUsed = 0;
                    //table.addFooterCell("");
                    document.add(table);
                    
                    //pdfDoc.addNewPage();
                    //document.add(new AreaBreak());
                    document.setMargins(marginLeft, marginTop, 0f, marginTop );
                    table = new Table(nc, false);
                    //table.addHeaderCell("");
                    //table.RowRange(1,8)
                    //table.complete();
//                    document.add(table);
//                    document.newPage();
//                    table = new Table(nc, true);
                }
            }
            //table.flushContent();
            //table.complete();
            //
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }
        document.add(table);
        document.flush();
        document.close();
    }
    private void createPdfFileItext5(ArrayList<LabelInfo> awardList, String outputFile) {
        //
        int nc = this.getSelectedNumberOfColumns();
        int numLabels = awardList.size();
        int firstLabel = this.getSelectedLabelPosition() - 1;
        float marginValue = this.getMarginValue();
        
/*
    try {
	//Create Document instance.
	Document document = new Document();
 
	//Create OutputStream instance.
	OutputStream outputStream = 
	    new FileOutputStream(new File("D:\\TestTableFile.pdf"));
 
	//Create PDFWriter instance.
        PdfWriter.getInstance(document, outputStream);
 
        //Open the document.
        document.open();
 
        //Create Table object, Here 4 specify the no. of columns
        PdfPTable pdfPTable = new PdfPTable(4);
 
        //Create cells
        PdfPCell pdfPCell1 = new PdfPCell(new Paragraph("Cell 1"));
        PdfPCell pdfPCell2 = new PdfPCell(new Paragraph("Cell 2"));
        PdfPCell pdfPCell3 = new PdfPCell(new Paragraph("Cell 3"));
        PdfPCell pdfPCell4 = new PdfPCell(new Paragraph("Cell 4"));
 
        //Add cells to table
        pdfPTable.addCell(pdfPCell1);
        pdfPTable.addCell(pdfPCell2);
        pdfPTable.addCell(pdfPCell3);
        pdfPTable.addCell(pdfPCell4);
 
        //Add content to the document using Table objects.
        document.add(pdfPTable);
 
        //Close document and outputStream.
        document.close();
        outputStream.close();
 
        System.out.println("Pdf created successfully.");
    } catch (Exception e) {
	e.printStackTrace();
    }
  }        
        */        
/*        
        int currentLabelIndex = 1;
        int labelPositionUsed = 0;
        
        //Font fontLabel= new Font(FontFamily.COURIER, 8, Font.NORMAL);
        
        Document document = new Document();
        try {
            int aw = 0;
            PdfWriter.getInstance(document, new FileOutputStream(outputFile));
            document.open();
            document.setMargins(36.0f, 36.0f, 36.0f, 36.0f );
            PdfPTable table = new PdfPTable(nc);
            //table.getDefaultCell().setBorder(0); // no border
            table.getDefaultCell().setHorizontalAlignment(ALIGN_CENTER);
            float cellHeight = comboBoxItems_labelStyle.get(LabelStyle_jComboBox1.getSelectedIndex()).getHeightFloat();
            cellHeight = 139.68f;
            table.getDefaultCell().setCalculatedHeight(cellHeight);
            LabelInfo emptyOne = new LabelInfo();
            for (int blank=0;blank<firstLabel;blank++){
                table.addCell(emptyOne.blankLabel());
                labelPositionUsed++;
            }
            //if (currentLabelIndex < firstLabel) {
            //    table.addCell(" ");
            //    currentLabelIndex++;
            //}
                for(aw = 0; aw < numLabels; aw++) {
                
//            for(LabelInfo singleLabel : awardList) {
                String councilName = CouncilList_jComboBox.getSelectedItem().toString();
                String unitTypeName = UnitType_jComboBox.getSelectedItem().toString();
                String unitNumber = TroopNumber_jTextField.getText();
                String rankMSG = RankMsg_jTextField.getText();
                String mbMsg = MBMsg_jTextField.getText();
                String cardMSG = "";
                int currentFontSize = this.getSelectedFontValue();
                awardList.get(aw).setFontValue(currentFontSize);
                if (awardList.get(aw).getItemType().equals("Merit Badges")) {
                    String mbName = awardList.get(aw).getItemName();
                    mbName = mbName.substring(0, mbName.indexOf("Emblem"));
                    cardMSG = mbMsg.replace(msg_meritbadgeTag, mbName);
                } else {
                    String rankName =awardList.get(aw).getItemName().substring(0,awardList.get(aw).getItemName().indexOf("Emblem"));
                    cardMSG = rankMSG.replace(msg_rankTag, rankName);
                }
                table.addCell(awardList.get(aw).formatAward(councilName, unitTypeName, unitNumber, cardMSG));
//                Paragraph labelText = awardList.get(aw).formatAward(councilName, unitTypeName, unitNumber, cardMSG);
//                table.addCell(
//                    new PdfPCell(labelText.toString());
                labelPositionUsed++;
                currentLabelIndex++;
            }
//            for(aw = numLabels+1; aw < 16; aw++){
//                table.addCell("hi");
//                currentLabelIndex++;
//            }            
            
            table.completeRow();
            document.add(table);
            document.close();
        } catch (DocumentException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }
  */      
    }
    private void RankMsg_jTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RankMsg_jTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RankMsg_jTextFieldActionPerformed

    private void StoreSettings_jButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StoreSettings_jButtonActionPerformed
        // TODO add your handling code here:
//        StringBuilder builder = new StringBuilder();
//        InputStream in = getClass().getResourceAsStream("/Config.File"); 
//        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//        String line;
//        try {
//            while((line=reader.readLine())!=null){
//                builder.append(line).append("\n");
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println(builder.toString()); 
        
        try {
            File configFile = new File(ParamFileName);
            Properties props = new Properties();
            props.setProperty(LABEL_STRING_CSV, ScoutBookCSV_textFile.getText());
            props.setProperty(LABEL_STRING_TAG, LabelStyle_jComboBox1.getSelectedItem().toString());
            props.setProperty(LABEL_STRING_FONT, fontSize_jComboBox.getSelectedItem().toString());
            props.setProperty(LABEL_STRING_POSITION, labelPosition_jComboBox.getSelectedItem().toString());
            props.setProperty(LABEL_STRING_UNITTYPE, UnitType_jComboBox.getSelectedItem().toString());
            props.setProperty(LABEL_STRING_UNITNUMBER, TroopNumber_jTextField.getText());
            props.setProperty(LABEL_STRING_COUNCIL, CouncilList_jComboBox.getSelectedItem().toString());
            props.setProperty(LABEL_STRING_MSG_RANK, RankMsg_jTextField.getText());
            props.setProperty(LABEL_STRING_MSG_MB, MBMsg_jTextField.getText());
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "ScoutBook CSV Label Printing");
            writer.close();        
           
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }//GEN-LAST:event_StoreSettings_jButtonActionPerformed

    private void UnitType_jComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UnitType_jComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_UnitType_jComboBoxActionPerformed

        private static void getAllFiles(File curDir) {

        File[] filesList = curDir.listFiles();
        for(File f : filesList){
            if(f.isDirectory())
                //System.out.println("AP: "+f.getAbsolutePath());
                getAllFiles(f);
            if(f.isFile()){
                System.out.println(f.getName());
                System.out.println("AP: "+f.getAbsolutePath());
            }
        }

    }
        
public synchronized String getVersion() {
    final String versionLabel = "<version>";
    final String versionLabelClose = "</version>";
    String version = null;
    String line;
    BufferedReader br = null;    
    try {
        if (new File("pom.xml").exists()) {
            InputStream fis = new FileInputStream("pom.xml");
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);        
        } else {
            File curDir = new File("../../../../pom.xml");
            InputStream fis = new FileInputStream(curDir);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            br = new BufferedReader(isr);            
            //getAllFiles(curDir);
            
        }
        
        while ((line = br.readLine()) != null) {
        // Deal with the line
            if (line.contains(versionLabel)) {
                System.out.println(line);
                int startIdx = line.indexOf(versionLabel)+versionLabel.length();
                int endIdx = line.indexOf(versionLabelClose);
                version = line.substring(startIdx, endIdx);
                break;
            }
        }
        br.close();
    } catch (FileNotFoundException ex) { 
        Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
    }
    return version;
}

    private void jMenuItem_aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_aboutActionPerformed
        
        String version = getVersion();
        
        AboutDialog dlg = new AboutDialog(new JFrame(), DIALOG_TITLE, version);
         
/*       
        Model model = null;
        
        //XmlSlurper pom.xml example
        MavenXpp3Reader reader=new MavenXpp3Reader();
        try {
            if ((new File("C:\\Users\\Peter Maronda\\Documents\\BoyScouts\\scoutbook\\ScoutBookCSVPrint\\ScoutBookCsvPrint\\pom.xml")).exists()) {  
                //File pomFile = projectBuilder.getRoot().toAbsolutePath().toString()+ File.separator + "pom.xml");
                File pomFile = new File("C:\\Users\\Peter Maronda\\Documents\\BoyScouts\\scoutbook\\ScoutBookCSVPrint\\ScoutBookCsvPrint\\pom.xml");
                model = new MavenXpp3Reader().read(new FileInputStream(pomFile));
                //BufferedReader in = new BufferedReader(Files.newBufferedReader("pom.xml", StandardCharsets.UTF_8));
                //InputStream is = new InputStream("pom.xml");
//                File f = new File("pom.xml");
//                FileInputStream fis = new FileInputStream(f);
///                model = reader.read(fis);
                //model = reader.read(new FileReader(in));
            } else {
                model = reader.read(
                    new InputStreamReader(
                            ScoutBookRankCard.class.getResourceAsStream(
                            "/META-INF/maven/HoweScape/howescape.ScoutBookCsvPrint/pom.xml"
                    )));
            }
            String ver = model.getVersion();
            this.setTitle(ver);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XmlPullParserException ex) {
            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
        }

if (version == null || version.equals("")) {
    this.setTitle("no title");
} else {
    this.setTitle(version);
}
        */
//ava.io.InputStream is = this.getClass().getResourceAsStream("my.properties");
//is = this.getClass().getResourceAsStream("./META-INF");

//
//System.out.println(" resource: "+is.toString());
//java.util.Properties p = new Properties();
//        try {
//            p.load(is);
//        } catch (IOException ex) {
//            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
//        }
//String name = p.getProperty("name");
//String version = p.getProperty("version");
//String foo = p.getProperty("foo");
//
//File curDir = new File("../../../../../.");
// getAllFiles(curDir);
// curDir = new File("../../../META-INF/maven/HoweScape/howescape.ScoutBookCsvPrint");
//  getAllFiles(curDir);

//            InputStream is = null;
//        Model model;
//        MavenXpp3Reader reader = new MavenXpp3Reader();
//        try {
//            File pomFile = new File("pom.xml");
//            is = new FileInputStream("/pom.xml");
//            int     i;
//            char    c;
//         // reads till the end of the stream
//         while((i = is.read())!=-1) {
//         
//            // converts integer to character
//            c = (char)i;
//            
//            // prints character
//            System.out.print(c);
//         }
         
            
//            if (pomFile.exists()) {
//                model = reader.read(new FileReader(pomFile));
//            } else {
//                model = reader.read(new InputStreamReader(ScoutBookRankCard.class.getResourceAsStream("/META-INF/maven/HoweScape/howescape.ScoutBookCsvPrint/pom.properties")));
//            }
 //       } catch (FileNotFoundException ex) {
//            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (XmlPullParserException ex) {
//            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
/*
}
       catch(Exception e) {
         
         // if any I/O error occurs
         e.printStackTrace();
      } finally {
         
         // releases system resources associated with this stream
 //        if(is!=null)
 //           is.close();
      }
*/

//Properties properties = new Properties();
//        try {
//            InputStream in = getClass().getResourceAsStream("pom.properties");
//            properties.load(in);
//            in.close();
//            properties.load(this.getClass().getResourceAsStream("pom.properties"));
//        } catch (IOException ex) {
//            Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
//        }
//System.out.println(properties.getProperty("version"));
//System.out.println(properties.getProperty("artifactId"));

/*
String version = null;

    // try to load from maven properties first
    try {
        Properties p = new Properties();
        InputStream is = this.getClass().getResourceAsStream("/META-INF/maven/HoweScape/howescape.ScoutBookCsvPrint/pom.xml");
        if (is != null) {
            p.load(is);
            version = p.getProperty("version", "");
        }
    } catch (Exception e) {
        // ignore
    }
*/

    }//GEN-LAST:event_jMenuItem_aboutActionPerformed

 private String getVersion2() {
     String version = null;
	        if (version == null) {
	            String res = "META-INF/maven/HoweScape/howescape.ScoutBookCsvPrint/pom.xml";
	            URL url = Thread.currentThread().getContextClassLoader().getResource(res);
	            if (url == null) {
//	                version = "SNAPSHOT." + Utils.timestamp();
	            } else {
//	                Properties props = Utils.loadProperties(res);
//	                version = props.getProperty("version");
	            }
	        }
	        return version;
	    }
 
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */        
/*
    MavenXpp3Reader reader = new MavenXpp3Reader();
    Model model = null;
    try {
    if ((new File("pom.xml")).exists()) {
          model = reader.read(new FileReader("pom.xml"));
    } else {
          model = reader.read(
                  new InputStreamReader(
                        ScoutBookRankCard.class.getResourceAsStream(
                                  "/META-INF/maven/de.scrum-master.stackoverflow/aspectj-introduce-method/pom.xml"
                          )
                  )
          );
    }
    } catch (IOException ex) {
        Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
    } catch (XmlPullParserException ex) {
        Logger.getLogger(ScoutBookRankCard.class.getName()).log(Level.SEVERE, null, ex);
    }
    System.out.println(model.getId());
    System.out.println(model.getGroupId());
    System.out.println(model.getArtifactId());
    System.out.println(model.getVersion());        


        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScoutBookRankCard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScoutBookRankCard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScoutBookRankCard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScoutBookRankCard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
*/
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ScoutBookRankCard dialog = new ScoutBookRankCard(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CouncilList_jComboBox;
    private javax.swing.JMenuItem Exit;
    private javax.swing.JButton GenerateLabel_jButton;
    private javax.swing.JComboBox<String> LabelStyle_jComboBox1;
    private javax.swing.JTextField MBMsg_jTextField;
    private javax.swing.JMenuItem Open;
    private javax.swing.JTextField RankMsg_jTextField;
    private javax.swing.JTextField ScoutBookCSV_textFile;
    private javax.swing.JButton StoreSettings_jButton;
    private javax.swing.JTextField TroopNumber_jTextField;
    private javax.swing.JComboBox<String> UnitType_jComboBox;
    private javax.swing.JComboBox<String> fontSize_jComboBox;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem_about;
    private javax.swing.JComboBox<String> labelPosition_jComboBox;
    private howescape.scoutbookcsvprint.LabelPreviewPanel labelPreviewPanel;
    // End of variables declaration//GEN-END:variables
}
