package net.sf.otrcutmp4.controller.batch.video;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.ahtutils.exception.processing.UtilsProcessingException;
import net.sf.exlp.exception.ExlpUnsupportedOsException;
import net.sf.exlp.util.io.FilenameIllegalCharRemover;
import net.sf.exlp.util.os.shell.ShellCmdMove;
import net.sf.exlp.util.xml.JaxbUtil;
import net.sf.otrcutmp4.AviToMp4;
import net.sf.otrcutmp4.controller.batch.AbstactBatchGenerator;
import net.sf.otrcutmp4.controller.factory.FileNameFactoy;
import net.sf.otrcutmp4.model.xml.series.Episode;
import net.sf.otrcutmp4.model.xml.series.Video;
import net.sf.otrcutmp4.util.OtrConfig;
import net.sf.otrcutmp4.util.OtrConfig.Dir;
import net.sf.otrcutmp4.util.OtrConfig.Template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.TemplateException;

public class TagGenerator extends AbstactBatchGenerator
{	
	final static Logger logger = LoggerFactory.getLogger(TagGenerator.class);
	
	private boolean tagMp4;
	
	public TagGenerator(OtrConfig cfg, AviToMp4.Profile profile,boolean tagMp4)
	{
		super(cfg, profile);
		this.tagMp4=tagMp4;
	}
	
	public List<String> mvToMp4(Video video) throws UtilsProcessingException
	{
		String sFrom = rpf.relativate(new File(cfg.getDir(Dir.TMP),"final.mp4"));
		String sTo = rpf.relativate(new File(cfg.getDir(Dir.MP4),buildFinalName(video)));
		
		if(tagMp4)
		{
			if(video.isSetEpisode() && video.getEpisode().isSetId())
			{
				return tag(video.getEpisode(),sTo);
			}
			else
			{
				logger.warn("Tagging is selected, but we don't have an episode and/or id");
			}
		}
		return move(sFrom,sTo);
	}
	
	public List<String> tag(Episode episode, String dstName) throws UtilsProcessingException
	{
		StringBuffer sb = new StringBuffer();
		sb.append(episode).append("-");
		sb.append(dstName);
		
		List<String> result = new ArrayList<String>();
		result.add(sb.toString());
		return result;
	}
	
	public List<String> move(String sFrom, String sTo) throws UtilsProcessingException
	{
		List<String> result = new ArrayList<String>();
			
		try
		{
			result.add(ShellCmdMove.moveFile(sFrom, sTo));
		}
		catch (ExlpUnsupportedOsException e) {throw new UtilsProcessingException(e.getMessage());}
	
		return result;
	}
		
	public String buildFinalName(Video video) throws UtilsProcessingException
	{
		logger.trace("buildFinalName");
		JaxbUtil.trace(video);
		String fileName;
		if(video.isSetEpisode())
		{
			try
			{
				FileNameFactoy fnf = new FileNameFactoy();
				fnf.initTemplate(cfg.getTemplate(Template.fnSeries));
				fileName = fnf.convert(video.getEpisode())+".mp4";
			}
			catch (TemplateException e) {throw new UtilsProcessingException(e.getMessage());}
			catch (IOException e) {throw new UtilsProcessingException(e.getMessage());}
			
		}
		else if(video.isSetMovie())
		{
			logger.trace("build Movie Name");
			fileName=video.getMovie().getName()+".mp4";
		}
		else
		{
			logger.warn("Unknown type");
			fileName="Unknown";
		}
		return FilenameIllegalCharRemover.convert(fileName); 
	}
}