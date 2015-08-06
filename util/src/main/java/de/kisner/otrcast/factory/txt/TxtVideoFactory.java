package de.kisner.otrcast.factory.txt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kisner.otrcast.model.xml.series.Video;

public class TxtVideoFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtVideoFactory.class);
	
	public static String build(Video video)
	{
		StringBuffer sb = new StringBuffer();
		
		if(video.isSetMovie())
		{
			sb.append("Movie: ");
		}
		else if(video.isSetEpisode())
		{
			sb.append("Series: ");
			sb.append(TxtEpisodeFactory.build(video.getEpisode()));
		}
		
		return sb.toString();
	}
}
