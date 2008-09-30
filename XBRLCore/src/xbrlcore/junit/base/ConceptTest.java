package xbrlcore.junit.base;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.jdom.Element;

import xbrlcore.junit.sax.TestHelper;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.DTSFactory;
import junit.framework.TestCase;

/**
 * 
 * This JUnit Test class tests methods and features of the class
 * xbrlcore.taxonomy.Concept.
 * 
 * @author Daniel Hamm
 */

public class ConceptTest extends TestCase {

	DiscoverableTaxonomySet prTaxonomy;

	DiscoverableTaxonomySet templateTaxonomy;

	DiscoverableTaxonomySet tmcTaxonomy;

	String PATH = "xbrl\\test\\taxonomy_original\\";

	String COREP_PATH = "xbrl\\taxonomies1.0\\";

	public void setUp() {
		try {
//			DTSFactory taxonomyFactory = DTSFactory.get();
//			File prTaxonomyFile = new File(PATH + "p-pr-2006-12-31.xsd");
//			prTaxonomy = taxonomyFactory.createTaxonomy(prTaxonomyFile);
//			templateTaxonomy = taxonomyFactory.createTaxonomy(new File(PATH
//					+ "t-st-2006-12-31.xsd"));
//			tmcTaxonomy = taxonomyFactory.createTaxonomy(new File(COREP_PATH
//					+ "t-mc-2005-12-31.xsd"));

			prTaxonomy = TestHelper
					.getDTS("xbrl/test/taxonomy_original/p-pr-2006-12-31.xsd");
			templateTaxonomy = TestHelper
					.getDTS("xbrl/test/taxonomy_original/t-st-2006-12-31.xsd");
			tmcTaxonomy = TestHelper
					.getDTS("xbrl/taxonomies1.0/t-mc-2005-12-31.xsd");

		} catch (Exception ex) {
			System.err.println(ex.toString());
			ex.printStackTrace();
			fail("Error when creating taxonomy pr: " + ex.getMessage());
		}
	}

	/**
	 * Tests core functions of Concept.
	 *  
	 */
	public void testConceptsCore() {
		Set conceptSet = prTaxonomy.getConcepts();

		/* 11 elements from p-pr and 2 elements from xbrldt */
		assertEquals(13, conceptSet.size());
		
		Concept essenElement = prTaxonomy.getConceptByID("p-pr_Essen");
		Concept wurstElement = prTaxonomy.getConceptByID("p-pr_Wurst");

		assertNotNull(essenElement);
		assertNotNull(wurstElement);

		assertEquals("Essen", essenElement.getName());
		assertEquals("p-pr_Essen", essenElement.getId());
		assertEquals("xbrli:stringItemType", essenElement.getType());
		assertEquals("xbrli:item", essenElement.getSubstitutionGroup());
		assertEquals("instant", essenElement.getPeriodType());
		assertTrue(essenElement.isAbstract());
		assertTrue(!wurstElement.isAbstract());
		assertTrue(essenElement.isNillable());
		assertEquals("p-pr-2006-12-31.xsd", essenElement
				.getTaxonomySchemaName());
	}

	/**
	 * Tests whether concepts can be derived from a DTS.
	 *  
	 */
	public void testConceptsFromImportedTaxonomies() {
		Concept essenElement = templateTaxonomy.getConceptByID("p-pr_Wurst");
		Concept usaElement = templateTaxonomy.getConceptByID("d-la_USA");
		assertNotNull(essenElement);
		assertNotNull(usaElement);
		assertEquals("d-la-2006-12-31.xsd", usaElement.getTaxonomySchemaName());
	}

	/**
	 * Tests whether other attributes can be derived from a concept.
	 *  
	 */
	/* DEPRECATED */
	//	public void testConceptsOtherAttributes() {
	//		Concept essenElement = prTaxonomy.getConceptByID("p-pr_Essen");
	//		Concept wurstElement = prTaxonomy.getConceptByID("p-pr_Wurst");
	//		
	//		assertEquals(0, essenElement.getOtherAttributes().size());
	//		assertEquals(2, wurstElement.getOtherAttributes().size());
	//		
	//		assertEquals("test1", wurstElement.getAttributeValue("test"));
	//		assertEquals("bar", wurstElement.getAttributeValue("foo"));
	//		
	//		String otherAttributeFooValue = wurstElement.getAttributeValue("foo");
	//		assertEquals("bar", otherAttributeFooValue);
	//	}
	public void testTypedDimension() {
		try {
			assertNotNull(tmcTaxonomy);

			Concept dimConcept = tmcTaxonomy
					.getConceptByID("t-mc_CommodityDimension");
			assertNotNull(dimConcept);

			Element typedElement = tmcTaxonomy
					.getElementForTypedDimension(dimConcept);
			assertNotNull(typedElement);
			assertEquals("Commodity", typedElement.getName());
		} catch (Exception ex) {
			System.err.println(ex.toString());
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}
}