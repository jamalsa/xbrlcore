/*
 * Created on 18.02.2006
 * 
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package xbrlcore.junit.linkbase;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import xbrlcore.dimensions.Hypercube;
import xbrlcore.dimensions.MultipleDimensionType;
import xbrlcore.dimensions.Dimension;
import xbrlcore.dimensions.SingleDimensionType;
import xbrlcore.junit.sax.TestHelper;
import xbrlcore.linkbase.DefinitionLinkbase;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.DTSFactory;
import xbrlcore.taxonomy.TaxonomySchema;
import xbrlcore.xlink.ExtendedLinkElement;
import xbrlcore.xlink.Locator;
import junit.framework.TestCase;

public class DefinitionLinkbaseTest extends TestCase {
	DiscoverableTaxonomySet stTaxonomy;

	DefinitionLinkbase definitionLinkbase;

	String PATH = "xbrl\\test\\taxonomy_original\\";

	public void setUp() {
		try {
			// DTSFactory taxonomyFactory = DTSFactory.get();
			// File stTaxonomyFile = new File(PATH+"t-st-2006-12-31.xsd");
			// stTaxonomy = taxonomyFactory.createTaxonomy(stTaxonomyFile);
			// definitionLinkbase = stTaxonomy.getDefinitionLinkbase();

			stTaxonomy = TestHelper
					.getDTS("xbrl/test/taxonomy_original/t-st-2006-12-31.xsd");
			definitionLinkbase = stTaxonomy.getDefinitionLinkbase();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
			fail("Fehler beim Erstellen der Taxonomy pr: " + ex.toString());
		}
	}

	public void testHypercube() {
		assertNotNull(definitionLinkbase);

		Set hypercubeSet = definitionLinkbase.getHypercubeSet();
		assertEquals(3, hypercubeSet.size());

		Hypercube hcEuropa = stTaxonomy.getHypercube(stTaxonomy
				.getConceptByID("t-st_hcEuropa"));
		Hypercube hcAsien = stTaxonomy.getHypercube(stTaxonomy
				.getConceptByID("t-st_hcAsien"));
		Hypercube hcAmerika = stTaxonomy.getHypercube(stTaxonomy
				.getConceptByID("t-st_hcAmerika"));

		assertNotNull(hcEuropa);
		assertNotNull(hcAsien);
		assertNotNull(hcAmerika);

		Set hcEuropaDimensions = hcEuropa.getDimensionSet();
		assertEquals(2, hcEuropaDimensions.size());

		Iterator hcEuropaDimensionsIterator = hcEuropaDimensions.iterator();
		while (hcEuropaDimensionsIterator.hasNext()) {
			Dimension currDim = (Dimension) hcEuropaDimensionsIterator.next();
			Concept currDimElement = currDim.getConcept();
			assertTrue(currDimElement.getId().equals("t-st_KontinentDimension")
					|| currDimElement.getId().equals("d-la_LaenderDimension"));
			if (currDimElement.getId().equals("t-st_KontinentDimension")) {
				Set dimensionDomain = hcEuropa
						.getDimensionDomain(currDimElement);
				assertEquals(2, dimensionDomain.size());
				Concept koKontinente = stTaxonomy
						.getConceptByID("d-ko_Kontinente");
				Concept koEuropa = stTaxonomy.getConceptByID("d-ko_Europa");
				assertNotNull(koKontinente);
				assertNotNull(koEuropa);
				Iterator dimensionDomainIterator = dimensionDomain.iterator();
				while (dimensionDomainIterator.hasNext()) {
					ExtendedLinkElement currXLinkElement = (ExtendedLinkElement) dimensionDomainIterator
							.next();
					Concept currDomainElement = ((Locator) currXLinkElement)
							.getConcept();
					assertTrue(currDomainElement.getId().equals(
							"d-ko_Kontinente")
							|| currDomainElement.getId().equals("d-ko_Europa"));
				}
			} else if (currDimElement.getId().equals("t-st_LaenderDimension")) {
				Set dimensionDomain = hcEuropa
						.getDimensionDomain(currDimElement);
				assertEquals(4, dimensionDomain.size());
				Iterator dimensionDomainIterator = dimensionDomain.iterator();
				while (dimensionDomainIterator.hasNext()) {
					Concept currDomainElement = (Concept) dimensionDomainIterator
							.next();
					assertTrue(currDomainElement.getId().equals("d-la_Laender")
							|| currDomainElement.getId().equals(
									"d-la_Deutschland")
							|| currDomainElement.getId().equals("d-la_Spanien")
							|| currDomainElement.getId().equals("d-la_England"));
				}
			}
		}

		assertNotNull(stTaxonomy.getConceptByID("d-la_LaenderDimension"));
		assertTrue(hcEuropa.containsDimension(stTaxonomy
				.getConceptByID("d-la_LaenderDimension")));

		assertTrue(hcEuropa.containsDimensionDomain(stTaxonomy
				.getConceptByID("d-la_LaenderDimension"), stTaxonomy
				.getConceptByID("d-la_Deutschland")));
		assertTrue(hcAsien.containsDimensionDomain(stTaxonomy
				.getConceptByID("t-st_KontinentDimension"), stTaxonomy
				.getConceptByID("d-ko_Asien")));
		assertFalse(hcAmerika.containsDimensionDomain(stTaxonomy
				.getConceptByID("t-st_KontinentDimension"), stTaxonomy
				.getConceptByID("d-la_Deutschland")));

		assertTrue(hcAsien.containsDimensionDomain(stTaxonomy
				.getConceptByID("t-st_KontinentDimension"), stTaxonomy
				.getConceptByID("d-ko_Kontinente")));
		assertFalse(hcAsien.containsUsableDimensionDomain(stTaxonomy
				.getConceptByID("t-st_KontinentDimension"), stTaxonomy
				.getConceptByID("d-ko_Kontinente")));
	}

	public void testDimensions() {
		/*
		 * contains - Dimension: t-st_KontinentDimension Domain: d-ko_Europa
		 * Dimension: t-st_LaenderDimension Domain: d-la_Deutschland
		 */
		try {
			Concept dimKontinent = stTaxonomy
					.getConceptByID("t-st_KontinentDimension");
			Concept elemEuropa = stTaxonomy.getConceptByID("d-ko_Europa");
			assertNotNull(dimKontinent);
			assertNotNull(elemEuropa);

			Concept dimLaender = stTaxonomy
					.getConceptByID("d-la_LaenderDimension");
			Concept elemDeutschland = stTaxonomy
					.getConceptByID("d-la_Deutschland");
			assertNotNull(dimLaender);
			assertNotNull(elemDeutschland);

			MultipleDimensionType mdt1 = new MultipleDimensionType(dimLaender,
					elemDeutschland);
			mdt1.addPredecessorDimensionDomain(new SingleDimensionType(
					dimKontinent, elemEuropa));

			Concept primaryEssen = stTaxonomy.getConceptByID("p-pr_Essen");
			assertNotNull(primaryEssen);

			assertTrue(definitionLinkbase.dimensionAllowed(primaryEssen, mdt1,
					"scenario"));
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
			fail(ex.toString());
		}
	}

	public void testGetDimensionTaxonomy() {
		Concept elementKontinent = stTaxonomy
				.getConceptByID("t-st_KontinentDimension");
		Concept elementLand = stTaxonomy
				.getConceptByID("d-la_LaenderDimension");
		Concept elementCube = stTaxonomy.getConceptByID("t-st_hcEuropa");

		Set dimensionTaxonomySet = stTaxonomy.getDefinitionLinkbase()
				.getDimensionTaxonomy(elementKontinent);
		Set dimensionTaxonomyLandSet = stTaxonomy.getDefinitionLinkbase()
				.getDimensionTaxonomy(elementLand);

		Iterator dimensionTaxonomyIterator = dimensionTaxonomySet.iterator();
		Iterator dimensionTaxonomyLandIterator = dimensionTaxonomyLandSet
				.iterator();

		dimensionTaxonomyIterator.hasNext();
		dimensionTaxonomyLandIterator.hasNext();

		TaxonomySchema dimensionTaxonomy = (TaxonomySchema) dimensionTaxonomyIterator
				.next();
		TaxonomySchema dimensionTaxonomyLand = (TaxonomySchema) dimensionTaxonomyLandIterator
				.next();

		assertNotNull(dimensionTaxonomy);
		assertNotNull(dimensionTaxonomyLand);
		assertNotNull(elementCube);
		assertEquals("d-ko-2006-12-31.xsd", dimensionTaxonomy.getName());
		assertEquals("d-la-2006-12-31.xsd", dimensionTaxonomyLand.getName());
	}

	public void testGetDimensionElements() {
		Set dimensionsSt = stTaxonomy.getDimensionConceptSet();
		assertEquals(2, dimensionsSt.size());
	}

	public void testGetDimensionElement() {
		Concept d1 = stTaxonomy.getConceptByID("d-ko_Asien");
		Concept d2 = stTaxonomy.getConceptByID("d-la_Deutschland");
		Concept d3 = stTaxonomy.getConceptByID("t-st_hcEuropa");

		Concept D1 = stTaxonomy.getDefinitionLinkbase().getDimensionElement(d1);
		Concept D2 = stTaxonomy.getDefinitionLinkbase().getDimensionElement(d2);
		Concept D3 = stTaxonomy.getDefinitionLinkbase().getDimensionElement(d3);
		Concept D4 = stTaxonomy.getDefinitionLinkbase().getDimensionElement(
				null);

		assertEquals("t-st_KontinentDimension", D1.getId());
		assertEquals("d-la_LaenderDimension", D2.getId());
		assertNull(D3);
		assertNull(D4);
	}

	public void testIsUsableDomainMemberOfDimension() {
		Concept d1 = stTaxonomy.getConceptByID("d-ko_Asien");
		Concept d2 = stTaxonomy.getConceptByID("d-la_Deutschland");

		Concept D1 = stTaxonomy.getConceptByID("t-st_KontinentDimension");
		Concept D2 = stTaxonomy.getConceptByID("d-la_LaenderDimension");

		assertTrue(stTaxonomy.getDefinitionLinkbase()
				.isUsableDomainMemberOfDimension(D1, d1));
		assertTrue(stTaxonomy.getDefinitionLinkbase()
				.isUsableDomainMemberOfDimension(D2, d2));
		assertFalse(stTaxonomy.getDefinitionLinkbase()
				.isUsableDomainMemberOfDimension(null, d1));
		assertFalse(stTaxonomy.getDefinitionLinkbase()
				.isUsableDomainMemberOfDimension(D1, null));
		assertFalse(stTaxonomy.getDefinitionLinkbase()
				.isUsableDomainMemberOfDimension(null, null));
		assertFalse(stTaxonomy.getDefinitionLinkbase()
				.isUsableDomainMemberOfDimension(D1, d2));
		assertFalse(stTaxonomy.getDefinitionLinkbase()
				.isUsableDomainMemberOfDimension(D2, d1));
	}
}