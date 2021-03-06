package rs.ftn.pma.tourismobile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rs.ftn.pma.tourismobile.views.FooterView_;
import rs.ftn.pma.tourismobile.views.IViewHolder;
import rs.ftn.pma.tourismobile.views.ViewWrapper;

/**
 * Generic abstract RecyclerViewAdapter class with header/footer support.
 * Created by danex on 5/12/16.
 * Based on <a href="https://github.com/excilys/androidannotations/wiki/Adapters-and-lists">this example</a>
 */
public abstract class RecyclerViewAdapterBase<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {

    private static final String TAG = RecyclerViewAdapterBase.class.getSimpleName();

    protected static final int TYPE_HEADER = 0;
    protected static final int TYPE_ITEM = 1;
    protected static final int TYPE_FOOTER = 2;

    protected boolean hasHeader = false;
    protected boolean hasFooter = false;

    protected RecyclerView recyclerView;
    protected List<T> items = new ArrayList<>();

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void addItems(List<T> items) {
        if(!items.isEmpty()) {
            this.items.addAll(items);
            // For efficiency purposes, notify the adapter of only the elements that got changed
            // curSize will equal to the index of the first element inserted because the list is 0-indexed
            int curSize = this.items.size() - items.size();
            // must check if layout is not yet computed to avoid exception to be thrown
            while (recyclerView.isComputingLayout()) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.notifyItemRangeInserted(curSize, items.size());
        }
    }

    public T getItem(int position) {
        if(hasHeader)
            position--;
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        int totalCount = 0;
        if(items != null)
            totalCount = items.size();
        if(hasHeader)
            totalCount++;
        if(hasFooter)
            totalCount++;
        return totalCount;
    }

    @Override
    public int getItemViewType (int position) {
        if(hasHeader && position == 0) {
            return TYPE_HEADER;
        } else if(hasFooter && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        else {
            return TYPE_ITEM;
        }
    }

    @Override
    public final ViewWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewWrapper<V>(onCreateItemView(parent, viewType));
        if(viewType == TYPE_HEADER) {
            return new ViewWrapper<>(FooterView_.build(parent.getContext()));
        } else if(viewType == TYPE_FOOTER) {
            return new ViewWrapper<>(FooterView_.build(parent.getContext()));
        } else if(viewType == TYPE_ITEM) {
            // force layout if necessary
            return new ViewWrapper<V>(layoutAfterCreate(parent, viewType));
        }
        else {
            throw new RuntimeException("Invalid view item type for recycler view!");
        }
    }

    /**
     * If view item is annotated, use this to inflate view with needed layout.<br>
     * Inflate method implementation is not working properly in generated class since parent is not passed as 2nd argument in inflate method.<br>
     * So, for example, view item's width can't match parent's (it's width takes only half of a screen).
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
        if ((hasHeader && position == 0) || (hasFooter && position == getItemCount() - 1))
            return;

        if (hasHeader)
            position--;

        V view = viewHolder.getView();
        T item = items.get(position);

        if (view instanceof IViewHolder) {
            ((IViewHolder) view).bind(item);
        }
    }

    /**
     * Inverse logic method for binding adapter to a recycler view.<br>
     * Use this method instead of <code>RecyclerView.setAdapter()</code> because this way we keep
     * a reference of the <code>RecyclerView</code> which is used to prevent throwing exception when updating
     * newly added items.
     * @param recyclerView
     */
    public void bindAdapterToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(this);
    }

}
