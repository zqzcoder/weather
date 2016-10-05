package cn.zll.tqyb.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.zll.tqyb.R;
import cn.zll.tqyb.entity.City;
import cn.zll.tqyb.entity.Province;
import cn.zll.tqyb.entity.Weather;

public class MainActivity extends Activity {
	private ListView lvProvince;
	private ListView lvCity;
	private ArrayList<Province> provinces;
	private ArrayList<City> allCities;
	private TextView tvDate;
	private TextView tvHigh;
	private TextView tvLow;
	private List<Weather> weathers = new ArrayList<Weather>();
	private List<City> cities = new ArrayList<City>();
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLER_UPDATE_UI:
				break;
			}
			
		};
	};
	private Weather w;
	
	private static final int HANDLER_UPDATE_UI =1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ��ʼ���ؼ�
		setViews();
		// ����ʡ�ݵ�xml�ĵ�
		try {
			parseAndLoadProvince();
			parseCity();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ���ü�����
		setListenters();
	}
	
			
	private void setListenters() {
		// ���������ʾ����Ԥ��
		lvCity.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				City city = cities.get(position);
			final String name = city.getName();
				// ����http���� ��ȡ����Ԥ����Ϣ
				new Thread(){
					public void run() {
						try {
							queryWeather(name);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		});
		
		
		//���ʡ����ʾ��Ӧ�ĳ���
		lvProvince.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//��ȡ��ǰѡ�е����ĸ�ʡ��
				Province p = provinces.get(position);
				int pid = p.getId();
				//����  allCities  ����������Ҫ�ĳ�������
				cities.clear();
				for(City c : allCities){
					if(c.getPid() == pid){
						cities.add(c); 
					}
				}
				//����Adapter
				ArrayAdapter<City> city = new ArrayAdapter<City>(MainActivity.this, android.R.layout.simple_list_item_1, cities);
				lvCity.setAdapter(city);
			}
		});
	}
	/**
	 * �ڹ����߳��У���ѯ����Ԥ����
	 * ����HTTP���� GET��ʽ
	 * @throws Exception
	 */
	private void queryWeather(String cityName) throws Exception{
		// get�����������ģ�������ת��url����
		cityName= URLEncoder.encode(cityName, "utf-8");
		Log.d("zll", ""+cityName);
		URL url = new URL("http://wthrcdn.etouch.cn/WeatherApi?city="+cityName);
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();
		InputStream is = conn.getInputStream();
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");
		int type = parser.getEventType();
		
		Log.i("info", "111111");
		int i=0;
		while(type!=XmlPullParser.END_DOCUMENT){
			switch (type) {
			case  XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				String name= parser.getName();
				//Log.i("info", "name -> " + name);
				if(name.equals("weather")){
				w = new Weather();
				Log.d("info", "Weather -> ....." );
				}
				if(name.equals("date")&&w!=null){
					w.setDate(parser.nextText());
					Log.i("zll", "getDate()="+w.getDate());
				}
				if(name.equals("high")&&w!=null){
					w.setHigh(parser.nextText());
					Log.i("zll", "getHigh()="+w.getHigh());
				}
				if(name.equals("low")&&w!=null){
					w.setLow(parser.nextText());
					Log.i("zll", "getLow()="+w.getLow());
				}
				weathers.add(w);
				break;
			}
			type= parser.next();
		}
		Log.i("zll", "weathers="+weathers.get(0));
		Log.i("zll", "weathers1="+weathers.get(1));
		Log.i("zll", "weathers2="+weathers.get(2));
		Log.i("zll", "weathers2="+weathers.get(12));
		//����Ϣ��handler������Adapter
		//handler.sendEmptyMessage(HANDLER_UPDATE_UI);
	}


	/**
	 * �������еĳ������ݲ����浽��Ա����
	 * �ʺ�ʹ��pull����
	 * @throws Exception
	 */
	private void parseCity() throws Exception{
		// 1. ����XmlPullParser����
		XmlPullParser parser = Xml.newPullParser();
		InputStream is = getAssets().open("Cities.xml");
		parser.setInput(is,"utf-8");
		// 2. ��ȡ�����¼�
		int type = parser.getEventType();
		allCities = new ArrayList<City>();
		City city =null;
		// 3. �жϵ�ǰ�����¼��Ƿ����һ���ĵ�
		while(type !=XmlPullParser.END_DOCUMENT){
			// �ж�type
			switch (type) {
			// ��������ʼ���ʱ
			case XmlPullParser.START_TAG:
				// ��ȡ��ǩ������
				String name = parser.getName();
				// �����ǩ��������б�ǩ����ȣ������ݷ�װ��city���󣬲���ӵ�������
				if(name.equals("City")){
					city = new City();
					city.setId(Integer.parseInt((parser.getAttributeValue(0))));
					city.setName(parser.getAttributeValue(1));
					city.setPid(Integer.parseInt(parser.getAttributeValue(2)));
					allCities.add(city);
				}
				break;
			}
			// ������һ���¼�
			type = parser.next();
		}
	}

	/**
	 * ���������س�����Ϣ ��DOM4J��
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void parseAndLoadProvince() throws IOException, DocumentException {
		// 1. ����SXMReader����
		SAXReader reader = new SAXReader();
		// 2. ��ȡassetsĿ¼�µ�xml�ĵ�
		InputStream is = getAssets().open("Provinces.xml");
		// 3. ����SXAReader.read()��������ȡDocument����
		Document doc = reader.read(is);
		// 4. ����getRootElement()��ȡ���ڵ�
		Element root = doc.getRootElement();
		// 5. ����elements(),��ȡproEles�����е��ӱ�ǩ
		List<Element> proEles = root.elements();
		provinces = new ArrayList<Province>();
		// 6. ��������
		for(Element e : proEles){
			// ��ȡʡ��ID��ʡ������
			String id = e.attributeValue("ID");
			String name = e.attributeValue("ProvinceName");
			Province p = new Province(Integer.parseInt(id), name);
			provinces.add(p);	
			}
		// ������ϣ� ������ʾ��LvProvince��
		ArrayAdapter<Province> adapter = new ArrayAdapter<Province>(MainActivity.this, android.R.layout.simple_list_item_1, provinces);
		lvProvince.setAdapter(adapter);
		}
	
	private void setViews() {
		lvCity = (ListView) findViewById(R.id.lvCity);
		lvProvince = (ListView) findViewById(R.id.lvProvince);
		tvDate = (TextView) findViewById(R.id.textView1);
		tvHigh = (TextView) findViewById(R.id.textView2);
		tvLow = (TextView) findViewById(R.id.textView3);
	}

	

}
