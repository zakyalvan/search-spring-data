package com.innovez.search.samples.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.innovez.core.search.annotation.Searchable;
import com.innovez.core.search.annotation.SearchableField;

@Searchable
@Entity
@Table(name="person")
public class Person {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@SearchableField(label="Person Name", order=1)
	@NotNull
	@Column(name="full_name")
	private String fullName;
	
	@SearchableField(label="Person Email", order=2)
	@NotNull
	@Column(name="email_address")
	private String emailAddress;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
