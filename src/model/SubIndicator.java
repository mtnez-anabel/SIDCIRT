package model;

/**
 *
 * @author Anabel
 */
public class SubIndicator{
    public String name = "";
    public String comment = "";
    public double min = 0.0f, max = 0.0f, ini = 0.0f;  

    public SubIndicator(String name, String comment, double min, double max, double inicial) {
        this.name = name;
        this.comment = comment;
        this.min = min;
        this.max = max;
        this.ini = inicial;
    }    
    
    public SubIndicator(String name, String comment, double min, double max) {
        this(name, comment, min, max, (min+max)/2);
    } 
}