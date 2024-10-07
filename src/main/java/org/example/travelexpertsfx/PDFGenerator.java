package org.example.travelexpertsfx;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class PDFGenerator {

    public static void generateInvoice(String dest) throws FileNotFoundException, MalformedURLException {
        // Create a PDF writer instance
        PdfWriter writer = new PdfWriter(dest);

        // Initialize the PDF document and set page size
        PdfDocument pdfDoc = new PdfDocument(writer);
        pdfDoc.setDefaultPageSize(PageSize.A4);

        // Create a Document layout object
        Document document = new Document(pdfDoc);

        // Add invoice title
        document.add(new Paragraph("INVOICE")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
        );

        // Create a table with 2 columns for company and client information
        float[] columnWidths = {1, 1}; // Two equal-width columns
        Table infoTable = new Table(columnWidths);
        infoTable.setWidth(UnitValue.createPercentValue(100)); // Set table width to 100%
        infoTable.setFixedLayout();

        // Add company information in the left column
        infoTable.addCell(new Cell()
                .add(new Paragraph("Company Name: XYZ Corporation"))
                .add(new Paragraph("Address: 1234 Main Street, City, Country"))
                .add(new Paragraph("Phone: (123) 456-7890"))
                .setTextAlignment(TextAlignment.LEFT)
        );

        // Add client information in the right column
        infoTable.addCell(new Cell()
                .add(new Paragraph("Bill To:"))
                .add(new Paragraph("Client Name: John Doe"))
                .add(new Paragraph("Client Address: 7890 Elm Street, City, Country"))
                .add(new Paragraph("Client Email: john.doe@example.com"))
                .setTextAlignment(TextAlignment.RIGHT)
        );

        // Add the info table to the document
        document.add(infoTable);

        // Add invoice details
        document.add(new Paragraph("\nInvoice Number: INV-1001"));
        document.add(new Paragraph("Invoice Date: 2024-10-04\n"));

        // Create a table with 5 columns for line items
        float[] itemColumnWidths = {1, 3, 1, 2, 2};
        Table table = new Table(itemColumnWidths);
        table.setWidth(UnitValue.createPercentValue(100)); // Set table width to 100%

        // Add table headers
        table.addCell(new Cell().add(new Paragraph("Item").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph("Description").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph("Quantity").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph("Unit Price").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addCell(new Cell().add(new Paragraph("Total").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Add sample data (line items)
        table.addCell(new Cell().add(new Paragraph("1")));
        table.addCell(new Cell().add(new Paragraph("Web Development Services")));
        table.addCell(new Cell().add(new Paragraph("2")));
        table.addCell(new Cell().add(new Paragraph("$750.00")));
        table.addCell(new Cell().add(new Paragraph("$1500.00")));

        // Add table to document
        document.add(table);

        // Add total amount due
        document.add(new Paragraph("\nTotal Amount Due: $1500.00").setBold());

        // Add payment details
        document.add(new Paragraph("\nPayment Details:"));
        document.add(new Paragraph("Bank: ABC Bank"));
        document.add(new Paragraph("Account Number: 1234567890"));
        document.add(new Paragraph("SWIFT Code: ABCD1234"));

        // Add closing note
        document.add(new Paragraph("\nThank you for your business!"));

        // Close the document
        document.close();
    }
}
