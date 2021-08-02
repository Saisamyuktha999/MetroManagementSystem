package metro.service;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

import metro.bean.Card;
import metro.bean.Station;
import metro.bean.TransactionHistory;
import metro.client.DisplayExceptions;
import metro.persistence.MetroDao;
import metro.persistence.MetroDaoInterface;

public class MetroService implements MetroServiceInterface{

	MetroDaoInterface metroDao = new MetroDao();
	DisplayExceptions display = new DisplayExceptions();
	
	@Override
	public Collection<TransactionHistory> getTransactionDetails(int cardId)
			throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Collection<TransactionHistory> transactionHistory = new ArrayList<TransactionHistory>();
		transactionHistory = metroDao.getTransactionDetails(cardId);
		return transactionHistory;
	}

	@Override
	public Collection<Card> getCardDetails(int cardId) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Collection<Card> card = new ArrayList<Card>();
		card = metroDao.getCardDetails(cardId);
		return card;
	}


	@Override
	public boolean swipeIn(int cardId, int sourceId) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		boolean status =false;
		if(metroDao.cardExists(cardId)==true)
		{
			if(metroDao.stationExists(cardId)==true) {
			 status = metroDao.swipeIn(cardId,sourceId);
			}
			else
			{
				display.noStation();
			}
        }
        return status;
	
	}

	@Override
	public boolean swipeOut(int cardId, int destinationId) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		int fare=0;
		int sourceid=0;
		int transid=0;
		Collection<TransactionHistory> lastTransaction = new ArrayList<TransactionHistory>();
		lastTransaction = metroDao.getLastTransaction(cardId);
		for(TransactionHistory trans: lastTransaction)
		{
			transid=trans.getTransactionId();
			fare=trans.getFare();
			sourceid = trans.getSourceId();
		}
		
		if(fare!=0) {
			if(sourceid != 0)
			{
				fare=(destinationId-sourceid)*5;
			}
		}
		else
		{
			display.transactionException();
		}
		boolean status = metroDao. updateTransactionHistory(transid,cardId,sourceid,destinationId,fare);
		Collection<Card> card = new ArrayList<Card>();
		if(status==true)
		{
			card = metroDao.getCardDetails(cardId);
			for(Card c:card)
			{
				int balance = c.getBalance();
				if(balance-fare>20)
				{
					balance=balance-fare;
					boolean temp = metroDao.updateBalance(cardId, balance);
					if(temp==false)
					{
						display.updateBalanceException();
					}
				}
				else
				{
					display.insufficientBalance();
				}
			}
		}
        return status;
	}

	@Override
	public Collection<Station> getStationDetails() throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Collection<Station> stations = new ArrayList<Station>();
		stations = metroDao.getStationDetails();
		return stations;
	}

	@Override
	public Collection<Card> createNewCard() throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		boolean status = metroDao.createNewCard();
        if(status==true)
        {
        	return metroDao.newIssueCard();
        }
        return null;
	}

	@Override
	public boolean cardExists(int cardId) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return metroDao.cardExists(cardId);
	}

	@Override
	public int rechargeBalance(int cardId, int amount) {
		int bal = 0;
		// TODO Auto-generated method stub
		if(amount>0)
		{
			try {
				bal = rechargeCard(cardId, amount);
			} catch (ClassNotFoundException | SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return bal;
			
		}
		return 0;
	}
	
	public int rechargeCard(int cardId, int amount) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Collection<Card> card = new ArrayList<Card>();
		card = getCardDetails(cardId);
        int balance =-1;
		for(Card card1:card)
        {
            balance = card1.getBalance();
        }
        balance=balance+amount;
        boolean status = metroDao.updateBalance(cardId,balance);
       	if(status==true)
       		return balance;
       	return -1;
        
	}

}
