package ch.ehi.sqlgen.repository;

public class DbTableName {
	private String name=null;
	private String schema=null;
	public DbTableName(String name) {
		super();
		this.name = name;
	}
	public DbTableName(String schema,String name) {
		super();
		this.name = name;
		this.schema = schema;
	}
	public String getName() {
		return name;
	}
	public String getSchema() {
		return schema;
	}
	public String getQName() {
		return (schema==null?"":schema+".")+ name;
	}
	public String toString() {
		return (schema==null?"":schema+".")+ name;
	}
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((schema == null) ? 0 : schema.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DbTableName other = (DbTableName) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (schema == null) {
			if (other.schema != null)
				return false;
		} else if (!schema.equals(other.schema))
			return false;
		return true;
	}

}
