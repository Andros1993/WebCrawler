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
		
		System.out.println("��ѡ��վ�㣺��1Ϊ����վ��2Ϊ���ô�վ��3Ϊ����վ��");
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
		
		System.out.println("�����������ؼ��ʣ�");
		String content = sc.nextLine();
		System.out.println("�������������ӹؼ��ʣ�");
		String name = sc.nextLine();
		
		System.out.println("����ʼΪ����������");
		
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
		
		// ����get����
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

			// ��ȡ������
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

			//�õ���ҳ����
			String str2 = buffer.toString();
			//�����Ƿ����
			int a = str2.indexOf(name);
			
			if (a != -1) {
				System.out.println("�ҵ��ˣ��ڵ� " + pNum + "ҳ");
			} 
//			else {
//				System.out.println("����������" + pNum + "ҳ����");
//			}

		} catch (Exception e) {

		}
	}

}