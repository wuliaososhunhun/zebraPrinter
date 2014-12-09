package zebraPrinter.zebra;

import zebraPrinter.Connection;
import zebraPrinter.PrintException;
import zebraPrinter.PrinterStatus;
import zebraPrinter.label.LabelFormat;
import zebraPrinter.printer.Printer;
import zebraPrinter.printer.PrinterControlLanguage;

/**
 * Author: yanyang.wang
 * Date: 08/12/2014
 */
public class ZebraPrinterZpl implements Printer {
    private Connection connection;

    public ZebraPrinterZpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PrinterControlLanguage getPrinterControlLanguage() {
        return PrinterControlLanguage.ZPL;
    }

    @Override
    public PrinterStatus getCurrentStatus() throws PrintException {
        PrinterStatusZpl status = new PrinterStatusZpl(connection);
        return status;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void printLabel(LabelFormat format, byte[] label) throws PrintException {
        if (format == LabelFormat.EPL) {
            if (!connection.isConnected()) {
                throw new PrintException("connection is not open");
            } else {
                connection.write(label);
            }
        } else {
            throw new PrintException("Zebra zpl printer does not support: " + format);
        }
    }
}
