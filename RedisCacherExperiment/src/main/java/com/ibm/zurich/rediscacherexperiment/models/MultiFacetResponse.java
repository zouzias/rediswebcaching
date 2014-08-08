/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibm.zurich.solrcacheserver.servlets.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author azo
 */
public class MultiFacetResponse {

    private final Map<String, List<Count>> facetResults = new HashMap<String, List<Count>>();
    private final Map<String, JSONArray> facetJSONResults = new HashMap<String, JSONArray>();

    private static final int MAXFACETNUM = 20;

    public MultiFacetResponse() {
    }

    public MultiFacetResponse(List<FacetField> facetInfo) {

        for (FacetField facetField : facetInfo) {
            List<Count> results = facetField.getValues().subList(0, Math.min(MAXFACETNUM, facetField.getValues().size()));

            JSONArray array = new JSONArray();
            for (Count count : results) {
                JSONObject o = new JSONObject();
                o.put("name", count.getName());
                o.put("count", count.getCount());
                array.add(o);
            }

            facetResults.put(facetField.getName(), results);
            facetJSONResults.put(facetField.getName(), array);

        }
    }

    public Map<String, JSONArray> getFacetJSONResults(){
        return facetJSONResults;
    }

}
