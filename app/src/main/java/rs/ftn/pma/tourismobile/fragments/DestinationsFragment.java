package rs.ftn.pma.tourismobile.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.activities.DestinationFilterActivity_;
import rs.ftn.pma.tourismobile.adapters.DestinationsAdapter;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.services.IServiceActivity;
import rs.ftn.pma.tourismobile.services.IServiceBindingNotification;
import rs.ftn.pma.tourismobile.util.DBPediaUtils;
import rs.ftn.pma.tourismobile.util.EndlessRecyclerViewScrollListener;
import rs.ftn.pma.tourismobile.util.FilterPreferences_;

@EFragment(R.layout.fragment_destinations)
public class DestinationsFragment extends Fragment implements IServiceBindingNotification {

    private static final String TAG = DestinationsFragment.class.getSimpleName();

    private static final int REQUEST_FILTERS = 1;

    @Bean
    DBPediaUtils dbPediaUtils;

    @Bean
    DestinationsAdapter destinationsAdapter;

    @ViewById
    RecyclerView destinationsList;

    @ViewById
    MaterialProgressBar progressBar;

    @Pref
    FilterPreferences_ filterPreferences;

    private boolean firstTime = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_fragment_destination, menu);
    }

    @OptionsItem
    void actionFilter() {
        DestinationFilterActivity_.intent(this).startForResult(REQUEST_FILTERS);
    }

    @OnActivityResult(REQUEST_FILTERS)
    void onFiltersSuccess() {
        updateUIForQuery(true);
        queryDBPedia(0);
    }

    @AfterViews
    void bindAdapter() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        destinationsList.setHasFixedSize(true);

        // must set layout manager to recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        destinationsList.setLayoutManager(layoutManager);

        destinationsList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(!firstTime) {
                    updateUIForQuery(true);
                    queryDBPedia(page);
                }
                else {
                    firstTime = false;
                    destinationsList.scrollToPosition(0);
                    this.movePageUp();
                }
            }
        });

        // binding adapter to the view
        destinationsAdapter.bindAdapterToRecyclerView(destinationsList);
        queryDBPedia(0); // first page
    }

    @Background
    public void queryDBPedia(int page) {
        updateUIForQuery(true);
        if(!hasSelectedTagsNumber()) {
            updateUIForQuery(false);
            openFilterErrorDialog();
            return;
        }

        try {
            updateUIForQuery(true);
            IServiceActivity serviceActivity = ((IServiceActivity) getContext());
            if(serviceActivity.getDBPediaService() == null) {
                serviceActivity.notifyWhenServiceIsBinded(this);
            }
            else {
                List<Destination> destinationList = serviceActivity.getDBPediaService().queryDestinationsWithFilters(page);
                queryDBPediaSuccess(destinationList);
            }
        }
        catch (HttpClientErrorException e) {
            Log.e(TAG, "http client");
            e.printStackTrace();
            if(e.getStatusCode().is4xxClientError()) {
                toast("Sorry! It seems that request isn't valid...");
            }
        }
        catch (ResourceAccessException e) {
            e.printStackTrace();
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())) {
                    toast("It seems that you are not connected to internet!\nPlease check your connection and try again...");
                }
                else {
                    toast("Sorry! DBPedia service is unavailable at the moment!\nPlease try again later...");
                }
            }
            else {
                toast("It seems that you are not connected to internet!\nPlease check your connection and try again...");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Uncaught exception!");
        }
        finally {
            updateUIForQuery(false);
        }
    }

    @UiThread
    void queryDBPediaSuccess(List<Destination> destinationList) {
        destinationsAdapter.addItems(destinationList);
    }

    @UiThread
    void updateUIForQuery(boolean success) {
        // it happens sometimes that view is not injected yet when method is called
        if(progressBar != null) {
            if (success) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    @UiThread
    void toast(String message) {
        if(this.getContext() != null) {
            Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasSelectedTagsNumber() {
        return filterPreferences.bySelectedTags().exists() &&
                filterPreferences.bySelectedTags().get().split(",").length > 0;
    }

    @UiThread
    void openFilterErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getString(R.string.dialog_filter_error_title))
                .setMessage(getActivity().getString(R.string.dialog_filter_error_message));

        // Add the buttons
        builder.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                actionFilter();
            }
        });
        builder.setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Set other dialog properties

        // Create the AlertDialog
        builder.create().show();
    }

    @Override
    public void notifyWhenBinded() {
        queryDBPedia(0);
    }
}