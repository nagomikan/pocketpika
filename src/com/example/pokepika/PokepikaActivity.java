package com.example.pokepika;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PokepikaActivity extends Activity implements SensorEventListener{
	private SensorManager manager;
	private TextView stepvalues;
	private int stepcount; //歩数カウント
	private int flag = 0; //フラグ
	private int firstflag = 0; //起動時に１増えるバグを直すフラグ
	Button button1;		//食べ物をあげるボタン
	Button button2;		//遊ぶ物をあげるボタン
	Button button3;		//衣装を変更するボタン

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pokepika);
		stepvalues = (TextView)findViewById(R.id.stepvalue_id);
		manager = (SensorManager)getSystemService(SENSOR_SERVICE);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);

		//読み込み
		try{
			FileInputStream in = openFileInput( "test.txt" );
			BufferedReader reader = new BufferedReader( new InputStreamReader( in , "UTF-8") );
			String str = "";
			String tmp;
			while( (tmp = reader.readLine()) != null ){
				str = str + tmp + "\n";
				  Log.v("readstr", str);
				  stepcount = Integer.parseInt(tmp);
			}
			reader.close();
		}catch( IOException e ){
			e.printStackTrace();
		}

		// 食べ物をあげるボタンに OnClickListener インターフェースを実装する
		button1.setOnClickListener(new View.OnClickListener() {
		    // クリック時に呼ばれるメソッド
		    @Override
		    public void onClick(View view) {
	            		if(stepcount > 50){
	            			stepcount = stepcount - 50;
	            			stepvalues.setText("ポイント:"+stepcount);
	            			 Log.v("readstr", "購入済みのポイント："+String.valueOf(stepcount));

	            		        //書き込み
	            				try{
	            				    String str =  String.valueOf(stepcount);
	            				    FileOutputStream out = openFileOutput( "test.txt", MODE_PRIVATE );
	            				    out.write( str.getBytes()   );
	            				}catch( IOException e ){
	            				    e.printStackTrace();
	            				}
	            		}
	            }
		});

	// 遊ぶものをあげるボタンに OnClickListener インターフェースを実装する
	button2.setOnClickListener(new View.OnClickListener() {

	    // クリック時に呼ばれるメソッド
	    @Override
	    public void onClick(View view) {
            		if(stepcount > 100){
            			stepcount = stepcount - 100;
            			stepvalues.setText("ポイント:"+stepcount);
            			 Log.v("readstr", "購入済みのポイント："+String.valueOf(stepcount));

            		        //書き込み
            				try{
            				    String str =  String.valueOf(stepcount);
            				    FileOutputStream out = openFileOutput( "test.txt", MODE_PRIVATE );
            				    out.write( str.getBytes()   );
            				}catch( IOException e ){
            				    e.printStackTrace();
            				}
            		}
            }
	});

	// 遊ぶものをあげるボタンに OnClickListener インターフェースを実装する
		button3.setOnClickListener(new View.OnClickListener() {

		    // クリック時に呼ばれるメソッド
		    @Override
		    public void onClick(View view) {
		    	  //選択項目を準備する。
		        String[] str_items = {
		                "衣装１",
		                "衣装２",
		                "衣装３",
		                "キャンセル"};

		        new AlertDialog.Builder(PokepikaActivity.this).setTitle("タイトルです").setItems(str_items, new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {

		                //選択したアイテムの番号(0～)がwhichに格納される
		                switch(which)
		                {
		                case 0:
		                // 選択１
		                    Toast.makeText(PokepikaActivity.this, "衣装１が選ばれました", Toast.LENGTH_LONG).show();
		                    break;
		                case 1:
		                // 選択２
		                    Toast.makeText(PokepikaActivity.this, "衣装２が選ばれました", Toast.LENGTH_LONG).show();
		                    break;
		                case 2:
		                // 選択３
		                    Toast.makeText(PokepikaActivity.this, "衣装３が選ばれました", Toast.LENGTH_LONG).show();
		                    break;
		                default:
		                // キャンセル
		                    Toast.makeText(PokepikaActivity.this, "キャンセルが選ばれました", Toast.LENGTH_LONG).show();
		                    break;
		                }
		            }
		        }
		        ).show();
	            }
		});
}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pokepika, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
    public void onStop(){
        super.onStop();
        Log.v("LifeCycle", "onStop");

       // Listenerの登録解除
        manager.unregisterListener(this);

        //書き込み
		try{
		    String str =  String.valueOf(stepcount);
		    FileOutputStream out = openFileOutput( "test.txt", MODE_PRIVATE );
		    out.write( str.getBytes()   );
		}catch( IOException e ){
		    e.printStackTrace();
		}
    }

	@Override
	protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	// Listenerの登録
	List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
	if(sensors.size() > 0) {
	Sensor s = sensors.get(0);
	manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
	}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
	// TODO Auto-generated method stub
	if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	String str = "加速度センサー値:"
	+ "\nX軸:" + event.values[SensorManager.DATA_X]
	+ "\nY軸:" + event.values[SensorManager.DATA_Y]
	+ "\nZ軸:" + event.values[SensorManager.DATA_Z];
	}

	if((event.values[SensorManager.DATA_Z]>0.5)&&(flag ==0)){
		flag=1;
		if(firstflag == 0){
			firstflag =1;
		}else{
			stepcount++;
		}
		stepvalues.setText("ポイント:"+stepcount);
	}else if((event.values[SensorManager.DATA_Z]<-0.5)&&(flag ==1)){
		flag=0;
	}
	}
}
