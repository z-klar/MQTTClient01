import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.logging.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.*;

public class frmMain {
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextField txServerUri;
    private JTextField txClientId;
    private JTextField txTopic;
    private JButton btnConnect;
    private JButton btnPublish;
    private JButton btnDisconnect;
    private JLabel lbConnState;
    private JList lbMainLog;
    private JButton btnClearMainLog;
    private JTextField txPayload;
    private JTextField txSubscribeTopic;
    private JButton btnSubscribe;

    private MqttClient mqtt_client;

    private DefaultListModel<String> dlmMainLog = new DefaultListModel<>();

    private Logger logger;

    private org.eclipse.paho.client.mqttv3.logging.Logger mqtt_logger;

    /*###################################################################

    ####################################################################*/
    public frmMain() {
        FileHandler fh;
        SimpleFormatter sf;

        $$$setupUI$$$();

        lbMainLog.setModel(dlmMainLog);

        logger = Logger.getLogger("Zdenda_MQTT");
        try {
            fh = new FileHandler("c:\\Temp\\Log\\mylog.txt", true);
        } catch (Exception ex) {
            LogException("LogFile", ex);
            return;
        }
        // Send logger output to our FileHandler.
        logger.addHandler(fh);
        // Request that every detail gets logged.
        logger.setLevel(Level.ALL);

        sf = new SimpleFormatter() {
            private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(format,
                        new Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        };
        fh.setFormatter(sf);

        btnConnect.addActionListener(e -> Connect());
        btnDisconnect.addActionListener(e -> Disconnect());
        btnClearMainLog.addActionListener(e -> dlmMainLog.clear());
        btnPublish.addActionListener(e -> Publish());
        btnSubscribe.addActionListener(e -> Subscribe());

        logger.log(Level.FINE, "Constructor completed");
    }

    /*------------------------------------------------------------------

    -------------------------------------------------------------------*/
    private void Subscribe() {
        MqttClient client;
        String topic;

        /*
        try {
            mqtt_client.subscribe(txSubscribeTopic.getText(), 1);
        }
        catch (Exception ex) {
            LogException("Construct client", ex);
        }
        */

        try {
            topic = txSubscribeTopic.getText();
            mqtt_client.subscribe(topic, 0, new mqttListener(dlmMainLog, topic, new clInterLogging(logger)));
            logger.log(Level.FINE, String.format("SUBSCRIBE: [%s]", topic));
        } catch (Exception ex) {
            LogException("Construct client", ex);
        }

    }

    /*------------------------------------------------------------------

    -------------------------------------------------------------------*/
    private void Publish() {

        MqttMessage message = new MqttMessage();
        message.setPayload(txPayload.getText().getBytes());
        try {
            mqtt_client.publish(txTopic.getText(), message);
            logger.log(Level.FINE, String.format("PUBLISH: [%s] : [%s]",
                    txTopic.getText(), txPayload.getText()));
        } catch (Exception ex) {
            LogException("PUBLISH", ex);
        }
    }

    /*------------------------------------------------------------------

    -------------------------------------------------------------------*/
    private void LogException(String source, Exception ex) {

        StackTraceElement[] trace;
        int i;
        dlmMainLog.addElement(String.format("%s: exception:", source));
        dlmMainLog.addElement(ex.getClass().toString());
        dlmMainLog.addElement(ex.getMessage());
        dlmMainLog.addElement("Stack Trace:");
        trace = ex.getStackTrace();
        i = 0;
        for (StackTraceElement se : trace) {
            dlmMainLog.addElement(String.format("Class:%s Method: %s Line: %d",
                    se.getClassName(), se.getMethodName(), se.getLineNumber()));
            i++;
            if (i > 20) break;
        }
    }

    /*------------------------------------------------------------------

    -------------------------------------------------------------------*/
    private void Disconnect() {

        try {
            mqtt_client.disconnect();
            lbConnState.setForeground(new Color(255, 0, 0));
            lbConnState.setText("DISCONNECTED");
            logger.log(Level.FINE, String.format("DISCONNECT"));
        } catch (Exception ex) {
            LogException("DISCONNECT", ex);
        }
    }

    /*------------------------------------------------------------------

    -------------------------------------------------------------------*/
    private void Connect() {

        try {
            mqtt_client = new MqttClient(txServerUri.getText(), txClientId.getText());

            mqtt_logger = LoggerFactory.getLogger("log_messages", "Zdenda_MQTT");

            ResourceBundle mybundle = ResourceBundle.getBundle("log_messages");
            mqtt_logger.initialise(mybundle, "Zdenda_MQTT", "RES01");


        } catch (Exception ex) {
            LogException("Construct client", ex);
        }
        try {
            /*
            mqtt_client.setCallback( new simpleMqttCallback(dlmMainLog) );
            */
            mqtt_client.connect();
            lbConnState.setForeground(new Color(0, 175, 0));
            lbConnState.setText("CONNECTED");
            logger.log(Level.FINE, String.format("CONNECT"));
        } catch (Exception ex) {
            LogException("CONNECT", ex);
        }
    }

    /*******************************************************************
     *
     * @param args
     *******************************************************************/
    public static void main(String[] args) {
        JFrame frame = new JFrame("frmMain");
        frame.setContentPane(new frmMain().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(new Dimension(900, 600));
        frame.setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        panel1.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(8, 6, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Main", panel2);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 12, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Server URI:");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txServerUri = new JTextField();
        Font txServerUriFont = this.$$$getFont$$$("Courier New", -1, 14, txServerUri.getFont());
        if (txServerUriFont != null) txServerUri.setFont(txServerUriFont);
        txServerUri.setText("tcp://localhost:1883");
        panel2.add(txServerUri, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 12, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Client ID:");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txClientId = new JTextField();
        Font txClientIdFont = this.$$$getFont$$$("Courier New", -1, 14, txClientId.getFont());
        if (txClientIdFont != null) txClientId.setFont(txClientIdFont);
        txClientId.setText("ZDENDA");
        panel2.add(txClientId, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$(null, Font.BOLD, 12, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Topic:");
        panel2.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txTopic = new JTextField();
        Font txTopicFont = this.$$$getFont$$$("Courier New", -1, 14, txTopic.getFont());
        if (txTopicFont != null) txTopic.setFont(txTopicFont);
        txTopic.setText("T01");
        panel2.add(txTopic, new GridConstraints(2, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(6, 1, 2, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        lbMainLog = new JList();
        Font lbMainLogFont = this.$$$getFont$$$("Courier New", -1, 12, lbMainLog.getFont());
        if (lbMainLogFont != null) lbMainLog.setFont(lbMainLogFont);
        scrollPane1.setViewportView(lbMainLog);
        btnConnect = new JButton();
        btnConnect.setText("CONNECT");
        panel2.add(btnConnect, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnPublish = new JButton();
        btnPublish.setText("PUBLISH");
        panel2.add(btnPublish, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lbConnState = new JLabel();
        Font lbConnStateFont = this.$$$getFont$$$(null, Font.BOLD, 14, lbConnState.getFont());
        if (lbConnStateFont != null) lbConnState.setFont(lbConnStateFont);
        lbConnState.setForeground(new Color(-65536));
        lbConnState.setText("DISCONNECTED");
        panel2.add(lbConnState, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDisconnect = new JButton();
        btnDisconnect.setText("DISCONNECT");
        panel2.add(btnDisconnect, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnClearMainLog = new JButton();
        btnClearMainLog.setText("Clear");
        panel2.add(btnClearMainLog, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$(null, Font.BOLD, 12, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Payload:");
        panel2.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txPayload = new JTextField();
        Font txPayloadFont = this.$$$getFont$$$("Courier New", -1, 14, txPayload.getFont());
        if (txPayloadFont != null) txPayload.setFont(txPayloadFont);
        txPayload.setText("Ahoj");
        panel2.add(txPayload, new GridConstraints(3, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$(null, Font.BOLD, 12, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Subscribe Topic:");
        panel2.add(label5, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txSubscribeTopic = new JTextField();
        Font txSubscribeTopicFont = this.$$$getFont$$$("Courier New", -1, 14, txSubscribeTopic.getFont());
        if (txSubscribeTopicFont != null) txSubscribeTopic.setFont(txSubscribeTopicFont);
        txSubscribeTopic.setText("T01");
        panel2.add(txSubscribeTopic, new GridConstraints(5, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnSubscribe = new JButton();
        btnSubscribe.setText("SUBSCRIBE");
        panel2.add(btnSubscribe, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

}
