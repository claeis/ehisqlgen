package ch.ehi.sqlgen.repository;

public class DbColDate extends DbColumn {
    private java.sql.Date minValue=null;
    private java.sql.Date maxValue=null;
    public java.sql.Date getMinValue() {
        return minValue;
    }
    public void setMinValue(java.sql.Date minValue) {
        this.minValue = minValue;
    }
    public java.sql.Date getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(java.sql.Date maxValue) {
        this.maxValue = maxValue;
    }

}
