package ubicom.htwg.en.healthapp.Model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import ubicom.htwg.en.healthapp.Controller.Recorder;
import ubicom.htwg.en.healthapp.R;

/*
 * @Author:         David Simon
 * @Semester:       WS2015/2016
 * @Professor:      Professor Doctor Seepold
 * @Description:    Main activity - Menu with all option for the user.
 */
public class menu extends AppCompatActivity {

    // Buttons from the menu
    private Button startMeasurmentButton;
    private Button stopMeasurmentButton;
    private Button viewMeasurmentButton;
    private Button personalButton;
    private Button infoButton;
    private Button closeButton;

    /*
     * @Description:         Init menu with all buttons with action and toast.
     * @Parameter:           TODO: savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        startMeasurmentButton = (Button)findViewById(R.id.start_button);
        startMeasurmentButton.setOnClickListener(new
                                                         View.OnClickListener() {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 Toast.makeText(menu.this, "INFO: Start measurment",
                                                                         Toast.LENGTH_LONG).show();
                                                                 startMeasurmentButton.setEnabled(false);
                                                                 stopMeasurmentButton.setEnabled(true);
                                                                 viewMeasurmentButton.setEnabled(false);

                                                                 //TODO: Start Measurment
                                                                 startService(new Intent(menu.this, Recorder.class));

                                                             }
                                                         });

        stopMeasurmentButton = (Button)findViewById(R.id.stop_button);
        stopMeasurmentButton.setOnClickListener(new
                                                        View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v)
                                                            {
                                                                Toast.makeText(menu.this, "INFO: Stop measurment",
                                                                        Toast.LENGTH_LONG).show();
                                                                startMeasurmentButton.setEnabled(true);
                                                                stopMeasurmentButton.setEnabled(false);
                                                                viewMeasurmentButton.setEnabled(true);
                                                                //TODO: Stop Measurment
                                                                stopService(new Intent(menu.this, Recorder.class));
                                                            }
                                                        });


        viewMeasurmentButton = (Button)findViewById(R.id.result_button);
        viewMeasurmentButton.setOnClickListener(new
                                                        View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v)
                                                            {
                                                                Toast.makeText(menu.this, "INFO: Show result",
                                                                        Toast.LENGTH_LONG).show();

                                                                //TODO: Show Result from Measurment with Tracker and Bioharness3
                                                                startActivity(new Intent(menu.this, ubicom.htwg.en.healthapp.View.map.class));
                                                            }
                                                        });

        personalButton = (Button)findViewById(R.id.person_button);
        personalButton.setOnClickListener(new
                                                  View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View v) {
                                                          Toast.makeText(menu.this, "INFO: Personal settings",
                                                                  Toast.LENGTH_LONG).show();
                                                          //TODO: Bioharness3 Settings
                                                      }
                                                  });

        infoButton = (Button)findViewById(R.id.info_button);
        infoButton.setOnClickListener(new
                                              View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v)
                                                  {
                                                      Toast.makeText(menu.this, "INFO: Show information",
                                                              Toast.LENGTH_LONG).show();
                                                      startActivity(new Intent(menu.this, ubicom.htwg.en.healthapp.View.information.class));
                                                  }
                                              });

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
}
