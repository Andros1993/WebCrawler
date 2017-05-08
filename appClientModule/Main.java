import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
	private final String BASE_URL_USA = "";
	private final String BASE_URL_CA = "";
	private final String BASE_URL_JP = "";
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("请选择站点：（1为美国站，2为加拿大站，3为美国站）");
		String site = sc.nextLine();
		String baseUrl = "";
		switch (Integer.parseInt(site)) {
		case 1: 
				baseUrl = "",
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		default:
			break;
		}
		
		System.out.println("请输入搜索关键词：");
		String content = sc.nextLine();
		System.out.println("请输入您的链接关键词：");
		String name = sc.nextLine();
		
		System.out.println("程序开始为您搜索……");
		
		ExecutorService es = Executors.newFixedThreadPool(100);

		for (int i = 0; i < 20; i++) {
			es.submit(new WebReques(content,name));
		}

		es.shutdown();
	}
}



class WebReques implements Runnable {

	private static int pages = 0;
	private static int buf = 0;
	private String content = "";
	private String name = "";
	
	public WebReques(String content,String name) {
		// TODO Auto-generated constructor stub
		this.content = content;
		this.name = name;
	}
	
	@Override
	public void run() {

		while (true) {

			synchronized (WebReques.class) {
				pages++;
				if (pages > 190) {
					break;
				}
			}

			if (buf == pages) {
				continue;
			}

			buf = pages;

			test(pages);

		}
	}

	public void test(int pNum) {

		String MyUrl = "https://www.amazon.co.jp/s/ref=sr_pg_" + pNum + "?rh=i:aps,k:" + content + "&page=" + pNum + "&keywords=" + content +"&ie=UTF8";
		
		// 建立get请求
		try {
			URL url = new URL(MyUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setConnectTimeout(20000);
			httpUrlConn.setReadTimeout(20000);
			httpUrlConn.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			httpUrlConn.setRequestProperty("connection", "Keep-Alive");
			httpUrlConn.setRequestProperty("Content-Encoding", "gzip");
			httpUrlConn.setRequestProperty("Content-Language", "ja-JP");
			httpUrlConn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
			httpUrlConn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.2; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
			httpUrlConn.setDoInput(true);
			httpUrlConn.setRequestMethod("GET");
			httpUrlConn.connect();

			// 获取输入流
			InputStream in = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(in, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			in.close();
			inputStreamReader.close();

			//得到网页内容
			String str2 = buffer.toString();
			//检索是否存在
			int a = str2.indexOf(name);
			
			if (a != -1) {
				System.out.println("找到了，在第 " + pNum + "页");
			} 
//			else {
//				System.out.println("正在搜索第" + pNum + "页……");
//			}

		} catch (Exception e) {

		}
	}

}