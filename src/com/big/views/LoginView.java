import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout implements View {


    public static String NAME="login";
    public static final String TITLE_ID = "dashboard-title";
    private Label titleLabel;
    TextField username = new TextField("Username");
    PasswordField password = new PasswordField("Password");
    CheckBox rem_me= new CheckBox("Remember me", true);
    Button signin = new Button("Sign In",new Button.ClickListener(){
            @Override
            public void buttonClick(final ClickEvent event) {

                //
                // Validate the fields using the navigator. By using validors for the
                // fields we reduce the amount of queries we have to use to the database
                // for wrongly entered passwords
                //

                if (!username.isValid() || !password.isValid()) {
                    return;
                }

                String usr = username.getValue();
                String pass = password.getValue();

                //
                // Validate username and password with database here. For examples sake
                // I use a dummy username and password.
                //
                boolean isValid = usr.equals("sandeep.jindal@oracle.com")
                        && pass.equals("passw0rd");

                if (isValid) {

                    // Store the current user in the service session
                    getSession().setAttribute("user", usr);
                    // Navigate to main view
                    getUI().getNavigator().navigateTo(DataBetweenDates.NAME);//

                } else {

                    // Wrong password clear the password field and refocuses it
                    password.setValue(null);
                    password.setStyleName(ValoTheme.NOTIFICATION_FAILURE);
                    password.focus();

                }



            }
        });

    public static String getNAME() {
        return NAME;
    }

    public static void setNAME(String NAME) {
        LoginView.NAME = NAME;
    }

    // Validator for validating the passwords
    private static final class PasswordValidator extends
            AbstractValidator<String> {

        public PasswordValidator() {
            super("The password provided is not valid");
        }

        @Override
        protected boolean isValidValue(String value) {
            //
            // Password must be at least 8 characters long and contain at least
            // one number
            //
            if (value != null
                    && (value.length() < 8 || !value.matches(".*\\d.*"))) {
                return false;
            }
            return true;
        }

        @Override
        public Class<String> getType() {
            return String.class;
        }
    }

    public LoginView() {
        setSizeFull();

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLabels());
        loginPanel.addComponent(buildFields());

        loginPanel.setStyleName("white");
        return loginPanel;
    }

    private Component buildFields() {
        VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        username.setWidth("300px");
        username.setRequired(true);
        username.setInputPrompt("Your username (eg. joe@email.com)");
        username.addValidator(new EmailValidator(
                "Username must be an email address"));
        username.setInvalidAllowed(false);
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        username.setValue("sandeep.jindal@oracle.com");

        // Create the password input field

        password.setWidth("300px");
        password.addValidator(new PasswordValidator());
        password.setRequired(true);
        password.setValue("passw0rd");
        password.setNullRepresentation("");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);


        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();


        fields.addComponents(username, password, rem_me, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
        fields.setMargin(true);

        return fields;
    }

    private Component buildLabels() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);

        titleLabel = new Label("Welcome to P4Tracker!");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);
        header.setMargin(true);
        return header;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // focus the username field when user arrives to the login view
        username.focus();

    }
} 
