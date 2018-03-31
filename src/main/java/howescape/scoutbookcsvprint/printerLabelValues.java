/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package howescape.scoutbookcsvprint;

/**
 *
 * @author Peter Maronda
 */
public class printerLabelValues {
    String  labelName;
    String  labelSize;
    String  labelDescription;
    // Margin
    String  marginSize;
    // Label-Rectangle
    int     id;
    String  radius;
    String  width;
    String  height;
    String  round;
    String  x_waste;
    String  y_waste;
    // Layout
    int     numberX;
    int     numberY;
    String  x0;
    String  y0;
    String  deltaX;
    String  deltaY;
    // 
    // avery lable info in points 72pt = 1 inch
    // Left margin 0.50"    Top  0.55"
    // right Margin 0.25"   bottom 0.50"
    //
    // Horizontal 1.94"    Vertical 1.25"
    
    
    public void setName(String inName) {
        labelName = inName;
    }
    public String getName() {
        return labelName;
    }
          
    public void setSize(String inSize) {
        labelSize = inSize;
    }
    
    public void setDescription(String inDescription) {
        labelDescription = inDescription;
    }
    
    public void setId (int inId) {
        id = inId;
    }
    
    public void setRadius (String inRadius) {
        radius = inRadius;
        width = null;
        height = null;
        round = null;
    }
    public void setWidth (String inWidth) {
        width = inWidth;
        radius = null;
    }
    public String getWidth() {
        return width;
    }
    public float getWidthFloat() {
        float widthValue = 0.0f;
        int indexUnit = width.indexOf("in");
        if (indexUnit>2) {
            String getDigits = width.substring(0,indexUnit);
            widthValue = (float) Double.parseDouble(getDigits);
        } else if ((indexUnit = width.indexOf("pt")) > 2) {
            String getDigits = width.substring(0,indexUnit);
            widthValue = (float) Double.parseDouble(getDigits);
        }
        return widthValue;        
    }

    public boolean isWidthInch() {
        boolean answer = true;
        if (width.indexOf("in")<2) {
            answer = false;
        }
        return answer;
    }
    public boolean isWidthPts() {
        boolean answer = true;
        if (width.indexOf("pt")<2) {
            answer =false;
        }
        return answer;
    }

    public void setHeight (String inHeight) {
        height = inHeight;
        radius = null;
    }
    public float getHeightFloat() {
        float heightValue = 0.0f;
        int indexUnit = height.indexOf("in");
        if (indexUnit>-1) {
            String getDigits = height.substring(0,indexUnit);
            heightValue = (float) Double.parseDouble(getDigits);
        } else if ((indexUnit = height.indexOf("pt")) > -1) {
            String getDigits = height.substring(0,indexUnit);
            heightValue = (float) Double.parseDouble(getDigits);
        }
        return heightValue;                
    }
    public boolean isHeightInch() {
        boolean answer = true;
        if (height.indexOf("in")<2) {
            answer = false;
        }
        return answer;
    }
    public boolean isHeightPts() {
        boolean answer = true;
        if (height.indexOf("pt")<2) {
            answer =false;
        }
        return answer;
    }
    
    public void setRound (String inRound) {
        round = inRound;
        radius = null;
    }

    public void setXWaste(String inXwaste) {
        x_waste = inXwaste;
    }
    public float getXWaste() {
        float x_wasteValue = 0.0f;
        int indexUnit = x_waste.indexOf("in");
        if (indexUnit>2) {
            String getDigits = x_waste.substring(0,indexUnit);
            x_wasteValue = (float) Double.parseDouble(getDigits);
        } else if ((indexUnit = x_waste.indexOf("pt")) > 2) {
            String getDigits = x_waste.substring(0,indexUnit);
            x_wasteValue = (float) Double.parseDouble(getDigits);
        }
        return x_wasteValue;                        
    }
    public void setYWaste(String inYwaste) {
        y_waste = inYwaste;
    }
    public float getYWaste() {
        float x_wasteValue = 0.0f;
        int indexUnit = y_waste.indexOf("in");
        if (indexUnit>2) {
            String getDigits = y_waste.substring(0,indexUnit);
            x_wasteValue = (float) Double.parseDouble(getDigits);
        } else if ((indexUnit = y_waste.indexOf("pt")) > 2) {
            String getDigits = y_waste.substring(0,indexUnit);
            x_wasteValue = (float) Double.parseDouble(getDigits);
        }
        return x_wasteValue;                        
    }    
    public void setNumberX (int inX) {
        numberX = inX;
    }
    public int getNumberX() {
        return numberX;
    }
    
    public void setNumberY (int inY) {
        numberY = inY;
    }
    public int getNumberY () {
        return numberY;
    }

    public void setX0 (String inX0) {
        x0 = inX0;
    }
    public float getX0() {
        int indexUnit = x0.indexOf("pt");
        if (indexUnit>=1) {
            String getDigits = x0.substring(0,indexUnit);
            return (float) Double.parseDouble(getDigits);
        }
        return 0.0f;        
    }
    
    public void setY0 (String inY0) {
        y0 = inY0;
    }
    public float getY0() {
        int indexUnit = y0.indexOf("pt");
        if (indexUnit>=1) {
            String getDigits = y0.substring(0,indexUnit);
            return (float) Double.parseDouble(getDigits);
        }
        return 0.0f;        
    }    
    public void setDeltaX (String inDeltaX) {
        deltaX = inDeltaX;
    }
    public float getDeltaX () {
        int indexUnit = deltaX.indexOf("pt");
        if (indexUnit>=1) {
            String getDigits = deltaX.substring(0,indexUnit);
            return (float) Double.parseDouble(getDigits);
        }
        return 0.0f;
    }
    
    public void setDeltaY (String inDeltaY) {
        deltaY = inDeltaY;
    }
    public float getDeltaY () {
        int indexUnit = deltaY.indexOf("pt");
        if (indexUnit>=1) {
            String getDigits = deltaY.substring(0,indexUnit);
            return (float) Double.parseDouble(getDigits);
        }
        return 0.0f;        
    }
    
    public void setMargin (String inMargin) {
    marginSize = inMargin;
    }
    public float getMarginFloat() {
        int indexUnit = marginSize.indexOf("in");
        if (indexUnit>=1) {
            String getDigits = marginSize.substring(0,indexUnit);
            return (float) Double.parseDouble(getDigits);
        }
        return 0.0f;
    }
    public boolean isMarginUnitPT() {
        boolean answer = true;
        if (marginSize.indexOf("pt")<2) {
            answer = false;
        }
        return answer;
    }
    public int getMarginInPT() {
        if (this.isMarginUnitPT()) {
            int indexUnit = marginSize.indexOf("pt");
            String getDigits = marginSize.substring(0,indexUnit);
            return Integer.parseInt(getDigits);
        }
        return 0;
    }
}
