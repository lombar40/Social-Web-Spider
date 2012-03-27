import java.net.*;

interface ISpiderReportable {
	public boolean spiderFoundURL(URL base, URL url);

	public void spiderURLError(URL url);

	public void spiderFoundEMail(String email);
}