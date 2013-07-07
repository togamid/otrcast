package net.sf.otrcutmp4.model.xml.mc;

import java.io.File;
import java.io.FileNotFoundException;

import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.otrcutmp4.test.OtrXmlTstBootstrap;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlStorage extends AbstractXmlMcTest
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlStorage.class);
	
	@BeforeClass
	public static void initFiles()
	{
		fXml = new File(rootDir,Storage.class.getSimpleName()+".xml");
	}
    
    @Test
    public void xml() throws FileNotFoundException
    {
    	Storage actual = create();
    	Storage expected = JaxbUtil.loadJAXB(fXml.getAbsolutePath(), Storage.class);
    	assertJaxbEquals(expected, actual);
    }
    
    public static Storage create()
    {
    	Storage xml = new Storage();
    	xml.setId(123);
    	xml.setHash("myHash");
    	xml.setSize(123);
    	xml.setName("myName");
    	
    	return xml;
    }
    
    public void save() {save(create(), fXml);}
	
	public static void main(String[] args)
    {
		OtrXmlTstBootstrap.init();
			
		TestXmlStorage.initXml();	
		TestXmlStorage.initFiles();
		TestXmlStorage test = new TestXmlStorage();
		test.save();
    }
}