package de.kisner.otrcast.controller.tag.reader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.MetaBox;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.UserDataBox;
import com.coremedia.iso.boxes.apple.AppleItemListBox;
import com.coremedia.iso.boxes.apple.AppleMediaTypeBox;
import com.coremedia.iso.boxes.apple.AppleShowBox;

import de.kisner.otrcast.controller.tag.util.Mp4BoxManager;
import de.kisner.otrcast.model.xml.series.Video;
import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;

public class Mp4TagReader
{
	final static Logger logger = LoggerFactory.getLogger(Mp4TagReader.class);

	private RandomAccessFile raf;
	private FileChannel fc;
	private IsoFile isoFile;
	
	private AppleItemListBox apple;

	
	private SeriesTagReader trSeries;
	private MovieTagReader trMovie;
	
	public Mp4TagReader(boolean withCover)
	{
		trSeries = new SeriesTagReader(withCover);
		trMovie = new MovieTagReader(withCover);
	}
	
	private void readAppleBox(File fSource) throws IOException
	{
		raf = new RandomAccessFile(fSource, "r");		
		fc = raf.getChannel();
		isoFile = new IsoFile(fc);
		
		MovieBox moov = Mp4BoxManager.movieBox(isoFile);		
		UserDataBox udta = Mp4BoxManager.userDataBox(moov);
		MetaBox meta = Mp4BoxManager.metaBox(udta);
		apple = Mp4BoxManager.appleItemListBox(meta);
	}
	
	private void closeFile() throws IOException
	{
		isoFile.close();
		fc.close();
		raf.close();
	}
	
	public Video read(File fSource) throws IOException
	{
		readAppleBox(fSource);

        Mp4BoxManager.Type type;
        try{type = getMediaType(apple);}
        catch (UtilsNotFoundException e) {type=guessType(apple);}

		Video video = new Video();
		switch(type)
		{
			case SERIES:	video.setEpisode(trSeries.readEpisode(apple));break;
			case MOVIE:		video.setMovie(trMovie.readMovie(apple));break;
			default: logger.warn("UNKNOWN handling for "+fSource);
		}
		closeFile();

		return video;
	}

    public Mp4BoxManager.Type readMediaType(File fSource) throws IOException,UtilsNotFoundException
    {
    	readAppleBox(fSource);

        Mp4BoxManager.Type type;
        try{type = getMediaType(apple);}
        catch (UtilsNotFoundException e) {throw e;}
        finally
        {
        	closeFile();
        }

        return type;
    }
    
	public Mp4BoxManager.Type getMediaType(AppleItemListBox apple) throws UtilsNotFoundException
    {
		logger.info("Getting "+AppleMediaTypeBox.class.getSimpleName());
		if (apple.getBoxes(AppleMediaTypeBox.class).isEmpty())
		{
            logger.info(AppleMediaTypeBox.class.getSimpleName()+" not set");
            throw new UtilsNotFoundException(AppleMediaTypeBox.class.getSimpleName()+" not set");
		}
		else
		{
			AppleMediaTypeBox box = apple.getBoxes(AppleMediaTypeBox.class).get(0);
			logger.info("Value "+box.getValue());
			if(box.getValue().equals(Mp4BoxManager.typeSeries)){return Mp4BoxManager.Type.SERIES;}
			else
			{
				logger.warn("Unknown Mediatype "+box.getValue());
				logger.debug("\t"+box.getClass().getSimpleName()+" "+box.getType()+" "+box.getValue());
				
				return Mp4BoxManager.Type.UNKNOWN;
			}		
		}
	}

    public Mp4BoxManager.Type guessType(File file) throws IOException
    {
        RandomAccessFile rafR = new RandomAccessFile(file, "r");
        FileChannel fcr = rafR.getChannel();
        IsoFile isoFile = new IsoFile(fcr);

        MovieBox moov = Mp4BoxManager.movieBox(isoFile);
        UserDataBox udta = Mp4BoxManager.userDataBox(moov);
        MetaBox meta = Mp4BoxManager.metaBox(udta);
        AppleItemListBox apple = Mp4BoxManager.appleItemListBox(meta);

        Mp4BoxManager.Type type;
        type = guessType(apple);

        isoFile.close();
        fcr.close();
        rafR.close();

        return type;
    }
	public Mp4BoxManager.Type guessType(AppleItemListBox apple)
	{
		if (!apple.getBoxes(AppleShowBox.class).isEmpty())
		{
			AppleShowBox box = apple.getBoxes(AppleShowBox.class).get(0);
			if(box.getValue().length()>0){return Mp4BoxManager.Type.SERIES;}
		}
		return Mp4BoxManager.Type.MOVIE;
	}
}