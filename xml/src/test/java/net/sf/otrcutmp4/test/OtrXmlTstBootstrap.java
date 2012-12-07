package net.sf.otrcutmp4.test;

import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.otrcutmp4.model.xml.OtrCutNsPrefixMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OtrXmlTstBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(OtrXmlTstBootstrap.class);
		
	public static void init()
	{
		LoggerInit loggerInit = new LoggerInit("log4j.xml");	
			loggerInit.addAltPath("config.otrcutmp4-xml.test");
			loggerInit.init();
		JaxbUtil.setNsPrefixMapper(new OtrCutNsPrefixMapper());
	}
}