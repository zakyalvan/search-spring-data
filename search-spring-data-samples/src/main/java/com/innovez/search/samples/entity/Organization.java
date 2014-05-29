package com.innovez.search.samples.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.innovez.core.entity.support.search.annotation.Searchable;
import com.innovez.core.entity.support.search.annotation.SearchableField;

@Searchable
@Entity
@Table(name="organization")
@SuppressWarnings("serial")
public class Organization implements Serializable {
	@Id
	@SequenceGenerator(name="ORG_ID_SEQ_GENERATOR", sequenceName="ORGANIZATION_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ORG_ID_SEQ_GENERATOR")
	private Integer id;
	
	@SearchableField(label="Name")
	@NotBlank
	@Column(name="name")
	private String name;
	
	@SearchableField(label="Email Address")
	@Email
	@NotBlank
	@Column(name="email")
	private String email;
	
	@SearchableField(label="Contact Person")
	@NotBlank
	@Column(name="contact_person")
	private String contactPerson;
	
	@SearchableField
	@OneToMany(fetch=FetchType.LAZY, mappedBy="owner")
	private List<Asset> assets;
	
	@SearchableField
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="currency_code", referencedColumnName="code")
	private Currency usedCurrency;
	
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
	
	public List<Asset> getAssets() {
		return assets;
	}
	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}
	
	public Currency getUsedCurrency() {
		return usedCurrency;
	}
	public void setUsedCurrency(Currency usedCurrency) {
		this.usedCurrency = usedCurrency;
	}
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}
