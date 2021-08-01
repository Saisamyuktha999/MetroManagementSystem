package metro.presentation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import metro.bean.Card;
import metro.bean.Station;
import metro.bean.TransactionHistory;
import metro.client.MetroClient;
import metro.service.MetroService;
import metro.service.MetroServiceInterface;

public class MetroPresentation implements MetroPresentationInterface{
	MetroServiceInterface metroService = new MetroService();
	MetroClient metroClient = new MetroClient();
	@Override
	public void showOptions() {
		// TODO Auto-generated method stub
		System.out.println("1. Swipe in");
        System.out.println("2. Swipe out");
        System.out.println("3. Check balance");
        System.out.println("4. View Transaction Details");
        System.out.println("5. Recharge Card");
        System.out.println("6. Exit");
		
	}

	@Override
	public void performOption(int choice) {
		
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
        switch(choice)
        {
            case 1:
            	Collection<Station> stations = new ArrayList<Station>();
                System.out.println("Enter cardId:");
                int id = scanner.nextInt();
			try {
				stations = metroService.getStationDetails();
			} 
			catch (ClassNotFoundException | SQLException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
                metroClient.displayStations(stations);
                System.out.println("Enter Source Station Id: ");
                int source = scanner.nextInt();
                try{
                    boolean status = metroService.swipeIn(id,source);
                    if(status==true)
                    {
                        System.out.println("Swipe in successfull.");
                    }
                    else
                    {
                        System.out.println("Enter valid data");
                    }
                }catch(SQLException| IOException| ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                break;
            case 2:
            	Collection<Station> stations1 = new ArrayList<Station>();
            	System.out.println("Enter cardId:");
            	int id1 = scanner.nextInt();
            	try {
            	stations1 = metroService.getStationDetails();
            	}catch(SQLException| IOException| ClassNotFoundException e)
            	{
            		e.printStackTrace();
            	}
            	metroClient.displayStations(stations1);
                System.out.println("Enter Destination Station: ");
                int destination = scanner.nextInt();
                try{
                    boolean status = metroService.swipeOut(id1,destination);
                    if(status==true)
                    {
                        System.out.println("Swipe out successfull. Balance Updated");
                    }
                    else
                    {
                        System.out.println("Swipe out unsuccessfull");
                    }
                }catch(SQLException| IOException| ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                break;
            case 3:
                Collection<Card> card = new ArrayList<Card>();
                System.out.println("Enter card Id:");
                int cardid = scanner.nextInt();
                try{
                    card = metroService.getCardDetails(cardid);
                    for(Card card1:card)
                    {
                        System.out.print(" Balance is "+card1.getBalance());
                    }
                }catch(SQLException| IOException| ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                break;

            case 4:
                System.out.println("Enter cardId:");
                int cardId = scanner.nextInt();
                Collection<TransactionHistory> transactionHistory = new ArrayList<TransactionHistory>();
                try {
                    transactionHistory = metroService.getTransactionDetails(cardId);
                    metroClient.displayTransactions(transactionHistory);
                }catch(SQLException| IOException| ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                break;
            case 5:
                System.out.println("Enter card Id:");
                int cardID = scanner.nextInt();
                if(isCard(cardID)==true)
                {
                    System.out.println("Enter Amount to recharge: ");
                    int amount = scanner.nextInt();
                    int status = metroService.rechargeBalance(cardID,amount);
                    metroClient.balanceStatus(status);
                }
                break;
            case 6:
            	System.out.println("Thank You");
            	break;

        }
		
	}

	@Override
	public void newUser() {
		// TODO Auto-generated method stub
		try {
			Collection<Card> card = new ArrayList<Card>();
            card = metroService.createNewCard();
            for(Card c:card) {
                System.out.println("New Card Generated.");
                metroClient.displayCard(card);
            }
        }catch(SQLException| IOException| ClassNotFoundException e)
        {
            e.printStackTrace();
        }
	}

	@Override
	public boolean isCard(int cardId) {
		// TODO Auto-generated method stub
		boolean status = false;
		try {
			 status = metroService.cardExists(cardId);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

}
