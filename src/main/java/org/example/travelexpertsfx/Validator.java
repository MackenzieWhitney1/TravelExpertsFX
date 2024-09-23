package org.example.travelexpertsfx;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class Validator {
    public static boolean validateNonEmptyEntry(TextField textField){
        String value = textField.getText();
        return value != null && !value.trim().isEmpty();
    }
    public static boolean validateEntryNotInList(TextField textField, ArrayList<String> list){
        String value = textField.getText();
        return !list.contains(value);
    }
    public static boolean validateNonEmptyPositiveDouble(TextField textField){
        String value = textField.getText();
        try{
            double parsedValue = Double.parseDouble(value);
            if(parsedValue < 0){
                return false;
            }
        } catch (NumberFormatException e){
            return false;
        }
        return validateNonEmptyEntry(textField);
    }

}
