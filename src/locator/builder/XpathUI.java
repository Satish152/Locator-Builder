package locator.builder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;

public class XpathUI{

	static JFileChooser choose;
	static int returnValue;
	static Image img;
	static String selectedFile;
	static String url;
	static String inputPath;
	static String tagName;
	static String locType;
	public String expectedOutput;
	public static String fileUrl;

	public static void main(String args[]) {
		XpathUI xpathUI = new XpathUI();
		JPanel main = new JPanel(new BorderLayout(8, 8));
		JPanel labels = new JPanel(new GridLayout(0, 1, 4, 4));
		labels.add(new JLabel("Choose file / Provide URL :", SwingConstants.LEFT));
		labels.add(new JLabel("Select Locator type : ", SwingConstants.RIGHT));
		labels.add(new JLabel("Select the HTML tag ", SwingConstants.RIGHT));

		main.add(labels, BorderLayout.WEST);

		JPanel fields = new JPanel(new GridLayout(0, 1, 4, 4));
		// fields.setPreferredSize(new Dimension(300,90));
		JPanel input = new JPanel();
		JTextField userInput = new JTextField();
		userInput.setPreferredSize(new Dimension(110, 25));
		input.add(userInput);
		JButton cfButton = new JButton("Choose File");
		cfButton.setPreferredSize(new DimensionUIResource(110, 24));
		String[] options = { "Select Locator", "Classname", "id", "linktext", "name", "xpath" };
		JComboBox<String> combo = new JComboBox<String>(options);
		combo.setPreferredSize(new DimensionUIResource(100, 18));

		String[] tags = { "Select Tag", "a", "h1", "h3", "h4", "img", "input", "label", "li", "option", "select",
				"span", "table", "td", "tr"};
		JComboBox<String> comboTags = new JComboBox<String>(tags);
		comboTags.setPreferredSize(new DimensionUIResource(100, 18));
		input.add(cfButton);
		fields.add(input);
		fields.add(combo);
		fields.add(comboTags);
		main.add(fields, BorderLayout.CENTER);

		cfButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrame jF = new JFrame();
				choose = new JFileChooser();
				jF.setPreferredSize(new Dimension(450, 450));
				choose.showDialog(jF, "Choose File");
				jF.setSize(300, 300);
				try {
					System.out.println(choose.getSelectedFile().toString());
					selectedFile = choose.getSelectedFile().toString();
					userInput.setText(selectedFile);
				} catch (Exception exc) {
					selectedFile = "";
				}

				/*
				 * jF.add(choose); jF.setVisible(true);
				 */
			}
		});

		JButton bj = new JButton("Generate XPath");
		bj.setPreferredSize(new DimensionUIResource(140, 30));
		JButton bj1 = new JButton("Close");
		bj1.setPreferredSize(new DimensionUIResource(140, 30));

		bj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JLabel j = null;
				url = userInput.getText();
				if(!url.startsWith("http") || !url.startsWith("https")){
					url="";
				}
				locType = combo.getSelectedItem().toString();
				tagName = comboTags.getSelectedItem().toString();
				boolean executeOutput = false;
				JPanel output = new JPanel(new BorderLayout(4, 4));
				output.setPreferredSize(new DimensionUIResource(500, 350));
				JPanel resOpJP = new JPanel(new GridLayout(0, 1, 4, 4));
				resOpJP.add(new JLabel("Output : ", SwingConstants.LEFT));
				JButton downLoad = new JButton("Download in file");

				JPanel Message = new JPanel();
				if (url.isEmpty()
						&& (selectedFile != null && selectedFile.endsWith(".html") && !selectedFile.isEmpty())) {
					j = new JLabel("Selected File/URL is " + selectedFile, SwingConstants.RIGHT);
					inputPath = selectedFile;
					executeOutput = true;
				} else if ((url.startsWith("http") || url.startsWith("https"))
						&& (selectedFile == null || selectedFile.isEmpty())) {
					j = new JLabel("Selected File/URL is " + url, SwingConstants.RIGHT);
					inputPath = url;
					executeOutput = true;
				} else if ((url==null ||url.isEmpty()) && (selectedFile == null || selectedFile.isEmpty())) {
					JOptionPane.showMessageDialog(Message, "Please provide a valid html file / provide a URL", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (selectedFile != null && !selectedFile.isEmpty()) {
					if (selectedFile.contains("html") && !(new File(selectedFile).exists())) {
						JOptionPane.showMessageDialog(Message, "Please provide a valid html file path", "Error",
								JOptionPane.ERROR_MESSAGE);
						selectedFile = "";
					} else if (!selectedFile.endsWith(".html") && (new File(selectedFile).exists())) {
						JOptionPane.showMessageDialog(Message, "Please provide a valid html file", "Error",
								JOptionPane.ERROR_MESSAGE);
						selectedFile = "";
					}
				} else if (!url.isEmpty() && (!url.startsWith("http") || !url.startsWith("https"))) {
					JOptionPane.showMessageDialog(Message, "Please provide a valid URL", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

				if (locType.contains("Select Locator") || tagName.contains("Select Tag")) {
					executeOutput = false;
					JOptionPane.showMessageDialog(Message, "Locator Type and Tag is mandatory", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (!locType.contains("Select Locator") && !tagName.contains("Select Tag")) {
					executeOutput = true;
				}
				if (executeOutput) {
					xpathUI.expectedOutput = generateOutput(inputPath, tagName);
					if (xpathUI.expectedOutput.equals("No html tag found in the provided html")) {
						JOptionPane.showMessageDialog(new JPanel(), xpathUI.expectedOutput, "Information",
								JOptionPane.WARNING_MESSAGE);
					} else if(xpathUI.expectedOutput.contains("Error")){
						JOptionPane.showMessageDialog(new JPanel(), xpathUI.expectedOutput, "Error Message",
								JOptionPane.ERROR_MESSAGE);
					}else {
					downLoad.addActionListener(new ActionListener() {

						@SuppressWarnings("null")
						@Override
						public void actionPerformed(ActionEvent e) {
								JFrame jF = new JFrame();
								jF.setPreferredSize(new Dimension(450, 450));
								choose = new JFileChooser();
								choose.showSaveDialog(jF);
								choose.setCurrentDirectory(new File(System.getProperty("user.home")));
								choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
									String saveFile = "";
									try {
										saveFile = choose.getSelectedFile().getAbsolutePath();
										System.out.println(saveFile);
									} catch (Exception ex) {
										saveFile = "";
									}

									if ((saveFile != null || !saveFile.isEmpty())
											&& !saveFile.split(".txt")[0].isEmpty()) {
										try{
											FileOutputStream fout = new FileOutputStream(new File(saveFile));
											fout.write(xpathUI.expectedOutput.getBytes());
											fout.flush();
											fout.close();
										}catch(Exception fileErr){
											JOptionPane.showMessageDialog(new JPanel(),
													"Locator file not saved successfully because of no access to directory, please try with another directory","Information Message", JOptionPane.INFORMATION_MESSAGE);
										}
										
										if (new File(saveFile).exists()) {
											JOptionPane.showMessageDialog(new JPanel(),
													"Locator file saved successfully at :\n" + saveFile,
													"Confirmation Message", JOptionPane.INFORMATION_MESSAGE);
										}
									}
							}
					});

					j.setFont(new FontUIResource("Arial", Font.PLAIN, 10));
					resOpJP.add(j);
					output.add(resOpJP, BorderLayout.LINE_START);
					JPanel text = new JPanel(new GridLayout(0, 1));
					JTextArea jta = new JTextArea();
					jta.setText("");
					jta.setText(xpathUI.expectedOutput);
					jta.setLineWrap(true);
					jta.setRows(15);
					jta.setColumns(1);
					text.add(jta);
					output.add(text, BorderLayout.PAGE_END);
					JOptionPane.showOptionDialog(null, output, "XPATH Generator : Output", JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE, null, new Object[] { downLoad }, null);
				}
			}
		}
	});
		

		bj1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});

		// JOptionPane.showMessageDialog(jp, "Test");
		JOptionPane.showOptionDialog(null, main, "XPATH Generator", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, new Object[] { bj, bj1 }, null);

		// JOptionPane.showConfirmDialog(null, main, "XPATH Generator",
		// JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}

	public static String generateOutput(String url, String tagName) {
		XpathUITest output = new XpathUITest();

		try {
			return output.xpathBuilder(url, tagName);
		} catch (IOException | InterruptedException e) {
			return "Failed with exceotion, please try again";
		}
	}

}
