package zebraPrinter.zebra;


import zebraPrinter.Connection;
import zebraPrinter.PrintException;
import zebraPrinter.PrinterStatus;
import zebraPrinter.ZplUtility;

/**
 * Author: yanyang.wang
 * Date: 08/12/2014
 */
public class PrinterStatusZpl implements PrinterStatus {
    public final static byte INTERNAL_LINE_SEPARATOR_CHAR = (byte)3;
    public final static byte READABLE_LINE_SEPARATOR_CHAR = ',';
    private ZplPrintMode printMode;
    private int labelLengthInDots;
    private int numberOfFormatsInReceiveBuffer;
    private int labelsRemainingInBatch;
    private boolean isPartialFormatInProgress;
    private boolean isHeadCold;
    private boolean isHeadOpen;
    private boolean isHeadTooHot;
    private boolean isPaperOut;
    private boolean isRibbonOut;
    private boolean isReceiveBufferFull;
    private boolean isPaused;
    private boolean isReadyToPrint;
    private Connection connection;
    private boolean statusHasBeenRetrievedFromPrinter = false;

    public PrinterStatusZpl(Connection connection) throws PrintException {
        this.connection = connection;
        this.numberOfFormatsInReceiveBuffer = 0;
        this.labelsRemainingInBatch = 0;
        this.isPartialFormatInProgress = false;
        this.isHeadCold = false;
        this.printMode = ZplPrintMode.UNKNOWN;
        this.labelLengthInDots = 0;
        this.getStatusFromPrinter();
    }

    private void getStatusFromPrinter() throws PrintException {
        if (!this.statusHasBeenRetrievedFromPrinter) {
            updateStatus();
            statusHasBeenRetrievedFromPrinter = true;
            isReadyToPrint = !isPaperOut;
            isReadyToPrint &= !isPaused;
            isReadyToPrint &= !isReceiveBufferFull;
            isReadyToPrint &= !isHeadTooHot;
            isReadyToPrint &= !isHeadOpen;
            isReadyToPrint &= !isRibbonOut;
        }
    }

    public void updateStatus() throws PrintException {
        String[] messages = this.getPrinterStatus();
        this.labelsRemainingInBatch = Integer.parseInt(messages[20]);
        this.numberOfFormatsInReceiveBuffer = Integer.parseInt(messages[4]);
        this.isPartialFormatInProgress = messages[7].equals("1");
        this.isHeadCold = messages[10].equals("1");
        this.isHeadOpen = messages[14].equals("1");
        this.isHeadTooHot = messages[11].equals("1");
        this.isPaperOut = messages[1].equals("1");
        this.isRibbonOut = messages[15].equals("1");
        this.isReceiveBufferFull = messages[5].equals("1");
        this.isPaused = messages[2].equals("1");
        this.labelLengthInDots = Integer.parseInt(messages[3]);
        this.printMode = getPrintModeFromHs(messages[17].charAt(0));
    }

    public String[] getPrinterStatus() throws PrintException {
        byte[] response = connection.sendAndWaitForResponse(ZplUtility.PRINTER_STATUS.getBytes(), null);
        StringBuffer buffer = new StringBuffer();
        String[] messages = new String[0];
        if(response != null) {
            for(int i = 0; i < response.length; ++i) {
                if(response[i] == INTERNAL_LINE_SEPARATOR_CHAR) {
                    response[i] = READABLE_LINE_SEPARATOR_CHAR;
                }
                if(response[i] > 31 && response[i] < 127) {
                    buffer.append((char)response[i]);
                }
            }
        }

        if(buffer.length() >= 1) {
            messages = buffer.toString().split(String.valueOf((char)READABLE_LINE_SEPARATOR_CHAR));
        }

        if(messages.length < 25) {
            throw new PrintException("Malformed status response - unable to determine printer status");
        }
        return messages;
    }

    private static ZplPrintMode getPrintModeFromHs(char var0) {
        char var1 = String.valueOf(var0).toUpperCase().charAt(0);
        switch(var1) {
            case '0':
                return ZplPrintMode.REWIND;
            case '1':
                return ZplPrintMode.PEEL_OFF;
            case '2':
                return ZplPrintMode.TEAR_OFF;
            case '3':
                return ZplPrintMode.CUTTER;
            case '4':
                return ZplPrintMode.APPLICATOR;
            case '5':
                return ZplPrintMode.DELAYED_CUT;
            case '6':
                return ZplPrintMode.LINERLESS_PEEL;
            case '7':
                return ZplPrintMode.LINERLESS_REWIND;
            case '8':
                return ZplPrintMode.PARTIAL_CUTTER;
            case '9':
                return ZplPrintMode.RFID;
            case ':':
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            default:
                return ZplPrintMode.UNKNOWN;
            case 'K':
                return ZplPrintMode.KIOSK;
        }
    }

    public ZplPrintMode getPrintMode() {
        return printMode;
    }

    public int getLabelLengthInDots() {
        return labelLengthInDots;
    }

    public int getNumberOfFormatsInReceiveBuffer() {
        return numberOfFormatsInReceiveBuffer;
    }

    public int getLabelsRemainingInBatch() {
        return labelsRemainingInBatch;
    }

    public boolean isPartialFormatInProgress() {
        return isPartialFormatInProgress;
    }

    public boolean isHeadCold() {
        return isHeadCold;
    }

    public boolean isHeadOpen() {
        return isHeadOpen;
    }

    public boolean isHeadTooHot() {
        return isHeadTooHot;
    }

    public boolean isPaperOut() {
        return isPaperOut;
    }

    public boolean isRibbonOut() {
        return isRibbonOut;
    }

    public boolean isReceiveBufferFull() {
        return isReceiveBufferFull;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isReadyToPrint() {
        return isReadyToPrint;
    }
}
