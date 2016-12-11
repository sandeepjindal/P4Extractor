import com.backend.HiveJdbcConnection;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


/**
 * Created by mukesh on 22/8/15.
 */
@Title("P4Extractor")
@Theme("reindeer")
public class VaadinApplication extends UI {
    @Override
    public void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        setContent(layout);
        layout.addComponent(new Label("Hello, world!"));

        HiveJdbcConnection.createConnection();
       // new Navigator(this,this);
    }
}
