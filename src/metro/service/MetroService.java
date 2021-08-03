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
		return metroDao.getTransactionDetails(cardId);
	}

	@Override
	public int getBalance(int cardId) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		int balance =0;
		Collection<Card> card = new ArrayList<Card>();
		card = metroDao.getCardDetails(cardId);
		for(Card c : card)
		{
			balance=c.getBalance();
		}
		return balance;
	}


	@Override
	public boolean swipeIn(int cardId, int sourceId) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		boolean status =false;
		 status = metroDao.swipeIn(cardId,sourceId);
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
			fare = trans.getFare();
			sourceid = trans.getSourceId();
		}
		if(fare==0) {
			if(sourceid<destinationId)
			{
				fare=(destinationId-sourceid)*5;
			}
			else
			{
				fare=(sourceid-destinationId)*5;
			}
		}
		else
		{
			display.transactionException();
		}
		int balance = getBalance(cardId);
		if(balance-fare>20)
		{
			balance=balance-fare;
			boolean temp = metroDao.reduceBalance(cardId, balance);
			if(temp==false)
			{
				display.updateBalanceException();
				return false;
			}
		}
		else
		{
			display.insufficientBalance();
			return false;
		}
		boolean status = metroDao. updateTransactionHistory(transid,destinationId,fare);
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
		boolean status = false;
		status = metroDao.cardExists(cardId);
		return status;
	}

	@Override
	public boolean rechargeBalance(int cardId, int amount) throws SQLException, IOException, ClassNotFoundException{
		boolean status = false;
		// TODO Auto-generated method stub
		if(amount>0)
		{
			try {
				status = metroDao.updateBalance(cardId,amount);
			} catch (ClassNotFoundException | SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			display.negativeAmountException();
		}
		return status;
		
	}

}
