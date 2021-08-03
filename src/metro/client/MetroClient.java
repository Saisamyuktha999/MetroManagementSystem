package metro.client;

import metro.bean.Card;
import metro.bean.Station;
import metro.bean.TransactionHistory;
import metro.presentation.MetroPresentation;
import metro.presentation.MetroPresentationInterface;

import java.util.Collection;
import java.util.Scanner;

public class MetroClient {
    public static void main(String[] args){

       
        MetroPresentationInterface metroPresentation=new MetroPresentation();
        Scanner scanner=new Scanner(System.in);
        while(true) {
        System.out.println("1. New User");
        System.out.println("2. Existing User");
        System.out.println("3. Exit");
        System.out.println("Enter your choice: ");
        int choice = scanner.nextInt();
        switch(choice)
        {
            case 1:
            	int userId = metroPresentation.newUser();
            	System.out.println("Your ID: "+userId);
            	while(true)
            	{
            		metroPresentation.showOptions();
            		System.out.println("Enter choice : ");
            		int choice1 = scanner.nextInt();
            		metroPresentation.performOption(userId,choice1);
            		
            	}
          
            case 2:
            	System.out.println("Enter card id:");
            	int id = scanner.nextInt();
            	if(metroPresentation.isCard(id)==true)
            	{
            		while(true)
            		{
            			metroPresentation.showOptions();
            			System.out.println("Enter choice : ");
            			int choice2 = scanner.nextInt();
            			metroPresentation.performOption(id,choice2);
            		}
            	}
            	else
            	{
            		System.out.println("Enter correct cardId");
            	}
            	break;
            case 3:
            	System.out.println("Have a good Day!!");
            	System.exit(0);
            	
        	}
        }
    }
    
    public void displayStations(Collection<Station> stations)
    {
    	for(Station station:stations)
    	{
    		System.out.println(station.getStationId()+"  "+station.getStationName());
    	}
    }
    
    public void displayTransactions(Collection<TransactionHistory> transaction) {
		System.out.println("TransactionId\tCardId\tSourceId\tDestinationId()\tSwapInTime\tOutTime\tFare");

    	for(TransactionHistory trans:transaction) {
    		System.out.println(trans.getTransactionId()+" \t "+trans.getCardId()+" \t "+trans.getSourceId()+" \t "+trans.getDestinationId()+" \t "+trans.getSwapInTime()+" \t "+trans.getSwapOutTime()+" \t "+trans.getFare());
    	}
    }

    public void displayCard(Collection<Card> card)
    {
    	for(Card c:card)
    	{
    		System.out.println("Your new card details:");
    		System.out.println("card Id:"+c.getCardId()+"  "+"Balance: "+c.getBalance());
    	}
    }
}
