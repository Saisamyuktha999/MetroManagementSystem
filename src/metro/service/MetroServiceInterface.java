package metro.service;

import metro.bean.Card;
import metro.bean.Station;
import metro.bean.TransactionHistory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

public interface MetroServiceInterface {
    public Collection<TransactionHistory> getTransactionDetails(int cardId) throws SQLException, IOException, ClassNotFoundException;
    public Collection<Card> getCardDetails(int cardId) throws SQLException, IOException, ClassNotFoundException;
    public boolean swipeIn(int cardId,int sourceId) throws SQLException, IOException, ClassNotFoundException;
    public boolean swipeOut(int cardId,int destinationId) throws SQLException, IOException, ClassNotFoundException;
    public Collection<Station> getStationDetails() throws SQLException, IOException, ClassNotFoundException;
    public Collection<Card> createNewCard() throws SQLException, IOException, ClassNotFoundException;
    public void showCardException();
    public void showBalanceException();
    public boolean cardExists(int cardId) throws SQLException, IOException, ClassNotFoundException  ;
    public int rechargeBalance(int cardId,int amount);
}
