import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/* Create custom UI Components.
 *
 * Create your own Vaadin components by inheritance and composition.
 * This is a form component inherited from VerticalLayout. Use
 * Use BeanFieldGroup to bind data fields from DTO to UI fields.
 * Similarly named field by naming convention or customized
 * with @PropertyId annotation.\n
 */

public class ChangeDetail extends FormLayout {

    com.backend.Change change;

    TextField changeId = new TextField("changeId");
    TextField userId = new TextField("userId");
    TextField rQ = new TextField("rQ");
    TextField subject =new TextField("subject");
    TextField datetime = new TextField("datetime");
    TextField linkToRq =new TextField("linkToRq");
    TextField rQiD =new TextField("rQiD");
    Label  label = new Label();

    BeanFieldGroup<com.backend.Change> formFieldBindings;

    public ChangeDetail() {

        this.configureComponents();
        this.buildLayout();
    }

    private void configureComponents() {
        this.setVisible(false);
    }

    private void buildLayout() {

        setSizeUndefined();
        setMargin(true);
     //   label.setWordwrap(true);
        addComponents(new Component[]{label});
                addComponent(new Panel());
       // addComponent(new MyComponent());
    }



    com.backend.Change edit(com.backend.Change change) {
        this.change = change;
        if(change != null) {
            // Bind the properties of the change POJO to fiels in this form
            formFieldBindings = BeanFieldGroup.bindFieldsBuffered(change, this);
            // ChangeId.focus();
        }
        label.setContentMode(ContentMode.HTML);
        if(rQ.getValue().equals("false"))
        {
            label.setValue("<p>Change "+changeId.getValue()+" by <a href=\"\">"+userId.getValue()+"</a> on "+datetime.getValue() + "</p><p>&nbsp;&nbsp;" + subject.getValue() + "</p>");
        } else {
            label.setValue("<p>Change "+changeId.getValue()+" by <a href=\"\">"+userId.getValue()+"</a> on "+datetime.getValue()+"</p><p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+subject.getValue()+"<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(auto-submit "+rQiD.getValue()+" after successfully running remote remote.all)<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"\">"+linkToRq.getValue()+"</a></p>");

        }setVisible(change != null);
        return this.change;
    }


}
