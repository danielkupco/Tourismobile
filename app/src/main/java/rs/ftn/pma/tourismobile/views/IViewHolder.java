package rs.ftn.pma.tourismobile.views;

/**
 * Interface for custom view group classes.<br>
 * Serves for implementation of ViewHolder pattern.
 * Created by danielkupco on 5/19/16.
 */
public interface IViewHolder<T> {
    void bind(T item);
}
