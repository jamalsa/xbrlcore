package xbrlcore.junit.instance;

import java.io.File;

import junit.framework.TestCase;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import xbrlcore.instance.Instance;
import xbrlcore.instance.InstanceFactory;
import xbrlcore.instance.InstanceOutputter;

/**
 * @author Daniel Hamm
 *
 */
public class XBRLInstanceFactoryTest extends TestCase {
	
	String PATH = "xbrl\\instances1.0\\";
	String TAX_PATH = "xbrl\\taxonomies1.0\\";
	Instance instance;
	Instance typedInstance;
	Instance instanceTest;
	
	public void setUp() {
		try {
			InstanceFactory instanceFactory = InstanceFactory.get();
			instance = instanceFactory.createInstance(new File(PATH+"ai_MKR.xml"));
			typedInstance = instanceFactory.createInstance(new File(PATH+"ai_MKR_COM.xml"));
		}
		catch(Exception ex) {
			System.err.println(ex.toString());
			ex.printStackTrace();
			fail(ex.toString());
		}
	}
	
	public void testPrintInstance() {
		assertNotNull(instance);
		
		try {
			InstanceOutputter instanceOutputter = new InstanceOutputter(instance);
			Document instanceXML = instanceOutputter.getXML();
			
			/* outputting XML */
			XMLOutputter serializer = new XMLOutputter();
			Format f = Format.getPrettyFormat();
			f.setOmitDeclaration(false);
			serializer.setFormat(f);
			serializer.output(instanceXML, System.out);	
		}
		catch(Exception ex) {
			fail(ex.toString());			
		}
	}
	
	public void testPrintInstanceTypedDimension() {
		assertNotNull(typedInstance);
		
		try {
			InstanceOutputter instanceOutputter = new InstanceOutputter(typedInstance);
			Document instanceXML = instanceOutputter.getXML();
			
			/* outputting XML */
			XMLOutputter serializer = new XMLOutputter();
			Format f = Format.getPrettyFormat();
			f.setOmitDeclaration(false);
			serializer.setFormat(f);
			serializer.output(instanceXML, System.out);	
		}
		catch(Exception ex) {
			ex.printStackTrace();
			fail(ex.toString());			
		}		
	}
}
