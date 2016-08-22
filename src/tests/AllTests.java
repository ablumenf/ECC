package src.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ JacobiTest.class, ModExpTest.class, PointTest.class,
		PolynomialPointTest.class, PolynomialTest.class, PrimeTest.class,
		SqrtTest.class })
public class AllTests {

}