package homework.mobile.sensorcollector;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SensorCollector extends Activity implements SensorEventListener{
    TextView view_location_info;
    TextView view_light_data;
    TextView view_accelerometer_data;
    TextView view_gyroscope_data;

    LocationManager mLocation;
    Location location;

    SensorManager mSensorManager;
    Sensor light_sensor;
    Sensor accelerometer_sensor;
    Sensor gyroscope_sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_collector);

        view_location_info = (TextView) findViewById(R.id.location);
        view_light_data = (TextView) findViewById(R.id.light);
        view_accelerometer_data = (TextView) findViewById(R.id.accelerometer);
        view_gyroscope_data = (TextView) findViewById(R.id.gyroscope);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light_sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        accelerometer_sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope_sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent eSensor){
        String label, value1, value2, value3;
        switch(eSensor.sensor.getType()){
            case Sensor.TYPE_LIGHT:
                label = "Light sensor\n";
                value1 = "Intensity: ";
                view_light_data.setText(label+value1+String.valueOf(eSensor.values[0]));
                break;
            case Sensor.TYPE_ACCELEROMETER:
                label = "ACCELEROMETER";
                value1 = "\nX-AXIS: ";
                value2 = "\nY-AXIS: ";
                value3 = "\nZ-AXIS: ";
                view_accelerometer_data.setText(label+
                        value1+String.valueOf(eSensor.values[0])+
                        value2+String.valueOf(eSensor.values[1])+
                        value3+String.valueOf(eSensor.values[2]));
                break;
            case Sensor.TYPE_GYROSCOPE:
                label = "GYROSCOPE";
                value1 = "\nX-AXIS: ";
                value2 = "\nY-AXIS: ";
                value3 = "\nZ-AXIS: ";
                view_gyroscope_data.setText(label+
                        value1+String.valueOf(eSensor.values[0])+
                        value2+String.valueOf(eSensor.values[1])+
                        value3+String.valueOf(eSensor.values[2]));
                break;
            default:
        }
    }

    @Override
    public void onAccuracyChanged(Sensor inSensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        mSensorManager.registerListener(this, light_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, accelerometer_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, gyroscope_sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);

        mLocation.removeUpdates(locationListener);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String label = "Location " + location.getProvider();
            String altitude = "\nAltitude : ";
            String latitude = ", Latitude : ";
            String longitude = ", Longitude : ";

            view_location_info.setText(label+
                    altitude+String.valueOf(location.getAltitude())+
                    latitude+String.valueOf(location.getLatitude())+
                    longitude+String.valueOf(location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extra) {
            location = mLocation.getLastKnownLocation(provider);

            if ( status != LocationProvider.AVAILABLE) {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);

                String new_provider = mLocation.getBestProvider(criteria, true);
                mLocation.requestLocationUpdates(new_provider, 0, 0, locationListener);
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_senser_logger, menu);
        return true;
    }

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
