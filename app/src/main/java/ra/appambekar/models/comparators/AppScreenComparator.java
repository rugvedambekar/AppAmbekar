package ra.appambekar.models.comparators;

import java.util.Comparator;

import ra.appambekar.models.AppScreen;

/**
 * Created by rugvedambekar on 2016-04-20.
 */
public class AppScreenComparator implements Comparator<AppScreen> {
    @Override
    public int compare(AppScreen lhs, AppScreen rhs) {
        return lhs.getIndex() - rhs.getIndex();
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }
}
