package org.example;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

// The sole purpose of this class is simply to remove the _class attribute to maintain consistency with other components
// The _class attribute is added by spring, but not necessary for our purposes
@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory,
                                       MappingMongoConverter converter) {
        // sets the type mapper to null to remove the _class field
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(databaseFactory, converter);
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory databaseFactory,
                                                       MongoMappingContext context) {
        MappingMongoConverter converter = new MappingMongoConverter(databaseFactory, context);
        // sets the type mapper to null
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }
}
