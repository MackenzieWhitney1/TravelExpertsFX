package org.example.travelexpertsfx;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.util.ArrayList;

public class Validator {
    public static boolean validateNonEmptyEntry(TextField textField){
        String value = textField.getText();
        return value != null && !value.trim().isEmpty();
    }
    public static boolean validateNonEmptyWithinLength(TextField textField,int maxLength){
        if(!validateNonEmptyEntry(textField))return false;
        if(textField.getText().length()>maxLength)return false;
        return true;
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

    public static boolean validateNonEmptyPositiveInteger(TextField textField){
        String value = textField.getText();
        try{
            double parsedValue = Integer.parseInt(value);
            if(parsedValue < 0){
                return false;
            }
        } catch (NumberFormatException e){
            return false;
        }
        return validateNonEmptyEntry(textField);
    }

    public static boolean validateDoubleHasTwoDecimalPrecision(TextField textField){
        String value = textField.getText();
        return value.matches("^\\d+(\\.\\d{1,2})?$");
    }

    public static boolean validateDoubleLessThanMax(TextField textField, Double max){
        return Double.parseDouble(textField.getText()) < max;
    }
    public static boolean validateDateSelected(DatePicker datePicker){
        return datePicker.getValue() != null;
    }

    public static boolean validateDateBeforeOtherDate(DatePicker startDatePicker, DatePicker endDatePicker){
        return (endDatePicker.getValue()).isAfter(startDatePicker.getValue());
    }

    public static boolean validateDateAfterNow(DatePicker datePicker){
        return (datePicker.getValue()).isAfter(LocalDate.now());
    }

}
