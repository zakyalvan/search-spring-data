package com.innovez.search.samples.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name="asset")
@SuppressWarnings("serial")
public class Asset implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	
	@NotBlank
	@Column(name="code", unique=true)
	private String code;
	
	@NotBlank
	@Column(name="name")
	private String name;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date procureDate;
	
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="owner_id", referencedColumnName="id")
	private Organization owner;
	
	@Version
	private Integer version;
	
	public Integer getId() {
		return id;
	}
	void setId(Integer id) {
		this.id = id;
	}
	
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
	
	public Date getProcureDate() {
		return procureDate;
	}
	public void setProcureDate(Date procureDate) {
		this.procureDate = procureDate;
	}
	
	public Organization getOwner() {
		return owner;
	}
	public void setOwner(Organization owner) {
		this.owner = owner;
	}

	public Integer getVersion() {
		return version;
	}
}
