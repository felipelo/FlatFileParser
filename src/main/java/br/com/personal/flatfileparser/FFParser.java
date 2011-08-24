package br.com.personal.flatfileparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Felipe
 */
public abstract class FFParser {

    protected FileInputStream fis;
    protected BufferedReader bis;
    protected boolean finished;
    protected int newLine;
    protected int columnCount;
    protected String value[];

    public FFParser( File file, int newLine, int columnCount, int header ) throws IOException {
        fis = new FileInputStream( file );
        this.newLine = newLine;
        this.columnCount = columnCount;
        this.value = new String[columnCount];

        InputStreamReader in = new InputStreamReader(fis, "UTF-8");
        bis = new BufferedReader(in);

        skipHeader( header );
    }

    public abstract boolean next();

    public abstract String getString( int columnPos );

    protected final void skipHeader( int header ) throws IOException {
        for( int _x = 0; _x < header; _x++ )
            bis.readLine();
    }

    protected final int skipLineBreak() throws IOException {
        int eof = 0;
        for( int _x = 0; _x < newLine; _x++ )
            eof = bis.read();
        return eof;
    }

    public void close() {
        try {
            bis.close();
        } catch (IOException ex) {}
    }

}
