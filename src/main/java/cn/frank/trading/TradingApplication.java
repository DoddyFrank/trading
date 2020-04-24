package cn.frank.trading;

import cn.frank.trading.view.TradingView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TradingApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(TradingApplication.class, TradingView.class, args);
    }

}
