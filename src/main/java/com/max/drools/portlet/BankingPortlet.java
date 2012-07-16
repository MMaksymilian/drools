package com.max.drools.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.max.drools.portlet.bean.Actor;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.*;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import javax.portlet.*;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: USER
 * Date: 16.07.12
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class BankingPortlet extends GenericPortlet {

    private String viewJsp;
    private static Log logger = LogFactoryUtil.getLog(BankingPortlet.class);

    @Override
    public void init() {
        viewJsp = getInitParameter("view-jsp");
    }

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        include(viewJsp, request, response);
    }


    private void include(String view, RenderRequest request, RenderResponse response) throws PortletException, IOException {
        PortletRequestDispatcher portletRequestDispatcher = getPortletContext().getRequestDispatcher(view);
        if (portletRequestDispatcher == null) {
            logger.error("podana strona nie istnieje - '" + view + "'");
        } else {
            portletRequestDispatcher.include(request, response);
        }
    }


    private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        DecisionTableConfiguration config = KnowledgeBuilderFactory.newDecisionTableConfiguration();
        config.setInputType(DecisionTableInputType.XLS);
        kbuilder.add(ResourceFactory.newClassPathResource("Banking.xls"), ResourceType.DTABLE, config);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error : errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        try {
            response.setPortletMode(PortletMode.VIEW);
            KnowledgeBase kbase = readKnowledgeBase();
            StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
            KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "test");
            Actor actor = new Actor();
            ksession.insert(actor);
            ksession.fireAllRules();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        response.setPortletMode(PortletMode.VIEW);
    }
}
