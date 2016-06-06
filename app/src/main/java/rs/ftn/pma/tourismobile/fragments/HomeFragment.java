package rs.ftn.pma.tourismobile.fragments;

import android.support.v4.app.Fragment;
import android.util.Log;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.util.LinkedMultiValueMap;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.network.ServiceDBPedia;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;

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
        Object result = serviceDBPedia.queryDBPedia(params);
        Log.e(TAG, "json");
        Log.e(TAG, DBPediaUtils.formatJson(result));

//        Log.e(TAG, "extracted json");
//        JsonArray json = DBPediaUtils.getResults(result);
//        Log.e(TAG, json.toString());
    }

}
