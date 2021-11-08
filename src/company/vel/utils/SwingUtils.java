package company.vel.utils;

import javax.swing.*;

public class SwingUtils
{
    private static void showErrorMessageBox(String message, String title, Throwable ex)
    {
        StringBuilder sb = new StringBuilder(ex.toString());
        if (message != null)
        {
            sb.append(message);
        }
        if (ex != null)
        {
            sb.append("\n");
            for (StackTraceElement ste : ex.getStackTrace())
            {
                sb.append("\n\tat ");
                sb.append(ste);
            }
        }
        JOptionPane.showMessageDialog(null, sb.toString(), title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorMessageBox(Throwable ex)
    {
        showErrorMessageBox(null, "Error", ex);
    }
}
