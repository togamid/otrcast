package net.sf.otrcutmp4.web.rest;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.ahtutils.exception.processing.UtilsProcessingException;
import net.sf.ahtutils.web.rest.auth.RestEasyPreemptiveClientExecutor;
import net.sf.exlp.exception.ExlpConfigurationException;
import net.sf.exlp.util.xml.JaxbUtil;

import org.apache.commons.configuration.Configuration;
import org.jboss.resteasy.client.ClientExecutor;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kisner.otrcast.interfaces.rest.OtrAdminRest;
import de.kisner.otrcast.model.xml.container.Otr;
import de.kisner.otrcast.model.xml.otr.Format;
import de.kisner.otrcast.model.xml.otr.Quality;
import de.kisner.otrcast.model.xml.series.Category;
import de.kisner.otrcast.model.xml.series.Series;
import de.kisner.otrcast.util.OtrBootstrap;

public class OtrRestSeedData
{
	final static Logger logger = LoggerFactory.getLogger(OtrRestSeedData.class);
	
	private OtrAdminRest restAdmin;
	private Configuration config;
	
	public OtrRestSeedData(Configuration config)
	{	
		this.config=config;
		
		String restUrl = config.getString("url.otrseries");
		logger.info("Connectiong to "+restUrl);
		
//		ResteasyClient client = new ResteasyClientBuilder().build();
//		ResteasyWebTarget target = client.target(restUrl);
//		restAdmin = target.proxy(OtrAdminRest.class);
		
		RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
		ClientExecutor clientExecutor = RestEasyPreemptiveClientExecutor.factory("user","pwd");
		restAdmin = ProxyFactory.create(OtrAdminRest.class, restUrl,clientExecutor);
	}
	
	public void all()
	{
		logger.warn("NYI");
//		Otr otr = restAdmin.allSeries();
//		JaxbUtil.debug(otr, new OtrCutNsPrefixMapper());
	}
		
	public void addCategories() throws FileNotFoundException
	{	
		Otr otr = (Otr)JaxbUtil.loadJAXB(config.getString(OtrBootstrap.cfgXmlCategories), Otr.class);
		JaxbUtil.debug(otr);
		for(Category category : otr.getCategory())
		{
			restAdmin.addCategory(category);
		}
	}
	
	public void addFormats() throws FileNotFoundException, UtilsProcessingException
	{
		Otr otr = (Otr)JaxbUtil.loadJAXB(config.getString(OtrBootstrap.cfgXmlFormats), Otr.class);
		JaxbUtil.debug(otr);
		for(Format format : otr.getFormat())
		{
			Format response = restAdmin.addFormat(format);
			logger.debug("Updated "+response.getType());
		}
	}
	
	public void addQualities() throws FileNotFoundException, UtilsProcessingException
	{
		Otr otr = (Otr)JaxbUtil.loadJAXB(config.getString(OtrBootstrap.cfgXmlQuality), Otr.class);
		JaxbUtil.debug(otr);
		for(Quality quality : otr.getQuality())
		{
			Quality response = restAdmin.addQuality(quality);
			JaxbUtil.debug(response);
		}
	}
	
	public void addSeries() throws FileNotFoundException, UtilsProcessingException
	{
		File dirEpisodes = new File(config.getString(OtrBootstrap.cfgXmlEpisodes));
		logger.info("Adding Episodes from "+dirEpisodes.getAbsolutePath());
		for(File f : dirEpisodes.listFiles())
		{
			if(f.getAbsolutePath().endsWith(".xml"))
			{
				logger.debug("\file: "+f.getAbsolutePath());
				Series series = (Series)JaxbUtil.loadJAXB(f.getAbsolutePath(), Series.class);
				logger.debug("\tSeasons: "+series.getSeason().size());
				series = restAdmin.addSeries(series);
				logger.debug("Updated "+series.getName());
			}
		}
	}
	
	public static void main(String[] args) throws ExlpConfigurationException, FileNotFoundException, UtilsProcessingException
	{
		Configuration config = OtrBootstrap.init();
		OtrRestSeedData rest = new OtrRestSeedData(config);
//		rest.all();
		
//		rest.addCategories();
//		rest.addFormats();
		rest.addSeries();
//		
//		rest.addQualities();
	}
}