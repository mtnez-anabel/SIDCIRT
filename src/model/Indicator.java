package model;

public class Indicator {    
    public String name;
    public SubIndicator[] subInd;

    public Indicator(String name, SubIndicator[] subInd) {
        this.name = name;
        this.subInd = subInd;
    }
}
