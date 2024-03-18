package com.example.miniprogramholder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.buttonCameraMiniProgram)
        button.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent);
        }
        val buttonBt = findViewById<Button>(R.id.buttonBluetoothMiniProgram)
        buttonBt.setOnClickListener {
            val intent = Intent(this, BluetoothActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent);
        }
        val buttonGeolocation = findViewById<Button>(R.id.buttonGeolocation)
        buttonGeolocation.setOnClickListener {
            val intent = Intent(this, GeolocationActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent);
        }
        val buttonDeviceMotion = findViewById<Button>(R.id.buttonDeviceMotion)
        buttonDeviceMotion.setOnClickListener {
            val intent = Intent(this, DeviceMotionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent);
        }

        val buttonNetworkInfo = findViewById<Button>(R.id.buttonNetworkInfo)
        buttonNetworkInfo.setOnClickListener {
            val intent = Intent(this, NetworkInfoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent);
        }
        val buttonGeolocationWithCapacitor = findViewById<Button>(R.id.buttonGeolocationWithCapacitor)
        buttonGeolocationWithCapacitor.setOnClickListener {
            val intent = Intent(this, GeolocationActivityWithCapacitor::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent);
        }
        val buttonCameraWithCapacitor = findViewById<Button>(R.id.buttonCameraWithCapacitor)
        buttonCameraWithCapacitor.setOnClickListener {
            val intent = Intent(this, CameraActivityWithCapacitor::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent);
        }
        val buttonDeviceMotionWithCapacitor = findViewById<Button>(R.id.buttonDeviceMotionWithCapacitor)
        buttonDeviceMotionWithCapacitor.setOnClickListener {
            val intent = Intent(this, DeviceMotionActivityWithCapacitor::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent);
        }
        val buttonBluetoothWithCapacitor = findViewById<Button>(R.id.buttonBluetoothWithCapacitor)
        buttonBluetoothWithCapacitor.setOnClickListener {
            val intent = Intent(this, BluetoothActivityWithCapacitor::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent);
        }
        val buttonNetworkWithCapacitor = findViewById<Button>(R.id.buttonNetworkWithCapacitor)
        buttonNetworkWithCapacitor.setOnClickListener {
            val intent = Intent(this, NetworkInfoActivityWithCapacitor::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent);
        }

        val buttonBluetoothNativeImpl = findViewById<Button>(R.id.buttonBluetoothNativeImpl)
        buttonBluetoothNativeImpl.setOnClickListener {
            val intent = Intent(this, BluetoothNativeImplActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent)
        }

        val auth = findViewById<Button>(R.id.buttonAuth)
        auth.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            startActivity(intent)
        }

    }
}