package metro.persistence;

import java.sql.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import metro.bean.Card;
import metro.bean.Station;
import metro.bean.TransactionHistory;
import metro.client.MetroClient;
import metro.helper.MySqlConnection;

public class MetroDao implements MetroDaoInterface {

	@Override
	public Collection<TransactionHistory> getTransactionDetails(int cardId)
			throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		Connection connection = MySqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT t.transactionid, t.cardid, t.sourceid, t.destinationid, t.fare, t.swipeintime, t.swipeouttime FROM transactionhistory t WHERE card_id = ? ORDER BY transaction_id DESC");
        preparedStatement.setInt(1, cardId);
        ResultSet resultSet = preparedStatement.executeQuery();
        Collection<TransactionHistory> transactions = new ArrayList<TransactionHistory>();
        while(resultSet.next())
        {
        	TransactionHistory th = new TransactionHistory();
        	th.setCardId(resultSet.getInt("cardid"));
        	th.setTransactionId(resultSet.getInt("transactionid"));
        	th.setSourceId(resultSet.getInt("sourceid"));
        	th.setDestinationId(resultSet.getInt("destinationid"));
        	th.setSwapInTime(resultSet.getString("swipeintime"));
        	th.setSwapOutTime(resultSet.getString("swipeOutTime"));
        	th.setFare(resultSet.getInt("fare"));
        	
        	transactions.add(th);
        	
        }
        connection.close();
        return transactions;
	}

	@Override
	public Collection<Card> getCardDetails(int cardId) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Connection connection = MySqlConnection.getConnection();

        Collection<Card> cardDetails = new ArrayList<Card>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CARDDETAILS WHERE CARDID=?");
        preparedStatement.setInt(1, cardId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next())
        {
            Card card = new Card();
            card.setCardId(resultSet.getInt("CARDID"));
            card.setBalance(resultSet.getInt("BALANCE"));

            cardDetails.add(card);

        }
        connection.close();
        return cardDetails;

	}

	@Override
	public boolean updateBalance(int cardId,int balance) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Connection connection = MySqlConnection.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE CARDDETAILS SET BALANCE =? WHERE CARDID=?");
        preparedStatement.setInt(1, balance);
        preparedStatement.setInt(2, cardId);
        int resultSet = preparedStatement.executeUpdate();
        connection.close();
		if(resultSet>0)
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean swipeIn(int cardId, int sourceId) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
        Connection connection = MySqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TRANSACTIONHISTORY(cardId,sourceId,swipeInTime) VALUES(?,?,SYSDATE(),?)");
        preparedStatement.setInt(1, cardId);
        preparedStatement.setInt(2, sourceId);
 
        int resultSet = preparedStatement.executeUpdate();
        connection.close();
		if(resultSet>0)
		{
			return true;
		}
		return false;
	}

	@Override
	public Collection<Station> getStationDetails() throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Connection connection = MySqlConnection.getConnection();

        Collection<Station> stations = new ArrayList<Station>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM STATION");
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next())
        {
            Station station = new Station();
            station.setStationId(resultSet.getInt("STATIONID"));
            station.setStationName(resultSet.getString("STATIONNAME"));
            stations.add(station);

        }
        connection.close();
        return stations;
		
	}

	@Override
	public boolean createNewCard() throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Connection connection = MySqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CARDDETAILS(balance,issueDate) VALUES(?,SYSDATE())");
        preparedStatement.setInt(1, 100);

        int rows = preparedStatement.executeUpdate();
        connection.close();
        return (rows>0);
		
	}

	@Override
	public boolean updateTransactionHistory(int transid,int cardId,int sourceid, int destinationId, int fare)
			throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Connection connection = MySqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE transactionHistory SET fare = ?,destinationId=? WHERE transaction_id = ?");
        preparedStatement.setInt(1, fare);
        preparedStatement.setInt(2, destinationId);
        preparedStatement.setInt(3, transid);
        int affectedRows = preparedStatement.executeUpdate();
        connection.commit();
        return affectedRows > 0;
		
	}

	@Override
	public boolean cardExists(int cardId) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Connection connection = MySqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT CARDID FROM CARDDETAILS WHERE CARDID=?");
        preparedStatement.setInt(1, cardId);

        ResultSet result = preparedStatement.executeQuery();
        connection.close();
        if(result != null) {
        	return true;
        }
        return false;
	}

	@Override
	public boolean stationExists(int stationId) throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Connection connection = MySqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM STATION WHERE STATIONID=?");
        preparedStatement.setInt(1, stationId);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        connection.close();
        return resultSet.next();
	}

	@Override
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
        boolean status = updateBalance(cardId,balance);
       	if(status==true)
       		return balance;
       	return -1;
        
	}

	@Override
	public Collection<Card> newIssueCard() throws SQLException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		Collection<Card> newCard = new ArrayList<Card>();
		Connection connection = MySqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CARDDETAILS ORDER BY CARDID DESC LIMIT 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next())
        {
        	Card c = new Card();
        	c.setCardId(resultSet.getInt("cardid"));
        	c.setCardId(resultSet.getInt("balance"));
        	newCard.add(c);
        }
        connection.close();
		return newCard;
	}

	@Override
	public Collection<TransactionHistory> getLastTransaction(int cardId)
			throws ClassNotFoundException, SQLException, IOException {
		// TODO Auto-generated method stub
		Connection connection = MySqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT t.transactionid, t.cardid, t.sourceid, t.destinationid, t.fare, t.swipeintime, t.swipeouttime FROM transactionhistory t WHERE card_id = ? ORDER BY transaction_id DESC LIMIT 1");
        preparedStatement.setInt(1, cardId);
        ResultSet resultSet = preparedStatement.executeQuery();
        Collection<TransactionHistory> transactions = new ArrayList<TransactionHistory>();
        while(resultSet.next())
        {
        	TransactionHistory th = new TransactionHistory();
        	th.setCardId(resultSet.getInt("cardid"));
        	th.setTransactionId(resultSet.getInt("transactionid"));
        	th.setSourceId(resultSet.getInt("sourceid"));
        	th.setDestinationId(resultSet.getInt("destinationid"));
        	th.setSwapInTime(resultSet.getString("swipeintime"));
        	th.setSwapOutTime(resultSet.getString("swipeOutTime"));
        	th.setFare(resultSet.getInt("fare"));
        	
        	transactions.add(th);
        	
        }
        connection.close();
        return transactions;
	
	}

}
