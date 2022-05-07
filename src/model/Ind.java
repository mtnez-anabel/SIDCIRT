package model;

/**
 * Enumeracion que identifica los indicadores
 * @author Anabel
 */
public enum Ind{
     FALTA_H("FALTA_H"), 
     FALTA_R("FALTA_R"), 
     FREC_DE("FREC_DE"), 
     FREC_ES("FREC_ES"), 
     FREC_EX("FREC_EX"), 
     FREC_IN("FREC_IN"), 
     VULN_DE("VULN_DE"), 
     VULN_ES("VULN_ES"), 
     VULN_EX("VULN_EX"), 
     VULN_IN("VULN_IN");

    private Ind(String name) {
        this.name = name;
    }
     
    private final String name;     
}