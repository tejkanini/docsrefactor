package reactors.email;

import java.util.Map;

import prerna.project.impl.ProjectProperties;
import prerna.sablecc2.om.PixelDataType;
import prerna.sablecc2.om.nounmeta.NounMetadata;
import prerna.sablecc2.reactor.AbstractReactor;
import prerna.util.EmailUtility;
import prerna.util.Utility;
import reactors.email.TutorialEmailBodyUtility.TutorialEmailTemplate;
import vha.elcm.VHAELCMPropHelper;
import vha.supply.email.VHAEmailBodyUtility;
import vha.supply.email.VHAEmailBodyUtility.EmailTemplate;
import vha.supply.utility.VHASuppConst;

/**
 * @author rylydzinski
 * Refer to <a href="https://repo.semoss.org/semoss-training/backend/-/blob/master/tutorials/Simple%20Email%20Tutorial.md">Simple Email tutorial markdown</a> 
 * This reactor demonstrates how to build and send an HTML email using the @EmailUtility class
 * The HTML file that will be sent in this example is SIMPLE_EMAIL.html located in the reactors > email folder
 * A recipient's email and name need to be passed in as input
 * 
 * This reactor can be tested in the pixel console in SEMOSS by running:
 * SimpleEmail ( email = [ "test@email.com" ] , name = [ "AJ Brown" ] ) ;
 */
public class SimpleEmailReactor extends AbstractReactor {

	// these keys are used to pass string inputs into the reactor
	public SimpleEmailReactor() {
		this.keysToGet = new String[] { "email", "name" };
	}

	@Override
	public NounMetadata execute() {
		// grab the user input and define variables
		organizeKeys();
		String email = this.keyValue.get(this.keysToGet[0]);
		String name = this.keyValue.get(this.keysToGet[1]);

		// grab project properties using helper methods
		// projectProps will be used to determine server settings, property file locations, etc. 
		String movieProjectId = "39fb01b3-c2d2-42ef-b95d-5a4d7f02a352";
		ProjectProperties projectProps = Utility.getProject(movieProjectId).getProjectProperties();

		// next we'll build a map to define dynamic strings that will be inserted into our email
		// we will be replacing any string that starts and ends with the $ symbol in our HTML file
		// the string as it appears in the HTML file is the key and the value is the string we want swapped in
		// in this example, the $NAME$ string at the top of our HTML file will be replaced with the value of the 'name' variable, which is determined by user input
		Map<String, String> replaceStrings = projectProps.getEmailStaticProps();
		replaceStrings.put(TutorialEmailBodyUtility.NAME, name);

		// now we can build the email by defining the subject, recipients, and email body
		String subject = "Simple Email Tutorial Subject";
		// generate email body using email body utility, string values to be inserted into the email are passed in here
		String emailBody = TutorialEmailBodyUtility.generateEmailBodyFromTemplate(TutorialEmailTemplate.SIMPLE_EMAIL_HTML.getTemplateContent(), null, replaceStrings);
		String[] recipients = {email};
		String[] ccRecipients = {"throwaway@example.com"};
		String[] bccRecepients = {"throwaway@example.com"};
		
		// finally, we call the 'sendEmail' method of the @EmailUtility class by passing in all of the required arguments that we defined prior
		// since we are using an HTML file as our email, be sure that the 'isHTML' boolean is set to true
		Boolean sendEmailResult = false;
		if (EmailUtility.sendEmail(projectProps.getEmailSession(), recipients, ccRecipients, bccRecepients,
				projectProps.getSmtpSender(), subject, emailBody, true, null)) {
			sendEmailResult = true;
		}
		NounMetadata noun = new NounMetadata(sendEmailResult, PixelDataType.BOOLEAN);

		return noun;
	}
}
