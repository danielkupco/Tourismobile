package rs.ftn.pma.tourismobile.fragments;

import android.support.v4.app.Fragment;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.LinkedMultiValueMap;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.network.ServiceDBPedia;

@EFragment(R.layout.fragment_home)
public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getSimpleName();

    @RestService
    ServiceDBPedia serviceDBPedia;

    @Background
    @Click
    void btnDBPedia() {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String query = "SELECT DISTINCT * FROM <http://dbpedia.org>\n" +
                "WHERE  { \n" +
                "  ?destination a dbo:Park ;\n" +
                "    dbp:name ?name ;\n" +
                "    geo:lat ?lat ;\n" +
                "    geo:long ?long ;\n" +
                "    dbo:thumbnail ?thumbnail ;\n" +
                "    dbp:website ?website ;\n" +
                "    rdfs:comment ?comment ;\n" +
                "    dbo:abstract ?abstract .\n" +
                "  FILTER( lang(?comment)=\"en\" && lang(?abstract)=\"en\") .\n" +
                "}\n" +
                "ORDER BY ?name\n" +
                "LIMIT 10";
        params.set("query", query);
        params.set("format", "json");
//        Object result = serviceDBPedia.queryDBPedia(params);
//        Log.e(TAG, "json");
//        Log.e(TAG, DBPediaUtils.formatJson(result));

//        SPARQLBuilder sparqlBuilder = new SPARQLBuilder();
//        String sparql = sparqlBuilder.startQuery().select()
//                .from("http://dbpedia.org")
//                .startWhere()
//                .triplet("destination", "a", "dbo:Park", false)
//                .property("dbp:name").as("name")
//                .property("geo:lat").as("lat")
//                .property("geo:long").as("long")
//                .property("dbo:thumbnail").as("thumbnail")
//                .property("foaf:isPrimaryTopicOf").as("wikiLink")
//                .property("rdfs:comment").as("comment")
//                .property("dbo:abstract").as("abstract")
//                .filter("lang(?comment)=\"en\" && lang(?abstract)=\"en\"")
//                .endWhere()
//                .orderBy("name")
//                .limit(10)
//                .build();
//
//        String prettified = sparqlBuilder.prettify();
//
//        Log.e(TAG, sparql);
//        Log.e(TAG, prettified);
//        Log.e(TAG, query);
    }

}
