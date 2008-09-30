package xbrlcore.junit.instance;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import xbrlcore.constants.GeneralConstants;
import xbrlcore.constants.NamespaceConstants;
import xbrlcore.dimensions.MultipleDimensionType;
import xbrlcore.dimensions.SingleDimensionType;
import xbrlcore.exception.InstanceException;
import xbrlcore.instance.Fact;
import xbrlcore.instance.Instance;
import xbrlcore.instance.InstanceContext;
import xbrlcore.instance.InstanceFactory;
import xbrlcore.instance.InstanceUnit;
import xbrlcore.instance.InstanceUnitFactory;
import xbrlcore.instance.InstanceValidator;
import xbrlcore.junit.sax.TestHelper;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DTSFactory;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.taxonomy.TaxonomySchema;

public class XBRLInstanceTest extends TestCase {

	String PATH = "xbrl\\taxonomies1.0\\";

	String PATH2 = "xbrl\\test\\corep_instance\\";

	DiscoverableTaxonomySet opTemplateTaxonomy;

	TaxonomySchema opPrimaryTaxonomy;

	TaxonomySchema baDimensionTaxonomy;

	TaxonomySchema blDimensionTaxonomy;

	DiscoverableTaxonomySet mkrSaComTemplateTaxonomy;

	DiscoverableTaxonomySet tcs;

	Instance instance;

	Concept year3;

	Concept year2;

	Concept capitalRequirements;

	Fact year3Fact;

	Fact year2Fact;

	Fact capitalRequirementsFact;

	InstanceContext ctx;

	InstanceContext ctx2;

	MultipleDimensionType myMDT;

	public void setUp() {
		try {
			/* Creation of taxonomies */
			// DTSFactory taxFactory = DTSFactory.get();
			// File opTemplateTaxonomyFile = new File(PATH +
			// "t-op-2005-12-31.xsd");
			// opTemplateTaxonomy = taxFactory
			// .createTaxonomy(opTemplateTaxonomyFile);
			// tcs = taxFactory.createTaxonomy(new File(PATH
			// + "t-cs-2005-12-31.xsd"));
			opTemplateTaxonomy = TestHelper
					.getDTS("xbrl/taxonomies1.0/t-op-2005-12-31.xsd");
			tcs = TestHelper.getDTS("xbrl/taxonomies1.0/t-cs-2005-12-31.xsd");

			opPrimaryTaxonomy = opTemplateTaxonomy
					.getTaxonomySchema("p-op-2005-12-31.xsd");
			baDimensionTaxonomy = opTemplateTaxonomy
					.getTaxonomySchema("d-ba-2005-12-31.xsd");
			blDimensionTaxonomy = opTemplateTaxonomy
					.getTaxonomySchema("d-bl-2005-12-31.xsd");
			assertNotNull(opPrimaryTaxonomy);
			assertNotNull(opTemplateTaxonomy);
			assertNotNull(baDimensionTaxonomy);
			assertNotNull(blDimensionTaxonomy);

			// mkrSaComTemplateTaxonomy = taxFactory.createTaxonomy(new
			// File(PATH
			// + "t-mc-2005-12-31.xsd"));
			mkrSaComTemplateTaxonomy = TestHelper
					.getDTS("xbrl/taxonomies1.0/t-mc-2005-12-31.xsd");
			assertNotNull(mkrSaComTemplateTaxonomy);

			/* Creation of own MultipleDimensionType for testing purposes */
			Concept dimensionalElementBL = opTemplateTaxonomy
					.getConceptByID("t-op_BusinessLinesDimension");
			Concept domainElementBL = opTemplateTaxonomy
					.getConceptByID("d-bl_CorporateFinance");
			Concept dimensionalElementBA = opTemplateTaxonomy
					.getConceptByID("t-op_BankingActivitiesDimension");
			Concept domainElementBA = opTemplateTaxonomy
					.getConceptByID("d-ba_TotalBankingActivitiesSubjectSTA");

			assertNotNull(dimensionalElementBA);
			assertNotNull(dimensionalElementBL);
			assertNotNull(domainElementBA);
			assertNotNull(domainElementBL);

			myMDT = new MultipleDimensionType(dimensionalElementBL,
					domainElementBL);
			myMDT.addPredecessorDimensionDomain(new SingleDimensionType(
					dimensionalElementBA, domainElementBA));
			assertNotNull(myMDT.getDimensionElement());
			assertNotNull(myMDT.getDomainMemberElement());

			/* creation of instance */
			Set dtsSet = new HashSet();
			dtsSet.add(opTemplateTaxonomy);
			instance = new Instance(dtsSet);
			instance.addNamespace(NamespaceConstants.ISO4217_NAMESPACE);

			/* Creation of Concept objects */
			year3 = opTemplateTaxonomy.getConceptByID("p-op_Year3GrossIncome");
			year2 = opTemplateTaxonomy.getConceptByID("p-op_Year2GrossIncome");
			capitalRequirements = opTemplateTaxonomy
					.getConceptByID("p-op_CapitalRequirements");
			assertNotNull(year3);
			assertNotNull(year2);
			assertNotNull(capitalRequirements);

			/* Creation of 1. fact */
			year3Fact = new Fact(year3);
			assertNotNull(year3Fact);
			year3Fact.setDecimals("0");
			year3Fact.setValue("100");
			year3Fact.setInstanceUnit(InstanceUnitFactory.getUnit4217EUR());

			/* Creation of 2. fact */
			year2Fact = new Fact(year2);
			assertNotNull(year2Fact);
			year2Fact.setDecimals("0");
			year2Fact.setPrecision("INF");
			year2Fact.setValue("200");
			year2Fact.setInstanceUnit(InstanceUnitFactory.getUnit4217EUR());

			/* Creation of 3. fact */
			capitalRequirementsFact = new Fact(capitalRequirements, "1000");
			capitalRequirementsFact.setDecimals("0");
			capitalRequirementsFact.setInstanceUnit(InstanceUnitFactory
					.getUnit4217EUR());

			/* Creation of MultipleDimensionType objects */
			MultipleDimensionType mdt = new MultipleDimensionType(
					opTemplateTaxonomy
							.getConceptByID("t-op_BankingActivitiesDimension"),
					opTemplateTaxonomy
							.getConceptByID("d-ba_TotalBankingActivitiesSubjectBIA"));
			MultipleDimensionType mdt2 = new MultipleDimensionType(
					opTemplateTaxonomy
							.getConceptByID("t-op_BankingActivitiesDimension"),
					opTemplateTaxonomy
							.getConceptByID("d-ba_TotalBankingActivitiesSubjectSTAAlternative"));
			MultipleDimensionType mdt4 = new MultipleDimensionType(
					opTemplateTaxonomy
							.getConceptByID("t-op_BusinessLinesDimension"),
					opTemplateTaxonomy.getConceptByID("d-bl_CorporateFinance"));
			mdt4
					.addPredecessorDimensionDomain(new SingleDimensionType(
							opTemplateTaxonomy
									.getConceptByID("t-op_BankingActivitiesDimension"),
							opTemplateTaxonomy
									.getConceptByID("d-ba_TotalBankingActivitiesSubjectSTA")));

			/* Creation of 1. context */
			ctx = new InstanceContext("1");
			ctx.setIdentifierScheme("http://www.c-ebs.org/eu/fr/esrs/corep");
			ctx.setIdentifier("091328");
			ctx.setPeriodValue("2005-09-30");
			ctx.setDimensionalInformation(mdt, GeneralConstants.DIM_SCENARIO);

			/* Creation of 2. context */
			ctx2 = new InstanceContext("2");
			ctx2.setIdentifierScheme("http://www.c-ebs.org/eu/fr/esrs/corep");
			ctx2.setIdentifier("091328");
			ctx2.setPeriodValue("2005-09-30");
			ctx2.setDimensionalInformation(mdt4, GeneralConstants.DIM_SCENARIO);

			/* assigning context objects to the facts */
			year3Fact.setInstanceContext(ctx);
			year2Fact.setInstanceContext(ctx);
			capitalRequirementsFact.setInstanceContext(ctx2);
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	public void testCorepInstance() {
		try {
			Instance instance = InstanceFactory.get().createInstance(
					new File(PATH2 + "FW1_instance.xml"));
			InstanceValidator validtator = new InstanceValidator(new File(PATH2
					+ "FW1_instance.xml"));
			validtator.schemaValidation();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	public void testXBRLFact() {
		try {
			/* add facts to instance */
			year2Fact.setInstanceUnit(InstanceUnitFactory.getUnit4217EUR());
			instance.addFact(year3Fact);
			instance.addFact(year2Fact);
			instance.addFact(capitalRequirementsFact);

			/* a few tests ... */
			int numOfFacts = instance.getNumberOfFacts();
			int numOfContexts = instance.getNumberOfContexts();
			assertEquals(3, numOfFacts);
			assertEquals(2, numOfContexts);
			InstanceContext ctx_test1 = instance.getContext("1");
			InstanceContext ctx_test2 = instance.getContext("hallo");
			assertNotNull(ctx_test1);
			assertNull(ctx_test2);

			/* ... more sophisticated tests ... */
			assertTrue(instance.getFact(year3Fact.getConcept(), ctx
					.getDimensionalInformation(GeneralConstants.DIM_SCENARIO),
					GeneralConstants.DIM_SCENARIO) != null);
			assertTrue(instance.getFact(capitalRequirementsFact.getConcept(),
					myMDT, GeneralConstants.DIM_SCENARIO) != null);
			assertFalse(instance
					.getFact(
							capitalRequirementsFact.getConcept(),
							ctx
									.getDimensionalInformation(GeneralConstants.DIM_SCENARIO),
							GeneralConstants.DIM_SCENARIO) != null);

			/* more tests - getReportedValue */
			assertEquals("100", instance.getFact(year3, ctx).getValue());

			/* more test - getFact */
			assertSame(year3Fact, instance.getFact(year3, ctx));
			assertNotSame(year2Fact, instance.getFact(year2, ctx2));
			assertNull(instance.getFact(year2, ctx2));

			/* test whether a fact can be overriden */
			year3Fact.setValue("5000000");
			instance.addFact(year3Fact);
			assertEquals(3, instance.getNumberOfFacts());
			Fact tmpFact = instance.getFact(year3, ctx);
			assertEquals("5000000", tmpFact.getValue());

			/* this must not throw an exception! */
			instance.addContext(ctx);

			/* test whether a second context with the same id can be added */
			try {
				InstanceContext _ctx = new InstanceContext("1");
				_ctx
						.setIdentifierScheme("http://www.c-ebs.org/eu/fr/esrs/corep");
				_ctx.setIdentifier("091328");
				_ctx.setPeriodValue("2005-09-30");
				instance.addContext(_ctx);
				fail("Exception not thrown");
			} catch (InstanceException ex) {
				System.out.println("Expected excpetion: " + ex.toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.toString());
		}
	}

	public void testExceptions() {
		/* a few tests which should lead to exceptions */
		year3Fact = null;
		try {
			instance.addFact(year3Fact);
			fail("Exception not thrown!");
		} catch (InstanceException ex) {
			System.out.println("Expected exception: " + ex.toString());
		}

		InstanceUnit tmpUnit = year2Fact.getInstanceUnit();
		tmpUnit.setNamespaceURI(null);
		try {
			instance.addFact(year2Fact);
			fail("Exception not thrown!");
		} catch (InstanceException ex) {
			System.out.println("Expected exception: " + ex.toString());
		}

	}

}
