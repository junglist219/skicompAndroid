package de.skicomp.utils.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.skicomp.data.manager.SkiAreaManager;
import de.skicomp.enums.SkiAreaCountry;
import de.skicomp.models.SkiArea;

/**
 * Created by benjamin.schneider on 26.05.17.
 */

public class SkiAreaHelper {

    private SkiAreaHelper() {
        // utility class
    }

    public static List<SkiAreaCountry> filterSkiAreaCountries(List<SkiArea> skiAreaList) {
        HashMap<String, Integer> skiAreaCountryMap = new HashMap<>();
        for (SkiArea skiArea : skiAreaList) {
            if (skiAreaCountryMap.containsKey(skiArea.getCountry())) {
                int updatedSkiAreaCountryCount = skiAreaCountryMap.get(skiArea.getCountry()) + 1;
                skiAreaCountryMap.put(skiArea.getCountry(), updatedSkiAreaCountryCount);
            } else {
                skiAreaCountryMap.put(skiArea.getCountry(), 1);
            }
        }

        List<SkiAreaCountry> skiAreaCountries = new ArrayList<>();
        for (Map.Entry<String, Integer> entrySet : skiAreaCountryMap.entrySet()) {
            skiAreaCountries.add(SkiAreaCountry.valueOf(entrySet.getKey().toUpperCase()));
        }

        return skiAreaCountries;
    }

    public static List<SkiArea> filterSkiAreasByCountry(List<SkiArea> skiAreaList, String countryName) {
        List<SkiArea> filteredSkiAreaList = new ArrayList<>();
        for (SkiArea skiArea : skiAreaList) {
            if (skiArea.getCountry().equalsIgnoreCase(countryName)) {
                filteredSkiAreaList.add(skiArea);
            }
        }

        if (skiAreaList.size() > 1) {
            Collections.sort(filteredSkiAreaList, new Comparator<SkiArea>() {
                @Override
                public int compare(SkiArea skiArea1, SkiArea skiArea2) {
                    return skiArea1.getName().compareToIgnoreCase(skiArea2.getName());
                }
            });
        }

        return filteredSkiAreaList;
    }

    public static boolean favoritesContainsSkiArea(SkiArea skiArea) {
        List<SkiArea> favoriteSkiAreasList = SkiAreaManager.getInstance().getFavoriteSkiAreas();
        for (SkiArea favoriteSkiArea : favoriteSkiAreasList) {
            if (favoriteSkiArea.getId() == skiArea.getId()) {
                return true;
            }
        }
        return false;
    }
}
