package poker.server.client.widgets;

import poker.server.client.RequestHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginWidget extends Composite {

	private static final String CANCEL = "Cancel";
	private static final String SUBMIT = "Submit";
	private static final String OK = "OK";
	private static final String DESCRIPTION = "Description:";
	private static final String GWT_LABEL_LOGIN = "gwt-Label-Login";
	private static final String GWT_LABEL_LOGIN_BIS = "gwt-Label-loginBis";
	private static final String TITLE = "Title";
	private static final String SECRET_KEY = "Secret key : ";
	private static final String CONSUMER_KEY = "Consumer key : ";
	private static final String APPLICATION_OR_DESCRIPTION_IS_EMPTY = "Application or Description is empty!";
	private static final String AGREE_THE_CONDITION = "Agree the condition!";
	private static final String GWT_CHECK_BOX_LOGIN = "gwt-CheckBox-Login";
	private static final String DESCRIBE_YOUR_APPLICATION = "Consumer key generator";
	private static final String GWT_BUTTON_LOGIN = "gwt-Button-Login";
	private static final String GWT_TEXT_AREA_LOGIN = "gwt-TextArea-Login";
	private static final String BLANK = "";

	Button btnOk;
	Label resulSecretKeyLabel;
	Label resultConsumerKeyLabel;
	FlexTable layout;

	public LoginWidget() {

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		initWidget(verticalPanel);
		
		Label lblSignIntoYour = new Label(DESCRIBE_YOUR_APPLICATION);
		lblSignIntoYour.setStyleName(TITLE);
		verticalPanel.add(lblSignIntoYour);

		FlexTable flexTable = new FlexTable();
		verticalPanel.add(flexTable);
		verticalPanel.setCellHorizontalAlignment(flexTable,
				HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.setSize("429px", "38px");

		Label lblUsername = new Label("Application:");
		lblUsername.setStyleName(GWT_LABEL_LOGIN_BIS);
		flexTable.setWidget(0, 0, lblUsername);
		lblUsername.setWidth("175px");
		flexTable.getCellFormatter().setStyleName(0, 0, GWT_LABEL_LOGIN);

		final TextBox textBoxAppli = new TextBox();
		textBoxAppli.setStyleName(GWT_TEXT_AREA_LOGIN);

		flexTable.setWidget(0, 1, textBoxAppli);
		textBoxAppli.setWidth("230px");

		Label lblNewLabel = new Label(DESCRIPTION);
		lblNewLabel.setStyleName(GWT_LABEL_LOGIN_BIS);
		flexTable.setWidget(1, 0, lblNewLabel);
		flexTable.getCellFormatter().setStyleName(1, 0, GWT_LABEL_LOGIN);

		final TextArea textArea = new TextArea();
		textArea.setCharacterWidth(50);
		textArea.setStyleName(GWT_TEXT_AREA_LOGIN);
		flexTable.setWidget(1, 1, textArea);
		flexTable.getCellFormatter().setStyleName(1, 1, GWT_LABEL_LOGIN);
		textArea.setSize("230px", "116px");

		final CheckBox chckbxCondition = new CheckBox("I agree the conditions");
		flexTable.setWidget(2, 1, chckbxCondition);
		chckbxCondition.setWidth("232px");
		flexTable.getCellFormatter().setStyleName(2, 1, GWT_CHECK_BOX_LOGIN);
		flexTable.getCellFormatter().setHorizontalAlignment(1, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);

		btnOk = new Button(OK);
		btnOk.setStyleName(GWT_BUTTON_LOGIN);
		btnOk.setVisible(false);

		Button btnSubmit = new Button(SUBMIT);
		btnSubmit.setStyleName(GWT_BUTTON_LOGIN);

		final LoginWidget loginWidget = this;

		btnSubmit.addClickHandler(new ClickHandler() {

			@SuppressWarnings("deprecation")
			public void onClick(ClickEvent event) {

				if (textBoxAppli.getText().length() == 0
						|| textArea.getText().length() == 0) {
					Window.alert(APPLICATION_OR_DESCRIPTION_IS_EMPTY);
				} else {
					if (chckbxCondition.isChecked() == false) {
						Window.alert(AGREE_THE_CONDITION);
					} else {
						RequestHandler requestHandler = new RequestHandler();
						requestHandler.getConsumerKey(loginWidget,
								textBoxAppli.getText());
					}
				}
			}

		});

		flexTable.setWidget(5, 0, btnSubmit);
		btnSubmit.setSize("100px", "25");
		flexTable.getCellFormatter().setStyleName(5, 0, GWT_BUTTON_LOGIN);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);

		Button btnCancel = new Button(CANCEL);
		btnCancel.setStyleName(GWT_BUTTON_LOGIN);
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				textBoxAppli.setText(BLANK);
				textArea.setText(BLANK);
			}
		});
		flexTable.setWidget(5, 1, btnCancel);
		btnCancel.setSize("100px", "25");
		flexTable.getCellFormatter().setStyleName(5, 1, GWT_BUTTON_LOGIN);
		flexTable.getCellFormatter().setVerticalAlignment(5, 1,
				HasVerticalAlignment.ALIGN_TOP);

		btnOk.addClickHandler(new ClickHandler() {
			@SuppressWarnings("deprecation")
			public void onClick(ClickEvent event) {

				textBoxAppli.setText(BLANK);
				textArea.setText(BLANK);
				chckbxCondition.setChecked(false);
				btnOk.setVisible(false);
				layout.setVisible(false);
			}
		});

		flexTable.getCellFormatter().setStyleName(5, 1, GWT_BUTTON_LOGIN);
		btnOk.setSize("100px", "25");
		flexTable.getCellFormatter().setHorizontalAlignment(5, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		
		layout = new FlexTable();
		layout.setCellSpacing(6);

		Label consumerLabel = new Label(CONSUMER_KEY);
		resultConsumerKeyLabel = new Label();

		Label secretTextLabel = new Label(SECRET_KEY);
		resulSecretKeyLabel = new Label();

		layout.setWidget(0, 0, new Label());
		layout.setWidget(1, 0, consumerLabel);
		layout.setWidget(1, 1, resultConsumerKeyLabel);
		layout.setWidget(2, 0, secretTextLabel);
		layout.setWidget(2, 1, resulSecretKeyLabel);
		layout.setWidget(3, 4, btnOk);
		layout.setVisible(false);
		layout.setStyleName("FlexTable");
		
		consumerLabel.setStyleName("LabelResultFlexTable");
		resultConsumerKeyLabel.setStyleName("LabelResultFlexTable");
		secretTextLabel.setStyleName("LabelResultFlexTable");
		resulSecretKeyLabel.setStyleName("LabelResultFlexTable");
		
		verticalPanel.add(layout);

	}

	public void updateResultConsumerKey(String consumerKey, String secret) {

		layout.setVisible(true);
		resultConsumerKeyLabel.setText(consumerKey);
		resulSecretKeyLabel.setText(secret);
		btnOk.setVisible(true);
	}

}
