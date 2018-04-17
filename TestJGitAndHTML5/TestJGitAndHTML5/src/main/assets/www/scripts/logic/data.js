
/*
	Author: Benjamin R. Olson
	Date: March 22, 2014
	Description: data management system
		to interface between UI and persistence methods:
		the following functions should bind to native methods
		that send and receive data and persist the data in local storage
*/

//variables, change to load from info passing in from java
var preferences = Android.loadFromFile("preferences.txt");//create this method in WebAppInterface.java
var fromLang = "en",//change to load from preferences json
	toLang = "en";//change to load from preferences json
var translation_text = "translation";//load string from obsTpage4.htm line 99

//saves the content from id into f (file)
function saveToFile(id, f)//'translation-text', 'translation.txt'
{
	var content = document.getElementById("translation-text").value;
	console.log(content);
	document.getElementById("debug").innerHTML=content;
	Android.saveToFile(content, f);
	//document.getElementById("debug").innerHTML=Android.loadFromFile(f);
}

//loads into var obj the given f (file) as a string
function loadFromFile(obj, f)
{
	obj = Android.loadFromFile(f);
	//obj = "hello";
	document.getElementById("debug").innerHTML=obj;
}

//functions
//used in obsTpage2.htm
//toOrFrom must equal "to" or "from" (see obsTpage2.htm lines 40 and 53
/*
function setLang(toOrFrom, lang)
{
	Android.setToFromLang(toOrFrom, lang);//save preferences to local storage
	if (toOrFrom == "from")
	{
		fromLang = lang;
		document.getElementById("from").innerHTML="Translate From: " + 
			document.getElementById(fromLang).innerHTML;
	}
	else
	{
		toLang = lang;
		document.getElementById("to").innerHTML="Translate To: " + 
			document.getElementById(toLang).innerHTML;
	}
	var cout = Android.setToFromLang(toOrFrom, lang);
	document.getElementById(toOrFrom).innerHTML=cout;
}
*/