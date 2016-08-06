package de.kisner.otrcast.web.tvdb.processor;

import java.io.File;

import net.sf.exlp.util.xml.JDomUtil;
import net.sf.exlp.util.xml.JaxbUtil;

import org.apache.commons.configuration.Configuration;
import org.jdom2.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kisner.otrcast.AbstractOtrcastTest;
import de.kisner.otrcast.OtrUtilTestBootstrap;
import de.kisner.otrcast.model.xml.tvdb.Banners;
import de.kisner.otrcast.web.tvdb.TvDbQuery;
import de.kisner.otrcast.web.tvdb.processor.TvDbBannerFactory;

public class CliTvDbBannerFactory extends AbstractOtrcastTest
{
	final static Logger logger = LoggerFactory.getLogger(TvDbQuery.class);
	
	private Configuration config;
	
	public CliTvDbBannerFactory(Configuration config)
	{
		this.config=config;
	}
	
	public void de()
	{
		File f = new File(config.getString("test.xml.tvdb.banners"));
		logger.info("Using de.xml with "+f);
		Document doc = JDomUtil.load(f);
		
		TvDbBannerFactory fStructure = new TvDbBannerFactory(doc);
		Banners banners = fStructure.build();
		JaxbUtil.info(banners);
	}
	
    public static void main(String args[]) throws Exception
    {
        Configuration config = OtrUtilTestBootstrap.init();

        CliTvDbBannerFactory test = new CliTvDbBannerFactory(config);
        test.de();
    }
 }