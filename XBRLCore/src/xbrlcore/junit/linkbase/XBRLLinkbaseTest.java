package xbrlcore.junit.linkbase;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import xbrlcore.junit.sax.TestHelper;
import xbrlcore.linkbase.DefinitionLinkbase;
import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbase;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.DTSFactory;
import xbrlcore.xlink.Arc;
import xbrlcore.xlink.ExtendedLinkElement;
import xbrlcore.xlink.Locator;
import xbrlcore.xlink.Resource;
import junit.framework.TestCase;

public class XBRLLinkbaseTest extends TestCase {
	DiscoverableTaxonomySet prTaxonomy;

	DiscoverableTaxonomySet taxProhibitedLink;

	String PATH = "xbrl\\test\\taxonomy_original\\";

	String PATH2 = "xbrl\\test\\linkbase_test\\";

	public void setUp() {
		try {
			// DTSFactory taxonomyFactory = DTSFactory.get();
			// File prTaxonomyFile = new File(PATH + "p-pr-2006-12-31.xsd");
			// prTaxonomy = taxonomyFactory.createTaxonomy(prTaxonomyFile);
			// taxProhibitedLink = taxonomyFactory.createTaxonomy(new File(PATH2
			// + "tax_prohibited_link_2.xsd"));
			prTaxonomy = TestHelper
					.getDTS("xbrl/test/taxonomy_original/p-pr-2006-12-31.xsd");
			taxProhibitedLink = TestHelper
					.getDTS("xbrl/test/linkbase_test/tax_prohibited_link_2.xsd");
		} catch (Exception ex) {
			fail("Fehler beim Erstellen der Taxonomy pr: " + ex.getMessage());
		}
	}

	public void testGetXLinkElementByLabel() {
		LabelLinkbase labelLinkbase = prTaxonomy.getLabelLinkbase();

		List xLinkElementProdukteList = labelLinkbase.getExtendedLinkElements(
				"label_Produkte", null, null);
		assertEquals(1, xLinkElementProdukteList.size());
		ExtendedLinkElement xLinkElementProdukte = (ExtendedLinkElement) xLinkElementProdukteList
				.get(0);
		assertNotNull(xLinkElementProdukte);
		assertEquals("label_Produkte", xLinkElementProdukte.getLabel());
		assertEquals("http://www.xbrl.org/2003/role/link", xLinkElementProdukte
				.getExtendedLinkRole());
		/* since this XLinkElement is a label, it does not refer to any Concept */
		assertEquals("de", ((Resource) xLinkElementProdukte).getLang());
		assertEquals("Produkte (domain)", ((Resource) xLinkElementProdukte)
				.getValue());

		List xLinkElementProdukte2List = labelLinkbase.getExtendedLinkElements(
				"label_Produkte_2", null, null);
		assertEquals(1, xLinkElementProdukte2List.size());
		ExtendedLinkElement xLinkElementProdukte2 = (ExtendedLinkElement) xLinkElementProdukte2List
				.get(0);
		assertNotNull(xLinkElementProdukte2);
		assertEquals("Products (domain)", ((Resource) xLinkElementProdukte2)
				.getValue());

		List xLinkElementEssenList = labelLinkbase.getExtendedLinkElements(
				"Essen", "http://www.xbrl.org/2003/role/link", null);
		assertEquals(1, xLinkElementEssenList.size());
		ExtendedLinkElement xLinkElementEssen = (ExtendedLinkElement) xLinkElementEssenList
				.get(0);
		assertNotNull(((Locator) xLinkElementEssen).getConcept());
		assertEquals("p-pr_Essen", ((Locator) xLinkElementEssen).getConcept()
				.getId());
	}

	public void testGetXLinkElementByXBRLElement() {
		LabelLinkbase labelLinkbase = prTaxonomy.getLabelLinkbase();

		Concept elementEssen = prTaxonomy.getConceptByID("p-pr_Essen");
		ExtendedLinkElement xLinkElementEssen = labelLinkbase
				.getExtendedLinkElementFromBaseSet(elementEssen, null);
		assertNotNull(xLinkElementEssen);
		assertEquals("Essen", xLinkElementEssen.getLabel());
	}

	public void testGetTargetXLinkElements() {
		PresentationLinkbase presentationLinkbase = prTaxonomy
				.getPresentationLinkbase();

		Concept elementEssen = prTaxonomy.getConceptByID("p-pr_Essen");
		Concept elementGetraenke = prTaxonomy.getConceptByID("p-pr_Getraenke");
		List xLinkElementListEssen = presentationLinkbase
				.getTargetExtendedLinkElements(elementEssen, null);
		List xLinkElementListGetraenke = presentationLinkbase
				.getTargetExtendedLinkElements(elementGetraenke, null);
		assertEquals(2, xLinkElementListEssen.size());
		assertEquals(3, xLinkElementListGetraenke.size());

		ExtendedLinkElement xLinkElementWurst = (ExtendedLinkElement) xLinkElementListEssen
				.get(0);
		assertEquals("Wurst", xLinkElementWurst.getLabel());
		assertNotNull(((Locator) xLinkElementWurst).getConcept());
	}

	public void testGetSourceXLinkElements() {
		PresentationLinkbase presentationLinkbase = prTaxonomy
				.getPresentationLinkbase();

		Concept elementWurst = prTaxonomy.getConceptByID("p-pr_Wurst");
		List xLinkElementList = presentationLinkbase
				.getSourceExtendedLinkElements(elementWurst,
						"http://www.xbrl.org/2003/role/link");
		assertEquals(1, xLinkElementList.size());

		ExtendedLinkElement xLinkElementEssen = (ExtendedLinkElement) xLinkElementList
				.get(0);
		assertNotNull(xLinkElementEssen);
		assertEquals("Essen", xLinkElementEssen.getLabel());
		assertEquals("p-pr_Essen", ((Locator) xLinkElementEssen).getConcept()
				.getId());
	}

	public void testGetXArcList() {
		PresentationLinkbase presentationLinkbase = prTaxonomy
				.getPresentationLinkbase();

		List xArcList = presentationLinkbase.getArcBaseSet(
				"http://www.xbrl.org/2003/arcrole/parent-child", null);
		assertEquals(10, xArcList.size());

		Arc xArc = (Arc) xArcList.get(0);
		assertEquals("http://www.xbrl.org/2003/arcrole/parent-child", xArc
				.getArcrole());
		assertEquals("http://www.xbrl.org/2003/role/link", xArc
				.getExtendedLinkRole());
		assertNull(xArc.getTargetRole());
		assertEquals(new Float(1.0f), new Float(xArc.getOrder()));

		List arcroleList = new ArrayList();
		arcroleList.add("test");
		arcroleList.add("quatsch");
		arcroleList.add("http://www.xbrl.org/2003/arcrole/parent-child");
		List xArcList2 = presentationLinkbase.getArcBaseSet(arcroleList,
				"http://www.xbrl.org/2003/role/link");
		assertEquals(xArcList, xArcList2);
	}

	public void testBuildSourceNetwork() {
		DefinitionLinkbase defLinkbase = prTaxonomy.getDefinitionLinkbase();
		Set sourceNetwork = defLinkbase.buildSourceNetwork(prTaxonomy
				.getConceptByID("p-pr_Bier"),
				"http://xbrl.org/int/dim/arcrole/domain-member", null);
		assertEquals(3, sourceNetwork.size());
		Iterator sourceNetworkIterator = sourceNetwork.iterator();
		while (sourceNetworkIterator.hasNext()) {
			Concept currCon = ((Locator) (ExtendedLinkElement) sourceNetworkIterator
					.next()).getConcept();
			System.out.println(currCon.getId());
		}
	}

	public void testGetExtendedLinkElementFromBaseSet() {
		Concept elementEssen = prTaxonomy.getConceptByID("p-pr_Essen");
		Concept elementFleisch = prTaxonomy.getConceptByID("p-pr_Fleisch");

		Concept p0a = taxProhibitedLink.getConceptByID("p0_a");
		Concept p0b = taxProhibitedLink.getConceptByID("p0_b");

		assertNotNull(prTaxonomy.getDefinitionLinkbase()
				.getExtendedLinkElementFromBaseSet(elementEssen,
						"http://www.xbrl.org/2003/role/link2"));
		assertNull(prTaxonomy.getDefinitionLinkbase()
				.getExtendedLinkElementFromBaseSet(elementFleisch,
						"http://www.xbrl.org/2003/role/link2"));

		assertNotNull(taxProhibitedLink.getDefinitionLinkbase()
				.getExtendedLinkElementFromBaseSet(p0a, null));
		assertNull(taxProhibitedLink.getDefinitionLinkbase()
				.getExtendedLinkElementFromBaseSet(p0b, null));
	}

	public void testBuildTargetNetwork() {
		try {
			DefinitionLinkbase defLinkbase = prTaxonomy.getDefinitionLinkbase();
			Set targetNetwork = defLinkbase.buildTargetNetwork(prTaxonomy
					.getConceptByID("p-pr_Produkte"),
					"http://xbrl.org/int/dim/arcrole/domain-member", null);
			assertEquals(10, targetNetwork.size());
		} catch (Exception ex) {
			fail("Fehler beim Erstellen der Taxonomy pr: " + ex.getMessage());
		}
	}

	public void testGetArcForSourceLocator() {
		Concept elementEssen = prTaxonomy.getConceptByID("p-pr_Essen");
		Locator loc = (Locator) prTaxonomy.getDefinitionLinkbase()
				.getExtendedLinkElementFromBaseSet(elementEssen, null);
		Arc arc = prTaxonomy.getDefinitionLinkbase()
				.getArcForSourceLocator(loc);
		assertEquals("p-pr_Wurst", ((Locator) arc.getTargetElement())
				.getConcept().getId());

		Locator loc2 = (Locator) prTaxonomy.getDefinitionLinkbase()
				.getExtendedLinkElementFromBaseSet(elementEssen,
						"http://www.xbrl.org/2003/role/link2");
		Arc arc2 = prTaxonomy.getDefinitionLinkbase().getArcForSourceLocator(
				loc2);
		assertNull(arc2);

		Arc arc3 = prTaxonomy.getDefinitionLinkbase().getArcForSourceLocator(
				null);
		assertNull(arc3);
	}

	public void testGetArcForTargetLocator() {
		Concept elementWurst = prTaxonomy.getConceptByID("p-pr_Wurst");
		Locator loc = (Locator) prTaxonomy.getDefinitionLinkbase()
				.getExtendedLinkElementFromBaseSet(elementWurst, null);
		Arc arc = prTaxonomy.getDefinitionLinkbase()
				.getArcForTargetLocator(loc);
		assertEquals("p-pr_Essen", ((Locator) arc.getSourceElement())
				.getConcept().getId());

		Arc arc2 = prTaxonomy.getDefinitionLinkbase().getArcForTargetLocator(
				null);
		assertNull(arc2);
	}
}