package poker.admin.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AuthentificationWidget extends Composite {

	private static final String ADMIN_USERNAME_NOT_CORRECT = "Admin Username not correct";
	private static final String ADMIN_PASSWORD_NOT_CORRECT = "Admin password not correct";
	private static final String PASSWORD = "1234";
	private static final String ADMIN = "Admin";
	TextBox userNameTextBox;
	PasswordTextBox passWordTextBox;
	TextBox consumerKeyTextBox;
	AdminWidget adminWidget;
	HelpWidget helpWidget;
	Label messageLabel;

	public AuthentificationWidget(AdminWidget admin, HelpWidget help) {

		// Create a table to layout the form options
		adminWidget = admin;
		helpWidget = help;

		FlexTable layout = new FlexTable();

		layout.setCellSpacing(6);
		FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

		// Add a title to the form

		Label topLabel = new Label("Authentification administrateur");
		topLabel.setStyleName("LoginAuthLabel");
		layout.setWidget(0, 0, topLabel);

		cellFormatter.setColSpan(0, 0, 3);
		cellFormatter.setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);

		// Add some standard form options
		userNameTextBox = new TextBox();
		passWordTextBox = new PasswordTextBox();

		Label userNameLabel = new Label("Username : ");
		userNameLabel.setStyleName("LoginAuthLabel");
		Label passWordLabel = new Label("Password : ");
		passWordLabel.setStyleName("LoginAuthLabel");

		Label consumerKeyLabel = new Label("Admin Consumer key : ");
		consumerKeyLabel.setStyleName("LoginAuthLabel");

		consumerKeyTextBox = new TextBox();

		layout.setWidget(1, 0, userNameLabel);
		layout.setWidget(1, 1, userNameTextBox);
		layout.setWidget(2, 0, passWordLabel);
		layout.setWidget(2, 1, passWordTextBox);

		layout.setWidget(3, 0, consumerKeyLabel);
		layout.setWidget(3, 1, consumerKeyTextBox);

		Button okButton = new Button("OK");
		okButton.addClickHandler(getOkButtonHandler(this));
		okButton.setStyleName("OkButton");

		layout.setWidget(4, 2, okButton);
		// Wrap the content in a DecoratorPanel
		DecoratorPanel decPanel = new DecoratorPanel();
		decPanel.setWidget(layout);

		messageLabel = new Label();

		VerticalPanel horizontalPanel = new VerticalPanel();
		horizontalPanel.add(decPanel);
		horizontalPanel.add(messageLabel);

		initWidget(horizontalPanel);

	}

	private ClickHandler getOkButtonHandler(
			final AuthentificationWidget authentificationWidget) {

		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (userNameTextBox.getText().equals(ADMIN)) {

					if (passWordTextBox.getText().equals(PASSWORD)) {

						authentificationWidget.setVisible(false);
						adminWidget.setVisible(true);
						helpWidget.setVisible(true);
						adminWidget.displayExistindGameTypes(consumerKeyTextBox
								.getText());
					} else
						messageLabel.setText(ADMIN_PASSWORD_NOT_CORRECT);
				} else
					messageLabel.setText(ADMIN_USERNAME_NOT_CORRECT);
			}
		};

	}
}
