
package smarthome.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeviceRepository<T> {

    private final List<T> items = new ArrayList<>();

    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "Item cannot be null."
            );
        }
        items.add(item);
    }

    public T get(int index) {
        return items.get(index);
    }

    public boolean remove(T item) {
        return items.remove(item);
    }

    public List<T> getAll() {
        return Collections.unmodifiableList(items);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }
}
