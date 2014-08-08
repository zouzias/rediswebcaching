/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ibm.zurich.rediscacherexperiment.searchers;

import com.ibm.zurich.rediscacherexperiment.connector.SolrConnector;
import com.ibm.zurich.solrcacheserver.servlets.models.SolrCoreResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;

/**
 *
 * @author azo
 */
public class SimpleSearcher {
    
    private static final int MAXRESULTS = 5;
    
    public static SolrCoreResponse querySolrCore(String coreName, String prefix){

        SolrConnector conn = new SolrConnector(coreName);
        HttpSolrServer server = conn.getServer();
        SolrQuery sQuery     = new SolrQuery();

        SolrCoreResponse solrResponse = new SolrCoreResponse();        
        try {   
            // Set up the parameters for the TermsComponent
            sQuery.setParam(CommonParams.ROWS, String.valueOf(MAXRESULTS));
            sQuery.setParam(CommonParams.QT, "/crystalPlus");
            sQuery.setParam(CommonParams.Q, prefix );
            sQuery.setParam(CommonParams.CACHE, "true");
            
            // Query the Solr server
            QueryResponse response = server.query(sQuery);
            
            solrResponse = new SolrCoreResponse(response.getResults());
     
        } catch (SolrServerException e) {
            System.out.println("Server connection problem...");
            e.printStackTrace(System.out);
        } catch (SolrException e) {
            System.out.println("Problem with query in SIMPLESearcher");
            System.err.println("The URL is " + conn.getSolrURL() + sQuery.toString());
            e.printStackTrace(System.out);
        }
        return solrResponse;
    }

    
}
