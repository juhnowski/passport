/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.csv;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.postgresql.core.v3.CopyInImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.passportinfo.PropsHolder;
import ru.simplgroupp.passportinfo.exception.PassportInfoException;

/**
 *
 * @author stechiev
 */
@Stateless
public class CsvCopyUtil {

    @EJB
    private PropsHolder propsHolder;

    private Logger log = LoggerFactory.getLogger(CsvCopyUtil.class);

    //@Asynchronous
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void copyInDb(InputStream is) throws Exception {

        try {
            Class.forName("org.postgresql.Driver");

            Connection con = DriverManager.getConnection(propsHolder.getJdbcDbUrl(),
                    propsHolder.getJdbcUser(),
                    propsHolder.getJdbcPass());

            CopyManager copyManager = new CopyManager((BaseConnection) con);

            log.info("truncating table PASSPORT...");
            Statement stmt = null;
            /*String query = "INSERT INTO passport(passp_series,passp_number) \n"
             + "    SELECT pt.passp_series, pt.passp_number FROM passport_temp pt \n"
             + "    WHERE NOT EXISTS (SELECT 1 FROM passport WHERE passport.passp_series = pt.passp_series AND passport.passp_number = pt.passp_number);";*/
            String truncateSQL = "TRUNCATE TABLE passport";
            String dropSQL = "DROP TABLE IF EXISTS passport";

            String createSQL = "CREATE TABLE IF NOT EXISTS passport "
                    + "(passp_series character varying(4) NOT NULL,"
                    + " passp_number character varying(6) NOT NULL)"
                    + "WITH (OIDS=FALSE)";     
            String pSeriesIndSQL = "CREATE INDEX pseries_ind ON passport (passp_series);";
            String pNumberIndSQL = "CREATE INDEX pNumber_ind ON passport (passp_number);";
            String passpIndSQL ="CREATE INDEX passp_ind ON passport (passp_series ASC NULLS LAST, passp_number ASC NULLS LAST)";
            try {
                stmt = con.createStatement();
                int res = stmt.executeUpdate(truncateSQL);
                log.info("TRUNCATE result: " + res);

                boolean r = stmt.execute(dropSQL);
                log.info("SQL: {} result: {}", dropSQL, r);
                
                r = stmt.execute(createSQL);
                log.info("SQL: {} result: {}", createSQL, r);

            } catch (SQLException e) {
                throw e;
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
            log.info("table PASSPORT recreated");

            log.info("Copying text data rows from stdin");
            //FileReader fileReader = new FileReader(args[3]);
            copyManager.copyIn("COPY passport FROM STDIN WITH(FORMAT CSV, DELIMITER ',', HEADER TRUE)", is);
            
            log.info("Done.");
            
            
            
            try {
                log.info("Creating index for passport...");
                stmt = con.createStatement();
                boolean r = stmt.execute(passpIndSQL);
                log.info("SQL: {} result: {}", passpIndSQL, r);
                
                /*log.info("Creating index for passp_series...");
                stmt = con.createStatement();
                boolean r = stmt.execute(pSeriesIndSQL);
                log.info("SQL: {} result: {}", pSeriesIndSQL, r);
                
                log.info("Creating index for passp_number...");
                stmt = con.createStatement();
                r = stmt.execute(pNumberIndSQL);
                log.info("SQL: {} result: {}", pNumberIndSQL, r);*/

            } catch (SQLException e) {
                throw e;
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
}
