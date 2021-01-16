# Locator-Builder

Locator Builder is a tool which provides you to generate xpath locators for the WebElements present in the webpage. unlike other apps, it gives you a comfort to get the xpath of 
saved html files (used when a html page required login to reach the target page. In this scenario, you can just save that html page and generate the locators for the web page). like other applications, it also have some limitations like it won't provide any xpath for the 'Style/Scripts' tags and also content inside the iframe whether it is direct html / 
saved html. it generate xpath for the some predefined attributes of a html tag and if that not able to generate relative xpath it will generate absoulte xpath for you.

# Additional Information

***Technologies Involved***
1.JSoup
2.XML Parser
3.Core Java
4.JOptionPane
5.Java XPath

***V 1.0 features***
1.As of now, it will generate only Xpath locator type.. other locator types like 'Id','Name','class' etc will be developed in the other features
2.It gives you a capability to save the generated xpath output in the desire file extensions
3.You can able to generate xpath for one tag in one iteration as in V 1.0, it doesn't have the option to select multiple tags at a time

***Usage Instructions***
1.Open the Locator Builder Application
2.Enter the Html url / by selecting the choose button, you can provide the html file
3.Select the any locator type (in V 1.0 you can select any locator type but it will generate only xpath, but you need to select atleast one locator type)
4.Select the any html tags available in the the Select tag dropdown and you can click on Generate button
5.It will open a output popup and you can save the output in a file by clicking the Download button
6. if you want to generate xpath for other tags.. just close the output popup and do the same process from Step 1 - Step 5

# Supporting Features

***Supporting Attributes in xpath generation***
1.Text
2.Class
3.Id
4.Name
5.value
6.placeholder
7.aria-label
8.title
9.alt
10.role


***Supporting html tags for xpath Generation***
1.a
2.h1
3.h2
4.h3
5.h4
6.img
7.input
8.label
9.li
10.option
11.select
12.span
13.table
14.td
15.tr


                       *********Hope You enjoy this tool and provide you feedback or any suggesstions to rongalasatish15@gmail.com *********



