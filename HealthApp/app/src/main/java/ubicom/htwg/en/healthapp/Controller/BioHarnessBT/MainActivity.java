/*
 * Author: 			David Simon
 * Sources: 		Android Example from Zephyr
 * 					http://www.verydemo.com/demo_c89_i35191.html
 */
package ubicom.htwg.en.healthapp.Controller.BioHarnessBT;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ubicom.htwg.en.healthapp.Model.menu;
import ubicom.htwg.en.healthapp.R;

/*
 * @Description: 	Activity for the connection with the Bioharness3 sensor
 */
public class MainActivity extends Activity {

	// Local variable
	public static List<Bundle> data = new ArrayList<Bundle>();
	protected static final ConnectedListener<BTClient> NULL = null;
	private final int HEART_RATE = 0x100;
	private final int RESPIRATION_RATE = 0x101;
	private final int POSTURE = 0x103;
	private final int PEAK_ACCLERATION = 0x104;
	
	private int TextSize = 14;
	private BluetoothAdapter adapter;
	BTClient _bt;
	ZephyrProtocol _protocol;
	ConnectedListener<BTClient> _listener;
	NewConnectedListener _NConnListener;

	/*
	 * @Description: 	Button - Connect/Disconnect with the Bioharness3 sensor
	 * @Parameter:		Bundle savedInstanceState
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		//Locale Variable
		final Button btnConnect = (Button) findViewById(R.id.ButtonConnect);
		final Button btnDisconnect = (Button) findViewById(R.id.ButtonDisconnect);
		btnDisconnect.setEnabled(true);
		btnConnect.setEnabled(true);

		//Register Receiver with filter
		IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
		this.getApplicationContext().registerReceiver(new BTBroadcastReceiver(), filter);
		IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
		this.getApplicationContext().registerReceiver(new BTBondReceiver(), filter2);

		//Button - Connect with Bioharness3
		if (btnConnect != null) {
			btnConnect.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					EditText MacAddress = (EditText) findViewById(R.id.EditTextMAC);
					String BhMacID = MacAddress.toString();
					adapter = BluetoothAdapter.getDefaultAdapter();
					data.clear();

					Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
					if (pairedDevices.size() > 0) {
						for (BluetoothDevice device : pairedDevices) {
							if (device.getName().startsWith("BH")) {
								BluetoothDevice btDevice = device;
								BhMacID = btDevice.getAddress();
								break;
							}
						}
					}

					BluetoothDevice Device = adapter.getRemoteDevice(BhMacID);
					String DeviceName = Device.getName();

					EditText serial = (EditText) findViewById(R.id.labelSerialNumber);
					serial.setText(DeviceName);

					_bt = new BTClient(adapter, BhMacID);
					_NConnListener = new NewConnectedListener(handler, handler);
					_bt.addConnectedEventListener(_NConnListener);

					if (_bt.IsConnected()) {
						_bt.start();
						String ErrorText = "Connected to BioHarness " + DeviceName;
						System.err.println(ErrorText);
						btnDisconnect.setEnabled(true);
						btnConnect.setEnabled(false);
						Toast.makeText(MainActivity.this, "INFO: Connected to BioHarness",
								Toast.LENGTH_LONG).show();
					} else {
						String ErrorText = "Unable to Connect !";
						System.err.println(ErrorText);
					}
				}
			});
		}

		// Disconnected with Bioharness3 sensor
		if (btnDisconnect != null) {
			btnDisconnect.setOnClickListener(new OnClickListener() {
				@Override
                /*Functionality to act if the button DISCONNECT is touched*/
				public void onClick(View v) {
					String ErrorText = "Disconnected from BioHarness!";
					System.err.println(ErrorText);
					Toast.makeText(MainActivity.this, ErrorText, Toast.LENGTH_LONG).show();
					_bt.removeConnectedEventListener(_NConnListener);
					_bt.Close();
					menu.stopTime = System.currentTimeMillis();
					finish();
				}
			});
		}
	}

	/*
	 * @Description: Receiver class
	 */
	private class BTBondReceiver extends BroadcastReceiver {

		/*
		 * @Description: 	Receive - Get device
		 * @Parameter:		Context context
		 * 					Intent intent
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle b = intent.getExtras();
			BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
			System.err.println("Bond State: " + device.getBondState());
		}
    }

	/*
	 * @Description: Receiver class
	 */
    private class BTBroadcastReceiver extends BroadcastReceiver {

		/*
		 * @Description:	Recieve device with pin
		 * @Parameter:		Context context
		 * 					Intent intent
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("BTIntent", intent.getAction());
			Bundle b = intent.getExtras();
			System.err.println("BTIntent" + b.get("android.bluetooth.device.extra.DEVICE").toString());
			System.err.println("BTIntent" + b.get("android.bluetooth.device.extra.PAIRING_VARIANT").toString());
			try {
				BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
				Method m = BluetoothDevice.class.getMethod("convertPinToBytes", new Class[] {String.class} );
				byte[] pin = (byte[])m.invoke(device, "1234");
				m = device.getClass().getMethod("setPin", new Class [] {pin.getClass()});
				Object result = m.invoke(device, pin);
				Log.d("BTTest", result.toString());
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
    }

	/*
	 * Variable handler - Get messages from Bioharness and order to save list.
	 */
    final Handler handler = new Handler() {
    	public void handleMessage(Message msg) {
			data.add(msg.getData());
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

			switch (msg.what)
    		{
				case HEART_RATE:
					String ecgText = msg.getData().getString("HeartRate");
					System.err.println("ECG: " + ecgText);
					EditText ecg = (EditText) findViewById(R.id.heart_ed);
					ecg.setText(ecgText);

					EditText ecg_status = (EditText) findViewById(R.id.heart_status);
					ecg_status.setTextColor(Color.BLACK);

					int tempHeart = Integer.parseInt(ecgText.toString());

					if (tempHeart > 139 || tempHeart < 41)
					{
						ecg_status.setBackgroundColor(Color.RED);
						v.vibrate(500);
					} else if (tempHeart > 59 && tempHeart < 100) {
						ecg_status.setBackgroundColor(Color.GREEN);
					} else {
						ecg_status.setBackgroundColor(Color.YELLOW);
					}

					break;
				case RESPIRATION_RATE:
					String breathText = msg.getData().getString("RespirationRate");
					System.err.println("Breath: " + breathText);
					EditText breath = (EditText) findViewById(R.id.breath_info);
					breath.setText(breathText);

					EditText breath_status = (EditText) findViewById(R.id.breath_status);
					breath_status.setTextColor(Color.BLACK);
					Float tempBreath = Float.parseFloat(breathText.toString());

					if (tempBreath > 34.9 || tempBreath < 4.0)
					{
						breath_status.setBackgroundColor(Color.RED);
						v.vibrate(500);
					} else if (tempBreath > 5.9  && tempBreath < 25.0) {
						breath_status.setBackgroundColor(Color.GREEN);
					} else {
						breath_status.setBackgroundColor(Color.YELLOW);
					}

					break;
				case PEAK_ACCLERATION:
					String AccelerometerText = msg.getData().getString("PeakAcceleration");
					System.err.println("Accelerometer: " + AccelerometerText);
					EditText accel = (EditText) findViewById(R.id.run_ed);
					accel.setText(AccelerometerText);
					break;
				case POSTURE:
					String Posture = msg.getData().getString("Posture");
					EditText posture = (EditText) findViewById(R.id.posture_ed);
					posture.setText(Posture);
					System.err.println("Posture: " + Posture);
					break;
    		}
    	}
 };
}    

