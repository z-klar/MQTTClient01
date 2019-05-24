import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class posluchac implements ActionListener {

    private timerInfo timer_info;
    private int nCounter;

    public posluchac(timerInfo ti) {
        nCounter = 0;

        timer_info = ti;
    }

    public void actionPerformed(ActionEvent e) {
        // code to be done during timer event
        nCounter++;
        timer_info.textField.setText(String.format("Counter: %d   ", nCounter));
    }
}




