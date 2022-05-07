package gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Anabel
 */
public class ModelFileFilter extends FileFilter{
    @Override
    public boolean accept(File pathname) {
        return (pathname.isDirectory() || pathname.getAbsolutePath().endsWith(".sidcirt.xml"));
    }
    @Override
    public String getDescription() {
        return "Modelo de " + System.getProperty("app.acronym") + " (*.sidcirt.xml)";
    }
    
}
