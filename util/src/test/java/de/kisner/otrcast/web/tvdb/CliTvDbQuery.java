package de.kisner.otrcast.web.tvdb;

import java.util.Date;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kisner.otrcast.AbstractOtrcastTest;
import de.kisner.otrcast.OtrUtilTestBootstrap;
import de.kisner.otrcast.web.tvdb.TvDbQuery;

public class CliTvDbQuery extends AbstractOtrcastTest
{
    final static Logger logger = LoggerFactory.getLogger(CliTvDbQuery.class);

    public static void main(String args[]) throws Exception
    {
        Configuration config = OtrUtilTestBootstrap.init();

        TvDbQuery dbQuery = new TvDbQuery(config.getString("tvDbApiKey"));

        Date last = dbQuery.getLastModificationTime();
        logger.info(last.toString());
    }
 }