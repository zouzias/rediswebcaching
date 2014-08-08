/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ibm.zurich.solrcacheserver.servlets.models;

import java.util.Map;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author azo
 */
public class SolrCoreResponse {
    private final long numFound;
    private final JSONArray resultSet;
    private final double maxScore;
    
    public SolrCoreResponse(){
        resultSet = new JSONArray();
        numFound  = 0;
        maxScore  = 0.0;
    }
    
    public SolrCoreResponse(SolrDocumentList results){
        resultSet = new JSONArray();
        numFound  = results.getNumFound();
        for ( SolrDocument doc : results){
                JSONObject o = new JSONObject();
                Map<String, Object> map = doc.getFieldValueMap();
                for ( String field : map.keySet())
                    o.put(field, map.get(field).toString());
                
                resultSet.add(o);
            }
        
        // Keep the maximum score
        if ( !resultSet.isEmpty() && ((JSONObject)resultSet.get(0)).containsKey("score")){
            maxScore = Double.valueOf( (String)((JSONObject)resultSet.get(0)).get("score") );
        }
        else
            maxScore = 0.0;
    }

    public long getNumFound() {
        return numFound;
    }   

    public JSONArray getResultSet() {
        return resultSet;
    }
    
    public double getMaxScore(){
        return maxScore;
    }
    
}
