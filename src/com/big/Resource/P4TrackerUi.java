import com.backend.HiveJdbcConnection;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import javax.servlet.annotation.WebServlet;
import java.util.Locale;

/**
 * Created by sandeep on 17/8/15.
 *
 */
@Title("p4tracker")
@Theme("reindeer")
public class P4TrackerUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        setLocale(Locale.US);

       // DashboardEventBus.register(this);
        Responsive.makeResponsive(this);
        addStyleName(ValoTheme.UI_WITH_MENU);

        HiveJdbcConnection.createConnection();
      //  addWindow(new Piechart());
        //
        // Create a new instance of the navigator. The navigator will attach
        // itself automatically to this view.
        //
        new Navigator(this, this);

        //
        // The initial log view where the user can login to the application
        //
        getNavigator().addView(LoginView.NAME, LoginView.class);//
        getNavigator().addView(DashboardView.NAME, DashboardView.class);
      //  getNavigator().addView(MainView.NAME,MainView.class);

        //
        // Add the main view of the application
        //

        getNavigator().addView(DataBetweenDates.NAME,
                DataBetweenDates.class);

        //  Presenter presenter = new Presenter(firstView, secondView);

        //
        // We use a view change handler to ensure the user is always redirected
        // to the login view if the user is not logged in.
        //
        getNavigator().addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {

                // Check if a user has logged in
                boolean isLoggedIn = getSession().getAttribute("user") != null;
                boolean isLoginView = event.getNewView() instanceof LoginView;

                if (!isLoggedIn && !isLoginView) {
                    // Redirect to login view always if a user has not yet
                    // logged in
                    getNavigator().navigateTo(LoginView.NAME);
                    return false;

                } else if (isLoggedIn && isLoginView) {
                    // If someone tries to access to login view while logged in,
                    // then cancel
                    return false;
                }

                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {

            }
        });
    }

    @WebServlet(urlPatterns = "/webapp/*")
    @VaadinServletConfiguration(ui = P4TrackerUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
