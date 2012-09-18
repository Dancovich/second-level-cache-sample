package br.gov.frameworkdemoiselle.samples.second_level_cache_sample.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;

@Entity
@Cacheable(true)
@NamedQuery(
		name="findAllBookmarks"
		,query="select b from Bookmark b"
		
		//MUITO IMPORTANTE, para queries mesmo ativando o query_cache no persistence.xml, é necessário
		//ativar cada query individualmente passando ao hint abaixo o valor true.
		,hints={@QueryHint(name="org.hibernate.cacheable", value="true")}
		
		)
public class Bookmark implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/*
	 *  If you are using Glassfish then remove the strategy attribute
	 */
	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;
	
	@Column
	private String description;
	
	@Column
	private String link;
	
	public Bookmark() {
		super();
	}
	
	public Bookmark(String description, String link) {
		this.description = description;
		this.link = link;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}

}
