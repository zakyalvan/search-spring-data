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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.innovez.core.search.annotation.FieldOverride;
import com.innovez.core.search.annotation.FieldOverrides;
import com.innovez.core.search.annotation.Searchable;
import com.innovez.core.search.annotation.SearchableField;

@Searchable
@Entity
@Table(name="organization")
@SuppressWarnings("serial")
public class Organization implements Serializable {
	@Id
	@SequenceGenerator(name="ORG_ID_SEQ_GENERATOR", sequenceName="ORGANIZATION_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ORG_ID_SEQ_GENERATOR")
	private Integer id;
	
	@SearchableField(label="Organization Name", order=1)
	@NotBlank
	@Column(name="name")
	private String name;
	
	@SearchableField(label="Organization Email", order=2)
	@Email
	@NotBlank
	@Column(name="email")
	private String email;
	
	@SearchableField(label="Manager", order=3)
	@FieldOverrides({
		@FieldOverride(name="fullName", field=@SearchableField(label="Manager Name", order=1)),
		@FieldOverride(name="emailAddress", field=@SearchableField(label="Manager Email", order=2))
	})
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="manager_id", referencedColumnName="id")
	private Person manager;
	
	@SearchableField(label="Contact Person", order=3)
	@FieldOverrides({
		@FieldOverride(name="fullName", field=@SearchableField(label="Contact Person Name", order=1)),
		@FieldOverride(name="emailAddress", field=@SearchableField(label="Contact Person Email", order=2))
	})
	@NotNull
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="contact_id", referencedColumnName="id")
	private Person contact;
	
	@SearchableField
	@FieldOverrides({
		@FieldOverride(name="code", field=@SearchableField(label="Asset Code")),
		@FieldOverride(name="name", field=@SearchableField(label="Asset Name"))
	})
	@OneToMany(fetch=FetchType.LAZY, mappedBy="owner")
	private List<Asset> assets;
	
	@SearchableField(order=4)
	@FieldOverrides({
		@FieldOverride(name="code", field=@SearchableField(label="Currency Code", order=1)),
		@FieldOverride(name="name", field=@SearchableField(label="Currency Name", order=2))
	})
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="currency_code", referencedColumnName="code")
	private Currency usedCurrency;
	
	@Version
	@Column(name="version")
	private Integer version;

	public Organization() {}
	public Organization(String name, String email) {
		this.name = name;
		this.email = email;
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
	
	public Person getManager() {
		return manager;
	}
	public void setManager(Person manager) {
		this.manager = manager;
	}
	
	public Person getContact() {
		return contact;
	}
	public void setContact(Person contact) {
		this.contact = contact;
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
