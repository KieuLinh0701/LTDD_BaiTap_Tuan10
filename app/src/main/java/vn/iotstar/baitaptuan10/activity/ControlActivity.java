package vn.iotstar.baitaptuan10.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import vn.iotstar.baitaptuan10.R;

public class ControlActivity extends AppCompatActivity {

    //public static final int REQUEST_BLUETOOTH = 1;
    ImageButton btnTb1, btnTb2, btnDis;
    TextView txt1, txtMAC;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    Set<BluetoothDevice> pairedDevices1;
    String address = null;
    private ProgressDialog progress;
    int flaglamp1;
    int flaglamp2;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(ControlMainActivity.EXTRA_ADDRESS); // receive the address of the bluetooth device
        setContentView(R.layout.activity_control);

        // ánh xạ
        btnTb1 = (ImageButton) findViewById(R.id.btnTb1);
        btnTb2 = (ImageButton) findViewById(R.id.btnTb2);
        txt1 = (TextView) findViewById(R.id.textV1);
        txtMAC = (TextView) findViewById(R.id.textViewMAC);
        btnDis = (ImageButton) findViewById(R.id.btnDisc);

        new ConnectBT().execute(); // Call the class to connect

        btnTb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thietTbil(); // gọi hàm
            }
        });

        btnTb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thiettbi7(); // gọi hàm
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect();
            }
        });
    }

    private void thietTbil() {
        if (btSocket != null) {
            try {
                if (this.flaglamp1 == 0) {
                    this.flaglamp1 = 1;
                    this.btnTb1.setBackgroundResource(R.drawable.tb1on);
                    btSocket.getOutputStream().write("1".toString().getBytes());
                    txt1.setText("Thiết bị số 1 đang bật");
                    return;
                } else {
                    if (this.flaglamp1 != 1) return;
                    this.flaglamp1 = 0;
                    this.btnTb1.setBackgroundResource(R.drawable.off);
                    btSocket.getOutputStream().write("A".toString().getBytes());
                    txt1.setText("Thiết bị số 1 đang tắt");
                    return;
                }
            } catch (IOException e) {
                Log.e("Lỗi", "Lỗi");
            }
        }
    }

    private void Disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                Log.e("Lỗi", "Lỗi");
            }
        }
        finish();
    }

    private void thiettbi7() {
        if (btSocket != null) {
            try {
                if (this.flaglamp2 == 0) {
                    this.flaglamp2 = 1;
                    this.btnTb2.setBackgroundResource(R.drawable.tb1on);
                    btSocket.getOutputStream().write("7".toString().getBytes());
                    txt1.setText("Thiết bị số 7 đang bật");
                    return;
                } else {
                    if (this.flaglamp2 != 1) return;
                    {
                        this.flaglamp2 = 0;
                        this.btnTb2.setBackgroundResource(R.drawable.off);
                        btSocket.getOutputStream().write("G".toString().getBytes());
                        txt1.setText("Thiết bị số 7 đang tắt");
                        return;
                    }
                }
            } catch (IOException e) {
                Log.e("Lỗi", "Lỗi");
            }
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> { // UI thread

        private boolean ConnectSuccess = true; // if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            // show a progress dialog
            progress = ProgressDialog.show(ControlActivity.this, "Đang kết nối...", "Xin vui lòng đợi!!!");
        }

        @Override
        protected Void doInBackground(Void... devices) { // while the progress dialog is shown, the connection is done in background
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter(); // get the mobile bluetooth device
                    // connects to the device's address and checks if it's available
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    if (ActivityCompat.checkSelfPermission(ControlActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // create a RFCOMM (SPP) connection
                        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                        btSocket.connect(); // start connection
                    }
                }
            } catch (IOException e) {
                ConnectSuccess = false; // if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) { // after the doInBackground, it checks if everything went fine
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                msg("Kết nối thất bại! Kiểm tra thiết bị.");
                finish();
            } else {
                msg("Kết nối thành công.");
                isBtConnected = true;
                pairedDevicesList1();
            }
            progress.dismiss();
        }

        // fast way to call Toast
        private void msg(String s) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

        private void pairedDevicesList1() {
            if (ActivityCompat.checkSelfPermission(ControlActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                pairedDevices1 = myBluetooth.getBondedDevices();
                if (pairedDevices1.size() > 0) {
                    for (BluetoothDevice bt : pairedDevices1) {
                        txtMAC.setText(bt.getName() + " " + bt.getAddress()); // Get the device's name and the address
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Không tìm thấy thiết bị kết nối.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}