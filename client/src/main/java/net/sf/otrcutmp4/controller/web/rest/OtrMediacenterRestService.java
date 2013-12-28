package net.sf.otrcutmp4.controller.web.rest;

import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;
import net.sf.exlp.util.DateUtil;
import net.sf.otrcutmp4.factory.xml.series.XmlMovieFactory;
import net.sf.otrcutmp4.factory.xml.series.XmlSeasonFactory;
import net.sf.otrcutmp4.factory.xml.series.XmlSeriesFactory;
import net.sf.otrcutmp4.interfaces.rest.OtrMediacenterRest;
import net.sf.otrcutmp4.model.*;
import net.sf.otrcutmp4.model.xml.container.Otr;
import net.sf.otrcutmp4.model.xml.mc.ServerStatus;
import net.sf.otrcutmp4.model.xml.series.Movie;
import net.sf.otrcutmp4.model.xml.series.Season;
import net.sf.otrcutmp4.model.xml.series.Series;
import net.sf.otrcutmp4.util.query.SeriesQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Date;

@Path("/rest/mc")
public class OtrMediacenterRestService extends AbstractOtrRestService implements OtrMediacenterRest
{
	final static Logger logger = LoggerFactory.getLogger(OtrMediacenterRestService.class);
	
	@Override
	public ServerStatus status()
	{
		ServerStatus status = new ServerStatus();
		status.setLastRestart(DateUtil.toXmlGc(new Date()));
		return status;
	}
	
	// ---------------------- Movies ----------------------

    @Override
    public Otr allMovies()
	{
		init();
		XmlMovieFactory<OtrMovie,OtrImage,OtrStorage> fMovie = new XmlMovieFactory<OtrMovie,OtrImage,OtrStorage>(SeriesQuery.get(SeriesQuery.Key.Movie));
		Otr otr = new Otr();
		for(OtrMovie ejb : ufb.all(OtrMovie.class))
		{
			otr.getMovie().add(fMovie.build(ejb));
		}
		return otr;
	}
	
	@Override
	public Movie movie(@PathParam("id") long movieId) throws UtilsNotFoundException
	{
		init();
		XmlMovieFactory<OtrMovie,OtrImage,OtrStorage> f = new XmlMovieFactory<OtrMovie,OtrImage,OtrStorage>(SeriesQuery.get(SeriesQuery.Key.MovieAll));
		OtrMovie ejb = ufb.find(OtrMovie.class, movieId);
		return f.build(ejb);
	}
	
	// ---------------------- Series ----------------------

    @Override
	public Otr allSeries()
	{
		init();
		XmlSeriesFactory<OtrSeries,OtrSeason,OtrEpisode,OtrImage,OtrStorage> f = new XmlSeriesFactory<OtrSeries,OtrSeason,OtrEpisode,OtrImage,OtrStorage>(SeriesQuery.get(SeriesQuery.Key.Series));
		Otr otr = new Otr();
		for(OtrSeries ejb : ufb.all(OtrSeries.class))
		{
			otr.getSeries().add(f.build(ejb));
		}
		return otr;
	}
	
	@Override
	public Series seriesAll(@PathParam("id") long seriesId) throws UtilsNotFoundException
	{
		init();
		XmlSeriesFactory<OtrSeries,OtrSeason,OtrEpisode,OtrImage,OtrStorage> f = new XmlSeriesFactory<OtrSeries,OtrSeason,OtrEpisode,OtrImage,OtrStorage>(SeriesQuery.get(SeriesQuery.Key.SeriesAll));
		OtrSeries ejb = ufb.find(OtrSeries.class, seriesId);
		ejb = osfb.load(OtrSeries.class, ejb, false);
		return f.build(ejb);
	}

	@Override
	public Series seriesWithSeason(@PathParam("id") long seriesId) throws UtilsNotFoundException
	{
		init();
		XmlSeriesFactory<OtrSeries,OtrSeason,OtrEpisode,OtrImage,OtrStorage> f = new XmlSeriesFactory<OtrSeries,OtrSeason,OtrEpisode,OtrImage,OtrStorage>(SeriesQuery.get(SeriesQuery.Key.SeriesWithSeason));
		OtrSeries ejb = ufb.find(OtrSeries.class, seriesId);
		ejb = osfb.load(OtrSeries.class, ejb, false);
		return f.build(ejb);
	}
	
	@Override
	public Season seasonWithEpisode(@PathParam("id") long seasonId) throws UtilsNotFoundException
	{
		init();
		XmlSeasonFactory<OtrSeries,OtrSeason,OtrEpisode,OtrImage,OtrStorage> f = new XmlSeasonFactory<OtrSeries,OtrSeason,OtrEpisode,OtrImage,OtrStorage>(SeriesQuery.get(SeriesQuery.Key.SeasonWithEpisodes));
		OtrSeason ejb = ufb.find(OtrSeason.class, seasonId);
		ejb = osfb.load(OtrSeason.class, ejb);
		Season season = f.build(ejb);
		return season;
	}

}