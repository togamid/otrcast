package de.kisner.otrcast.controller.processor.hotfolder;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kisner.otrcast.controller.processor.hotfolder.TagFromFilenameProcessor;
import de.kisner.otrcast.controller.tag.deprecated.SeriesTagWriter2;
import de.kisner.otrcast.model.xml.series.Episode;
import de.kisner.otrcast.test.AbstractUtilTest;

public class TestTagFromFilenameProcessor extends AbstractUtilTest
{
	
	final static Logger logger = LoggerFactory.getLogger(TestTagFromFilenameProcessor.class);
	
	@Test @Ignore public void dummy(){}
	
	public static void main(String[] args) throws Exception
	{
		TagFromFilenameProcessor processor = new TagFromFilenameProcessor();
		String testfile = "src/test/resources/hotfolder/ENTER-S01-E01-TESTFILENAMEHERE.mp4";
		File input = new File(testfile);
		Episode episode = processor.createEpisodeFromFilename(input.getName());
		logger.info("Episode: " +episode.toString());
		
		SeriesTagWriter2 tagger = new SeriesTagWriter2();
		tagger.tagEpisode(input, episode, new File("target/new4" +input.getName()));
	}
	
	
}
