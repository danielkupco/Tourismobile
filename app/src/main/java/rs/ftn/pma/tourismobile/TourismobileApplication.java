package rs.ftn.pma.tourismobile;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

import rs.ftn.pma.tourismobile.database.InitializeDB;

/**
 * Created by danex on 5/12/16.
 */
@EApplication
public class TourismobileApplication extends Application {

    @Bean
    InitializeDB initializeDB;

    @Override
    public void onCreate() {
        super.onCreate();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true).build();

        // Initialization of the Fresco library. This is needed if you want to use Fresco.
        Fresco.initialize(this, config);

        initializeDB.initData();
    }
}
