package rs.ftn.pma.tourismobile.network;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Header;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;

/**
 * HTTP client for DBPedia SPARQL service (SPARQL query engine)
 * Created by Daniel Kupčo on 04.06.2016.
 */
@Rest(rootUrl = APIServiceConstants.ROOT_URL, converters = {FormHttpMessageConverter.class, GsonHttpMessageConverter.class, StringHttpMessageConverter.class})
public interface RestDBPedia {

    @Header(name = "Content-Type", value = "application/x-www-form-urlencoded")
    @Post(value = APIServiceConstants.PARAMS)
    Object queryDBPedia(@Body LinkedMultiValueMap<String, String> body);

}
