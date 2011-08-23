package br.com.idealogic.flatfileparser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author felipe
 */
public class FixedWidthFFParser extends FFParser {

    private int[] widthsColumns;
    private int lineLenght;
    private char[] value;

    public FixedWidthFFParser( File file, int[] widthsColumns, int header, int newLine ) throws IOException {
        super( file, newLine, widthsColumns.length, header );

        this.widthsColumns = widthsColumns;
        
        for( int lenght : widthsColumns ) {
            lineLenght += lenght;
        }
        value = new char[lineLenght];

        for( int _x = 0; _x < header; _x++ )
            bis.read(value);
    }

    @Override
    public String getString( int columnPos ) {
        int widthColumn = widthsColumns[--columnPos];

        int off = 0;
        for( int _x = 0; _x < columnPos; _x++ ) {
            off += widthsColumns[_x];
        }

        return String.valueOf(Arrays.copyOfRange(value, off, off+widthColumn));
    }

    @Override
    public boolean next() {
        int b = -1;
        if( finished )
            return false;
        try {
            b = bis.read(value);
            
            if( b == -1 || skipLineBreak() == -1 ) {
                finished = true;
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            finished = true;
            return false;
        }
        finished = false;
        return true;
    }

    public static void main(String[] args) throws Exception {
//        FixedWidthFFParser f = new FixedWidthFFParser(
//                new File("Produtos_materiais20100830142812.txt"),
//                new int[]{15,50,90,2}, //columns
//                1, //header lines
//                "\r\n".length());
//        long time = System.currentTimeMillis();
//       while(f.next()){
////           System.out.println(f.getString(1)+"!");
//       }
//        System.out.println("Time: " + (System.currentTimeMillis()-time));

//        System.out.println("e .6  .".length());
        Pattern p = Pattern.compile("^(.{3})", Pattern.DOTALL);
        Matcher matcher = p.matcher("e\n.");
        matcher.find();
        System.out.println(matcher.group()+")");
    }

}
