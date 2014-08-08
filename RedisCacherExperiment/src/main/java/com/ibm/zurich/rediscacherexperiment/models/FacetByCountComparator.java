/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zurich.ibm.crystalsearcher.models;

import java.util.Comparator;
import org.apache.solr.client.solrj.response.FacetField.Count;

/**
 * Bigger count is first
 *
 * @author azo
 */
public class FacetByCountComparator implements Comparator<Count> {
    
    @Override
    public int compare(Count s1, Count s2) {
                
        return (int)s2.getCount() - (int)s1.getCount();        
    }
}