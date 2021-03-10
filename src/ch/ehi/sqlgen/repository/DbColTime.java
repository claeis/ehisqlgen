package ch.ehi.sqlgen.repository;

public class DbColTime extends DbColumn {
    private java.sql.Time minValue=null;
    private java.sql.Time maxValue=null;
    public java.sql.Time getMinValue() {
        return minValue;
    }
    public void setMinValue(java.sql.Time minValue) {
        this.minValue = minValue;
    }
    public java.sql.Time getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(java.sql.Time maxValue) {
        this.maxValue = maxValue;
    }

}
