/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ibm.zurich.rediscacherexperiment.models;

import java.util.LinkedList;
import java.util.List;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author azo
 */
public class SimpleFacetResponse {
    
    private final List<Count> facetResults = new LinkedList<Count>();
    private final JSONArray resultSet = new JSONArray();
    private final String facetField;
    
    private static final int MAXFACETNUM = 20;
    
    public SimpleFacetResponse(){
        facetField = "N/A";
    }
    
    public SimpleFacetResponse(FacetField facetInfo){
        facetField = facetInfo.getName();
        facetResults.addAll(facetInfo.getValues().subList(0, Math.min(MAXFACETNUM, facetInfo.getValues().size())));
        
        for ( Count count : facetResults){
                JSONObject o = new JSONObject();
                o.put("name", count.getName());
                o.put("count", count.getCount());
                resultSet.add(o);   
            }
    }
    
    public JSONArray getResultSet(){
        return resultSet;
    }
    public String getFacetField(){
        return facetField;
    }
    
}
