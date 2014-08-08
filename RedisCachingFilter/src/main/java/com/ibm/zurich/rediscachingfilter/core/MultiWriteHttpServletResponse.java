/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibm.zurich.rediscachingfilter.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 *
 * @author azo
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
