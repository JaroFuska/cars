/*
 * Copyright shanki. All rights reserved.
 */

package sk.shanki.lp.parser;

import java.util.ArrayList;
import sk.shanki.lp.Preference;
import sk.shanki.lp.Preferences;
import sk.shanki.lp.Rule;

/**
 *
 * @author shanki
 */
public class NamePreferences {
    
    private final ArrayList<NamePreference> preferences = new ArrayList<>();

    public void add(NamePreference preference) {
        preferences.add(preference);
    }

    public Preferences toPreferences(UniqueRuleNameMap ruleMap) {
        ArrayList<Preference> prefs = new ArrayList<>();
        
        for (NamePreference preference : preferences) {
            Rule less = ruleMap.get(preference.getLess());
            Rule more = ruleMap.get(preference.getMore());
            
            if (less != null && more != null) {
                prefs.add(new Preference(less, more));
            }
        }
        
        return new Preferences(prefs);
    }

}