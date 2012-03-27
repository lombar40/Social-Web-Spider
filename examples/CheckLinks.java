import java.awt.Dimension;
import java.awt.Insets;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.SwingUtilities;

/**
 * This example uses a Java spider to scan a Web site and check for broken
 * links. Written by Jeff Heaton. Jeff Heaton is the author of "Programming
 * Spiders, Bots, and Aggregators" by Sybex. Jeff can be contacted through his
 * Web site at http://www.jeffheaton.com.
 * 
 * @author Jeff Heaton(http://www.jeffheaton.com)
 * @version 1.0
 */
public class CheckLinks extends javax.swing.JFrame implements Runnable,
		ISpiderReportable {

	/**
	 * Needed for serialization with Roo.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The constructor. Perform setup here.
	 */
	public CheckLinks() {
		// {{INIT_CONTROLS
		setTitle("Find Broken Links");
		getContentPane().setLayout(null);
		setSize(405, 288);
		setVisible(false);
		label1.setText("Enter a URL:");
		getContentPane().add(label1);
		label1.setBounds(12, 12, 84, 12);
		begin.setText("Begin");
		begin.setActionCommand("Begin");
		getContentPane().add(begin);
		begin.setBounds(12, 36, 84, 24);
		getContentPane().add(url);
		url.setBounds(108, 36, 288, 24);
		errorScroll.setAutoscrolls(true);
		errorScroll
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		errorScroll
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		errorScroll.setOpaque(true);
		getContentPane().add(errorScroll);
		errorScroll.setBounds(12, 120, 384, 156);
		errors.setEditable(false);
		errorScroll.getViewport().add(errors);
		errors.setBounds(0, 0, 366, 138);
		current.setText("Currently Processing: ");
		getContentPane().add(current);
		current.setBounds(12, 72, 384, 12);
		goodLinksLabel.setText("Good Links: 0");
		getContentPane().add(goodLinksLabel);
		goodLinksLabel.setBounds(12, 96, 192, 12);
		badLinksLabel.setText("Bad Links: 0");
		getContentPane().add(badLinksLabel);
		badLinksLabel.setBounds(216, 96, 96, 12);
		// }}

		// {{INIT_MENUS
		// }}

		// {{REGISTER_LISTENERS
		SymAction lSymAction = new SymAction();
		begin.addActionListener(lSymAction);
		// }}
	}

	/**
	 * Main method for the application
	 * 
	 * @param args
	 *            Not used
	 */
	public static void main(final String[] args) {
		(new CheckLinks()).setVisible(true);
	}

	/**
	 * Add notifications.
	 */
	public final void addNotify() {
		// Record the size of the window prior to calling parent's
		// addNotify.
		Dimension size = getSize();

		super.addNotify();

		if (frameSizeAdjusted) {
			return;
		}
		frameSizeAdjusted = true;

		// Adjust size of frame according to the insets and menu bar
		Insets insets = getInsets();
		javax.swing.JMenuBar menuBar = getRootPane().getJMenuBar();
		int menuBarHeight = 0;
		if (menuBar != null) {
			menuBarHeight = menuBar.getPreferredSize().height;
		}
		setSize(insets.left + insets.right + size.width, insets.top
				+ insets.bottom + size.height + menuBarHeight);
	}

	// Used by addNotify
	boolean frameSizeAdjusted = false;

	// {{DECLARE_CONTROLS
	javax.swing.JLabel label1 = new javax.swing.JLabel();

	/**
	 * The begin or cancel button
	 */
	javax.swing.JButton begin = new javax.swing.JButton();

	/**
	 * The URL being processed
	 */
	javax.swing.JTextField url = new javax.swing.JTextField();

	/**
	 * Scroll the errors.
	 */
	javax.swing.JScrollPane errorScroll = new javax.swing.JScrollPane();

	/**
	 * A place to store the errors created
	 */
	javax.swing.JTextArea errors = new javax.swing.JTextArea();
	javax.swing.JLabel current = new javax.swing.JLabel();
	javax.swing.JLabel goodLinksLabel = new javax.swing.JLabel();
	javax.swing.JLabel badLinksLabel = new javax.swing.JLabel();
	// }}

	// {{DECLARE_MENUS
	// }}

	/**
	 * The background spider thread
	 */
	protected Thread backgroundThread;

	/**
	 * The spider object being used
	 */
	protected Spider spider;

	/**
	 * The URL that the spider began with
	 */
	protected URL base;

	/**
	 * How many bad links have been found
	 */
	protected int badLinksCount = 0;

	/**
	 * How many good links have been found
	 */
	protected int goodLinksCount = 0;

	/**
	 * Internal class used to dispatch events
	 * 
	 * @author Jeff Heaton
	 * @version 1.0
	 */
	class SymAction implements java.awt.event.ActionListener {
		public void actionPerformed(final java.awt.event.ActionEvent event) {
			Object object = event.getSource();
			if (object == begin) {
				begin_actionPerformed(event);
			}
		}
	}

	/**
	 * Called when the begin or cancel buttons are clicked
	 * 
	 * @param event
	 *            The event associated with the button.
	 */
	final void begin_actionPerformed(final java.awt.event.ActionEvent event) {
		if (backgroundThread == null) {
			begin.setLabel("Cancel");
			backgroundThread = new Thread(this);
			backgroundThread.start();
			goodLinksCount = 0;
			badLinksCount = 0;
		} else {
			spider.cancel();
		}

	}

	/**
	 * Perform the background thread operation. This method actually starts the
	 * background thread.
	 */
	public final void run() {
		try {
			errors.setText("");
			spider = new Spider(this);
			spider.clear();
			base = new URL(url.getText());
			spider.addURL(base);
			spider.begin();
			Runnable doLater = new Runnable() {
				public void run() {
					begin.setText("Begin");
				}
			};
			SwingUtilities.invokeLater(doLater);
			backgroundThread = null;

		} catch (MalformedURLException e) {
			UpdateErrors err = new UpdateErrors();
			err.msg = "Bad address.";
			SwingUtilities.invokeLater(err);

		}
	}

	/**
	 * Called by the spider when a URL is found. It is here that links are
	 * validated.
	 * 
	 * @param base
	 *            The page that the link was found on.
	 * @param url
	 *            The actual link address.
	 */
	public final boolean spiderFoundURL(final URL base, final URL url) {
		UpdateCurrentStats cs = new UpdateCurrentStats();
		cs.msg = url.toString();
		SwingUtilities.invokeLater(cs);

		if (!checkLink(url)) {
			UpdateErrors err = new UpdateErrors();
			err.msg = url + "(on page " + base + ")\n";
			SwingUtilities.invokeLater(err);
			badLinksCount++;
			return false;
		}

		goodLinksCount++;
		if (!url.getHost().equalsIgnoreCase(base.getHost())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Called when a URL error is found
	 * 
	 * @param url
	 *            The URL that resulted in an error.
	 */
	public void spiderURLError(final URL url) {
	}

	/**
	 * Called internally to check whether a link is good
	 * 
	 * @param url
	 *            The link that is being checked.
	 * @return True if the link was good, false otherwise.
	 */
	protected final boolean checkLink(final URL url) {
		try {
			URLConnection connection = url.openConnection();
			connection.connect();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Called when the spider finds an e-mail address
	 * 
	 * @param email
	 *            The email address the spider found.
	 */
	public void spiderFoundEMail(final String email) {
	}

	/**
	 * Internal class used to update the error information in a Thread-Safe way
	 * 
	 * @author Jeff Heaton
	 * @version 1.0
	 */

	class UpdateErrors implements Runnable {
		public String msg;

		public void run() {
			errors.append(msg);
		}
	}

	/**
	 * Used to update the current status information in a "Thread-Safe" way
	 * 
	 * @author Jeff Heaton
	 * @version 1.0
	 */

	class UpdateCurrentStats implements Runnable {
		public String msg;

		public void run() {
			current.setText("Currently Processing: " + msg);
			goodLinksLabel.setText("Good Links: " + goodLinksCount);
			badLinksLabel.setText("Bad Links: " + badLinksCount);
		}
	}
}
