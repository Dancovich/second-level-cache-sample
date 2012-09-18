package br.gov.frameworkdemoiselle.samples.second_level_cache_sample.persistence;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;

import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.util.Beans;

import br.gov.frameworkdemoiselle.samples.second_level_cache_sample.domain.Bookmark;

@PersistenceController
public class BookmarkDAO extends JPACrud<Bookmark, Long> {
	
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unchecked")
	public List<Bookmark> findCachedAll(){
		
		//A query nomeada abaixo foi definida na entidade Bookmark.
		Query q = getEntityManager().createNamedQuery("findAllBookmarks");
		List<Bookmark> retorno = q.getResultList();
		logQueryStatistics();
		return retorno;
		
		//Caso prefira criar as queries apenas nas classes DAO, aqui está como ativar o cache para elas
		/*
		Query cachedQuery = getEntityManager().createQuery("select b from Bookmark b");
		cachedQuery.setHint("org.hibernate.cacheable", "true");
		return cachedQuery.getResultList();
		*/
		
	}
	
	public Bookmark loadCachedBookmark(Long primaryKey){
		
		//A entidade já está com cache ativado, então um load direto via primary key vai sempre ativar o cache
		Bookmark retorno = getEntityManager().find(Bookmark.class, primaryKey);
		logQueryStatistics();
		return retorno;
		
	}
	
	public void logQueryStatistics(){
		//Este método é apenas para monitorarmos o andamento de nosso cache,
		//em produção nada assim pode estar ativo pois levantar estas estatísticas requer tempo
		Session s = getEntityManager().unwrap(Session.class);
		Statistics stats = s.getSessionFactory().getStatistics();
		
		Logger l = Beans.getReference(Logger.class);
		l.info("===================");
		
		for (String regionName : stats.getSecondLevelCacheRegionNames()){
			SecondLevelCacheStatistics secStats = stats.getSecondLevelCacheStatistics(regionName);
			
			l.info("["+regionName+"] Acertos: "+secStats.getHitCount());
			l.info("["+regionName+"] Falhas: "+secStats.getMissCount());
			l.info("["+regionName+"] Novas entradas: "+secStats.getPutCount());
		}
		
		l.info("===================");
	}
	
}
