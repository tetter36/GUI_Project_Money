import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.*;

import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import swing2swt.layout.BorderLayout;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

import swing2swt.layout.FlowLayout;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

/**This program converts current currency rates into the
 * desired currency rate of the user based on their combo
 * box selection and the amount that they type in the text
 * box.
 * 
 * @author Brett Peterson*/
public class MainScreen {

	protected Shell shell;
	private Text textFromAmt;//text box info
	private Text textToAmt;//text box info
	String text; //holds info from one text box to set in the other
	StringBuilder contentBuilder = new StringBuilder();//combines each line from website (x-rates) into a single string
	String fullWebpage = contentBuilder.toString();//website(x-rates) converted into a single string
	String chosenRateTo;//rate user chose
	String chosenRateFrom;//rate user chose
	float conversion;//conversion rate based on user selection
	String conversionString;//converts the float conversion to string to be displayed in text box
	
	//the following are the current rates to USD
	Float EURrate;
	Float JPYrate;
	Float GBPrate;
	Float CHFrate;
	Float CNYrate;
	Float KRWrate;
	Float INRrate;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainScreen window = new MainScreen();
			window.getURL();//gets the URL info and gets the rates using Regex
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new BorderLayout(0, 0));
		
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(BorderLayout.SOUTH);
		composite.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		Button btnRefresh = new Button(composite, SWT.NONE);
		btnRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getURL();
			}
		});
		btnRefresh.setText("Refresh Rates");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setLayoutData(BorderLayout.CENTER);
		composite_1.setLayout(new GridLayout(5, false));
		
		Label lblFrom = new Label(composite_1, SWT.NONE);
		lblFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFrom.setText("From:");
		
		//combo box for user selected rate 
		final Combo comboFrom = new Combo(composite_1, SWT.READ_ONLY);
		comboFrom.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		comboFrom.add("USD");
		comboFrom.add("EUR");
		comboFrom.add("JPY");
		comboFrom.add("GBP");
		comboFrom.add("CHF");
		comboFrom.add("CNY");
		comboFrom.add("KRW");
		comboFrom.add("INR");
		
		new Label(composite_1, SWT.NONE);
		
		Label lblTo = new Label(composite_1, SWT.NONE);
		lblTo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTo.setText("To:");
		
		//combo box for user selected rate 
		final Combo comboTo = new Combo(composite_1, SWT.READ_ONLY);
		comboTo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboTo.add("USD");
		comboTo.add("EUR");
		comboTo.add("JPY");
		comboTo.add("GBP");
		comboTo.add("CHF");
		comboTo.add("CNY");
		comboTo.add("KRW");
		comboTo.add("INR");
		
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		
		Label label = new Label(composite_1, SWT.NONE);
		label.setText("<-->");
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		
		Label lblAmount = new Label(composite_1, SWT.NONE);
		lblAmount.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAmount.setText("Amount:");
		
		textFromAmt = new Text(composite_1, SWT.BORDER);
		textFromAmt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				//if statement checks to make sure that the text box is not empty or something
				//other than a number
				char c = e.character;
				if(textFromAmt.getText().isEmpty() || !(Character.isDigit(c))){
					textToAmt.setText("");
				}
				else{
					//All of the if statements are to calculate the conversion based on what the user selected
					chosenRateFrom = comboFrom.getText();
					chosenRateTo = comboTo.getText();
					if(chosenRateFrom.equalsIgnoreCase("USD")){
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (1/EURrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (1/GBPrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (1/JPYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (1/CHFrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (1/CNYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (1/KRWrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){
							conversion = (1/INRrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
					}
					else if(chosenRateFrom.equalsIgnoreCase("EUR")){
						if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (GBPrate/EURrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = EURrate * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (EURrate/JPYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (EURrate/CHFrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (EURrate/CNYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (EURrate/KRWrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){
							conversion = (EURrate/INRrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
					}
					else if(chosenRateFrom.equalsIgnoreCase("JPY")){
						if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (GBPrate/JPYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = JPYrate * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (JPYrate/GBPrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (JPYrate/CHFrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (JPYrate/CNYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (JPYrate/KRWrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){
							conversion = (JPYrate/INRrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
					}
					else if(chosenRateFrom.equalsIgnoreCase("GBP")){
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (GBPrate/EURrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = GBPrate * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (GBPrate/GBPrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (GBPrate/JPYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (GBPrate/CHFrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (GBPrate/CNYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (GBPrate/KRWrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else{//INR 
							conversion = (GBPrate/INRrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
					}
					else if(chosenRateFrom.equalsIgnoreCase("CHF")){
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (CHFrate/EURrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = CHFrate * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (CHFrate/JPYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (GBPrate/CHFrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (CHFrate/CNYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (CHFrate/KRWrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){//INR 
							conversion = (CHFrate/INRrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
					}
					else if (chosenRateFrom.equalsIgnoreCase("CNY")){
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (CNYrate/EURrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = CNYrate * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (CNYrate/JPYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (CNYrate/CHFrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (CNYrate/GBPrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (CNYrate/KRWrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){
							conversion = (CNYrate/INRrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
					}
					else if (chosenRateFrom.equalsIgnoreCase("KRW")){
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (KRWrate/EURrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = KRWrate * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (KRWrate/JPYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (KRWrate/CHFrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (KRWrate/CNYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (KRWrate/GBPrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){//INR 
							conversion = (KRWrate/INRrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
							}
					}
					else{ //INR
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (INRrate/EURrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = INRrate * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}	
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (INRrate/JPYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (INRrate/CHFrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (INRrate/CNYrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}		
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (INRrate/KRWrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){//INR 
							conversion = (GBPrate/INRrate) * Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textFromAmt.getText());
							conversionString = Float.toString(conversion);
							textToAmt.setText(conversionString);
						}
					}//INR
				}//if statement to test if text box is empty
			}//key released event
		});
		
		textFromAmt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_1, SWT.NONE);
		
		Label lblAmount_1 = new Label(composite_1, SWT.NONE);
		lblAmount_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAmount_1.setText("Amount:");
		
		textToAmt = new Text(composite_1, SWT.BORDER);
		textToAmt.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				//if statement checks to make sure that the text box is not empty or something
				//other than a number
				char c = e.character;
				if(textToAmt.getText().isEmpty() || !(Character.isDigit(c))){
					textFromAmt.setText("");
				}
				else{
					//All of the if statements are to calculate the conversion based on what the user selected
					chosenRateFrom = comboFrom.getText();
					chosenRateTo = comboTo.getText();
					if(chosenRateTo.equalsIgnoreCase("USD")){
						if(chosenRateFrom.equalsIgnoreCase("EUR")){
							conversion = (1/EURrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (1/GBPrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (1/JPYrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (1/CHFrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (1/CNYrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (1/KRWrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){//INR 
							conversion = (1/INRrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
					}
					else if(chosenRateFrom.equalsIgnoreCase("GBP")){
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (GBPrate/EURrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = GBPrate * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (GBPrate/JPYrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (GBPrate/CHFrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (GBPrate/CNYrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (GBPrate/KRWrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){
							conversion = (GBPrate/INRrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
					}
					else if(chosenRateFrom.equalsIgnoreCase("CHF")){
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (CHFrate/EURrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = CHFrate * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (CHFrate/JPYrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (CHFrate/GBPrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (CHFrate/CNYrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (CHFrate/KRWrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){//INR 
							conversion = (CHFrate/INRrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
					}
					else if (chosenRateFrom.equalsIgnoreCase("CNY")){
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (CNYrate/EURrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = CNYrate * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (CNYrate/JPYrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (CNYrate/CHFrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (CNYrate/GBPrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (CNYrate/KRWrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){//INR 
							conversion = (CNYrate/INRrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
					}
					else if (chosenRateFrom.equalsIgnoreCase("JPY")){
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (JPYrate/EURrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = JPYrate * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (JPYrate/GBPrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (JPYrate/CHFrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (JPYrate/GBPrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (JPYrate/KRWrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){//INR 
							conversion = (JPYrate/INRrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
					}
					else if (chosenRateFrom.equalsIgnoreCase("KRW")){
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (KRWrate/EURrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = KRWrate * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (KRWrate/JPYrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (KRWrate/CHFrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (KRWrate/CNYrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){
							conversion = (KRWrate/GBPrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("INR")){
							conversion = (KRWrate/INRrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
					}
					else{ //INR
						if(chosenRateTo.equalsIgnoreCase("EUR")){
							conversion = (INRrate/EURrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("USD")){
							conversion = INRrate * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("JPY")){
							conversion = (INRrate/JPYrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CHF")){
							conversion = (INRrate/CHFrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("CNY")){
							conversion = (INRrate/CNYrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("KRW")){
							conversion = (INRrate/KRWrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else if(chosenRateTo.equalsIgnoreCase("GBP")){//INR 
							conversion = (INRrate/GBPrate) * Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
						else{
							conversion = Float.parseFloat(textToAmt.getText());
							conversionString = Float.toString(conversion);
							textFromAmt.setText(conversionString);
						}
					}//INR
				}//if/else statement that sets textFromAmt as empty if textToAmt is empty
			}//key released event
		});
		textToAmt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}//createContents method
	
	/**gets the HTML from x-rates and stores all of the HTML
	 * to a single string variable*/
	public void getURL(){
		URL webpage = null;
		try {
			webpage = new URL("http://www.x-rates.com/table/?from=USD&amount=1.00");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URLConnection html = null;
		try {
			html = webpage.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        BufferedReader in = null;
		try {
			in = new BufferedReader(
			                        new InputStreamReader(
			                        html.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        try {
        	String inputLine;
			while ((inputLine = in.readLine())!=null){
				contentBuilder.append(inputLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        getRates();
     
        try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}//getURL method
	
	/**gets the rates pulled from x-rates' website and uses regex to pull
	 * the desired rates and stores them to variables*/
	public void getRates(){
		fullWebpage = contentBuilder.toString();
		Pattern eur = Pattern.compile("from=EUR&amp;to=USD'>\\d*\\.\\d*<");
		Matcher m = eur.matcher(fullWebpage);
		while (m.find()){
			Pattern rate = Pattern.compile("\\d*\\.\\d*");//pulls out the rate for specified pattern
			Matcher m2 = rate.matcher(m.group());
			while (m2.find()){
					EURrate = Float.parseFloat(m2.group());
			}//second while
		}//first while
		
		//This gets the rate for the Great Britain Pound
		Pattern gbp = Pattern.compile("from=GBP&amp;to=USD'>\\d*\\.\\d*<");
		m = gbp.matcher(fullWebpage);
		while (m.find()){
			Pattern rate = Pattern.compile("\\d*\\.\\d*");//pulls out the rate for specified pattern
			Matcher m2 = rate.matcher(m.group());
			while (m2.find()){
					GBPrate = Float.parseFloat(m2.group());
			}//second while
		}//first while
		
		//This gets the rate for the Japanese Yen (JPY)
		Pattern jpy = Pattern.compile("from=JPY&amp;to=USD'>\\d*\\.\\d*<");
		m = jpy.matcher(fullWebpage);
		while (m.find()){
			Pattern rate = Pattern.compile("\\d*\\.\\d*");//pulls out the rate for specified pattern
			Matcher m2 = rate.matcher(m.group());
			while (m2.find()){
					JPYrate = Float.parseFloat(m2.group());
			}//second while
		}//first while
		
		//Gets rate for the Swiss Franc (CHF)
		Pattern chf = Pattern.compile("from=CHF&amp;to=USD'>\\d*\\.\\d*<");
		m = chf.matcher(fullWebpage);
		while (m.find()){
			Pattern rate = Pattern.compile("\\d*\\.\\d*");//pulls out the rate for specified pattern
			Matcher m2 = rate.matcher(m.group());
			while (m2.find()){
					CHFrate = Float.parseFloat(m2.group());
			}//second while
		}//first while
		
		//Gets rate for the Chinese Yuan (CNY)
		Pattern cny = Pattern.compile("from=CNY&amp;to=USD'>\\d*\\.\\d*<");
		m = cny.matcher(fullWebpage);
		while (m.find()){
			Pattern rate = Pattern.compile("\\d*\\.\\d*");//pulls out the rate for specified pattern
			Matcher m2 = rate.matcher(m.group());
			while (m2.find()){
					CNYrate = Float.parseFloat(m2.group());
			}//second while
		}//first while
		
		//Gets rate for South Korean Won (KRW)
		Pattern krw = Pattern.compile("from=KRW&amp;to=USD'>\\d*\\.\\d*<");
		m = krw.matcher(fullWebpage);
		while (m.find()){
			Pattern rate = Pattern.compile("\\d*\\.\\d*");//pulls out the rate for specified pattern
			Matcher m2 = rate.matcher(m.group());
			while (m2.find()){
					KRWrate = Float.parseFloat(m2.group());
			}//second while
		}//first while
		
		//Gets rate for Indian Rupee (INR)
		Pattern inr = Pattern.compile("from=INR&amp;to=USD'>\\d*\\.\\d*<");
		m = inr.matcher(fullWebpage);
		while (m.find()){
			Pattern rate = Pattern.compile("\\d*\\.\\d*");//pulls out the rate for specified pattern
			Matcher m2 = rate.matcher(m.group());
			while (m2.find()){
					INRrate = Float.parseFloat(m2.group());
			}//second while
		}//first while
	}//getRates method
}//main class
