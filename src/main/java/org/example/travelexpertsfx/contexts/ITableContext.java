package org.example.travelexpertsfx.contexts;

import org.example.travelexpertsfx.Mode;

public interface ITableContext {
    // Read - used to populate the table view
    public void displayTableContent();
    public void openDialog(Object obj, Mode mode);
    public void setupTableColumns();

}
