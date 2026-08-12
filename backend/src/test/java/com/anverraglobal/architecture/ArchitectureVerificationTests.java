package com.anverraglobal.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.anverraglobal", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureVerificationTests {

    @ArchTest
    static final ArchRule platform_must_not_depend_on_business_modules =
            noClasses().that().resideInAPackage("..platform..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "..domain..",
                            "..application..",
                            "..adapter..",
                            "..contracts..",
                            "..events.."
                    )
                    .allowEmptyShould(true)
                    .because("Platform infrastructure must remain completely devoid of business logic and must not depend on business module internals.");

    @ArchTest
    static final ArchRule domain_must_be_pure =
            noClasses().that().resideInAPackage("..domain..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "org.springframework..",
                            "..platform..",
                            "..adapter.."
                    )
                    .allowEmptyShould(true)
                    .because("Domain layer must remain pure and free from framework and infrastructure dependencies.");

    @ArchTest
    static final ArchRule hexagonal_architecture_enforced = Architectures.onionArchitecture()
            .domainModels("..domain..")
            .domainServices("..domain..")
            .applicationServices("..application..")
            .adapter("inbound", "..adapter.inbound..")
            .adapter("outbound", "..adapter.outbound..")
            .allowEmptyShould(true); // Allow empty as modules are not yet implemented
}
