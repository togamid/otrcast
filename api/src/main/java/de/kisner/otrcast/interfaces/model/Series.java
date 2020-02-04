package de.kisner.otrcast.interfaces.model;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.with.code.EjbWithCode;

import de.kisner.otrcast.interfaces.model.with.EjbWithImage;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;
import net.sf.ahtutils.model.interfaces.with.EjbWithName;

public interface Series<SERIES extends Series<SERIES,SEASON,EPISODE,IMAGE>,
						SEASON extends Season<SERIES,SEASON,EPISODE,IMAGE,?>,
						EPISODE extends Episode<SEASON>,
						IMAGE extends Image>
		extends Serializable,EjbWithId,
					EjbWithImage<IMAGE>,
					EjbPersistable,
					EjbWithCode,EjbWithName
{
	List<SEASON> getSeasons();
	void setSeasons(List<SEASON> seasons);
}