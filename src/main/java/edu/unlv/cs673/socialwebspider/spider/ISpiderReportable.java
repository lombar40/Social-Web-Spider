package edu.unlv.cs673.socialwebspider.spider;

import java.net.*;

interface ISpiderReportable {
	boolean spiderFoundURL(URL base, URL url);

	void spiderURLError(URL url);

	void spiderFoundEMail(String email);
}