package gov.hhs.gsrs.invitropharmacology;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import gsrs.GSRSDataSourceConfig;
import lombok.extern.slf4j.Slf4j;

// first link used in approach below.
// https://stackoverflow.com/questions/45663025/spring-data-jpa-multiple-enablejparepositories
// https://www.baeldung.com/spring-data-jpa-multiple-databases
// https://springframework.guru/how-to-configure-multiple-data-sources-in-a-spring-boot-application/
// https://www.kindsonthegenius.com/microservices/multiple-database-configuration-for-microservice-in-spring-boot/
// https://www.javadevjournal.com/spring-boot/multiple-data-sources-with-spring-boot/

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = InvitroPharmacologyDataSourceConfig.NAME_ENTITY_MANAGER,
        transactionManagerRef = InvitroPharmacologyDataSourceConfig.NAME_TRANSACTION_MANAGER,
        basePackages = {"gov.hhs.gsrs.invitropharmacology"}
)
@Slf4j
public class InvitroPharmacologyDataSourceConfig extends GSRSDataSourceConfig {
    //These 3 things and the basePackages above are the typical things that
    //may need to change if trying to make a new DataSourceConfig
    protected static final String[] BASE_PACKAGES = new String[] {"gov.hhs.gsrs.invitropharmacology"};
    protected static final String PERSIST_UNIT = "invitropharmacology";
  //  protected static final String DATASOURCE_PROPERTY_PATH_PREFIX = "spring";

    //In most other cases you will want this variable to be the same as the PERSIST_UNIT
    //as shown below:
    protected static final String DATASOURCE_PROPERTY_PATH_PREFIX = PERSIST_UNIT;

    // As-written, this shouldn't have to change when adapting
    // but there may be cases where it is desirable to change
    protected static final String DATASOURCE_PROPERTY_PATH_FULL = DATASOURCE_PROPERTY_PATH_PREFIX + ".datasource";

    //These below shouldn't have to change and are built from the constants above
    protected static final String NAME_DATA_SOURCE = PERSIST_UNIT + "DataSource";
    public static final String NAME_ENTITY_MANAGER = PERSIST_UNIT + "EntityManager";
    protected static final String NAME_DATA_SOURCE_PROPERTIES = PERSIST_UNIT + "DataSourceProperties";
    protected static final String NAME_TRANSACTION_MANAGER = PERSIST_UNIT + "TransactionManager";

    @Bean(name = NAME_ENTITY_MANAGER)
    // @Primary
    public LocalContainerEntityManagerFactoryBean getInvitroPharmacologyEntityManager(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier(NAME_DATA_SOURCE) DataSource defaultDataSource){
        return builder
                .dataSource(defaultDataSource)
                .packages(BASE_PACKAGES)
                .persistenceUnit(PERSIST_UNIT)
                .properties(additionalJpaProperties(DATASOURCE_PROPERTY_PATH_PREFIX))
                .build();
    }

    @Bean(NAME_DATA_SOURCE_PROPERTIES)
    @ConfigurationProperties(DATASOURCE_PROPERTY_PATH_FULL)
    public DataSourceProperties defaultDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(NAME_DATA_SOURCE)
    @ConfigurationProperties(DATASOURCE_PROPERTY_PATH_FULL)
    public DataSource defaultDataSource(@Qualifier(NAME_DATA_SOURCE_PROPERTIES) DataSourceProperties defaultDataSourceProperties) {
        return defaultDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = NAME_TRANSACTION_MANAGER)
    public JpaTransactionManager transactionManager(@Qualifier(NAME_ENTITY_MANAGER) EntityManagerFactory defaultEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(defaultEntityManager);
        return transactionManager;
    }
}