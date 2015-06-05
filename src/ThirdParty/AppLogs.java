/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ThirdParty;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author Doctormaliko
 */
public class AppLogs extends OutputStream {

    private JTextPane textPane;
    private JTextArea textArea;
    private final static int TEXTAREA = 1;
    private final static int TEXTPANE = 2;
    private final int LOG_COMPONENT;

    public AppLogs(JTextPane textPane) {
        this.textPane = textPane;
        LOG_COMPONENT = TEXTPANE;
    }

    public AppLogs(JTextArea textArea) {
        this.textArea = textArea;
        LOG_COMPONENT = TEXTAREA;
    }

    @Override
    public void write(final int b) throws IOException {
        switch (LOG_COMPONENT) {
            case TEXTPANE:
                updateTextPane(String.valueOf((char) b));

                break;
            case TEXTAREA:
                updateTextArea(String.valueOf((char) b));
                break;
        }

    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        switch (LOG_COMPONENT) {
            case TEXTPANE:
                updateTextPane(new String(b, off, len));
                break;
            case TEXTAREA:
                updateTextArea(new String(b, off, len));
                break;
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    private void updateTextPane(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Document doc = textPane.getDocument();
                try {
                    doc.insertString(doc.getLength(), text, null);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
                textPane.setCaretPosition(doc.getLength() - 1);
            }
        });
    }

    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                textArea.append(text);
            }
        });
    }
}
