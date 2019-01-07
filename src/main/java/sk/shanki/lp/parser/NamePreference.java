/*
 * Copyright shanki. All rights reserved.
 */
package sk.shanki.lp.parser;

import java.util.Objects;

/**
 *
 * @author shanki
 */
public class NamePreference {
    private final String less;
    private final String more;

    public NamePreference(String less, String more) {
        this.less = less;
        this.more = more;
    }

    public String getLess() {
        return less;
    }
    
    public String getMore() {
        return more;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.less);
        hash = 17 * hash + Objects.hashCode(this.more);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NamePreference other = (NamePreference) obj;
        if (!Objects.equals(this.less, other.less)) {
            return false;
        }
        return Objects.equals(this.more, other.more);
    }

}