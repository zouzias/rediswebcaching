/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ibm.zurich.rediscacherexperiment.connector;

import com.ibm.zurich.rediscacherexperiment.config.Configuration;
import org.apache.solr.client.solrj.impl.HttpSolrServer;


/**
 * Start a new Solr connection to a specific core/collection name
 *
 * @author azo
 */
public class SolrConnector {
    
    private final HttpSolrServer server;
    private final String         solrURL;
    
    public SolrConnector(String coreName){
        
        solrURL = Configuration.SOLRURL + coreName;
         
        server = new HttpSolrServer(solrURL);
        
        server.setMaxRetries(1); // defaults to 0.  > 1 not recommended.
        server.setConnectionTimeout(5000); // 5 seconds to establish TCP
        // Setting the XML response parser is only required for cross
        // version compatibility and only when one side is 1.4.1 or
        // earlier and the other side is 3.1 or later.
        //server.setParser(new XMLResponseParser()); // binary parser is used by default
        // The following settings are provided here for completeness.
        // They will not normally be required, and should only be used 
        // after consulting javadocs to know whether they are truly required.
        server.setSoTimeout(5000);  // socket read timeout
        server.setDefaultMaxConnectionsPerHost(100);
        server.setMaxTotalConnections(100);
        server.setFollowRedirects(false);  // defaults to false
        // allowCompression defaults to false.
        // Server side must support gzip or deflate for this to have any effect.
        server.setAllowCompression(true);
        
    }
    
    public String getSolrURL(){
        return solrURL;
    }
    
    public HttpSolrServer getServer(){
        return server;
    }
    
}
