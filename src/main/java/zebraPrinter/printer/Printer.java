package zebraPrinter.printer;

import zebraPrinter.Connection;
import zebraPrinter.PrintException;
import zebraPrinter.PrinterStatus;
import zebraPrinter.label.LabelFormat;

/**
 * Author: yanyang.wang
 * Date: 08/12/2014
 */
public interface Printer {
    PrinterControlLanguage getPrinterControlLanguage();

    PrinterStatus getCurrentStatus() throws PrintException;

    Connection getConnection();

    void printLabel(LabelFormat format, byte[] label) throws PrintException;
}
