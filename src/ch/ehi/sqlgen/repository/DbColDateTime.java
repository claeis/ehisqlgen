package ch.ehi.sqlgen.repository;

import ch.ehi.sqlgen.repository.DbColumn;

public class DbColDateTime extends DbColumn
{
    private java.sql.Timestamp minValue=null;
    private java.sql.Timestamp maxValue=null;
    public java.sql.Timestamp getMinValue() {
        return minValue;
    }
    public void setMinValue(java.sql.Timestamp minValue) {
        this.minValue = minValue;
    }
    public java.sql.Timestamp getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(java.sql.Timestamp maxValue) {
        this.maxValue = maxValue;
    }

}

