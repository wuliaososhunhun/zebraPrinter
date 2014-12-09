package zebraPrinter.printer;


import zebraPrinter.Connection;
import zebraPrinter.PrintException;
import zebraPrinter.zebra.ZebraPrinterZpl;

/**
 * Author: yanyang.wang
 * Date: 08/12/2014
 */
public class PrinterFactory {

    public static Printer getPrinterInstance(Connection connection, PrinterControlLanguage language) throws PrintException {
        Printer printer;
        switch (language) {
            case ZPL:
                printer = new ZebraPrinterZpl(connection);
                break;
            default:
                throw new PrintException("printer language is not support: " + language);
        }
        return printer;
    }
}
