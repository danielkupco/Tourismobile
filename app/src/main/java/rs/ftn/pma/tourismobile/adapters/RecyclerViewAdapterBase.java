package rs.ftn.pma.tourismobile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.views.IBindedViewItem;
import rs.ftn.pma.tourismobile.views.ViewWrapper;

/**
 * Created by danex on 5/12/16.
 * Based on <a href="https://github.com/excilys/androidannotations/wiki/Adapters-and-lists">this example</a>
 */
public abstract class RecyclerViewAdapterBase<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {

    protected List<T> items = new ArrayList<>();

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewWrapper<V>(onCreateItemView(parent, viewType));
        // force layout if necessary
        return new ViewWrapper<V>(layoutAfterCreate(parent, viewType));
    }

    /**
     * If view item is annotated, user this to inflate view with needed layout.<br>
     * Inflate method implementation is not working properly in generated class since parent is not passed as 2nd argument in inflate method.<br>
     * So, for example, view item's width can't match parent's.
     * @param parent
     * @param viewType
     * @return view with forced layout
     */
    private V layoutAfterCreate(ViewGroup parent, int viewType) {
        V view = onCreateItemView(parent, viewType);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return view;
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(ViewWrapper<V> viewHolder, int position) {
        V view = viewHolder.getView();
        T item = items.get(position);

        if(view instanceof IBindedViewItem) {
            ((IBindedViewItem) view).bind(item);
        }
    }

    // additional methods to manipulate the items
}
