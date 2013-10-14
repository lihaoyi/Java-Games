import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
public class FilePicker{
    public static String userGetFile(String Title, String InitialValue, Component Parent){
        
        try{
            JFileChooser Chooser = new JFileChooser(System.getProperty("user.dir"));
            Chooser.setDialogTitle(Title);
            Chooser.setSelectedFile(new File(InitialValue));
        
            if(Chooser.showDialog(Parent, "Accept") == 1){
                return null;
            }else{
                String Path = Chooser.getSelectedFile().getCanonicalPath();
                return Path;
            }
        }catch(Exception e){
            System.out.println("PICKEREXCEPTION:" + e);
            return null;
        }
    }
}