package com.adobe.aem.guides.wknd.core.servlets;

import java.io.IOException;
import java.util.Iterator;
import javax.json.JsonException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

@Component(service = Servlet.class)
@SlingServletPaths(value = { "/bin/sitelist", "/bin/sitename" })
public class PageList extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(PageList.class);

    @Override
    protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
            throws ServletException, IOException {
        final ResourceResolver resourceResolver = req.getResourceResolver();
        Page page = resourceResolver.adaptTo(PageManager.class).getPage("/content/wknd");
        JsonObject pageObject = new JsonObject();
        int siteCounter = 1;
        try {
            Iterator<Page> childPages = page.listChildren();
            while (childPages.hasNext()) {
                Page childPage = childPages.next();
                String pageName = childPage.getName();
                if (pageName != null) {
                    String siteKey = "site-" + siteCounter++;
                    pageObject.add(siteKey, new JsonPrimitive(pageName));
                    
                } else {
                    LOG.info("Page Title is null for Page Path : " + pageName);
                }
            }
        } catch (JsonException e) {
            LOG.info("\n ERROR {} ", e.getMessage());
        }

        resp.setContentType("application/json");
        resp.getWriter().write(pageObject.toString());
    }

}