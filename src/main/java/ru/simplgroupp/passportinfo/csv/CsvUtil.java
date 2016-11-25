/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.csv;

import java.io.*;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.passportinfo.PropsHolder;
import ru.simplgroupp.passportinfo.db.facade.CronJobLogFacade;
import ru.simplgroupp.passportinfo.db.facade.PassportFacade;
import ru.simplgroupp.passportinfo.db.model.CronJobLog;
import ru.simplgroupp.passportinfo.db.model.CronJobLogType;
import ru.simplgroupp.passportinfo.exception.PassportInfoException;

/**
 *
 * @author stechiev
 */
@Singleton
@Startup
public class CsvUtil {
    public static final String TMP_FILE_PREFIX = "passinfo";

    private Logger log = LoggerFactory.getLogger(CsvUtil.class);

    @EJB
    private CsvToDBWriter dBWriter;

    @EJB
    private PassportFacade passportFacade;

    @EJB
    private CsvCopyUtil csvCopyUtil;

    @EJB
    private PropsHolder propsHolder;

    @EJB
    private CronJobLogFacade cronJobLogFacade;

    @Resource
    private TimerService timerService;

    private Timer timer;

    @PostConstruct
    public void initialize() {
        ScheduleExpression expression = new ScheduleExpression();
        //expression.second("0").minute("16").hour("*");

        expression.dayOfWeek(propsHolder.getDayOfWeek())
                .hour(propsHolder.getHour())
                .minute(propsHolder.getMinute())
                .second(0);
        timer = timerService.createCalendarTimer(expression);
        log.info("Initialized");
    }

    @PreDestroy
    private void destroy() {
        Collection timersList = timerService.getTimers();
        for (Object timer : timersList) {
            if (timer instanceof Timer) {
                log.debug("Timer still running, kill it : " + timer);
                ((Timer) timer).cancel();
            }
        }
    }

    @Timeout
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)    
    public void execute() {
        log.info("----Invoked: " + System.currentTimeMillis());
        processURL(propsHolder.getCsvURL());
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void processURL(String url) {
        try {
            log.info("URL to download archive is : {}", url);

            File bz2File = null;
            FileInputStream fis = null;
            try {

                /*CronJobLog jobLog = new CronJobLog();
                jobLog.setStartDate(new Date());
                jobLog.setUploadType(CronJobLog.TYPE_URL);*/
                bz2File = File.createTempFile(TMP_FILE_PREFIX, ".bz2");
                FileUtils.copyURLToFile(new URL(url), bz2File);
                log.info("Downloading complete; archive file for reading - {}", bz2File.getAbsolutePath());
                fis = new FileInputStream(bz2File);
                BufferedInputStream bis = new BufferedInputStream(fis);
                BZip2CompressorInputStream bz2In = new BZip2CompressorInputStream(bis);
                copyInDb(bz2In, CronJobLogType.URL);
                /*try {
                    csvCopyUtil.copyInDb(bzIn);
                    jobLog.setStatus(CronJobLog.STATUS_SUCCESS);

                } catch (Exception e) {
                    jobLog.setStatus(CronJobLog.STATUS_ERROR);
                    jobLog.setErrorDesc(e.getMessage());
                } finally {
                    jobLog.setEndDate(new Date());
                    cronJobLogFacade.create(jobLog);
                }*/

            } catch (Exception e) {
                throw new PassportInfoException("Exception during csv file extraction by URL" + url, e);
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (IOException ex) {
                    throw ex;
                }

                if (bz2File.delete()) {
                    log.info("File \"{}\" deleted", bz2File.getAbsolutePath());
                }
            }

        } catch (Exception e) {
            log.error("Error just happened", e);
        }
    }

    public File normalizeInputStreamFile(InputStream in) throws IOException {
        log.info("Input stream normalization started");
        long startTime = System.nanoTime();

        boolean headersIncluded = true;
        Pattern csvRowPattern = Pattern.compile(this.propsHolder.getCsvRowRegex());

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        File normFile = File.createTempFile(TMP_FILE_PREFIX, ".csv");
        log.info("File for normalized data: \"{}\"", normFile.getAbsolutePath());

        PrintWriter writer = new PrintWriter(normFile);
        try {
            String line;
            long cnt = 0;
            while((line = reader.readLine()) != null) {
                if(csvRowPattern.matcher(line).matches()) {
                    writer.println(line);
                } else if (headersIncluded && cnt == 0) {
                    writer.println(line);
                } else {
                    log.warn("Skip line #{} during csv import. Illegal line format: \"{}\"", cnt, line);
                }
                cnt++;
            }
            log.info("Input stream normalized at {} seconds", 1.0*(System.nanoTime()-startTime)/1000000000);
        } finally {
            writer.close();
        }

        return normFile;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void copyInDb(InputStream is, CronJobLogType type) {
        try {
            File normFile = normalizeInputStreamFile(is);
            FileInputStream fin = new FileInputStream(normFile);

            CronJobLog jobLog = new CronJobLog();
            jobLog.setStartDate(new Date());
            jobLog.setUploadType(type.toString());
            try {
                csvCopyUtil.copyInDb(fin);
                jobLog.setStatus(CronJobLog.STATUS_SUCCESS);

            } catch (Exception e) {
                jobLog.setStatus(CronJobLog.STATUS_ERROR);
                jobLog.setErrorDesc(e.getMessage());
                log.error("Error cpoying passports into db...", e);
            } finally {
                fin.close();
                if (normFile.delete()) {
                    log.info("File \"{}\" deleted", normFile.getAbsolutePath());
                }

                jobLog.setEndDate(new Date());
                cronJobLogFacade.create(jobLog);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//    public void logPersist(CronJobLog jobLog) {
//        cronJobLogFacade.create(jobLog);
//    }
}
