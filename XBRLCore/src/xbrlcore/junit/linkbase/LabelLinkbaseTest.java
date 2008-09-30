package xbrlcore.junit.linkbase;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import xbrlcore.constants.GeneralConstants;
import xbrlcore.junit.sax.TestHelper;
import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.DTSFactory;
import junit.framework.TestCase;

public class LabelLinkbaseTest extends TestCase {
	DiscoverableTaxonomySet prTaxonomy;

	DiscoverableTaxonomySet tstTaxonomy;

	LabelLinkbase labelLinkbase;

	DiscoverableTaxonomySet gvfdiTaxonomy;

	String PATH = "xbrl\\test\\taxonomy_original\\";

	String NAT_PATH_GVFDI = "xbrl\\test\\taxonomy_national\\gvfdi\\";

	public void setUp() {
		try {
			// DTSFactory taxonomyFactory = DTSFactory.get();
			// File prTaxonomyFile = new File(PATH + "p-pr-2006-12-31.xsd");
			// File tstTaxonomyFile = new File(PATH + "t-st-2006-12-31.xsd");
			// prTaxonomy = taxonomyFactory.createTaxonomy(prTaxonomyFile);
			// tstTaxonomy = taxonomyFactory.createTaxonomy(tstTaxonomyFile);
			// labelLinkbase = prTaxonomy.getLabelLinkbase();
			// gvfdiTaxonomy = taxonomyFactory.createTaxonomy(new File(
			// NAT_PATH_GVFDI + "t-gvfdi-2005-06.xsd"));
			prTaxonomy = TestHelper
					.getDTS("xbrl/test/taxonomy_original/p-pr-2006-12-31.xsd");
			tstTaxonomy = TestHelper
					.getDTS("xbrl/test/taxonomy_original/t-st-2006-12-31.xsd");
			labelLinkbase = prTaxonomy.getLabelLinkbase();
			gvfdiTaxonomy = TestHelper
					.getDTS("xbrl/test/taxonomy_national/gvfdi/t-gvfdi-2005-06.xsd");
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
			fail("Fehler beim Erstellen der Taxonomy pr: " + ex.getMessage());
		}
	}

	public void testGetLabel() {
		Concept elementWurst = prTaxonomy.getConceptByID("p-pr_Wurst");
		Concept elementBier = prTaxonomy.getConceptByID("p-pr_Bier");

		String wurstLabelDE = labelLinkbase.getLabel(elementWurst, "de", null);
		String wurstLabelEN = labelLinkbase.getLabel(elementWurst, "en",
				"http://www.xbrl.org/2003/role/link");

		String bierLabelDE = labelLinkbase.getLabel(elementBier, "de", null);
		String bierLabelES = labelLinkbase.getLabel(elementBier, "es",
				"http://www.xbrl.org/2003/role/link");

		assertEquals("Wurst", wurstLabelDE);
		assertEquals("Sausage", wurstLabelEN);
		assertEquals("Bier", bierLabelDE);
		assertNull(bierLabelES);
	}

	public void testGetLabelRole() {
		Concept elementWurst = prTaxonomy.getConceptByID("p-pr_Wurst");

		String wurstLabelDoku = labelLinkbase.getLabel(elementWurst, "de",
				GeneralConstants.XBRL_ROLE_LABEL_DOCUMENTATION, null);
		assertEquals("Eine andere Rolle", wurstLabelDoku);

		String wurstLabelNichts = labelLinkbase.getLabel(elementWurst, "de",
				"thisRoleDoesNotExist", null);
		assertNull(wurstLabelNichts);

		String wurstPositionLabel = labelLinkbase.getLabel(elementWurst,
				"http://xbrl.bundesbank.de/role/positionLabel");
		assertEquals("noch ne rolle", wurstPositionLabel);

		String wurstPositionLabel2 = tstTaxonomy.getLabelLinkbase().getLabel(
				elementWurst, "http://xbrl.bundesbank.de/role/positionLabel");
		assertEquals("noch ne rolle", wurstPositionLabel2);
	}

	public void testNationalLabel() {
		assertNotNull(gvfdiTaxonomy);
		Concept firstConcept = gvfdiTaxonomy
				.getConceptByID("p-gvfdi_GVFDIDomain");
		assertNotNull(gvfdiTaxonomy.getLabelLinkbase().getLabel(firstConcept,
				"de", null));

		Set conceptSet = gvfdiTaxonomy.getConcepts();
		Iterator conceptIterator = conceptSet.iterator();
		while (conceptIterator.hasNext()) {
			Concept currConcept = (Concept) conceptIterator.next();
			assertNull(gvfdiTaxonomy.getLabelLinkbase().getLabel(currConcept,
					"en", null));
		}
		Set languageSet = gvfdiTaxonomy.getLabelLinkbase().getLanguageSet();
		assertEquals(1, languageSet.size());
		assertTrue(gvfdiTaxonomy.getLabelLinkbase().containsLanguage("de"));
		assertFalse(gvfdiTaxonomy.getLabelLinkbase().containsLanguage("en"));
	}

	public void testGetLabel2() {
		Concept elementWurst = prTaxonomy.getConceptByID("p-pr_Wurst");
		assertNotNull(elementWurst);
		String label = labelLinkbase.getLabel(elementWurst,
				"http://xbrl.bundesbank.de/role/positionLabel");
		assertEquals("noch ne rolle", label);
	}
}