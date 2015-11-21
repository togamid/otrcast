package net.sf.otrcutmp4.controller.processor.mc;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kisner.otrcast.controller.cover.FileSystemCoverManager;
import de.kisner.otrcast.controller.processor.mc.McLibraryTagger;
import de.kisner.otrcast.interfaces.controller.TestPropertyKeys;
import de.kisner.otrcast.interfaces.rest.OtrSeriesRest;
import de.kisner.otrcast.util.OtrBootstrap;
import de.kisner.otrcast.util.OtrConfig;
import de.kisner.otrcast.util.query.io.FileQuery;
import net.sf.ahtutils.web.rest.RestUrlDelay;

public class CliMcLibraryTagger
{
	final static Logger logger = LoggerFactory.getLogger(CliMcLibraryTagger.class);
	
	public static void main(String args[]) throws Exception
	{
		Configuration config = OtrBootstrap.init();

		File fLibrary = new File(config.getString(TestPropertyKeys.dirTaggerDst));
		File fTmp = new File(config.getString(TestPropertyKeys.dirTaggerTmp));
		File fBackup = new File(config.getString(TestPropertyKeys.dirMcBackup));
		File fCovers = new File(config.getString(OtrConfig.dirCover));
		
		File fMcXmlLib = new File(config.getString(OtrConfig.fileMcXmlLib));
		
		if(fLibrary.listFiles(FileQuery.mp4FileFilter()).length==0)
		{
			logger.warn("No Files in directory "+fLibrary.getAbsolutePath());
			logger.info("Probably you need to create some test files with CliMp4TagWriter");
			System.exit(-1);
		}
		
		String restUrl = RestUrlDelay.getUrl(config, OtrConfig.urlOtrSeries);
		
		McLibraryTagger tagger = new McLibraryTagger(fTmp,fBackup);
		tagger.setCoverManager(new FileSystemCoverManager(fCovers));
		tagger.scan(fLibrary);
		tagger.saveToXml(fMcXmlLib);
		
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(restUrl); 
		OtrSeriesRest rest = target.proxy(OtrSeriesRest.class);
	}
}