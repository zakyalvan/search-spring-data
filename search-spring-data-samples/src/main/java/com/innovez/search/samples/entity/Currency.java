package com.innovez.search.samples.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.innovez.core.entity.support.search.annotation.Searchable;
import com.innovez.core.entity.support.search.annotation.SearchableField;

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
}
