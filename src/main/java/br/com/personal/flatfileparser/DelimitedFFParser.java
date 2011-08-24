package br.com.personal.flatfileparser;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Felipe
 */
public class DelimitedFFParser extends FFParser {
    
    private String qualifier;
    private Pattern csvRgx;
    private Pattern doubleQualifier;

    public DelimitedFFParser( File file, int newLine, String qualifier, int columnCount, String separator ) throws IOException {
        super( file, newLine, columnCount, newLine );

        this.qualifier = qualifier;//("(?:[^"]|"")*"|[^,]*)

        String _regex = "^";
        String _group = "("
                .concat( qualifier )
                .concat( "(?:[^")
                .concat( qualifier )
                .concat( "]|")
                .concat( qualifier )
                .concat( qualifier )
                .concat( ")*")
                .concat( qualifier )
                .concat( "|[^" )
                .concat( separator )
                .concat( "]*)" );
//        System.out.println(_group);
        for( int _x = 0; _x < columnCount-1; _x++)
            _regex = _regex.concat( _group.concat(",") );
        _regex = _regex.concat( _group );
        _regex = _regex.concat( "$" );
//        System.out.println(_regex);

        this.csvRgx = Pattern.compile( _regex );
        this.doubleQualifier = Pattern.compile( qualifier.concat(qualifier) );
    }

    @Override
    public boolean next() {
        if( finished ) return false;
        try {
            String _line = "";
            Matcher _matcher = null;
            boolean _m = false;
            while( !_m ) {
                String _singleLine = bis.readLine();
                if( _singleLine == null ) {
                    finished = true;
                    return false;
                }

                _line = _line.concat( _singleLine ).concat( System.getProperty("line.separator") );

                _matcher = csvRgx.matcher( _line );
                _m = _matcher.find();
            }

//            if( _matcher.find() ) {
                for( int _x = 1; _x <= _matcher.groupCount(); _x++ ) {
                    String _match = _matcher.group(_x);
                    if( _match.contains(qualifier) ) {
                        _match = ( _match.startsWith(qualifier) && _match.endsWith(qualifier) ) ? _match.substring( 1, _match.length()-1 ) : _match;
                        _match = doubleQualifier.matcher(_match).replaceAll( qualifier );
                    }
                    value[_x-1] = _match;
                }
//            } else {
//
//            }
        } catch (IOException ex) {
            ex.printStackTrace();
            finished = true;
            return false;
        }

        return true;
    }

    @Override
    public String getString(int columnPos) {
        return value[columnPos];
    }

    public static void main(String[] args) throws IOException {
        DelimitedFFParser _d = new DelimitedFFParser( new File("delimitedFile.csv"), 1, "\"", 5, ",");

        long t = System.currentTimeMillis();
        while(_d.next()){
            System.out.println(_d.getString(2));
        }
         System.out.println("--"+(System.currentTimeMillis()-t));
    }

}
