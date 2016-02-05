/*

 */
package com.infinity.service;

import com.infinity.dto.Candidat;
import com.infinity.dto.ClientOffers;
import com.infinity.dto.TechnoCriteria;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author t311372
 */
@Service
public class PowerSearchEngine {

    private static final Logger LOG = LoggerFactory.getLogger(PowerSearchEngine.class);

    @Autowired
    private ClientsJobsService jobsService;

    @Autowired
    private CandidatService candidatService;

    private ClientOffers criteriaFromDb;

   
    public HashMap<ClientOffers, ArrayList<Candidat>> matchCandidat(){


        HashMap<ClientOffers, ArrayList<Candidat>> res = new HashMap<>();
        try {

            ArrayList<ClientOffers> allJobs = jobsService.getAll();
            for (ClientOffers jobs : allJobs) {

                ArrayList<TechnoCriteria> technoCriterias = jobs.getTechnoCriterias();
                if(technoCriterias != null && !technoCriterias.isEmpty()){
                    
                
                StringBuilder stringBuilder = new StringBuilder();
                for (TechnoCriteria technoCriteria : technoCriterias) {

                    String technoName = technoCriteria.getTechnoName();
                    stringBuilder.append(" ")
                            .append(technoName);

                }
                ArrayList<Candidat> powerSearch = candidatService.powerSearch(jobs.getDep(), stringBuilder.toString());
                res.put(jobs, powerSearch);
                } 
            }

        } catch (IOException ex) {

            LOG.error(ex.getMessage(),ex);
        }
        return res;
    }

}
