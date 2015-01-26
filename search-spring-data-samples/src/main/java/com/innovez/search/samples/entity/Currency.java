package com.innovez.search.samples.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.innovez.core.search.annotation.Searchable;
import com.innovez.core.search.annotation.SearchableField;

@Searchable
@Entity
@Table(name="currency")
@SuppressWarnings("serial")
public class Currency implements Serializable {
	@Id
	@SearchableField(label="Currency Code")
	@Column(name="code")
	private String code;
	
	@SearchableField(label="Currency Name")
	@NotNull
	@Column(name="name")
	private String name;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Currency other = (Currency) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Currency [code=" + code + ", name=" + name + "]";
	}
	
	
}
