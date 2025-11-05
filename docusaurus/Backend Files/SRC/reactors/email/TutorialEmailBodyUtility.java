package reactors.email;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vha.supply.email.VHAEmailTable;

/**
 * 
 * @author rylydzinski
 * email body utility to grab template content for Simple Email Tutorial
 * !!ensure that the emailTemplatesPath in the getTemplateContent method is correct for your setup
 */
public class TutorialEmailBodyUtility {
	// Builds a html-valid string that's used as the body of emails for visn approvals, shipment verifications, etc.
	
	private static final Logger logger = LogManager.getLogger(TutorialEmailBodyUtility.class);
	
	public static final String NAME = "$NAME$";
	
	public enum TutorialEmailTemplate {
		SIMPLE_EMAIL_HTML;
		
		/**
		 * This loads the template and gets the content
		 * @return
		 */
		public String getTemplateContent() {
			String emailTemplatesPath = "C:\\workspace\\vha-supply\\src\\reactors\\email\\";
			String templatePath = emailTemplatesPath + this.name().replace("_HTML", ".html");
			File templateFile = new File(templatePath);
			String template = null;
			try {
				template = FileUtils.readFileToString(templateFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return template;
		}
	}
	
	private static final String TABLES = "$TABLES$";
	private static final String TABLE_STYLING = "style='font-family:Arial, Helvetica, sans-serif; font-size: 16px; color: #1F2238;'";
	
	public static String generateEmailBodyFromTemplate(String emailTemplate, List<VHAEmailTable> tables, Map<String, String> replaceStrings) {
		return generateEmailBodyFromTemplate(emailTemplate, tables, replaceStrings, false);
	}
	
	public static String generateEmailBodyFromTemplate(String emailTemplate, List<VHAEmailTable> tables, Map<String, String> replaceStrings, boolean breakBetweenTables) {
		if(emailTemplate == null) {
			throw new IllegalArgumentException("Could not find the email template.");
		}
		// Replace any key strings in template with values in replaceStrings
		if (replaceStrings != null) {
			for (Map.Entry<String, String> entry : replaceStrings.entrySet()) {
				String key = entry.getKey();
				String replacementValue = entry.getValue();
				emailTemplate = emailTemplate.replace(key, replacementValue);
			}
		}
		
		// Add in any tables
		if (tables != null && tables.size() > 0) {
			StringBuilder tableSB = new StringBuilder();
			tableSB.append("<table align='center' border='0' cellpadding='0' cellspacing='0' width='100%' ");
			tableSB.append(TABLE_STYLING);
			tableSB.append(">");
			tableSB.append("<tbody>");
			
			for (VHAEmailTable table : tables) {
				tableSB.append(table.getTableAsHtml());
				if(breakBetweenTables) {
					tableSB.append("<br>");
				}
			}

			tableSB.append("</tbody>");
			tableSB.append("</table>");
			
			emailTemplate = emailTemplate.replace(TABLES, tableSB.toString());
		}
		
		// According to https://kb.benchmarkemail.com/using-css-in-html-emails/, most email services
		// ignore tags like body (and maybe head) so remove them
		emailTemplate = emailTemplate.replace("<head>", "");
		emailTemplate = emailTemplate.replace("</head>", "");
		emailTemplate = emailTemplate.replace("<body>", "");
		emailTemplate = emailTemplate.replace("</body>", "");
		
		return emailTemplate;
	}
}
