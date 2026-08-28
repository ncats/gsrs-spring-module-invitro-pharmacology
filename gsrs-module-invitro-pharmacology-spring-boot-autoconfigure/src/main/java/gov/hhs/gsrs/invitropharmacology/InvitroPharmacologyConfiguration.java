package gov.hhs.gsrs.invitropharmacology;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.data.jpa.autoconfigure.DataJpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureAfter(DataJpaRepositoriesAutoConfiguration.class)
@Import(InvitroPharmacologyStarterEntityRegistrar.class)
public class InvitroPharmacologyConfiguration {
}
