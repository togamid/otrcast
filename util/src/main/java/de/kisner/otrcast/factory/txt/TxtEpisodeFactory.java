package de.kisner.otrcast.factory.txt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kisner.otrcast.model.xml.series.Episode;

public class TxtEpisodeFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtEpisodeFactory.class);
	
	public static String build(Episode episode){return build(episode,false);}
	public static String build(Episode episode, boolean withId)
	{
		StringBuffer sb = new StringBuffer();
		
		if(episode.isSetSeason() && episode.getSeason().isSetSeries())
		{
			sb.append(episode.getSeason().getSeries().getName());
			sb.append(" ").append(episode.getSeason().getNr());
			sb.append("x").append(episode.getNr());
			sb.append(" ").append(episode.getName());
			
		}
		if(withId && episode.isSetId()){sb.append(" otrc:").append(episode.getId());}
		
		return sb.toString();
	}
	
	public static String buld(String name)
	{
		
		return name;
	}
}
