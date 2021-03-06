package net.whydah.token.user;

import junit.framework.TestCase;
import net.whydah.token.application.ApplicationCredential;
import net.whydah.token.application.ApplicationToken;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

public class XPathTest extends TestCase {

    public void testCreateApplicationToken() throws Exception {
        ApplicationCredential cred = new ApplicationCredential();
        cred.setApplicationID("12345678");
        cred.setApplicationSecret("dummy");
        ApplicationToken imp = new ApplicationToken(cred.toXML());
        //System.out.println(imp.toXML());
        //System.out.println(imp.getApplicationID());
        //System.out.println(cred.getApplicationID());
        assertTrue("The generated application token is wrong.", imp.getApplicationID().equals(cred.getApplicationID()));
        assertTrue(imp.getApplicationTokenId().length() > 12);
        xpathParseAppToken(imp.toXML());
    }

    private void xpathParseAppToken(String appTokenXml) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(appTokenXml)));
        String expression = "/token/*/applicationtoken[1]";
        XPath xPath = XPathFactory.newInstance().newXPath();

        XPathExpression xPathExpression =
                xPath.compile(expression);
        //System.out.println("XML parse: ide =" + xPathExpression.evaluate(doc));
        //xPathExpression.evaluate(appTokenXml);

        Object result = xPathExpression.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        /*
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println("Nodelist: "+nodes.item(i).getNodeValue());
        }
        */
    }
}

