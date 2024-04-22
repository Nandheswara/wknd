package com.adobe.aem.guides.wknd.core.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ProjectListQuery.class, immediate = true)
public class ProjectListQuery {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectListQuery.class);

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    public Map<String, String> searchQuery() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("path", "/content");
        queryMap.put("type", "cq:Page");
        queryMap.put("property", "jcr:content/sling:configRef");
        queryMap.put("property.operation", "exists");
        return queryMap;
    }

}