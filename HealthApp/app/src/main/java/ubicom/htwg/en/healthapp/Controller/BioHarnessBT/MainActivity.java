package ubicom.htwg.en.healthapp.Controller.BioHarnessBT;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ubicom.htwg.en.healthapp.R;

public class MainActivity extends Activity {
	protected static final ConnectedListener<BTClient> NULL = null;
	private final int HEART_RATE = 0x100;
	private final int RESPIRATION_RATE = 0x101;
	private final int SKIN_TEMPERATURE = 0x102;
	private final int POSTURE = 0x103;
	private final int PEAK_ACCLERATION = 0x104;
	private final int SERIAL_NUM_PACKET = 1210;
	
	private int TextSize = 14;
	private BluetoothAdapter adapter;
	BTClient _bt;
	ZephyrProtocol _protocol;
	ConnectedListener<BTClient> _listener;
	NewConnectedListener _NConnListener;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

        /*Sending a message to android that we are going to
         * initiate a pairing request*/
		IntentFilter filter = new IntentFilter(
				"android.bluetooth.device.action.PAIRING_REQUEST");
        /*Registering a new BTBroadcast receiver from the Main
         *  Activity context with pairing request event*/
		this.getApplicationContext().registerReceiver(new BTBroadcastReceiver(), filter);
		// Registering the BTBondReceiver in the application
		//that the status of the receiver has changed to Paired
		IntentFilter filter2 = new IntentFilter(
				"android.bluetooth.device.action.BOND_STATE_CHANGED");
		this.getApplicationContext().registerReceiver(new BTBondReceiver(), filter2);

		Button btnConnect = (Button) findViewById(R.id.ButtonConnect);
		if (btnConnect != null) {
			btnConnect.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String BhMacID = "00:17:E9:C0:84:03";
					adapter = BluetoothAdapter.getDefaultAdapter();

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
					_bt = new BTClient(adapter, BhMacID);
					_NConnListener = new NewConnectedListener(handler, handler);
					_bt.addConnectedEventListener(_NConnListener);

					if (_bt.IsConnected()) {
						_bt.start();
						String ErrorText = "Connected to BioHarness " + DeviceName;
						System.err.println(ErrorText);

						//Reset all the values to 0s

					} else {
						String ErrorText = "Unable to Connect !";
						System.err.println(ErrorText);
					}
				}
			});
		}
        /*Obtaining the handle to act on the DISCONNECT button*/
		Button btnDisconnect = (Button) findViewById(R.id.ButtonDisconnect);
		if (btnDisconnect != null) {
			btnDisconnect.setOnClickListener(new OnClickListener() {
				@Override
                /*Functionality to act if the button DISCONNECT is touched*/
				public void onClick(View v) {
					// TODO Auto-generated method stub
                    /*Reset the global variables*/
					String ErrorText = "Disconnected from BioHarness!";
					System.err.println(ErrorText);

                    /*This disconnects listener from acting on received messages*/
					_bt.removeConnectedEventListener(_NConnListener);
                    /*Close the communication with the device & throw an exception if failure*/
					_bt.Close();

				}
			});
		}
	}

		private class BTBondReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle b = intent.getExtras();
			BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
			System.err.println("Bond State: " + device.getBondState());
		}
    }

    private class BTBroadcastReceiver extends BroadcastReceiver {
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
    final Handler handler = new Handler() {
    	public void handleMessage(Message msg) {

			System.err.println("DATA: " + msg.getData());

			switch (msg.what)
    		{
    		case HEART_RATE:
				String ecgText = msg.getData().getString("HeartRate");
				System.err.println("ECG: " + ecgText);
				EditText ecg = (EditText) findViewById(R.id.heart_ed);
				ecg.setText(ecgText);
				break;
    		case RESPIRATION_RATE:
        		String breathText = msg.getData().getString("RespirationRate");
        		System.err.println("Breath: " + breathText);
				EditText breath = (EditText) findViewById(R.id.breath_info);
				breath.setText(breathText);
        		break;
    		case SKIN_TEMPERATURE:
        		String RtoRText = msg.getData().getString("SkinTemperature");
        		System.err.println("R2R: " + RtoRText);
				EditText rtor = (EditText) findViewById(R.id.temperature_ed);
				rtor.setText(RtoRText);
        		break;
    		case PEAK_ACCLERATION:
        		String AccelerometerText = msg.getData().getString("PeakAcceleration");
        		System.err.println("Accelerometer: " + AccelerometerText);
				EditText accel = (EditText) findViewById(R.id.run_ed);
				accel.setText(AccelerometerText);
    			break;
    		case POSTURE:
    			String SerialNumtext = msg.getData().getString("Posture");
				EditText serial = (EditText) findViewById(R.id.posture_ed);
				serial.setText(SerialNumtext);
    			System.err.println("Serialnumber: " + SerialNumtext);
    			break;
    		}
    	}
 };
}    

