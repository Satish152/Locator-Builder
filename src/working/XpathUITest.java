package working;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.xpath.XPathExpressionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import working.ElementCountReturn;

public class XpathUITest {

	public static Document document;
	public static String locName = "";
	public static String temp = "xpath_";
	static String tempVar = "";
	public static String xpath = "//";
	public static XpathUITest htmlPage;
	public static org.w3c.dom.Document xmlDoc;
	public static ElementCountReturn obj = new ElementCountReturn();

	public static String output = "";
	// String srcUrl,String baseURLIfFile

	public static void main(String[] args) throws IOException, InterruptedException {
		XpathUITest onj = new XpathUITest();
		onj.xpathBuilder("https://google.com", "a");
	}

	public String xpathBuilder(String url, String tag) throws IOException, InterruptedException {
		try {
			htmlPage = new XpathUITest();
			try {
				document = getHTML(url, "baseuri");
				if (document == null) {
					return "Exception : Not able to read the html url provided, try agan by saving the file as html file in local";
				}
			} catch (Exception e) {
				return "Exception : Not able to read the html url provided, try agan by saving the file as html file in local";
			}
			ElementCountReturn obj = new ElementCountReturn();
			Elements elements = document.getElementsByTag(tag);
			List<String> textTags = new LinkedList<String>();
			textTags.add("a");
			textTags.add("label");
			textTags.add("span");
			textTags.add("h1");
			textTags.add("li");
			textTags.add("td");
			textTags.add("h2");
			textTags.add("h3");
			textTags.add("h4");
			boolean isExclude = textTags.contains(tag);
			if (elements.size() != 0) {
				xmlDoc = obj.xmlDoc(document);
				if (xmlDoc == null) {
					return "Exception : Error while generating locator for html";
				} else {
					String generatedPath = "";
					String generatedName = "";
					for (Element ele : elements) {
						locName = "";
						temp = "xpath_";
						tempVar = "";
						xpath = "//";
						generatedName = returnLocName(ele);
						if (textTags.contains(tag)) {
							generatedPath = getElementText(ele);
							if (generatedPath.isEmpty()) {
								generatedPath = returnXpath(document, ele);
								if (generatedPath.isEmpty() || (obj.elementCount(generatedPath, xmlDoc) > 2
										|| obj.elementCount(generatedPath, xmlDoc) == 0)) {
									generatedPath = generateAbsXpath(ele);
									if (obj.elementCount(generatedPath, xmlDoc) == 1) {
										output = output + generatedName + " = " + generatedPath + "\n";
										System.out.println(generatedName + " = " + generatedPath);
									} else if (generatedPath.isEmpty() || (obj.elementCount(generatedPath, xmlDoc) > 2
											|| obj.elementCount(generatedPath, xmlDoc) == 0))
										output = output + "" + "\n";
								} else if (obj.elementCount(generatedPath, xmlDoc) == 1) {
									output = output + generatedName + " = " + generatedPath + "\n";
									System.out.println(generatedName + " = " + generatedPath);
								}
							} else if (!generatedPath.isEmpty() && obj.elementCount(generatedPath, xmlDoc) == 1) {
								output = output + generatedName + " = " + generatedPath + "\n";
								System.out.println(generatedName + " = " + generatedPath);
							} else if (!generatedPath.isEmpty() && obj.elementCount(generatedPath, xmlDoc) > 1) {
								generatedPath = generateAbsXpath(ele);
								if (obj.elementCount(generatedPath, xmlDoc) == 1) {
									output = output + generatedName + " = " + generatedPath + "\n";
									System.out.println(generatedName + " = " + generatedPath);
								} else if (generatedPath.isEmpty() || (obj.elementCount(generatedPath, xmlDoc) > 2
										|| obj.elementCount(generatedPath, xmlDoc) == 0))
									output = output + "" + "\n";
							}
						}

						if (!isExclude && !generatedName.isEmpty() && !ele.attributes().toString().contains("hidden")
								&& !(ele.tagName().equals("a") && ele.hasAttr("aria-expanded"))
								&& (!ele.attributes().toString().contains("display:none")
										|| !ele.attributes().toString().contains("height=\"0\""))) {
							generatedPath = returnXpath(document, ele);
							if (!generatedPath.isEmpty()) {
								if (obj.elementCount(generatedPath, xmlDoc) < 2
										&& obj.elementCount(generatedPath, xmlDoc) != 0) {
									System.out.println(generatedName + "_" + ele.tagName() + " = " + generatedPath);
									output = output + generatedName + "_" + ele.tagName() + " = " + generatedPath
											+ "\n";
								} else if (obj.elementCount(generatedPath, xmlDoc) > 1) {
									String gen = htmlPage.absoluteXpathGenerator(ele);
									if (!gen.isEmpty()) {
										output = output + generatedName + "_" + ele.tagName() + " = " + gen + "\n";
										System.out.println(output);
									}
								}
							} else if (generatedPath.isEmpty()) {
								String gen = htmlPage.absoluteXpathGenerator(ele);
								if (!gen.isEmpty()) {
									output = output + generatedName + "_" + ele.tagName() + " = " + gen + "\n";
									System.out.println(output);
								}
							}
						}
					}
				}
			} else {
				return "No html tag found in the provided html";
			}
		} catch (Exception e) {
			System.out.println("Exception : Error while building xpath and please find the below error log : " + e.getMessage());
			output = output + "Exception : Error while building xpath and please find the below error log : " + e.getMessage();
		}
		return output;
	}

	public String returnLocName(Element element) {
		Attributes atrb = element.attributes();
		locName = "";
		boolean found = false;
		if (!element.ownText().isEmpty()) {
			temp = temp + element.ownText();
			int i = element.getElementsContainingOwnText(element.ownText()).size();
			if (i == 0 || i > 2) {
				found = true;
			} else {
				temp = "xpath_";
			}
		}
		if (found != true) {
			if (atrb.hasKey("aria-label")) {
				temp = temp + atrb.get("aria-label");
			} else if (atrb.hasKey("name")) {
				temp = temp + atrb.get("name");
			} else if (atrb.hasKey("title")) {
				temp = temp + atrb.get("title");
			} else if (atrb.hasKey("value")) {
				temp = temp + atrb.get("value");
			} else if (atrb.hasKey("id")) {
				temp = temp + atrb.get("id");
			} else if (atrb.hasKey("class")) {
				temp = temp + atrb.get("class");
			} else if (!element.ownText().isEmpty()) {
				if (element.ownText().length() > 25) {
					temp = temp + element.ownText().substring(0, 25).trim();
					locName = temp;
				} else {
					temp = temp + element.ownText();
					locName = temp;
				}
				temp = "xpath_";
				return locName;
			} else if (atrb.hasKey("placeholder")) {
				temp = temp + atrb.get("placeholder");
			} else if (atrb.hasKey("alt")) {
				temp = temp + atrb.get("alt");
			}
		}

		if (temp.equals("xpath_")) {
			locName = temp + element.tagName() + "_1";
			return locName;
		} else {
			locName = temp;
			temp = "xpath_";
			return locName.replaceAll(" ", "_");
		}

	}

	public static Document getHTML(String urL, String baseURI) throws IOException {
		try {
			URL url = null;
			String temp = "";
			HttpURLConnection con = null;
			try {
				url = new URL(urL);
				con = (HttpURLConnection) url.openConnection();
			} catch (Exception e) {
				String fPath = urL;
				File file = new File(fPath);
				Document doc = Jsoup.parse(file, "UTF-8", baseURI);
				return doc;
			}

			BufferedReader in = null;
			/* Read webpage coontent */
			try {
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} catch (Exception e) {
				return null;
			}
			/* Read line by line */
			String inputLine = "";
			while ((inputLine = in.readLine()) != null) {
				inputLine = inputLine + in.readLine();
				if (inputLine.contains("DOCTYPE")) {
					inputLine = inputLine.replace("<!DOCTYPE html .*>", "<!DOCTYPE html>");
				}
				temp = temp + inputLine;

			}
			/* close BufferedReader */
			in.close();
			/* close HttpURLConnection */
			con.disconnect();
			return Jsoup.parse(temp);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String returnXpath(Document document, Element element) throws XPathExpressionException {
		xpath = "//";
		Attributes atrbs = element.attributes();
		String attrb;
		String temp = "";
		if (atrbs.size() == 0) {
			if (element.text().length() != 0) {
				temp = xpath + element.tagName() + "[text()='" + element.text() + "']";
				if (obj.elementCount(temp, xmlDoc) == 0)
					temp = xpath + element.tagName() + "[contains(text(),'" + element.text() + "')]";
			}
			if (!temp.isEmpty()) {
				try {
					int tot = obj.elementCount(temp, xmlDoc);
					if (tot == 1) {
						return temp;
					} else if (tot > 1) {
						temp = "";
					}
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
			}
		}
		boolean textDone = false;
		if (atrbs.size() != 0) {
			List<Attribute> atrbList = atrbs.asList();
			for (int i = 0; i < atrbList.size(); i++) {
				attrb = atrbList.get(i).toString();

				if (element.ownText().length() != 0 && !textDone) {
					temp = xpath + element.tagName() + "[contains(text(),\"" + element.text() + "\")]";
					if (obj.elementCount(temp, xmlDoc) == 0) {
						temp = xpath + element.tagName() + "[contains(text(),\"" + element.text() + "\")]";
						i = -1;
						textDone = true;
					} else {
						i = -1;
						textDone = true;
					}
				} else if (atrbs.hasKey("aria-label") && !atrbs.get("aria-label").isEmpty()) {
					temp = xpath + element.tagName() + "[@aria-label = \"" + atrbs.get("aria-label") + "\"]";
				} else if (atrbs.hasKey("value") && !atrbs.get("value").isEmpty())
					temp = xpath + element.tagName() + "[@value = \"" + atrbs.get("value") + "\"]";
				else if (atrbs.hasKey("name") && !atrbs.get("name").isEmpty() && !regExMatch(atrbs.get("name")))
					temp = xpath + element.tagName() + "[@name = \"" + atrbs.get("name") + "\"]";
				else if (atrbs.hasKey("id") && !atrbs.get("id").isEmpty() && !regExMatch(atrbs.get("id")))
					temp = xpath + element.tagName() + "[@id = \"" + atrbs.get("id") + "\"]";
				else if (atrbs.hasKey("title") && !atrbs.get("title").isEmpty())
					temp = xpath + element.tagName() + "[@title = \"" + atrbs.get("title") + "\"]";
				else if (atrbs.hasKey("placeholder") && !atrbs.get("placeholder").isEmpty())
					temp = xpath + element.tagName() + "[@placeholder = \"" + atrbs.get("placeholder") + "\"]";
				else if (atrbs.hasKey("class") && !atrbs.get("class").isEmpty() && !regExMatch(atrbs.get("class")))
					temp = xpath + element.tagName() + "[@class = \"" + atrbs.get("class") + "\"]";
				else if (atrbs.hasKey("alt") && !atrbs.get("alt").isEmpty())
					temp = xpath + element.tagName() + "[@alt = \"" + atrbs.get("alt") + "\"]";
				else if (atrbs.hasKey("role") && !atrbs.get("role").isEmpty())
					temp = xpath + element.tagName() + "[@role = \"" + atrbs.get("role") + "\"]";
				if (!temp.isEmpty()) {
					try {
						if (element.tagName().equals("td") || element.tagName().equals("tr")
								|| element.tagName().equals("table")) {
							int tot = obj.elementCount(temp, xmlDoc);
							if (tot == 1) {
								return temp;
							} else if (tot < 1 || tot > 1) {
								temp = "";
								continue;
							}
						} else {
							int tot = obj.elementCount(temp, xmlDoc);
							if (tot == 1 || tot > 1) {
								return temp;
							} else if (tot < 1) {
								temp = "";
								continue;
							}
						}
					} catch (XPathExpressionException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (xpath.isEmpty()) {
			for (Attribute atrb : atrbs) {
				attrb = atrb.getKey();
				if (attrb.equals("style"))
					continue;
				else {
					attrb = atrb.getKey();
					int size = document.getElementsByAttributeValue(attrb, atrb.getValue()).size();
					if (size == 1) {
						return xpath + element.tagName() + "[@" + attrb + "=\"" + atrb.getValue() + "\"]";
					} else if (size > 1) {
						return xpath + element.tagName() + "[@" + atrb.getKey() + "=\"" + atrb.getValue() + "\"]";
					}
				}
			}
		}
		return "";
	}

	public String absoluteXpathGenerator(Element element) throws XPathExpressionException {
		xpath = "//";
		String value = returnXpath(document, element);
		try {
			if (value.contains("[")) {
				String attribute = value.replace("//", "").split("@")[1].split("=")[0];
				String attributeValue = xpath.replace("//", "").split("@")[1].split("=")[1].replaceAll("'", "")
						.replaceAll("[a-zA-z0-9]?$", "");
				Elements list = document.getElementsByAttributeValue(attribute, attributeValue);
				if (list.size() != 0 && list.size() == 1) {
					return value;
				}
			} else {
				return generateAbsXpath(element);
			}
		} catch (Exception e) {
			System.out.println("Exception : Error while building xpath and please find the below error log : " + e.getMessage());
		}
		return "";
	}

	public String generateAbsXpath(Element element) throws XPathExpressionException {
		boolean status = true;
		xpath = "//";
		Element parent = element.parent();
		int size = parent.children().size();
		String tempXpath = "";
		while (status) {
			int count = 1;
			for (int i = 0; i < size; i++) {
				if (parent.children().get(i).tagName().equals("tbody")) {
					tempXpath = "//tbody" + tempVar;
				} else if (parent.children().get(i).equals(element)
						&& parent.children().get(i).tagName().equals(element.tagName())) {
					tempXpath = returnXpath(document, element);
					if (tempXpath.isEmpty()) {
						tempXpath = xpath + parent.children().get(i).tagName() + "[" + (count) + "]";
						tempXpath = tempXpath + tempVar;
						int list = obj.elementCount(tempXpath, xmlDoc);
						if (list != 0 && list == 1) {
							status = false;
							return tempXpath;
						} else if (list > 1) {
							tempVar = "";
							element = parent;
							parent = element.parent();
							size = parent.children().size();
							tempXpath = tempXpath.replaceAll("//", "/");
							tempVar = tempXpath;
							i = -1;
						}
					} else {
						tempXpath = tempXpath + tempVar;
						int list = obj.elementCount(tempXpath, xmlDoc);
						if (list != 0 && list == 1) {
							status = false;
							return tempXpath;
						} else if (list > 1) {
							tempVar="";
							element = parent;
							parent = element.parent();
							size = parent.children().size();
							tempXpath = tempXpath.replaceAll("//", "/");
							tempVar = tempXpath;
						}
					}
				}
				try {
					if (element.tagName().equals(parent.children().get(i).tagName()))
						count = count + 1;
				} catch (Exception countExcep) {
					// do nothing
					count = 1;
				}

			}
		}
		return "";
	}

	public static boolean regExMatch(String value) {
		try {
			return Pattern.compile("^[a-zA-Z0-9//s]+[A-Z|0-9]$").matcher(value).find();
		} catch (Exception e) {
			try {
				return Pattern.compile(".*_[A-Z|0-9]*").matcher(value).find();
			} catch (Exception ex) {
				try {
					return Pattern.compile("^[\\d]+$]").matcher(value).find();
				} catch (Exception exp) {
					return false;
				}
			}
		}

	}

	public static String getElementText(Element element) throws XPathExpressionException {
		String temp = xpath + element.tagName() + "/";
		Element parent = element;
		if (parent.text().isEmpty()) {
			return "";
		}
		if (element.ownText().equals(element.text())) {
			temp = xpath + element.tagName() + "[text()=\"" + element.ownText() + "\"]";
			if (obj.elementCount(temp, xmlDoc) == 1)
				return temp;
			else
				return xpath + element.tagName() + "[contains(text(),'" + element.ownText().trim() + "')]";
		} else if (element.ownText().isEmpty() && !element.text().isEmpty() && element.children().size() != 0) {
			int iter = 0;
			while (iter < 3) {
				Elements children = parent.children();
				for (Element child : children) {
					if (child.ownText().isEmpty()) {
						continue;
					} else if (parent.text().contains(child.ownText())) {
						String temp1 = temp;
						String value = child.ownText();
						if (value.contains("//") || value.contains("/")) {
							value = value.split("//")[1];
						}
						temp = temp1 + child.tagName() + "[text()=\"" + value + "\"]";

						if (obj.elementCount(temp, xmlDoc) != 0)
							return temp;
						else {
							temp = temp1 + child.tagName() + "[contains(text(),\"" + value + "\")]";
						}
					}
					if (obj.elementCount(temp, xmlDoc) == 1) {
						if (temp.substring(temp.length() - 1).equals("/"))
							return "";
						else
							return temp;
					}
				}
				for (Element child : children) {
					int count = 1;
					if (!child.text().isEmpty() && parent.text().contains(child.text())) {
						String path = "";
						try {
							path = htmlPage.returnXpath(document, child).replaceAll("//", "");
							if (!path.isEmpty())
								temp = temp + path + "/";
							else
								temp = temp + child.tagName() + "[" + count + "]/";
						} catch (Exception e) {
							temp = temp + child.tagName() + "[" + count + "]/";
						}
						parent = child;
						break;
					}
				}
				iter = iter + 1;
			}
		} else if (element.children().size() == 0) {
			temp = htmlPage.returnXpath(document, element);
			if (obj.elementCount(temp, xmlDoc) == 1)
				return temp;
			else
				return "";
		}
		return "";
	}
}
