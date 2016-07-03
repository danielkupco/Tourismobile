package rs.ftn.pma.tourismobile.database;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import rs.ftn.pma.tourismobile.database.dao.wrapper.TagDAOWrapper;
import rs.ftn.pma.tourismobile.model.Tag;

/**
 * Created by Daniel Kupčo on 26.06.2016.
 */
@EBean
public class InitializeDB {

    @Bean
    TagDAOWrapper tagDAOWrapper;

    public void initData() {
        if(tagDAOWrapper.findAllDefaults().isEmpty()) {
            Tag tag;
            tag = new Tag("Country", "Tag that defines all countries in the world.", "rdf:type", "dbo:Country", true, true);
            tagDAOWrapper.createTag(tag);
            tag = new Tag("City", "Tag that defines all cities in the world.", "rdf:type", "dbo:City", true, true);
            tagDAOWrapper.createTag(tag);
            tag = new Tag("Park", "Tag that defines all parks in the world.", "rdf:type", "dbo:Park", true, true);
            tagDAOWrapper.createTag(tag);
            tag = new Tag("USA States", "Tag that defines all states of the USA.", "rdf:type", "yago:StatesOfTheUnitedStates", true, true);
            tagDAOWrapper.createTag(tag);
            tag = new Tag("Capitals in Europe", "Tag that defines all capitals in the Europe.", "rdf:type", "yago:CapitalsInEurope", true, true);
            tagDAOWrapper.createTag(tag);

            // custom
            tag = new Tag("Tag1", "Tag number one.");
            tagDAOWrapper.createTag(tag);
            tag = new Tag("Tag2", "Tag number two.");
            tagDAOWrapper.createTag(tag);
            tag = new Tag("Tag3", "Tag number three.");
            tagDAOWrapper.createTag(tag);
        }
    }

}
