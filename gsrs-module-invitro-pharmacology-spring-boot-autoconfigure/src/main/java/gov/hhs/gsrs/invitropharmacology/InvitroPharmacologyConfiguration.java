package gov.hhs.gsrs.invitropharmacology;

import gov.hhs.gsrs.invitropharmacology.InvitroPharmacologyStarterEntityRegistrar;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(JpaRepositoriesAutoConfiguration.class)
@Import(InvitroPharmacologyStarterEntityRegistrar.class)
public class InvitroPharmacologyConfiguration {
}
