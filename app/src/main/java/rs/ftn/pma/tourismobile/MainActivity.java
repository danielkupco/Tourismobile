package rs.ftn.pma.tourismobile;

import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById
    Button btnNavigation;

    @ViewById
    Button btnScroll;

    @Click
    void btnNavigation() {
        NavigationActivity_.intent(this).start();
    }

    @Click
    void btnScroll() {
        ScrollingActivity_.intent(this).start();
    }
}
