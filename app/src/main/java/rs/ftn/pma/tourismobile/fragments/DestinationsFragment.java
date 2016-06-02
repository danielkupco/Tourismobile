package rs.ftn.pma.tourismobile.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import rs.ftn.pma.tourismobile.R;
import rs.ftn.pma.tourismobile.adapters.DestinationsAdapter;
import rs.ftn.pma.tourismobile.model.Destination;
import rs.ftn.pma.tourismobile.repository.DestinationRepository;
import rs.ftn.pma.tourismobile.util.ItemClickSupport;

@EFragment(R.layout.fragment_destinations)
public class DestinationsFragment extends Fragment {

    @ViewById
    RecyclerView destinationsList;

    @Bean
    DestinationsAdapter destinationsAdapter;

    @Bean
    DestinationRepository destinationRepository;

    @AfterViews
    void bindAdapter() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        destinationsList.setHasFixedSize(true);

        // must set layout manager to recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        destinationsList.setLayoutManager(layoutManager);

        // binding adapter to the view
        destinationsList.setAdapter(destinationsAdapter);


        ItemClickSupport.addTo(destinationsList).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        // do it
                        Destination d = destinationRepository.getDestinations().get(position);
                        Toast.makeText(getActivity(), String.format("Click on %s", d.getName()), Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

}