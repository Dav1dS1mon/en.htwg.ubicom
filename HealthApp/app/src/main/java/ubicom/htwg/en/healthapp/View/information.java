package ubicom.htwg.en.healthapp.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ubicom.htwg.en.healthapp.R;

/*
 * @Author:         David Simon
 * @Semester:       WS2015/2016
 * @Professor:      Professor Doctor Seepold
 * @Description:    Information Activity
 */
public class information extends Activity {

    //Button variable
    private Button okButton;

    /*
     * @Description:    Init Information activity
     * @Param:          Bundle - TODO
     */
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_information);

        okButton = (Button)findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}
