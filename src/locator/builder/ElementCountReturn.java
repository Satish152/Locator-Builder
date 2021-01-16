package locator.builder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jsoup.nodes.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
public class ElementCountReturn {
	

		/*static WebDriver driver;
		public static void main(String[] args) throws IOException, ScriptException, ParserConfigurationException, SAXException, XPathExpressionException {
			String src="https://partners-qa.subaru.com/customloginpage/pages/login.jsp?bmctx=314C6927BA5E7DA2D3F79D217EB49482&password=secure_string&contextType=external&username=string&challenge_url=%2Fcustomloginpage%2Fpages%2Flogin.jsp&request_id=509194333725417254&authn_try_count=0&locale=en&resource_url=%252Fuser%252Floginsso";
			String file="C:/Users/DELL/Desktop/PHPTRAVELS.html";
			Document doc=HtmlPage.getHTML(null, file, "https://phptravels.com");
			doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
			String text=doc.body().toString().replaceAll("&nbsp;", "").replace("\u00a9", "?");
			System.out.println(doc.body().toString());
			InputStream ins=new ByteArrayInputStream(text.getBytes());
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			DocumentBuilder db=dbf.newDocumentBuilder();
			org.w3c.dom.Document xmlDoc=db.parse(ins);
			XPath xPath=XPathFactory.newInstance().newXPath();
			String xpath="//input[@name='username']/parent::td/preceding-sibling::td";
			NodeList nodeList=(NodeList)xPath.compile(xpath).evaluate(xmlDoc,XPathConstants.NODESET);
			System.out.println(nodeList.getLength());
			System.out.println(nodeList.item(0).getTextContent());
			}*/
		
		public org.w3c.dom.Document xmlDoc(Document doc) throws ParserConfigurationException, SAXException, IOException{
			doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
			String text=doc.toString().replaceAll("&nbsp;", "").replaceAll("&", "&amp;").replaceAll("lt;", "&lt;").replaceAll("gt;", "&gt;").replaceAll("\u00a9", "?");
			text=text.replaceAll("<script>.*</script>", "").replaceAll("<style>.*</style>", "").replaceAll("<script .*/>", "").replaceAll("<script .*>.*</script>", "");
			text=text.replaceFirst("<!DOCTYPE html .*>", "<!DOCTYPE html>");
			InputSource is = new InputSource(new ByteArrayInputStream(text.getBytes("UTF-8"))); 
			is.setEncoding("ISO-8859-1");
			try{
				DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
				DocumentBuilder db=dbf.newDocumentBuilder();
				return db.parse(is);
			}catch(Exception e){
				try{
				text=doc.body().toString().replaceAll("&nbsp;", "").replaceAll("&", "&amp;").replaceAll("lt;", "&lt;").replaceAll("gt;", "&gt;").replaceAll("\u00a9", "?");
				text=text.replaceAll("<script>.*</script>", "").replaceAll("<style>.*</style>", "").replaceAll("<script .*/>", "").replaceAll("<script .*>.*</script>", "");
				text=text.replaceFirst("<!DOCTYPE html .*>", "<!DOCTYPE html>");
				is = new InputSource(new ByteArrayInputStream(text.getBytes("UTF-8"))); 
				is.setEncoding("ISO-8859-1");
					DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
					DocumentBuilder db=dbf.newDocumentBuilder();
					return db.parse(is);
				}catch(Exception ex){
					return null;
				}
			}
		}
		
		public int elementCount(String loc,org.w3c.dom.Document xmlDoc) throws XPathExpressionException{
			XPath xpath=XPathFactory.newInstance().newXPath();
			NodeList nodeList=(NodeList)xpath.compile(loc).evaluate(xmlDoc,XPathConstants.NODESET);
			return nodeList.getLength();
		}
		
		public static void main(String[] args) {
			String clsname="btnk";
			//[A-Z|0-9]?$   .*_[A-Z|0-9]* ^[\\d]+$  [[A-Z|0-9]$ | .*_[A-Z|0-9]* | ^[\\d]+$]
		    Pattern p=Pattern.compile("^[a-zA-Z0-9//s]+[A-Z|0-9]$");
		    Matcher mat=p.matcher(clsname);
		    System.out.println(mat.find());
		    System.out.println(mat.group(0));
		//	System.out.println(Pattern.compile(".*[A-Z|0-9]$").matcher(clsname).find());
		}
}