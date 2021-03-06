package org.zouzias.rediscachingfilter.aux;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *  A wrapper over HttpServletResponse so that respose.getWriter() can be called twice (used in Filters)
 * 
 * @author zouzias
 */
public class MultiWriteHttpServletResponse extends HttpServletResponseWrapper {

    private final StringWriter buffer; // This can be used as an Writer  

    public MultiWriteHttpServletResponse(HttpServletResponse httpServletResponse) throws IOException {
        super(httpServletResponse);
        buffer = new StringWriter();
    }
    
    @Override
    public String toString() {
        return buffer.toString();
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(buffer);  
    }
}
