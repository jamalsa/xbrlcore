package xbrlcore.junit.base;

import java.io.File;

import xbrlcore.exception.TaxonomyCreationException;
import xbrlcore.junit.sax.TestHelper;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.DTSFactory;
import junit.framework.TestCase;

/**
 * 
 * This JUnit tests tries to load an invalid taxonomy, and it must fail.
 * 
 * @author Daniel Hamm
 */
public class InvalidTaxonomyTest extends TestCase {
	
	DiscoverableTaxonomySet tTaxonomy;
	String PATH = "xbrl\\test\\taxonomy_original\\";
	
	/**
	 * An error is expected when trying to load an invalid taxonomy. This taxonomy is invalid 
	 * because a hypercube item is not in the substitution group xbrldt:hypercubeItem.
	 *
	 */
	public void testHypercubeWrongSubstGroup() {
		try {
//			DTSFactory taxonomyFactory = DTSFactory.get();
//			File tTaxonomyFile = new File(PATH+"t-st-invalid-2006-12-31.xsd");
//			tTaxonomy = taxonomyFactory.createTaxonomy(tTaxonomyFile);
			tTaxonomy = TestHelper.getDTS("xbrl/test/taxonomy_original/t-st-invalid-2006-12-31.xsd");
			fail("TaxonomyCreationException should have been thrown!");
		}
		catch(TaxonomyCreationException ex) {
			System.out.println("Expected exception: " + ex.getMessage());
		}	
		catch(Exception ex) {
			fail("Error when creating taxonomy t: " + ex.getMessage());	
		}
	}	

}
