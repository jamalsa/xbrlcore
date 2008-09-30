package xbrlcore.junit.instance;

import java.io.File;

import xbrlcore.exception.InstanceException;
import xbrlcore.instance.Fact;
import xbrlcore.junit.sax.TestHelper;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DTSFactory;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import junit.framework.TestCase;

/**
 * @author Daniel Hamm
 * 
 * 
 */
public class FactTest extends TestCase {

	DiscoverableTaxonomySet prDTS;

	String PATH = "xbrl\\test\\taxonomy_original\\";

	public void setUp() {
		try {
			/* Creation of taxonomies */
//			 DTSFactory taxFactory = DTSFactory.get();
//			 File opTemplateTaxonomyFile = new File(PATH +
//			 "p-pr-2006-12-31.xsd");
//			 prDTS = taxFactory.createTaxonomy(opTemplateTaxonomyFile);
			prDTS = TestHelper
					.getDTS("xbrl/test/taxonomy_original/p-pr-2006-12-31.xsd");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	public void testFact() {
		try {
			Concept c = prDTS.getConceptByID("p-pr_Essen");
			try {
				Fact f = new Fact(c);
				fail("InstanceException not thrown");
			} catch (InstanceException ex) {
			}
		} catch (Exception ex) {
			fail(ex.toString());
		}
	}

	public void testPrecision() {
		try {
			Concept c = prDTS.getConceptByID("p-pr_Wurst");
			Fact f = new Fact(c);
			try {
				f.setPrecision("0");
			} catch (InstanceException ex) {
				fail(ex.toString());
			}
			try {
				f.setPrecision("INF");
			} catch (InstanceException ex) {
				fail(ex.toString());
			}
			try {
				f.setPrecision("hallo");
				fail("InstanceException not thrown");
			} catch (InstanceException ex) {
			}
			try {
				f.setPrecision("1");
			} catch (InstanceException ex) {
				fail(ex.toString());
			}
			try {
				f.setPrecision("-1");
				fail("InstanceException not thrown");
			} catch (InstanceException ex) {
			}

			Concept c2 = prDTS.getConceptByID("p-pr_Fleisch");
			c2.setNumericItem(false);
			Fact f2 = new Fact(c2);

			try {
				f2.setPrecision("1");
				// fail("InstanceException not thrown");
			} catch (InstanceException ex) {
			}
		} catch (Exception ex) {
			fail(ex.toString());
		}

	}

	public void testDecimals() {
		try {
			Concept c = prDTS.getConceptByID("p-pr_Wurst");
			Fact f = new Fact(c);
			try {
				f.setDecimals("0");
			} catch (InstanceException ex) {
				fail(ex.toString());
			}
			try {
				f.setDecimals("INF");
			} catch (InstanceException ex) {
				fail(ex.toString());
			}
			try {
				f.setDecimals("hallo");
				fail("InstanceException not thrown");
			} catch (InstanceException ex) {
			}
			try {
				f.setDecimals("1");
			} catch (InstanceException ex) {
				fail(ex.toString());

			}
			try {
				f.setDecimals("-1");
			} catch (InstanceException ex) {
				fail(ex.toString());
			}

			Concept c2 = prDTS.getConceptByID("p-pr_Fleisch");
			c2.setNumericItem(false);
			Fact f2 = new Fact(c2);

			try {
				f2.setDecimals("1");
				// fail("InstanceException not thrown");
			} catch (InstanceException ex) {
			}
		} catch (Exception ex) {
			fail(ex.toString());
		}
	}

}
