package zebraPrinter;

/**
 * Author: yanyang.wang
 * Date: 09/12/2014
 */
public class ZplUtility {
    public static final int ZPL_INTERNAL_FORMAT_PREFIX_CHAR = 30;
    public static final int ZPL_INTERNAL_COMMAND_PREFIX_CHAR = 16;
    public static final int ZPL_INTERNAL_DELIMITER_CHAR = 31;
    public static final String ZPL_INTERNAL_FORMAT_PREFIX = new String(new byte[]{(byte) 30});
    public static final String ZPL_INTERNAL_COMMAND_PREFIX = new String(new byte[]{(byte) 16});
    public static final String ZPL_INTERNAL_DELIMITER = new String(new byte[]{(byte) 31});
    public static final String PRINTER_INFO = new String(new byte[]{(byte) 16, (byte) 72, (byte) 73});
    public static final String PRINTER_STATUS = new String(new byte[]{(byte) 16, (byte) 72, (byte) 83});
    public static final String PRINTER_CONFIG_LABEL = new String(new byte[]{(byte) 16, (byte) 87, (byte) 67});
    public static final String PRINTER_DIRECTORY_LABEL = new String(new byte[]{(byte) 30, (byte) 88, (byte) 65, (byte) 30, (byte) 87, (byte) 68, (byte) 42, (byte) 58, (byte) 42, (byte) 46, (byte) 42, (byte) 30, (byte) 88, (byte) 90});
    public static final String PRINTER_NETWORK_CONFIG_LABEL = new String(new byte[]{(byte) 16, (byte) 87, (byte) 76});
    public static final String PRINTER_CALIBRATE = new String(new byte[]{(byte) 16, (byte) 74, (byte) 67});
    public static final String PRINTER_RESET = new String(new byte[]{(byte) 16, (byte) 74, (byte) 82});
    public static final String PRINTER_RESET_NETWORK = new String(new byte[]{(byte) 16, (byte) 87, (byte) 82});
    public static final String PRINTER_RESTORE_DEFAULTS = new String(new byte[]{(byte) 30, (byte) 88, (byte) 65, (byte) 30, (byte) 74, (byte) 85, (byte) 70, (byte) 30, (byte) 88, (byte) 90});
    public static final String PRINTER_GET_SUPER_HOST_STATUS = new String(new byte[]{(byte) 30, (byte) 88, (byte) 65, (byte) 30, (byte) 72, (byte) 90, (byte) 65, (byte) 30, (byte) 88, (byte) 90});
    public static final String PRINTER_GET_DIR_LISTING_XML = new String(new byte[]{(byte) 30, (byte) 88, (byte) 65, (byte) 30, (byte) 72, (byte) 90, (byte) 76, (byte) 30, (byte) 88, (byte) 90});
    public static final String PRINTER_GET_STORAGE_INFO_COMMAND = new String(new byte[]{(byte) 30, (byte) 88, (byte) 65, (byte) 30, (byte) 72, (byte) 87, (byte) 42, (byte) 58, (byte) 88, (byte) 88, (byte) 88, (byte) 88, (byte) 46, (byte) 81, (byte) 81, (byte) 81, (byte) 30, (byte) 88, (byte) 90});

    public static String decorateWithCommandPrefix(String message) {
        return message == null ? null : (message.indexOf("~") != -1 ? message.replace('~', '\u0010') : ZPL_INTERNAL_COMMAND_PREFIX + message);
    }

    public static String decorateWithFormatPrefix(String message) {
        return message == null ? null : (message.indexOf("^") != -1 ? message.replace('^', '\u001e') : ZPL_INTERNAL_FORMAT_PREFIX + message);
    }

    public static String replaceAllWithInternalDelimeter(String message) {
        return message == null ? null : message.replaceAll(",", ZPL_INTERNAL_DELIMITER);
    }

    public static String replaceAllWithInternalCharacters(String message) {
        return message == null ? null : replaceAllWithInternalDelimeter(message).replace("^", ZPL_INTERNAL_FORMAT_PREFIX).replace("~", ZPL_INTERNAL_COMMAND_PREFIX);
    }

    public static String replaceInternalCharactersWithReadableCharacters(String message) {
        return message.replaceAll(ZPL_INTERNAL_COMMAND_PREFIX, "~").replaceAll(ZPL_INTERNAL_FORMAT_PREFIX, "^").replaceAll(ZPL_INTERNAL_DELIMITER, ",");
    }

    public static byte[] replaceInternalCharactersWithReadableCharacters(byte[] message) {
        for (int var1 = 0; var1 < message.length; ++var1) {
            if (message[var1] == 16) {
                message[var1] = 126;
            } else if (message[var1] == 31) {
                message[var1] = 44;
            } else if (message[var1] == 30) {
                message[var1] = 94;
            }
        }
        return message;
    }
}
