package zebraPrinter;

import zebraPrinter.label.LabelFormat;
import zebraPrinter.printer.Printer;
import zebraPrinter.printer.PrinterControlLanguage;
import zebraPrinter.printer.PrinterFactory;
import zebraPrinter.zebra.PrinterStatusZpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Author: yanyang.wang
 * Date: 09/12/2014
 */
public class PrintLabel {
    public static void main(String[] args) throws PrintException, IOException {
        Connection connection = new TcpConnection("10.252.65.227", 6101);
        connection.open();
        Printer printer = PrinterFactory.getPrinterInstance(connection, PrinterControlLanguage.ZPL);
        PrinterStatus status = printer.getCurrentStatus();
        if (status instanceof PrinterStatusZpl) {
            PrinterStatusZpl statusZpl = (PrinterStatusZpl) status;
            System.out.println("length in dot: " + statusZpl.getLabelLengthInDots());
            System.out.println("read to print: " + statusZpl.isReadyToPrint());
        } else {
            System.out.println("unexpected status");
        }
        printer.printLabel(LabelFormat.EPL, Files.readAllBytes(Paths.get("classpath:epl.txt")));
        connection.close();
    }
}
