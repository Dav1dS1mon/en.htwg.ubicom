/*
 * @Author:         David Simon
 * @Semester:       WS2015/2016
 * @Professor:      Professor Doctor Seepold
 * @Description:    Main activity - Menu with all option for the user.
 */
package ubicom.htwg.en.healthapp.Model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import ubicom.htwg.en.healthapp.Controller.BioHarnessBT.MainActivity;
import ubicom.htwg.en.healthapp.Controller.Recorder;
import ubicom.htwg.en.healthapp.R;


/*
 * @Description: Main activity with user options.
 */
public class menu extends AppCompatActivity {

    // Buttons from the menu
    private Button startMeasurmentButton;
    private Button viewMeasurmentButton;
    private Button diagrammButton;
    private Button infoButton;
    private Button closeButton;
    private long startTime;
    public static long stopTime;
    private static long diffTime;

    /*
     * @Description:         Init menu with all buttons with action and toast.
     * @Parameter:           Bundle savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final MainActivity bioHarnessSensor = new MainActivity();

        //Button: Start measurement with Bioharness3 sensor and start GPS service
        startMeasurmentButton = (Button)findViewById(R.id.start_button);
        startMeasurmentButton.setOnClickListener(new
                                                         View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 Toast.makeText(menu.this, "INFO: Bioharness 3 - PIN 1234",
                                                                         Toast.LENGTH_LONG).show();

                                                                 startService(new Intent(menu.this, Recorder.class));
                                                                 startTime = System.currentTimeMillis();

                                                                 startMeasurmentButton.setEnabled(false);
                                                                 viewMeasurmentButton.setEnabled(true);                                                                 diagrammButton.setEnabled(true);
                                                                 startActivity(new Intent(menu.this, MainActivity.class));
                                                             }
                                                         });

        //Button: Show route result in Google Map
        viewMeasurmentButton = (Button)findViewById(R.id.result_button);
        viewMeasurmentButton.setOnClickListener(new
                                                        View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v)
                                                            {
                                                                stopService(new Intent(menu.this, Recorder.class));
                                                                diffTime = stopTime - startTime;
                                                                startActivity(new Intent(menu.this, ubicom.htwg.en.healthapp.View.map.class));
                                                                startMeasurmentButton.setEnabled(true);
                                                            }
                                                        });

        //Button: Show line chart with Bioharness3 sensor
        diagrammButton = (Button)findViewById(R.id.button_diagramm);
        diagrammButton.setOnClickListener(new
                                                        View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v)
                                                            {
                                                                diffTime = stopTime - startTime;
                                                                startActivity(new Intent(menu.this, ubicom.htwg.en.healthapp.View.diagramm.class));
                                                                startMeasurmentButton.setEnabled(true);
                                                            }
                                                        });

        //Button: Show information about the auhtor and university
        infoButton = (Button)findViewById(R.id.info_button);
        infoButton.setOnClickListener(new
                                              View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v)
                                                  {
                                                      startActivity(new Intent(menu.this, ubicom.htwg.en.healthapp.View.information.class));
                                                  }
                                              });
        //Button: Close Application
        closeButton = (Button)findViewById(R.id.close_button);
        closeButton.setOnClickListener(new
                                               View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v)
                                                   {
                                                       AlertDialog.Builder builder = new AlertDialog.Builder(menu.this);
                                                       builder.setTitle("Close Application?");
                                                       builder.setMessage("Do you want to close this application?");
                                                       builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                                           @Override
                                                           public void onClick(DialogInterface dialog, int which) {
                                                               finish();
                                                               System.exit(1);
                                                               dialog.dismiss();
                                                           }

                                                       });
                                                       builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                                           @Override
                                                           public void onClick(DialogInterface dialog, int which) {
                                                               Toast.makeText(menu.this, "INFO: App not closed",
                                                                       Toast.LENGTH_LONG).show();
                                                               dialog.dismiss();
                                                           }

                                                       });
                                                       AlertDialog alert = builder.create();
                                                       alert.show();
                                                   }
                                               });
    }

    /*
     * @Description:    Init menu options
     * @Parameter:      Menu-Variable
     * @Return:         Boolean Variable - true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    /*
     * @Description:    Option Item
     * @Param:          MenuItem
     * @Return:         Boolean - If select option goes right
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * @Description:    Method to return the difference time for time measurement
     * @Return:         Long diffTime - Difference time from measurement
     */
    public static long getDiffTime() {
        return diffTime;
    }
}
