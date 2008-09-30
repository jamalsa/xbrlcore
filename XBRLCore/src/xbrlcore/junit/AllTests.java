package xbrlcore.junit;

import xbrlcore.junit.base.*;
import xbrlcore.junit.dimensions.MultipleDimensionTypeTest;
import xbrlcore.junit.dimensions.DimensionTest;
import xbrlcore.junit.instance.FactTest;
import xbrlcore.junit.instance.InstanceTestTmp;
import xbrlcore.junit.instance.XBRLInstanceFactoryTest;
import xbrlcore.junit.instance.XBRLInstanceTest;
import xbrlcore.junit.linkbase.*;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for xbrlcore.junit");
		//$JUnit-BEGIN$
		suite.addTestSuite(DTSFactoryTest.class);
		suite.addTestSuite(ConceptTest.class);
		suite.addTestSuite(XBRLLinkbaseTest.class);
		suite.addTestSuite(LabelLinkbaseTest.class);
		suite.addTestSuite(PresentationLinkbaseTest.class);
		suite.addTestSuite(DefinitionLinkbaseTest.class);
		suite.addTestSuite(CalculationLinkbaseTest.class);
		suite.addTestSuite(DTSExtendedLinkbaseTests.class);
		suite.addTestSuite(InvalidTaxonomyTest.class);
		suite.addTestSuite(DimensionTest.class);
		suite.addTestSuite(FactTest.class);
		suite.addTestSuite(XBRLInstanceTest.class);
		suite.addTestSuite(MultipleDimensionTypeTest.class);
		suite.addTestSuite(XBRLInstanceFactoryTest.class);
		suite.addTestSuite(InstanceTestTmp.class);
		
		//$JUnit-END$
		return suite;
	}

}
