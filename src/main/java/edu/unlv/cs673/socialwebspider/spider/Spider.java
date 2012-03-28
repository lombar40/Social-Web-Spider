package edu.unlv.cs673.socialwebspider.spider;

import java.util.*;
import java.net.*;
import java.io.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

/**
 * That class implements a reusable spider
 * 
 * @author Jeff Heaton(http://www.jeffheaton.com)
 * @version 1.0
 */
public class Spider {

	/**
	 * A collection of URLs that resulted in an error
	 */
	protected Collection workloadError = new ArrayList(3);

	/**
	 * A collection of URLs that are waiting to be processed
	 */
	protected Collection workloadWaiting = new ArrayList(3);

	/**
	 * A collection of URLs that were processed
	 */
	protected Collection workloadProcessed = new ArrayList(3);

	/**
	 * The class that the spider should report its URLs to
	 */
	protected ISpiderReportable report;

	/**
	 * A flag that indicates whether this process should be canceled
	 */
	protected boolean cancel = false;

	/**
	 * The constructor
	 * 
	 * @param report
	 *            A class that implements the ISpiderReportable interface, that
	 *            will receive information that the spider finds.
	 */
	public Spider(final ISpiderReportable report) {
		this.report = report;
	}

	/**
	 * Get the URLs that resulted in an error.
	 * 
	 * @return A collection of URL's.
	 */
	public final Collection getWorkloadError() {
		return workloadError;
	}

	/**
	 * Get the URLs that were waiting to be processed. You should add one URL to
	 * this collection to begin the spider.
	 * 
	 * @return A collection of URLs.
	 */
	public final Collection getWorkloadWaiting() {
		return workloadWaiting;
	}

	/**
	 * Get the URLs that were processed by this spider.
	 * 
	 * @return A collection of URLs.
	 */
	public final Collection getWorkloadProcessed() {
		return workloadProcessed;
	}

	/**
	 * Clear all of the workloads.
	 */
	public final void clear() {
		getWorkloadError().clear();
		getWorkloadWaiting().clear();
		getWorkloadProcessed().clear();
	}

	/**
	 * Set a flag that will cause the begin method to return before it is done.
	 */
	public final void cancel() {
		cancel = true;
	}

	/**
	 * Add a URL for processing.
	 * 
	 * @param url
	 */
	public final void addURL(final URL url) {
		if (getWorkloadWaiting().contains(url)) {
			return;
		}
		if (getWorkloadError().contains(url)) {
			return;
		}
		if (getWorkloadProcessed().contains(url)) {
			return;
		}
		log("Adding to workload: " + url);
		getWorkloadWaiting().add(url);
	}

	/**
	 * Called internally to process a URL
	 * 
	 * @param url
	 *            The URL to be processed.
	 */
	public final void processURL(final URL url) {
		URLConnection connection = null;
		InputStream is = null;
		Reader r = null;
		try {
			log("Processing: " + url);
			// get the URL's contents
			connection = url.openConnection();
			if ((connection.getContentType() != null) && !connection.getContentType().toLowerCase().startsWith("text/")) {
				getWorkloadWaiting().remove(url);
				getWorkloadProcessed().add(url);
				log("Not processing because content type is: " + connection.getContentType());
				return;
			}

			// read the URL
			is = connection.getInputStream();
			r = new InputStreamReader(is);
			// parse the URL
			HTMLEditorKit.Parser parse = new HTMLParse().getParser();
			parse.parse(r, new Parser(url), true);
			
		} catch (IOException e) {
			getWorkloadWaiting().remove(url);
			getWorkloadError().add(url);
			log("Error: " + url);
			report.spiderURLError(url);
			return;
		} finally {
			try {
				r.close();
				is.close();
				connection.getInputStream().close();
				connection.getOutputStream().close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		// mark URL as complete
		getWorkloadWaiting().remove(url);
		getWorkloadProcessed().add(url);
		log("Complete: " + url);

	}

	/**
	 * Called to start the spider
	 */
	public final void begin() {
		cancel = false;
		while (!getWorkloadWaiting().isEmpty() && !cancel) {
			Object[] list = getWorkloadWaiting().toArray();
			for (int i = 0; (i < list.length) && !cancel; i++) {
				processURL((URL) list[i]);
			}
		}
	}

	/**
	 * A HTML parser callback used by this class to detect links
	 * 
	 * @author Jeff Heaton
	 * @version 1.0
	 */
	protected class Parser extends HTMLEditorKit.ParserCallback {
		protected URL base;

		public Parser(final URL base) {
			this.base = base;
		}

		public final void handleSimpleTag(final HTML.Tag t, final MutableAttributeSet a, final int pos) {
			String href = (String) a.getAttribute(HTML.Attribute.HREF);

			if ((href == null) && (t == HTML.Tag.FRAME)) {
				href = (String) a.getAttribute(HTML.Attribute.SRC);
			}

			if (href == null) {
				return;
			}

			int i = href.indexOf('#');
			if (i != -1) {
				href = href.substring(0, i);
			}

			if (href.toLowerCase().startsWith("mailto:")) {
				report.spiderFoundEMail(href);
				return;
			}

			handleLink(base, href);
		}

		public final void handleStartTag(final HTML.Tag t, final MutableAttributeSet a, final int pos) {
			handleSimpleTag(t, a, pos); // handle the same way

		}

		protected final void handleLink(final URL base, final String str) {
			try {
				URL url = new URL(base, str);
				if (report.spiderFoundURL(base, url)) {
					addURL(url);
				}
			} catch (MalformedURLException e) {
				log("Found malformed URL: " + str);
			}
		}
	}

	/**
	 * Called internally to log information This basic method just writes the
	 * log out to the stdout.
	 * 
	 * @param entry
	 *            The information to be written to the log.
	 */
	public final void log(final String entry) {
		System.out.println((new Date()) + ":" + entry);
	}
}