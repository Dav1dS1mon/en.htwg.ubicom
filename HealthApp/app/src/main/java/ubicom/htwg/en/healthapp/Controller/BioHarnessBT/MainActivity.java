package ubicom.htwg.en.healthapp.Controller.BioHarnessBT;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import ubicom.htwg.en.healthapp.Controller.Recorder;
import ubicom.htwg.en.healthapp.R;

public class MainActivity extends Activity {
	protected static final ConnectedListener<BTClient> NULL = null;
	private final int GEN_PACKET = 1200;
	private final int ECG_PACKET = 1202;
	private final int BREATH_PACKET = 1204;
	private final int R_to_R_PACKET = 1206;
	private final int ACCELEROMETER_PACKET = 1208;
	private final int SERIAL_NUM_PACKET = 1210;
	private final int SUMMARY_DATA_PACKET =1212;
	private final int EVENT_DATA_PACKET =1214;
	public byte[] DataBytes;
	
	private int TextSize = 14;
	BluetoothAdapter adapter = null;
	BTClient _bt;
	ZephyrProtocol _protocol;
	ConnectedListener<BTClient> _listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        IntentFilter filter = new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
        this.getApplicationContext().registerReceiver(new BTBroadcastReceiver(), filter);
        IntentFilter filter2 = new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED");
        this.getApplicationContext().registerReceiver(new BTBondReceiver(), filter2);

        Button btnConnect = (Button) findViewById(R.id.ButtonConnect);
        if (btnConnect != null)
        	btnConnect.setOnClickListener(new OnClickListener() {
				@Override
				//Functionality to act if the button CONNECT is touched
				public void onClick(View v) {
					adapter = BluetoothAdapter.getDefaultAdapter();
					EditText mac = (EditText) findViewById(R.id.EditTextMAC);
					_bt = new BTClient(adapter, mac.getText().toString());
					_listener = new ConnectListenerImpl(handler,DataBytes);
					_bt.addConnectedEventListener(_listener);
					if(_bt.IsConnected())
					{
						System.err.println("INFO: Connect to BioHarness");
						_bt.start();
					}
					else
					{
						String ErrorText  = "ERROR: Unable to connect to BioHarness !";

						AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
						builder.setTitle("Info");
						builder.setMessage("Unable to connect to BioHarness!");
						builder.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										//dismiss the dialog
									}
								});
						AlertDialog alert = builder.create();
						alert.show();

						System.err.println(ErrorText);
					}
				}
        	});
        Button btnDisconnect = (Button) findViewById(R.id.ButtonDisconnect);
        if (btnDisconnect != null)
        	btnDisconnect.setOnClickListener(new OnClickListener() {
				@Override
				/*Functionality to act if the button DISCONNECT is touched*/
				public void onClick(View v) {

					System.err.println("INFO: Disconnected to BioHarness");
					try {
						_bt.removeConnectedEventListener(_listener);
						_bt.Close();
						finish();
					} catch (Exception e) {
						finish();
					}
				}
        	});
    }

    private class BTBondReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle b = intent.getExtras();
			BluetoothDevice device = adapter.getRemoteDevice(b.get("android.bluetooth.device.extra.DEVICE").toString());
			Log.d("BOnd state", "BOND_STATED = " + device.getBondState());
		}
    }
    
    private class BTBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("BTIntent", intent.getAction());
			Bundle b = intent.getExtras();
			Log.d("BTIntent", b.get("android.bluetooth.device.extra.DEVICE").toString());
			Log.d("BTIntent", b.get("android.bluetooth.device.extra.PAIRING_VARIANT").toString());
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
    		TextView tv;
    		switch (msg.what)
    		{
    		case GEN_PACKET:
				String genText = msg.getData().getString("genText");
				System.err.println("Information: " + genText);
				EditText info = (EditText) findViewById(R.id.info_ed);
				info.setText(genText);
				break;
    		case ECG_PACKET:
				String ecgText = msg.getData().getString("ecgText");
				System.err.println("ECG: " + ecgText);
				EditText ecg = (EditText) findViewById(R.id.ecg_info);
				ecg.setText(ecgText);
				break;
    		case BREATH_PACKET:
        		String breathText = msg.getData().getString("breathText");
        		System.err.println("Breath: " + breathText);
				EditText breath = (EditText) findViewById(R.id.breath_ed);
				breath.setText(breathText);
        		break;
    		case R_to_R_PACKET:
        		String RtoRText = msg.getData().getString("RtoRText");
        		System.err.println("R2R: " + RtoRText);
				EditText rtor = (EditText) findViewById(R.id.rtor_ed);
				rtor.setText(RtoRText);
        		break;
    		case ACCELEROMETER_PACKET:
        		String AccelerometerText = msg.getData().getString("Accelerometertext");
        		System.err.println("Accelerometer: " + AccelerometerText);
				EditText accel = (EditText) findViewById(R.id.accel_ed);
				accel.setText(AccelerometerText);
    			break;
    		case SERIAL_NUM_PACKET:
    			String SerialNumtext = msg.getData().getString("SerialNumtxt");
				EditText serial = (EditText) findViewById(R.id.labelSerialNumber);
				serial.setText(SerialNumtext);
    			System.err.println("Serialnumber: " + SerialNumtext);
    			break;
    		case SUMMARY_DATA_PACKET:
        		String SummaryText = msg.getData().getString("SummaryDataText");
        		System.err.println("Summary: " + SummaryText);
				EditText summary = (EditText) findViewById(R.id.summary_ed);
				summary.setText(SummaryText);
    			break;
    		case EVENT_DATA_PACKET:
        		String EventText = msg.getData().getString("EventDataText");
        		System.err.println("Event: " + EventText);
				EditText eventText = (EditText) findViewById(R.id.event_ed);
				eventText.setText(EventText);
    			break;
    		}
    	}
 };
}    

