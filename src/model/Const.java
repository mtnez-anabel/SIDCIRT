package model;

/**
 *
 * @author anabel
 */
public class Const {    
    public static final String MODEL = "model";
    public static final String INDICATOR = "indicator";
    public static final String SUBINDICATOR = "subindicator";
    public static final String VALUE = "value";
    public static final String NAME = "name";
    public static final String COMMENT = "comment";
    public static final String MIN = "min";
    public static final String MAX = "max";
    public static final String INI = "ini";
    
    public static final String CONFIG_DIR = "conf/"; 
    
    public static final String PROPERTIES = CONFIG_DIR + "properties.xml";
    
    public static final String EXTENSION = ".sidcirt.xml";
    
    public static final String HELP_FILE = "doc/help.html";
    
    public static final String ACCEPT = "Aceptar";
    public static final String CANCEL = "Cancelar";
    
    public static String aboutTitle = "Acerca de " + System.getProperty("app.acronym");
    public static String aboutMessage =                 
                "Nombre:  " + System.getProperty("app.name") + " (" + System.getProperty("app.acronym") + ")\n" +
                "Versión:  " + System.getProperty("app.version") + "\n" +
                "Autor:  " + System.getProperty("app.author") + "\n" +
                "Organismo:  " + System.getProperty("app.org") + "\n" +
                "\n" +
                "Plataforma Java en uso" + "\n" + 
                "Versión:  " + System.getProperty("java.version") + "\n" +
                "Máquina Virtual:  " + System.getProperty("java.vm.name") + "\n" +
                "\n" +
                "Sistema operativo en uso" + "\n" +
                "Nombre:  " + System.getProperty("os.name") + "\n" +
                "Arquitectura:  " + System.getProperty("os.arch");
    
    public static String helpMessage = "La ayuda de la aplicación se encuentra en el archivo \n\"" +
                                       "help.html\" del propio directorio de la aplicación.";
}
