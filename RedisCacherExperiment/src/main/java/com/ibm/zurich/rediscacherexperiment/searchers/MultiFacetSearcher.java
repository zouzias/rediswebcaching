/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ibm.zurich.rediscacherexperiment.searchers;

import com.ibm.zurich.rediscacherexperiment.connector.SolrConnector;
import com.ibm.zurich.solrcacheserver.servlets.models.MultiFacetResponse;
import java.util.List;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.FacetParams;
import org.apache.solr.common.params.ModifiableSolrParams;

/**
 *
 * @author azo
 */
public class MultiFacetSearcher {
     private static final int MAXRESULTS = 5;
    
    public static MultiFacetResponse querySolrCore(String coreName, String query, List<String> facetFields){

        SolrConnector conn = new SolrConnector(coreName);
        HttpSolrServer server = conn.getServer();
        ModifiableSolrParams params = new ModifiableSolrParams();

        MultiFacetResponse multiFacetResponse = new MultiFacetResponse();        
        try {   

            // Set up the parameters for the TermsComponent
            params.add(CommonParams.ROWS, String.valueOf(MAXRESULTS));
            params.add(CommonParams.QT, "/crystalplus");
            params.add(CommonParams.Q, query );
            params.add(FacetParams.FACET, "on");
            for (String facetField : facetFields)
                params.add(FacetParams.FACET_FIELD, facetField);
            
            params.add(FacetParams.FACET_SORT, FacetParams.FACET_SORT_COUNT);
            params.add(FacetParams.FACET_MINCOUNT, "1");            
            params.add(CommonParams.CACHE, "true");
                        
            System.out.println("The URL is " + conn.getSolrURL() + params.toString());
            
            // Query the Solr server
            QueryResponse response = server.query(params);
            
            multiFacetResponse = new MultiFacetResponse(response.getFacetFields());
     
        } catch (SolrServerException e) {
            System.out.println("Server connection problem...");
            e.printStackTrace(System.out);
        } catch (SolrException e) {
            System.out.println("Problem with query in MULTIFACETSEARCHER");
            System.err.println("The URL is " + conn.getSolrURL() + params.toString());
            e.printStackTrace(System.out);
        }
        return multiFacetResponse;
    }
    
}
