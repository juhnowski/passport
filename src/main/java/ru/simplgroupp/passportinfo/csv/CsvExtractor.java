/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.FileUtils;
import ru.simplgroupp.passportinfo.exception.PassportInfoException;

/**
 *
 * @author stechiev
 */
public class CsvExtractor {
    
    

    public static void extractToFile(String url, String filename) throws IOException, PassportInfoException {

        FileInputStream fis = null;
        try {
            File bz2File = new File(filename + ".bz2");
            FileUtils.copyURLToFile(new URL(url), bz2File);
            fis = new FileInputStream(bz2File);
            BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(fis);
            final byte[] buffer = new byte[4096];
            int n = 0;
            
            BufferedReader br = new BufferedReader(new InputStreamReader(bzIn));
            String line = null;
            while((line=br.readLine())!=null){
                
            }        
            br.close();
        } catch (Exception e) {
            throw new PassportInfoException("Exception during csv file extraction by URL" + url, e);
        } finally {
            try {
                /*if (fout != null) {
                    fout.close();
                }*/
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                throw ex;
            }
        }
    }

}
