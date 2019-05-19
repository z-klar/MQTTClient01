import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.swing.*;
import java.util.ArrayList;

public class simpleMqttCallback implements MqttCallback {

    private DefaultListModel<String> dlmLog;
    private ArrayList<String> alRes;

    public simpleMqttCallback(DefaultListModel dlm) {
        dlmLog = dlm;
        //alRes = al;
    }

    public void connectionLost(Throwable throwable) {
        dlmLog.addElement("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String spom = String.format("ID=%d  PAYLOAD=%s" ,
                        mqttMessage.getId(), new String(mqttMessage.getPayload()));
        dlmLog.addElement(spom);
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        dlmLog.addElement("... Delivery completed");
    }

}
