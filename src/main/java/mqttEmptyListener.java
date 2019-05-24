import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class mqttEmptyListener implements IMqttMessageListener {

    public void 	messageArrived(String topic, MqttMessage msg) {

    }

}
