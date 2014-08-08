/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ibm.zurich.rediscacherexperiment.searchers;

import com.ibm.zurich.rediscacherexperiment.connector.SolrConnector;
import com.ibm.zurich.rediscacherexperiment.models.SimpleFacetResponse;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.FacetParams;

/**
 *
 * @author azo
 */
public class SimpleFacetSearcher {
     private static final int MAXRESULTS = 5;
    
    public static SimpleFacetResponse querySolrCore(String coreName, String query, String facetField){

        SolrConnector conn = new SolrConnector(coreName);
        HttpSolrServer server = conn.getServer();
        SolrQuery sQuery     = new SolrQuery();

        SimpleFacetResponse simpleFacetResponse = new SimpleFacetResponse();        
        try {   
            // Set up the parameters for the TermsComponent
            sQuery.setParam(CommonParams.ROWS, String.valueOf(MAXRESULTS));
            sQuery.setParam(CommonParams.QT, "/crystalPlus");
            sQuery.setParam(CommonParams.Q, query );
            sQuery.setParam(FacetParams.FACET, "on");
            sQuery.setParam(FacetParams.FACET_FIELD, facetField);
            sQuery.setParam(FacetParams.FACET_SORT, FacetParams.FACET_SORT_COUNT);
            sQuery.setParam(FacetParams.FACET_MINCOUNT, "1");            
            sQuery.setParam(CommonParams.CACHE, "true");
            
            System.out.println("The URL is " + conn.getSolrURL() + sQuery.toString());
            
            // Query the Solr server
            QueryResponse response = server.query(sQuery);
            
            simpleFacetResponse = new SimpleFacetResponse(response.getFacetField(facetField));
     
        } catch (SolrServerException e) {
            System.out.println("Server connection problem...");
            e.printStackTrace(System.out);
        } catch (SolrException e) {
            System.out.println("Problem with query in SIMPLEFACETSERVER");
            System.err.println("The URL is " + conn.getSolrURL() + sQuery.toString());
            e.printStackTrace(System.out);
        }
        return simpleFacetResponse;
    }
    
}

