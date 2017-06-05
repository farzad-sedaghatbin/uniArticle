package ir.university.toosi.tms.util;

import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FacesUtil {

    public static FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            LifecycleFactory lifecycleFactory = (LifecycleFactory)
                    FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
            FacesContextFactory contextFactory = (FacesContextFactory)
                    FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
            facesContext = contextFactory.getFacesContext(
                    request.getSession().getServletContext(), request, response, lifecycle);
            UIViewRoot view = facesContext.getApplication().getViewHandler().createView(
                    facesContext, "");
            facesContext.setViewRoot(view);
            FacesContextWrapper.setCurrentInstance(facesContext);
        }
        return facesContext;
    }

    private static abstract class FacesContextWrapper extends FacesContext {
        protected static void setCurrentInstance(FacesContext facesContext) {
            FacesContext.setCurrentInstance(facesContext);
        }
    }

}