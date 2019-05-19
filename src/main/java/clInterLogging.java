import java.util.logging.Level;
import java.util.logging.Logger;

public class clInterLogging implements ICallBack1 {
    private Logger log;

    public clInterLogging(Logger l) {
        log = l;
    }

    public void Loguj(String message) {
        log.log(Level.FINE, message);

    }
}
