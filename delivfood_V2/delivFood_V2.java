/**
 * COMMENT SECTION
 * ==========================================	
 * Creator: Michael Xie
 * Date Created:Jan. 8, 2020
 * Date Finished: Jan 17, 2019
 * Description: A GUI based program off the popular program delivfood!
 * Files Needed: refund.gif || thanks.gif || welcome.gif|| welcome2.gif||
 * Note: The French easter egg is NOT translated by a professional(it was done by Google Translate). The grammer is not perfect but the basic idea should be able to be conveyed.
 * Note: Images used are copyright to San-X Inc.
 * Note: This program will be able to recover data from the manager document about receipts and revenue. Please do not tamper with it.
 * Note: In order to round items to two decimal places, when these variables are displayed, they will show two decimal places using the Java String() format method.
 */

//Package used
package delivfood_V2;

//All Imports needed
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;


public class delivFood_V2 extends JFrame{
	private static final long serialVersionUID = 1L;//A unique identifier for Serizable clsses

	//All Items used
	int cartCounter = 0;// This will count the length of the items in the cart
	double revenue;// Counter used for how much revenue that the company got
	double total = 0;// Counter for the total amount of money that the user has to pay
	int nextFood = 6;// Used to replace the length of the inventory to only be how many items are in the array, not the length of the array
	static String[] food = new String[11];//This array records all the food names
	static int[] foodStock = new int[11];//This array records all the stock of food
	static double[] foodPrice = new double[11];//This array records all the prices of food

	//All of the prepopulated values of these can be found in the main method

	int[] cart = new int[100];//The cart. Can hold a maximum of 100 items. 
	double tax = 0.00;//The value used to hold tax
	int txtNum;//The variable used for the next receipt number.

	//All Counters Used
	//The main panel that all other panels will build themselves onto
	JPanel parentPanel;

	///Used in Intro Panel
	JPanel introBut;//Panel used to put all the introduction buttons on the screen
	JPanel introPane;//Panel used to put ALL introduction components on the screen

	///Used in Manager Menu
	JPanel manageBut;

	///Buttons used in Manage Menu
	JButton add;
	JButton updateBut;
	JButton display;//Used in customer too
	JPanel manageMenu;

	//Buttons used in Customer Menu
	JPanel custBut;
	JPanel custMenu;
	JLabel custMessage;
	int theftCounter = 0;//Checks to make sure all items are checked out before leaving the menu

	//Used in Manager Menu
	JButton priceButton;

	//Used in Cart Adding
	JButton cartAdd[];

	//Creates and writes on the manager document, giving information about the receipts and revenue made.
	private void manageDoc() {
		try {
			FileWriter write = new FileWriter("managerDocument.txt");//Creates the manager document with Filewriter and Printwriter. If the file is already there, it will rewrite on the existing file
			PrintWriter out = new PrintWriter(write);

			File file = new File("managerDocument.txt");//Allows reading from the document
			Scanner scan = new Scanner(file);//Scanner for reading certain parts of the document
			txtNum++;//Adds the value by 1
			out.println(txtNum);
			out.println(revenue);
			out.println("Note: Above items are used only for the program. Please do not adjust the top numbers.");
			out.println(String.format("Revenue Made: $ %1.02f" ,revenue));
			out.println("The number of reciepts made is "+Integer.toString(txtNum-1));
			out.println(String.format("The revenue made is $ %1.02f" ,revenue));

			out.close();//Closes the PrintWriter

			txtNum =  Integer.parseInt(scan.nextLine());
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	//Searches for the manager document. If there is one, it will be able to make receipts and recover the revenue from the last used time.
	private void extractManage() {
		try {
			File file = new File("managerDocument.txt");
			Scanner scan;
			scan = new Scanner(file);
			txtNum = Integer.parseInt(scan.nextLine());
			revenue = Double.parseDouble(scan.nextLine());
		} catch (Exception FileNotFoundException) {
			System.out.println("File not found");
			//No need for concern, this will just say an error message that it can't retrieve information since no file can be found. If this is the first time using this, the program will create the document after the first transaction is made. 
		}

	}

	//Panels used
	//Starting Screen Buttons
	public JPanel introBut() throws IOException {
		parentPanel.removeAll();
		introBut = new JPanel();
		introBut.setBackground(Color.white);	
		FlowLayout flow = new FlowLayout();
		introBut.setLayout(flow);

		//Button for the customers
		JButton customer = new JButton("Customer");
		customer.setBackground(Color.yellow);
		customer.addActionListener(e -> {
			custMenu();
		});

		//Button for the manager
		JButton manager = new JButton("Manager");
		manager.setBackground(Color.pink);
		manager.addActionListener(e -> {
			manage();
		});

		//Button to exit and close the program
		JButton kill = new JButton("Exit");
		kill.setBackground(Color.red);
		kill.setForeground(Color.white);
		kill.addActionListener(e -> {
			setVisible(false);
			dispose();
		}); 
		//Adds the components to the panel
		introBut.add(manager);
		introBut.add(customer);
		introBut.add(kill);

		//Sets the location of the panel
		setLocation(500, 220);

		return introBut;
	}

	//Intro Panel
	//The basic selection screen for the user, allowing them to go to the manager menu, customer menu, or leave the program
	public JPanel introPane() throws IOException {
		//Components used in the method
		JLabel image = new JLabel(new ImageIcon("welcome.gif"));
		JLabel image2 = new JLabel(new ImageIcon("welcome2.gif"));
		JLabel welcome = new JLabel("Welcome to delivfood! Please click on who you are with the options below.", JLabel.CENTER);

		introPane = new JPanel();
		introPane.setPreferredSize(new Dimension(1000,400));//Sets the dimensions of the panel


		introPane.setLayout(new GridLayout(2,1));
		introPane.add(welcome);

		introPane.setBackground(Color.white);
		introPane.add(image);
		introPane.add(introBut());
		introPane.add(image2);
		parentPanel.add(introPane);
		parentPanel.setBackground(Color.yellow);
		add(parentPanel);

		pack();//Sizes the frame to make sure all contents are at their preferred size

		//Sets the title to delivfood.exe
		setTitle("delivfood.exe");

		return(introPane);
	}

	//Manager menu buttons
	//The buttons used in selection for the manager menu
	public JPanel manageBut(){
		manageBut = new JPanel();
		manageBut.setBackground(Color.white);
		FlowLayout flow = new FlowLayout();
		introBut.setLayout(flow);

		//Button to add a new product
		add = new JButton("Add Products");
		add.setBackground(Color.blue);
		add.setForeground(Color.white);
		add.addActionListener(e -> {
			addProducts();
		});

		//Button to update stock of products
		updateBut = new JButton("Update Products");
		updateBut.setBackground(Color.green);
		updateBut.addActionListener(e -> {
			manUpdate();
		});

		//Button to display products
		display = new JButton("Display products");
		display.setBackground(Color.yellow);
		display.addActionListener(e -> {
			manageDisplay();
		});

		//Button to return to the main menu of the program
		JButton exitMan = new JButton("Return to main menu");
		exitMan.setBackground(Color.red);
		exitMan.setForeground(Color.white);
		exitMan.addActionListener(e ->{
			parentPanel.remove(manageBut);
			parentPanel.remove(manage());
			parentPanel.revalidate();
			parentPanel.repaint();
			try {
				introPane();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		manageBut.add(add);
		manageBut.add(updateBut);
		manageBut.add(display);
		manageBut.add(exitMan);

		return manageBut;
	}

	//Manager Menu
	//The basic selection screen for the manager, allowing them to 'Add products', 'Update products', 'Display Products', and 'return to main menu'
	public JPanel manage() {
		parentPanel.remove(introBut);
		parentPanel.remove(introPane);
		parentPanel.revalidate();
		parentPanel.repaint();

		//Creates the Panel for the manager menu
		manageMenu = new JPanel();
		manageMenu.setBackground(Color.white);
		manageMenu.setLayout(new GridLayout(3,1));

		//Stating on revenue made
		JLabel salesUpdate = new JLabel(String.format("Revenue Made: $ %1.02f" ,+ revenue), JLabel.CENTER);


		JLabel manageMessage = new JLabel("Welcome to DelivFood manager menu.", JLabel.CENTER);//Introduction of the manager menu
		manageMenu.add(manageMessage);
		manageMenu.add(salesUpdate);
		manageMenu.add(manageBut());

		//Adds the manager menu to the parent panel
		parentPanel.add(manageMenu);
		parentPanel.setBackground(Color.green);
		setTitle("delivfood Manager Menu.exe");
		pack();

		return manageMenu;
	}

	//ManagerDisplay option of all items, stocks, and prices
	public JPanel manageDisplay() {
		parentPanel.remove(manageBut);
		parentPanel.remove(manageMenu);
		parentPanel.revalidate();
		parentPanel.repaint();

		//Sets the size of the display to be 700 pixels x 500 pixels
		setSize(700,500);
		JPanel display = new JPanel();
		JPanel buttonL = new JPanel();//Panel used to make sure the button is the biggest at the bottom
		display.setBackground(Color.white);
		JLabel label = new JLabel("");

		GridLayout layout = new GridLayout(nextFood+4, 3);
		display.setLayout(layout);

		JLabel nameLabel[] = new JLabel[100];
		JLabel stockLabel[] = new JLabel[100];
		JLabel priceLabel[] = new JLabel[100];

		JLabel blank1 = new JLabel("");
		JLabel menu = new JLabel("Inventory of DelivFood products", JLabel.CENTER);
		JLabel blank2 = new JLabel("");

		JLabel name = new JLabel("Name:", JLabel.CENTER);
		JLabel stock = new JLabel("Stock:", JLabel.CENTER);
		JLabel price = new JLabel("Price:", JLabel.CENTER);

		display.add(blank1);
		display.add(menu);
		display.add(blank2);

		display.add(name);
		display.add(stock);
		display.add(price);

		//Will show the layout in a border format
		display.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
		parentPanel.setBorder(new MatteBorder(2, 2, 2, 2, Color.CYAN));
		for(int i = 0; i <= nextFood; i++){
			if (food[i] == null  || foodStock[i] == 0){
			} else {
				nameLabel[i] =new JLabel(food[i], JLabel.CENTER);
				nameLabel[i].setText(food[i]);
				nameLabel[i].setLayout(layout);
				nameLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				display.add(nameLabel[i]);

				stockLabel[i] =new JLabel(String.valueOf(foodStock[i]), JLabel.CENTER);
				stockLabel[i].setText(String.valueOf(foodStock[i]));
				stockLabel[i].setLayout(layout);
				stockLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				display.add(stockLabel[i]);

				priceLabel[i] =new JLabel("", JLabel.CENTER);
				priceLabel[i].setText(String.valueOf(String.format("%1.02f", +foodPrice[i])));
				priceLabel[i].setLayout(layout);
				priceLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				display.add(priceLabel[i]);
			}}

		//Adds an exit button to the program
		JButton buttonDis = new JButton("Exit");
		buttonDis.addActionListener(e -> {
			parentPanel.remove(buttonL);
			parentPanel.remove(label);
			parentPanel.remove(display);
			parentPanel.revalidate();
			parentPanel.repaint();
			manage();
		});

		//Adds the button to the display and the rest to the parent panel
		buttonL.setBackground(Color.green);
		buttonL.add(buttonDis);
		parentPanel.add(buttonL);
		setSize(610,600);	
		parentPanel.add(display);
		pack();
		return display;
	}

	//Manager Add Products
	//Allow the manager for the creation of new foods
	public JPanel addProducts() {
		parentPanel.remove(manageBut);
		parentPanel.remove(manageMenu);
		parentPanel.revalidate();
		parentPanel.repaint();

		//Creation of the panel and the exit button
		JPanel update = new JPanel();
		JButton exit = new JButton("Exit");

		exit.addActionListener(e ->{
			parentPanel.remove(update);
			parentPanel.remove(addProducts());
			parentPanel.revalidate();
			parentPanel.repaint();
			manage();
		});

		if(nextFood > 10) {//If the number of items is greater than 10, the program will not allow the creation of a new product
			JLabel error = new JLabel("You cannot add anymore products.");
			JButton errorBut= new JButton("Okay");
			update.add(error);
			update.add(errorBut);
			parentPanel.add(update);
			errorBut.addActionListener(e ->{
				parentPanel.remove(update);
				parentPanel.revalidate();
				parentPanel.repaint();

				manage();
			});

		}
		else {
			JLabel askAdd = new JLabel("Please enter below to add new products, prices, and stocks.");

			JTextField inputName = new JTextField("Name", 30);
			JTextField inputPrice = new JTextField("Price", 30);
			JTextField inputStock = new JTextField("Stock", 30);

			inputName.setMaximumSize(new Dimension(123, 123));
			inputPrice.setMaximumSize(new Dimension(123, 123));
			inputStock.setMaximumSize(new Dimension(123, 123));

			JButton addButton = new JButton("Enter");
			//Button used to make sure that the items can be added
			addButton.addActionListener(a ->{
				try {
					food[nextFood] = inputName.getText();
					foodPrice[nextFood] = Double.parseDouble(inputPrice.getText());
					foodStock[nextFood] = Integer.valueOf(inputStock.getText());

					nextFood++;

					parentPanel.remove(update);
					parentPanel.removeAll();
					parentPanel.setSize(50,50);
					parentPanel.revalidate();
					parentPanel.repaint();

					manage();

				}catch(Exception e){
					askAdd.setText("Invalid response. TRY AGAIN");
				}
			});

			GridLayout layout = new GridLayout(6,1);


			askAdd.setMaximumSize(new Dimension(600,50));
			inputName.setMaximumSize(new Dimension(600, 50));
			inputPrice.setMaximumSize(new Dimension(300,50));
			inputStock.setMaximumSize(new Dimension(300,50));

			update.add(askAdd);
			update.add(inputName);
			update.add(inputPrice);
			update.add(inputStock);
			update.add(addButton);
			update.add(exit);
			update.setLayout(layout);
			parentPanel.add(update);
		}
		setSize(400,400);

		return update;
	}

	//Manager Update Inventory
	//Displays the names, stock, and prices for certain items
	public JPanel manUpdate() {
		parentPanel.remove(manageBut);
		parentPanel.remove(manageMenu);
		parentPanel.revalidate();
		parentPanel.repaint();

		parentPanel.setSize(500,200);
		JPanel update = new JPanel();
		JLabel instruction = new JLabel("Please select the item to change its inventory.");
		JComboBox<?> foodList = new JComboBox<>(food);
		foodList.setSelectedIndex(nextFood);
		foodList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setSize(750,200);
				int temp = (int) foodList.getSelectedIndex();
				try {
					instruction.setText("What is the current stock of "+food[temp]+"?");
					update.remove(foodList);
					JTextField input= new JTextField("", 30);
					JButton inputButton = new JButton("Submit");
					inputButton.addActionListener(a ->{
						try {
							foodStock[temp] = Integer.parseInt(input.getText());
							//Removes Everything
							parentPanel.removeAll();
							parentPanel.revalidate();
							parentPanel.repaint();
							manage();
						}catch(Exception E){
							instruction.setText("Invalid! Enter an integer value!");
						}
					});
					update.add(input);
					update.add(inputButton);
					update.revalidate();
				}catch(Exception ArrayIndexOutOfBoundsException) {//If user selected a blank
					instruction.setText("DO NOT SELECT A BLANK!");
				}

			}

		});
		update.add(instruction);
		update.add(foodList);
		parentPanel.add(update);
		parentPanel.revalidate();
		return update;
	}

	//Customer Menu buttons
	//The buttons used for selection in the customer menu
	public JPanel custBut(){
		custBut = new JPanel();
		custBut.setBackground(Color.white);
		FlowLayout flow = new FlowLayout();
		custBut.setLayout(flow);

		//Button to display products
		display = new JButton("Display Products");
		display.setBackground(Color.blue);
		display.setForeground(Color.white);
		display.addActionListener(e -> {
			custDisplay();
		});
		//Button to add to cart
		JButton addCart = new JButton("Add to cart");
		addCart.setBackground(Color.cyan);
		addCart.addActionListener(e ->{
			addCart();
		});

		//Button to checkout with the items in the cart
		JButton checkOut = new JButton("Checkout");
		checkOut.setBackground(Color.pink);
		checkOut.addActionListener(e ->{
			checkOutConfirm();
		});

		//Button to refund the items from a reciept
		JButton refund = new JButton("Refund");
		refund.setBackground(Color.green);
		refund.addActionListener(e ->{
			refund();
		});

		//Returns to the main menu
		JButton exitCust = new JButton("Exit to main menu");
		exitCust.setBackground(Color.red);
		exitCust.setForeground(Color.white);
		exitCust.addActionListener(e ->{
			//The theftCounter is used in order to make sure that all items are checked out or revenue might be incorrectly calculated
			for(int i = 0; i < cart.length; i++) {
				//If items are in the cart, it will not let the customer leave the menu
				if(cart[i] != 0) {
					custMessage.setText("There are still items in the cart. Please checkout.");
					theftCounter++;//Increases the theft counter so the user can't leave the menu
					break;
				}else {
				}//End of If Else verification
			}//End of For Loop
			if(theftCounter == 0) {
				parentPanel.remove(custBut);
				parentPanel.remove(custMenu());
				parentPanel.revalidate();
				parentPanel.repaint();
				try {
					introPane();
				}catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//Checks the TheftCounter
			}//End of Try Catch
		});//End of ActionListener
		custBut.add(addCart);
		custBut.add(display);
		custBut.add(checkOut);
		custBut.add(refund);
		custBut.add(exitCust);

		return custBut;
	}

	//Customer Menu
	//The basic selection screen for the customer, allowing them to move to 'add to cart', 'customer display', 'checkout', 'refund', and 'return to main menu'
	public JPanel custMenu() {
		parentPanel.remove(introBut);
		parentPanel.remove(introPane);
		parentPanel.revalidate();
		parentPanel.repaint();

		custMenu = new JPanel();
		custMenu.setBackground(Color.white);
		custMenu.setLayout(new GridLayout(2,1));

		custMessage = new JLabel("Welcome to DelivFood customer menu.", JLabel.CENTER);
		custMenu.add(custMessage);
		custMenu.add(custBut());


		custMenu.add(custBut);
		parentPanel.add(custMenu);
		parentPanel.setBackground(Color.cyan);
		setTitle("delivfood Customer Menu.exe");
		pack();

		return custMenu;
	}

	//Customer menu display menu
	//Displays the food and prices for the customer
	public JPanel custDisplay() {
		parentPanel.remove(custBut);
		parentPanel.remove(custMenu);
		parentPanel.revalidate();
		parentPanel.repaint();

		JPanel display = new JPanel();
		JPanel title = new JPanel();

		display.setBackground(Color.white);

		GridLayout layout = new GridLayout(nextFood+4, 2);

		JLabel nameLabel[] = new JLabel[100];
		JLabel priceLabel[] = new JLabel[100];

		JLabel menu1 = new JLabel("DelivFood", JLabel.CENTER);
		JLabel menu2 = new JLabel("Menu", JLabel.CENTER);
		JLabel name = new JLabel("Name:", JLabel.CENTER);
		JLabel price = new JLabel("Price:", JLabel.CENTER);

		display.add(menu1);
		display.add(menu2);
		display.add(name);
		name.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		display.add(price);
		price.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		//Creates a grid for easy viewing
		display.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
		parentPanel.setBorder(new MatteBorder(2, 2, 2, 2, Color.CYAN));
		for(int i = 0; i < food.length; i++){
			if (food[i] == null  || foodPrice[i] == 0){
			} else {
				nameLabel[i] =new JLabel(food[i], JLabel.CENTER);
				nameLabel[i].setText(food[i]);
				nameLabel[i].setLayout(layout);
				nameLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				display.add(nameLabel[i]);

				priceLabel[i] =new JLabel("", JLabel.CENTER);
				priceLabel[i].setText(String.valueOf(String.format("%1.02f", +foodPrice[i])));
				priceLabel[i].setLayout(layout);
				priceLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				display.add(priceLabel[i]);
			}
		}
		//Button to exit
		JButton buttonDis = new JButton("Exit");
		buttonDis.addActionListener(e -> {
			parentPanel.removeAll();
			parentPanel.revalidate();
			parentPanel.repaint();
			custMenu();
		});
		display.add(buttonDis);
		setSize(610,400);	
		display.setLayout(layout);
		parentPanel.add(display);
		pack();
		return display;
	}

	//Customer refund menu
	//If the user has a receipt, this will allow the user to commence a refund with the receipt and make the receipt turn void
	public JPanel refund() {
		parentPanel.remove(custBut);
		parentPanel.remove(custMenu);
		parentPanel.revalidate();
		parentPanel.repaint();

		JPanel refund = new JPanel();
		refund.setBackground(Color.WHITE);

		JPanel exitP = new JPanel();
		exitP.setBackground(Color.WHITE);

		setSize(750,300);
		GridLayout gLayout = new GridLayout(3,2);
		refund.setLayout(gLayout);

		//Procedure to return an item
		JLabel instruction = new JLabel("Please enter the number of the reciept.", JLabel.CENTER);
		JLabel example = new JLabel("Eg.(delivfood Reciept Order [number])", JLabel.CENTER);
		JTextField name = new JTextField("", 30);
		JButton button = new JButton("Enter");
		JLabel image = new JLabel(new ImageIcon("refund.gif"));
		JLabel exitInstr = new JLabel("Click the button on the right to exit");
		JButton exit = new JButton("Exit");
		int returnDate = (int) (Math.random() * ((30 - 1) + 1)) + 1;//Randomizes the date on when it should be refunded with a range of 1-30
		button.addActionListener(a ->{
			String num = name.getText();
			try {
				File file = new File("delivfood Reciept Order "+num+".txt");
				Scanner fileReader = new Scanner(file);
				String totalString = fileReader.nextLine();
				try {
					double total2 = Double.parseDouble(totalString);
					PrintWriter print = new PrintWriter(file);
					String stockReturn = fileReader.nextLine();
					for(int i = 0; i < nextFood; i++) {
						String[] returns = stockReturn.split("-");
						try {
							foodStock[i] = foodStock[i]+Integer.parseInt(returns[i]);
						}catch(Exception ARRAYOUTOFBOUNDSEXCEPTION) {
						}
					}

					//Changes the text so the reciept can't be refunded twice
					print.println("RECIEPT NUMBER "+num+"- ALL ITEMS RETURNED. THIS RECIEPT IS VOID.");
					revenue = revenue - total2;

					print.close();

					exitP.removeAll();
					refund.removeAll();
					refund.revalidate();
					refund.repaint();

					GridLayout finalRefund = new GridLayout(5,1);
					parentPanel.setBackground(Color.white);
					JLabel refundMessage = new JLabel("The return will occur in "+returnDate+" days.");
					JLabel custSupport = new JLabel("If the refund doesn't occur, please contact us.");
					JLabel bye = new JLabel("Have a nice day!");
					JButton thanks = new JButton("Thanks!");
					refund.add(refundMessage);
					refund.add(custSupport);
					refund.add(bye);
					refund.add(thanks);
					pack();

					thanks.addActionListener(e ->{
						parentPanel.removeAll();
						parentPanel.revalidate();
						parentPanel.repaint();
						custMenu();
					});
					//Error if the receipt is voided
				}catch(Exception NumberFormatException) {
					example.setText("Eg.(delivfood Reciept Order [number])");
					exitInstr.setText("This reciept is voided and cannot be used. Please exit or select another one.");
				}

				//Error if no receipts match the number
			}
			catch(IOException e){
				example.setText("No valid reciepts are found. Please try again.");
				setLocation(200,200);
				setSize(700,200);
				pack();
			}

		});
		exit.addActionListener(e ->{
			parentPanel.removeAll();
			parentPanel.revalidate();
			parentPanel.repaint();
			custMenu();
		});
		parentPanel.setBackground(Color.white);
		refund.add(instruction);
		refund.add(example);
		refund.add(name);
		refund.add(button);
		exitP.add(exitInstr);
		exitP.add(exit);
		exitP.add(image);

		parentPanel.add(refund);
		parentPanel.add(exitP);
		return refund;
	}

	//Customer adding cart menu
	//This method will be the procedure for the user to add items to their cart
	public JPanel addCart() {
		parentPanel.remove(custBut);
		parentPanel.remove(custMenu);
		parentPanel.revalidate();
		parentPanel.repaint();

		JPanel cart = new JPanel();
		cart.setBackground(Color.white);
		JLabel label = new JLabel("");

		JLabel nameLabel[] = new JLabel[100];
		JLabel priceLabel[] = new JLabel[100];
		JButton cartAdd[] = new JButton[11];
		JButton cartSubtract[] = new JButton[11];

		JLabel blank1 = new JLabel("");
		JLabel menu = new JLabel("DelivFood menu", JLabel.CENTER);
		JLabel menu2 = new JLabel("Click to add to Cart!", JLabel.CENTER);
		JLabel blank2 = new JLabel("");

		JLabel name = new JLabel("Name:", JLabel.CENTER);
		JLabel stock = new JLabel("Price:", JLabel.CENTER);	
		JLabel price = new JLabel("Add", JLabel.CENTER);
		JLabel remove = new JLabel("Remove", JLabel.CENTER);
		JLabel outError = new JLabel("Nothing is out of stock!", JLabel.CENTER);

		GridLayout layout = new GridLayout(nextFood+4, 4);
		cart.setLayout(layout);

		cart.add(blank1);
		cart.add(menu);
		cart.add(menu2);
		cart.add(blank2);

		cart.add(name);
		cart.add(stock);
		cart.add(price);
		cart.add(remove);

		//Creates the layout to add the items
		cart.setLayout(layout);
		cart.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
		parentPanel.setBorder(new MatteBorder(2, 2, 2, 2, Color.CYAN));
		for(int i = 0; i < food.length; i++){
			if (food[i] == null  || foodStock[i] == 0 ){
			} else {
				nameLabel[i] =new JLabel(food[i], JLabel.CENTER);
				nameLabel[i].setText(food[i]);
				nameLabel[i].setLayout(layout);
				nameLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				cart.add(nameLabel[i]);

				priceLabel[i] =new JLabel((""), JLabel.CENTER);
				priceLabel[i].setText(String.valueOf(String.format("%1.02f", +foodPrice[i])));
				priceLabel[i].setLayout(layout);
				priceLabel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				cart.add(priceLabel[i]);

				//Creation of the buttons to add items to cart
				cartAdd[i] = new JButton("Add "+food[i]+ " to cart");
				cartAdd[i].addActionListener(e ->{
					for(int a = 0; a < cartAdd.length; a++) {
						if(e.getSource() == cartAdd[a]) {
							if(foodStock[a] <= 0) {
								outError.setText(food[a]+" is out of stock!");
							}
							else {
								outError.setText("Nothing is out of stock!");
								try {
									this.cart[a]++;
								}catch(Exception ArraysOutOfBoundsException) {//If items in cart reach greater than limit
									parentPanel.remove(label);
									parentPanel.remove(cart);
									parentPanel.revalidate();
									parentPanel.repaint();

									JLabel cartError = new JLabel("You cannot add anymore items to cart!");
									JButton errButton = new JButton("Okay.");
									errButton.addActionListener(b ->{
										parentPanel.revalidate();
										parentPanel.repaint();
										custMenu();
									});
									parentPanel.add(cartError);
									parentPanel.add(errButton);
								}
								cartCounter++;
								total = total + foodPrice[a];
								revenue = foodPrice[a] + revenue;
								foodStock[a]--;
								break;
							}
						}
					}
				});

				//Creates a border layout for easy removal and adding
				cartAdd[i].setLayout(layout);
				cartAdd[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				cart.add(cartAdd[i]);

				//Creation of buttons to remove items from the cart
				cartSubtract[i] = new JButton("Remove "+food[i]+ " from cart");
				cartSubtract[i].addActionListener(e ->{
					for(int a = 0; a < cartSubtract.length; a++) {
						if(e.getSource() == cartSubtract[a]) {
							if(this.cart[a] <= 0) {
								outError.setText("You don't have this item in the cart!");
							}else {
								this.cart[a]--;
								revenue = revenue - foodPrice[a];
								total = total - foodPrice[a];
								outError.setText("Nothing is out of stock!");
								cartCounter--;
								foodStock[a]++; 
								pack();
								break;
							}
						}
					}
				});
				cartSubtract[i].setLayout(layout);

				cartSubtract[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				cart.add(cartSubtract[i]);
			}
		}
		JButton buttonExit = new JButton("Exit");
		buttonExit.addActionListener(e -> {
			parentPanel.remove(label);
			parentPanel.remove(cart);
			parentPanel.revalidate();
			parentPanel.repaint();
			custMenu();
		});

		cart.add(buttonExit);
		cart.add(outError);
		setSize(800,400);	
		parentPanel.add(cart);
		pack();
		return cart;

	}//End of adding to cart

	//Customer confirmation of checkout(Part 1/3 of checkout)
	//This part will just make sure that the customer is ready to checkout and ensure that there are items in the cart
	public JPanel checkOutConfirm(){
		parentPanel.remove(custBut);
		parentPanel.remove(custMenu);
		parentPanel.revalidate();
		parentPanel.repaint();

		JPanel check = new JPanel();
		check.setBackground(Color.WHITE);
		check.setSize(600,100);
		parentPanel.setSize(600,100);

		//Confirmation of the checkout procedure
		JLabel confirm = new JLabel("Are you sure that you want to check out?");
		JButton yes = new JButton("Yes");
		JButton no = new JButton("No");

		yes.addActionListener(e ->{
			//If there is nothing in the cart
			if(cartCounter == 0){
				confirm.setText("You have nothing in the cart! Please add something and then try again!");
				check.remove(yes);
				no.setText("Okay");
				revalidate();
				repaint();
				pack();

				//If there are items in the cart, it will allow the user to go select the province for their tax
			}else {
				tax();
			}
		});

		no.addActionListener(e ->{
			parentPanel.remove(check);
			parentPanel.revalidate();
			parentPanel.repaint();
			custMenu();
		});

		check.add(confirm);
		check.add(yes);
		check.add(no);
		parentPanel.add(check);
		pack();
		return check;
	}//End of Confirmation of Check Out

	//Customer tax selection(Part 2/3 of checkout)
	//This part is a succession of part 1 and will ensure that the user can select the province/territory that they are residing in and output the correct tax
	public JPanel tax(){
		parentPanel.removeAll();
		parentPanel.revalidate();
		parentPanel.repaint();

		setSize(650, 200);
		JPanel taxPanel = new JPanel();
		taxPanel.setBackground(Color.WHITE);

		FlowLayout flow = new FlowLayout();
		taxPanel.setLayout(flow);

		//Makes the user input the province or territory
		JLabel province = new JLabel("Please select your province or territory.");
		taxPanel.add(province);

		String provinceArray[] = {"Alberta", "British Columbia", "Manitoba", "New Brunswick", "Newfoundland and Labrador", "Northwest Territories", "Nova Scotia", "Nunavut", "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan", "Yukon"};

		//Implements the drop down bar to select the province
		JComboBox<?> provinceList = new JComboBox(provinceArray);
		provinceList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setSize(550,200);
				JButton enter = new JButton("Enter");
				taxPanel.add(enter);
				enter.addActionListener(a ->{
					String provinceName = String.valueOf(provinceList.getSelectedItem());
					if(provinceName == "Alberta" || provinceName == "Northwest Territories" || provinceName == "Nunavut" || provinceName == "Yukon") {
						tax = 1.05;
						parentPanel.removeAll();
						parentPanel.revalidate();
						parentPanel.repaint();
						checkOut();
					}else if (provinceName == "British Columbia" || provinceName == "Manitoba") {
						tax = 1.12;
						parentPanel.removeAll();
						parentPanel.revalidate();
						parentPanel.repaint();
						checkOut();
					}else if(provinceName == "New Brunswick" || provinceName == "Newfoundland and Labrador" ||provinceName == "Nova Scotia") {
						tax = 1.15;
						parentPanel.removeAll();
						parentPanel.revalidate();
						parentPanel.repaint();
						checkOut();
					}else if(provinceName == "Ontario") {
						tax = 1.13;
						parentPanel.removeAll();
						parentPanel.revalidate();
						parentPanel.repaint();
						checkOut();
					}else if(provinceName == "Quebec") {
						tax = 1.14975;
						parentPanel.removeAll();
						parentPanel.revalidate();
						parentPanel.repaint();
						frenchOut();
					}else {//Saskatchewan
						tax = 1.11;
						parentPanel.removeAll();
						parentPanel.revalidate();
						parentPanel.repaint();
						checkOut();
					}
				});

			}
		});

		taxPanel.add(provinceList);
		taxPanel.setBackground(Color.pink);
		parentPanel.setBackground(Color.white);
		parentPanel.add(taxPanel);
		return taxPanel;
	}

	//Checking out the items(Part 3/3 of checkout)
	//This is the last part in the checkout procedure to create the receipt and restart the purchasing for the next user. 
	public JPanel checkOut(){
		JPanel items = new JPanel();

		items.setBackground(Color.white);
		JPanel flow = new JPanel();
		flow.setBackground(Color.white);

		JButton end = new JButton("Exit");
		end.addActionListener(e ->{
			parentPanel.removeAll();
			parentPanel.revalidate();
			parentPanel.repaint();
			custMenu();
		});

		GridLayout gLayout = new GridLayout(cartCounter+2, 2);
		items.setLayout(gLayout);

		FlowLayout fl = new FlowLayout();
		flow.setLayout(fl);

		JLabel statement = new JLabel("You have bought these following items:");
		JLabel[] read = new JLabel[11];

		items.add(statement);
		for(int i = 0; i <= nextFood; i++) {
			if(cart[i] == 0) {
			}else {
				read[i] = new JLabel(food[i]+" || Stock: "+Integer.toString(cart[i]));
				read[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				read[i].setLayout(gLayout);
				items.add(read[i]);
			}
		}
		//Lets user enter the address to deliver the products to 
		JLabel locQuestion = new JLabel("Please enter the address to deliver this to.");
		JTextField location = new JTextField("", 30);
		JButton address = new JButton("Enter");

		flow.add(items);

		flow.add(locQuestion);
		flow.add(location);
		flow.add(address);
		revalidate();
		repaint();
		pack();

		address.addActionListener(e ->{
			String send = location.getText();

			flow.remove(items);
			flow.remove(locQuestion);
			flow.remove(location);
			flow.remove(address);
			revalidate();
			repaint();
			setSize(1000,300);
			double grandTotal = (double) Math.round(((total*tax)*100))/100;//Grand Total
			double taxAmount  = (double) Math.round(((total*(tax-1))*100))/100;//Tax owed
			JLabel totalEnding = new JLabel((String.format("The total of your items is $ %1.02f" ,+grandTotal)+". To see a more detailed report, please look at your reciept. Your reciept number is "+txtNum+". Thank you for ordering at delivfood!"));
			JLabel thanks = new JLabel(new ImageIcon("thanks.gif"));
			write(grandTotal, txtNum, cartCounter, cart, send, taxAmount, total);
			manageDoc();
			setLocation(200, 220);
			flow.add(totalEnding);
			flow.add(end);
			flow.add(thanks);
			pack();
			for (int i = 0; i < cart.length; i++) {// Makes the cart empty
				cart[i] = 0;
			}
			total = 0;
			grandTotal = 0;// Makes the taxTotal equal to 0 for the next user
			cartCounter = 0;// Turns the counter to zero in order for the user to re-add items to the cart
			theftCounter = 0;//Restarts the theftCounter
		});

		parentPanel.add(flow);
		pack();
		return flow;
	}

	//Checking out the items(Easter Egg for this program!)
	//If the user selects Quebec as their province, the remainder of the procedure will be in French!
	public JPanel frenchOut() {
		JPanel items = new JPanel();

		items.setBackground(Color.white);
		JPanel flow = new JPanel();
		flow.setBackground(Color.white);

		JButton end = new JButton("Sortie");
		end.addActionListener(e ->{
			parentPanel.removeAll();
			parentPanel.revalidate();
			parentPanel.repaint();
			custMenu();
		});

		GridLayout gLayout = new GridLayout(cartCounter+2, 2);
		items.setLayout(gLayout);

		FlowLayout fl = new FlowLayout();
		flow.setLayout(fl);

		JLabel statement = new JLabel("Vous avez acheté les articles suivants:");
		JLabel[] read = new JLabel[11];

		items.add(statement);
		for(int i = 0; i <= nextFood; i++) {
			if(cart[i] == 0) {
			}else {
				read[i] = new JLabel(food[i]+" || : "+Integer.toString(cart[i]));
				read[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				read[i].setLayout(gLayout);
				items.add(read[i]);
			}
		}
		JLabel locQuestion = new JLabel("Veuillez saisir l'adresse à laquelle le livrer.");
		JTextField location = new JTextField("", 30);
		JButton address = new JButton("Entre");

		flow.add(items);

		flow.add(locQuestion);
		flow.add(location);
		flow.add(address);
		revalidate();
		repaint();
		pack();

		address.addActionListener(e ->{
			String send = location.getText();

			flow.remove(items);
			flow.remove(locQuestion);
			flow.remove(location);
			flow.remove(address);
			revalidate();
			repaint();
			pack();

			double grandTotal = (double) Math.round(((total*tax)*100))/100;//Grand Total
			double taxAmount  = (double) Math.round(((total*(tax-1))*100))/100;//Tax owed
			JLabel totalEnding = new JLabel("Le total de vos articles est de $"+grandTotal+". Pour voir un rapport plus détaillé, veuillez consulter votre reçu. Votre numéro de reçu est "+txtNum+". Merci d'avoir commandé chez delivfood!");
			ecrire(grandTotal, txtNum, cartCounter, cart, send, taxAmount, total);
			JLabel thanks = new JLabel(new ImageIcon("thanks.gif"));
			manageDoc();
			flow.add(totalEnding);
			flow.add(thanks);
			flow.add(end);
			pack();
			for (int i = 0; i < food.length; i++) {// Makes the cart empty
				cart[i] = 0;
			}
			total = 0;
			grandTotal = 0;// Makes the taxTotal equal to 0 for the next user
			theftCounter = 0;
			cartCounter = 0;// Turns the counter to zero in order for the user to re-add items to the cart
		});

		parentPanel.add(flow);
		pack();
		return flow;
	}

	//Receipt writer
	//Creates the receipt for the user and can be used in case refunds are needed
	public void write(double price,int num, int counter, int[] contents, String address, double tax, double total) {
		try {
			PrintWriter output = new PrintWriter("delivfood Reciept Order "+num+".txt");
			System.out.println("Name of reciept: delivfood Reciept Order "+num+".txt");

			output.println(total);
			for(int i = 0; i < nextFood; i++) {
				output.print(cart[i]+"-");
			}
			output.println("");
			output.println("===========================");
			output.println("delivfood reciept. Keep this in for all future records or return claims");
			output.println("WARNING: Any tampering with this reciept can lead to prosecution or refusal of delivfood services. DO NOT TAMPER WITH THIS RECIEPT.");
			output.println("");
			output.println("Reciept number "+num);
			output.println("===========================");
			output.println("Items bought:");
			for(int i = 0 ; i < 10; i++) {
					output.println(food[i]+" bought: "+contents[i]);
				}

			output.println("===================");
			output.println((String.format("Total of items bought: $%1.02f", +total)));
			output.println("Tax: $"+tax);
			output.println((String.format("Grand total $%1.02f:", +price)));

			output.println("===============================");
			output.println("These items are sent to "+address);
			output.println("Thank you for shopping at delivfood!"); 
			output.println("================================");
			output.println("                       					©delivfood");
			output.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	//Receipt Writer French Version
	//Creates the receipt in FRENCH for the user(If they selected Quebec as their province) and can be used in case refunds are needed
	public void ecrire(double price,int num, int counter, int[] contents, String address, double tax, double total) {
		try {
			PrintWriter output = new PrintWriter("delivfood Reciept Order "+num+".txt");
			System.out.println("Name of reciept: delivfood Reciept Order "+num+".txt");

			output.println(total);
			for(int i = 0; i < nextFood; i++) {
				output.print(cart[i]+"-");
			}
			output.println("");
			output.println("===========================");
			output.println("delivfood reciept. Conservez-le dans tous les dossiers futurs ou retournez les réclamations");
			output.println("AVERTISSEMENT: Toute altération de ce reçu peut entraîner des poursuites ou le refus des services de Delivfood. NE PAS GAMMER AVEC CE RÉCEPTEUR.");
			output.println("");
			output.println("Numéro de reçu "+num);
			output.println("===========================");
			output.println("Articles achetés:");
			for(int i = 0 ; i < 10; i++) {
				if(food[i] == null) {
				}else {
					output.println(food[i]+" acheté: "+contents[i]);
				}
			}
			output.println("===================");
			output.println(String.format("Totals des articles achetes $%1.02f:", +total));
			output.println("Taxe: $"+tax);
			output.println(String.format("Somme finale: $%1.02f:", +price));

			output.println("===============================");
			output.println("Ces articles sont envoyés à "+address);
			output.println("Merci de magasiner chez delivfood!"); 
			output.println("================================");
			output.println("                       					©delivfood");
			output.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	//Constructor
	public delivFood_V2() throws IOException {
		extractManage();
		parentPanel = new JPanel();
		parentPanel.setBackground(Color.white);
		parentPanel.add(introPane());
		setResizable(false);//Makes sure the whole thing cannot be resized
		setVisible(true);//Makes the whole program visible
	}

	//Main Method 
	public static void main(String[] args) throws Exception{
		food[0] = "Le Burger";
		food[1] = "PotatFries";
		food[2] = "Limonade";
		food[3] = "Hero-Hola";
		food[4] = "Popsi";
		food[5] = "SkottlePie";
		food[6] = "Clake";

		// Food prices of these foods:
		foodPrice[0] = 4;// Le Burger
		foodPrice[1] = 3;// PotatFries
		foodPrice[2] = 2;// Limonade
		foodPrice[3] = 1;// Hero-Hola
		foodPrice[4] = 1;// Popsi
		foodPrice[5] = 1.50;// SkottlePie
		foodPrice[6] = 2;// Clake

		// Original stocks of these foods:
		foodStock[0] = 10;// Le Burger
		foodStock[1] = 20;// PotatFriesz
		foodStock[2] = 50;// Limonade
		foodStock[3] = 100;// Hero-Hola
		foodStock[4] = 100;// Popsi
		foodStock[5] = 15;// SkottlePie
		foodStock[6] = 5;// Clake

		new delivFood_V2();
	}

}
