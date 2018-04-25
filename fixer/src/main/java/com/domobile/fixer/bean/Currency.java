package com.domobile.fixer.bean;

/**
 * Created by maikel on 2018/3/31.
 */

public class Currency {
    private String name;
    private String rate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null && null == name) {
            return true;
        }
        if (name == null) {
            return false;
        }
        if (!(obj instanceof Currency)) {
            return false;
        }
        Currency currency = (Currency) obj;
        return name.equals(currency.name) && rate == currency.rate;
    }
}
