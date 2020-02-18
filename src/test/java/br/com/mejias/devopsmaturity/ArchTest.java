package br.com.mejias.devopsmaturity;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("br.com.mejias.devopsmaturity");

        noClasses()
            .that()
                .resideInAnyPackage("br.com.mejias.devopsmaturity.service..")
            .or()
                .resideInAnyPackage("br.com.mejias.devopsmaturity.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..br.com.mejias.devopsmaturity.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
