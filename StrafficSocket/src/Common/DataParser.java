package Common;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class DataParser {
	
	private TelegramParser tp = null;
	
	public DataParser(String msg) {
		
		try {
			tp = new TelegramParser();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
