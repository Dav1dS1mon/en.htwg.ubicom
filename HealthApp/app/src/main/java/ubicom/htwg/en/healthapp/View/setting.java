package ubicom.htwg.en.healthapp.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ubicom.htwg.en.healthapp.R;

/**
 * Created by Dave on 21.01.2016.
 */
public class setting extends Activity {
    //Button variable
    private Button okButton;

    /*
     * @Description:    Init Information activity
     * @Param:          Bundle - TODO
     */
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_settings);

        /*okButton = (Button)findViewById(R.id.okSetting_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });*/
    }
}
