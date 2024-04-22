package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.services.ProjectListQuery;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Component(service = Servlet.class)
@SlingServletPaths(value = { "/bin/dropdownlist"})
public class DropDownList extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DropDownList.class);

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Reference
    ProjectListQuery projectListQuery;

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
            throws ServletException, IOException {

        final ResourceResolver resourceResolver = req.getResourceResolver();
        JsonObject outputJson = new JsonObject();
        String projectName = null;
        
        QueryBuilder builder = resourceResolver.adaptTo(QueryBuilder.class);
        Session session = resourceResolver.adaptTo(Session.class);
        Query query = builder.createQuery(PredicateGroup.create(projectListQuery.searchQuery()), session);
        SearchResult result = query.getResult();
        try {
            JsonObject projectlist = new JsonObject();
            int index = 1;
            for (Hit hit : result.getHits()) {
                try {
                    projectName = hit.getResource().getName();
                    LOG.error("Page path: " + projectName);
                    projectlist.add("projectname-" + index, new JsonPrimitive(projectName));
                    index++;
                } catch (Exception e) {
                    LOG.error("Exception: " + e.getMessage());
                }
            }
            outputJson.add("projectlist", projectlist); 
        
        } catch (Exception e) {
            LOG.error("\n ----ERROR -----{} ", e.getMessage());
        }

        try {
            for (Hit hit : result.getHits()) {
                try {
                    projectName = hit.getResource().getName();
                    LOG.error("Page path: " + projectName); 
                    JsonObject pageObject = new JsonObject();
                    Page page = resourceResolver.adaptTo(PageManager.class).getPage("/content/"+projectName);
                    int siteCounter = 1;
                    Iterator<Page> childPages = page.listChildren();
                    while (childPages.hasNext()) {
                        Page childPage = childPages.next();
                        String pageName = childPage.getName();
                        if (pageName != null) {
                            String siteKey = "site-" + siteCounter++;
                            pageObject.add(siteKey, new JsonPrimitive(pageName));
                        } else {
                            LOG.error("Page Title is null for Page Path : " + pageName);
                        }
                    }
                    outputJson.add(projectName, pageObject);
                } catch (Exception e) {
                    LOG.error("Exception: " + e.getMessage());
                }
            }
        
        } catch (Exception e) {
            LOG.error("\n ----ERROR -----{} ", e.getMessage());
        }

        resp.setContentType("application/json");
        resp.getWriter().write(outputJson.toString());
    }

}

