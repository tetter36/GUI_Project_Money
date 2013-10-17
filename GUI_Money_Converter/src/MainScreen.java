import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

//download from web
//parse from HTML (regular expressions) check string api for regular expression
	//use a Pattern * = Pattern.complie type thing. and matcher. look for java
	//regular expression groups.


public class MainScreen {

	protected Shell shell;
	private Text textFromAmt;
	private Text textToAmt;
	boolean lock = true;
	String text; //holds info from one text box to set in the other

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainScreen window = new MainScreen();
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
		composite_1.setLayout(new GridLayout(2, false));
		
		Label lblFrom = new Label(composite_1, SWT.NONE);
		lblFrom.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblFrom.setText("From:");
		
		Combo comboFrom = new Combo(composite_1, SWT.READ_ONLY);
		comboFrom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		//comboFrom.setItems({"USD", "EUR", "JPY", "GPB", "CHF", "CNY", "KRW", "INR"});
		
		comboFrom.add("USD");
		comboFrom.add("EUR");
		comboFrom.add("JPY");
		comboFrom.add("GPB");
		comboFrom.add("CHF");
		comboFrom.add("CNY");
		comboFrom.add("KRW");
		comboFrom.add("INR");
		
		Label lblAmount = new Label(composite_1, SWT.NONE);
		lblAmount.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAmount.setText("Amount:");
		
		textFromAmt = new Text(composite_1, SWT.BORDER);
		textFromAmt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if(lock==true){
					lockModifier();
					text = textFromAmt.getText();
					textToAmt.setText(text);
				}
				
			}
		});
		textFromAmt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTo = new Label(composite_1, SWT.NONE);
		lblTo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTo.setText("To:");
		
		Combo comboTo = new Combo(composite_1, SWT.READ_ONLY);
		comboTo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboTo.add("USD");
		comboTo.add("EUR");
		comboTo.add("JPY");
		comboTo.add("GPB");
		comboTo.add("CHF");
		comboTo.add("CNY");
		comboTo.add("KRW");
		comboTo.add("INR");
		
		Label lblAmount_1 = new Label(composite_1, SWT.NONE);
		lblAmount_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAmount_1.setText("Amount:");
		
		textToAmt = new Text(composite_1, SWT.BORDER);
		textToAmt.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (lock==true){
					lockModifier();
					text = textToAmt.getText();
					textFromAmt.setText(text);
				}
			}
		});
		textToAmt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}//createContents method
	
	public void lockModifier(){
		lock = false;
	}//lock modifier method
	
	public void getURL(){
		URL webpage = null;
		try {
			webpage = new URL("http://www.x-rates.com/table/?from=USD");
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
		
        String inputLine;

        try {
			while ((inputLine = in.readLine()) != null) 
			    System.out.println(inputLine);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}//getURL method
}//main class
