package com.innovez.search.samples.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.innovez.core.entity.support.search.annotation.SearchField;
import com.innovez.core.entity.support.search.annotation.Searchable;

@Searchable
@Entity
@Table(name="organization")
public class Organization {
	@Id
	@SequenceGenerator(name="ORG_ID_SEQ_GENERATOR", sequenceName="ORGANIZATION_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ORG_ID_SEQ_GENERATOR")
	private Integer id;
	
	@SearchField(label="Name")
	@NotBlank
	@Column(name="name")
	private String name;
	
	@SearchField(label="Email Address")
	@Email
	@NotBlank
	@Column(name="email")
	private String email;
	
	@SearchField(label="Contact Person")
	@NotBlank
	@Column(name="contact_person")
	private String contactPerson;
	
	@Version
	@Column(name="version")
	private Integer version;

	public Organization() {}
	public Organization(String name, String email, String contactPerson) {
		this.name = name;
		this.email = email;
		this.contactPerson = contactPerson;
	}
	
	public Integer getId() {
		return id;
	}
	void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}
