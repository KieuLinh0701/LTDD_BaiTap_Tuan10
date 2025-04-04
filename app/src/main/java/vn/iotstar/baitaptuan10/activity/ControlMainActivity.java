package vn.iotstar.baitaptuan10.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Set;

import vn.iotstar.baitaptuan10.R;

public class ControlMainActivity extends AppCompatActivity {

    Button btnPaired;
    ListView listDanhSach;

    public static int REQUEST_BLUETOOTH = 1;

    private BluetoothAdapter myBlueTooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_control_main);

        btnPaired = (Button) findViewById(R.id.btnTimthietbi);
        listDanhSach = (ListView) findViewById(R.id.listTb);

        myBlueTooth = BluetoothAdapter.getDefaultAdapter();

        if (myBlueTooth == null) {
            Toast.makeText(getApplicationContext(), "Thiết bị Bluetooth chưa bật", Toast.LENGTH_LONG).show();
            finish();
        } else if (!myBlueTooth.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this
                    , Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Thiết bị Bluetooth chưa bật", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(), "Thiết bị Bluetooth đã bật", Toast.LENGTH_LONG).show();
            startActivitiesForResult(turnBTon, REQUEST_BLUETOOTH);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });

    }

    private void pairedDevicesList() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            pairedDevices = myBlueTooth.getBondedDevices();
            ArrayList list = new ArrayList();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice bt:pairedDevices) {
                    if (ActivityCompat.checkSelfPermission(this
                            , Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Danh sách thiết bị Bluetooth đã bật", Toast.LENGTH_LONG).show();
                        list.add(bt.getName() + "\n" + bt.getAddress());
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Không tìm thấy thiết bị kêt nối", Toast.LENGTH_LONG).show();
            }

            final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            listDanhSach.setAdapter(adapter);
            listDanhSach.setOnClickListener(myListCLickListener);
            return;
        }
    }
}