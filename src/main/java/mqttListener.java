import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.swing.*;

public class mqttListener implements IMqttMessageListener {

    private DefaultListModel dlmLog;
    private String _topic;
    private ICallBack1 log;

    public mqttListener(DefaultListModel dlm, String top, ICallBack1 l) {
        dlmLog = dlm;
        _topic = top;
        log = l;
    }

    public void 	messageArrived(String topic, MqttMessage msg) {
        String spom;
        spom = String.format("SUBSCRIBED Topic: %s   Payload: %s", topic, new String (msg.getPayload()));
        dlmLog.addElement(spom);
        log.Loguj(spom);
    }

}
