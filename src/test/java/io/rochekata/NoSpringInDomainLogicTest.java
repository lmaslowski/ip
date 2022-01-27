package io.rochekata;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.runner.RunWith;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = {"io.rochekata", "org.springframework"})
    public class NoSpringInDomainLogicTest {

    @ArchTest
    public static final ArchRule model_should_not_depend_on_spring =
            noClasses()
                    .that()
                    .resideInAPackage(
                            "..io.sampleproject..domain.model..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("org.springframework..");

    @ArchTest
    public static final ArchRule application_should_not_depend_on_spring =
            noClasses()
                    .that()
                    .resideInAPackage(
                            "..io.sampleproject..application..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("org.springframework..");


}