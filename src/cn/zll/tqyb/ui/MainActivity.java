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
		// 初始化控件
		setViews();
		// 解析省份的xml文档
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
		// 配置监听器
		setListenters();
	}
	
			
	private void setListenters() {
		// 点击城市显示天气预报
		lvCity.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				City city = cities.get(position);
			final String name = city.getName();
				// 发送http请求 获取天气预报信息
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
		
		
		//点击省份显示对应的城市
		lvProvince.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//获取当前选中的是哪个省份
				Province p = provinces.get(position);
				int pid = p.getId();
				//遍历  allCities  过滤所有需要的城市数据
				cities.clear();
				for(City c : allCities){
					if(c.getPid() == pid){
						cities.add(c); 
					}
				}
				//更新Adapter
				ArrayAdapter<City> city = new ArrayAdapter<City>(MainActivity.this, android.R.layout.simple_list_item_1, cities);
				lvCity.setAdapter(city);
			}
		});
	}
	/**
	 * 在工作线程中，查询天气预报，
	 * 发送HTTP请求 GET方式
	 * @throws Exception
	 */
	private void queryWeather(String cityName) throws Exception{
		// get请求中有中文，将中文转成url编码
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
		//发消息给handler，更新Adapter
		//handler.sendEmptyMessage(HANDLER_UPDATE_UI);
	}


	/**
	 * 解析所有的城市数据并保存到成员变量
	 * 适合使用pull解析
	 * @throws Exception
	 */
	private void parseCity() throws Exception{
		// 1. 创建XmlPullParser对象
		XmlPullParser parser = Xml.newPullParser();
		InputStream is = getAssets().open("Cities.xml");
		parser.setInput(is,"utf-8");
		// 2. 获取驱动事件
		int type = parser.getEventType();
		allCities = new ArrayList<City>();
		City city =null;
		// 3. 判断当前驱动事件是否最后一个文档
		while(type !=XmlPullParser.END_DOCUMENT){
			// 判断type
			switch (type) {
			// 当遇到开始标记时
			case XmlPullParser.START_TAG:
				// 提取标签的名字
				String name = parser.getName();
				// 如果标签名与参数中标签名相等，则将数据封装到city对象，并添加到集合中
				if(name.equals("City")){
					city = new City();
					city.setId(Integer.parseInt((parser.getAttributeValue(0))));
					city.setName(parser.getAttributeValue(1));
					city.setPid(Integer.parseInt(parser.getAttributeValue(2)));
					allCities.add(city);
				}
				break;
			}
			// 驱动到一个事件
			type = parser.next();
		}
	}

	/**
	 * 解析并加载城市信息 （DOM4J）
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void parseAndLoadProvince() throws IOException, DocumentException {
		// 1. 创建SXMReader对象
		SAXReader reader = new SAXReader();
		// 2. 获取assets目录下的xml文档
		InputStream is = getAssets().open("Provinces.xml");
		// 3. 调用SXAReader.read()方法，获取Document对象
		Document doc = reader.read(is);
		// 4. 调用getRootElement()获取根节点
		Element root = doc.getRootElement();
		// 5. 调用elements(),获取proEles下所有的子标签
		List<Element> proEles = root.elements();
		provinces = new ArrayList<Province>();
		// 6. 迭代遍历
		for(Element e : proEles){
			// 获取省份ID和省份名称
			String id = e.attributeValue("ID");
			String name = e.attributeValue("ProvinceName");
			Province p = new Province(Integer.parseInt(id), name);
			provinces.add(p);	
			}
		// 解析完毕， 立即显示到LvProvince中
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
